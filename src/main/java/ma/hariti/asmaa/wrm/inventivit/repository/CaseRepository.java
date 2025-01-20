package ma.hariti.asmaa.wrm.inventivit.repository;

import ma.hariti.asmaa.wrm.inventivit.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
}
