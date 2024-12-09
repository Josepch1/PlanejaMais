package josehomenhuck.planejamais.application.financialRecord.impl;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialRecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialRecord.service.FinancialRecordService;
import josehomenhuck.planejamais.domain.user.entity.User;
import josehomenhuck.planejamais.domain.user.service.UserService;
import josehomenhuck.planejamais.infrastructure.repository.FinancialRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final FinancialRecordMapper recordMapper;
    private final UserService userService;

    public FinancialRecordServiceImpl(FinancialRecordRepository recordRepository, FinancialRecordMapper recordMapper, UserService userService) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public FinancialRecordResponse create(FinancialRecordRequest financialRecordRequest) {
        User user = userService.findByEmail(financialRecordRequest.getUserEmail());

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        FinancialRecord financialRecord = recordMapper.toRecord(financialRecordRequest);

        financialRecord.setUser(user);

        FinancialRecord savedFinancialRecord = recordRepository.save(financialRecord);

        return recordMapper.toRecordResponse(savedFinancialRecord);
    }

    @Override
    public List<FinancialRecordResponse> findAllByUserEmail(String email) {
        List<FinancialRecord> financialRecords = recordRepository.findAllByUserEmail(email);

        return financialRecords.stream()
                .map(recordMapper::toRecordResponse)
                .toList();
    }

    @Override
    public FinancialSummary getSummary(String email) {
        User user = userService.findByEmail(email);

        List<FinancialRecord> financialRecords = recordRepository.findAllByUserEmail(user.getEmail());

        Double totalIncome = financialRecords.stream()
                .filter(record -> record.getType().isIncome())
                .mapToDouble(FinancialRecord::getValue)
                .sum();

        Double totalExpense = financialRecords.stream()
                .filter(record -> record.getType().isExpense())
                .mapToDouble(FinancialRecord::getValue)
                .sum();

        Double balance = totalIncome - totalExpense;

        return FinancialSummary.builder()
                .user(user)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    @Override
    @Transactional
    public FinancialRecordResponse update(String id, FinancialRecordRequest financialRecordRequest) {
        FinancialRecord financialRecord = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        financialRecord.setDescription(financialRecordRequest.getDescription());
        financialRecord.setValue(financialRecordRequest.getValue());
        financialRecord.setType(financialRecordRequest.getType());

        FinancialRecord updatedFinancialRecord = recordRepository.save(financialRecord);

        return recordMapper.toRecordResponse(updatedFinancialRecord);
    }

    @Override
    public FinancialRecordResponse deleteById(String id) {
        FinancialRecord financialRecord = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        recordRepository.deleteById(id);

        return recordMapper.toRecordResponse(financialRecord);
    }

    @Override
    public void deleteAll() {
        recordRepository.deleteAll();
    }
}
