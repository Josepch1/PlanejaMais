package josehomenhuck.planejamais.application.goal.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalFundsRequest {
    private String email;
    private Double value;
}
