package josehomenhuck.planejamais.application.financialRecord.dto;

import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordRequest {
    private String userEmail;
    private String description;
    private FinancialRecordType type;
    private Double value;
}
