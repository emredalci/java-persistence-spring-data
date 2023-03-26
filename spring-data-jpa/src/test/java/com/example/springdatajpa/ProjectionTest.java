package com.example.springdatajpa;

import com.example.springdatajpa.model.User;
import com.example.springdatajpa.model.UserProjection;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectionTest extends AbstractTestContainer{

    @Test
    void testProjectionUsername() {

        List<UserProjection.UsernameOnly> users = userRepository.findByEmail("john@somedomain.com");

        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("john", users.get(0).username())
        );
    }

    @Test
    void testProjectionUserSummary() {
        List<UserProjection.UserSummary> users = userRepository.findByRegistrationDateAfter(LocalDate.of(2021, Month.FEBRUARY, 1));

        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("julius", users.get(0).getUsername()),
                () -> assertEquals("julius julius@someotherdomain.com", users.get(0).getInfo())
        );
    }

    @Test
    void testDynamicProjection() {
        List<UserProjection.UsernameOnly> usernames = userRepository.findByEmail("mike@somedomain.com", UserProjection.UsernameOnly.class);
        List<User> users = userRepository.findByEmail("mike@somedomain.com", User.class);

        assertAll(
                () -> assertEquals(1, usernames.size()),
                () -> assertEquals("mike", usernames.get(0).username()),
                () -> assertEquals(1, users.size()),
                () -> assertEquals("mike", users.get(0).username())
        );
    }
}
