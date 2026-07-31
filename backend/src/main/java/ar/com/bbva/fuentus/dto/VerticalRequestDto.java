package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VerticalRequestDto {
    private String name;
    private String refVertical;
    private List<String> orgN2fabricaList;
}