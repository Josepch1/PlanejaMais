package josehomenhuck.planejamais.domain.financialRecord.service;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialSummary;

import java.util.List;

public interface FinancialRecordService {
    FinancialRecordResponse create(FinancialRecordRequest recordRequest);

    List<FinancialRecordResponse> findAll();

    FinancialSummary getSummary();

    FinancialRecordResponse update(String id, FinancialRecordRequest recordRequest);

    FinancialRecordResponse deleteById(String id);

    void deleteAll();
}
