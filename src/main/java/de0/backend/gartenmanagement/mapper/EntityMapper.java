package de0.backend.gartenmanagement.mapper;

import java.util.List;
import java.util.stream.Collectors;

/** generic interface for mappers Entity <-> DTO
 *
 * @param <E>  Type for entity
 * @param <D>  Type for dto
 */

public interface EntityMapper <E, D> {

    /** convert entity to dto
     *
     * @param entity : entity converting
     * @return dto corresponding
     */
    D toDto(E entity);


    /** convert dto to entity
     *
     * @param dto : dto converting
     * @return dto corresponding
     */
    E toEntity(D dto);


    /** convert list from entities into a list of DTOs
     *
     * @param entities the list of entities
     * @return list of DTOs
     */
    default List<D> toDtoList(List<E> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Converts a list of DTOs into a list of entities.
     *
     * @param dtos the list of DTOs
     * @return the list of entities
     */
    default List<E> toEntityList(List<D> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
