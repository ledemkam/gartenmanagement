package de0.backend.gartenmanagement.services.catalog.impl;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.exceptions.DuplicateProductException;
import de0.backend.gartenmanagement.mapper.ProductMapper;
import de0.backend.gartenmanagement.repository.ProductRepository;
import de0.backend.gartenmanagement.services.catalog.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public void create(final ProductDTORequest request) {
        log.info("Create new product with name: {}", request.name());
        checkProductAlreadyExistsByName(request.name());
        final Product entity = productMapper.toEntityFromCreate(request);
        log.info("Saving product: {}", entity);
        productRepository.save(entity);

    }


    private void  checkProductAlreadyExistsByName(final String name) {
        productRepository.findByNameIgnoreCase(name)
                .ifPresent(product -> {
                    log.error("Product with name {} already exists", name);
                    throw new DuplicateProductException(name);
                });
    }


}
