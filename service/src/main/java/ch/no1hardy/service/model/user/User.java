package ch.no1hardy.service.model.user;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Bet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
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
    /**
     * The username of the user. This is used for login and must be unique.
     */
    @NotNull
    private String username;

    /**
     * The password of the user. This is used for login and must be at least 8 characters long.
     */
    @NotNull
    private String password;

    /**
     * The email address of the user. This is used for communication and must be unique.
     */
    @NotNull
    private String email;

    /**
     * The first name of the user. This is used for display purposes.
     */
    @NotNull
    private String firstname;

    /**
     * The last name of the user. This is used for display purposes.
     */
    @NotNull
    private String lastname;

    /**
     * The amount of points the user has earned.
     */
    @NotNull
    private Integer points = 0;

    /**
     * The role of the user. Only used for authorization
     */
    @NotNull
    private Role role = Role.USER;

    /**
     * The points the user has earned before the last upload of results.
     */
    private Integer lastReviewedPoints = 0;

    /**
     * The avatar URL of the user. This is used for display purposes.
     * TODO: Implement avatar upload
     */
    private String avatarUrl;

    /**
     * The date when the user confirmed his email address
     * TODO: Implement email confirmation
     */
    private LocalDateTime emailConfirmedAt;

    /**
     * The date when an admin has reviewed the user and accepted or denied it
     */
    private LocalDateTime applicationReviewedAt;
    /**
     * The status of the user application. Can be either ACCEPTED, DENIED or PENDING.
     */
    private UserApplicationStatus userApplicationStatus = UserApplicationStatus.PENDING;

    /**
     * A list with all bets the user has placed.
     */
    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Bet> bets = List.of();

    /**
     * The authorities granted to the user. This is used for authorization.
     */
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return List.of(authority);
    }

    /**
     * Returns whether the account has not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Returns whether the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Returns whether the credentials (password) are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Returns whether the user is active. This is used to determine if the user can log in.
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /**
     * Returns whether the user has been confirmed by an admin. This is used to determine if the user should be included in the ranking.
     */
    public boolean isConfirmed() {
        return isActive() && getApplicationReviewedAt() != null && getUserApplicationStatus() == UserApplicationStatus.ACCEPTED;
    }
}
