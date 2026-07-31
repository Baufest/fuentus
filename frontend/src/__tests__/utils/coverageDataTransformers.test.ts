import {
  transformCoverageToDonutChart,
  transformCoverageToBarChart,
  getCoverageCategory,
  getCoverageCategoryColor,
  getCoverageTableData,
  getCoverageTrendData,
  getLowCoverageServices,
  getCoverageExecutiveSummary,
  COVERAGE_COLORS
} from '../../utils/coverageDataTransformers';
import { DashboardDataRow } from '../../data/dashboardData';

// Mock data for testing
const mockDashboardData: DashboardDataRow[] = [
  {
    period_month: '2024-01',
    ug_name: 'UG Banking',
    uol1_name: 'UOL1 Banking',
    uol2_name: 'UOL2-Banking',
    servicel1_id: 'SVC001',
    servicel1_name: 'Service A',
    nivel_certificacion: 'Level 2',
    vertical: 'Banking',
    fichas_rfo_status_ok: 'Active',
    sn2_dependencias_asignadas: 'DB, API',
    calidad_features: '85.5%',
    certificacion: 'Certified',
    operating_model: 'Standard',
    evolucion_vulnerabilidades: 'Low',
    adopcion_total: '90%'
  },
  {
    period_month: '2024-01',
    ug_name: 'UG Retail',
    uol1_name: 'UOL1 Retail',
    uol2_name: 'UOL2-Retail',
    servicel1_id: 'SVC002',
    servicel1_name: 'Service B',
    nivel_certificacion: 'Level 3',
    vertical: 'Retail',
    fichas_rfo_status_ok: 'Active',
    sn2_dependencias_asignadas: 'Cache, Queue',
    calidad_features: '95.2%',
    certificacion: 'Certified',
    operating_model: 'Advanced',
    evolucion_vulnerabilidades: 'Very Low',
    adopcion_total: '95%'
  },
  {
    period_month: '2024-01',
    ug_name: 'UG Banking',
    uol1_name: 'UOL1 Banking',
    uol2_name: 'UOL2-Banking',
    servicel1_id: 'SVC003',
    servicel1_name: 'Service C',
    nivel_certificacion: 'Level 1',
    vertical: 'Banking',
    fichas_rfo_status_ok: 'Inactive',
    sn2_dependencias_asignadas: 'Legacy System',
    calidad_features: '45.8%',
    certificacion: 'Not Certified',
    operating_model: 'Legacy',
    evolucion_vulnerabilidades: 'High',
    adopcion_total: '30%'
  },
  {
    period_month: '2024-02',
    ug_name: 'UG Insurance',
    uol1_name: 'UOL1 Insurance',
    uol2_name: 'UOL2-Insurance',
    servicel1_id: 'SVC004',
    servicel1_name: 'Service D',
    nivel_certificacion: 'Level 2',
    vertical: 'Insurance',
    fichas_rfo_status_ok: 'Active',
    sn2_dependencias_asignadas: 'External API',
    calidad_features: '72.3%',
    certificacion: 'Certified',
    operating_model: 'Standard',
    evolucion_vulnerabilidades: 'Medium',
    adopcion_total: '75%'
  }
];

