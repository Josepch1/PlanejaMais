package josehomenhuck.planejamais.application.user.controller;

import josehomenhuck.planejamais.application.user.dto.CredentialsRequest;
import josehomenhuck.planejamais.application.user.dto.UserRequest;
import josehomenhuck.planejamais.application.user.mapper.UserMapper;
import josehomenhuck.planejamais.domain.user.entity.AccessToken;
import josehomenhuck.planejamais.domain.user.entity.User;
import josehomenhuck.planejamais.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/v1/users")
@Tag(name = "User")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody UserRequest request) {
        try {
        User user = userMapper.toEntity(request);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Authenticate a user", security = {})
    @PostMapping("/auth")
    public ResponseEntity<AccessToken> authenticate(@RequestBody CredentialsRequest request) {
        AccessToken token = userService.authenticate(request.getEmail(), request.getPassword());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(token);
    }
}
