package com.example.unitconverter.controller;

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.unitconverter.service.UnitConverterService;

@WebMvcTest(UnitConverterController.class)
@Import(UnitConverterService.class)
class UnitConverterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /c-to-f: 100 °C = 212 °F")
    void celsiusToFahrenheit() throws Exception {
        mockMvc.perform(get("/api/convert/c-to-f").param("value", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("c-to-f"))
                .andExpect(jsonPath("$.value").value(100.0))
                .andExpect(jsonPath("$.result", closeTo(212.0, 1e-6)));
    }

    @Test
    @DisplayName("GET /c-to-k: 0 °C = 273.15 °F")
    void celsiusToKelvin() throws Exception {
        mockMvc.perform(get("/api/convert/c-to-k").param("value", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("c-to-k"))
                .andExpect(jsonPath("$.value").value(0))
                .andExpect(jsonPath("$.result", closeTo(273.15, 1e-6)));
    }

    @Test
    @DisplayName("GET /mi-to-km: 15 = 24.14")
    void milesToKilometers() throws Exception {
        mockMvc.perform(get("/api/convert/mi-to-km").param("value", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("mi-to-km"))
                .andExpect(jsonPath("$.value").value(15))
                .andExpect(jsonPath("$.result", closeTo(24.14, 1e-1)));
    }

    @Test
    @DisplayName("GET /lb-to-kg: 75 = 33.95")
    void poundsToKilograms() throws Exception {
        mockMvc.perform(get("/api/convert/lb-to-kg").param("value", "75"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("lb-to-kg"))
                .andExpect(jsonPath("$.value").value(75))
                .andExpect(jsonPath("$.result", closeTo(33.95, 1e-1)));
    }



    @Test
    @DisplayName("GET /f-to-c: 212 °F = 100 °C (ловит BUG-002)")
    void fahrenheitToCelsius() throws Exception {
        mockMvc.perform(get("/api/convert/f-to-c").param("value", "212"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", closeTo(100.0, 1e-6)));
    }

    @Test
    @DisplayName("GET /km-to-mi: 1.609344 км = 1 миля")
    void kilometersToMiles() throws Exception {
        mockMvc.perform(get("/api/convert/km-to-mi").param("value", "1.609344"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", closeTo(1.0, 1e-6)));
    }

    @Test
    @DisplayName("GET /kg-to-lb: 10 кг ≈ 22.046 фунта")
    void kilogramsToPounds() throws Exception {
        mockMvc.perform(get("/api/convert/kg-to-lb").param("value", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", closeTo(22.046226, 1e-5)));
    }

    @Test
    @DisplayName("Неизвестный тип преобразования → 400")
    void unknownType() throws Exception {
        mockMvc.perform(get("/api/convert/foo-to-bar").param("value", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("Ниже абсолютного нуля → 400")
    void belowAbsoluteZero() throws Exception {
        mockMvc.perform(get("/api/convert/c-to-k").param("value", "-300"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Нет параметра value или он не число → 400")
    void invalidValue() throws Exception {
        mockMvc.perform(get("/api/convert/c-to-f"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/convert/c-to-f").param("value", "abc"))
                .andExpect(status().isBadRequest());
    }
}
