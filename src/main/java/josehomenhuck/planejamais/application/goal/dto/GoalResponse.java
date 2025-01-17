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
    private Long id;
    private UserResponse user;
    private String name;
    private String description;
    private Double actualValue;
    private Double targetValue;
    private LocalDateTime date;
}