package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrgN2FabricaResponseDto {

    private String orgN2;
    private String refVertical;
    private List<VerticalResponseDto> verticales;
}