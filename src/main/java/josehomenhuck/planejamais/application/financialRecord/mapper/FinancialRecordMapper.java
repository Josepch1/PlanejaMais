package josehomenhuck.planejamais.application.record.mapper;

import josehomenhuck.planejamais.application.record.dto.RecordRequest;
import josehomenhuck.planejamais.application.record.dto.RecordResponse;
import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import org.springframework.stereotype.Component;

@Component
public class FinancialRecordMapper {
    public RecordResponse toRecordResponse(FinancialRecord record) {
        return RecordResponse.builder()
                .description(record.getDescription())
                .type(record.getType())
                .createdAt(record.getCreatedAt())
                .build();
    }

    public FinancialRecord toRecord(RecordRequest recordRequest) {
        return FinancialRecord.builder()
                .description(recordRequest.getDescription())
                .type(recordRequest.getType())
                .build();
    }
}
