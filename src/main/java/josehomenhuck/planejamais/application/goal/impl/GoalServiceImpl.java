package josehomenhuck.planejamais.application.goal.impl;

import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalFundsRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.application.goal.mapper.GoalMapper;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.financialrecord.service.FinancialRecordService;
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
    private final FinancialRecordService financialRecordService;

    public GoalServiceImpl(GoalRepository goalRepository,
            GoalMapper goalMapper,
            UserService userService,
            UserMapper userMapper,
            FinancialRecordService financialRecordService) {
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.financialRecordService = financialRecordService;
    }

    @Override
    @Transactional
    public GoalResponse create(GoalRequest goalRequest) {
        User user = userService.findByEmail(goalRequest.getEmail());

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
                .goals(financialRecordResponses)
                .build();
    }

    @Override
    @Transactional
    public GoalResponse update(Long id, GoalRequest goalRequest) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        User user = userService.findByEmail(goalRequest.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        } else if (!goal.getUser().getEmail().equalsIgnoreCase(goalRequest.getEmail())) {
            throw new IllegalArgumentException("User not allowed to update this record");
        }

        goal.setName(goalRequest.getName());
        goal.setDescription(goalRequest.getDescription());
        goal.setTargetValue(goalRequest.getTargetValue());

        Goal updatedGoal = goalRepository.save(goal);

        return goalMapper.toRecordResponse(updatedGoal);
    }

    @Override
    public GoalResponse deleteById(Long id, String email) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        if (!goal.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("User not allowed to delete this record");
        }

        goalRepository.deleteById(id);

        return goalMapper.toRecordResponse(goal);
    }

    @Override
    public GoalResponse addFunds(Long id, GoalFundsRequest goalRequest) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        String email = goalRequest.getEmail();
        Double value = goalRequest.getValue();

        if (!goal.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("User not allowed to update this record");
        }

        double balance = financialRecordService.getTotalIncome(email) - financialRecordService.getTotalExpense(email);

        if (balance < value) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        goal.setActualValue(goal.getActualValue() + value);

        return goalMapper.toRecordResponse(goalRepository.save(goal));
    }

    @Override
    public GoalResponse removeFunds(Long id, GoalFundsRequest goalRequest) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        String email = goalRequest.getEmail();
        Double value = goalRequest.getValue();

        if (!goal.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalArgumentException("User not allowed to update this record");
        }

        if (goal.getActualValue() < value) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        goal.setActualValue(goal.getActualValue() - value);

        return goalMapper.toRecordResponse(goalRepository.save(goal));
    }
}
