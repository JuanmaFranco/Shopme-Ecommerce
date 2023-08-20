package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));
            Set<Category> children = rootCategory.getChildren();

            for (Category subcategory : children) {
                String name = "--" + subcategory.getName();
                hierarchicalCategories.add(Category.copyFull(subcategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subcategory, 1);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent,
                                               int subLevel) {

        Set<Category> children = parent.getChildren();
        int newSubLevel = subLevel + 1;

        for (Category subcategory : children) {
            String name = "";

            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subcategory.getName();

            hierarchicalCategories.add(Category.copyFull(subcategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subcategory, newSubLevel);
        }

    }


    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepository.findAll();

        for (Category category : categoriesInDb) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = category.getChildren();

                for (Category subcategory : children) {
                    String name = "--" + subcategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subcategory.getId(), name));

                    listSubcategoriesUsedInForm(categoriesUsedInForm, subcategory, 1);
                }
            }

            System.out.println();
        }

        return categoriesUsedInForm;
    }

    private void listSubcategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subcategory : children) {
            String name = "";

            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subcategory.getName();

            categoriesUsedInForm.add(Category.copyIdAndName(subcategory.getId(), name));

            listSubcategoriesUsedInForm(categoriesUsedInForm, subcategory, newSubLevel);
        }
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new CategoryNotFoundException("Could not find any category with ID: " + id);
        }
    }


}
