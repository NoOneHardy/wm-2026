package ch.no1hardy.service.model.user;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Bet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "wm-user")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity implements UserDetails {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private Boolean isActivated = false;

    @NotNull
    private Integer points = 0;

    @NotNull
    private Role role = Role.UNCONFIRMED_USER;

    private Integer lastReviewedPoints = 0;

    private String avatarUrl;

    private LocalDateTime confirmedAt;

    @OneToMany(mappedBy = "user")
    private List<Bet> bets = List.of();

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return List.of(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
