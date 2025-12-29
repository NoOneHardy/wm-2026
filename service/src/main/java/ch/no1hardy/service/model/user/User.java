package ch.no1hardy.service.model.user;

import ch.no1hardy.service.common.ListHelper;
import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.preferences.NotificationPreference;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
     * A list of notifications the user has received.
     */
    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Notification> notifications = List.of();

    /**
     * The avatar URL of the user. This is used for display purposes.
     */
    private String avatarUrl;

    /**
     * The date the user verified their email address.
     */
    private LocalDateTime emailVerifiedAt;

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

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<NotificationPreference> notificationPreferences = List.of();

    @OneToMany(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<VerificationCode> verificationCodes = List.of();

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
     * Returns whether the user has confirmed their email and has been manually approved by an admin and. This is used to determine if the user should be included in the ranking.
     */
    public boolean isConfirmed() {
        return isActive() && isApproved() && isEmailConfirmed();
    }

    /**
     * Returns whether the user application has been approved by an admin.
     *
     * @return true if the user application is approved, false otherwise
     */
    public boolean isApproved() {
        return getApplicationReviewedAt() != null && getUserApplicationStatus() == UserApplicationStatus.ACCEPTED;
    }

    /**
     * Returns whether the user has verified their email address.
     *
     * @return true if the email is verified, false otherwise
     */
    public boolean isEmailConfirmed() {
        return getEmailVerifiedAt() != null;
    }

    /**
     * Returns whether the user application has been denied by an admin.
     *
     * @return true if the user application is denied, false otherwise
     */
    public boolean isDenied() {
        return getApplicationReviewedAt() != null && getUserApplicationStatus() == UserApplicationStatus.DENIED;
    }

    /**
     * Accepts the user application and sets the application reviewed date to now.
     */
    public void approve() {
        if (getUserApplicationStatus() == UserApplicationStatus.ACCEPTED) return;

        this.setApplicationReviewedAt(LocalDateTime.now());
        this.setUserApplicationStatus(UserApplicationStatus.ACCEPTED);
    }

    /**
     * Denies the user application and sets the application reviewed date to now.
     */
    public void deny() {
        if (getUserApplicationStatus() == UserApplicationStatus.DENIED) return;

        this.setApplicationReviewedAt(LocalDateTime.now());
        this.setUserApplicationStatus(UserApplicationStatus.DENIED);
    }

    public VerificationCode createVerificationCode(VerificationCodeType type, int validMinutes) {
        VerificationCode code = new VerificationCode(validMinutes, type, this);
        this.setVerificationCodes(ListHelper.add(this.verificationCodes, code));
        return code;
    }

    public Optional<VerificationCode> getMostRecentEmailVerificationCode() {
        return getVerificationCodes().stream()
                .filter(code -> code.getType() == VerificationCodeType.EMAIL)
                .filter(VerificationCode::isValid)
                .max(Comparator.comparing(BaseEntity::getCreatedAt));
    }
}
