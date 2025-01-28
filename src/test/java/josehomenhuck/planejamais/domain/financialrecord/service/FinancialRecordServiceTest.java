package josehomenhuck.planejamais.domain.financialrecord.service;

import josehomenhuck.planejamais.application.financialrecord.dto.FinancialFindAllResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.impl.FinancialRecordServiceImpl;
import josehomenhuck.planejamais.application.financialrecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.financialrecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialrecord.enums.FinancialRecordType;
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

        private FinancialRecordMapper financialRecordMapper = new FinancialRecordMapper();

        private UserMapper userMapper = new UserMapper();

        @BeforeEach
        void setUp() {
                underTest = new FinancialRecordServiceImpl(financialRecordRepository, financialRecordMapper,
                                userService, userMapper);
        }

        private FinancialRecordRequest createRecordRequest(String email, String description, FinancialRecordType type,
                        double value) {
                return FinancialRecordRequest.builder()
                                .userEmail(email)
                                .description(description)
                                .type(type)
                                .value(value)
                                .build();
        }

        private User createDefaultUser(String email) {
                return User.builder()
                                .id(UUID.randomUUID().toString())
                                .email(email)
                                .name("Test User")
                                .password("123456")
                                .build();
        }

        @Test
        void create() {
                // Given
                String email = "test@test.com";

                User user = createDefaultUser(email);

                FinancialRecordRequest recordRequest = createRecordRequest(email, "Test", FinancialRecordType.INCOME,
                                100.0);

                when(userService.findByEmail(user.getEmail())).thenReturn(user);

                FinancialRecord financialRecord = financialRecordMapper.toRecord(recordRequest);
                financialRecord.setUser(user);

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

                User user = createDefaultUser(email);

                when(userService.findByEmail(user.getEmail())).thenReturn(user);

                FinancialRecordRequest recordRequest1 = createRecordRequest(email, "Test", FinancialRecordType.INCOME,
                                100.0);

                FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
                record1.setUser(user);

                FinancialRecordRequest recordRequest2 = createRecordRequest(email, "SecondTest",
                                FinancialRecordType.EXPENSE, 10.0);

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

                User user = createDefaultUser(email);

                when(userService.findByEmail(user.getEmail())).thenReturn(user);

                FinancialRecordRequest recordRequest1 = createRecordRequest(email, "Test", FinancialRecordType.INCOME,
                                100.0);

                FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
                assertNotNull(record1, "record1 should not be null");

                FinancialRecordRequest recordRequest2 = createRecordRequest(email, "SecondTest",
                                FinancialRecordType.EXPENSE, 10.0);

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
        }

        @Test
        void update() {
                // Given
                String email = "test@test.com";

                User user = createDefaultUser(email);

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

                when(financialRecordRepository.findById(existingRecord.getId()))
                                .thenReturn(Optional.of(existingRecord));
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

                FinancialRecordRequest recordRequest = createRecordRequest(email, "Test", FinancialRecordType.INCOME,
                                100.0);

                User user = createDefaultUser(email);

                // Set the User in the FinancialRecord
                String id = UUID.randomUUID().toString();
                FinancialRecord financialRecord = financialRecordMapper.toRecord(recordRequest);
                financialRecord.setId(id);
                financialRecord.setUser(user); // Ensure the User is set here

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