package josehomenhuck.planejamais.domain.goal.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalFundsRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.application.goal.impl.GoalServiceImpl;
import josehomenhuck.planejamais.application.goal.mapper.GoalMapper;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.financialrecord.service.FinancialRecordService;
import josehomenhuck.planejamais.domain.goal.entity.Goal;
import josehomenhuck.planejamais.domain.user.entity.User;
import josehomenhuck.planejamais.domain.user.service.UserService;
import josehomenhuck.planejamais.infrastructure.repository.GoalRepository;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {
  private GoalServiceImpl underTest;

  @Mock
  private GoalRepository goalRepository;

  @Mock
  private UserService userService;

  @Mock
  private FinancialRecordService financialRecordService;

  @Mock
  private GoalMapper goalMapper;

  @Mock
  private UserMapper userMapper;

  @BeforeEach
  void setUp() {
    underTest = new GoalServiceImpl(goalRepository, goalMapper, userService, userMapper, financialRecordService);
  }

  private User createDefaultUser() {
    return new User(
        "123",
        "Test User",
        "test@test.com",
        "password123",
        null,
        null);
  }

  private GoalRequest createGoalRequest(String email, String name, String description, Double targetValue) {
    return GoalRequest.builder()
        .email(email)
        .name(name)
        .description(description)
        .targetValue(targetValue)
        .build();
  }

  @Test
  void testCreate() {
    // Given
    User user = createDefaultUser();
    UserResponse userResponse = userMapper.toResponse(user);
    GoalRequest goalRequest = createGoalRequest("test@test.com", "GoalTest", "Goal Test Description", 10.0);

    Goal expectedGoal = Goal.builder()
        .id(1L)
        .name(goalRequest.getName())
        .description(goalRequest.getDescription())
        .targetValue(goalRequest.getTargetValue())
        .user(user)
        .build();
    GoalResponse expectedGoalResponse = GoalResponse.builder()
        .id(expectedGoal.getId())
        .user(userResponse)
        .name(expectedGoal.getName())
        .description(expectedGoal.getDescription())
        .actualValue(expectedGoal.getActualValue())
        .targetValue(expectedGoal.getTargetValue())
        .date(expectedGoal.getCreatedAt())
        .build();

    when(userService.findByEmail("test@test.com")).thenReturn(user);
    when(goalMapper.toRecord(goalRequest)).thenReturn(expectedGoal);
    when(goalRepository.save(expectedGoal)).thenReturn(expectedGoal);
    when(goalMapper.toRecordResponse(expectedGoal)).thenReturn(expectedGoalResponse);

    // When
    GoalResponse actualGoalResponse = underTest.create(goalRequest);

    // Then
    assertNotNull(actualGoalResponse);
    assertEquals(expectedGoalResponse.getId(), actualGoalResponse.getId());
    assertEquals(expectedGoalResponse.getName(), actualGoalResponse.getName());
    assertEquals(expectedGoalResponse.getDescription(), actualGoalResponse.getDescription());
    assertEquals(expectedGoalResponse.getTargetValue(), actualGoalResponse.getTargetValue());
  }

  @Test
  void testDeleteById() {
    // Given
    User user = createDefaultUser();
    Goal goal = Goal.builder()
        .id(1L)
        .name("GoalTest")
        .description("Goal Test Description")
        .targetValue(10.0)
        .user(user)
        .build();
    GoalResponse expectedGoalResponse = GoalResponse.builder()
        .id(goal.getId())
        .user(userMapper.toResponse(user))
        .name(goal.getName())
        .description(goal.getDescription())
        .actualValue(goal.getActualValue())
        .targetValue(goal.getTargetValue())
        .date(goal.getCreatedAt())
        .build();

    when(goalRepository.findById(goal.getId())).thenReturn(java.util.Optional.of(goal));
    when(goalMapper.toRecordResponse(goal)).thenReturn(expectedGoalResponse);

    // When
    GoalResponse actualGoalResponse = underTest.deleteById(goal.getId(), user.getEmail());

    // Then
    assertNotNull(actualGoalResponse);
    assertEquals(expectedGoalResponse.getId(), actualGoalResponse.getId());
    assertEquals(expectedGoalResponse.getName(), actualGoalResponse.getName());
    assertEquals(expectedGoalResponse.getDescription(), actualGoalResponse.getDescription());
    assertEquals(expectedGoalResponse.getTargetValue(), actualGoalResponse.getTargetValue());
  }

  @Test
  void testFindAllByUserEmail() {
    // Given
    User user = createDefaultUser();
    UserResponse userResponse = userMapper.toResponse(user);
    List<Goal> goals = new ArrayList<>();

    for (int i = 1; i <= 2; i++) {
      Goal goal = Goal.builder()
          .id((long) i)
          .name("Goal" + i)
          .description("Description" + i)
          .targetValue(100.0 * i)
          .user(user)
          .build();
      goals.add(goal);
    }

    List<GoalResponse> goalResponses = new ArrayList<>();
    for (Goal goal : goals) {
      GoalResponse goalResponse = GoalResponse.builder()
          .id(goal.getId())
          .user(userResponse)
          .name(goal.getName())
          .description(goal.getDescription())
          .actualValue(goal.getActualValue())
          .targetValue(goal.getTargetValue())
          .date(goal.getCreatedAt())
          .build();
      goalResponses.add(goalResponse);
    }

    GoalFindAllResponse expectedResponse = GoalFindAllResponse.builder()
        .user(userResponse)
        .goals(goalResponses)
        .build();

    when(userService.findByEmail(user.getEmail())).thenReturn(user);
    when(goalRepository.findAllByUserEmail(user.getEmail())).thenReturn(goals);
    when(goalMapper.toRecordResponse(goals.get(0))).thenReturn(goalResponses.get(0));
    when(goalMapper.toRecordResponse(goals.get(1))).thenReturn(goalResponses.get(1));

    // When
    GoalFindAllResponse actualResponse = underTest.findAllByUserEmail(user.getEmail());

    // Then
    assertNotNull(actualResponse);
    assertEquals(expectedResponse.getUser(), actualResponse.getUser());
    assertEquals(expectedResponse.getGoals().size(), actualResponse.getGoals().size());
    for (int i = 0; i < expectedResponse.getGoals().size(); i++) {
      assertEquals(expectedResponse.getGoals().get(i).getId(), actualResponse.getGoals().get(i).getId());
      assertEquals(expectedResponse.getGoals().get(i).getName(), actualResponse.getGoals().get(i).getName());
      assertEquals(expectedResponse.getGoals().get(i).getDescription(),
          actualResponse.getGoals().get(i).getDescription());
      assertEquals(expectedResponse.getGoals().get(i).getTargetValue(),
          actualResponse.getGoals().get(i).getTargetValue());
    }
  }

  @Test
void testUpdate() {
    // Given
    User user = createDefaultUser();
    GoalRequest goalRequest = createGoalRequest("test@test.com", "UpdatedGoal", "Updated Description", 20.0);
    Goal existingGoal = Goal.builder()
        .id(1L)
        .name("GoalTest")
        .description("Goal Test Description")
        .targetValue(10.0)
        .user(user)
        .build();
    Goal updatedGoal = Goal.builder()
        .id(existingGoal.getId())
        .name(goalRequest.getName())
        .description(goalRequest.getDescription())
        .targetValue(goalRequest.getTargetValue())
        .user(user)
        .build();
    GoalResponse expectedGoalResponse = GoalResponse.builder()
        .id(updatedGoal.getId())
        .user(userMapper.toResponse(user))
        .name(updatedGoal.getName())
        .description(updatedGoal.getDescription())
        .actualValue(updatedGoal.getActualValue())
        .targetValue(updatedGoal.getTargetValue())
        .date(updatedGoal.getCreatedAt())
        .build();

    when(userService.findByEmail(user.getEmail())).thenReturn(user);
    when(goalRepository.findById(existingGoal.getId())).thenReturn(java.util.Optional.of(existingGoal));
    doReturn(updatedGoal).when(goalRepository).save(any(Goal.class));
    when(goalMapper.toRecordResponse(updatedGoal)).thenReturn(expectedGoalResponse);

    // When
    GoalResponse actualGoalResponse = underTest.update(existingGoal.getId(), goalRequest);

    // Then
    assertNotNull(actualGoalResponse);
    assertEquals(expectedGoalResponse.getId(), actualGoalResponse.getId());
    assertEquals(expectedGoalResponse.getName(), actualGoalResponse.getName());
    assertEquals(expectedGoalResponse.getDescription(), actualGoalResponse.getDescription());
    assertEquals(expectedGoalResponse.getTargetValue(), actualGoalResponse.getTargetValue());
  }


  @Test
  void testAddFunds() {
    // Given
    User user = createDefaultUser();

    Goal goal = Goal.builder()
        .id(1L)
        .name("GoalTest")
        .description("Goal Test Description")
        .targetValue(10.0)
        .actualValue(0.0)
        .user(user)
        .build();
    GoalFundsRequest goalFundsRequest = GoalFundsRequest.builder()
        .email(user.getEmail())
        .value(5.0)
        .build();
    Goal updatedGoal = Goal.builder()
        .id(goal.getId())
        .name(goal.getName())
        .description(goal.getDescription())
        .targetValue(goal.getTargetValue())
        .actualValue(goal.getActualValue() + goalFundsRequest.getValue())
        .user(user)
        .build();
    GoalResponse expectedGoalResponse = GoalResponse.builder()
        .id(updatedGoal.getId())
        .user(userMapper.toResponse(user))
        .name(updatedGoal.getName())
        .description(updatedGoal.getDescription())
        .actualValue(updatedGoal.getActualValue())
        .targetValue(updatedGoal.getTargetValue())
        .date(updatedGoal.getCreatedAt())
        .build();

    when(goalRepository.findById(goal.getId())).thenReturn(java.util.Optional.of(goal));
    doReturn(updatedGoal).when(goalRepository).save(any(Goal.class));
    when(goalMapper.toRecordResponse(updatedGoal)).thenReturn(expectedGoalResponse);
    when(financialRecordService.getTotalExpense(user.getEmail())).thenReturn(0.0);
    when(financialRecordService.getTotalIncome(user.getEmail())).thenReturn(1000.0);

    // When
    GoalResponse actualGoalResponse = underTest.addFunds(goal.getId(), goalFundsRequest);

    // Then
    assertNotNull(actualGoalResponse);
    assertEquals(expectedGoalResponse.getId(), actualGoalResponse.getId());
    assertEquals(expectedGoalResponse.getName(), actualGoalResponse.getName());
    assertEquals(expectedGoalResponse.getDescription(), actualGoalResponse.getDescription());
    assertEquals(expectedGoalResponse.getTargetValue(), actualGoalResponse.getTargetValue());
    assertEquals(expectedGoalResponse.getActualValue(), actualGoalResponse.getActualValue());
  }

  @Test
  void testRemoveFunds() {
    // Given
    User user = createDefaultUser();
    Goal goal = Goal.builder()
        .id(1L)
        .name("GoalTest")
        .description("Goal Test Description")
        .targetValue(10.0)
        .actualValue(10.0)
        .user(user)
        .build();
    GoalFundsRequest goalFundsRequest = GoalFundsRequest.builder()
        .email(user.getEmail())
        .value(5.0)
        .build();
    Goal updatedGoal = Goal.builder()
        .id(goal.getId())
        .name(goal.getName())
        .description(goal.getDescription())
        .targetValue(goal.getTargetValue())
        .actualValue(goal.getActualValue() - goalFundsRequest.getValue())
        .user(user)
        .build();
    GoalResponse expectedGoalResponse = GoalResponse.builder()
        .id(updatedGoal.getId())
        .user(userMapper.toResponse(user))
        .name(updatedGoal.getName())
        .description(updatedGoal.getDescription())
        .actualValue(updatedGoal.getActualValue())
        .targetValue(updatedGoal.getTargetValue())
        .date(updatedGoal.getCreatedAt())
        .build();

    when(goalRepository.findById(goal.getId())).thenReturn(java.util.Optional.of(goal));
    when(goalMapper.toRecordResponse(updatedGoal)).thenReturn(expectedGoalResponse);
    doReturn(updatedGoal).when(goalRepository).save(any(Goal.class));

    // When
    GoalResponse actualGoalResponse = underTest.removeFunds(goal.getId(), goalFundsRequest);

    // Then
    assertNotNull(actualGoalResponse);
    assertEquals(expectedGoalResponse.getId(), actualGoalResponse.getId());
    assertEquals(expectedGoalResponse.getName(), actualGoalResponse.getName());
    assertEquals(expectedGoalResponse.getDescription(), actualGoalResponse.getDescription());
    assertEquals(expectedGoalResponse.getTargetValue(), actualGoalResponse.getTargetValue());
    assertEquals(expectedGoalResponse.getActualValue(), actualGoalResponse.getActualValue());
  }
}