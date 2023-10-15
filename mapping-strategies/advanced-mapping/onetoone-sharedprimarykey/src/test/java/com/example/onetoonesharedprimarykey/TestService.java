/*
 * ========================================================================
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package com.example.onetoonesharedprimarykey;


import com.example.onetoonesharedprimarykey.model.Address;
import com.example.onetoonesharedprimarykey.model.User;
import com.example.onetoonesharedprimarykey.repository.AddressRepository;
import com.example.onetoonesharedprimarykey.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TestService {

    private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:latest";

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE_NAME);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    @Test
    void test() {

        Address address = new Address("Flowers Street", "01246", "Boston");
        addressRepository.save(address);

        User john = new User(address.getId(), // Assign same identifier value
                "John Smith"
        );
        john.setShippingAddress(address);
        userRepository.save(john);

        User user = userRepository.findById(john.getId()).get();
        Address address2 = addressRepository.findById(address.getId()).get();

        assertAll(
                () -> Assertions.assertEquals("Flowers Street", user.getShippingAddress().getStreet()),
                () -> Assertions.assertEquals("01246", user.getShippingAddress().getZipcode()),
                () -> Assertions.assertEquals("Boston", user.getShippingAddress().getCity()),
                () -> Assertions.assertEquals("Flowers Street", address2.getStreet()),
                () -> Assertions.assertEquals("01246", address2.getZipcode()),
                () -> Assertions.assertEquals("Boston", address2.getCity())
        );

    }
}
