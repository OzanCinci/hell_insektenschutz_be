package com.ozan.be.externalProducts;

import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.externalProducts.dtos.KasondaPriceRequestDTO;
import com.ozan.be.externalProducts.dtos.KasondaPriceResponseDTO;
import com.ozan.be.kasondaApi.dtos.ColorOption;
import com.ozan.be.kasondaApi.dtos.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external-products")
@RequiredArgsConstructor
public class ExternalProductsController {

  private final ExternalProductsService externalProductsService;

  @GetMapping("/colors/{category}")
  public ResponseEntity<Product> getAllProductsByCategory(
      @PathVariable("category") String category) {
    Product response = externalProductsService.getAllProductsByCategory(category);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/price")
  public ResponseEntity<KasondaPriceResponseDTO> getPrice(
      @RequestBody KasondaPriceRequestDTO requestDTO) {
    KasondaPriceResponseDTO response = externalProductsService.getPrice(requestDTO);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/colors/{category}/{id}")
  public ResponseEntity<ColorOption> getColorOptionByCategoryAndId(
      @PathVariable("category") String category, @PathVariable("id") String id) {
    ColorOption response = externalProductsService.getColorOptionByCategoryAndId(category, id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/test/{bool}")
  private ResponseEntity<String> test(@PathVariable("bool") String bool) {
    if (bool.equals("bad"))
      throw new BadRequestException("it is a bad request exception");

    return ResponseEntity.ok("No exception");
  }
}
