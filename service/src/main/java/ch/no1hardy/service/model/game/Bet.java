package ch.no1hardy.service.model.game;

import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@Entity
public class Bet extends BaseEntity {
    @NotNull
    private Integer joker;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    @ToString.Exclude
    private Game game;

    @NotNull
    private Integer scoreTeamHome;

    @NotNull
    private Integer scoreTeamGuest;

    public Boolean isHomeTeamWinner() {
        return scoreTeamHome > scoreTeamGuest;
    }

    public Boolean isGuestTeamWinner() {
        return scoreTeamGuest > scoreTeamHome;
    }

    public Boolean isTie() {
        return scoreTeamHome.equals(scoreTeamGuest);
    }

    public Integer getTotalScore() {
        return scoreTeamHome + scoreTeamGuest;
    }

    public boolean equals(BetReq bet) {
        return Objects.equals(this.getScoreTeamGuest(), bet.getScoreTeamGuest())
                && Objects.equals(this.getScoreTeamHome(), bet.getScoreTeamHome())
                && Objects.equals(this.getJoker(), bet.getJoker());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
