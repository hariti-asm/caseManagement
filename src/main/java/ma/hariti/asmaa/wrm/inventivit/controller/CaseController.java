package ma.hariti.asmaa.wrm.inventivit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.hariti.asmaa.wrm.inventivit.dto.ApiResponseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.CreateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.UpdateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.entity.Case;
import ma.hariti.asmaa.wrm.inventivit.service.CaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<Case>> createCase(@Valid @RequestBody CreateCaseDTO createCaseDTO) {
        log.info("REST request to create Case");
        Case createdCase = caseService.createCase(createCaseDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(createdCase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Case>> getCase(@PathVariable Long id) {
        log.info("REST request to get Case : {}", id);
        Case foundCase = caseService.getCase(id);
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(foundCase));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Case>> updateCase(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCaseDTO updateCaseDTO) {
        log.info("REST request to update Case : {}", id);
        Case updatedCase = caseService.updateCase(id, updateCaseDTO);
        return ResponseEntity
                .ok()
                .body(ApiResponseDTO.success(updatedCase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        log.info("REST request to delete Case : {}", id);
        caseService.deleteCase(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}