// Datos del Dashboard procesados desde el CSV
import { VERTICAL_MAPPING, CERTIFICATION_LEVELS, DEFAULT_VERTICAL } from './constants';

export interface DashboardDataRow {
  period_month: string;
  ug_name: string;
  uol1_name: string;
  uol2_name: string;
  servicel1_id: string;
  servicel1_name: string;
  nivel_certificacion: string;
  vertical: string;
  fichas_rfo_status_ok: string;
  sn2_dependencias_asignadas: string;
  calidad_features: string;
  certificacion: string;
  operating_model: string;
  evolucion_vulnerabilidades: string;
  adopcion_total: string;
}

// Re-exportar constantes para mantener compatibilidad
export { VERTICAL_MAPPING } from './constants';

// Generar mapa plano desde la configuración centralizada
const serviceToVerticalMap: { [key: string]: string } = Object.entries(VERTICAL_MAPPING)
  .reduce((acc, [vertical, services]) => {
    services.forEach(service => {
      acc[service] = vertical;
    });
    return acc;
  }, {} as { [key: string]: string });

// Función para determinar la vertical basándose en el uol2_name
const getVerticalFromUOL2 = (uol2Name: string): string => {
  // Buscar coincidencia exacta primero
  if (serviceToVerticalMap[uol2Name]) {
    return serviceToVerticalMap[uol2Name];
  }
  
  // Buscar coincidencia parcial (case-insensitive)
  const uol2Upper = uol2Name.toUpperCase();
  for (const [service, vertical] of Object.entries(serviceToVerticalMap)) {
    if (uol2Upper.includes(service.toUpperCase()) || service.toUpperCase().includes(uol2Upper)) {
      return vertical;
    }
  }
  
  return DEFAULT_VERTICAL; // Valor por defecto si no se encuentra coincidencia
};

// Función para actualizar datos existentes con vertical
const addVerticalToData = (data: Omit<DashboardDataRow, 'vertical'>[]): DashboardDataRow[] => {
  return data.map(row => ({
    ...row,
    vertical: getVerticalFromUOL2(row.uol2_name)
  }));
};

// Función para parsear CSV
export const parseCSVLine = (line: string): string[] => {
  const result: string[] = [];
  let current = '';
  let inQuotes = false;
  
  for (let i = 0; i < line.length; i++) {
    const char = line[i];
    
    if (char === '"') {
      inQuotes = !inQuotes;
    } else if (char === ',' && !inQuotes) {
      result.push(current.trim());
      current = '';
    } else {
      current += char;
    }
  }
  
  result.push(current.trim());
  return result;
};

// Función para cargar datos del CSV
export const loadDashboardDataFromCSV = async (): Promise<DashboardDataRow[]> => {
  try {
    const response = await fetch('/data/Dashboard_Fuentus.csv');
    const csvText = await response.text();
    
    const lines = csvText.split('\n');
    const headers = parseCSVLine(lines[0]);
    
    // Encontrar los índices de las columnas que necesitamos
    const periodIndex = headers.findIndex(h => h.includes('period_month'));
    const ugNameIndex = headers.findIndex(h => h.includes('ug_name'));
    const uol1NameIndex = headers.findIndex(h => h.includes('uol1_name'));
    const uol2NameIndex = headers.findIndex(h => h.includes('uol2_name'));
    const servicel1IdIndex = headers.findIndex(h => h.includes('servicel1_id'));
    const servicel1NameIndex = headers.findIndex(h => h.includes('servicel1_name'));
    const nivelCertificacionIndex = headers.findIndex(h => h.includes('Nivel de Certificación'));
    const fichasRfoStatusOkIndex = headers.findIndex(h => h.includes('Fichas RFO con status OK'));
    const sn2DependenciasIndex = headers.findIndex(h => h.includes('SN2 con Dependencias asignadas'));
    const calidadFeaturesIndex = headers.findIndex(h => h.includes('Calidad de Features'));
    const certificacionIndex = headers.findIndex(h => h.includes('Está certificado?'));
    const operatingModelIndex = headers.findIndex(h => h.includes('Cumpliminto objetivo adopción del Op. Model'));
    const evolucionVulnerabilidadesIndex = headers.findIndex(h => h.includes('Evolución de vulnerabilidades'));
    const adopcionTotalIndex = headers.findIndex(h => h.includes('% Adopción total'));
    
    const data: DashboardDataRow[] = [];
    
    for (let i = 1; i < lines.length; i++) {
      if (lines[i].trim()) {
        const values = parseCSVLine(lines[i]);
        
        if (values.length >= headers.length) {
          const uol2Name = values[uol2NameIndex] || '';
          data.push({
            period_month: values[periodIndex] || '',
            ug_name: values[ugNameIndex] || '',
            uol1_name: values[uol1NameIndex] || '',
            uol2_name: uol2Name,
            servicel1_id: values[servicel1IdIndex] || '',
            servicel1_name: values[servicel1NameIndex] || '',
            nivel_certificacion: values[nivelCertificacionIndex] || 'Not certified',
            vertical: getVerticalFromUOL2(uol2Name),
            fichas_rfo_status_ok: values[fichasRfoStatusOkIndex] || '',
            sn2_dependencias_asignadas: values[sn2DependenciasIndex] || '',
            calidad_features: values[calidadFeaturesIndex] || '',
            certificacion: values[certificacionIndex] || '',
            operating_model: values[operatingModelIndex] || '',
            evolucion_vulnerabilidades: values[evolucionVulnerabilidadesIndex] || '',
            adopcion_total: values[adopcionTotalIndex] || ''
          });
        }
      }
    }
    
    return data;
  } catch (error) {
    console.error('Error loading CSV data:', error);
    return dashboardData; // Fallback a datos estáticos
  }
};

