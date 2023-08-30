package com.shopme.admin.brand;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class BrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    @Autowired
    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }


    @GetMapping("/brands")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "name", "asc", null);
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") Integer pageNum, Model model,
                             @RequestParam(value = "sortField", required = false) String sortField,
                             @RequestParam(value = "sortDir", required = false) String sortDir,
                             @RequestParam(value = "keyword", required = false) String keyword) {

        Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Brand> brands = page.getContent();

        long startCount = (long) (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
        long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("brands", brands);

        return "brands/brands";
    }


    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        List<Category> categories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("categories", categories);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Category");

        return "brands/brand_form";
    }


    @PostMapping("/brands/save")
    public String saveBrand(Brand brand,
                            RedirectAttributes redirectAttributes,
                            @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            brand.setLogo(fileName);

            Brand savedBrand = brandService.save(brand);
            String uploadDir = "../brand-logos/" + savedBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");

        return "redirect:/brands";
    }


    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable("id") Integer id,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        try {
            Brand brand = brandService.get(id);
            List<Category> categories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");

            return "brands/brand_form";

        } catch (BrandNotFoundException brandNotFoundException) {
            redirectAttributes.addFlashAttribute("message", brandNotFoundException.getMessage());
            return "redirect:/brands";
        }
    }


    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable("id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        try {
            brandService.delete(id);

            String brandDir = "../brand-logos/" + id;
            FileUploadUtil.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message",
                    "The brand with id " + id + " has been deleted successfully");

        } catch (BrandNotFoundException brandNotFoundException) {
            redirectAttributes.addFlashAttribute("message", brandNotFoundException.getMessage());
        }

        return "redirect:/brands";
    }
}
