package com.ozan.be.externalProducts.services;

import static java.util.Objects.isNull;

import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.externalProducts.dtos.KasondaPriceRequestDTO;
import com.ozan.be.externalProducts.dtos.KasondaPriceResponseDTO;
import com.ozan.be.externalProducts.dtos.extendedPrice.BaseExtendedPriceDTO;
import com.ozan.be.externalProducts.dtos.extendedPrice.KasondaExtendedPriceRequestDTO;
import com.ozan.be.externalProducts.services.extendedPrice.IExtendedPriceBuilder;
import com.ozan.be.kasondaApi.KasondaApiService;
import com.ozan.be.kasondaApi.dtos.ColorOption;
import com.ozan.be.kasondaApi.dtos.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalProductsService {
  private final RedisService redisService;
  private final KasondaApiService kasondaApiService;
  private final List<? extends IExtendedPriceBuilder> extendedPriceBuilders;

  private boolean keyExists(String key) {
    return redisService.keyExists(key);
  }

  public Object getObject(String key) {
    return redisService.getKeyValue(key);
  }

  private void cacheProduct(String key, Product product, long TTL) {
    redisService.setKeyProductWithTtl(key, product, TTL);
  }

  private void cachePrice(String key, KasondaPriceResponseDTO price, long TTL) {
    redisService.setKeyPriceWithTtl(key, price, TTL);
  }

  private void cacheColorOption(String key, ColorOption color, long TTL) {
    redisService.setKeyColorOptionWithTtl(key, color, TTL);
  }

  private long calculateTTLByMinute(long minute) {
    return minute * 60;
  }

  public Product getAllProductsByCategory(String category) {
    if (keyExists(category)) {
      return (Product) getObject(category);
    }

    Product product = kasondaApiService.getProductList(category);
    long TTL = calculateTTLByMinute(30);
    cacheProduct(category, product, TTL);
    return product;
  }

  public ColorOption getColorOptionByCategoryAndId(String category, String id) {
    String key = category + "/" + id;
    if (keyExists(key)) {
      return (ColorOption) getObject(key);
    }

    Product product = getAllProductsByCategory(category);
    Product.Color color =
        product.getColors().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);

    if (isNull(color)) {
      throw new BadRequestException("No such color option found!");
    }

    ColorOption colorOption = new ColorOption();
    colorOption.setId(product.getId());
    colorOption.setColor(color);
    colorOption.setSubCategories(product.getSubCategories());
    colorOption.setBlendColors(product.blendColors);

    long TTL = calculateTTLByMinute(30);
    cacheColorOption(key, colorOption, TTL);

    return colorOption;
  }

  public KasondaPriceResponseDTO getPrice(KasondaPriceRequestDTO requestDTO) {
    String key = requestDTO.toString();
    if (keyExists(key)) {
      return (KasondaPriceResponseDTO) getObject(key);
    }

    KasondaPriceResponseDTO price = kasondaApiService.getPrice(requestDTO);
    long TTL = calculateTTLByMinute(30);
    cachePrice(key, price, TTL);
    return price;
  }

  public KasondaPriceResponseDTO getPriceExtended(KasondaExtendedPriceRequestDTO requestDTO) {
    IExtendedPriceBuilder extendedPriceBuilder = provideExtendedPriceBuilder(requestDTO);
    BaseExtendedPriceDTO extendedPriceDTO = extendedPriceBuilder.build(requestDTO);
    String key = extendedPriceDTO.toString();

    if (keyExists(key)) {
      return (KasondaPriceResponseDTO) getObject(key);
    }

    KasondaPriceResponseDTO price = kasondaApiService.getExtendedPrice(extendedPriceDTO);
    long TTL = calculateTTLByMinute(30);
    cachePrice(key, price, TTL);
    return price;
  }

  private IExtendedPriceBuilder provideExtendedPriceBuilder(
      KasondaExtendedPriceRequestDTO requestDTO) {
    return extendedPriceBuilders.stream()
        .filter(
            extendedPriceBuilders ->
                extendedPriceBuilders
                    .getBuildType()
                    .equals(requestDTO.getKasondaExtendedPriceBuildType()))
        .findFirst()
        .orElseThrow(
            () ->
                new DataNotFoundException(
                    "Failed to find builder class with type: "
                        + requestDTO.getKasondaExtendedPriceBuildType()));
  }
}
