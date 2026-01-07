package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.user.NotLoggedInException;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.exception.user.UserValidationException;
import ch.no1hardy.service.exception.user.UsernameNotFoundException;
import ch.no1hardy.service.exception.verification.VerificationException;
import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.LoginReq;
import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.front.verification.ResetPasswordReq;
import ch.no1hardy.service.front.verification.VerifyEmailReq;
import ch.no1hardy.service.mapper.UserMapper;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.group.GroupRepository;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeType;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class UserService {
    @Value("${wm.verification.email.validity}")
    private Integer emailConfirmationValidity;
    @Value("${wm.verification.password_reset.validity}")
    private Integer passwordResetValidity;

    private final UserRepository repository;
    private final GroupRepository groupRepository;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FileService fileService;
    private final AuthService authService;
    private final VerificationService verificationService;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * List all active users.
     *
     * @return a list of active users.
     */
    public List<User> listRaw() {
        return repository.findAll().stream()
                .filter(User::isActive)
                .toList();
    }

    /**
     * List all users, regardless of their active status.
     *
     * @return a list of all users.
     */
    public List<User> listAllRaw() {
        return repository.findAll();
    }

    /**
     * List all confirmed users (users who have been confirmed by an admin).
     *
     * @return a list of confirmed users.
     */
    public List<User> listConfirmedRaw() {
        return listRaw().stream().filter(User::isConfirmed).toList();
    }

    /**
     * Get a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the user if found
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public User getRaw(String id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Get a user by their username.
     *
     * @param username the username of the user to retrieve.
     * @return the user if found
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public User getRawByUsername(@NotNull String username) throws UserNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Get a user by their email.
     *
     * @param email the email of the user to retrieve.
     * @return the user as optional if found
     */
    public Optional<User> getRawByEmail(@NotNull String email) {
        return repository.findByEmail(email);
    }

    /**
     * List all active users as DTOs.
     *
     * @return a list of active users as UserRes DTOs.
     */
    public List<UserRes> list() {
        return mapper.toDto(listRaw());
    }

    /**
     * List all users, regardless of their active status, as DTOs.
     *
     * @return a list of all users as UserRes DTOs.
     */
    public List<UserRes> listAll() {
        return mapper.toDto(listAllRaw());
    }

    /**
     * Get a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return the user as DTO
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public UserRes get(@NotNull String id) throws UserNotFoundException {
        return mapper.toDto(getRaw(id));
    }

    /**
     * Check if a username and an email are available.
     *
     * @param username the username to check. Can be null.
     * @param email    the email to check. Can be null.
     * @return CheckRes containing availability status of username and email.
     */
    public CheckRes check(@Nullable String username, @Nullable String email) {
        return CheckRes.builder()
                .isUsernameAvailable(username == null || isUsernameAvailable(username))
                .isEmailAvailable(email == null || isEmailAvailable(email))
                .build();
    }

    /**
     * Check if a username is available.
     *
     * @param username the username to check.
     * @return true if the username is available, false otherwise.
     */
    public boolean isUsernameAvailable(@NotNull String username) {
        return repository.findByUsername(username).stream().noneMatch(User::isActive);
    }

    /**
     * Check if an email is available.
     *
     * @param email the email to check.
     * @return true if the email is available, false otherwise.
     */
    public boolean isEmailAvailable(@NotNull String email) {
        return repository.findByEmail(email).stream().noneMatch(User::isActive);
    }

    /**
     * Validate that a dto only contains values that are available.
     *
     * @param dto the UserReq DTO to validate
     * @throws UserValidationException if the dto is invalid
     */
    private void checkAvailableDtoValues(@NotNull UserReq dto) throws UserValidationException {
        if (!isEmailAvailable(dto.getEmail()))
            throw new UserValidationException("Email " + dto.getEmail() + " is already taken", "Email ist bereits vergeben");

        if (!isUsernameAvailable(dto.getUsername()))
            throw new UserValidationException("Username " + dto.getUsername() + " is already taken", "Username ist bereits vergeben");
    }

    /**
     * Create a new user using a DTO. The dto will be validated and the password will be encoded.
     *
     * @param dto the UserReq DTO containing user details.
     * @return the created user as UserRes DTO.
     * @throws UserValidationException if the dto is invalid
     */
    public UserRes create(@NotNull UserReq dto) throws UserValidationException {
        checkAvailableDtoValues(dto);
        dto.validateAll();
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User entity = mapper.toEntity(dto);
        repository.save(entity);

        sendEmailVerificationMail(entity);

        return mapper.toDto(entity);
    }

    /**
     * Update an existing user using a DTO. The dto will be validated and the password will be encoded if provided.
     *
     * @param id  the ID of the user to update.
     * @param dto the UserReq DTO containing updated user details.
     * @return the updated user as UserRes DTO.
     * @throws UserNotFoundException   if the user with the given ID does not exist.
     * @throws UserValidationException if the dto is invalid
     */
    public UserRes update(@NotNull String id, @NotNull UserReq dto) throws UserNotFoundException, UserValidationException {
        checkAvailableDtoValues(dto);

        User user = getRaw(id);
        if (dto.getPasswordChange() != null) {
            String oldPassword = dto.getPasswordChange().currentPassword();
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new UserValidationException("Current password is incorrect", "Aktuelles Passwort ist falsch");
            }

            dto.setPassword(passwordEncoder.encode(dto.getPasswordChange().password()));
        }

        // temporarily disable email change because it requires re-verification
        // TODO: implement email change with re-verification
        dto.setEmail(user.getEmail());

        dto.validate();
        mapper.update(dto, user);
        return mapper.toDto(repository.save(user));
    }

    public UserRes updateAvatar(@NotNull MultipartFile avatar) {
        User currentUser = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);
        currentUser.setAvatarUrl(fileService.storeAvatar(avatar, currentUser.getId()));
        return mapper.toDto(repository.save(currentUser));
    }

    /**
     * Delete a user by their ID. The user will be marked as deleted.
     *
     * @param id the ID of the user to delete.
     * @return the deleted user as UserRes DTO.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public UserRes delete(@NotNull String id) throws UserNotFoundException {
        User user = getRaw(id);
        user.delete();
        return mapper.toDto(repository.save(user));
    }

    /**
     * Log in a user using their username and password.
     *
     * @param dto the LoginReq DTO containing username and password.
     * @return the logged-in user as UserRes DTO.
     * @throws AuthenticationException if authentication fails (e.g., wrong username or password).
     * @throws UserNotFoundException   if the user with the given username does not exist.
     */
    public UserRes login(@NotNull LoginReq dto) throws AuthenticationException, UserNotFoundException {
        verificationService.clearExpiredCodes();
        return repository.findByUsername(dto.getUsername())
                .map(user -> {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getId(),
                                    dto.getPassword()
                            )
                    );
                    return mapper.toDto(user);
                }).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    }

    /**
     * Get the currently logged-in user as a DTO.
     *
     * @return the current user as UserRes DTO, or an empty UserRes if not authenticated.
     */
    public UserRes getCurrentUser() {
        return authService.getLoggedInUser().map(mapper::toDto).orElse(new UserRes());
    }

    /**
     * Add points to users based on their bets in a game.
     *
     * @param game the game for which to add points.
     */
    public void addUserPoints(@NotNull Game game) {
        if (game.getResult() == null) return;
        Score result = game.getResult();

        listRaw().forEach(user -> user.setLastReviewedPoints(user.getPoints()));

        for (Bet bet : game.getBets()) {
            if (bet.getUpdatedAt().isAfter(result.getCreatedAt())) return;
            int points = calculateUserPoints(bet, result);

            bet.getUser().setPoints(bet.getUser().getPoints() + points);
        }
    }

    /**
     * Remove points from users based on their bets in a game.
     *
     * @param game the game for which to remove points.
     */
    public void removeUserPoints(@NotNull Game game) {
        if (game.getResult() == null) return;
        Score result = game.getResult();

        listRaw().forEach(user -> user.setLastReviewedPoints(user.getPoints()));

        for (Bet bet : game.getBets()) {
            if (bet.getUpdatedAt().isAfter(result.getCreatedAt())) return;
            int points = calculateUserPoints(bet, result);

            bet.getUser().setPoints(bet.getUser().getPoints() - points);
        }
    }

    /**
     * Calculate the points a user earns based on their bet and the actual game result.
     *
     * @param bet    the user's bet.
     * @param result the actual game result.
     * @return the calculated points.
     */
    public int calculateUserPoints(@NotNull Bet bet, @NotNull Score result) {
        int points = 0;

        boolean correctWinnerHome = bet.isHomeTeamWinner() && result.isHomeTeamWinner();
        boolean correctWinnerGuest = bet.isGuestTeamWinner() && result.isGuestTeamWinner();
        boolean correctTie = bet.isTie() && result.isTie();

        if (correctWinnerHome || correctWinnerGuest || correctTie) points += 5;

        if (bet.getScoreTeamGuest().equals(result.getScoreTeamGuest())) points += 2;
        if (bet.getScoreTeamHome().equals(result.getScoreTeamHome())) points += 2;

        if (bet.getTotalScore().equals(result.getTotalScore())) points += 1;

        return points * bet.getJoker();
    }

    /**
     * Get all unread notifications for a user.
     *
     * @param user the user for whom to retrieve notifications.
     * @return a list of unread notifications.
     */
    public List<Notification> getNotifications(@NotNull User user) {
        return repository.listNotifications(user).stream().filter(Notification::isUnread).toList();
    }

    /**
     * Verify the user's email.
     */
    public void verifyEmail(@NotNull User user) {
        user.setEmailVerifiedAt(java.time.LocalDateTime.now());
        repository.save(user);
    }

    /**
     * Verifies a user's email address and returns the updated user.
     *
     * @return the updated user as UserRes DTO.
     */
    public UserRes verifyEmail(VerifyEmailReq verifyEmailReq) {
        return verificationService.getVerificationCode(verifyEmailReq.code())
                .map(VerificationCode::getUser)
                .filter(user -> verificationService.verifyEmailCode(user, verifyEmailReq))
                .map(user -> {
                    verifyEmail(user);
                    verificationService.deleteVerificationCode(verifyEmailReq.code());
                    return mapper.toDto(user);
                })
                .orElseThrow(() -> new VerificationException("Invalid verification code", "Ungültiger Verifizierungscode"));
    }

    /**
     * Generate a new verification code and send the email verification mail to a user
     *
     * @param user the user to send the email to
     */
    public boolean sendEmailVerificationMail(@NotNull User user) {
        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, this.emailConfirmationValidity);
        verificationService.saveVerificationCode(code);

        return mailService.sendEmailVerificationMail(user, code);
    }

    /**
     * Send an email verification mail to the currently logged-in user if their email is not yet verified.
     *
     * @return true if the email was sent, false if the email is already verified.
     * @throws NotLoggedInException if no user is logged in.
     */
    public boolean sendEmailVerificationMail() {
        User user = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);
        if (user.isEmailConfirmed()) {
            this.logger.info("Not sending verification email to {} because email is already verified", user.getUsername());
            return false;
        }

        return this.sendEmailVerificationMail(user);
    }

    public boolean sendPasswordResetMail(@NotNull String email) {
        User user = getRawByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        VerificationCode code = user.createVerificationCode(VerificationCodeType.PASSWORD_RESET, this.emailConfirmationValidity);
        verificationService.saveVerificationCode(code);

        return mailService.sendPasswordResetMail(user, code);
    }

    public boolean resetPassword(ResetPasswordReq resetPasswordReq) {
        verificationService.getVerificationCode(resetPasswordReq.code())
                .map(VerificationCode::getUser)
                .filter(user -> verificationService.verifyPasswordResetCode(user, resetPasswordReq))
                .ifPresentOrElse(user -> {
                    user.setPassword(passwordEncoder.encode(resetPasswordReq.newPassword()));
                    repository.save(user);
                    verificationService.deleteVerificationCode(resetPasswordReq.code());
                }, () -> {
                    throw new VerificationException("Invalid password reset code", "Ungültiger Passwort-Zurücksetzungs-Code");
                });
        return true;
    }
}
