package com.shopme.admin.brand;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTests {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BrandRepositoryTests(BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }


    @Test
    public void testCreateBrand() {
        Category laptops = categoryRepository.findByName("Laptops");

        Brand acer = new Brand("Acer");
        acer.addCategory(laptops);
        Brand savedAcerBrand = brandRepository.save(acer);

        assertThat(savedAcerBrand).isNotNull();
        assertThat(savedAcerBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateBrand2() {
        Category cellPhones = categoryRepository.findByName("Cell Phones & Accessories");
        Category tablets = categoryRepository.findByName("Tablets");

        Brand apple = new Brand("Apple");
        apple.addCategory(cellPhones);
        apple.addCategory(tablets);

        Brand savedAppleBrand = brandRepository.save(apple);

        assertThat(savedAppleBrand).isNotNull();
        assertThat(savedAppleBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testCreateBrand3() {
        Category memory = categoryRepository.findByName("Memory");
        Category internalHardDrives = categoryRepository.findByName("Internal Hard Drives");

        Brand samsung = new Brand("Samsung");
        samsung.addCategory(memory);
        samsung.addCategory(internalHardDrives);

        Brand savedSamsungBrand = brandRepository.save(samsung);

        assertThat(savedSamsungBrand).isNotNull();
        assertThat(savedSamsungBrand.getId()).isGreaterThan(0);
    }


    @Test
    public void testFindAll() {
        Iterable<Brand> brands = brandRepository.findAll();
        brands.forEach(System.out::println);
    }


    @Test
    public void testGetById() {
        Integer id = 2;
        Brand brand = brandRepository.findById(id).get();

        assertThat(brand.getName()).isEqualTo("Apple");
    }


    @Test
    public void testUpdateName() {
        String newName = "Samsung Electronics";
        Brand samsung = brandRepository.findById(3).get();

        samsung.setName(newName);

        Brand updatedBrand = brandRepository.save(samsung);

        assertThat(updatedBrand.getName()).isEqualTo(newName);
    }


    @Test
    public void testDelete() {
        Integer id = 2;
        brandRepository.deleteById(id);

        Optional<Brand> result = brandRepository.findById(id);

        assertThat(result).isEmpty();
    }


}