// Datos simulados basados en el CSV real (como fallback)
const createDataRow = (
  period: string, 
  ug: string, 
  uol1: string, 
  uol2: string, 
  serviceId: string, 
  serviceName: string, 
  certification: string,
  rfoStatus: string,
  dependencies: string,
  qualityFeatures: string,
  isCertified: string,
  operatingModel: string,
  vulnerabilities: string,
  adoption: string
): Omit<DashboardDataRow, 'vertical'> => ({
  period_month: period,
  ug_name: ug,
  uol1_name: uol1,
  uol2_name: uol2,
  servicel1_id: serviceId,
  servicel1_name: serviceName,
  nivel_certificacion: certification,
  fichas_rfo_status_ok: rfoStatus,
  sn2_dependencias_asignadas: dependencies,
  calidad_features: qualityFeatures,
  certificacion: isCertified,
  operating_model: operatingModel,
  evolucion_vulnerabilidades: vulnerabilities,
  adopcion_total: adoption
});

const staticDashboardData = [
  // Marzo 2025 - Argentina
  createDataRow("mar 25", "ARGENTINA", "INGENIERÍA & DATA", "ALPHA ARCHITECTURE", "3572", "ALPHA", "Level 3", "100,00%", "100,00%", "98,77%", "1", "1", "100,00%", "100%"),
  createDataRow("mar 25", "ARGENTINA", "INGENIERÍA & DATA", "DATAHUB ARGENTINA", "4343", "DATIO-AR RIESGOS Y FINANZAS", "Not certified", "100,00%", "100,00%", "80,00%", "0", "0", "100,00%", "69%"),
  createDataRow("mar 25", "ARGENTINA", "SYSTEMS ENGINEERING", "ADQUIRENCIA", "416", "PA02 MERCHANTS-ACQUIRING", "Level 1", "85,00%", "90,00%", "95,00%", "1", "1", "85,00%", "88%"),
  
  // Abril 2025 - Argentina
  createDataRow("abr 25", "ARGENTINA", "SYSTEMS ENGINEERING", "MEDIOS DE PAGO", "417", "PA03 PAYMENT METHODS", "Level 2", "92,00%", "88,00%", "91,00%", "1", "1", "90,00%", "90%"),
  createDataRow("abr 25", "ARGENTINA", "SYSTEMS ENGINEERING", "VENTAS", "418", "PA04 SALES MANAGEMENT", "Level 1", "78,00%", "82,00%", "85,00%", "1", "0", "75,00%", "80%"),
  
  // Mayo 2025 - Argentina
  createDataRow("may 25", "ARGENTINA", "SYSTEMS ENGINEERING", "CANALES EMPRESAS", "419", "PA05 ENTERPRISE CHANNELS", "Level 3", "95,00%", "93,00%", "96,00%", "1", "1", "95,00%", "94%"),
  createDataRow("may 25", "ARGENTINA", "SYSTEMS ENGINEERING", "PAYMENTS", "420", "PA06 DIGITAL PAYMENTS", "Level 2", "89,00%", "85,00%", "88,00%", "1", "1", "87,00%", "87%"),
  
  // Otros países con datos condensados
  createDataRow("mar 25", "MÉXICO", "SYSTEMS ENGINEERING", "CORE PRODUCTS", "421", "MX01 CORE BANKING", "Level 1", "83,00%", "80,00%", "82,00%", "1", "0", "81,00%", "81%"),
  createDataRow("mar 25", "MÉXICO", "INGENIERÍA & DATA", "DATAHUB MÉXICO", "422", "MX02 DATA ANALYTICS", "Not certified", "65,00%", "70,00%", "68,00%", "0", "0", "60,00%", "66%"),
  createDataRow("abr 25", "COLOMBIA", "SYSTEMS ENGINEERING", "MERCADOS Y CUSTODIA", "423", "CO01 MARKET CUSTODY", "Level 2", "91,00%", "89,00%", "90,00%", "1", "1", "88,00%", "89%"),
  createDataRow("abr 25", "COLOMBIA", "SYSTEMS ENGINEERING", "EXPERIENCIA AL CLIENTE", "424", "CO02 CUSTOMER EXPERIENCE", "Level 1", "77,00%", "79,00%", "81,00%", "1", "0", "76,00%", "78%"),
  createDataRow("may 25", "ESPAÑA", "SYSTEMS ENGINEERING", "TRANSFORMACION", "425", "ES01 DIGITAL TRANSFORMATION", "Level 3", "97,00%", "96,00%", "98,00%", "1", "1", "97,00%", "97%"),
  createDataRow("may 25", "ESPAÑA", "INGENIERÍA & DATA", "STAFFING Y METODOLOGIA", "426", "ES02 METHODOLOGY", "Not certified", "72,00%", "75,00%", "73,00%", "0", "0", "70,00%", "72%")
];

