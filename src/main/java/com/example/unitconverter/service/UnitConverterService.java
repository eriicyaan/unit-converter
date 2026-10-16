package com.example.unitconverter.service;

import org.springframework.stereotype.Service;

/**
 * Модуль «Конвертер единиц измерения»: температура, длина, масса.
 */
@Service
public class UnitConverterService {

    private static final double ABSOLUTE_ZERO_C = -273.15;
    private static final double KM_PER_MILE = 1.609344;
    private static final double LB_PER_KG = 2.2046226218;

    /** °C → °F. */
    public double celsiusToFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }

    /** °F → °C. */
    public double fahrenheitToCelsius(double fahrenheit) {
        // ПРЕДНАМЕРЕННАЯ ОШИБКА (BUG-002): перепутаны числитель и знаменатель коэффициента.
        // Правильно: (fahrenheit - 32) * 5 / 9
        return (fahrenheit - 32) * 5 / 9;
    }

    /** °C → K. Температура ниже абсолютного нуля недопустима. */
    public double celsiusToKelvin(double celsius) {
        if (celsius < ABSOLUTE_ZERO_C) {
            throw new IllegalArgumentException("Temperature below absolute zero");
        }
        return celsius - ABSOLUTE_ZERO_C;
    }

    /** км → мили. Расстояние не может быть отрицательным. */
    public double kilometersToMiles(double km) {
        requireNonNegative(km);
        return km / KM_PER_MILE;
    }

    /** мили → км. Расстояние не может быть отрицательным. */
    public double milesToKilometers(double miles) {
        requireNonNegative(miles);
        return miles * KM_PER_MILE;
    }

    /** кг → фунты. Масса не может быть отрицательной. */
    public double kilogramsToPounds(double kg) {
        requireNonNegative(kg);
        return kg * LB_PER_KG;
    }

    /** фунты → кг. Масса не может быть отрицательной. */
    public double poundsToKilograms(double pounds) {
        requireNonNegative(pounds);
        return pounds / LB_PER_KG;
    }

    private void requireNonNegative(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must not be negative");
        }
    }
}
