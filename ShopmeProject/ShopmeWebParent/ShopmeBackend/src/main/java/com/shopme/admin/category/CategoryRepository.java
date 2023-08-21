package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>, CrudRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL")
    List<Category> findRootCategories();

    // returns one category because name is unique
    Category findByName(String name);

    // returns one category because alias is unique
    Category findByAlias(String alias);
}
