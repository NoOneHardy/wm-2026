package ch.no1hardy.service.model.game;

import ch.no1hardy.service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Score extends BaseEntity {
    @NotNull
    @OneToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Game game;

    @NotNull
    private Integer scoreTeamHome;

    @NotNull
    private Integer scoreTeamGuest;
}
