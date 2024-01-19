package org.example.tests;

import org.example.basetestsclass.BaseTests;
import org.example.pojos.ItemPojo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static io.restassured.RestAssured.given;

@DisplayName("RestTest - класс, проверяющий функциональность работы API")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestTest extends BaseTests {

    @DisplayName("Проверка добавления товара POST")
    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "абв, VEGETABLE, true",
            "1, FRUIT, false",
            "*?!, VEGETABLE, false",
            "аб*в12?!3, FRUIT, true"
    })
    public void testAddingItem(String name, String type, Boolean exotic) {
        ItemPojo itemPojo = createItem(name, type, exotic);

        given()
                .basePath("/food").body(itemPojo)
                .when()
                .log().all()
                .header("Accept", "*/*")
                .header("Content-Type", "application/json")
                .post("")
                .then()
                .log().all();
    }

    @DisplayName("Проверка наличия товара в списке товаров GET")
    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "абв, VEGETABLE, true",
            "1, FRUIT, false",
            "*?!, VEGETABLE, false",
            "аб*в12?!3, FRUIT, true"
    })
    public void testCheckItemInList(String name, String type, Boolean exotic) {
        ItemPojo itemPojo = createItem(name, type, exotic);

        List<ItemPojo> itemList = given()
                .basePath("/food")
                .when()
                .log().all()
                .get("")
                .then()
                .log().all()
                .extract()
                .jsonPath()
                .getList("", ItemPojo.class);

        Assertions.assertTrue(itemList.contains(itemPojo));
    }

    @DisplayName("Проверка сброса данных POST")
    @Order(3)
    @Test
    public void testReset() {
        given()
                .basePath("/data")
                .when()
                .log().all()
                .post("/reset")
                .then()
                .log().all();
    }

    private ItemPojo createItem(String name, String type, Boolean exotic) {
        ItemPojo itemPojo = new ItemPojo();
        itemPojo.setName(name);
        itemPojo.setType(type);
        itemPojo.setExotic(exotic);
        return itemPojo;
    }
}
