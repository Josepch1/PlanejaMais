package josehomenhuck.planejamais.application.user.mapper;

import josehomenhuck.planejamais.application.user.dto.UserRequest;
import josehomenhuck.planejamais.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper{
    public User toEntity(UserRequest request){
        return User.builder()
               .name(request.getName())
               .email(request.getEmail())
               .password(request.getPassword())
               .build();
    }
}
