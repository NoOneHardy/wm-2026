package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.BadRequestException;
import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.LoginReq;
import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.mapper.UserMapperImpl;
import ch.no1hardy.service.model.game.Bet;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapperImpl mapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<UserRes> list() {
        return mapper.toDto(repository.findAll()
                .stream()
                .filter(User::isActive)
                .toList());
    }

    public List<UserRes> listAll() {
        return mapper.toDto(repository.findAll());
    }

    public CheckRes check(@Nullable String username, @Nullable String email) {
        return CheckRes.builder()
                .isUsernameAvailable(username == null || isUsernameAvailable(username))
                .isEmailAvailable(email == null || isEmailAvailable(email))
                .build();
    }

    public Boolean isUsernameAvailable(String username) {
        return repository.findByUsername(username).stream().noneMatch(User::isActive);
    }

    public Boolean isEmailAvailable(String email) {
        return repository.findByEmail(email).stream().noneMatch(User::isActive);
    }

    public UserRes get(String id) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) throw new NotFoundException("User with id " + id + " not found");
        return mapper.toDto(entity);
    }

    public UserRes create(UserReq dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        String username = dto.getUsername();
        String email = dto.getEmail();

        if (!isEmailAvailable(email)) throw new BadRequestException("Email " + email + " is already taken");
        if (!isUsernameAvailable(username)) throw new BadRequestException("Username " + username + " is already taken");

        if (!Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email).matches())
            throw new BadRequestException("Invalid email format");

        User entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public UserRes update(String id, UserReq dto) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) throw new NotFoundException("User with id " + id + " not found");
        mapper.update(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    public UserRes delete(String id) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) throw new NotFoundException("User with id " + id + " not found");
        entity.delete();
        return mapper.toDto(repository.save(entity));
    }

    public UserRes login(LoginReq dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );
        return mapper.toDto(repository.findByUsername(dto.getUsername()).orElseThrow());
    }

    public UserRes getLoggedInUserRes() {
        User currentUser = getLoggedInUser();
        return currentUser == null ? new UserRes() : mapper.toDto(currentUser);
    }

    @Nullable
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        User currentUser = (User) authentication.getPrincipal();
        return repository.findById(currentUser.getId()).orElse(null);
    }

    public void addUserPoints(Game game) {
        if (game.getResult() == null) return;
        Score result = game.getResult();

        for (Bet bet : game.getBets()) {
            if (bet.getUpdatedAt().isAfter(result.getCreatedAt())) return;
            int points = calculateUserPoints(bet, result);

            bet.getUser().setPoints(bet.getUser().getPoints() + points);
        }
    }

    public void removeUserPoints(Game game) {
        if (game.getResult() == null) return;
        Score result = game.getResult();

        for (Bet bet : game.getBets()) {
            if (bet.getUpdatedAt().isAfter(result.getCreatedAt())) return;
            int points = calculateUserPoints(bet, result);

            bet.getUser().setPoints(bet.getUser().getPoints() - points);
        }
    }

    private int calculateUserPoints(Bet bet, Score result) {
        int points = 0;

        Boolean correctWinnerHome = bet.isHomeTeamWinner() && result.isHomeTeamWinner();
        Boolean correctWinnerGuest = bet.isGuestTeamWinner() && result.isGuestTeamWinner();
        Boolean correctTie = bet.isTie() && result.isTie();

        if (correctWinnerHome || correctWinnerGuest || correctTie) points += 50;

        if (bet.getScoreTeamGuest().equals(result.getScoreTeamGuest())) points += 20;
        if (bet.getScoreTeamHome().equals(result.getScoreTeamHome())) points += 20;

        if (bet.getTotalScore().equals(result.getTotalScore())) points += 10;

        return points * bet.getJoker();
    }
}
