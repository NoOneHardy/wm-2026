package ch.no1hardy.service.service;

import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.mapper.GameMapperImpl;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository repository;
    private final GameMapperImpl mapper;

    public List<BetGameRes> listAll() {
        return repository.findAll().stream().filter(Game::isActive).map(mapper::toDto).toList();
    }

    public BetGameRes create(GameReq dto) {
        Game game = repository.save(mapper.toEntity(dto));
        return mapper.toDto(game);
    }
}
