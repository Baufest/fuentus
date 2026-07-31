package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.ComboValuesDTO;
import ar.com.bbva.fuentus.dto.NucleusGetRequestDTO;
import ar.com.bbva.fuentus.services.NucleusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/nucleus")
@RestController
public class NucleusController {

     @Autowired
     private NucleusService nucleusService;

    @GetMapping("/service/{id}")
    public ResponseEntity<NucleusGetRequestDTO> getServiceById(@PathVariable Long id) {
        NucleusGetRequestDTO nucleusDTO = nucleusService.getServiceById(id);
        if (nucleusDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nucleusDTO);
    }

    @GetMapping("/{uuaa}")
    public ResponseEntity<List<NucleusGetRequestDTO>> getAppsByUUAA(@PathVariable String uuaa) {

        List<NucleusGetRequestDTO> nucleusGetRequestDTOList =  nucleusService.getAppsByUUAA(uuaa);
        return ResponseEntity.ok(nucleusGetRequestDTOList);

    }

    @GetMapping("/search")
    public ResponseEntity<Page<NucleusGetRequestDTO>> searchNucleus(
            @RequestParam(required = false, name = "q") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String uol2,
            @RequestParam(required = false) String sn1,
            @RequestParam(required = false) String sn2) {
        Page<NucleusGetRequestDTO> nucleusPage = nucleusService.searchWithFilters(searchTerm, vertical, uol2, sn1, sn2, page);
        return ResponseEntity.ok(nucleusPage);
    }

    @GetMapping("/filters")
    public ResponseEntity<Page<NucleusGetRequestDTO>> getNucleusByOptionalFields(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String orgN1,
            @RequestParam(required = false) String orgN2fabrica,
            @RequestParam(defaultValue = "0") int page) {

        Page<NucleusGetRequestDTO> nucleusGetRequestDTOPage = nucleusService.getNucleusByOptionalFields(area, orgN1, orgN2fabrica, page);
        return ResponseEntity.ok(nucleusGetRequestDTOPage);

    }

    @GetMapping("/combo-values")
    public ResponseEntity<ComboValuesDTO> getComboValues(
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String uol2,
            @RequestParam(required = false) String sn1) {
        ComboValuesDTO comboValues = nucleusService.getComboValues(vertical, uol2, sn1);
        return ResponseEntity.ok(comboValues);
    }

}
