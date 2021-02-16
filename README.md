# Simbir backend Intership

[Тествое задание](https://docviewer.yandex.ru/view/217796654/?*=gXcKy%2FVIepwSVXPx12wNvxVffiV7InVybCI6InlhLWRpc2stcHVibGljOi8vYU96d0VMS2hYN1ZHT1FWc3ZBTlJINkc2NklyQ3NQRVNtSTNGWVhlZktZWjdzSlh0WW1Ha3JCQlRQazRpdEh3SEVrSTBlMGl0L1A1M0pqQktkcmpGdWc9PSIsInRpdGxlIjoi0J3QvtCy0L7QtSDRgtC10YHRgtC%2B0LLQvtC1INC30LDQtNCw0L3QuNC1LnBkZiIsIm5vaWZyYW1lIjpmYWxzZSwidWlkIjoiMjE3Nzk2NjU0IiwidHMiOjE2MTMxMTY5MTQwODAsInl1IjoiMjE4NzM0NjI2MTYwNDUxNTMxOCJ9) для поступления на курс.

Разбор html страницы по набору разделителей: `' ', ',', '.', '!', '?', '"', ';', ':', '[', ']', '(', ')', '\n', '\r', '\t'`.

Статистика разбора сохраняется в БД H2.

Пример вывода:

``` sh
font_loaded:2
25px:2
```

## Запуск

Для запуска необходимо (с in-memory H2):

- перейти в директорию проекта `SimbirSoft Internship backend`
- выполнить в консоли `mvn package appassembler:assemble`
- выполнить `"./target/appassembler/bin/simbir.bat" https://www.simbirsoft.com`

Для запуска в файловой базы данных задайте параметры запуска. Например:

``` sh
"./target/appassembler/bin/simbir.bat" https://www.simbirsoft.com --database-connection="jdbc:h2:tcp://localhost/~/test"
```

Для запуска в файловой базе необходима установленная БД H2.

## Параметры запуска

| Параметр              | Описание                          |
|:----------------------|:----------------------------------|
| --database-connection | Строка соединения с БД            |
| --database-user       | Логин к БД                        |
| --database-password   | Пароль к БД                       |
| --buffer-size         | Размер буфера при чтении страницы |
