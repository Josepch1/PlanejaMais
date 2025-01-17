package josehomenhuck.planejamais.application.goal.controller;

import jakarta.websocket.server.PathParam;
import josehomenhuck.planejamais.application.goal.dto.GoalFindAllResponse;
import josehomenhuck.planejamais.application.goal.dto.GoalRequest;
import josehomenhuck.planejamais.application.goal.dto.GoalResponse;
import josehomenhuck.planejamais.domain.goal.service.GoalService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/goal")
@Tag(name = "Goal")
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @Operation(summary = "Find all goals by user email", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<GoalFindAllResponse> findAllByUserEmail(@PathParam("email") String email) {
        return ResponseEntity.ok(goalService.findAllByUserEmail(email));
    }


    @Operation(summary = "Create a goal", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<GoalResponse> create(@RequestBody GoalRequest goalRequest) {
        return ResponseEntity.ok(goalService.create(goalRequest));
    }

    @Operation(summary = "Update a goal", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> update(@PathVariable Long id, @RequestBody GoalRequest goalRequest) {
        return ResponseEntity.ok(goalService.update(id, goalRequest));
    }

    @Operation(summary = "Delete a goal by id", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<GoalResponse> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.deleteById(id));
    }
}
