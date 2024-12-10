package josehomenhuck.planejamais.application.financialrecord.dto;

import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.domain.financialrecord.enums.FinancialRecordType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordResponse {
    private String id;
    private UserResponse user;
    private String description;
    private FinancialRecordType type;
    private Double value;
    private LocalDateTime date;
}
