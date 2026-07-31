import { 
  StatsSummaryDTO, 
  CoverageChartData, 
  ChimeraChartData, 
  ApplicationCoverageData, 
  ApplicationChimeraData
} from '../types/statsSummary';

/**
 * Transforma datos de StatsSummaryDTO[] a CoverageChartData[]
 * Agrupa por UUAA y calcula el coverage promedio de todas las aplicaciones
 */
export const transformToCoverageChartData = (data: StatsSummaryDTO[]): CoverageChartData[] => {
  const uuaaMap = new Map<string, CoverageChartData>();

  data.forEach(item => {
    if (!uuaaMap.has(item.uuaa)) {
      uuaaMap.set(item.uuaa, {
        uuaa: item.uuaa,
        averageCoverage: 0,
        totalApps: 0,
        applications: []
      });
    }

    const uuaaData = uuaaMap.get(item.uuaa)!;

    // Procesar cada repositorio
    item.repositories.forEach(repo => {
      if (repo.sonarInfo && repo.sonarInfo.coverage !== null && repo.sonarInfo.coverage !== undefined) {
        const appData: ApplicationCoverageData = {
          name: repo.name,
          coverage: repo.sonarInfo.coverage,
          bitbucketUrl: repo.bitbucketUrl
        };

        uuaaData.applications.push(appData);
        uuaaData.totalApps++;
      }
    });

    // Calcular coverage promedio
    if (uuaaData.applications.length > 0) {
      uuaaData.averageCoverage = uuaaData.applications.reduce((sum, app) => sum + app.coverage, 0) / uuaaData.applications.length;
    }
  });

  return Array.from(uuaaMap.values()).filter(item => item.totalApps > 0);
};

/**
 * Transforma datos de StatsSummaryDTO[] a ChimeraChartData[]
 * Agrupa por UUAA y suma todas las vulnerabilidades SAST y SCA
 */
export const transformToChimeraChartData = (data: StatsSummaryDTO[]): ChimeraChartData[] => {
  const uuaaMap = new Map<string, ChimeraChartData>();

  data.forEach(item => {
    if (!uuaaMap.has(item.uuaa)) {
      uuaaMap.set(item.uuaa, {
        uuaa: item.uuaa,
        sastData: {
          totalLow: 0,
          totalMedium: 0,
          totalHigh: 0
        },
        scaData: {
          totalLow: 0,
          totalMedium: 0,
          totalHigh: 0,
          totalCritical: 0
        },
        totalApps: 0,
        applications: []
      });
    }

    const uuaaData = uuaaMap.get(item.uuaa)!;

    // Procesar cada repositorio
    item.repositories.forEach(repo => {
      if (repo.sonarInfo && (repo.sonarInfo.chimeraSast || repo.sonarInfo.chimeraSca)) {
        const appData: ApplicationChimeraData = {
          name: repo.name,
          bitbucketUrl: repo.bitbucketUrl,
          chimeraUrl: repo.sonarInfo.sonarUrl || '', // Usar sonarUrl como base para chimeraUrl
          chimeraSast: {
            totalLow: repo.sonarInfo.chimeraSast?.totalLow || 0,
            totalMedium: repo.sonarInfo.chimeraSast?.totalMedium || 0,
            totalHigh: repo.sonarInfo.chimeraSast?.totalHigh || 0
          },
          chimeraSca: {
            totalLow: repo.sonarInfo.chimeraSca?.totalLow || 0,
            totalMedium: repo.sonarInfo.chimeraSca?.totalMedium || 0,
            totalHigh: repo.sonarInfo.chimeraSca?.totalHigh || 0,
            totalCritical: repo.sonarInfo.chimeraSca?.totalCritical || 0
          }
        };

        uuaaData.applications.push(appData);
        uuaaData.totalApps++;

        // Sumar vulnerabilidades SAST
        if (repo.sonarInfo.chimeraSast) {
          uuaaData.sastData.totalLow += repo.sonarInfo.chimeraSast.totalLow || 0;
          uuaaData.sastData.totalMedium += repo.sonarInfo.chimeraSast.totalMedium || 0;
          uuaaData.sastData.totalHigh += repo.sonarInfo.chimeraSast.totalHigh || 0;
        }

        // Sumar vulnerabilidades SCA
        if (repo.sonarInfo.chimeraSca) {
          uuaaData.scaData.totalLow += repo.sonarInfo.chimeraSca.totalLow || 0;
          uuaaData.scaData.totalMedium += repo.sonarInfo.chimeraSca.totalMedium || 0;
          uuaaData.scaData.totalHigh += repo.sonarInfo.chimeraSca.totalHigh || 0;
          uuaaData.scaData.totalCritical += repo.sonarInfo.chimeraSca.totalCritical || 0;
        }
      }
    });
  });

  // Filtrar solo UUAAs que tienen datos de vulnerabilidades
  const filteredUuaas = Array.from(uuaaMap.values()).filter(item => {
    const hasSastData = item.sastData.totalLow > 0 || 
                       item.sastData.totalMedium > 0 || 
                       item.sastData.totalHigh > 0;
    
    const hasScaData = item.scaData.totalLow > 0 || 
                      item.scaData.totalMedium > 0 || 
                      item.scaData.totalHigh > 0 || 
                      item.scaData.totalCritical > 0;
    
    return hasSastData || hasScaData;
  });

  // Ordenar por total de issues (de mayor a menor)
  return filteredUuaas.sort((a, b) => {
    // Calcular total de issues para UUAA A
    const totalA = (a.sastData.totalLow + a.sastData.totalMedium + a.sastData.totalHigh) +
                   (a.scaData.totalLow + a.scaData.totalMedium + a.scaData.totalHigh + a.scaData.totalCritical);
    
    // Calcular total de issues para UUAA B  
    const totalB = (b.sastData.totalLow + b.sastData.totalMedium + b.sastData.totalHigh) +
                   (b.scaData.totalLow + b.scaData.totalMedium + b.scaData.totalHigh + b.scaData.totalCritical);
    
    // Ordenar de mayor a menor
    return totalB - totalA;
  }).map(uuaa => {
    // Ordenar aplicaciones dentro de cada UUAA por volumen total de issues
    uuaa.applications.sort((a, b) => {
      const totalA = ((a.chimeraSast.totalLow || 0) + (a.chimeraSast.totalMedium || 0) + (a.chimeraSast.totalHigh || 0)) +
                     ((a.chimeraSca.totalLow || 0) + (a.chimeraSca.totalMedium || 0) + (a.chimeraSca.totalHigh || 0) + (a.chimeraSca.totalCritical || 0));
      
      const totalB = ((b.chimeraSast.totalLow || 0) + (b.chimeraSast.totalMedium || 0) + (b.chimeraSast.totalHigh || 0)) +
                     ((b.chimeraSca.totalLow || 0) + (b.chimeraSca.totalMedium || 0) + (b.chimeraSca.totalHigh || 0) + (b.chimeraSca.totalCritical || 0));
      
      return totalB - totalA; // Mayor a menor
    });
    
    return uuaa;
  });
};
