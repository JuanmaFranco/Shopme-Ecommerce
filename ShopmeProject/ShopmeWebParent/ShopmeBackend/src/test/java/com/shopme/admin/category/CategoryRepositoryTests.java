package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryRepositoryTests(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubcategory() {
        Category parent = new Category(7);
        Category subcategory = new Category("iPhone", parent);

        Category savedCategory = categoryRepository.save(subcategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory() {

        Category category = categoryRepository.findById(2).get();

        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category child : children) {
            System.out.println(child.getName());
        }

        System.out.println();

        children.forEach(cat -> System.out.println(cat.getName()));

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subcategory : children) {
                    System.out.println("--" + subcategory.getName());
                    printChildren(subcategory, 1);
                }
            }

            System.out.println();
        }
    }

    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subcategory : children) {

            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }

            System.out.println(subcategory.getName());

            printChildren(subcategory, newSubLevel);
        }
    }


    @Test
    public void testListRootCategories() {
        List<Category> categories = categoryRepository.findRootCategories();
        categories.forEach(category -> System.out.println(category.getName()));
    }
}