export const dashboardData: DashboardDataRow[] = addVerticalToData(staticDashboardData);

// Utilidades de filtrado generalizadas
type FilterCriteria = {
  geography?: string;
  uol1Names?: string[];
  vertical?: string;
};

const applyBasicFilters = (data: DashboardDataRow[], criteria: FilterCriteria): DashboardDataRow[] => {
  return data.filter(row => {
    const geographyMatch = !criteria.geography || row.ug_name === criteria.geography;
    const uol1Match = !criteria.uol1Names || criteria.uol1Names.length === 0 || criteria.uol1Names.includes(row.uol1_name);
    const verticalMatch = !criteria.vertical || row.vertical === criteria.vertical;
    return geographyMatch && uol1Match && verticalMatch;
  });
};

export const getUniqueValues = (data: DashboardDataRow[], field: keyof DashboardDataRow): string[] => {
  return Array.from(new Set(data.map(row => row[field]))).sort((a, b) => a.localeCompare(b));
};

// Función para obtener UOL1 filtradas por geografía
export const getUOL1ByGeography = (data: DashboardDataRow[], geography?: string): string[] => {
  return getUniqueValues(applyBasicFilters(data, { geography }), 'uol1_name');
};

// Función para obtener UOL2 filtradas por geografía y UOL1
export const getUOL2ByGeographyAndUOL1 = (
  data: DashboardDataRow[], 
  geography?: string, 
  uol1Names?: string[]
): string[] => {
  return getUniqueValues(applyBasicFilters(data, { geography, uol1Names }), 'uol2_name');
};

// Función para obtener UOL2 filtradas por vertical
export const getUOL2ByVertical = (data: DashboardDataRow[], vertical?: string): string[] => {
  return getUniqueValues(applyBasicFilters(data, { vertical }), 'uol2_name');
};

// Función para obtener UOL1 filtradas por vertical
export const getUOL1ByVertical = (data: DashboardDataRow[], vertical?: string): string[] => {
  return getUniqueValues(applyBasicFilters(data, { vertical }), 'uol1_name');
};

// Función para obtener UOL1 filtradas por geografía y vertical
export const getUOL1ByGeographyAndVertical = (
  data: DashboardDataRow[], 
  geography?: string, 
  vertical?: string
): string[] => {
  return getUniqueValues(applyBasicFilters(data, { geography, vertical }), 'uol1_name');
};

// Función para obtener UOL2 filtradas por geografía, UOL1 y vertical
export const getUOL2ByGeographyUOL1AndVertical = (
  data: DashboardDataRow[], 
  geography?: string, 
  uol1Names?: string[],
  vertical?: string
): string[] => {
  return getUniqueValues(applyBasicFilters(data, { geography, uol1Names, vertical }), 'uol2_name');
};

