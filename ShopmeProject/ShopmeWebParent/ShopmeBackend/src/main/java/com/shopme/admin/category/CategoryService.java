package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepository.findAll();

        for (Category category : categoriesInDb) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(new Category(category.getName()));

                Set<Category> children = category.getChildren();

                for (Category subcategory : children) {
                    String name = "--" + subcategory.getName();
                    categoriesUsedInForm.add(new Category(name));

                    listChildren(categoriesUsedInForm, subcategory, 1);
                }
            }

            System.out.println();
        }

        return categoriesUsedInForm;
    }

    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subcategory : children) {
            String name = "";

            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subcategory.getName();

            categoriesUsedInForm.add(new Category(name));

            listChildren(categoriesUsedInForm, subcategory, newSubLevel);
        }
    }


}
