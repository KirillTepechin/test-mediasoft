# Склад товаров

## Инструкция по запуску

### Способ 1

> **_Примечание:_**  Этот способ требует наличия jdk, gradle и postgresql.
1. Клонируйте репозиторий проекта
    ````
    git clone https://github.com/KirillTepechin/test-mediasoft.git
    ````
2. Откройте проект в командной строке
    ````
    cd path/to/warehouse
    ````
3. Запустите проект
    ````
    ./gradlew bootRun
    ````

### Способ 2

> **_Примечание:_**  Этот способ требует наличия docker.

1. Клонируйте репозиторий проекта
    ````
    git clone https://github.com/KirillTepechin/test-mediasoft.git
    ````
2. Откройте проект в командной строке
    ````
    cd path/to/warehouse
    ````
3. Запустите докер контейнер с проектом
    ````
    docker-compose up
    ````