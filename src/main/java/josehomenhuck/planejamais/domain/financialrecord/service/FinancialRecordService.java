package josehomenhuck.planejamais.domain.financialrecord.service;

import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.dto.FindAllResponse;

public interface FinancialRecordService {
    FinancialRecordResponse create(FinancialRecordRequest recordRequest);

    FindAllResponse findAllByUserEmail(String email);

    FinancialSummary getSummary(String email);

    FinancialRecordResponse update(String id, FinancialRecordRequest recordRequest);

    FinancialRecordResponse deleteById(String id);

    void deleteAll();
}
