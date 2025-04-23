package ch.no1hardy.service.model.team;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Team extends BaseEntity {
    @NotNull
    private String name;

    @NotNull
    private String flag;

    @OneToMany(mappedBy = "teamHome")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Game> gamesHome = List.of();

    @OneToMany(mappedBy = "teamGuest")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Game> gamesGuest = List.of();

    @Transient
    public List<Game> games() {
        List<Game> games = new ArrayList<>();
        games.addAll(gamesHome);
        games.addAll(gamesGuest);
        return games;
    }
}
