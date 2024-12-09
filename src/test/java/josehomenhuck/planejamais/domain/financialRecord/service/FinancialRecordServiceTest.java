package josehomenhuck.planejamais.domain.financialRecord.service;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialRecord.impl.FinancialRecordServiceImpl;
import josehomenhuck.planejamais.application.financialRecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
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

    private FinancialRecordServiceImpl underTest;

    @BeforeEach
    void setUp() {
        financialRecordMapper = new FinancialRecordMapper();
        underTest = new FinancialRecordServiceImpl(financialRecordRepository, financialRecordMapper, userService);
    }

    @Test
    void create() {
        // Given
        User newUser = User.builder()
                .email("test@test.com")
                .name("Test")
                .password("123456")
                .build();

        when(userService.findByEmail(newUser.getEmail())).thenReturn(newUser);

        FinancialRecordRequest recordRequest = FinancialRecordRequest.builder()
                .userEmail("test@test.com")
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        FinancialRecord record = financialRecordMapper.toRecord(recordRequest);

        User user = userService.findByEmail(recordRequest.getUserEmail());
        assertNotNull(user, "user should not be null");

        record.setUser(user);

        when(financialRecordRepository.save(record)).thenReturn(record);

        // When
        underTest.create(recordRequest);

        // Then
        verify(financialRecordRepository).save(record);
    }

    @Test
    void findAllByUserEmail() {
        // Given
        String email = "test@test.com";

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
        List<FinancialRecordResponse> result = underTest.findAllByUserEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test", result.get(0).getDescription());
        assertEquals("SecondTest", result.get(1).getDescription());
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
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        FinancialRecord existingRecord = FinancialRecord.builder()
                .id(UUID.randomUUID().toString())
                .description("OldTest")
                .type(FinancialRecordType.EXPENSE)
                .value(50.0)
                .build();

        FinancialRecord record = financialRecordMapper.toRecord(recordRequest);
        record.setId(existingRecord.getId());

        when(financialRecordRepository.findById(existingRecord.getId())).thenReturn(Optional.of(existingRecord));
        when(financialRecordRepository.save(record)).thenReturn(record);

        // When
        underTest.update(existingRecord.getId(), recordRequest);

        // Then
        verify(financialRecordRepository).save(record);
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

        FinancialRecord record = financialRecordMapper.toRecord(recordRequest);
        assertNotNull(record, "record1 should not be null");

        when(financialRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));

        String id = record.getId();

        // When
        underTest.deleteById(id);

        // Then
        verify(financialRecordRepository).deleteById(id);
        verifyNoMoreInteractions(financialRecordRepository);
    }

    @Test
    void deleteAll() {
        // When
        underTest.deleteAll();

        // Then
        verify(financialRecordRepository).deleteAll();
        verifyNoMoreInteractions(financialRecordRepository);
    }
}