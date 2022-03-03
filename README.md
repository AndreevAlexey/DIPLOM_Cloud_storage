# Дипломная работа “Облачное хранилище”

## Описание проекта

Приложение REST-сервис реализует интерфейс загрузки файлов и вывода списка уже загруженных файлов пользователя 
для ранее разработанного веб-приложения https://github.com/netology-code/jd-homeworks/tree/master/diploma/netology-diplom-frontend
по спецификации https://github.com/netology-code/jd-homeworks/blob/master/diploma/CloudServiceSpecification.yaml

## Функциональность

### Хранилище

В качестве хранилища используется БД PostgreSQL

### Работа с хранилищем

Загрузка, удаление, изменение, скачивание файлов из хранилища

## Описание работы приложения

Запущенное приложение(port `9999` указан в настройках `application.yml` и `Dockerfile`) ожидает входящих запросов:

### 1. Аутентификация

http://localhost:9999/login 

`POST`

`Content-Type: application/json`

#### *пример:*

`{"login":"user",
  "password":"password"
}`

#### *Результаты обработки:*

+ *предоставление доступа к приложению, успешный ответ с кодом "200" и "auth-token" - токен доступа, сформированный для пользователя, по которому будет происходить авторизация к дальнейшим запросам*

+ *неуспешный ответ с кодом "400"(неверные параметры аутентификации) и текстом ошибки*

+ *неуспешный ответ с кодом "500"(ошибка приложения) и текстом ошибки*

### 2. Загрузка файла

http://localhost:9999/file?filename=example.txt

`POST`

`Content-Type: multipart/form-data`

#### *Результаты обработки:*

+ *загрузка файлав хранилище, успешный ответ с кодом "200"*

+ *неуспешный ответ с кодом "500"(ошибка приложения) и текстом ошибки*

### 3. Изменение имени файла

http://localhost:9999/file?filename=example.txt

`PUT`

`Content-Type: application/json`

#### *пример:*

`{"filename":"new_filename.txt"}`

#### *Результаты обработки:*

+ *изменение имени указанного файла, успешный ответ с кодом "200"*

+ *неуспешный ответ с кодом "400"(неверные параметры) и текстом ошибки*

+ *неуспешный ответ с кодом "500"(ошибка приложения) и текстом ошибки*

### 4. Скачать файл

http://localhost:9999/file?filename=example.txt

`GET`

#### *Результаты обработки:*

+ *multipart/form-data контент, успешный ответ с кодом "200"*

+ *неуспешный ответ с кодом "400"(неверные параметры) и текстом ошибки*

+ *неуспешный ответ с кодом "500"(ошибка приложения) и текстом ошибки*

### 5. Удаление файла

http://localhost:9999/file?filename=example.txt

`DELETE`

#### *Результаты обработки:*

+ *удаление файла из хранилища, успешный ответ с кодом "200"*

+ *неуспешный ответ с кодом "400"(неверные параметры) и текстом ошибки*

+ *неуспешный ответ с кодом "500"(ошибка приложения) и текстом ошибки*

### 6. Получить список файлов

http://localhost:9999/list?limit=5

`GET`

#### *Результаты обработки:*

+ *список файлов пользователя, успешный ответ с кодом "200"*

+ *неуспешный ответ с кодом "400"(неверные параметры) и текстом ошибки*

+ *неуспешный ответ с кодом "500"(ошибка приложения) и текстом ошибки*


### Примеры ответов:

`HTTP/1.1 200`
`Content-Type: text/plain;charset=UTF-8`
`{"auth-token":"значение токена"}`

`HTTP/1.1 400` 
`Content-Type: text/plain;charset=UTF-8`
`{"message":"Неверный пароль!","id":"0"}`


## Структура проекта

+ package config        - конфигурация
+ package repository    - репозиторий
+ package service       - сервис
+ package controller    - контроллер
+ package model         - модель данных
+ package exceptions    - выбрасываемые исключения
+ package constant      - используемые константы
+ package security      - контроль доступа

### Запуск

Для запуска приложения в терминале выполнить команды:

`./mvnw clean package` - буден сформирован jar-файл приложения

`docker-compose up` - будет запщен контейнер БД и контейнер приложения
