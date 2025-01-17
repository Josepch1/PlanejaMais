package josehomenhuck.planejamais.domain.financialrecord.service;

import josehomenhuck.planejamais.application.financialrecord.dto.FinancialFindAllResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.impl.FinancialRecordServiceImpl;
import josehomenhuck.planejamais.application.financialrecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.financialrecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialrecord.enums.FinancialRecordType;
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

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    private FinancialRecordMapper financialRecordMapper;

    @Mock
    private UserService userService;

    @Mock
    private GoalService goalService;

    private FinancialRecordServiceImpl underTest;

    @BeforeEach
    void setUp() {
        financialRecordMapper = new FinancialRecordMapper();
        UserMapper userMapper = new UserMapper();
        underTest = new FinancialRecordServiceImpl(financialRecordRepository, financialRecordMapper, userService, userMapper, goalService);
    }

    @Test
    void create() {
        // Given
        User newUser = User.builder()
                .email("test@test.com")
                .name("Test")
                .password("123456")
                .build();


        FinancialRecordRequest recordRequest = FinancialRecordRequest.builder()
                .userEmail("test@test.com")
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        when(userService.findByEmail(newUser.getEmail())).thenReturn(newUser);

        FinancialRecord financialRecord = financialRecordMapper.toRecord(recordRequest);

        User user = userService.findByEmail(recordRequest.getUserEmail());
        assertNotNull(user, "user should not be null");

        financialRecord.setUser(user);

        when(financialRecordRepository.save(financialRecord)).thenReturn(financialRecord);

        // When
        underTest.create(recordRequest);

        // Then
        verify(financialRecordRepository).save(financialRecord);
    }

    @Test
    void findAllByUserEmail() {
        // Given
        String email = "test@test.com";

        // Create a User object
        User user = User.builder()
                .email(email)
                .name("Test User")
                .password("123456")
                .build();

        // Create FinancialRecordRequest for the first record
        FinancialRecordRequest recordRequest1 = FinancialRecordRequest.builder()
                .userEmail(email)
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        // Create FinancialRecord and set the User
        FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
        record1.setUser(user);
        assertNotNull(record1, "record1 should not be null");

        // Create FinancialRecordRequest for the second record
        FinancialRecordRequest recordRequest2 = FinancialRecordRequest.builder()
                .userEmail(email)
                .description("SecondTest")
                .type(FinancialRecordType.EXPENSE)
                .value(10.0)
                .build();

        // Create FinancialRecord and set the User
        FinancialRecord record2 = financialRecordMapper.toRecord(recordRequest2);
        record2.setUser(user);
        assertNotNull(record2, "record2 should not be null");

        // Return the list of records
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

        User newUser = User.builder()
                .email(email)
                .name("Test")
                .password("123456")
                .build();

        when(userService.findByEmail(newUser.getEmail())).thenReturn(newUser);

        FinancialRecordRequest recordRequest1 = FinancialRecordRequest.builder()
                .userEmail(email)
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
        assertNotNull(record1, "record1 should not be null");

        FinancialRecordRequest recordRequest2 = FinancialRecordRequest.builder()
                .userEmail(email)
                .description("SecondTest")
                .type(FinancialRecordType.EXPENSE)
                .value(10.0)
                .build();

        FinancialRecord record2 = financialRecordMapper.toRecord(recordRequest2);
        assertNotNull(record2, "record2 should not be null");

        List<FinancialRecord> records = List.of(record1, record2);
        when(financialRecordRepository.findAllByUserEmail(email)).thenReturn(records);

        // When
        FinancialSummary result = underTest.getSummary(email);

        // Then
        assertNotNull(result);
        assertEquals(90.0, result.getBalance());
        assertEquals(100.0, result.getTotalIncome());
        assertEquals(10.0, result.getTotalExpense());
        verify(financialRecordRepository).findAllByUserEmail(email);
        verifyNoMoreInteractions(financialRecordRepository);
    }

    @Test
    void update() {
        // Given
        FinancialRecordRequest recordRequest = FinancialRecordRequest.builder()
                .userEmail("test@test.com")
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        String email = "test@test.com";

        User newUser = User.builder()
                .email(email)
                .name("Test")
                .password("123456")
                .build();

        FinancialRecord existingRecord = FinancialRecord.builder()
                .user(newUser) // ensure user is set
                .id(UUID.randomUUID().toString())
                .description("OldTest")
                .type(FinancialRecordType.EXPENSE)
                .value(50.0)
                .build();

        FinancialRecord financialRecord = financialRecordMapper.toRecord(recordRequest);
        financialRecord.setId(existingRecord.getId());
        financialRecord.setUser(newUser); // ensure user is set on the new record

        when(financialRecordRepository.findById(existingRecord.getId())).thenReturn(Optional.of(existingRecord));
        when(financialRecordRepository.save(financialRecord)).thenReturn(financialRecord);

        // When
        underTest.update(existingRecord.getId(), recordRequest);

        // Then
        verify(financialRecordRepository).save(financialRecord);
        verifyNoMoreInteractions(financialRecordRepository);
    }


    @Test
    void deleteById() {
        // Given
        FinancialRecordRequest recordRequest = FinancialRecordRequest.builder()
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        // Create a new User for the FinancialRecord
        User user = User.builder()
                .email("test@test.com")
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