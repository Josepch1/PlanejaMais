package josehomenhuck.planejamais.infrastructure.repository;

import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, String> {
}
