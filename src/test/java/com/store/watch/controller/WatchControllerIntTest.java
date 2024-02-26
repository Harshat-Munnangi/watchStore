package com.store.watch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WatchControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCheckoutWithValidWatchIds() throws Exception {
        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(Arrays.asList("001", "002", "003"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(230));
    }

    @Test
    void shouldNotCheckoutWithInvalidWatchIds() throws Exception {
        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(Arrays.asList("001", "005"))))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Watch with ID/s (005) not found"));
    }

    @Test
    void shouldNotCheckoutWithBadRequest() throws Exception {
        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[001, 1221, 003]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @Test
    void shouldCheckoutWithLargeNumberOfWatchIds() throws Exception {
        String[] largeWatchIds = new String[10000];
        Arrays.fill(largeWatchIds, "001");

        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJson(largeWatchIds)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.price").value(666700));
    }

    @Test
    void shouldNotCheckoutWithInvalidWatchIdsStringFormat() throws Exception {
        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"001\", \"abc\", \"003\"]"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Watch with ID/s (abc) not found"));
    }

    @Test
    void shouldNotCheckoutWithNullWatchIds() throws Exception {
        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCheckoutWithNoContent() throws Exception {
        mockMvc.perform(post("/checkout").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @Test
    void performanceTesting() {
        IntStream.range(0, 1000)
                .parallel()
                .forEach(i -> {
                    try {
                        mockMvc.perform(post("/checkout")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(convertObjectToJson(Arrays.asList("001", "002", "003"))))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.price").value(230));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private String convertObjectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
