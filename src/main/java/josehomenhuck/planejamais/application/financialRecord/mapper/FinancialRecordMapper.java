package josehomenhuck.planejamais.application.financialRecord.mapper;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FinancialRecordMapper {
    public FinancialRecordResponse toRecordResponse(FinancialRecord financialRecord) {
        LocalDateTime date;

        if (financialRecord.getUpdatedAt() == null) {
            date = financialRecord.getCreatedAt();
        } else {
            date = financialRecord.getUpdatedAt();
        }

        return FinancialRecordResponse.builder()
                .id(financialRecord.getId())
                .description(financialRecord.getDescription())
                .type(financialRecord.getType())
                .value(financialRecord.getValue())
                .date(date)
                .build();
    }

    public FinancialRecord toRecord(FinancialRecordRequest financialRecordRequest) {
        return FinancialRecord.builder()
                .description(financialRecordRequest.getDescription())
                .type(financialRecordRequest.getType())
                .value(financialRecordRequest.getValue())
                .build();
    }
}
