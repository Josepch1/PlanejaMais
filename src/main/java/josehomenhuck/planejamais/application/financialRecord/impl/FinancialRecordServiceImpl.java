package josehomenhuck.planejamais.application.record.impl;

import josehomenhuck.planejamais.application.record.dto.RecordRequest;
import josehomenhuck.planejamais.application.record.dto.RecordResponse;
import josehomenhuck.planejamais.application.record.mapper.FinancialRecordMapper;
import josehomenhuck.planejamais.domain.financialRecord.entity.FinancialRecord;
import josehomenhuck.planejamais.domain.financialRecord.service.FinancialRecordService;
import josehomenhuck.planejamais.infrastructure.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private final FinancialRecordRepository recordRepository;

    private final FinancialRecordMapper recordMapper;

    @Override
    @Transactional
    public RecordResponse create(RecordRequest recordRequest) {
        FinancialRecord record = recordMapper.toRecord(recordRequest);




        return null;
    }

    @Override
    public List<RecordResponse> findAll() {
        return List.of();
    }
}
