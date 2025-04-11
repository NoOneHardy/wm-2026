package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.NotFoundException;
import ch.no1hardy.service.front.game.BetGameRes;
import ch.no1hardy.service.front.game.GameReq;
import ch.no1hardy.service.front.game.ScoreReq;
import ch.no1hardy.service.mapper.GameMapperImpl;
import ch.no1hardy.service.model.game.Game;
import ch.no1hardy.service.model.game.GameRepository;
import ch.no1hardy.service.model.game.Score;
import ch.no1hardy.service.model.game.ScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameService {
    private final GameRepository repository;
    private final ScoreRepository scoreRepository;
    private final GameMapperImpl mapper;

    public List<BetGameRes> listAll() {
        return repository.findAll().stream().filter(Game::isActive).map(mapper::toDto).toList();
    }

    public BetGameRes create(GameReq dto) {
        Game game = repository.save(mapper.toEntity(dto));
        return mapper.toDto(game);
    }

    public BetGameRes uploadResult(String id, ScoreReq dto) {
        Game game = repository.findById(id).orElse(null);
        if (game == null) throw new NotFoundException("Game " + id + " not found");

        dto.setGame(id);
        Score result;
        Score existingResult = game.getResult();
        if (existingResult != null) {
            mapper.update(dto, existingResult);
            result = existingResult;
        } else {
            result = scoreRepository.save(mapper.toEntity(dto));
        }
        game.setResult(result);
        return mapper.toDto(repository.save(game));
    }
}
