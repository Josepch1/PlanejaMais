package josehomenhuck.planejamais.domain.financialRecord.service;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialRecord.impl.FinancialRecordServiceImpl;
import josehomenhuck.planejamais.application.financialRecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialRecord.enums.FinancialRecordType;
import josehomenhuck.planejamais.infrastructure.repository.FinancialRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialRecordServiceTest {

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    private FinancialRecordMapper financialRecordMapper;

    private FinancialRecordServiceImpl underTest;

    @BeforeEach
    void setUp() {
        financialRecordMapper = new FinancialRecordMapper();
        underTest = new FinancialRecordServiceImpl(financialRecordRepository, financialRecordMapper);
    }

    @Test
    void create() {
        // Given
        FinancialRecordRequest recordRequest = new FinancialRecordRequest();
        recordRequest.setDescription("Test");
        recordRequest.setType(FinancialRecordType.INCOME);
        recordRequest.setValue(100.0);

        FinancialRecord record = financialRecordMapper.toRecord(recordRequest);
        when(financialRecordRepository.save(record)).thenReturn(record);

        // When
        underTest.create(recordRequest);

        // Then
        verify(financialRecordRepository).save(record);
    }

    @Test
    void findAll() {
        // Given
        FinancialRecordRequest recordRequest1 = FinancialRecordRequest.builder()
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
        assertNotNull(record1, "record1 should not be null");

        FinancialRecordRequest recordRequest2 = new FinancialRecordRequest();
        recordRequest2.setDescription("SecondTest");
        recordRequest2.setType(FinancialRecordType.EXPENSE);
        recordRequest2.setValue(10.0);

        FinancialRecord record2 = financialRecordMapper.toRecord(recordRequest2);
        assertNotNull(record2, "record2 should not be null");

        List<FinancialRecord> records = List.of(record1, record2);
        when(financialRecordRepository.findAll()).thenReturn(records);

        // When
        List<FinancialRecordResponse> result = underTest.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test", result.get(0).getDescription());
        assertEquals("SecondTest", result.get(1).getDescription());
        verify(financialRecordRepository).findAll();
    }

    @Test
    void getSummary() {
        // Given
        FinancialRecordRequest recordRequest1 = FinancialRecordRequest.builder()
                .description("Test")
                .type(FinancialRecordType.INCOME)
                .value(100.0)
                .build();

        FinancialRecord record1 = financialRecordMapper.toRecord(recordRequest1);
        assertNotNull(record1, "record1 should not be null");

        FinancialRecordRequest recordRequest2 = new FinancialRecordRequest();
        recordRequest2.setDescription("SecondTest");
        recordRequest2.setType(FinancialRecordType.EXPENSE);
        recordRequest2.setValue(10.0);

        FinancialRecord record2 = financialRecordMapper.toRecord(recordRequest2);
        assertNotNull(record2, "record2 should not be null");

        List<FinancialRecord> records = List.of(record1, record2);
        when(financialRecordRepository.findAll()).thenReturn(records);

        // When
        FinancialSummary result = underTest.getSummary();

        // Then
        assertNotNull(result);
        assertEquals(90.0, result.getBalance());
        assertEquals(100.0, result.getTotalIncome());
        assertEquals(10.0, result.getTotalExpense());
        verify(financialRecordRepository).findAll();
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
                .id(1L)
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

        Long id = record.getId();

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