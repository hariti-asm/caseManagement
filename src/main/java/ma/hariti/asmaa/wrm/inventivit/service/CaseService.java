package ma.hariti.asmaa.wrm.inventivit.service;

import ma.hariti.asmaa.wrm.inventivit.dto.CreateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.UpdateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.entity.Case;

public interface CaseService {
    Case createCase(CreateCaseDTO createCaseDTO);

    Case getCase(Long id);

    Case updateCase(Long id, UpdateCaseDTO updateCaseDTO);

    void deleteCase(Long id);
}