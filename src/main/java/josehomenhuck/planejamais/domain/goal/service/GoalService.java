package josehomenhuck.planejamais.domain.goal.service;

import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalFundsRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;

public interface GoalService {
    GoalResponse create(GoalRequest goalRequest);

    GoalFindAllResponse findAllByUserEmail(String email);

    GoalResponse update(Long id, GoalRequest goalRequest);

    GoalResponse deleteById(Long id, String email);

    GoalResponse addFunds(Long id, GoalFundsRequest goalRequest);

    GoalResponse removeFunds(Long id, GoalFundsRequest goalRequest);
}
