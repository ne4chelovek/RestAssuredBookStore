import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class APITest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://demoqa.com/BookStore/v1/";
    }

    @DisplayName("Проверка на отсутствие книги")
    @Test
    public void bookNotFoundTest() {
        given().when()
                .get(baseURI + "Book?ISBN=9781449328862")
                .then()
                .log().all()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 Bad Request")
                .body("code", equalTo("1205"))
                .body("message", equalTo("ISBN supplied is not available in Books Collection!"));
    }

    @DisplayName("Получения подробной информации о книге по ее идентификационному номеру")
    @Test
    public void testSuccessStatus() {
        given()
                .when()
                .get(baseURI + "Book?ISBN=9781449325862")
                .then()
                .log().all()
                .statusCode(200)
                .body("title", equalTo("Git Pocket Guide"))
                .body("pages", equalTo(234));
    }


    @DisplayName("Проверка получения статус кода, после авторизации")
    @Test()
    public void bookTest() {
        String userName = "Petoto";
        String password = "GaGaga!1";

        Map<String, String> request = new HashMap<>();
        request.put("userName", userName);
        request.put("password", password);
        given().contentType("application/json")
                .body(request)
                .when()
                .post( "https://demoqa.com/Account/v1/Authorized")
                .then()
                .log().all()
                .time(lessThan(3000L))
                .statusCode(200);
    }
}
