package josehomenhuck.planejamais.domain.financialrecord.enums;

import lombok.Getter;

@Getter
public enum FinancialRecordType {
    INCOME("Income"),
    EXPENSE("Expense");

    private final String description;

    FinancialRecordType(String description) {
        this.description = description;
    }

    public boolean isIncome() {
        return this == INCOME;
    }

    public boolean isExpense() {
        return this == EXPENSE;
    }
}
