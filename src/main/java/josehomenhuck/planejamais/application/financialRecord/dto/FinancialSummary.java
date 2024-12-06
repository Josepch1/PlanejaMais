package josehomenhuck.planejamais.application.financialRecord.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummary {
    private Double totalIncome;
    private Double totalExpense;
    private Double balance;
}
