package ar.com.bbva.fuentus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductividadDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nucleusId")
    private Long nucleusId;

    @JsonProperty("servicioN2")
    private String servicioN2; // Para mostrar el nombre del servicio

    @JsonProperty("features")
    private Integer features;

    @JsonProperty("ftesDirectos")
    private Double ftesDirectos;

    @JsonProperty("ftesIndirectos")
    private Double ftesIndirectos;

    @JsonProperty("fecha")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
}
