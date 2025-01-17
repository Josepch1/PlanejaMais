package josehomenhuck.planejamais.application.goal.impl;

import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.application.goal.mapper.GoalMapper;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.goal.entity.Goal;
import josehomenhuck.planejamais.domain.goal.service.GoalService;
import josehomenhuck.planejamais.domain.user.entity.User;
import josehomenhuck.planejamais.domain.user.service.UserService;
import josehomenhuck.planejamais.infrastructure.repository.GoalRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public GoalServiceImpl(GoalRepository goalRepository, GoalMapper goalMapper, UserService userService, UserMapper userMapper) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
        this.userService = userService;
        this.userMapper = userMapper;    
    }

    @Override
    @Transactional
    public GoalResponse create(GoalRequest goalRequest) {
        User user = userService.findByEmail(goalRequest.getUserEmail());

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Goal goal = goalMapper.toRecord(goalRequest);

        goal.setUser(user);

        Goal savedGoal = goalRepository.save(goal);

        return goalMapper.toRecordResponse(savedGoal);
    }

    @Override
    public GoalFindAllResponse findAllByUserEmail(String email) {
        List<Goal> financialRecords = goalRepository.findAllByUserEmail(email);

        List<GoalResponse> financialRecordResponses = financialRecords.stream()
                .map(goalMapper::toRecordResponse)
                .toList();

        UserResponse userResponse = userMapper.toResponse(userService.findByEmail(email));

        return GoalFindAllResponse.builder()
                .user(userResponse)
                .financialRecords(financialRecordResponses)
                .build();
    }

    @Override
    @Transactional
    public GoalResponse update(String id, GoalRequest goalRequest) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        goal.setName(goalRequest.getName());
        goal.setDescription(goalRequest.getDescription());
        goal.setValue(goalRequest.getValue());

        Goal updatedGoal = goalRepository.save(goal);

        return goalMapper.toRecordResponse(updatedGoal);
    }

    @Override
    public GoalResponse deleteById(String id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        goalRepository.deleteById(id);

        return goalMapper.toRecordResponse(goal);
    }

    @Override
    public void deleteAll() {
        goalRepository.deleteAll();
    }
}