export const filterData = (
  data: DashboardDataRow[], 
  period?: string, 
  geography?: string, 
  uol2Names?: string[],
  vertical?: string
): DashboardDataRow[] => {
  return data.filter(row => {
    const periodMatch = !period || row.period_month === period;
    const geographyMatch = !geography || row.ug_name === geography;
    const uol2Match = !uol2Names || uol2Names.length === 0 || uol2Names.includes(row.uol2_name);
    const verticalMatch = !vertical || row.vertical === vertical;
    return periodMatch && geographyMatch && uol2Match && verticalMatch;
  });
};

export const getCertificationCounts = (data: DashboardDataRow[]): { [key: string]: number } => {
  const counts: { [key: string]: number } = {};
  
  data.forEach(row => {
    const cert = row.nivel_certificacion;
    counts[cert] = (counts[cert] || 0) + 1;
  });
  
  // Usar orden definido en constantes
  const orderedCounts: { [key: string]: number } = {};
  CERTIFICATION_LEVELS.forEach(level => {
    if (counts[level] > 0) {
      orderedCounts[level] = counts[level];
    }
  });
  
  return orderedCounts;
};

// Función para obtener todas las verticales únicas
export const getUniqueVerticals = (data: DashboardDataRow[]): string[] => {
  return getUniqueValues(data, 'vertical');
};

// Función para filtrar datos por vertical (reutiliza applyBasicFilters)
export const filterDataByVertical = (data: DashboardDataRow[], vertical?: string): DashboardDataRow[] => {
  return applyBasicFilters(data, { vertical });
};

// Función para obtener estadísticas por vertical
export const getVerticalStats = (data: DashboardDataRow[]): { [vertical: string]: { total: number, certificationCounts: { [cert: string]: number } } } => {
  const stats: { [vertical: string]: { total: number, certificationCounts: { [cert: string]: number } } } = {};
  
  data.forEach(row => {
    const vertical = row.vertical;
    const cert = row.nivel_certificacion;
    
    if (!stats[vertical]) {
      stats[vertical] = { 
        total: 0, 
        certificationCounts: {} 
      };
    }
    
    stats[vertical].total++;
    stats[vertical].certificationCounts[cert] = (stats[vertical].certificationCounts[cert] || 0) + 1;
  });
  
  return stats;
};

// Función para verificar si un UOL2 pertenece a una vertical específica
export const isUOL2InVertical = (uol2Name: string, vertical: string): boolean => {
  const services = VERTICAL_MAPPING[vertical as keyof typeof VERTICAL_MAPPING];
  if (!services) return false;
  
  const uol2Upper = uol2Name.toUpperCase();
  return services.some(service => 
    uol2Upper.includes(service.toUpperCase()) || service.toUpperCase().includes(uol2Upper)
  );
};

// Funciones para manejar datos de cobertura
export interface CoverageData {
  serviceName: string;
  serviceId: string;
  uol2Name: string;
  coverage: number;
  qualityFeatures: string;
  rfoStatus: string;
  dependencies: string;
  period: string;
  geography: string;
  vertical: string;
}

// Función para transformar datos del dashboard a datos de cobertura
export const transformToCoverageData = (data: DashboardDataRow[]): CoverageData[] => {
  return data.map(row => ({
    serviceName: row.servicel1_name,
    serviceId: row.servicel1_id,
    uol2Name: row.uol2_name,
    coverage: parseFloat(row.calidad_features.replace(/%/g, '').replace(',', '.')) || 0,
    qualityFeatures: row.calidad_features,
    rfoStatus: row.fichas_rfo_status_ok,
    dependencies: row.sn2_dependencias_asignadas,
    period: row.period_month,
    geography: row.ug_name,
    vertical: row.vertical
  }));
};

