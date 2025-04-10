package ch.no1hardy.service.model.game;

import ch.no1hardy.service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Score extends BaseEntity {
    @NotNull
    @OneToOne
    private Game game;

    @NotNull
    private Integer scoreTeamHome;

    @NotNull
    private Integer scoreTeamGuest;
}
