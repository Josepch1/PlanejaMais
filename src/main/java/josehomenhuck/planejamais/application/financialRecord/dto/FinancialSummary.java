package josehomenhuck.planejamais.application.financialRecord.dto;

import josehomenhuck.planejamais.application.user.dto.UserResponse;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummary {
    private UserResponse user;
    private Double totalIncome;
    private Double totalExpense;
    private Double balance;
}
