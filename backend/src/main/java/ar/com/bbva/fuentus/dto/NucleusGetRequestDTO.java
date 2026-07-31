package ar.com.bbva.fuentus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NucleusGetRequestDTO {

    private Long serviceId;
    private List<String> uuaa;
    private String serviceN1;
    private String serviceN2;
    private String ownerServiceN1;
    private String appOwner;
    private String verticalName;

    // RFO Information
    private Long rfoId;
    private String rfoEstado;

}
