package josehomenhuck.planejamais.domain.record.service;

import josehomenhuck.planejamais.application.record.dto.RecordRequest;
import josehomenhuck.planejamais.application.record.dto.RecordResponse;

import java.util.List;

public interface RecordService {
    RecordResponse create(RecordRequest recordRequest);

    List<RecordResponse> findAll();
}
