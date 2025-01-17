package josehomenhuck.planejamais.application.goal.dto;

import josehomenhuck.planejamais.application.user.dto.UserResponse;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
    private String id;
    private UserResponse user;
    private String name;
    private String description;
    private Double value;
    private LocalDateTime date;
}
