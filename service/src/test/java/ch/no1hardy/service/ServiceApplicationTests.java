package ch.no1hardy.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {
    @Autowired
    private GreetingController greetingController;

    @Test
    void contextLoads() {
        assertThat(greetingController).isNotNull();
    }
}
