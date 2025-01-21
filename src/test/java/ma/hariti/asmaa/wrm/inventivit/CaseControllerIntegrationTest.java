package ma.hariti.asmaa.wrm.inventivit;

import ma.hariti.asmaa.wrm.inventivit.dto.CreateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.UpdateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.entity.Case;
import ma.hariti.asmaa.wrm.inventivit.repository.CaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.yaml")
class CaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CaseRepository caseRepository;

    @AfterEach
    void cleanup() {
        caseRepository.deleteAll();
    }

    @Nested
    @DisplayName("Create Case Integration Tests")
    class CreateCaseTests {

        @Test
        @DisplayName("Should successfully create a case")
        void shouldCreateCase() throws Exception {
            CreateCaseDTO createCaseDTO = new CreateCaseDTO("Test Case", "Test Description");
            String requestBody = objectMapper.writeValueAsString(createCaseDTO);

            mockMvc.perform(post("/cases")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.title").value("Test Case"))
                    .andExpect(jsonPath("$.data.description").value("Test Description"))
                    .andExpect(jsonPath("$.data.caseId").isNumber());
        }

        @Test
        @DisplayName("Should return 400 when title is null")
        void shouldReturn400WhenTitleIsNull() throws Exception {
            CreateCaseDTO createCaseDTO = new CreateCaseDTO(null, "Test Description");
            String requestBody = objectMapper.writeValueAsString(createCaseDTO);

            mockMvc.perform(post("/cases")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when description is null")
        void shouldReturn400WhenDescriptionIsNull() throws Exception {
            CreateCaseDTO createCaseDTO = new CreateCaseDTO("Test Case", null);
            String requestBody = objectMapper.writeValueAsString(createCaseDTO);

            mockMvc.perform(post("/cases")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Get Case Integration Tests")
    class GetCaseTests {

        @Test
        @DisplayName("Should successfully retrieve a case")
        void shouldGetCase() throws Exception {
            Case savedCase = createAndSaveCase("Test Case", "Test Description");

            mockMvc.perform(get("/cases/{id}", savedCase.getCaseId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.caseId").value(savedCase.getCaseId()))
                    .andExpect(jsonPath("$.data.title").value("Test Case"))
                    .andExpect(jsonPath("$.data.description").value("Test Description"));
        }

        @Test
        @DisplayName("Should return 404 when case not found")
        void shouldReturn404WhenCaseNotFound() throws Exception {
            mockMvc.perform(get("/cases/{id}", 999))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Update Case Integration Tests")
    class UpdateCaseTests {

        @Test
        @DisplayName("Should successfully update a case")
        void shouldUpdateCase() throws Exception {
            Case savedCase = createAndSaveCase("Original Title", "Original Description");
            UpdateCaseDTO updateCaseDTO = new UpdateCaseDTO("Updated Title", "Updated Description");
            String requestBody = objectMapper.writeValueAsString(updateCaseDTO);

            mockMvc.perform(put("/cases/{id}", savedCase.getCaseId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.caseId").value(savedCase.getCaseId()))
                    .andExpect(jsonPath("$.data.title").value("Updated Title"))
                    .andExpect(jsonPath("$.data.description").value("Updated Description"));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent case")
        void shouldReturn404WhenUpdatingNonExistentCase() throws Exception {
            UpdateCaseDTO updateCaseDTO = new UpdateCaseDTO("Updated Title", "Updated Description");
            String requestBody = objectMapper.writeValueAsString(updateCaseDTO);

            mockMvc.perform(put("/cases/{id}", 999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 when update DTO is invalid")
        void shouldReturn400WhenUpdateDTOIsInvalid() throws Exception {
            Case savedCase = createAndSaveCase("Original Title", "Original Description");
            UpdateCaseDTO updateCaseDTO = new UpdateCaseDTO(null, null);
            String requestBody = objectMapper.writeValueAsString(updateCaseDTO);

            mockMvc.perform(put("/cases/{id}", savedCase.getCaseId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Delete Case Integration Tests")
    class DeleteCaseTests {

        @Test
        @DisplayName("Should successfully delete a case")
        void    shouldDeleteCase() throws Exception {
            Case savedCase = createAndSaveCase("Test Case", "Test Description");

            mockMvc.perform(delete("/cases/{id}", savedCase.getCaseId()))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/cases/{id}", savedCase.getCaseId()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent case")
        void shouldReturn404WhenDeletingNonExistentCase() throws Exception {
            mockMvc.perform(delete("/cases/{id}", 999))
                    .andExpect(status().isNotFound());
        }
    }

    private Case createAndSaveCase(String title, String description) {
        Case newCase = new Case();
        newCase.setTitle(title);
        newCase.setDescription(description);
        return caseRepository.save(newCase);
    }
}
