package com.jumkid.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumkid.activity.controller.dto.Activity;
import com.jumkid.activity.model.ActivityEntity;
import com.jumkid.activity.repository.ActivityRepository;
import com.jumkid.activity.service.mapper.ActivityMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.test.properties")
public class ActivityAPITests {

    @Autowired
    private MockMvc mockMvc;

    private Activity activity;

    private final ActivityMapper activityMapper = Mappers.getMapper( ActivityMapper.class );

    @MockBean
    private ActivityRepository activityRepository;

    @Before
    public void setup() {
        try {
            activity = TestsSetup.buildActivity();

            when(activityRepository.save(any(ActivityEntity.class)))
                    .thenReturn(activityMapper.dtoToEntity(activity));

            when(activityRepository.findById(-1L)).thenReturn(Optional.of(activityMapper.dtoToEntity(activity)));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void whenGivenActivity_shouldSaveActivityEntity() throws Exception{
        mockMvc.perform(post("/activities")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityId").value(activity.getActivityId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    public void whenGivenActivityIdAdActivity_shouldUpdateActivityEntity() throws Exception{
        mockMvc.perform(put("/activities/"+activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(activity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityId").value(activity.getActivityId()))
                .andExpect(jsonPath("$.name").value(activity.getName()));
    }

    @Test
    public void whenGivenActivityId_shouldDeleteActivityEntity() throws Exception{
        mockMvc.perform(delete("/activities/"+activity.getActivityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
