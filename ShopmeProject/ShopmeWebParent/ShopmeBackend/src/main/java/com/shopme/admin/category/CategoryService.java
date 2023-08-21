package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        List<Category> rootCategories = categoryRepository.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subcategory : children) {
                String name = "--" + subcategory.getName();
                hierarchicalCategories.add(Category.copyFull(subcategory, name));

                listSubHierarchicalCategories(hierarchicalCategories, subcategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent,
                                               int subLevel,
                                               String sortDir) {

        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subcategory : children) {
            String name = "";

            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }

            name += subcategory.getName();

            hierarchicalCategories.add(Category.copyFull(subcategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subcategory, newSubLevel, sortDir);
        }

    }


    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = categoryRepository.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDb) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

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
        Set<Category> children = sortSubCategories(parent.getChildren());
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


    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicatedName";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicatedAlias";
                }
            }
        } else {  // editing mode
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicatedName";
            }

            Category categoryByAlias = categoryRepository.findByAlias(alias);

            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicatedAlias";
            }
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }


    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {

        SortedSet<Category> sortedChildren = new TreeSet<>((category1, category2) -> {
            if (sortDir.equals("asc")) {
                return category1.getName().compareTo(category2.getName());
            } else {
                return category2.getName().compareTo(category1.getName());
            }
        });

        sortedChildren.addAll(children);
        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public void deleteById(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

}
