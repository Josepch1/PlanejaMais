package josehomenhuck.planejamais.application.financialrecord.dto;

import josehomenhuck.planejamais.application.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FinancialFindAllResponse {
    private UserResponse user;
    private List<FinancialRecordResponse> financialRecords;
}
