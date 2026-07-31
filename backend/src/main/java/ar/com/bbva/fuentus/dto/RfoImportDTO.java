package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.opencsv.bean.CsvBindByName;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RfoImportDTO {
    
    @CsvBindByName(column = "RFO ID")
    @JsonProperty("rfoId")
    private Long rfoId;
    
    @CsvBindByName(column = "SEVICIO N2")
    @JsonProperty("sevicioN2")
    private String sevicioN2;
    
    @CsvBindByName(column = "EMAIL")
    @JsonProperty("email")
    private String email;
    
    @CsvBindByName(column = "ESTADO RFO")
    @JsonProperty("estadoRfo")
    private String estadoRfo;
    
    @CsvBindByName(column = "FECHA puesta en producción")
    @JsonProperty("fechaPuestaProduccion")
    private String fechaPuestaProduccion;
}