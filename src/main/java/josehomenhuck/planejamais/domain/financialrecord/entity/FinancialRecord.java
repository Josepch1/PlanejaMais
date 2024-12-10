package josehomenhuck.planejamais.domain.financialrecord.entity;

import jakarta.persistence.*;
import josehomenhuck.planejamais.domain.financialrecord.enums.FinancialRecordType;
import josehomenhuck.planejamais.domain.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private FinancialRecordType type;

    @Column
    private Double value;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
