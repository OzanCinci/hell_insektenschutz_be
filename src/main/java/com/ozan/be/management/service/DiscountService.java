package com.ozan.be.management.service;

import com.ozan.be.common.dtos.appconfig.AppDiscountConfigResponseDTO;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.management.domain.entity.Discount;
import com.ozan.be.management.domain.entity.QDiscount;
import com.ozan.be.management.domain.enums.DiscountProductType;
import com.ozan.be.management.dto.DiscountManagementResponseDTO;
import com.ozan.be.management.dto.DiscountUpdateRequestDTO;
import com.ozan.be.management.repository.DiscountRepository;
import com.ozan.be.utils.ModelMapperUtils;
import com.ozan.be.utils.PageableUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DiscountService {
  private final DiscountRepository discountRepository;

  private Discount findDiscountByIdThrowsError(UUID id) {
    return discountRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("No discount found with id: " + id));
  }

  private void saveAndFlush(Discount discount) {
    discountRepository.saveAndFlush(discount);
  }

  public Page<DiscountManagementResponseDTO> getAllDiscounts(Pageable pageable, Predicate filter) {
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    Page<Discount> discountPage = discountRepository.findAll(filter, finalPageable);
    List<DiscountManagementResponseDTO> discountManagementResponseDTOS =
        ModelMapperUtils.mapAll(discountPage.getContent(), DiscountManagementResponseDTO.class);
    return new PageImpl<>(
        discountManagementResponseDTOS,
        discountPage.getPageable(),
        discountPage.getTotalElements());
  }

  @Transactional
  public void updateDiscount(UUID id, @Valid DiscountUpdateRequestDTO requestDTO) {
    Discount discount = findDiscountByIdThrowsError(id);
    ModelMapperUtils.mapToExisting(requestDTO, discount);
    saveAndFlush(discount);
  }

  public AppDiscountConfigResponseDTO getDiscountConfig() {
    Predicate filter =
        ExpressionUtils.allOf(
            QDiscount.discount.isActive.eq(Boolean.TRUE),
            QDiscount.discount.discountProductType.eq(DiscountProductType.NOT_A_PRODUCT));

    Iterable<Discount> discountIterable = discountRepository.findAll(filter);
    List<AppDiscountConfigResponseDTO.DiscountOption> discountOptions =
        StreamSupport.stream(discountIterable.spliterator(), false)
            .map(
                discount -> {
                  AppDiscountConfigResponseDTO.DiscountOption discountOption =
                      ModelMapperUtils.map(
                          discount, AppDiscountConfigResponseDTO.DiscountOption.class);
                  discountOption.setKey(discount.getKey());
                  return discountOption;
                })
            .toList();
    return new AppDiscountConfigResponseDTO(discountOptions);
  }
}
