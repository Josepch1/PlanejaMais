package josehomenhuck.planejamais.application.financialrecord.controller;

import jakarta.websocket.server.PathParam;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialFindAllResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordRequest;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialRecordResponse;
import josehomenhuck.planejamais.application.financialrecord.dto.FinancialSummary;
import josehomenhuck.planejamais.domain.financialrecord.service.FinancialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/financial-record")
@Tag(name = "Financial Record")
public class FinancialRecordController {
    private final FinancialRecordService financialRecordService;

    public FinancialRecordController(FinancialRecordService financialRecordService) {
        this.financialRecordService = financialRecordService;
    }

    @Operation(summary = "Find all financial records by user email", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<FinancialFindAllResponse> findAllByUserEmail(@PathParam("email") String email) {
        return ResponseEntity.ok(financialRecordService.findAllByUserEmail(email));
    }


    @Operation(summary = "Get financial summary by user email", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/summary")
    public ResponseEntity<FinancialSummary> getSummary(@PathParam("email") String email) {
        return ResponseEntity.ok(financialRecordService.getSummary(email));
    }

    @Operation(summary = "Create a financial record", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<FinancialRecordResponse> create(@RequestBody FinancialRecordRequest financialRecordRequest) {
        return ResponseEntity.ok(financialRecordService.create(financialRecordRequest));
    }

    @Operation(summary = "Update a financial record", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> update(@PathVariable String id, @RequestBody FinancialRecordRequest financialRecordRequest) {
        return ResponseEntity.ok(financialRecordService.update(id, financialRecordRequest));
    }

    @Operation(summary = "Delete all financial records", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        financialRecordService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a financial record by id", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<FinancialRecordResponse> deleteById(@PathVariable String id) {
        return ResponseEntity.ok(financialRecordService.deleteById(id));
    }
}
