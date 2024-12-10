package josehomenhuck.planejamais.application.financialRecord.mapper;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
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

        UserResponse userResponse = UserResponse.builder()
                .name(financialRecord.getUser().getName())
                .email(financialRecord.getUser().getEmail())
                .build();

        return FinancialRecordResponse.builder()
                .id(financialRecord.getId())
                .user(userResponse)
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
