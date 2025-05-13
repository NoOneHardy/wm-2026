package ch.no1hardy.service.model.game;

import ch.no1hardy.service.front.game.BetReq;
import ch.no1hardy.service.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
public class Bet extends Score {
    @NotNull
    private Integer joker;

    @NotNull
    @ManyToOne
    private User user;

    public boolean equals(BetReq bet) {
        return Objects.equals(this.getScoreTeamGuest(), bet.getScoreTeamGuest())
                && Objects.equals(this.getScoreTeamHome(), bet.getScoreTeamHome())
                && Objects.equals(this.getJoker(), bet.getJoker());
    }
}
