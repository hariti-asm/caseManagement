package ma.hariti.asmaa.wrm.inventivit;

import jakarta.persistence.EntityNotFoundException;
import ma.hariti.asmaa.wrm.inventivit.dto.CreateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.UpdateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.entity.Case;
import ma.hariti.asmaa.wrm.inventivit.mapper.CaseMapper;
import ma.hariti.asmaa.wrm.inventivit.repository.CaseRepository;
import ma.hariti.asmaa.wrm.inventivit.service.impl.DefaultCaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCaseServiceTest {

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private CaseMapper caseMapper;

    @InjectMocks
    private DefaultCaseService caseService;

    @Nested
    @DisplayName("Create Case Tests")
    class CreateCaseTests {

        @Test
        @DisplayName("Should successfully create a case")
        void shouldCreateCase() {
            CreateCaseDTO createCaseDTO = new CreateCaseDTO("Test Case", "Test Description");
            Case mappedCase = new Case();
            Case savedCase = new Case();
            savedCase.setCaseId(1L);

            when(caseMapper.toEntity(createCaseDTO)).thenReturn(mappedCase);
            when(caseRepository.save(mappedCase)).thenReturn(savedCase);

            Case result = caseService.createCase(createCaseDTO);

            assertNotNull(result);
            assertEquals(savedCase.getCaseId(), result.getCaseId());
            verify(caseMapper).toEntity(createCaseDTO);
            verify(caseRepository).save(mappedCase);
        }

        @Test
        @DisplayName("Should handle null DTO")
        void shouldHandleNullDTO() {
            assertThrows(IllegalArgumentException.class, () -> caseService.createCase(null));
            verify(caseMapper, never()).toEntity(any());
            verify(caseRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Get Case Tests")
    class GetCaseTests {

        @Test
        @DisplayName("Should successfully retrieve a case")
        void shouldGetCase() {
            Case existingCase = new Case();
            existingCase.setCaseId(1L);
            when(caseRepository.findById(1L)).thenReturn(Optional.of(existingCase));

            Case result = caseService.getCase(1L);

            assertNotNull(result);
            assertEquals(existingCase.getCaseId(), result.getCaseId());
            verify(caseRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when case not found")
        void shouldThrowEntityNotFoundExceptionWhenCaseNotFound() {
            when(caseRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> caseService.getCase(999L));
            verify(caseRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Update Case Tests")
    class UpdateCaseTests {

        @Test
        @DisplayName("Should successfully update a case")
        void shouldUpdateCase() {
            UpdateCaseDTO updateCaseDTO = new UpdateCaseDTO("Updated Title", "Updated Description");
            Case existingCase = new Case();
            existingCase.setCaseId(1L);

            when(caseRepository.findById(1L)).thenReturn(Optional.of(existingCase));
            doNothing().when(caseMapper).updateCaseFromDto(updateCaseDTO, existingCase);

            Case result = caseService.updateCase(1L, updateCaseDTO);

            assertNotNull(result);
            verify(caseRepository).findById(1L);
            verify(caseMapper).updateCaseFromDto(updateCaseDTO, existingCase);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when updating non-existent case")
        void shouldThrowEntityNotFoundExceptionWhenUpdatingNonExistentCase() {
            UpdateCaseDTO updateCaseDTO = new UpdateCaseDTO("Updated Title", "Updated Description");
            when(caseRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> caseService.updateCase(999L, updateCaseDTO));
            verify(caseRepository).findById(999L);
            verify(caseMapper, never()).updateCaseFromDto(any(), any());
        }

        @Test
        @DisplayName("Should handle null DTO when updating")
        void shouldHandleNullDTOWhenUpdating() {
            assertThrows(IllegalArgumentException.class, () -> caseService.updateCase(1L, null));
            verify(caseRepository, never()).findById(any());
            verify(caseMapper, never()).updateCaseFromDto(any(), any());
        }
    }

    @Nested
    @DisplayName("Delete Case Tests")
    class DeleteCaseTests {
        @Test
        @DisplayName("Should successfully delete a case")
        void shouldDeleteCase() {
            when(caseRepository.existsById(1L)).thenReturn(true);
            doNothing().when(caseRepository).deleteById(1L);

            caseService.deleteCase(1L);

            verify(caseRepository).existsById(1L);
            verify(caseRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when deleting non-existent case")
        void shouldThrowEntityNotFoundExceptionWhenDeletingNonExistentCase() {
            when(caseRepository.existsById(999L)).thenReturn(false);

            assertThrows(EntityNotFoundException.class, () -> caseService.deleteCase(999L));
            verify(caseRepository).existsById(999L);
            verify(caseRepository, never()).deleteById(any());
        }
    }
}
