package josehomenhuck.planejamais.domain.goal.service;

import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;

public interface GoalService {
    GoalResponse create(GoalRequest goalRequest);

    GoalFindAllResponse findAllByUserEmail(String email);

    GoalResponse update(Long id, GoalRequest goalRequest);

    GoalResponse deleteById(Long id);
}
