package com.jumkid.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.controller.dto.ActivityNotification;
import com.jumkid.activity.enums.NotifyTimeUnit;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import com.jumkid.share.util.DateTimeUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
@EnableTestContainers
@TestPropertySource("/application.share.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityAPITest{
    @LocalServerPort
    private int port;

    @Value("${com.jumkid.jwt.test.user-token}")
    private String testUserToken;
    @Value("${com.jumkid.jwt.test.user-id}")
    private String testUserId;
    @Value("${com.jumkid.jwt.test.admin-token}")
    private String testAdminToken;

    private Activity activity;
    private ActivityEntity activityEntity;

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private MapperContext mapperContext;

    @BeforeAll
    void setUp() {
        try {
            RestAssured.defaultParser = Parser.JSON;

            activity = TestObjectsBuilder.buildActivity(TestObjectsBuilder.DUMMY_ID, testUserId);
            activityEntity = activityMapper.dtoToEntity(activity, mapperContext);

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Get activities by user")
    @Order(1)
    void shouldGetActivitiesByUser() throws Exception {
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThan(0));
    }

    @Test
    @DisplayName("Get activity by id")
    @Order(2)
    void whenGivenActivityId_shouldGetActivity() throws Exception {
        String datetimeStr =
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities/" + activity.getId())
                .then()
                    .log()
                    .all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(activity.getId().intValue()))
                .extract()
                    .path("modificationDate");

        activity.setModificationDate(DateTimeUtils.stringToLocalDatetime(datetimeStr));
    }

    @Test
    @DisplayName("Get activity by id as guest - Forbidden")
    @Order(3)
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
    @DisplayName("Get activity by id as noneOwner - Forbidden")
    @Order(4)
    void whenAssessAsNoOwner_shouldReturnForbiddenStat() throws Exception {
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testAdminToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Save new activity")
    @Order(4)
    void whenGivenActivity_shouldSaveActivityEntity() throws Exception {
        Activity newActivity = TestObjectsBuilder.buildActivity(null, testUserId);
        newActivity.setActivityNotification(ActivityNotification.builder()
                .notifyBefore(5).notifyBeforeUnit(NotifyTimeUnit.MINUTE).build());

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                    .body(new ObjectMapper().writeValueAsBytes(newActivity))
                .when()
                    .post("/activities")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", greaterThan(0), "name", equalTo(activity.getName()));
    }

    @Test
    @DisplayName("Save new activity as guest - Forbidden")
    @Order(5)
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
    @DisplayName("Update existing activity")
    @Order(6)
    void whenGivenActivityIdAndActivity_shouldUpdateActivityEntity() throws Exception{
        String updateText = "test update";
        Activity updateActivity = Activity.builder()
                .description(updateText)
                .modificationDate(activity.getModificationDate())
                .build();

        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                    .body(new ObjectMapper().writeValueAsBytes(updateActivity))
                .when()
                    .put("/activities/" + activity.getId())
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(activity.getId().intValue()), "description", equalTo(updateText));
    }

    @Test
    @DisplayName("Update existing activity as guest - Forbidden")
    @Order(7)
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
    @DisplayName("Delete existing activity")
    @Order(8)
    void whenGivenActivityId_shouldDeleteActivityEntity() throws Exception{
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                .when()
                    .delete("/activities/" + activity.getId())
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
