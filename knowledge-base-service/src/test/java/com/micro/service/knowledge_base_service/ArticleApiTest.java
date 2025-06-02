package com.micro.service.knowledge_base_service;

import com.micro.service.knowledge_base_service.entity.Article;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArticleApiTest {

    private static Long testArticleId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
    }

    @Test
    @Order(1)
    public void testCreateArticle() {
        Map<String, Object> article = new HashMap<>();
        article.put("title", "测试文章");
        article.put("author", "测试作者");
        article.put("views", 0);
        article.put("downloads", 0);
        article.put("content", "这是一篇测试文章内容");

        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .body(article)
                .when()
                .post("/api/articles")
                .then()
                .statusCode(200)
                .body("title", equalTo("测试文章"));

        Number id = response.extract().path("id");
        testArticleId = id.longValue();
    }

    @Test
    @Order(2)
    public void testGetArticleById() {
        given()
                .when()
                .get("/api/articles/" + testArticleId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testArticleId.intValue()))
                .body("title", equalTo("测试文章"));
    }

    @Test
    @Order(3)
    public void testGetAllArticles() {
        given()
                .when()
                .get("/api/articles")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    @Order(4)
    public void testIncrementViews() {
        given()
                .when()
                .put("/api/articles/views/increment/" + testArticleId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testArticleId.intValue()))
                .body("views", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(5)
    public void testIncrementDownloads() {
        given()
                .when()
                .put("/api/articles/downloads/increment/" + testArticleId)
                .then()
                .statusCode(200)
                .body("id", equalTo(testArticleId.intValue()))
                .body("downloads", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(6)
    public void testGetTop8ArticlesByViews() {
        given()
                .when()
                .get("/api/articles/views/top8")
                .then()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(8));
    }

    @Test
    @Order(7)
    public void testGetTop8RecentArticles() {
        given()
                .when()
                .get("/api/articles/recent/top8")
                .then()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(8));
    }

    @Test
    @Order(8)
    public void testGetTop8ArticlesByDownloads() {
        given()
                .when()
                .get("/api/articles/downloads/top8")
                .then()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(8));
    }

    @Test
    @Order(9)
    public void testDeleteArticle() {
        given()
                .when()
                .delete("/api/articles/" + testArticleId)
                .then()
                .statusCode(200);
    }
}
