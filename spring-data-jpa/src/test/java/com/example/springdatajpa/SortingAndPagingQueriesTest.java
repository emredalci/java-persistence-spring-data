package com.example.springdatajpa;

import com.example.springdatajpa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortingAndPagingQueriesTest extends AbstractTestContainer{

    @Test
    void testOrder() {

        Optional<User> optionalUser1 = userRepository.findFirstByOrderByUsernameAsc();
        Optional<User> optionalUser2 = userRepository.findTopByOrderByRegistrationDateDesc();
        Page<User> userPage = userRepository.findAll(PageRequest.of(1, 3));
        List<User> users = userRepository.findFirst2ByLevel(2, Sort.by("registrationDate"));

        assertAll(
                () -> assertEquals("beth", optionalUser1.get().username()),
                () -> assertEquals("julius", optionalUser2.get().username()),
                () -> assertEquals(2, users.size()),
                () -> assertEquals(3, userPage.getSize()),
                () -> assertEquals("beth", users.get(0).username()),
                () -> assertEquals("marion", users.get(1).username())
        );

    }

    @Test
    void testFindByLevel() {
        Sort.TypedSort<User> user = Sort.sort(User.class);

        List<User> users = userRepository.findByLevel(3, user.by(User::registrationDate).descending());
        assertAll(
                () -> assertEquals(2, users.size()),
                () -> assertEquals("james", users.get(0).username())
        );

    }

    @Test
    void testFindByActive() {
        List<User> users = userRepository.findByActive(true, PageRequest.of(1, 4, Sort.by("registrationDate")));
        assertAll(
                () -> assertEquals(4, users.size()),
                () -> assertEquals("burk", users.get(0).username())
        );

    }
}
