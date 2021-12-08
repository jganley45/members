package com.members;

import com.members.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@TestInstance(value = TestInstance.Lifecycle.PER_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("it-test")
@TestPropertySource(locations = "/application-it.properties")
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = { "com.members" })
//@EnableJpaRepositories("com.members.repository")
@EntityScan("com.members.model")
@Slf4j
public abstract class AbstractITCase {

    @Autowired
    protected UserRepository userRepository;

    public String email1 = "sparker@dd.com";
    public String loginId1 = "sparker";
    public String name1 = "Sam Parker1";
    public String email2 = "kerry@dd.com";
    public String loginId2 = "kerry";
    public String name2 = "Kerry O";

    public void cleanupData() {
        // ** order matters here
        userRepository.deleteAllInBatch();

    }
}
