package de0.backend.gartenmanagement.mapper;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper implements EntityMapper<Product, ProductDTOResponse> {

    @Override
    public ProductDTOResponse toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .active(entity.getActive())
                .stock(entity.getStock())
                .creationDate(entity.getCreationDate())
                .build();
    }

    @Override
    public Product toEntity(ProductDTOResponse dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .category(dto.category())
                .active(dto.active())
                .stock(dto.stock())
                .creationDate(dto.creationDate())
                .build();
    }

    /**
     * convert list from entities into a list of DTOs
     *
     * @param entities the list of entities
     * @return list of DTOs
     */
    @Override
    public List<ProductDTOResponse> toDtoList(List<Product> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of DTOs into a list of entities.
     *
     * @param dtos the list of DTOs
     * @return the list of entities
     */
    @Override
    public List<Product> toEntityList(List<ProductDTOResponse> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * convert createProductDTO to entity product
     *
     * @param createDto  the creation DTO
     * @return Product entity
     */
    public Product toEntityFromCreate(ProductDTORequest createDto) {
        if (createDto == null) {
            return null;
        }
        return Product.builder()
                .name(createDto.name())
                .description(createDto.description())
                .price(createDto.price())
                .category(createDto.category())
                .active(true)// activ im default
                .stock(createDto.stock())
                .build();
    }

    /**
     * Updates an existing entity with data from a DTO.
     *
     * @param dto  the source DTO
     * @param entity: the entity to update
     */
    public void  updateEntityFromDto(ProductDTOResponse dto, Product entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setCategory(dto.category());
        entity.setActive(dto.active());
        entity.setStock(dto.stock());
    }
}
