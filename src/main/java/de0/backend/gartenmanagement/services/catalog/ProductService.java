package de0.backend.gartenmanagement.services.catalog;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.services.CrudServices;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService extends CrudServices<ProductDTORequest, ProductDTOResponse,String> {


}
