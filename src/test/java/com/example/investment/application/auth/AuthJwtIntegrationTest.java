package com.example.investment.application.auth;

import com.example.investment.presentation.auth.dto.LoginRequest;
import com.example.investment.presentation.auth.dto.SignupRequest;
import com.example.investment.testsupport.SecureTestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        properties = {
                // 테스트에서 jwt.secret 반드시 존재해야 함
                "jwt.secret=THIS_IS_A_DEMO_SECRET_KEY_CHANGE_LATER",
                // H2 (원하면 application-test.yml로 빼도 됨)
                "spring.datasource.url=jdbc:h2:mem:investment;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create"
        }
)
@AutoConfigureMockMvc
@Import(SecureTestController.class)
public class AuthJwtIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void signup_login_then_access_secure_endpoint_with_bearer_token() throws Exception {
        // 1) signup
        SignupRequest signup = new SignupRequest("a@test.com", "1234", "nick");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk());

        // 2) login -> token 받기 (jsonPath로 뽑아서 다음 요청에 사용)
        LoginRequest login = new LoginRequest("a@test.com", "1234");

        String tokenJson = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(tokenJson).get("token").asText();

        // 3) 토큰으로 보호 API 접근
        mockMvc.perform(get("/secure/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                // principal이 userId(Long) 이므로 숫자 응답
                .andExpect(content().string(org.hamcrest.Matchers.matchesPattern("\\d+")));
    }

    @Test
    void access_secure_endpoint_without_token_returns_401() throws Exception {
        mockMvc.perform(get("/secure/me"))
                .andExpect(status().isUnauthorized());
    }
}
