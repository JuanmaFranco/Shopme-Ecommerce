package com.shopme.admin.brand;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
    public String listAll(Model model) {
        List<Brand> brands = brandService.listAll();
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

}
