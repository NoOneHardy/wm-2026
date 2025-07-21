package ch.no1hardy.service.model.game;

import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@Data
@Entity
public class Score extends BaseEntity {
    @NotNull
    @OneToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Game game;

    private Integer scoreTeamHome;

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

    public boolean equals(ScoreReq score) {
        return Objects.equals(this.getScoreTeamGuest(), score.getScoreTeamGuest())
                && Objects.equals(this.getScoreTeamHome(), score.getScoreTeamHome());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
