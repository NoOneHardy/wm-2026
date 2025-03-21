package ch.no1hardy.service.model.user;

import ch.no1hardy.service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private Boolean isActivated = false;

    @NotNull
    private Integer points = 0;

    private Integer lastReviewedPoints = 0;

    private String avatarUrl;

    private LocalDateTime confirmedAt;
}
