package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    private final UserRepository userRepository;
    private final TestEntityManager testEntityManager;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository, TestEntityManager testEntityManager) {
        this.userRepository = userRepository;
        this.testEntityManager = testEntityManager;
    }

    @Test
    public void testCreateUserWithOneRole() {
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User juanMartin = new User("juanmartin_franco@hotmail.com",
                "juan2023", "Juan Martín", "Franco");
        juanMartin.addRole(roleAdmin);

        User savedUser = userRepository.save(juanMartin);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles() {
        User lara = new User("lara_vega@hotmail.com",
                "lara2023", "Lara", "Vega");

        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        lara.addRole(roleEditor);
        lara.addRole(roleAssistant);

        User savedUser = userRepository.save(lara);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        List<User> usersList = (List<User>) userRepository.findAll();
        usersList.forEach(System.out::println);
    }

    @Test
    public void testGetUserById() {
        Optional<User> userOptional = userRepository.findById(1);
        assertThat(userOptional).isPresent();

        User juanMartin = userOptional.get();
        System.out.println(juanMartin);
        assertThat(juanMartin).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User juanMartin = userRepository.findById(1).get();
        juanMartin.setEnabled(true);
        juanMartin.setEmail("juanmartin_franco@outlook.com");

        userRepository.save(juanMartin);
    }

    @Test
    public void testUpdateUserRoles() {
        User lara = userRepository.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        lara.getRoles().remove(roleEditor);
        lara.addRole(roleSalesperson);

        userRepository.save(lara);
    }

    @Test
    public void testDeleteUser() {
        Integer id = 11;
        userRepository.deleteById(id);

        Optional<User> user = userRepository.findById(2);
        assertThat(user).isNotPresent();
    }

    @Test
    public void testGetUserByEmail() {
        String email = "juanmartin_franco@outlook.com";
        User user = userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = userRepository.countById(id);

        assertThat(countById)
                .isNotNull()
                .isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 25;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 1;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> page = userRepository.findAll(pageable);

        List<User> usersList = page.getContent();
        usersList.forEach(System.out::println);

        assertThat(usersList.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "bruce";

        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> page = userRepository.findAll(keyword, pageable);

        List<User> usersList = page.getContent();
        usersList.forEach(System.out::println);

        assertThat(usersList.size()).isGreaterThan(0);
    }

}
