package josehomenhuck.planejamais.application.user.dto;

import lombok.Data;

@Data
public class CredentialsRequest {
    private String email;
    private String password;
}
