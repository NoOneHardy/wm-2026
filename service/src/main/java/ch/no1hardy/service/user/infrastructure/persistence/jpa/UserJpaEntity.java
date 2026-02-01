package ch.no1hardy.service.user.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "wm-user-v2")
public class UserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String email;
}
