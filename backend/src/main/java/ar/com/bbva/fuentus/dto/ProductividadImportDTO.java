package ar.com.bbva.fuentus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductividadImportDTO {

    @CsvBindByName(column = "Geografía Servicio")
    @JsonProperty("Geografía Servicio")
    private String geografiaServicio;

    @CsvBindByName(column = "Servicio N1")
    @JsonProperty("Servicio N1")
    private String servicioN1;

    @CsvBindByName(column = "Servicio N2")
    @JsonProperty("Servicio N2")
    private String servicioN2;

    @CsvBindByName(column = "Full Service Id")
    @JsonProperty("Full Service Id")
    private String fullServiceId;

    @CsvBindByName(column = "UOL1")
    @JsonProperty("UOL1")
    private String uol1;

    @CsvBindByName(column = "UOL2")
    @JsonProperty("UOL2")
    private String uol2;

    @CsvBindByName(column = "Features")
    @JsonProperty("Features")
    private String features;

    @CsvBindByName(column = "FTEs Directos")
    @JsonProperty("FTEs Directos")
    private String ftesDirectos;

    @CsvBindByName(column = "FTEs Indirectos")
    @JsonProperty("FTEs Indirectos")
    private String ftesIndirectos;

    @CsvBindByName(column = "FTEs Totales")
    @JsonProperty("FTEs Totales")
    private String ftesTotales;

    @CsvBindByName(column = "Productividad")
    @JsonProperty("Productividad")
    private String productividad;

    @CsvBindByName(column = "LT")
    @JsonProperty("LT")
    private String lt;

    @CsvBindByName(column = "CT")
    @JsonProperty("CT")
    private String ct;
}
