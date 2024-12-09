package josehomenhuck.planejamais.infrastructure.repository;

import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, String> {
    List<FinancialRecord> findAllByUserEmail(String email);
}
