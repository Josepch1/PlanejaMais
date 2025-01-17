package josehomenhuck.planejamais.application.financialrecord.dto;

import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import lombok.*;

import java.util.List;

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
    private List<GoalResponse> goals;
}
