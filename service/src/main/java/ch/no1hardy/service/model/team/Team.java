package ch.no1hardy.service.model.team;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Team extends BaseEntity {

    @NotNull
    @ToString.Include
    private String name;

    @NotNull
    @ToString.Include
    private String shortName;

    @NotNull
    private String flag;

    @OneToMany(mappedBy = "teamHome")
    private List<Game> gamesHome = new ArrayList<>();

    @OneToMany(mappedBy = "teamGuest")
    private List<Game> gamesGuest = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team other)) {
            return false;
        }

        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Transient
    public List<Game> getGames() {
        List<Game> games = new ArrayList<>();
        games.addAll(gamesHome);
        games.addAll(gamesGuest);
        return games;
    }

    public List<Game> getPreviousGames() {
        return getGames().stream()
                .filter(Game::isGroupPhase)
                .filter(Game::hasResult)
                .toList();
    }
}