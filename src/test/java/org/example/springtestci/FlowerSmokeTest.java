package org.example.springtestci;

import org.example.springtestci.ui.FlowerDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 애플리케이션 전 계층이 실제로 연결되어 동작하는지 확인하는 스모크 테스트
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FlowerSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("애플리케이션 상태를 확인하는 헬스 체크가 정상 응답한다")
    void given_running_application_when_health_checked_then_returns_up() throws Exception {
        // when & then: 액추에이터 헬스 엔드포인트가 UP 상태를 반환
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("꽃 등록부터 조회까지 전체 흐름이 정상 동작한다")
    void given_new_flower_when_registered_then_reflected_in_count_and_list() throws Exception {
        // given: 등록할 꽃 요청 DTO 준비
        FlowerDTO request = new FlowerDTO("장미", "빨강", 5000);

        // when: REST API로 꽃 등록 요청
        mockMvc.perform(post("/api/flowers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(request)));

        // then: 등록 이후 전체 개수가 1로 반영됨
        mockMvc.perform(get("/api/flowers/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // then: 등록 이후 전체 목록에 등록한 꽃이 포함됨
        mockMvc.perform(get("/api/flowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("장미"))
                .andExpect(jsonPath("$[0].color").value("빨강"))
                .andExpect(jsonPath("$[0].price").value(5000));
    }
}
