package com.jumkid.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityAPITest implements TestObjectsBuilder{

    @LocalServerPort
    private int port;

    @Value("${com.jumkid.jwt.test-token}")
    private String testToken;

    @Value("${com.jumkid.jwt.test-user-id}")
    private String testUserId;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Activity activity;
    private ActivityEntity activityEntity;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private MapperContext mapperContext;

    @MockBean
    private ActivityRepository activityRepository;

    @BeforeAll
    void setUp() {
        try {
            RestAssured.defaultParser = Parser.JSON;
            RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

            activity = buildActivity(testUserId);
            activityEntity = activityMapper.dtoToEntity(activity, mapperContext);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void shouldGetActivitiesByUser() throws Exception {
        when(activityRepository.findByUser(anyString())).thenReturn(List.of(activityEntity));

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].id", equalTo(0),
                            "[0].name", equalTo(activity.getName()));
    }

    @Test
    void whenGivenActivityId_shouldGetActivity() throws Exception {
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activityEntity));

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(0));
    }

    @Test
    void whenAssessAsGuest_shouldReturnForbiddenStat() throws Exception {
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void whenAssessAsNoOwner_shouldReturnForbiddenStat() throws Exception {
        Activity _activity = buildActivity("nobody");
        when(activityRepository.findById(activity.getId()))
                .thenReturn(Optional.of(activityMapper.dtoToEntity(_activity, mapperContext)));

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void whenGivenActivity_shouldSaveActivityEntity() throws Exception {
        when(activityRepository.save(any(ActivityEntity.class))).thenReturn(activityEntity);

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testToken)
                    .contentType(ContentType.JSON)
                    .body(new ObjectMapper().writeValueAsBytes(activity))
                .when()
                    .post("/activities")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(0), "name", equalTo(activity.getName()));
    }

    @Test
    void whenSaveAsGuest_shouldReturnForbiddenStat() throws Exception {
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .contentType(ContentType.JSON)
                .when()
                    .post("/activities")
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());

    }

    @Test
    void whenGivenActivityIdAndActivity_shouldUpdateActivityEntity() throws Exception{
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activityEntity));
        when(activityRepository.save(any(ActivityEntity.class))).thenReturn(activityEntity);

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testToken)
                    .contentType(ContentType.JSON)
                    .body(new ObjectMapper().writeValueAsBytes(activity))
                .when()
                    .put("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(0), "name", equalTo(activity.getName()));
    }

    @Test
    void whenUpdateAsGuest_shouldReturnForbiddenStat() throws Exception {
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .contentType(ContentType.JSON)
                    .body(new ObjectMapper().writeValueAsBytes(activity))
                .when()
                    .put("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void whenGivenActivityId_shouldDeleteActivityEntity() throws Exception{
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activityEntity));

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testToken)
                    .contentType(ContentType.JSON)
                .when()
                    .delete("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
