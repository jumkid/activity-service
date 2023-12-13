package com.jumkid.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityAPITest {

    @Autowired
    private MockMvc mockMvc;

    private Activity activity;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private MapperContext mapperContext;

    @MockBean
    private ActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
        try {
            activity = TestSetup.buildActivity();
            ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);

            when(activityRepository.save(any(ActivityEntity.class))).thenReturn(activityEntity);

            when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activityEntity));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    void shouldGetActivitiesByUser() throws Exception {
        ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);
        when(activityRepository.findByUser(anyString())).thenReturn(List.of(activityEntity));

        mockMvc.perform(get("/activities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(activity.getId()))
                .andExpect(jsonPath("$.[0].name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    void whenGivenActivityId_shouldGetActivity() throws Exception {
        mockMvc.perform(get("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(activity.getId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    void whenAssessAsGuest_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(get("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="notOwner", password = "notOwner", authorities="USER_ROLE")
    void whenAssessAsNoOwner_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(get("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    void whenGivenActivity_shouldSaveActivityEntity() throws Exception {
        mockMvc.perform(post("/activities")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(activity.getId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    void whenSaveAsGuest_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    void whenGivenActivityIdAndActivity_shouldUpdateActivityEntity() throws Exception{
        mockMvc.perform(put("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(activity.getId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    void whenUpdateAsGuest_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(put("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="notOwner", authorities="USER_ROLE")
    void whenUpdateAsNotOwner_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(put("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    void whenGivenActivityId_shouldDeleteActivityEntity() throws Exception{
        mockMvc.perform(delete("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    void whenDeleteAsGuest_shouldReturnForbiddenStat() throws Exception{
        mockMvc.perform(delete("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="notOwner", authorities="USER_ROLE")
    void whenDeleteAsNotOwner_shouldReturnForbiddenStat() throws Exception{
        mockMvc.perform(delete("/activities/" + activity.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
