package com.nus.project.capstone.idc.web;

import com.nus.project.capstone.model.persistence.base.UserRepository;
import com.nus.project.capstone.model.persistence.game.GameTeamRepository;
import com.nus.project.capstone.model.persistence.game.GamesRepository;
import com.nus.project.capstone.model.persistence.idc.*;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(value = {IdcGroupController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(IdcGroupController.class)
@ComponentScan(basePackages = "com.nus.project.capstone.idc")
public class IdcGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdcGroupRepository idcGroupRepository;

    @MockBean
    private IdcTeamRepository idcTeamRepository;

    @MockBean
    private GamesRepository gamesRepository;

    @MockBean
    private IdcMetricsRepository idcMetricsRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GameTeamRepository gameTeamRepository;

    @MockBean
    private PresentationRepository presentationRepository;

    @Test
    void testapi() throws Exception {

        val i = IdcTeamJpaEntities.builder()
                .id(UUID.randomUUID())
                .teamName("Some team name")
                .presentations(new HashSet<>(Collections.singletonList(
                        PresentationJpaEntities.builder()
                                .stage("stage 1")
                                .build()
                )))
                .build();

        Mockito.when(idcTeamRepository.findAll()).thenReturn(Collections.singletonList(i));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/idcteam/view-all-teams")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
