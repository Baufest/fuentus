package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.entities.App;
import ar.com.bbva.fuentus.repositories.AppsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppsRepository appsRepository;

    private App testApp1;
    private App testApp2;
    private List<App> testAppList;

    @BeforeEach
    void setUp() {
        testApp1 = new App();
        testApp1.setId(1L);
        testApp1.setName("test-app-1");
        testApp1.setUuaa("ASTA");
        testApp1.setBitbucketUrl("https://bitbucket.com/test-app-1");

        testApp2 = new App();
        testApp2.setId(2L);
        testApp2.setName("test-app-2");
        testApp2.setUuaa("ASTA");
        testApp2.setBitbucketUrl("https://bitbucket.com/test-app-2");

        testAppList = Arrays.asList(testApp1, testApp2);
    }

    @Test
    void getTest_ShouldReturnAppsForASTA_WhenCalled() throws Exception {
        // Given
        when(appsRepository.findByUuaa(eq("ASTA"))).thenReturn(testAppList);

        // When & Then
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test-app-1"))
                .andExpect(jsonPath("$[0].uuaa").value("ASTA"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("test-app-2"))
                .andExpect(jsonPath("$[1].uuaa").value("ASTA"));
    }

    @Test
    void getTest_ShouldReturnEmptyList_WhenNoAppsFoundForASTA() throws Exception {
        // Given
        when(appsRepository.findByUuaa(eq("ASTA"))).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getIndex_ShouldRedirectToIndexHtml_WhenRootPathAccessed() throws Exception {
        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index.html"));
    }

    @Test
    void getTest_ShouldReturnSingleApp_WhenOnlyOneAppExistsForASTA() throws Exception {
        // Given
        List<App> singleAppList = Arrays.asList(testApp1);
        when(appsRepository.findByUuaa(eq("ASTA"))).thenReturn(singleAppList);

        // When & Then
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("test-app-1"));
    }
}
