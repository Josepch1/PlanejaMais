package josehomenhuck.planejamais.infrastructure.repository;

import josehomenhuck.planejamais.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
