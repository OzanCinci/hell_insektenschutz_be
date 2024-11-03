package com.ozan.be.kasondaApi;

import com.ozan.be.customException.types.KasondaApiException;
import com.ozan.be.externalProducts.dtos.KasondaPriceRequestDTO;
import com.ozan.be.externalProducts.dtos.KasondaPriceResponseDTO;
import com.ozan.be.externalProducts.dtos.extendedPrice.BaseExtendedPriceDTO;
import com.ozan.be.kasondaApi.dtos.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KasondaApiService {
  private final WebClient webClient;

  @Autowired
  public KasondaApiService(@Qualifier("kasondaApi") WebClient webClient) {
    this.webClient = webClient;
  }

  public Product getProductList(String category) {
    String uri = "categories/" + category + ".json";
    Mono<Product> response =
        webClient
            .get()
            .uri(uri)
            .retrieve()
            .onStatus(
                status -> !status.is2xxSuccessful(),
                clientResponse ->
                    clientResponse
                        .bodyToMono(String.class)
                        .flatMap(
                            errorBody ->
                                Mono.error(
                                    new KasondaApiException(
                                        "Kasonda Request Failed with status: "
                                            + clientResponse.statusCode()
                                            + " and body: "
                                            + errorBody))))
            .bodyToMono(Product.class)
            .doOnError(
                error -> System.out.println("Error during API request: " + error.getMessage()));
    return response.block();
  }

  public KasondaPriceResponseDTO getPrice(KasondaPriceRequestDTO requestDTO) {
    String uri = "getPrice";
    Mono<KasondaPriceResponseDTO> response =
        webClient
            .post()
            .uri(uri)
            .body(Mono.just(requestDTO), KasondaPriceRequestDTO.class)
            .retrieve()
            .onStatus(
                status -> !status.is2xxSuccessful(),
                clientResponse ->
                    clientResponse
                        .bodyToMono(String.class)
                        .flatMap(
                            errorBody ->
                                Mono.error(
                                    new KasondaApiException(
                                        "Kasonda Request Failed with status: "
                                            + clientResponse.statusCode()
                                            + " and body: "
                                            + errorBody))))
            .bodyToMono(KasondaPriceResponseDTO.class)
            .doOnError(
                error -> System.out.println("Error during API request: " + error.getMessage()));

    return response.block();
  }

  public KasondaPriceResponseDTO getExtendedPrice(BaseExtendedPriceDTO requestDTO) {
    String uri = "getPrice";
    Mono<KasondaPriceResponseDTO> response =
        webClient
            .post()
            .uri(uri)
            .body(Mono.just(requestDTO), BaseExtendedPriceDTO.class)
            .retrieve()
            .onStatus(
                status -> !status.is2xxSuccessful(),
                clientResponse ->
                    clientResponse
                        .bodyToMono(String.class)
                        .flatMap(
                            errorBody ->
                                Mono.error(
                                    new KasondaApiException(
                                        "Kasonda Request Failed with status: "
                                            + clientResponse.statusCode()
                                            + " and body: "
                                            + errorBody))))
            .bodyToMono(KasondaPriceResponseDTO.class)
            .doOnError(
                error -> System.out.println("Error during API request: " + error.getMessage()));

    return response.block();
  }
}
