package josehomenhuck.planejamais.application.goal.mapper;

import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.domain.goal.entity.Goal;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GoalMapper {
    public GoalResponse toRecordResponse(Goal goal) {
        LocalDateTime date;

        if (goal.getUpdatedAt() == null) {
            date = goal.getCreatedAt();
        } else {
            date = goal.getUpdatedAt();
        }

        UserResponse userResponse = UserResponse.builder()
                .name(goal.getUser().getName())
                .email(goal.getUser().getEmail())
                .build();

        return GoalResponse.builder()
                .id(goal.getId())
                .name(goal.getName())
                .user(userResponse)
                .description(goal.getDescription())
                .actualValue(goal.getActualValue())
                .targetValue(goal.getTargetValue())
                .date(date)
                .build();
    }

    public Goal toRecord(GoalRequest goalRequest) {
        return Goal.builder()
                .name(goalRequest.getName())
                .description(goalRequest.getDescription())
                .targetValue(goalRequest.getTargetValue())
                .build();
    }
}
