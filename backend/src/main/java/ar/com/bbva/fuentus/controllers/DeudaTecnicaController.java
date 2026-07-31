package ar.com.bbva.fuentus.controllers;

import ar.com.bbva.fuentus.dto.DeudaTecnicaItemDTO;
import ar.com.bbva.fuentus.services.DeudaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deuda-tecnica")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class DeudaTecnicaController {

    @Autowired
    private DeudaTecnicaService deudaTecnicaService;

    @GetMapping
    public ResponseEntity<List<DeudaTecnicaItemDTO>> getDeudaTecnica(
            @RequestParam(required = false) String vertical,
            @RequestParam(required = false) String fabrica,
            @RequestParam(required = false) String sn1
    ) {
        List<DeudaTecnicaItemDTO> data = deudaTecnicaService.getDeudaTecnicaPorNivel(vertical, fabrica, sn1);
        return ResponseEntity.ok(data);
    }
}
