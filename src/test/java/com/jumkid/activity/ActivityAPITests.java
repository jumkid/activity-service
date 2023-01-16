package com.jumkid.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import com.jumkid.activity.service.mapper.MapperContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:10092", "port=10092" })
public class ActivityAPITests {

    @Autowired
    private MockMvc mockMvc;

    private Activity activity;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private MapperContext mapperContext;

    @MockBean
    private ActivityRepository activityRepository;

    @Before
    public void setup() {
        try {
            activity = APITestsSetup.buildActivity();
            ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);

            when(activityRepository.save(any(ActivityEntity.class))).thenReturn(activityEntity);

            when(activityRepository.findById(activity.getActivityId()))
                    .thenReturn(Optional.of(activityEntity));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    public void shouldGetActivitiesByUser() throws Exception {
        ActivityEntity activityEntity = activityMapper.dtoToEntity(activity, mapperContext);
        when(activityRepository.findByUser(anyString())).thenReturn(List.of(activityEntity));

        mockMvc.perform(get("/activities/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].activityId").value(activity.getActivityId()))
                .andExpect(jsonPath("$.[0].name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    public void whenGivenActivityId_shouldGetActivity() throws Exception {
        mockMvc.perform(get("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityId").value(activity.getActivityId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    public void whenAssessAsGuest_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(get("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="notOwner", password = "notOwner", authorities="USER_ROLE")
    public void whenAssessAsNoOwner_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(get("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    public void whenGivenActivity_shouldSaveActivityEntity() throws Exception {
        mockMvc.perform(post("/activities")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityId").value(activity.getActivityId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    public void whenSaveAsGuest_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    public void whenGivenActivityIdAdActivity_shouldUpdateActivityEntity() throws Exception{
        mockMvc.perform(put("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityId").value(activity.getActivityId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    public void whenUpdateAsGuest_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(put("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="notOwner", authorities="USER_ROLE")
    public void whenUpdateAsNotOwner_shouldReturnForbiddenStat() throws Exception {
        mockMvc.perform(put("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test", password="test", authorities="USER_ROLE")
    public void whenGivenActivityId_shouldDeleteActivityEntity() throws Exception{
        mockMvc.perform(delete("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username="guest", authorities="GUEST_ROLE")
    public void whenDeleteAsGuest_shouldReturnForbiddenStat() throws Exception{
        mockMvc.perform(delete("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="notOwner", authorities="USER_ROLE")
    public void whenDeleteAsNotOwner_shouldReturnForbiddenStat() throws Exception{
        mockMvc.perform(delete("/activities/" + activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
