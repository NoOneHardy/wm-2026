package ch.no1hardy.service.model.group;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@Table(name = "wm-group")
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity {
    @NotNull
    private String name;

    @OneToMany(mappedBy = "group")
    @EqualsAndHashCode.Exclude
    private List<Game> games = List.of();

    @NotNull
    private Boolean isKnockout = false;
}
