package josehomenhuck.planejamais.application.financialrecord.impl;

import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.dto.FindAllResponse;
import josehomenhuck.planejamais.application.financialrecord.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.application.user.dto.UserResponse;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.financialrecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialrecord.service.FinancialRecordService;
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
    private final UserMapper userMapper;

    public FinancialRecordServiceImpl(FinancialRecordRepository recordRepository, FinancialRecordMapper recordMapper, UserService userService, UserMapper userMapper) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
        this.userService = userService;
        this.userMapper = userMapper;
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
    public FindAllResponse findAllByUserEmail(String email) {
        List<FinancialRecord> financialRecords = recordRepository.findAllByUserEmail(email);

        List<FinancialRecordResponse> financialRecordResponses = financialRecords.stream()
                .map(recordMapper::toRecordResponse)
                .toList();

        UserResponse userResponse = userMapper.toResponse(userService.findByEmail(email));

        return FindAllResponse.builder()
                .user(userResponse)
                .financialRecords(financialRecordResponses)
                .build();
    }

    @Override
    public FinancialSummary getSummary(String email) {
        User user = userService.findByEmail(email);

        UserResponse userResponse = userMapper.toResponse(user);

        List<FinancialRecord> financialRecords = recordRepository.findAllByUserEmail(user.getEmail());

        Double totalIncome = financialRecords.stream()
                .filter(fr -> fr.getType().isIncome())
                .mapToDouble(FinancialRecord::getValue)
                .sum();

        Double totalExpense = financialRecords.stream()
                .filter(fr -> fr.getType().isExpense())
                .mapToDouble(FinancialRecord::getValue)
                .sum();

        Double balance = totalIncome - totalExpense;

        return FinancialSummary.builder()
                .user(userResponse)
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
