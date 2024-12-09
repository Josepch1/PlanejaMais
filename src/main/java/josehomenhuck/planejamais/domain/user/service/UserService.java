package josehomenhuck.planejamais.domain.user.service;

import josehomenhuck.planejamais.domain.user.entity.AccessToken;
import josehomenhuck.planejamais.domain.user.entity.User;

public interface UserService {
    User findByEmail(String email);
    void save(User user);
    AccessToken authenticate(String email, String password);
}
