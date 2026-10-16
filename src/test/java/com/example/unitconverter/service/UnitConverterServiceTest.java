package com.example.unitconverter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

class UnitConverterServiceTest {

    private static final double DELTA = 1e-6;

    private UnitConverterService service;

    @BeforeEach
    void setUp() {
        service = new UnitConverterService();
    }

    // ---------- celsiusToFahrenheit ----------

    @ParameterizedTest
    @CsvSource({"0, 32", "100, 212", "-40, -40", "37, 98.6"})
    @DisplayName("°C → °F: опорные точки")
    void celsiusToFahrenheit(double c, double expected) {
        assertEquals(expected, service.celsiusToFahrenheit(c), DELTA);
    }

    // ---------- fahrenheitToCelsius ----------

    @ParameterizedTest
    @CsvSource({"32, 0", "212, 100", "-40, -40", "98.6, 37", "122, 50"})
    @DisplayName("°F → °C: опорные точки (ловит BUG-002 на всех значениях, кроме 32)")
    void fahrenheitToCelsius(double f, double expected) {
        assertEquals(expected, service.fahrenheitToCelsius(f), DELTA);
    }

    // ---------- celsiusToKelvin ----------

    @ParameterizedTest
    @CsvSource({"0, 273.15", "100, 373.15", "-273.15, 0"})
    @DisplayName("°C → K: значения, включая абсолютный ноль")
    void celsiusToKelvin(double c, double expected) {
        assertEquals(expected, service.celsiusToKelvin(c), DELTA);
    }

    @ParameterizedTest
    @CsvSource({"-273.16", "-300", "-1000"})
    @DisplayName("°C → K: ниже абсолютного нуля → IllegalArgumentException")
    void celsiusToKelvin_belowAbsoluteZero(double c) {
        assertThrows(IllegalArgumentException.class, () -> service.celsiusToKelvin(c));
    }

    // ---------- kilometersToMiles / milesToKilometers ----------

    @ParameterizedTest
    @CsvSource({"0, 0", "1.609344, 1", "10, 6.213712", "100, 62.137119"})
    @DisplayName("км → мили")
    void kilometersToMiles(double km, double expected) {
        assertEquals(expected, service.kilometersToMiles(km), DELTA);
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 1.609344", "10, 16.09344", "26.2, 42.164813"})
    @DisplayName("мили → км")
    void milesToKilometers(double miles, double expected) {
        assertEquals(expected, service.milesToKilometers(miles), DELTA);
    }

    @Test
    @DisplayName("Длина: отрицательное значение → IllegalArgumentException, ноль допустим")
    void length_negativeValues() {
        assertThrows(IllegalArgumentException.class, () -> service.kilometersToMiles(-0.001));
        assertThrows(IllegalArgumentException.class, () -> service.milesToKilometers(-0.001));
        assertEquals(0.0, service.kilometersToMiles(0), DELTA);
        assertEquals(0.0, service.milesToKilometers(0), DELTA);
    }

    // ---------- kilogramsToPounds / poundsToKilograms ----------

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 2.2046226218", "10, 22.046226218", "70, 154.323584"})
    @DisplayName("кг → фунты")
    void kilogramsToPounds(double kg, double expected) {
        assertEquals(expected, service.kilogramsToPounds(kg), DELTA);
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "2.2046226218, 1", "22.046226218, 10", "154.323584, 70"})
    @DisplayName("фунты → кг")
    void poundsToKilograms(double lb, double expected) {
        assertEquals(expected, service.poundsToKilograms(lb), DELTA);
    }

    @Test
    @DisplayName("Масса: отрицательное значение → IllegalArgumentException, ноль допустим")
    void mass_negativeValues() {

        assertThrows(IllegalArgumentException.class, () -> service.kilogramsToPounds(-0.001));
        assertThrows(IllegalArgumentException.class, () -> service.poundsToKilograms(-0.001));
        assertEquals(0.0, service.kilogramsToPounds(0), DELTA);
        assertEquals(0.0, service.poundsToKilograms(0), DELTA);
    }

    // ---------- обратимость ----------

    @Test
    @DisplayName("Прямое и обратное преобразования взаимно обратны")
    void roundTrips() {
        assertEquals(42.0, service.milesToKilometers(service.kilometersToMiles(42.0)), DELTA);
        assertEquals(73.5, service.poundsToKilograms(service.kilogramsToPounds(73.5)), DELTA);
    }
}
