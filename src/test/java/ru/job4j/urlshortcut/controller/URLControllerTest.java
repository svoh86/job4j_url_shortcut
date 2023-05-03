package ru.job4j.urlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.UrlShortcutApplication;
import ru.job4j.urlshortcut.dto.URLAddressDTO;
import ru.job4j.urlshortcut.dto.URLCodeDTO;
import ru.job4j.urlshortcut.dto.URLStatisticDTO;
import ru.job4j.urlshortcut.service.URLService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
class URLControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    URLService urlService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void whenSuccessConvert() throws Exception {
        URLAddressDTO urlAddressDTO = new URLAddressDTO("https://ya.ru/search/user");
        String json = objectMapper.writeValueAsString(urlAddressDTO);

        URLCodeDTO urlCodeDTO = new URLCodeDTO("8Nt1fk1F");

        ArgumentCaptor<URLAddressDTO> argumentCaptor = ArgumentCaptor.forClass(URLAddressDTO.class);
        when(urlService.save(argumentCaptor.capture())).thenReturn(urlCodeDTO);

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value("8Nt1fk1F"));
    }

    @Test
    @WithMockUser
    public void whenURLAlreadyRegistered() throws Exception {
        URLAddressDTO urlAddressDTO = new URLAddressDTO("https://ya.ru/search/user");
        String json = objectMapper.writeValueAsString(urlAddressDTO);

        ArgumentCaptor<URLAddressDTO> argumentCaptor = ArgumentCaptor.forClass(URLAddressDTO.class);
        when(urlService.save(argumentCaptor.capture()))
                .thenThrow(new IllegalArgumentException("This url https://ya.ru/search/user is already registered!"));

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Check the entered data"))
                .andExpect(jsonPath("details").value("This url https://ya.ru/search/user is already registered!"));
    }

    @Test
    public void whenRedirectSuccess() throws Exception {
        String code = "8Nt1fk1F";
        when(urlService.redirect(code)).thenReturn("https://ya.ru/search/user");

        mockMvc.perform(get("/redirect/8Nt1fk1F"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("REDIRECT", "https://ya.ru/search/user"));
    }

    @Test
    public void whenRedirectFail() throws Exception {
        String code = "123456";
        when(urlService.redirect(code)).thenThrow(new NoSuchElementException("This code did not found"));

        mockMvc.perform(get("/redirect/123456"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Check the entered data"))
                .andExpect(jsonPath("details").value("This code did not found"));
    }

    @Test
    @WithMockUser
    public void whenGetStatistic() throws Exception {
        URLStatisticDTO urlStatisticDTO = new URLStatisticDTO("https://ya.ru/search/user", 123);
        URLStatisticDTO urlStatisticDTO2 = new URLStatisticDTO("https://ya.ru/search/account", 432);
        List<URLStatisticDTO> list = List.of(urlStatisticDTO, urlStatisticDTO2);

        when(urlService.statistic()).thenReturn(list);

        mockMvc.perform(get("/statistic"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].address", is("https://ya.ru/search/user")))
                .andExpect(jsonPath("$[0].count", is(123)))
                .andExpect(jsonPath("$[1].address", is("https://ya.ru/search/account")))
                .andExpect(jsonPath("$[1].count", is(432)));
    }
}