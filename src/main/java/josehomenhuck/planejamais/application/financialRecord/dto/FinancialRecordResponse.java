package josehomenhuck.planejamais.application.financialRecord.dto;

import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordResponse {
    private String id;
    private String description;
    private FinancialRecordType type;
    private Double value;
    private LocalDateTime date;
}
