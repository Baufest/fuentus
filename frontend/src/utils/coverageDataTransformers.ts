// Utilidades para transformar datos de cobertura para charts
import { DashboardDataRow, getCoverageData, getCoverageStats } from '../data/dashboardData';

export interface CoverageChartData {
  labels: string[];
  series: number[];
  colors: string[];
  totalServices: number;
  averageCoverage: number;
}

export interface CoverageBarData {
  categories: string[];
  series: Array<{
    name: string;
    data: number[];
  }>;
}

// Colores para diferentes rangos de cobertura
export const COVERAGE_COLORS = {
  HIGH: '#10B981', // Verde - Alta cobertura (>=80%)
  MEDIUM: '#F59E0B', // Amarillo - Cobertura media (60-79%)
  LOW: '#EF4444', // Rojo - Baja cobertura (<60%)
  EXCELLENT: '#059669', // Verde oscuro (>=90%)
  GOOD: '#34D399', // Verde claro (80-89%)
  FAIR: '#FBBF24', // Amarillo (70-79%)
  POOR: '#F87171', // Rojo claro (60-69%)
  CRITICAL: '#DC2626' // Rojo oscuro (<60%)
};

// Función para transformar datos de cobertura a formato de chart donut
export const transformCoverageToDonutChart = (data: DashboardDataRow[]): CoverageChartData => {
  // Verificar que tenemos datos válidos
  if (!data || data.length === 0) {
    return {
      labels: [],
      series: [],
      colors: [],
      totalServices: 0,
      averageCoverage: 0
    };
  }

  const coverageData = getCoverageData(data);
  const stats = getCoverageStats(coverageData);
  
  const labels: string[] = [];
  const series: number[] = [];
  const colors: string[] = [];
  
  // Verificar que stats y stats.coverageRanges existan antes de usarlo
  if (!stats || !stats.coverageRanges) {
    return {
      labels: [],
      series: [],
      colors: [],
      totalServices: 0,
      averageCoverage: 0
    };
  }
  
  // Agregar rangos que tengan datos
  Object.entries(stats.coverageRanges).forEach(([range, count]) => {
    if (count > 0) {
      labels.push(range);
      series.push(count);
      
      // Asignar colores según el rango
      if (range === '90-100%') {
        colors.push(COVERAGE_COLORS.EXCELLENT);
      } else if (range === '80-89%') {
        colors.push(COVERAGE_COLORS.GOOD);
      } else if (range === '70-79%') {
        colors.push(COVERAGE_COLORS.FAIR);
      } else if (range === '60-69%') {
        colors.push(COVERAGE_COLORS.POOR);
      } else {
        colors.push(COVERAGE_COLORS.CRITICAL);
      }
    }
  });
  
  return {
    labels,
    series,
    colors,
    totalServices: stats.total,
    averageCoverage: stats.averageCoverage
  };
};

// Función para transformar datos de cobertura por UOL2 a formato de chart de barras
export const transformCoverageToBarChart = (
  data: DashboardDataRow[],
  topN: number = 10
): CoverageBarData => {
  const coverageData = getCoverageData(data);
  
  // Agrupar por UOL2 y calcular promedio
  const uol2Coverage: { [uol2: string]: { sum: number; count: number; services: string[] } } = {};
  
  coverageData.forEach(item => {
    const uol2 = item.uol2Name;
    
    if (!uol2Coverage[uol2]) {
      uol2Coverage[uol2] = { sum: 0, count: 0, services: [] };
    }
    
    uol2Coverage[uol2].sum += item.coverage;
    uol2Coverage[uol2].count++;
    uol2Coverage[uol2].services.push(item.serviceName);
  });
  
  // Calcular promedios y ordenar
  const averages = Object.entries(uol2Coverage)
    .map(([uol2, data]) => ({
      uol2,
      average: Math.round((data.sum / data.count) * 100) / 100,
      serviceCount: data.count,
      services: [...new Set(data.services)] // Eliminar duplicados
    }))
    .sort((a, b) => b.average - a.average)
    .slice(0, topN);
  
  const categories = averages.map(item => item.uol2);
  const coverageValues = averages.map(item => item.average);
  
  return {
    categories,
    series: [{
      name: 'Cobertura Promedio (%)',
      data: coverageValues
    }]
  };
};

// Función para obtener datos de cobertura para tabla detallada
export const getCoverageTableData = (
  data: DashboardDataRow[],
  filters?: {
    uol2Names?: string[];
    minCoverage?: number;
    maxCoverage?: number;
  }
): Array<{
  serviceName: string;
  serviceId: string;
  uol2Name: string;
  coverage: number;
  coverageCategory: string;
  rfoStatus: string;
  dependencies: string;
  period: string;
  geography: string;
  vertical: string;
}> => {
  const coverageData = getCoverageData(data, filters);
  
  return coverageData.map(item => ({
    ...item,
    coverageCategory: getCoverageCategory(item.coverage)
  }));
};

// Función auxiliar para categorizar la cobertura
export const getCoverageCategory = (coverage: number): string => {
  if (coverage >= 90) return 'Excelente';
  if (coverage >= 80) return 'Buena';
  if (coverage >= 70) return 'Regular';
  if (coverage >= 60) return 'Baja';
  return 'Crítica';
};

