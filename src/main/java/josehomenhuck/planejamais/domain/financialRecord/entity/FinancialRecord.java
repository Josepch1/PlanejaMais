package josehomenhuck.planejamais.domain.record.entity;

import jakarta.persistence.*;
import josehomenhuck.planejamais.domain.record.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private RecordType type;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;
}
