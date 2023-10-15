package com.example.mappingvaluetypes;

import com.example.mappingvaluetypes.model.*;
import com.example.mappingvaluetypes.repository.User2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConvertingPropertiesOfComponentTest extends AbstractTestContainer{

    @Autowired
    User2Repository user2Repository;

    @Test
    void storeLoadEntities() {

        City city = new City();
        city.setName("Boston");
        city.setZipcode(new GermanZipcode("12345"));
        city.setCountry("USA");

        User2 user = new User2();
        user.setUsername("username");
        user.setHomeAddress(new Address2("Flowers Street", city));

        user2Repository.save(user);

        List<User2> users = user2Repository.findAll();


        assertAll(
                () -> assertEquals(1, users.size()),
                () -> assertEquals("username", users.get(0).getUsername()),
                () -> assertEquals("Flowers Street", users.get(0).getHomeAddress().getStreet()),
                () -> assertEquals("Boston", users.get(0).getHomeAddress().getCity().getName()),
                () -> assertEquals("12345", users.get(0).getHomeAddress().getCity().getZipcode().getValue()),
                () -> assertEquals("USA", users.get(0).getHomeAddress().getCity().getCountry())
        );
    }
}
