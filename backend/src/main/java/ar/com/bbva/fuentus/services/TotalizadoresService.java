package ar.com.bbva.fuentus.services;

import ar.com.bbva.fuentus.dto.RankingDTO;
import ar.com.bbva.fuentus.dto.RankingItemDTO;
import ar.com.bbva.fuentus.dto.TopLevelMetricsDTO;
import ar.com.bbva.fuentus.dto.TotalizadoresDTO;
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
public class TotalizadoresService {

    @Autowired
    private NucleusRepository nucleusRepository;

    @Autowired
    private ProductividadRepository productividadRepository;

    @Autowired
    private VelocidadRepository velocidadRepository;

    /**
     * Obtiene los mejores niveles organizacionales según los filtros aplicados
     * Retorna los mejores subniveles para cada métrica (CT, LT, Productividad)
     */
    public TotalizadoresDTO getMejoresNiveles(String vertical, String fabrica, String sn1, String sn2) {
        log.info("Calculando mejores niveles - Vertical: {}, Fabrica: {}, SN1: {}, SN2: {}", 
                 vertical, fabrica, sn1, sn2);

        List<TopLevelMetricsDTO> mejoresNiveles = new ArrayList<>();

        // Obtener todos los nucleus que aplican según filtros
        List<Nucleus> allNucleus = nucleusRepository.findAll();
        List<Nucleus> filteredNucleus = filterNucleus(allNucleus, vertical, fabrica, sn1, sn2);

        if (filteredNucleus.isEmpty()) {
            log.warn("No se encontraron datos con los filtros aplicados");
            return new TotalizadoresDTO(mejoresNiveles);
        }

        // Obtener todos los datos de productividad y velocidad
        List<Productividad> allProductividad = productividadRepository.findAll();
        List<Velocidad> allVelocidad = velocidadRepository.findAll();

        // Crear mapas para acceso rápido, filtrando nulls
        Map<Long, List<Productividad>> productividadByNucleus = allProductividad.stream()
                .filter(p -> p.getNucleus() != null && p.getNucleus().getId() != null)
                .collect(Collectors.groupingBy(p -> p.getNucleus().getId()));
        
        Map<Long, List<Velocidad>> velocidadByNucleus = allVelocidad.stream()
                .filter(v -> v.getNucleus() != null && v.getNucleus().getId() != null)
                .collect(Collectors.groupingBy(v -> v.getNucleus().getId()));

        // Determinar qué niveles calcular según los filtros
        if (sn1 != null && !sn1.isEmpty()) {
            // Si está filtrado por SN1, mostrar mejores SN2 por cada métrica
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN2", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN2));
            
        } else if (fabrica != null && !fabrica.isEmpty()) {
            // Si está filtrado por Fábrica, mostrar mejores SN1 y SN2 por cada métrica
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN1", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN1));
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN2", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN2));
            
        } else if (vertical != null && !vertical.isEmpty()) {
            // Si está filtrado por Vertical, mostrar mejores Fábrica, SN1 y SN2 por cada métrica
            mejoresNiveles.addAll(calcularMejoresPorMetrica("FABRICA", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getFabrica));
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN1", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN1));
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN2", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN2));
            
        } else {
            // Sin filtros: mostrar mejores de cada nivel por cada métrica
            mejoresNiveles.addAll(calcularMejoresVerticalPorMetrica(filteredNucleus, productividadByNucleus, velocidadByNucleus));
            mejoresNiveles.addAll(calcularMejoresPorMetrica("FABRICA", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getFabrica));
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN1", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN1));
            mejoresNiveles.addAll(calcularMejoresPorMetrica("SN2", filteredNucleus, productividadByNucleus, velocidadByNucleus, TotalizadoresService::getSN2));
        }

        log.info("Se calcularon {} mejores niveles", mejoresNiveles.size());
        return new TotalizadoresDTO(mejoresNiveles);
    }

    /**
     * Filtra los nucleus según los parámetros dados
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
     * Interfaz funcional para extraer valores de nivel de Nucleus
     */
    @FunctionalInterface
    private interface NivelExtractor {
        String extract(Nucleus n);
    }

    private static String getFabrica(Nucleus n) {
        return n.getOrgN2fabrica();
    }

    private static String getSN1(Nucleus n) {
        return n.getServiceN1();
    }

    private static String getSN2(Nucleus n) {
        return n.getServiceN2();
    }

