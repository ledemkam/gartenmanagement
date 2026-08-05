package de0.backend.gartenmanagement.services.catalog;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.common.PageResponse;
import de0.backend.gartenmanagement.services.CrudServices;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


public interface ProductService extends CrudServices<ProductDTORequest, ProductDTOResponse,String> {

	PageResponse<ProductDTOResponse> findByCategory(final ProductCategory category,final  Pageable pageable);
	ProductDTOResponse adjustStock(final String id, final Integer quantity);

	ProductDTOResponse applyDiscount(final String id, final BigDecimal discountPercentage);

	PageResponse<ProductDTOResponse> getOutOfStockProducts(final Pageable pageable);

	PageResponse<ProductDTOResponse> getLowStockProducts(final Integer threshold, final Pageable pageable);

}
