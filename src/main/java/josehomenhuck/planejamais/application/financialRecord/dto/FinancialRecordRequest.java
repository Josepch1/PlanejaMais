package josehomenhuck.planejamais.application.record.dto;

import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordRequest {
    private String description;
    private FinancialRecordType type;
}
