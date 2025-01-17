package josehomenhuck.planejamais.application.goal.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequest {
    private String userEmail;
    private String name;
    private String description;
    private Double value;
}
