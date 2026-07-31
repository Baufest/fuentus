package ar.com.bbva.fuentus.mapper;

import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class NucleusMapper {

    static public NucleusGetRequestDTO mapNucleus2GetRequestDTO(Nucleus nucleus) {
        if (nucleus == null) {
            return null;
        }
        NucleusGetRequestDTO nucleusGetRequestDTO = new NucleusGetRequestDTO();
        nucleusGetRequestDTO.setServiceId(nucleus.getId());
        nucleusGetRequestDTO.setOwnerServiceN1(nucleus.getOwnerServiceN1());
        nucleusGetRequestDTO.setServiceN1(nucleus.getServiceN1());
        nucleusGetRequestDTO.setServiceN2(nucleus.getServiceN2());
        nucleusGetRequestDTO.setUuaa(convertUUAA2List(nucleus.getUuaa()));
        nucleusGetRequestDTO.setAppOwner(nucleus.getOwnerServiceN2());
        nucleusGetRequestDTO.setVerticalName(getVerticalName(nucleus));

        // Map RFO information if available
        if (nucleus.getRfo() != null) {
            nucleusGetRequestDTO.setRfoId(nucleus.getRfo().getRfoId());
            nucleusGetRequestDTO.setRfoEstado(nucleus.getRfo().getEstadoRfo());
        }

        return nucleusGetRequestDTO;
    }



    private static List<String> convertUUAA2List(String uuaa) {
        if (uuaa == null || uuaa.isEmpty()) {
            return Collections.emptyList();
        }
        String[] uuaaArray = uuaa.split(",");
        return Arrays.asList(uuaaArray);
    }

    private static String getVerticalName(Nucleus nucleus) {
        if (nucleus.getVerticalNucleus() != null && !nucleus.getVerticalNucleus().isEmpty()) {
            return nucleus.getVerticalNucleus().get(0).getVertical().getName();
        }
        return null;
    }
}
