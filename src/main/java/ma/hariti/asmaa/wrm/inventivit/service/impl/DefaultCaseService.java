package ma.hariti.asmaa.wrm.inventivit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.hariti.asmaa.wrm.inventivit.dto.CreateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.UpdateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.entity.Case;
import ma.hariti.asmaa.wrm.inventivit.mapper.CaseMapper;
import ma.hariti.asmaa.wrm.inventivit.repository.CaseRepository;
import ma.hariti.asmaa.wrm.inventivit.service.CaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultCaseService implements CaseService {

    private final CaseRepository caseRepository;
    private final CaseMapper caseMapper;

    @Override
    public Case createCase(CreateCaseDTO createCaseDTO) {
        if (createCaseDTO == null) {
            throw new IllegalArgumentException("CreateCaseDTO cannot be null");
        }

        log.info("Creating new case with title: {}", createCaseDTO.title());

        Case newCase = caseMapper.toEntity(createCaseDTO);

        return caseRepository.save(newCase);
    }

    @Override
    public Case getCase(Long id) {
        log.info("Fetching case with id: {}", id);
        return caseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + id));
    }

    @Override
    public Case updateCase(Long id, UpdateCaseDTO updateCaseDTO) {
        if (updateCaseDTO == null) {
            throw new IllegalArgumentException("UpdateCaseDTO cannot be null");
        }

        log.info("Updating case with id: {}", id);

        Case existingCase = getCase(id);
        caseMapper.updateCaseFromDto(updateCaseDTO, existingCase);

        return existingCase;
    }

    @Override
    public void deleteCase(Long id) {
        log.info("Deleting case with id: {}", id);

        if (!caseRepository.existsById(id))
            throw new EntityNotFoundException("Case not found with id: " + id);

        caseRepository.deleteById(id);
    }
}