// Función para obtener datos de cobertura filtrados
export const getCoverageData = (
  data: DashboardDataRow[],
  filters?: {
    period?: string;
    geography?: string;
    uol1Names?: string[];
    uol2Names?: string[];
    vertical?: string;
    minCoverage?: number;
    maxCoverage?: number;
  }
): CoverageData[] => {
  let filteredData = data;
  
  if (filters) {
    filteredData = filterData(
      data,
      filters.period,
      filters.geography,
      filters.uol2Names,
      filters.vertical
    );
  }
  
  const coverageData = transformToCoverageData(filteredData);
  
  // Aplicar filtros de cobertura si se proporcionan
  if (filters?.minCoverage !== undefined || filters?.maxCoverage !== undefined) {
    return coverageData.filter(item => {
      const coverage = item.coverage;
      const minMatch = filters.minCoverage === undefined || coverage >= filters.minCoverage;
      const maxMatch = filters.maxCoverage === undefined || coverage <= filters.maxCoverage;
      return minMatch && maxMatch;
    });
  }
  
  return coverageData;
};

// Función para obtener estadísticas de cobertura
export const getCoverageStats = (data: CoverageData[]): {
  total: number;
  averageCoverage: number;
  highCoverage: number; // >= 80%
  mediumCoverage: number; // 60-79%
  lowCoverage: number; // < 60%
  coverageRanges: { [range: string]: number };
} => {
  const total = data.length;
  
  if (total === 0) {
    return {
      total: 0,
      averageCoverage: 0,
      highCoverage: 0,
      mediumCoverage: 0,
      lowCoverage: 0,
      coverageRanges: {}
    };
  }
  
  const averageCoverage = data.reduce((sum, item) => sum + item.coverage, 0) / total;
  
  let highCoverage = 0;
  let mediumCoverage = 0;
  let lowCoverage = 0;
  
  const coverageRanges: { [range: string]: number } = {
    '90-100%': 0,
    '80-89%': 0,
    '70-79%': 0,
    '60-69%': 0,
    '50-59%': 0,
    '<50%': 0
  };
  
  data.forEach(item => {
    const coverage = item.coverage;
    
    if (coverage >= 80) {
      highCoverage++;
    } else if (coverage >= 60) {
      mediumCoverage++;
    } else {
      lowCoverage++;
    }
    
    // Clasificar en rangos
    if (coverage >= 90) {
      coverageRanges['90-100%']++;
    } else if (coverage >= 80) {
      coverageRanges['80-89%']++;
    } else if (coverage >= 70) {
      coverageRanges['70-79%']++;
    } else if (coverage >= 60) {
      coverageRanges['60-69%']++;
    } else if (coverage >= 50) {
      coverageRanges['50-59%']++;
    } else {
      coverageRanges['<50%']++;
    }
  });
  
  return {
    total,
    averageCoverage: Math.round(averageCoverage * 100) / 100,
    highCoverage,
    mediumCoverage,
    lowCoverage,
    coverageRanges
  };
};

// Función para obtener cobertura promedio por UOL2
export const getCoverageByUOL2 = (data: DashboardDataRow[]): { [uol2: string]: number } => {
  const uol2Coverage: { [uol2: string]: { sum: number; count: number } } = {};
  
  data.forEach(row => {
    const uol2 = row.uol2_name;
    const coverage = parseFloat(row.calidad_features.replace(/%/g, '').replace(',', '.')) || 0;
    
    if (!uol2Coverage[uol2]) {
      uol2Coverage[uol2] = { sum: 0, count: 0 };
    }
    
    uol2Coverage[uol2].sum += coverage;
    uol2Coverage[uol2].count++;
  });
  
  const result: { [uol2: string]: number } = {};
  Object.keys(uol2Coverage).forEach(uol2 => {
    result[uol2] = Math.round((uol2Coverage[uol2].sum / uol2Coverage[uol2].count) * 100) / 100;
  });
  
  return result;
};

// Función para obtener tendencia de cobertura por período
export const getCoverageTrend = (data: DashboardDataRow[], uol2Name?: string): { [period: string]: number } => {
  let filteredData = data;
  
  if (uol2Name) {
    filteredData = data.filter(row => row.uol2_name === uol2Name);
  }
  
  const periodCoverage: { [period: string]: { sum: number; count: number } } = {};
  
  filteredData.forEach(row => {
    const period = row.period_month;
    const coverage = parseFloat(row.calidad_features.replace(/%/g, '').replace(',', '.')) || 0;
    
    if (!periodCoverage[period]) {
      periodCoverage[period] = { sum: 0, count: 0 };
    }
    
    periodCoverage[period].sum += coverage;
    periodCoverage[period].count++;
  });
  
  const result: { [period: string]: number } = {};
  Object.keys(periodCoverage).forEach(period => {
    result[period] = Math.round((periodCoverage[period].sum / periodCoverage[period].count) * 100) / 100;
  });
  
  return result;
};

