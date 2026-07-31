package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.ComparativaProductividadDTO;
import ar.com.bbva.fuentus.dto.GraficoNivelDTO;
import ar.com.bbva.fuentus.dto.ItemGraficoDTO;
import ar.com.bbva.fuentus.entities.Nucleus;
import ar.com.bbva.fuentus.entities.Productividad;
import ar.com.bbva.fuentus.entities.Velocidad;
import ar.com.bbva.fuentus.entities.VerticalNucleus;
import ar.com.bbva.fuentus.repositories.NucleusRepository;
import ar.com.bbva.fuentus.repositories.ProductividadRepository;
import ar.com.bbva.fuentus.repositories.VelocidadRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ComparativaProductividadService {

    @Autowired
    private NucleusRepository nucleusRepository;

    @Autowired
    private ProductividadRepository productividadRepository;

    @Autowired
    private VelocidadRepository velocidadRepository;

    /**
     * Obtiene los datos comparativos según los filtros aplicados
     */
    public ComparativaProductividadDTO getComparativa(String vertical, String fabrica, String sn1, String sn2) {
        log.info("Generando comparativa - Vertical: {}, Fabrica: {}, SN1: {}, SN2: {}", 
                 vertical, fabrica, sn1, sn2);

        List<GraficoNivelDTO> graficos = new ArrayList<>();
        String nivelFiltrado = determinarNivelFiltrado(vertical, fabrica, sn1, sn2);

        // Obtener nucleus filtrados
        List<Nucleus> allNucleus = nucleusRepository.findAll();
        List<Nucleus> filteredNucleus = filterNucleus(allNucleus, vertical, fabrica, sn1, sn2);

        if (filteredNucleus.isEmpty()) {
            log.warn("No se encontraron datos con los filtros aplicados");
            return new ComparativaProductividadDTO(nivelFiltrado, graficos);
        }

        // Obtener datos de productividad y velocidad
        List<Productividad> allProductividad = productividadRepository.findAll();
        List<Velocidad> allVelocidad = velocidadRepository.findAll();

        Map<Long, List<Productividad>> productividadByNucleus = allProductividad.stream()
                .filter(p -> p.getNucleus() != null && p.getNucleus().getId() != null)
                .collect(Collectors.groupingBy(p -> p.getNucleus().getId()));
        
        Map<Long, List<Velocidad>> velocidadByNucleus = allVelocidad.stream()
                .filter(v -> v.getNucleus() != null && v.getNucleus().getId() != null)
                .collect(Collectors.groupingBy(v -> v.getNucleus().getId()));

        // Generar gráficos según el nivel filtrado
        if (sn1 != null && !sn1.isEmpty()) {
            // Filtrado por SN1 → mostrar SN2
            GraficoNivelDTO graficoSN2 = generarGraficoNivel("SN2", "Service N2", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN2);
            if (graficoSN2 != null && !graficoSN2.getItems().isEmpty()) {
                graficos.add(graficoSN2);
            }
            
        } else if (fabrica != null && !fabrica.isEmpty()) {
            // Filtrado por Fábrica → mostrar SN1 y SN2
            GraficoNivelDTO graficoSN1 = generarGraficoNivel("SN1", "Service N1", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN1);
            if (graficoSN1 != null && !graficoSN1.getItems().isEmpty()) {
                graficos.add(graficoSN1);
            }

            GraficoNivelDTO graficoSN2 = generarGraficoNivel("SN2", "Service N2", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN2);
            if (graficoSN2 != null && !graficoSN2.getItems().isEmpty()) {
                graficos.add(graficoSN2);
            }
            
        } else if (vertical != null && !vertical.isEmpty()) {
            // Filtrado por Vertical → mostrar Fábrica, SN1 y SN2
            GraficoNivelDTO graficoFabrica = generarGraficoNivel("FABRICA", "Fábricas", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getOrgN2fabrica);
            if (graficoFabrica != null && !graficoFabrica.getItems().isEmpty()) {
                graficos.add(graficoFabrica);
            }

            GraficoNivelDTO graficoSN1 = generarGraficoNivel("SN1", "Service N1", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN1);
            if (graficoSN1 != null && !graficoSN1.getItems().isEmpty()) {
                graficos.add(graficoSN1);
            }

            GraficoNivelDTO graficoSN2 = generarGraficoNivel("SN2", "Service N2", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN2);
            if (graficoSN2 != null && !graficoSN2.getItems().isEmpty()) {
                graficos.add(graficoSN2);
            }
            
        } else {
            // Sin filtros → mostrar Vertical, Fábrica, SN1 y SN2
            GraficoNivelDTO graficoVertical = generarGraficoVertical(filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus);
            if (graficoVertical != null && !graficoVertical.getItems().isEmpty()) {
                graficos.add(graficoVertical);
            }

            GraficoNivelDTO graficoFabrica = generarGraficoNivel("FABRICA", "Fábricas", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getOrgN2fabrica);
            if (graficoFabrica != null && !graficoFabrica.getItems().isEmpty()) {
                graficos.add(graficoFabrica);
            }

            GraficoNivelDTO graficoSN1 = generarGraficoNivel("SN1", "Service N1", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN1);
            if (graficoSN1 != null && !graficoSN1.getItems().isEmpty()) {
                graficos.add(graficoSN1);
            }

            GraficoNivelDTO graficoSN2 = generarGraficoNivel("SN2", "Service N2", filteredNucleus, 
                    productividadByNucleus, velocidadByNucleus, Nucleus::getServiceN2);
            if (graficoSN2 != null && !graficoSN2.getItems().isEmpty()) {
                graficos.add(graficoSN2);
            }
        }

        log.info("Se generaron {} gráficos", graficos.size());
        return new ComparativaProductividadDTO(nivelFiltrado, graficos);
    }

    /**
     * Determina qué nivel está filtrado
     */
    private String determinarNivelFiltrado(String vertical, String fabrica, String sn1, String sn2) {
        if (sn1 != null && !sn1.isEmpty()) return "SN1";
        if (fabrica != null && !fabrica.isEmpty()) return "FABRICA";
        if (vertical != null && !vertical.isEmpty()) return "VERTICAL";
        return "NINGUNO";
    }

    /**
     * Filtra nucleus según parámetros
     */
    private List<Nucleus> filterNucleus(List<Nucleus> allNucleus, String vertical, String fabrica, String sn1, String sn2) {
        return allNucleus.stream()
                .filter(n -> vertical == null || vertical.isEmpty() || 
                        n.getVerticalNucleus().stream()
                                .anyMatch(vn -> vn.getVertical().getName().equals(vertical)))
                .filter(n -> fabrica == null || fabrica.isEmpty() || 
                        (n.getOrgN2fabrica() != null && n.getOrgN2fabrica().equals(fabrica)))
                .filter(n -> sn1 == null || sn1.isEmpty() || 
                        (n.getServiceN1() != null && n.getServiceN1().equals(sn1)))
                .filter(n -> sn2 == null || sn2.isEmpty() || 
                        (n.getServiceN2() != null && n.getServiceN2().equals(sn2)))
                .collect(Collectors.toList());
    }

    /**
     * Interfaz funcional para extraer valores
     */
    @FunctionalInterface
    private interface NivelExtractor {
        String extract(Nucleus n);
    }

    /**
     * Genera un gráfico para un nivel específico (Fabrica, SN1, SN2)
     */
    private GraficoNivelDTO generarGraficoNivel(
            String nivelTipo,
            String titulo,
            List<Nucleus> nucleusList,
            Map<Long, List<Productividad>> productividadMap,
            Map<Long, List<Velocidad>> velocidadMap,
            NivelExtractor extractor) {

        // Agrupar por nivel
        Map<String, List<Nucleus>> byNivel = nucleusList.stream()
                .filter(n -> {
                    String valor = extractor.extract(n);
                    return valor != null && !valor.isEmpty();
                })
                .collect(Collectors.groupingBy(extractor::extract));

        if (byNivel.isEmpty()) {
            return null;
        }

        // Calcular métricas para cada nivel
        List<ItemGraficoDTO> items = byNivel.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    return calcularMetricasCompletas(nombre, nucleus, productividadMap, velocidadMap);
                })
                .filter(item -> item.getProductividad() != null || item.getPromedioLT() != null || item.getPromedioCT() != null)
                .sorted(Comparator.comparing(ItemGraficoDTO::getProductividad, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        return new GraficoNivelDTO(nivelTipo, titulo, items);
    }

    /**
     * Genera gráfico para Vertical (caso especial)
     */
    private GraficoNivelDTO generarGraficoVertical(
            List<Nucleus> nucleusList,
            Map<Long, List<Productividad>> productividadMap,
            Map<Long, List<Velocidad>> velocidadMap) {

        // Agrupar por vertical
        Map<String, List<Nucleus>> byVertical = new HashMap<>();
        for (Nucleus n : nucleusList) {
            for (VerticalNucleus vn : n.getVerticalNucleus()) {
                String verticalName = vn.getVertical().getName();
                byVertical.computeIfAbsent(verticalName, k -> new ArrayList<>()).add(n);
            }
        }

        if (byVertical.isEmpty()) {
            return null;
        }

        List<ItemGraficoDTO> items = byVertical.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    return calcularMetricasCompletas(nombre, nucleus, productividadMap, velocidadMap);
                })
                .filter(item -> item.getProductividad() != null || item.getPromedioLT() != null || item.getPromedioCT() != null)
                .sorted(Comparator.comparing(ItemGraficoDTO::getProductividad, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        return new GraficoNivelDTO("VERTICAL", "Verticales", items);
    }

    /**
     * Calcula todas las métricas para un conjunto de nucleus
     */
    private ItemGraficoDTO calcularMetricasCompletas(
            String nombre,
            List<Nucleus> nucleusList,
            Map<Long, List<Productividad>> productividadMap,
            Map<Long, List<Velocidad>> velocidadMap) {

        ItemGraficoDTO item = new ItemGraficoDTO();
        item.setNombre(nombre);

        // Calcular Productividad
        int totalFeatures = 0;
        double totalFTEs = 0.0;
        
        for (Nucleus n : nucleusList) {
            List<Productividad> prodList = productividadMap.get(n.getId());
            if (prodList != null && !prodList.isEmpty()) {
                Productividad prod = prodList.get(prodList.size() - 1);
                if (prod.getFeatures() != null) {
                    totalFeatures += prod.getFeatures();
                }
                if (prod.getFtesDirectos() != null) {
                    totalFTEs += prod.getFtesDirectos();
                }
                if (prod.getFtesIndirectos() != null) {
                    totalFTEs += prod.getFtesIndirectos();
                }
            }
        }
        
        item.setTotalFeatures(totalFeatures);
        item.setTotalFTEs(totalFTEs);
        
        if (totalFTEs > 0) {
            item.setProductividad(totalFeatures / totalFTEs);
        }

        // Calcular Velocidad LT y CT
        int totalLT = 0;
        int totalCT = 0;
        int countLT = 0;
        int countCT = 0;
        
        for (Nucleus n : nucleusList) {
            List<Velocidad> velList = velocidadMap.get(n.getId());
            if (velList != null) {
                for (Velocidad vel : velList) {
                    if (vel.getLt() != null) {
                        totalLT += vel.getLt();
                        countLT++;
                    }
                    if (vel.getCt() != null) {
                        totalCT += vel.getCt();
                        countCT++;
                    }
                }
            }
        }
        
        if (countLT > 0) {
            item.setPromedioLT((double) totalLT / countLT);
        }
        if (countCT > 0) {
            item.setPromedioCT((double) totalCT / countCT);
        }

        return item;
    }
}
