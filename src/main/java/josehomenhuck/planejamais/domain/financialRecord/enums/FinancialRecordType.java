package josehomenhuck.planejamais.domain.record.enums;

import lombok.Getter;

@Getter
public enum RecordType {
    INCOME("Income"),
    EXPENSE("Expense");

    private final String description;

    RecordType(String description) {
        this.description = description;
    }

}
