package org.example.tests;

import io.restassured.response.Response;
import org.example.basetestsclass.BaseTests;
import org.example.pojos.ItemPojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@DisplayName("RestTest - класс, проверяющий функциональность работы API")
public class RestTest extends BaseTests {

    @DisplayName("Проверка добавления товара")
    @ParameterizedTest
    @CsvSource(value = {
            "абв, VEGETABLE, true",
            "1, FRUIT, false",
            "*?!, VEGETABLE, false",
            "аб*в12?!3, FRUIT, true"
    })
    public void testAddingItem(String name, String type, Boolean exotic) {

        ItemPojo itemPojo = createItem(name, type, exotic);

        //запрос на добавление товара POST
        Response addingResponse = given()
                .basePath("/food").body(itemPojo)
                .when()
                .log().all()
                .header("Accept", "*/*")
                .header("Content-Type", "application/json")
                .post("")
                .then()
                .log().all()
                .extract()
                .response();

        Map<String, String> cookies = addingResponse.getCookies();

        List<ItemPojo> itemList = getList(cookies);
        int listSize = itemList.size();

        Assertions.assertTrue(itemList.contains(itemPojo));

        //запрос на сброс данных POST
        given()
                .cookies(cookies)
                .basePath("/data")
                .when()
                .log().all()
                .post("/reset")
                .then()
                .log().all();

        List<ItemPojo> resultList = getList(cookies);

        Assertions.assertEquals(resultList.size(), listSize - 1);
    }

    /**
     * Запрос на получение всех товаров GET
     *
     * @param cookies - cookies данной сессии
     * @return List<ItemPojo> - возвращается список объектов ItemPojo
     */
    private List<ItemPojo> getList(Map<String, String> cookies) {
        return given()
                .cookies(cookies)
                .basePath("/food")
                .when()
                .log().all()
                .get("")
                .then()
                .log().all()
                .extract()
                .jsonPath()
                .getList("", ItemPojo.class);
    }

    /**
     * Создание объекта ItemPojo
     *
     * @param name   - наименование
     * @param type   - тип
     * @param exotic - экзотический
     * @return ItemPojo - возвращается объект ItemPojo
     */
    private ItemPojo createItem(String name, String type, Boolean exotic) {
        ItemPojo itemPojo = new ItemPojo();
        itemPojo.setName(name);
        itemPojo.setType(type);
        itemPojo.setExotic(exotic);
        return itemPojo;
    }
}
