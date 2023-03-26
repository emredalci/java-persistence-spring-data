package com.example.springdatajpa;

import com.example.springdatajpa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteQueryTest extends AbstractTestContainer{

    @Test
    void testDeleteByLevel() {
        userRepository.deleteByLevel(2);
        List<User> users = userRepository.findByLevel(2, Sort.by("username"));
        assertEquals(0, users.size());
    }

    @Test
    void testDeleteBulkByLevel() {
        userRepository.deleteBulkByLevel(2);
        List<User> users = userRepository.findByLevel(2, Sort.by("username"));
        assertEquals(0, users.size());
    }
}
