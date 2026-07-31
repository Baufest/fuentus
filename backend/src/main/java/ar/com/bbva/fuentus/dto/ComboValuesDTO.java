package ar.com.bbva.fuentus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboValuesDTO {
    private List<String> verticals;
    private List<String> uol2Values;
    private List<String> sn1Values;
    private List<String> sn2Values;
}