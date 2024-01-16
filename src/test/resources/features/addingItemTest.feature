# language: ru

@all
Функция: Добавление товара

  Предыстория:
    * нажать на элемент выпадающий список в меню
    * выбрать в выпадающем списке элемент "Товары"
    * нажать на кнопку Добавить

  @correct
  Структура сценария: Успешное добавление товара <name> <type> <typeValue> <exotic>
    * заполняются поля:
      | Наименование | <name> |
      | Тип          | <type> |
    * чекбокс Экзотический принимает значение "<exotic>"
    * нажать на кнопку Сохранить
    * проверить отображение элемента "<name>", "<typeValue>", "<exotic>" в таблицу
    * удалить элемент

    Примеры:
      | name      | type      | typeValue | exotic |
      | абв       | VEGETABLE | Овощ      | true   |
      | 1         | FRUIT     | Фрукт     | false  |
      | *?!       | VEGETABLE | Овощ      | false  |
      | аб*в12?!3 | FRUIT     | Фрукт     | true   |

  @fail
  Сценарий: Некорректное добавление товара
    Если не заполненить поле "Наименование"
    То выводится сообщение "Поле Наименование не заполнено / заполнено неправильно!"