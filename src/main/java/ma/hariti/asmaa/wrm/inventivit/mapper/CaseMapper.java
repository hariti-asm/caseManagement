package ma.hariti.asmaa.wrm.inventivit.mapper;

import ma.hariti.asmaa.wrm.inventivit.dto.CreateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.dto.UpdateCaseDTO;
import ma.hariti.asmaa.wrm.inventivit.entity.Case;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CaseMapper {

    Case toEntity(CreateCaseDTO createCaseDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", expression = "java(java.time.LocalDateTime.now())")
    void updateCaseFromDto(UpdateCaseDTO updateCaseDTO, @MappingTarget Case case_);
}