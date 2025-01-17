package josehomenhuck.planejamais.domain.financialrecord.service;

import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialFindAllResponse;

public interface FinancialRecordService {
    FinancialRecordResponse create(FinancialRecordRequest recordRequest);

    FinancialFindAllResponse findAllByUserEmail(String email);

    FinancialSummary getSummary(String email);

    FinancialRecordResponse update(String id, FinancialRecordRequest recordRequest);

    FinancialRecordResponse deleteById(String id);

    Double getTotalIncome(String email);

    Double getTotalExpense(String email);
}
