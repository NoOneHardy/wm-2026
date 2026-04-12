package ch.no1hardy.service.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface EntityMapper<E, Req, Res> {
    void update(Req dto, @MappingTarget E entity);

    E toEntity(Req dto);

    Res toDto(E entity);

    List<Res> toDto(List<E> entities);
}
