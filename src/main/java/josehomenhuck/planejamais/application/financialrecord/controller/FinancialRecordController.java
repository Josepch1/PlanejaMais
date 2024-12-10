package josehomenhuck.planejamais.application.financialrecord.controller;

import jakarta.websocket.server.PathParam;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.application.financialrecord.dto.FindAllResponse;
import josehomenhuck.planejamais.domain.financialrecord.service.FinancialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/financial-record")
public class FinancialRecordController {
    private final FinancialRecordService financialRecordService;

    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }

    @GetMapping
    public ResponseEntity<FindAllResponse> findAllByUserEmail(@PathParam("email") String email) {
        return ResponseEntity.ok(financialRecordService.findAllByUserEmail(email));
    }


    @GetMapping("/summary")
    public ResponseEntity<FinancialSummary> getSummary(@PathParam("email") String email) {
        return ResponseEntity.ok(financialRecordService.getSummary(email));
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
