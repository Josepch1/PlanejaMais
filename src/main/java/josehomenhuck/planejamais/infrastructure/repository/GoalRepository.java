package josehomenhuck.planejamais.infrastructure.repository;

import josehomenhuck.planejamais.domain.goal.entity.Goal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUserEmail(String email);
}
