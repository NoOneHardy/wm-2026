package ch.no1hardy.service.model.game;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
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
}
