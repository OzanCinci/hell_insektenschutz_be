package com.ozan.be.product;

import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.utils.ModelMapperUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public Product getProductByIdThrowsException(UUID id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("No product found with id: " + id));
  }

  public void saveAndFlush(Product product) {
    productRepository.saveAndFlush(product);
  }

  public Product getProductById(UUID id) {
    return getProductByIdThrowsException(id);
  }

  public Map<UUID, Product> findByIdInAndMapByUUID(Set<UUID> ids) {
    List<Product> products = productRepository.findByIdIn(ids);
    return ModelMapperUtils.convertListToMap(products, Product::getId);
  }
}
