package com.members.rest;


import com.members.AbstractITCase;
import com.members.MembersApplication;
import com.members.model.Event;
import com.members.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Slf4j
@Tag("integration")
@SpringBootTest(classes = {MembersApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class EventControllerITCase extends AbstractITCase  {
    @Autowired
    protected MockMvc mvc;

    @BeforeAll
    public void init() {
        log.info("****** EventControllerITCase - INIT ***************");

        try {
            cleanupData();

            Event event1 = Event.builder().description(description1).location(location1)
                    .eventName(eventName1).eventDate(new Date()).build();
            eventRepository.saveAndFlush(event1);
        } catch (Exception e) {
            // ** catch any exception and FAIL
            log.error("EventControllerITCase @BEFORE ALL - General Exception occurred" + e);
            e.printStackTrace();
            return;
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        return;
    }

    @Test
    public void testGet() throws Exception {

        ResultActions result = mvc.perform(get("/event/get-events")
                .contentType(MediaType.ALL_VALUE)).andExpect(status().isOk());
        assertNotNull(result);
        log.info(String.format("get: %s", result.andReturn().getResponse().getContentAsString()));
        assertTrue(result.andReturn().getResponse().getContentAsString().contains(eventName1));

    }
}
