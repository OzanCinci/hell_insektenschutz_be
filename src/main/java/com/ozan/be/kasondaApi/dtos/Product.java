package com.ozan.be.kasondaApi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class Product {
  public String id;
  public List<Color> colors;
  public List<SubCategory> subCategories;
  public List<BlendColor> blendColors;

  @Data
  public static class Color {
    public String id;
    public String title;
    public Properties properties;
    public String tileImage;
    public String previewImage;
  }

  @Data
  public static class Properties {
    @JsonProperty("Icon")
    private String icon;

    @JsonProperty("HexColor")
    private String hexColor;

    @JsonProperty("Translucency")
    private String translucency;

    @JsonProperty("MaxWidth")
    private String maxWidth;

    @JsonProperty("MaterialType")
    private String materialType;

    @JsonProperty("DampWipeable")
    private String dampWipeable;

    @JsonProperty("SuitableHumidRooms")
    private String suitableHumidRooms;

    @JsonProperty("DirtRepellant")
    private String dirtRepellant;

    @JsonProperty("PearlCoated")
    private String pearlCoated;

    @JsonProperty("OekoTexStandard100")
    private String oekoTexStandard100;

    @JsonProperty("Availability")
    private String availability;

    @JsonProperty("MinPrice")
    private String minPrice;

    @JsonProperty("Hue")
    private List<String> hue;

    @JsonProperty("Material")
    private List<String> material;

    @JsonProperty("Room")
    private List<String> room;

    @JsonProperty("Look")
    private List<String> look;

    @JsonProperty("LamellaWidth")
    private List<String> lamellaWidth;

    @JsonProperty("Function")
    private List<String> function;

    @JsonProperty("BlindWidth")
    private String blindWidth;

    @JsonProperty("WoodGrain")
    private String woodGrain;

    @JsonProperty("SlatsStandard")
    private String SlatsStandard;

    @JsonProperty("SlatsPerforated")
    private String SlatsPerforated;

    @JsonProperty("SlatsMatte")
    private String SlatsMatte;

    @JsonProperty("SlatsHighGloss")
    private String SlatsHighGloss;

    @JsonProperty("SlatsBrushed")
    private String SlatsBrushed;

    @JsonProperty("SlatsMetallic")
    private String SlatsMetallic;
  }

  public static class SubCategory {
    public String id;
    public String title;
  }

  public static class BlendColor {
    public String id;
    public String title;
  }
}
