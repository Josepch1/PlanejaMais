package josehomenhuck.planejamais.application.financialrecord.dto;

import josehomenhuck.planejamais.domain.financialrecord.enums.FinancialRecordType;
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
