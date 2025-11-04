package com.bookie.bookie;

import com.bookie.bookie.entities.User;
import com.bookie.bookie.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookieApplicationTests {

    @Autowired
    UserRepository userRepository;
    @Test
    @Transactional
    void test1() {

    }

}
