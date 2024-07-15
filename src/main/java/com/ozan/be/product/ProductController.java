package com.ozan.be.product;

import com.ozan.be.common.BaseController;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController extends BaseController {

  private final ProductService productService;

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id) {
    Product product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  @GetMapping("/{category}/{name}")
  public ResponseEntity<Product> getProductByCategoryAndName(
      @PathVariable("category") String category, @PathVariable("name") String name) {
    Product product = productService.getProductByCategoryAndName(category, name);
    return ResponseEntity.ok(product);
  }
}
