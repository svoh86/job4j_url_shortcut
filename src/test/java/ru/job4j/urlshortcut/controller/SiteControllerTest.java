package ru.job4j.urlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.UrlShortcutApplication;
import ru.job4j.urlshortcut.domain.Site;
import ru.job4j.urlshortcut.dto.SiteDTO;
import ru.job4j.urlshortcut.dto.SiteDomainDTO;
import ru.job4j.urlshortcut.service.SiteService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UrlShortcutApplication.class)
@AutoConfigureMockMvc
class SiteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SiteService siteService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void whenSuccessRegistration() throws Exception {
        SiteDomainDTO siteDomainDTO = new SiteDomainDTO();
        siteDomainDTO.setDomain("google.com");
        String json = objectMapper.writeValueAsString(siteDomainDTO);

        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setLogin("login");
        siteDTO.setPassword("password");
        siteDTO.setRegistration(true);

        ArgumentCaptor<Site> argumentCaptor = ArgumentCaptor.forClass(Site.class);
        when(siteService.save(argumentCaptor.capture())).thenReturn(siteDTO);

        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("registration").value(true))
                .andExpect(jsonPath("login").value("login"))
                .andExpect(jsonPath("password").value("password"));
    }

    @Test
    public void whenFailRegistration() throws Exception {
        SiteDomainDTO siteDomainDTO = new SiteDomainDTO();
        siteDomainDTO.setDomain("google.com");
        String json = objectMapper.writeValueAsString(siteDomainDTO);

        ArgumentCaptor<Site> argumentCaptor = ArgumentCaptor.forClass(Site.class);
        when(siteService.save(argumentCaptor.capture()))
                .thenThrow(new IllegalArgumentException("This site google.com is already registered!"));

        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Check the entered data"))
                .andExpect(jsonPath("details").value("This site google.com is already registered!"));
    }
}