// Función para obtener el color de la categoría de cobertura
export const getCoverageCategoryColor = (coverage: number): string => {
  if (coverage >= 90) return COVERAGE_COLORS.EXCELLENT;
  if (coverage >= 80) return COVERAGE_COLORS.GOOD;
  if (coverage >= 70) return COVERAGE_COLORS.FAIR;
  if (coverage >= 60) return COVERAGE_COLORS.POOR;
  return COVERAGE_COLORS.CRITICAL;
};

// Función para crear tooltip personalizado para charts de cobertura
export const createCoverageTooltip = (
  seriesIndex: number,
  w: any
): string => {
  const value = w.globals.series[seriesIndex];
  const label = w.globals.labels[seriesIndex];
  const percentage = ((value / w.globals.seriesTotals.reduce((a: number, b: number) => a + b, 0)) * 100).toFixed(1);
  
  return `
    <div class="px-3 py-2">
      <div class="font-semibold">${label}</div>
      <div class="text-sm">
        <div>Servicios: ${value}</div>
        <div>Porcentaje: ${percentage}%</div>
      </div>
    </div>
  `;
};

// Función para crear datos de tendencia de cobertura por período
export const getCoverageTrendData = (
  data: DashboardDataRow[],
  uol2Name?: string
): {
  categories: string[];
  series: Array<{
    name: string;
    data: number[];
  }>;
} => {
  let filteredData = data;
  
  if (uol2Name) {
    filteredData = data.filter(row => row.uol2_name === uol2Name);
  }
  
  // Agrupar por período
  const periodData: { [period: string]: { sum: number; count: number } } = {};
  
  filteredData.forEach(row => {
    const period = row.period_month;
    const coverage = parseFloat(row.calidad_features.replace(/%/g, '').replace(/,/g, '.')) || 0;
    
    if (!periodData[period]) {
      periodData[period] = { sum: 0, count: 0 };
    }
    
    periodData[period].sum += coverage;
    periodData[period].count++;
  });
  
  // Calcular promedios y ordenar por período
  const periods = Object.keys(periodData).sort((a, b) => a.localeCompare(b));
  const averages = periods.map(period => {
    const data = periodData[period];
    return Math.round((data.sum / data.count) * 100) / 100;
  });
  
  return {
    categories: periods,
    series: [{
      name: uol2Name ? `Cobertura ${uol2Name}` : 'Cobertura Promedio',
      data: averages
    }]
  };
};

// Función para obtener servicios con baja cobertura
export const getLowCoverageServices = (
  data: DashboardDataRow[],
  threshold: number = 60
): Array<{
  serviceName: string;
  serviceId: string;
  uol2Name: string;
  coverage: number;
  geography: string;
  vertical: string;
}> => {
  return getCoverageData(data)
    .filter(item => item.coverage < threshold)
    .sort((a, b) => a.coverage - b.coverage)
    .map(item => ({
      serviceName: item.serviceName,
      serviceId: item.serviceId,
      uol2Name: item.uol2Name,
      coverage: item.coverage,
      geography: item.geography,
      vertical: item.vertical
    }));
};

// Función para obtener resumen ejecutivo de cobertura
export const getCoverageExecutiveSummary = (data: DashboardDataRow[]): {
  totalServices: number;
  averageCoverage: number;
  highCoverageCount: number;
  lowCoverageCount: number;
  topPerformingUOL2: string[];
  improvementNeeded: string[];
  coverageDistribution: { [category: string]: number };
} => {
  const coverageData = getCoverageData(data);
  const stats = getCoverageStats(coverageData);
  
  // Obtener UOL2 con mejor y peor performance
  const uol2Performance: { [uol2: string]: number[] } = {};
  
  coverageData.forEach(item => {
    if (!uol2Performance[item.uol2Name]) {
      uol2Performance[item.uol2Name] = [];
    }
    uol2Performance[item.uol2Name].push(item.coverage);
  });
  
  const uol2Averages = Object.entries(uol2Performance)
    .map(([uol2, coverages]) => ({
      uol2,
      average: coverages.reduce((sum, cov) => sum + cov, 0) / coverages.length
    }))
    .sort((a, b) => b.average - a.average);
  
  const topPerformingUOL2 = uol2Averages
    .filter(item => item.average >= 80)
    .slice(0, 5)
    .map(item => item.uol2);
  
  const improvementNeeded = uol2Averages
    .filter(item => item.average < 60)
    .slice(-5)
    .map(item => item.uol2);
  
  return {
    totalServices: stats.total,
    averageCoverage: stats.averageCoverage,
    highCoverageCount: stats.highCoverage,
    lowCoverageCount: stats.lowCoverage,
    topPerformingUOL2,
    improvementNeeded,
    coverageDistribution: {
      'Excelente (≥90%)': stats.coverageRanges['90-100%'] || 0,
      'Buena (80-89%)': stats.coverageRanges['80-89%'] || 0,
      'Regular (70-79%)': stats.coverageRanges['70-79%'] || 0,
      'Baja (60-69%)': stats.coverageRanges['60-69%'] || 0,
      'Crítica (<60%)': (stats.coverageRanges['50-59%'] || 0) + (stats.coverageRanges['<50%'] || 0)
    }
  };
};