    /**
     * Calcula los mejores niveles para cada métrica (Productividad, LT, CT)
     */
    private List<TopLevelMetricsDTO> calcularMejoresPorMetrica(
            String nivelTipo,
            List<Nucleus> nucleusList,
            Map<Long, List<Productividad>> productividadMap,
            Map<Long, List<Velocidad>> velocidadMap,
            NivelExtractor extractor) {

        List<TopLevelMetricsDTO> resultados = new ArrayList<>();

        // Agrupar por nivel
        Map<String, List<Nucleus>> byNivel = nucleusList.stream()
                .filter(n -> {
                    String valor = extractor.extract(n);
                    return valor != null && !valor.isEmpty();
                })
                .collect(Collectors.groupingBy(extractor::extract));

        if (byNivel.isEmpty()) {
            return resultados;
        }

        // Calcular mejor en Productividad
        TopLevelMetricsDTO mejorProductividad = byNivel.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = calcularProductividad(nucleus, productividadMap);
                    return new TopLevelMetricsDTO(nivelTipo, "PRODUCTIVIDAD", nombre, valor);
                })
                .filter(dto -> dto.getValor() != null && dto.getValor() > 0)
                .max(Comparator.comparing(TopLevelMetricsDTO::getValor))
                .orElse(null);

        if (mejorProductividad != null) {
            resultados.add(mejorProductividad);
        }

        // Calcular mejor en LT (menor es mejor)
        TopLevelMetricsDTO mejorLT = byNivel.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = calcularPromedioLT(nucleus, velocidadMap);
                    return new TopLevelMetricsDTO(nivelTipo, "LT", nombre, valor);
                })
                .filter(dto -> dto.getValor() != null && dto.getValor() > 0)
                .min(Comparator.comparing(TopLevelMetricsDTO::getValor))
                .orElse(null);

        if (mejorLT != null) {
            resultados.add(mejorLT);
        }

        // Calcular mejor en CT (menor es mejor)
        TopLevelMetricsDTO mejorCT = byNivel.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = calcularPromedioCT(nucleus, velocidadMap);
                    return new TopLevelMetricsDTO(nivelTipo, "CT", nombre, valor);
                })
                .filter(dto -> dto.getValor() != null && dto.getValor() > 0)
                .min(Comparator.comparing(TopLevelMetricsDTO::getValor))
                .orElse(null);

        if (mejorCT != null) {
            resultados.add(mejorCT);
        }

        return resultados;
    }

    /**
     * Calcula los mejores niveles de Vertical para cada métrica
     */
    private List<TopLevelMetricsDTO> calcularMejoresVerticalPorMetrica(
            List<Nucleus> nucleusList,
            Map<Long, List<Productividad>> productividadMap,
            Map<Long, List<Velocidad>> velocidadMap) {

        List<TopLevelMetricsDTO> resultados = new ArrayList<>();

        // Agrupar por vertical
        Map<String, List<Nucleus>> byVertical = new HashMap<>();
        for (Nucleus n : nucleusList) {
            for (VerticalNucleus vn : n.getVerticalNucleus()) {
                String verticalName = vn.getVertical().getName();
                byVertical.computeIfAbsent(verticalName, k -> new ArrayList<>()).add(n);
            }
        }

        if (byVertical.isEmpty()) {
            return resultados;
        }

        // Calcular mejor en Productividad
        TopLevelMetricsDTO mejorProductividad = byVertical.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = calcularProductividad(nucleus, productividadMap);
                    return new TopLevelMetricsDTO("VERTICAL", "PRODUCTIVIDAD", nombre, valor);
                })
                .filter(dto -> dto.getValor() != null && dto.getValor() > 0)
                .max(Comparator.comparing(TopLevelMetricsDTO::getValor))
                .orElse(null);

        if (mejorProductividad != null) {
            resultados.add(mejorProductividad);
        }

        // Calcular mejor en LT
        TopLevelMetricsDTO mejorLT = byVertical.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = calcularPromedioLT(nucleus, velocidadMap);
                    return new TopLevelMetricsDTO("VERTICAL", "LT", nombre, valor);
                })
                .filter(dto -> dto.getValor() != null && dto.getValor() > 0)
                .min(Comparator.comparing(TopLevelMetricsDTO::getValor))
                .orElse(null);

        if (mejorLT != null) {
            resultados.add(mejorLT);
        }

        // Calcular mejor en CT
        TopLevelMetricsDTO mejorCT = byVertical.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = calcularPromedioCT(nucleus, velocidadMap);
                    return new TopLevelMetricsDTO("VERTICAL", "CT", nombre, valor);
                })
                .filter(dto -> dto.getValor() != null && dto.getValor() > 0)
                .min(Comparator.comparing(TopLevelMetricsDTO::getValor))
                .orElse(null);

        if (mejorCT != null) {
            resultados.add(mejorCT);
        }

        return resultados;
    }

    /**
     * Calcula la productividad para un conjunto de nucleus
     */
    private Double calcularProductividad(List<Nucleus> nucleusList,
                                         Map<Long, List<Productividad>> productividadMap) {
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
        
        return totalFTEs > 0 ? totalFeatures / totalFTEs : null;
    }

    /**
     * Calcula el promedio de LT para un conjunto de nucleus
     */
    private Double calcularPromedioLT(List<Nucleus> nucleusList,
                                      Map<Long, List<Velocidad>> velocidadMap) {
        int total = 0;
        int count = 0;
        
        for (Nucleus n : nucleusList) {
            List<Velocidad> velList = velocidadMap.get(n.getId());
            if (velList != null) {
                for (Velocidad vel : velList) {
                    if (vel.getLt() != null) {
                        total += vel.getLt();
                        count++;
                    }
                }
            }
        }
        
        return count > 0 ? (double) total / count : null;
    }

    /**
     * Calcula el promedio de CT para un conjunto de nucleus
     */
    private Double calcularPromedioCT(List<Nucleus> nucleusList,
                                      Map<Long, List<Velocidad>> velocidadMap) {
        int total = 0;
        int count = 0;
        
        for (Nucleus n : nucleusList) {
            List<Velocidad> velList = velocidadMap.get(n.getId());
            if (velList != null) {
                for (Velocidad vel : velList) {
                    if (vel.getCt() != null) {
                        total += vel.getCt();
                        count++;
                    }
                }
            }
        }
        
        return count > 0 ? (double) total / count : null;
    }

    /**
     * Obtiene el ranking top 3 de un nivel organizacional específico para una métrica específica
     */
    public RankingDTO getRankingTop3(String nivelTipo, String metricaTipo, 
                                     String vertical, String fabrica, String sn1, String sn2) {
        log.info("Calculando ranking top 3 - Nivel: {}, Métrica: {}, Filtros - Vertical: {}, Fabrica: {}, SN1: {}, SN2: {}", 
                 nivelTipo, metricaTipo, vertical, fabrica, sn1, sn2);

        // Obtener y filtrar nucleus
        List<Nucleus> allNucleus = nucleusRepository.findAll();
        List<Nucleus> filteredNucleus = filterNucleus(allNucleus, vertical, fabrica, sn1, sn2);

        if (filteredNucleus.isEmpty()) {
            log.warn("No se encontraron datos con los filtros aplicados");
            return new RankingDTO(nivelTipo, metricaTipo, new ArrayList<>());
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

        List<RankingItemDTO> ranking = new ArrayList<>();

        // Determinar qué extractor usar según el nivel
        NivelExtractor extractor;
        Map<String, List<Nucleus>> byNivel;

        switch (nivelTipo.toUpperCase()) {
            case "VERTICAL":
                byNivel = new HashMap<>();
                for (Nucleus n : filteredNucleus) {
                    for (VerticalNucleus vn : n.getVerticalNucleus()) {
                        String verticalName = vn.getVertical().getName();
                        byNivel.computeIfAbsent(verticalName, k -> new ArrayList<>()).add(n);
                    }
                }
                break;
            case "FABRICA":
                extractor = TotalizadoresService::getFabrica;
                byNivel = filteredNucleus.stream()
                        .filter(n -> {
                            String valor = extractor.extract(n);
                            return valor != null && !valor.isEmpty();
                        })
                        .collect(Collectors.groupingBy(extractor::extract));
                break;
            case "SN1":
                extractor = TotalizadoresService::getSN1;
                byNivel = filteredNucleus.stream()
                        .filter(n -> {
                            String valor = extractor.extract(n);
                            return valor != null && !valor.isEmpty();
                        })
                        .collect(Collectors.groupingBy(extractor::extract));
                break;
            case "SN2":
                extractor = TotalizadoresService::getSN2;
                byNivel = filteredNucleus.stream()
                        .filter(n -> {
                            String valor = extractor.extract(n);
                            return valor != null && !valor.isEmpty();
                        })
                        .collect(Collectors.groupingBy(extractor::extract));
                break;
            default:
                log.warn("Nivel tipo desconocido: {}", nivelTipo);
                return new RankingDTO(nivelTipo, metricaTipo, ranking);
        }

        // Calcular valores según la métrica y ordenar
        List<RankingItemDTO> items = byNivel.entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey();
                    List<Nucleus> nucleus = entry.getValue();
                    Double valor = null;

                    switch (metricaTipo.toUpperCase()) {
                        case "PRODUCTIVIDAD":
                            valor = calcularProductividad(nucleus, productividadByNucleus);
                            break;
                        case "LT":
                            valor = calcularPromedioLT(nucleus, velocidadByNucleus);
                            break;
                        case "CT":
                            valor = calcularPromedioCT(nucleus, velocidadByNucleus);
                            break;
                        default:
                            log.warn("Métrica tipo desconocida: {}", metricaTipo);
                    }

                    return new RankingItemDTO(0, nombre, valor);
                })
                .filter(item -> item.getValor() != null && item.getValor() > 0)
                .collect(Collectors.toList());

        // Ordenar según la métrica (Productividad: mayor es mejor, LT/CT: menor es mejor)
        if ("PRODUCTIVIDAD".equalsIgnoreCase(metricaTipo)) {
            items.sort(Comparator.comparing(RankingItemDTO::getValor).reversed());
        } else {
            items.sort(Comparator.comparing(RankingItemDTO::getValor));
        }

        // Tomar top 3 y asignar posiciones
        for (int i = 0; i < Math.min(3, items.size()); i++) {
            items.get(i).setPosicion(i + 1);
            ranking.add(items.get(i));
        }

        log.info("Ranking top 3 calculado con {} items", ranking.size());
        return new RankingDTO(nivelTipo, metricaTipo, ranking);
    }
}
