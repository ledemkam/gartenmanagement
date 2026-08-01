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

	PageResponse<ProductDTOResponse> findByCategory(ProductCategory category, Pageable pageable);
	ProductDTOResponse adjustStock(String id, Integer quantity);

	ProductDTOResponse applyDiscount(String id, BigDecimal discountPercentage);

	PageResponse<ProductDTOResponse> getOutOfStockProducts(Pageable pageable);
	PageResponse<ProductDTOResponse> getLowStockProducts(Integer threshold,Pageable pageable);

}
