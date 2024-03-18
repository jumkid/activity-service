package com.jumkid.activity;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@PropertySource("classpath:application.share.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
class ActivityStatusAPITest {

    @LocalServerPort
    private int port;

    @Value("${com.jumkid.jwt.test.user-token}")
    private String testUserToken;

    @Test
    void shouldGetActivityStatuses() throws Exception {
        RestAssured
                .given()
                    .baseUri("http://localhost").port(port)
                    .headers("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activity/statuses")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", equalTo(4));
    }
}
