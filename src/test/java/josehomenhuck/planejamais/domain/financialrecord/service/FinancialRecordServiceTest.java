package josehomenhuck.planejamais.domain.financialrecord.service;

import josehomenhuck.planejamais.application.financialrecord.dto.FinancialFindAllResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.impl.FinancialRecordServiceImpl;
import josehomenhuck.planejamais.application.financialrecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.application.goal.mapper.GoalMapper;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.financialrecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialrecord.enums.FinancialRecordType;
import josehomenhuck.planejamais.domain.goal.entity.Goal;
import josehomenhuck.planejamais.domain.goal.service.GoalService;
import josehomenhuck.planejamais.domain.user.entity.User;
import josehomenhuck.planejamais.domain.user.service.UserService;
import josehomenhuck.planejamais.infrastructure.repository.FinancialRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialRecordServiceTest {

    private FinancialRecordServiceImpl underTest;

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    @Mock
    private UserService userService;

    @Mock
    private GoalService goalService;

    private FinancialRecordMapper financialRecordMapper;

    private GoalMapper goalMapper;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        financialRecordMapper = new FinancialRecordMapper();
        userMapper = new UserMapper();
        goalMapper = new GoalMapper();
        underTest = new FinancialRecordServiceImpl(financialRecordRepository, financialRecordMapper, userService, userMapper, goalService);
    }

    private FinancialRecordRequest createRecordRequest(String email, String description, FinancialRecordType type, double value) {
        return FinancialRecordRequest.builder()
                .userEmail(email)
                .description(description)
                .type(type)
                .value(value)
                .build();
    }


    @Test
    void create() {
        // Given
        String email = "test@test.com";

        User newUser = User.builder()
                .email(email)
                .name("Test")
                .password("123456")
                .build();

        FinancialRecordRequest recordRequest = createRecordRequest(email, "Test", FinancialRecordType.INCOME, 100.0);

        when(userService.findByEmail(newUser.getEmail())).thenReturn(newUser);

        FinancialRecord financialRecord = financialRecordMapper.toRecord(recordRequest);
        financialRecord.setUser(newUser);

        when(financialRecordRepository.save(any(FinancialRecord.class))).thenReturn(financialRecord);

        // When
        underTest.create(recordRequest);

        // Then
        verify(financialRecordRepository).save(any(FinancialRecord.class));
    }


    @Test
    void findAllByUserEmail() {
        // Given
        String email = "test@test.com";

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .name("Test User")
                .password("123456")
                .build();

        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        FinancialRecordRequest recordRequest1 = createRecordRequest(email, "Test", FinancialRecordType.INCOME, 100.0);

        FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
        record1.setUser(user);

        FinancialRecordRequest recordRequest2 = createRecordRequest(email, "SecondTest", FinancialRecordType.EXPENSE, 10.0);

        FinancialRecord record2 = financialRecordMapper.toRecord(recordRequest2);
        record2.setUser(user);

        List<FinancialRecord> records = List.of(record1, record2);
        when(financialRecordRepository.findAllByUserEmail(email)).thenReturn(records);

        // When
        FinancialFindAllResponse result = underTest.findAllByUserEmail(email);

        // Then
        assertNotNull(result);
        verify(financialRecordRepository).findAllByUserEmail(email);
        verifyNoMoreInteractions(financialRecordRepository);
    }


    @Test
    void getSummary() {
        // Given
        String email = "test@test.com";

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .name("Test User")
                .password("123456")
                .build();

        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        UserResponse userResponse = userMapper.toResponse(user);

        FinancialRecordRequest recordRequest1 = createRecordRequest(email, "Test", FinancialRecordType.INCOME, 100.0);

        FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
        assertNotNull(record1, "record1 should not be null");

        FinancialRecordRequest recordRequest2 = createRecordRequest(email, "SecondTest", FinancialRecordType.EXPENSE, 10.0);

        FinancialRecord record2 = financialRecordMapper.toRecord(recordRequest2);
        assertNotNull(record2, "record2 should not be null");

        List<FinancialRecord> records = List.of(record1, record2);
        when(financialRecordRepository.findAllByUserEmail(email)).thenReturn(records);

        Goal goal1 = Goal.builder()
                .id(1L)
                .name("Goal 1")
                .targetValue(1000.0)
                .build();

        Goal goal2 = Goal.builder()
                .id(2L)
                .name("Goal 2")
                .targetValue(2000.0)
                .build();

        GoalResponse goalResponse1 = goalMapper.toResponse(goal1);
        GoalResponse goalResponse2 = goalMapper.toResponse(goal2);

        GoalFindAllResponse goalFindAllResponse = GoalFindAllResponse.builder()
                .user(userResponse)
                .goals(List.of(goalResponse1, goalResponse2))
                .build();

        when(goalService.findAllByUserEmail(email)).thenReturn(goalFindAllResponse);

        // When
        FinancialSummary result = underTest.getSummary(email);

        // Then
        assertNotNull(result);
        assertEquals(90.0, result.getBalance());
        assertEquals(100.0, result.getTotalIncome());
        assertEquals(10.0, result.getTotalExpense());
    }


    @Test
    void update() {
        // Given
        String email = "test@test.com";

        User user = User.builder()
                .email(email)
                .name("Test User")
                .password("123456")
                .build();

        FinancialRecord existingRecord = FinancialRecord.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .description("Old Description")
                .type(FinancialRecordType.EXPENSE)
                .value(50.0)
                .build();

        FinancialRecordRequest updatedRequest = FinancialRecordRequest.builder()
                .userEmail(email)
                .description("Updated Description")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        FinancialRecord updatedRecord = financialRecordMapper.toRecord(updatedRequest);
        updatedRecord.setId(existingRecord.getId());
        updatedRecord.setUser(user);

        when(financialRecordRepository.findById(existingRecord.getId())).thenReturn(Optional.of(existingRecord));
        when(financialRecordRepository.save(any(FinancialRecord.class))).thenReturn(updatedRecord);

        // When
        underTest.update(existingRecord.getId(), updatedRequest);

        // Then
        verify(financialRecordRepository).save(any(FinancialRecord.class));
        verifyNoMoreInteractions(financialRecordRepository);
    }


    @Test
    void deleteById() {
        // Given
        String email = "test@test.com";

        FinancialRecordRequest recordRequest = createRecordRequest(email, "Test", FinancialRecordType.INCOME, 100.0);

        // Create a new User for the FinancialRecord
        User user = User.builder()
                .email(email)
                .name("Test User")
                .password("123456")
                .build();

        // Set the User in the FinancialRecord
        String id = UUID.randomUUID().toString();
        FinancialRecord financialRecord = financialRecordMapper.toRecord(recordRequest);
        financialRecord.setId(id);
        financialRecord.setUser(user);  // Ensure the User is set here

        assertNotNull(financialRecord, "record should not be null");

        // Mock findById to return the record
        when(financialRecordRepository.findById(id)).thenReturn(Optional.of(financialRecord));

        // When
        underTest.deleteById(id);

        // Then
        verify(financialRecordRepository).deleteById(id);
        verifyNoMoreInteractions(financialRecordRepository);
    }
}