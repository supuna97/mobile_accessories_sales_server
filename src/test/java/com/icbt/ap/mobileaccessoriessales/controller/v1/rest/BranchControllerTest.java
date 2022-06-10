package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.config.AppConfig;
import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.BranchType;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.DATE_TIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@Import(AppConfig.class)
@WebMvcTest(value = BranchController.class)
class BranchControllerTest {

    private static final String BRANCH_PATH = "/v1/branch";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BranchService branchService;
    @Autowired
    private MessageSource messageSource;

    @Test
    void getBranches() throws Exception {
        final LocalDateTime now = LocalDateTime.now();
        List<Branch> branches = new ArrayList<>();
        branches.add(Branch.builder()
                .id("323432")
                .name("Colombo Branch")
                .address("Colombo")
                .tel("0774935895")
                .type(BranchType.BRANCH)
                .status(BranchStatus.ACTIVE)
                .createdAt(now)
                .build());
        branches.add(Branch.builder()
                .id("43242324")
                .name("Galle Branch")
                .address("Galle")
                .tel("0776288969")
                .type(BranchType.BRANCH)
                .status(BranchStatus.ACTIVE)
                .createdAt(now)
                .build());

        when(branchService.getAll())
                .thenReturn(branches);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BRANCH_PATH)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andReturn();

        log.info("Response: {}", result.getResponse().getContentAsString());
        String expected = "{\"success\":true,\"data\":[{\"id\":\"323432\",\"name\":\"Colombo Branch\"," +
                "\"address\":\"Colombo\",\"tel\":\"0774935895\",\"type\":\"Branch\",\"typeId\":2,\"status\":\"Active\"," +
                "\"statusId\":1,\"createdAt\":\"" + DATE_TIME_FORMATTER.format(now) + "\"}," +
                "{\"id\":\"43242324\",\"name\":\"Galle Branch\"," +
                "\"address\":\"Galle\",\"tel\":\"0776288969\",\"type\":\"Branch\",\"typeId\":2,\"status\":\"Active\"," +
                "\"statusId\":1,\"createdAt\":\"" + DATE_TIME_FORMATTER.format(now) + "\"}]}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void getBranch() throws Exception {
        final LocalDateTime now = LocalDateTime.now();

        final Branch branch = Branch.builder()
                .id("323432")
                .name("Colombo Branch")
                .address("Colombo")
                .tel("0774935895")
                .type(BranchType.BRANCH)
                .status(BranchStatus.ACTIVE)
                .createdAt(now)
                .build();

        when(branchService.getById(Mockito.anyString()))
                .thenReturn(branch);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(BRANCH_PATH + "/323432")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andReturn();

        log.info("Response: {}", result.getResponse().getContentAsString());
        String expected = "{\"success\":true,\"data\":{\"id\":\"323432\",\"name\":\"Colombo Branch\"," +
                "\"address\":\"Colombo\",\"tel\":\"0774935895\",\"type\":\"Branch\",\"typeId\":2,\"status\":\"Active\"," +
                "\"statusId\":1,\"createdAt\":\"" + DATE_TIME_FORMATTER.format(now) + "\"}}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void saveBranch() throws Exception {

        // Send course as body to /students/Student1/courses
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BRANCH_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "   \"name\": \"My Branch\",\n" +
                        "   \"address\": \"Kandy\",\n" +
                        "   \"tel\": \"0486699325\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void updateBranch() throws Exception {
        // Send course as body to /students/Student1/courses
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(BRANCH_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"id\": \"32323\",\n" +
                        "  \"name\": \"My Branch\",\n" +
                        "  \"address\": \"Kandy\",\n" +
                        "  \"tel\": \"0486699325\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void deleteBranch() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(BRANCH_PATH + "/121212")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}