package com.ozan.be.product.domain;

import com.ozan.be.common.Auditable;
import com.ozan.be.product.domain.dto.PredefinedWindowCsvDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
    name = "predefined_windows",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"fullName"})})
public class PredefinedWindows extends Auditable<UUID> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String fullName;
  private String windowProducer;
  private String windowType;
  private String windowNumber;
  private Integer windowWidth;
  private Integer windowHeight;

  public PredefinedWindows(PredefinedWindowCsvDTO dto) {
    this.fullName = dto.getWindowName();
    this.windowProducer = dto.getWindowProducer();
    this.windowType = dto.getWindowType();
    this.windowNumber = dto.getWindowNumber();
    this.windowWidth = (int) (Double.parseDouble(dto.getWindowWidth()) * 10);
    this.windowHeight = (int) (Double.parseDouble(dto.getWindowHeight()) * 10);
  }
}
