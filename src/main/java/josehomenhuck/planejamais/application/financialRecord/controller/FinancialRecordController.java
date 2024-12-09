package josehomenhuck.planejamais.application.financialRecord.controller;

import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialRecord.dto.FinancialSummary;
import josehomenhuck.planejamais.domain.financialRecord.service.FinancialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/financial-record")
public class FinancialRecordController {
    private final FinancialRecordService financialRecordService;

    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }

    @GetMapping
    public ResponseEntity<List<FinancialRecordResponse>> findAll() {
        return ResponseEntity.ok(financialRecordService.findAll());
    }


    @GetMapping("/summary")
    public ResponseEntity<FinancialSummary> getSummary() {
        return ResponseEntity.ok(financialRecordService.getSummary());
    }

    @PostMapping
    public ResponseEntity<FinancialRecordResponse> create(@RequestBody FinancialRecordRequest financialRecordRequest) {
        return ResponseEntity.ok(financialRecordService.create(financialRecordRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> update(@PathVariable String id, @RequestBody FinancialRecordRequest financialRecordRequest) {
        return ResponseEntity.ok(financialRecordService.update(id, financialRecordRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        financialRecordService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(financialRecordService.deleteById(id));
    }
}
