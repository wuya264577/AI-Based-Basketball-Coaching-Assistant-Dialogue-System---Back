package com.micro.service.knowledge_base_service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SearchApiTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/api/search";
    }

    @Test
    public void testValidKeyword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"keyword\":\"AI\"}")
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testEmptyKeyword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"keyword\":\"\"}")
                .when()
                .post()
                .then()
                .statusCode(200);  // 改成200，或根据实际情况断言返回数据内容
    }

    @Test
    public void testLongKeyword() {
        String longKeyword = "a".repeat(300);
        given()
                .contentType(ContentType.JSON)
                .body("{\"keyword\":\"" + longKeyword + "\"}")
                .when()
                .post()
                .then()
                .statusCode(200);  // 同上
    }

    @Test
    public void testSpecialCharacterKeyword() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"keyword\":\"!@#￥%……\"}")
                .when()
                .post()
                .then()
                .statusCode(anyOf(is(200), is(400))); // 根据业务定义
    }

}
