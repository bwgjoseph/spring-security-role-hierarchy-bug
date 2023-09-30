package com.bwgjoseph.springsecurityrolehierarchybug;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(MeController.class)
@Import({ SecurityConfig.class})
class MeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = { "ADMIN" }, authorities = { "ROLE_ADMIN" } )
    void test1() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders
                .get("/me"))
            .andDo(MockMvcResultHandlers.print());
    }
}
