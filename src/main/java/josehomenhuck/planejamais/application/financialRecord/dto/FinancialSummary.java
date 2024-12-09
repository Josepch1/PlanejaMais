package josehomenhuck.planejamais.application.financialRecord.dto;

import josehomenhuck.planejamais.domain.user.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummary {
    private User user;
    private Double totalIncome;
    private Double totalExpense;
    private Double balance;
}
