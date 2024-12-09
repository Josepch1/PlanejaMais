package josehomenhuck.planejamais.application.financialRecord.dto;

import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
import josehomenhuck.planejamais.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordResponse {
    private String id;
    private User user;
    private String description;
    private FinancialRecordType type;
    private Double value;
    private LocalDateTime date;
}
