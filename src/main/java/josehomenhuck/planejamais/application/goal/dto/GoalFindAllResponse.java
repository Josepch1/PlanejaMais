package josehomenhuck.planejamais.application.goal.dto;

import josehomenhuck.planejamais.application.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GoalFindAllResponse {
    private UserResponse user;
    private List<GoalResponse> goals;
}
