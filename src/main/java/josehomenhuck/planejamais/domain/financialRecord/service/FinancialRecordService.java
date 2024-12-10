package josehomenhuck.planejamais.domain.financialRecord.service;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialRecord.dto.FindAllResponse;

public interface FinancialRecordService {
    FinancialRecordResponse create(FinancialRecordRequest recordRequest);

    FindAllResponse findAllByUserEmail(String email);

    FinancialSummary getSummary(String email);

    FinancialRecordResponse update(String id, FinancialRecordRequest recordRequest);

    FinancialRecordResponse deleteById(String id);

    void deleteAll();
}
