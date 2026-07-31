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
public class VelocidadDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nucleusId")
    private Long nucleusId;

    @JsonProperty("servicioN2")
    private String servicioN2;

    @JsonProperty("lt")
    private Integer lt;

    @JsonProperty("ct")
    private Integer ct;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
