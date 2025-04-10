package ch.no1hardy.service.model.team;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.group.Group;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Team extends BaseEntity {
    @NotNull
    private String name;

    @NotNull
    private String flag;

    @OneToMany(mappedBy = "homeTeam")
    private List<Game> gamesHome = List.of();

    @OneToMany(mappedBy = "guestTeam")
    private List<Game> gamesGuest = List.of();

    @ManyToOne
    private Group group;

    @Transient
    public List<Game> games() {
        List<Game> games = new ArrayList<>();
        games.addAll(gamesHome);
        games.addAll(gamesGuest);
        return games;
    }
}
