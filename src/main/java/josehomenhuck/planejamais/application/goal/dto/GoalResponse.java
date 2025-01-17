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
    private Double value;
    private LocalDateTime date;
}


/*
/goal

POST ("Register a goal for the user")

PUT ("Update the goal of the user")

DELETE ("Delete the goal of the user")
 */