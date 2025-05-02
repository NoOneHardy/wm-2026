package ch.no1hardy.service.model.game;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.group.Group;
import ch.no1hardy.service.model.team.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Game extends BaseEntity {
    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne
    @NotNull
    @ToString.Exclude
    private Group group;

    @NotNull
    @ManyToOne
    private Team teamHome;

    @NotNull
    @ManyToOne
    private Team teamGuest;

    @OneToOne
    private Score result;

    @OneToMany(mappedBy = "game")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Bet> bets = List.of();

    public Boolean isInFuture() {
        return LocalDateTime.now().isBefore(this.getTimestamp());
    }

    public Boolean isKnockout() {
        return getGroup().getIsKnockout();
    }

    public Boolean hasResult() {
        return getResult() != null;
    }

    public Boolean isGroupPhase() {
        return !isKnockout();
    }
}
