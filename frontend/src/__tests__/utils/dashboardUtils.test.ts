import {
  sortPeriods,
  calculateCertificationStats,
  initializeDashboardData,
  getCertificationColors
} from '../../utils/dashboardUtils';
import { DashboardDataRow } from '../../data/dashboardData';

// Mock dashboardData module
jest.mock('../../data/dashboardData', () => ({
  getUniqueValues: jest.fn()
}));

import { getUniqueValues } from '../../data/dashboardData';

describe('dashboardUtils', () => {
  // Mock data used across multiple tests
  const mockData: DashboardDataRow[] = [
    {
      period_month: 'mar 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'SYSTEMS ENGINEERING',
      uol2_name: 'ADQUIRENCIA',
      servicel1_id: '416',
      servicel1_name: 'PA02 MERCHANTS-ACQUIRING',
      nivel_certificacion: 'Level 1',
      vertical: 'INDIVIDUOS Y PYMES',
      fichas_rfo_status_ok: '85,00%',
      sn2_dependencias_asignadas: '90,00%',
      calidad_features: '95,00%',
      certificacion: '1',
      operating_model: '1',
      evolucion_vulnerabilidades: '85,00%',
      adopcion_total: '88%'
    },
    {
      period_month: 'mar 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'INGENIERÍA & DATA',
      uol2_name: 'DATAHUB ARGENTINA',
      servicel1_id: '4343',
      servicel1_name: 'DATIO-AR RIESGOS Y FINANZAS',
      nivel_certificacion: 'Not certified',
      vertical: 'INGENIERÍA & DATA',
      fichas_rfo_status_ok: '100,00%',
      sn2_dependencias_asignadas: '100,00%',
      calidad_features: '80,00%',
      certificacion: '0',
      operating_model: '0',
      evolucion_vulnerabilidades: '100,00%',
      adopcion_total: '69%'
    },
    {
      period_month: 'mar 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'SYSTEMS ENGINEERING',
      uol2_name: 'MEDIOS DE PAGO',
      servicel1_id: '417',
      servicel1_name: 'PA03 PAYMENT METHODS',
      nivel_certificacion: 'Level 2',
      vertical: 'INDIVIDUOS Y PYMES',
      fichas_rfo_status_ok: '92,00%',
      sn2_dependencias_asignadas: '88,00%',
      calidad_features: '91,00%',
      certificacion: '1',
      operating_model: '1',
      evolucion_vulnerabilidades: '90,00%',
      adopcion_total: '90%'
    }
  ];

  describe('sortPeriods', () => {
    it('sorts periods in descending order (most recent first)', () => {
      const periods = ['ene 25', 'mar 25', 'feb 25', 'dic 24', 'nov 24'];
      const sorted = sortPeriods(periods);
      
      expect(sorted).toEqual(['mar 25', 'feb 25', 'ene 25', 'dic 24', 'nov 24']);
    });

    it('handles cross-year sorting correctly', () => {
      const periods = ['ene 26', 'dic 25', 'feb 25', 'nov 25'];
      const sorted = sortPeriods(periods);
      
      expect(sorted).toEqual(['ene 26', 'dic 25', 'nov 25', 'feb 25']);
    });

    it('handles invalid period formats gracefully', () => {
      const periods = ['invalid', 'mar 25', 'bad format', 'feb 25'];
      const sorted = sortPeriods(periods);
      
      // Valid periods should still be sorted correctly
      expect(sorted).toContain('mar 25');
      expect(sorted).toContain('feb 25');
      expect(sorted).toContain('invalid');
      expect(sorted).toContain('bad format');
    });

    it('handles empty array', () => {
      const sorted = sortPeriods([]);
      expect(sorted).toEqual([]);
    });

    it('handles single period', () => {
      const sorted = sortPeriods(['mar 25']);
      expect(sorted).toEqual(['mar 25']);
    });

    it('handles periods with unknown months', () => {
      const periods = ['xyz 25', 'mar 25', 'abc 25'];
      const sorted = sortPeriods(periods);
      
      // Should not crash and should include all periods
      expect(sorted).toHaveLength(3);
      expect(sorted).toContain('mar 25');
    });

    it('sorts same year periods correctly', () => {
      const periods = ['dic 25', 'ene 25', 'jun 25', 'mar 25'];
      const sorted = sortPeriods(periods);
      
      expect(sorted).toEqual(['dic 25', 'jun 25', 'mar 25', 'ene 25']);
    });
  });

  describe('calculateCertificationStats', () => {
    it('calculates certification stats correctly', () => {
      const result = calculateCertificationStats(mockData);
      
      expect(result.totalCases).toBe(3);
      expect(result.certifiedCases).toBe(2); // Level 1 + Level 2
      expect(result.certificationPercentage).toBe('66.7'); // (2/3) * 100
      
      expect(result.chartData.labels).toEqual(['Level 1', 'Not certified', 'Level 2']);
      expect(result.chartData.series).toEqual([1, 1, 1]);
    });

    it('handles empty data', () => {
      const result = calculateCertificationStats([]);
      
      expect(result.totalCases).toBe(0);
      expect(result.certifiedCases).toBe(0);
      expect(result.certificationPercentage).toBe('0.0');
      expect(result.chartData.labels).toEqual([]);
      expect(result.chartData.series).toEqual([]);
    });

    it('handles data with null certification levels', () => {
      const dataWithNulls: DashboardDataRow[] = [
        {
          ...mockData[0],
          nivel_certificacion: undefined as any
        },
        mockData[1]
      ];

      const result = calculateCertificationStats(dataWithNulls);
      
      expect(result.totalCases).toBe(2);
      expect(result.chartData.labels).toContain('No Certificado');
      expect(result.chartData.labels).toContain('Not certified');
    });

    it('calculates 100% certification correctly', () => {
      const allCertifiedData: DashboardDataRow[] = [
        { ...mockData[0], nivel_certificacion: 'Level 1' },
        { ...mockData[1], nivel_certificacion: 'Level 2' },
        { ...mockData[2], nivel_certificacion: 'Level 3' }
      ];

      const result = calculateCertificationStats(allCertifiedData);
      
      expect(result.certificationPercentage).toBe('100.0');
      expect(result.certifiedCases).toBe(3);
    });

    it('handles 0% certification correctly', () => {
      const noCertifiedData: DashboardDataRow[] = [
        { ...mockData[0], nivel_certificacion: 'Not certified' },
        { ...mockData[1], nivel_certificacion: 'Not certified' }
      ];

      const result = calculateCertificationStats(noCertifiedData);
      
      expect(result.certificationPercentage).toBe('0.0');
      expect(result.certifiedCases).toBe(0);
    });

    it('counts certification levels correctly', () => {
      const mixedData: DashboardDataRow[] = [
        { ...mockData[0], nivel_certificacion: 'Level 1' },
        { ...mockData[1], nivel_certificacion: 'Level 1' },
        { ...mockData[2], nivel_certificacion: 'Level 2' },
        { ...mockData[0], nivel_certificacion: 'Not certified' }
      ];

      const result = calculateCertificationStats(mixedData);
      
      expect(result.chartData.labels).toContain('Level 1');
      expect(result.chartData.labels).toContain('Level 2');
      expect(result.chartData.labels).toContain('Not certified');
      
      const level1Index = result.chartData.labels.indexOf('Level 1');
      const level2Index = result.chartData.labels.indexOf('Level 2');
      const notCertifiedIndex = result.chartData.labels.indexOf('Not certified');
      
      expect(result.chartData.series[level1Index]).toBe(2);
      expect(result.chartData.series[level2Index]).toBe(1);
      expect(result.chartData.series[notCertifiedIndex]).toBe(1);
    });
  });

  describe('initializeDashboardData', () => {
    const mockGetUniqueValues = getUniqueValues as jest.MockedFunction<typeof getUniqueValues>;

    beforeEach(() => {
      jest.clearAllMocks();
    });

    it('initializes dashboard data correctly', () => {
      const testData = [mockData[0]]; // Use testData here
      
      mockGetUniqueValues
        .mockReturnValueOnce(['mar 25', 'feb 25', 'ene 25']) // periods
        .mockReturnValueOnce(['ARGENTINA', 'MÉXICO', 'COLOMBIA']) // geographies
        .mockReturnValueOnce(['INDIVIDUOS Y PYMES', 'EMPRESAS']) // verticals
        .mockReturnValueOnce(['SYSTEMS ENGINEERING', 'INGENIERÍA & DATA']); // uol1_name

      const result = initializeDashboardData(testData);
      
      expect(result.periods).toEqual(['mar 25', 'feb 25', 'ene 25']);
      expect(result.geographies).toEqual(['ARGENTINA', 'MÉXICO', 'COLOMBIA']);
      expect(result.verticals).toEqual(['INDIVIDUOS Y PYMES', 'EMPRESAS']);
      expect(result.defaultPeriod).toBe('mar 25'); // Most recent
      expect(result.shouldPreselectSystemsEngineering).toBe(true);
    });

    it('handles empty periods gracefully', () => {
      mockGetUniqueValues
        .mockReturnValueOnce([]) // periods
        .mockReturnValueOnce(['ARGENTINA']) // geographies
        .mockReturnValueOnce(['INDIVIDUOS Y PYMES']) // verticals
        .mockReturnValueOnce(['SYSTEMS ENGINEERING']); // uol1_name

      const result = initializeDashboardData([]);
      
      expect(result.periods).toEqual([]);
      expect(result.defaultPeriod).toBe('');
    });

    it('sets shouldPreselectSystemsEngineering to false when not present', () => {
      const testData = [mockData[0]]; // Use testData here
      
      mockGetUniqueValues
        .mockReturnValueOnce(['mar 25']) // periods
        .mockReturnValueOnce(['ARGENTINA']) // geographies
        .mockReturnValueOnce(['INDIVIDUOS Y PYMES']) // verticals
        .mockReturnValueOnce(['INGENIERÍA & DATA']); // uol1_name (no SYSTEMS ENGINEERING)

      const result = initializeDashboardData(testData);
      
      expect(result.shouldPreselectSystemsEngineering).toBe(false);
    });

    it('sorts periods correctly in initialization', () => {
      const testData = [mockData[0]]; // Use testData here
      
      mockGetUniqueValues
        .mockReturnValueOnce(['ene 25', 'mar 25', 'feb 25']) // unsorted periods
        .mockReturnValueOnce(['ARGENTINA']) // geographies
        .mockReturnValueOnce(['INDIVIDUOS Y PYMES']) // verticals
        .mockReturnValueOnce(['SYSTEMS ENGINEERING']); // uol1_name

      const result = initializeDashboardData(testData);
      
      expect(result.periods).toEqual(['mar 25', 'feb 25', 'ene 25']); // Should be sorted
      expect(result.defaultPeriod).toBe('mar 25'); // Most recent
    });
  });

  describe('getCertificationColors', () => {
    it('returns correct number of colors', () => {
      expect(getCertificationColors(1)).toHaveLength(1);
      expect(getCertificationColors(2)).toHaveLength(2);
      expect(getCertificationColors(3)).toHaveLength(3);
      expect(getCertificationColors(4)).toHaveLength(4);
    });

    it('returns expected colors in order', () => {
      const colors = getCertificationColors(4);
      
      expect(colors).toEqual(['#8093b3', '#1973b8', '#004481', '#0f3669']);
    });

    it('handles zero count', () => {
      const colors = getCertificationColors(0);
      expect(colors).toEqual([]);
    });

    it('handles count greater than available colors', () => {
      const colors = getCertificationColors(6);
      expect(colors).toHaveLength(4); // Should return all available colors
      expect(colors).toEqual(['#8093b3', '#1973b8', '#004481', '#0f3669']);
    });

    it('returns consistent colors for same count', () => {
      const colors1 = getCertificationColors(3);
      const colors2 = getCertificationColors(3);
      
      expect(colors1).toEqual(colors2);
    });

    it('returns first N colors correctly', () => {
      const oneColor = getCertificationColors(1);
      const twoColors = getCertificationColors(2);
      const threeColors = getCertificationColors(3);
      
      expect(oneColor).toEqual(['#8093b3']);
      expect(twoColors).toEqual(['#8093b3', '#1973b8']);
      expect(threeColors).toEqual(['#8093b3', '#1973b8', '#004481']);
    });
  });
});
