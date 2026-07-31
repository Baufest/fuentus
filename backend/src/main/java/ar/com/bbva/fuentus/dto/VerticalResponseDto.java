package ar.com.bbva.fuentus.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerticalResponseDto {
    
    private Long id;
    private String name;
    private String refVertical;
    private List<FactoryDTO> orgN2fabrica;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class FactoryDTO {
        private String name;
        private String ownerFactory;
    }
}