describe('coverageDataTransformers', () => {
  describe('transformCoverageToDonutChart', () => {
    test('should transform data correctly for donut chart', () => {
      const result = transformCoverageToDonutChart(mockDashboardData);
      
      expect(result).toHaveProperty('labels');
      expect(result).toHaveProperty('series');
      expect(result).toHaveProperty('colors');
      expect(result).toHaveProperty('totalServices');
      expect(result).toHaveProperty('averageCoverage');
      expect(result.labels).toBeInstanceOf(Array);
      expect(result.series).toBeInstanceOf(Array);
      expect(result.colors).toBeInstanceOf(Array);
    });

    test('should return empty data for empty input', () => {
      const result = transformCoverageToDonutChart([]);
      
      expect(result.labels).toEqual([]);
      expect(result.series).toEqual([]);
      expect(result.colors).toEqual([]);
      expect(result.totalServices).toBe(0);
      expect(result.averageCoverage).toBe(0);
    });

    test('should handle null or undefined input', () => {
      const result1 = transformCoverageToDonutChart(null as any);
      const result2 = transformCoverageToDonutChart(undefined as any);
      
      expect(result1.totalServices).toBe(0);
      expect(result2.totalServices).toBe(0);
    });
  });

  describe('transformCoverageToBarChart', () => {
    test('should transform data correctly for bar chart', () => {
      const result = transformCoverageToBarChart(mockDashboardData, 5);
      
      expect(result).toHaveProperty('categories');
      expect(result).toHaveProperty('series');
      expect(result.categories).toBeInstanceOf(Array);
      expect(result.series).toBeInstanceOf(Array);
      expect(result.series.length).toBe(1);
      expect(result.series[0]).toHaveProperty('name');
      expect(result.series[0]).toHaveProperty('data');
    });

    test('should limit results to topN parameter', () => {
      const result = transformCoverageToBarChart(mockDashboardData, 2);
      
      expect(result.categories.length).toBeLessThanOrEqual(2);
      expect(result.series[0].data.length).toBeLessThanOrEqual(2);
    });
  });

  describe('getCoverageCategory', () => {
    test('should categorize coverage correctly', () => {
      expect(getCoverageCategory(95)).toBe('Excelente');
      expect(getCoverageCategory(85)).toBe('Buena');
      expect(getCoverageCategory(75)).toBe('Regular');
      expect(getCoverageCategory(65)).toBe('Baja');
      expect(getCoverageCategory(45)).toBe('Crítica');
    });

    test('should handle edge cases', () => {
      expect(getCoverageCategory(90)).toBe('Excelente');
      expect(getCoverageCategory(80)).toBe('Buena');
      expect(getCoverageCategory(70)).toBe('Regular');
      expect(getCoverageCategory(60)).toBe('Baja');
      expect(getCoverageCategory(0)).toBe('Crítica');
      expect(getCoverageCategory(100)).toBe('Excelente');
    });
  });

  describe('getCoverageCategoryColor', () => {
    test('should return correct colors for coverage ranges', () => {
      expect(getCoverageCategoryColor(95)).toBe(COVERAGE_COLORS.EXCELLENT);
      expect(getCoverageCategoryColor(85)).toBe(COVERAGE_COLORS.GOOD);
      expect(getCoverageCategoryColor(75)).toBe(COVERAGE_COLORS.FAIR);
      expect(getCoverageCategoryColor(65)).toBe(COVERAGE_COLORS.POOR);
      expect(getCoverageCategoryColor(45)).toBe(COVERAGE_COLORS.CRITICAL);
    });
  });

  describe('getCoverageTableData', () => {
    test('should return table data with coverage categories', () => {
      const result = getCoverageTableData(mockDashboardData);
      
      expect(result).toBeInstanceOf(Array);
      result.forEach(item => {
        expect(item).toHaveProperty('serviceName');
        expect(item).toHaveProperty('serviceId');
        expect(item).toHaveProperty('uol2Name');
        expect(item).toHaveProperty('coverage');
        expect(item).toHaveProperty('coverageCategory');
        expect(item).toHaveProperty('rfoStatus');
        expect(item).toHaveProperty('dependencies');
        expect(item).toHaveProperty('period');
        expect(item).toHaveProperty('geography');
        expect(item).toHaveProperty('vertical');
      });
    });

    test('should apply filters correctly', () => {
      const filters = {
        uol2Names: ['UOL2-Banking'],
        minCoverage: 50,
        maxCoverage: 90
      };
      
      const result = getCoverageTableData(mockDashboardData, filters);
      
      // All results should match the filters
      result.forEach(item => {
        expect(filters.uol2Names).toContain(item.uol2Name);
        expect(item.coverage).toBeGreaterThanOrEqual(filters.minCoverage);
        expect(item.coverage).toBeLessThanOrEqual(filters.maxCoverage);
      });
    });
  });

  describe('getCoverageTrendData', () => {
    test('should return trend data for all services', () => {
      const result = getCoverageTrendData(mockDashboardData);
      
      expect(result).toHaveProperty('categories');
      expect(result).toHaveProperty('series');
      expect(result.categories).toBeInstanceOf(Array);
      expect(result.series).toBeInstanceOf(Array);
      expect(result.series.length).toBe(1);
      expect(result.series[0]).toHaveProperty('name');
      expect(result.series[0]).toHaveProperty('data');
    });

    test('should filter by UOL2 when specified', () => {
      const result = getCoverageTrendData(mockDashboardData, 'UOL2-Banking');
      
      expect(result.series[0].name).toContain('UOL2-Banking');
    });

    test('should remove all percentage symbols before calculating trend data', () => {
      const result = getCoverageTrendData([
        { ...mockDashboardData[0], period_month: '2024-01', calidad_features: '85.5%%' },
        { ...mockDashboardData[2], period_month: '2024-01', calidad_features: '45.5%%' }
      ], 'UOL2-Banking');

      expect(result.categories).toEqual(['2024-01']);
      expect(result.series[0].data).toEqual([65.5]);
    });
  });

  describe('getLowCoverageServices', () => {
    test('should return services below threshold', () => {
      const result = getLowCoverageServices(mockDashboardData, 70);
      
      result.forEach(service => {
        expect(service.coverage).toBeLessThan(70);
      });
    });

    test('should sort by coverage ascending', () => {
      const result = getLowCoverageServices(mockDashboardData, 80);
      
      for (let i = 1; i < result.length; i++) {
        expect(result[i].coverage).toBeGreaterThanOrEqual(result[i - 1].coverage);
      }
    });

    test('should use default threshold of 60', () => {
      const result = getLowCoverageServices(mockDashboardData);
      
      result.forEach(service => {
        expect(service.coverage).toBeLessThan(60);
      });
    });
  });

  describe('getCoverageExecutiveSummary', () => {
    test('should return comprehensive executive summary', () => {
      const result = getCoverageExecutiveSummary(mockDashboardData);
      
      expect(result).toHaveProperty('totalServices');
      expect(result).toHaveProperty('averageCoverage');
      expect(result).toHaveProperty('highCoverageCount');
      expect(result).toHaveProperty('lowCoverageCount');
      expect(result).toHaveProperty('topPerformingUOL2');
      expect(result).toHaveProperty('improvementNeeded');
      expect(result).toHaveProperty('coverageDistribution');
      
      expect(typeof result.totalServices).toBe('number');
      expect(typeof result.averageCoverage).toBe('number');
      expect(Array.isArray(result.topPerformingUOL2)).toBe(true);
      expect(Array.isArray(result.improvementNeeded)).toBe(true);
      expect(typeof result.coverageDistribution).toBe('object');
    });

    test('should have valid coverage distribution categories', () => {
      const result = getCoverageExecutiveSummary(mockDashboardData);
      
      const expectedCategories = [
        'Excelente (≥90%)',
        'Buena (80-89%)',
        'Regular (70-79%)',
        'Baja (60-69%)',
        'Crítica (<60%)'
      ];
      
      expectedCategories.forEach(category => {
        expect(result.coverageDistribution).toHaveProperty(category);
        expect(typeof result.coverageDistribution[category]).toBe('number');
      });
    });
  });

  describe('COVERAGE_COLORS', () => {
    test('should have all required color constants', () => {
      const expectedColors = [
        'HIGH', 'MEDIUM', 'LOW', 'EXCELLENT', 'GOOD', 'FAIR', 'POOR', 'CRITICAL'
      ];
      
      expectedColors.forEach(color => {
        expect(COVERAGE_COLORS).toHaveProperty(color);
        expect(typeof COVERAGE_COLORS[color as keyof typeof COVERAGE_COLORS]).toBe('string');
        expect(COVERAGE_COLORS[color as keyof typeof COVERAGE_COLORS]).toMatch(/^#[0-9A-Fa-f]{6}$/);
      });
    });
  });
});