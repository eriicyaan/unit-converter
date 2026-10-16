package com.example.unitconverter.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.unitconverter.service.UnitConverterService;

@RestController
@RequestMapping("/api/convert")
public class UnitConverterController {

    public record ConversionResponse(String type, double value, double result) {}

    private final UnitConverterService service;

    public UnitConverterController(UnitConverterService service) {
        this.service = service;
    }

    /** Типы: c-to-f, f-to-c, c-to-k, km-to-mi, mi-to-km, kg-to-lb, lb-to-kg. */
    @GetMapping("/{type}")
    public ConversionResponse convert(@PathVariable String type, @RequestParam double value) {
        double result = switch (type) {
            case "c-to-f" -> service.celsiusToFahrenheit(value);
            case "f-to-c" -> service.fahrenheitToCelsius(value);
            case "c-to-k" -> service.celsiusToKelvin(value);
            case "km-to-mi" -> service.kilometersToMiles(value);
            case "mi-to-km" -> service.milesToKilometers(value);
            case "kg-to-lb" -> service.kilogramsToPounds(value);
            case "lb-to-kg" -> service.poundsToKilograms(value);
            default -> throw new IllegalArgumentException("Unknown conversion type: " + type);
        };
        return new ConversionResponse(type, value, result);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}
