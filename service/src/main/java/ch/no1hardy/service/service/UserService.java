package ch.no1hardy.service.service;

import ch.no1hardy.service.front.user.UserReq;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.mapper.UserMapperImpl;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.user.UserRepository;
import com.google.common.hash.Hashing;
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

    public UserRes get(String id) {
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    public UserRes create(UserReq dto) {
        User entity = mapper.toEntity(dto);
        String password = Hashing.sha256().hashString(entity.getPassword(), StandardCharsets.UTF_8).toString();
        entity.setPassword(password);
        return mapper.toDto(repository.save(entity));
    }

    public UserRes update(String id, UserReq dto) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        mapper.update(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    public UserRes delete(String id) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        entity.delete();
        return mapper.toDto(repository.save(entity));
    }
}
