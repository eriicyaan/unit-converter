# Unit Converter — практическая работа №2 (модуль 2)

Конвертер единиц измерения. Стек: **Java 17, Spring Boot 3.3, Maven, JUnit 5, JaCoCo, PIT**.

## Функции модуля (`UnitConverterService`)

| Функция | Описание |
|---|---|
| `celsiusToFahrenheit` | °C → °F |
| `fahrenheitToCelsius` | °F → °C |
| `celsiusToKelvin` | °C → K (ниже −273.15 °C — ошибка) |
| `kilometersToMiles` | км → мили (отрицательные — ошибка) |
| `milesToKilometers` | мили → км (отрицательные — ошибка) |
| `kilogramsToPounds` | кг → фунты (отрицательные — ошибка) |
| `poundsToKilograms` | фунты → кг (отрицательные — ошибка) |

## REST API

`GET /api/convert/{type}?value=...`, где `type` — `c-to-f`, `f-to-c`, `c-to-k`, `km-to-mi`, `mi-to-km`, `kg-to-lb`, `lb-to-kg`.

```bash

curl "http://localhost:8080/api/convert/c-to-f?value=100"
# {"type":"c-to-f","value":100.0,"result":212.0}
```

Ошибки (неизвестный тип, недопустимое значение, не число) → `400 Bad Request`.

## Запуск

```bash

mvn spring-boot:run
mvn test

mvn org.pitest:pitest-maven:mutationCoverage

```

Отчёты: `target/site/jacoco/index.html` (покрытие), `target/pit-reports/<дата>/index.html` (мутации).

## Преднамеренная ошибка (BUG-002)

В `fahrenheitToCelsius` коэффициент записан как `* 9 / 5` вместо `* 5 / 9`.
Особенность: на значении 32 °F ошибка **не проявляется** (результат 0 при любом коэффициенте),
поэтому тест с одной «удобной» точкой её пропустит.
Тесты `(212, 100)`, `(-40, -40)`, `(98.6, 37)`, `(122, 50)` и контроллерный тест её обнаруживают.

**Исправление:**

```java
return (fahrenheit - 32) * 5 / 9;
```

**Отчёт об ошибке:** Краткое описание: «Неверная формула перевода °F → °C». Статус: Open. Категория: Major.
Шаги: вызвать `fahrenheitToCelsius(212)`. Полученный результат: `324.0`. Ожидаемый: `100.0`.

Затем: `mvn test` (повторное тестирование) и `mvn org.pitest:pitest-maven:mutationCoverage`.
PIT запускается только на зелёных тестах.
