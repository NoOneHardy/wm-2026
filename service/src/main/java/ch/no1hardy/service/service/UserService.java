package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.user.CheckRes;
import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.mapper.UserMapperImpl;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import com.google.common.hash.Hashing;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserService {
    protected final UserRepository repository;
    protected final UserMapperImpl mapper;

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
        return repository.findByUsername(username).isEmpty();
    }

    public Boolean isEmailAvailable(String email) {
        return repository.findByEmail(email).isEmpty();
    }

    public UserRes get(String id) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) throw new NotFoundException("User with id " + id + " not found");
        return mapper.toDto(entity);
    }

    public UserRes create(UserReq dto) {
        User entity = mapper.toEntity(dto);
        String password = Hashing.sha256().hashString(entity.getPassword(), StandardCharsets.UTF_8).toString();
        entity.setPassword(password);
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
}
