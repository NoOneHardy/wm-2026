package ch.no1hardy.service.model.game;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Game extends BaseEntity {
    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne
    @NotNull
    private Group group;

    @NotNull
    @ManyToOne
    private Team homeTeam;

    @NotNull
    @ManyToOne
    private Team guestTeam;

    @OneToOne
    private Score score;
}
