package josehomenhuck.planejamais.application.record.dto;

import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponse {
    private String description;
    private FinancialRecordType type;
    private LocalDateTime createdAt;
}
