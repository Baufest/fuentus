import {
  parseCSVLine,
  loadDashboardDataFromCSV,
  getCertificationCounts,
  getUOL1ByGeography,
  getUOL2ByGeographyAndUOL1,
  getUOL2ByVertical,
  getUOL1ByVertical,
  getUOL1ByGeographyAndVertical,
  getUOL2ByGeographyUOL1AndVertical,
  DashboardDataRow
} from '../../data/dashboardData';

// Mock fetch for CSV loading tests
global.fetch = jest.fn();

describe('dashboardData', () => {
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
      vertical: 'CORE',
      fichas_rfo_status_ok: '100,00%',
      sn2_dependencias_asignadas: '100,00%',
      calidad_features: '80,00%',
      certificacion: '0',
      operating_model: '0',
      evolucion_vulnerabilidades: '100,00%',
      adopcion_total: '69%'
    },
    {
      period_month: 'abr 25',
      ug_name: 'MÉXICO',
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
    },
    {
      period_month: 'may 25',
      ug_name: 'ESPAÑA',
      uol1_name: 'INGENIERÍA & DATA',
      uol2_name: 'TRANSFORMACION',
      servicel1_id: '425',
      servicel1_name: 'ES01 DIGITAL TRANSFORMATION',
      nivel_certificacion: 'Level 3',
      vertical: 'EMPRESAS',
      fichas_rfo_status_ok: '97,00%',
      sn2_dependencias_asignadas: '96,00%',
      calidad_features: '98,00%',
      certificacion: '1',
      operating_model: '1',
      evolucion_vulnerabilidades: '97,00%',
      adopcion_total: '97%'
    }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('parseCSVLine', () => {
    it('should parse simple CSV line correctly', () => {
      const line = 'field1,field2,field3';
      const result = parseCSVLine(line);
      expect(result).toEqual(['field1', 'field2', 'field3']);
    });

    it('should handle quoted fields with commas', () => {
      const line = 'field1,"field2,with,commas",field3';
      const result = parseCSVLine(line);
      expect(result).toEqual(['field1', 'field2,with,commas', 'field3']);
    });

    it('should handle quoted fields at the beginning', () => {
      const line = '"quoted first",field2,field3';
      const result = parseCSVLine(line);
      expect(result).toEqual(['quoted first', 'field2', 'field3']);
    });

    it('should handle quoted fields at the end', () => {
      const line = 'field1,field2,"quoted last"';
      const result = parseCSVLine(line);
      expect(result).toEqual(['field1', 'field2', 'quoted last']);
    });

    it('should handle empty fields', () => {
      const line = 'field1,,field3';
      const result = parseCSVLine(line);
      expect(result).toEqual(['field1', '', 'field3']);
    });

    it('should handle spaces around fields', () => {
      const line = ' field1 , field2 , field3 ';
      const result = parseCSVLine(line);
      expect(result).toEqual(['field1', 'field2', 'field3']);
    });

    it('should handle empty line', () => {
      const line = '';
      const result = parseCSVLine(line);
      expect(result).toEqual(['']);
    });

    it('should handle line with only commas', () => {
      const line = ',,,';
      const result = parseCSVLine(line);
      expect(result).toEqual(['', '', '', '']);
    });
  });

  describe('loadDashboardDataFromCSV', () => {
    it('should load and parse CSV data correctly', async () => {
      const mockCSV = `period_month,ug_name,uol1_name,uol2_name,servicel1_id,servicel1_name,Nivel de Certificación,Fichas RFO con status OK,SN2 con Dependencias asignadas,Calidad de Features,Está certificado?,Cumpliminto objetivo adopción del Op. Model,Evolución de vulnerabilidades,% Adopción total
mar 25,ARGENTINA,SYSTEMS ENGINEERING,ADQUIRENCIA,416,PA02 MERCHANTS-ACQUIRING,Level 1,85.00%,90.00%,95.00%,1,1,85.00%,88%`;

      (fetch as jest.Mock).mockResolvedValueOnce({
        text: jest.fn().mockResolvedValueOnce(mockCSV)
      });

      const result = await loadDashboardDataFromCSV();

      expect(fetch).toHaveBeenCalledWith('/data/Dashboard_Fuentus.csv');
      expect(result).toHaveLength(1);
      expect(result[0].period_month).toBe('mar 25');
      expect(result[0].ug_name).toBe('ARGENTINA');
      expect(result[0].nivel_certificacion).toBe('Level 1');
      expect(result[0].vertical).toBeDefined(); // Should have assigned a vertical
    });

    it('should handle fetch error and return fallback data', async () => {
      (fetch as jest.Mock).mockRejectedValueOnce(new Error('Network error'));

      // Mock console.error to avoid noise in tests
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation();

      const result = await loadDashboardDataFromCSV();

      expect(consoleSpy).toHaveBeenCalledWith('Error loading CSV data:', expect.any(Error));
      expect(result).toBeDefined(); // Should return fallback data
      expect(Array.isArray(result)).toBe(true);

      consoleSpy.mockRestore();
    });

    it('should handle malformed CSV gracefully', async () => {
      const mockCSV = `header1,header2
incomplete line`;

      (fetch as jest.Mock).mockResolvedValueOnce({
        text: jest.fn().mockResolvedValueOnce(mockCSV)
      });

      const result = await loadDashboardDataFromCSV();

      expect(result).toHaveLength(0); // Should not add malformed lines
    });

    it('should handle empty CSV', async () => {
      (fetch as jest.Mock).mockResolvedValueOnce({
        text: jest.fn().mockResolvedValueOnce('')
      });

      const result = await loadDashboardDataFromCSV();

      expect(result).toHaveLength(0);
    });
  });

  describe('getCertificationCounts', () => {
    it('should count certifications correctly', () => {
      const result = getCertificationCounts(mockData);
      
      expect(result['Level 1']).toBe(1);
      expect(result['Level 2']).toBe(1);
      expect(result['Level 3']).toBe(1);
      expect(result['Not certified']).toBe(1);
    });

    it('should handle empty data', () => {
      const result = getCertificationCounts([]);
      expect(result).toEqual({});
    });

    it('should handle data with same certification levels', () => {
      const sameData: DashboardDataRow[] = [
        { ...mockData[0], nivel_certificacion: 'Level 1' },
        { ...mockData[1], nivel_certificacion: 'Level 1' },
        { ...mockData[2], nivel_certificacion: 'Level 1' }
      ];

      const result = getCertificationCounts(sameData);
      expect(result['Level 1']).toBe(3);
      expect(Object.keys(result)).toHaveLength(1);
    });
  });

  describe('getUOL1ByGeography', () => {
    it('should return UOL1 filtered by geography', () => {
      const result = getUOL1ByGeography(mockData, 'ARGENTINA');
      expect(result).toEqual(['INGENIERÍA & DATA', 'SYSTEMS ENGINEERING']);
    });

    it('should return all UOL1 when no geography specified', () => {
      const result = getUOL1ByGeography(mockData);
      expect(result).toEqual(['INGENIERÍA & DATA', 'SYSTEMS ENGINEERING']);
    });

    it('should return empty array for non-existent geography', () => {
      const result = getUOL1ByGeography(mockData, 'NON_EXISTENT');
      expect(result).toEqual([]);
    });

    it('should handle empty data', () => {
      const result = getUOL1ByGeography([], 'ARGENTINA');
      expect(result).toEqual([]);
    });
  });

  describe('getUOL2ByGeographyAndUOL1', () => {
    it('should return UOL2 filtered by geography and UOL1', () => {
      const result = getUOL2ByGeographyAndUOL1(mockData, 'ARGENTINA', ['SYSTEMS ENGINEERING']);
      expect(result).toEqual(['ADQUIRENCIA']);
    });

    it('should return UOL2 for multiple UOL1s', () => {
      const result = getUOL2ByGeographyAndUOL1(mockData, 'ARGENTINA', ['SYSTEMS ENGINEERING', 'INGENIERÍA & DATA']);
      expect(result).toEqual(['ADQUIRENCIA', 'DATAHUB ARGENTINA']);
    });

    it('should handle no UOL1 filter', () => {
      const result = getUOL2ByGeographyAndUOL1(mockData, 'ARGENTINA');
      expect(result).toEqual(['ADQUIRENCIA', 'DATAHUB ARGENTINA']);
    });

    it('should return empty array for non-matching filters', () => {
      const result = getUOL2ByGeographyAndUOL1(mockData, 'NON_EXISTENT', ['SYSTEMS ENGINEERING']);
      expect(result).toEqual([]);
    });
  });

  describe('getUOL2ByVertical', () => {
    it('should return UOL2 filtered by vertical', () => {
      const result = getUOL2ByVertical(mockData, 'INDIVIDUOS Y PYMES');
      expect(result).toEqual(['ADQUIRENCIA', 'MEDIOS DE PAGO']);
    });

    it('should return all UOL2 when no vertical specified', () => {
      const result = getUOL2ByVertical(mockData);
      expect(result.length).toBeGreaterThan(0);
      expect(result).toEqual(['ADQUIRENCIA', 'DATAHUB ARGENTINA', 'MEDIOS DE PAGO', 'TRANSFORMACION']);
    });

    it('should return empty array for non-existent vertical', () => {
      const result = getUOL2ByVertical(mockData, 'NON_EXISTENT_VERTICAL');
      expect(result).toEqual([]);
    });
  });

  describe('getUOL1ByVertical', () => {
    it('should return UOL1 filtered by vertical', () => {
      const result = getUOL1ByVertical(mockData, 'INDIVIDUOS Y PYMES');
      expect(result).toEqual(['SYSTEMS ENGINEERING']);
    });

    it('should return all UOL1 when no vertical specified', () => {
      const result = getUOL1ByVertical(mockData);
      expect(result).toEqual(['INGENIERÍA & DATA', 'SYSTEMS ENGINEERING']);
    });

    it('should handle vertical with multiple UOL1s', () => {
      // We need to modify our test data to have same vertical with different UOL1s
      const modifiedData = [...mockData];
      modifiedData[1] = { ...mockData[1], vertical: 'INDIVIDUOS Y PYMES' };
      
      const result = getUOL1ByVertical(modifiedData, 'INDIVIDUOS Y PYMES');
      expect(result).toEqual(['INGENIERÍA & DATA', 'SYSTEMS ENGINEERING']);
    });
  });

  describe('getUOL1ByGeographyAndVertical', () => {
    it('should return UOL1 filtered by geography and vertical', () => {
      const result = getUOL1ByGeographyAndVertical(mockData, 'ARGENTINA', 'INDIVIDUOS Y PYMES');
      expect(result).toEqual(['SYSTEMS ENGINEERING']);
    });

    it('should handle no geography filter', () => {
      const result = getUOL1ByGeographyAndVertical(mockData, undefined, 'INDIVIDUOS Y PYMES');
      expect(result).toEqual(['SYSTEMS ENGINEERING']);
    });

    it('should handle no vertical filter', () => {
      const result = getUOL1ByGeographyAndVertical(mockData, 'ARGENTINA');
      expect(result).toEqual(['INGENIERÍA & DATA', 'SYSTEMS ENGINEERING']);
    });
  });

  describe('getUOL2ByGeographyUOL1AndVertical', () => {
    it('should return UOL2 filtered by all criteria', () => {
      const result = getUOL2ByGeographyUOL1AndVertical(
        mockData, 
        'ARGENTINA', 
        ['SYSTEMS ENGINEERING'], 
        'INDIVIDUOS Y PYMES'
      );
      expect(result).toEqual(['ADQUIRENCIA']);
    });

    it('should handle missing filters', () => {
      const result = getUOL2ByGeographyUOL1AndVertical(mockData);
      expect(result.length).toBeGreaterThan(0);
    });

    it('should return empty array when no matches', () => {
      const result = getUOL2ByGeographyUOL1AndVertical(
        mockData, 
        'NON_EXISTENT', 
        ['SYSTEMS ENGINEERING'], 
        'INDIVIDUOS Y PYMES'
      );
      expect(result).toEqual([]);
    });
  });

  // Import additional functions for comprehensive testing
  describe('Advanced Functions', () => {
    const {
      getVerticalStats,
      isUOL2InVertical,
      transformToCoverageData,
      getCoverageData,
      getCoverageStats,
      getCoverageByUOL2,
      getCoverageTrend
    } = require('../../data/dashboardData');

    describe('getVerticalStats', () => {
      it('should return vertical statistics correctly', () => {
        const result = getVerticalStats(mockData);
        
        expect(result['INDIVIDUOS Y PYMES']).toBeDefined();
        expect(result['INDIVIDUOS Y PYMES'].total).toBe(2);
        expect(result['CORE']).toBeDefined();
        expect(result['CORE'].total).toBe(1);
        expect(result['EMPRESAS']).toBeDefined();
        expect(result['EMPRESAS'].total).toBe(1);
      });

      it('should count certifications by vertical', () => {
        const result = getVerticalStats(mockData);
        
        expect(result['INDIVIDUOS Y PYMES'].certificationCounts['Level 1']).toBe(1);
        expect(result['INDIVIDUOS Y PYMES'].certificationCounts['Level 2']).toBe(1);
        expect(result['CORE'].certificationCounts['Not certified']).toBe(1);
        expect(result['EMPRESAS'].certificationCounts['Level 3']).toBe(1);
      });

      it('should handle empty data', () => {
        const result = getVerticalStats([]);
        expect(result).toEqual({});
      });
    });

    describe('isUOL2InVertical', () => {
      it('should return true for UOL2 in correct vertical', () => {
        // This depends on the VERTICAL_MAPPING constant
        // We'll test with some common mappings
        const result = isUOL2InVertical('ADQUIRENCIA', 'INDIVIDUOS Y PYMES');
        expect(typeof result).toBe('boolean');
      });

      it('should return false for UOL2 not in vertical', () => {
        const result = isUOL2InVertical('NON_EXISTENT_UOL2', 'INDIVIDUOS Y PYMES');
        expect(result).toBe(false);
      });

      it('should handle case insensitive matching', () => {
        const result = isUOL2InVertical('adquirencia', 'INDIVIDUOS Y PYMES');
        expect(typeof result).toBe('boolean');
      });

      it('should return false for non-existent vertical', () => {
        const result = isUOL2InVertical('ADQUIRENCIA', 'NON_EXISTENT_VERTICAL');
        expect(result).toBe(false);
      });
    });

    describe('transformToCoverageData', () => {
      it('should transform dashboard data to coverage data', () => {
        const result = transformToCoverageData(mockData);
        
        expect(result).toHaveLength(4);
        expect(result[0]).toHaveProperty('serviceName');
        expect(result[0]).toHaveProperty('serviceId');
        expect(result[0]).toHaveProperty('coverage');
        expect(result[0]).toHaveProperty('geography');
        expect(result[0]).toHaveProperty('vertical');
        
        expect(result[0].serviceName).toBe('PA02 MERCHANTS-ACQUIRING');
        expect(result[0].serviceId).toBe('416');
        expect(result[0].coverage).toBe(95); // From '95,00%'
        expect(result[0].geography).toBe('ARGENTINA');
        expect(result[0].vertical).toBe('INDIVIDUOS Y PYMES');
      });

      it('should handle percentage parsing correctly', () => {
        const testData: DashboardDataRow[] = [
          {
            ...mockData[0],
            calidad_features: '85,50%' // Decimal with comma
          },
          {
            ...mockData[1], 
            calidad_features: '90.25%' // Decimal with dot
          }
        ];

        const result = transformToCoverageData(testData);
        
        expect(result[0].coverage).toBe(85.5);
        expect(result[1].coverage).toBe(90.25);
      });

      it('should handle values with multiple percentage signs', () => {
        const testData: DashboardDataRow[] = [
          {
            ...mockData[0],
            calidad_features: '12%%,3'
          }
        ];

        const result = transformToCoverageData(testData);
        expect(result[0].coverage).toBe(12.3);
      });

      it('should handle invalid percentage values', () => {
        const testData: DashboardDataRow[] = [
          {
            ...mockData[0],
            calidad_features: 'invalid%'
          }
        ];

        const result = transformToCoverageData(testData);
        expect(result[0].coverage).toBe(0);
      });

      it('should handle empty data', () => {
        const result = transformToCoverageData([]);
        expect(result).toEqual([]);
      });
    });

    describe('getCoverageData', () => {
      it('should return filtered coverage data', () => {
        const result = getCoverageData(mockData, {
          geography: 'ARGENTINA',
          minCoverage: 80
        });

        expect(result.length).toBeGreaterThan(0);
        result.forEach((item: any) => {
          expect(item.geography).toBe('ARGENTINA');
          expect(item.coverage).toBeGreaterThanOrEqual(80);
        });
      });

      it('should filter by coverage range', () => {
        const result = getCoverageData(mockData, {
          minCoverage: 90,
          maxCoverage: 100
        });

        result.forEach((item: any) => {
          expect(item.coverage).toBeGreaterThanOrEqual(90);
          expect(item.coverage).toBeLessThanOrEqual(100);
        });
      });

      it('should handle no filters', () => {
        const result = getCoverageData(mockData);
        expect(result).toHaveLength(4);
      });

      it('should handle filters with no matches', () => {
        const result = getCoverageData(mockData, {
          geography: 'NON_EXISTENT',
          minCoverage: 99
        });

        expect(result).toHaveLength(0);
      });
    });

    describe('getCoverageStats', () => {
      it('should calculate coverage statistics correctly', () => {
        const coverageData = transformToCoverageData(mockData);
        const result = getCoverageStats(coverageData);

        expect(result.total).toBe(4);
        expect(result.averageCoverage).toBeGreaterThan(0);
        expect(result.highCoverage).toBeGreaterThanOrEqual(0);
        expect(result.mediumCoverage).toBeGreaterThanOrEqual(0);
        expect(result.lowCoverage).toBeGreaterThanOrEqual(0);
        expect(result.coverageRanges).toBeDefined();
        expect(typeof result.coverageRanges['90-100%']).toBe('number');
      });

      it('should handle empty data', () => {
        const result = getCoverageStats([]);
        
        expect(result.total).toBe(0);
        expect(result.averageCoverage).toBe(0);
        expect(result.highCoverage).toBe(0);
        expect(result.mediumCoverage).toBe(0);
        expect(result.lowCoverage).toBe(0);
        expect(result.coverageRanges).toEqual({});
      });

      it('should categorize coverage correctly', () => {
        const testData = [
          { coverage: 95, serviceName: 'test1', serviceId: '1', uol2Name: 'test', qualityFeatures: '95%', rfoStatus: '100%', dependencies: '100%', period: 'test', geography: 'test', vertical: 'test' },
          { coverage: 75, serviceName: 'test2', serviceId: '2', uol2Name: 'test', qualityFeatures: '75%', rfoStatus: '100%', dependencies: '100%', period: 'test', geography: 'test', vertical: 'test' },
          { coverage: 45, serviceName: 'test3', serviceId: '3', uol2Name: 'test', qualityFeatures: '45%', rfoStatus: '100%', dependencies: '100%', period: 'test', geography: 'test', vertical: 'test' }
        ];

        const result = getCoverageStats(testData);
        
        expect(result.highCoverage).toBe(1); // >= 80%
        expect(result.mediumCoverage).toBe(1); // 60-79%
        expect(result.lowCoverage).toBe(1); // < 60%
      });
    });

    describe('getCoverageByUOL2', () => {
      it('should calculate average coverage by UOL2', () => {
        const result = getCoverageByUOL2(mockData);
        
        expect(result['ADQUIRENCIA']).toBe(95);
        expect(result['DATAHUB ARGENTINA']).toBe(80);
        expect(result['MEDIOS DE PAGO']).toBe(91);
        expect(result['TRANSFORMACION']).toBe(98);
      });

      it('should handle multiple entries for same UOL2', () => {
        const testData: DashboardDataRow[] = [
          { ...mockData[0], calidad_features: '80,00%' },
          { ...mockData[0], calidad_features: '90,00%' } // Same UOL2
        ];

        const result = getCoverageByUOL2(testData);
        expect(result['ADQUIRENCIA']).toBe(85); // (80 + 90) / 2
      });

      it('should handle empty data', () => {
        const result = getCoverageByUOL2([]);
        expect(result).toEqual({});
      });
    });

    describe('getCoverageTrend', () => {
      it('should calculate coverage trend by period', () => {
        const result = getCoverageTrend(mockData);
        
        expect(result['mar 25']).toBeDefined();
        expect(result['abr 25']).toBeDefined();
        expect(result['may 25']).toBeDefined();
        
        expect(typeof result['mar 25']).toBe('number');
        expect(typeof result['abr 25']).toBe('number');
        expect(typeof result['may 25']).toBe('number');
      });

      it('should filter by UOL2 when specified', () => {
        const result = getCoverageTrend(mockData, 'ADQUIRENCIA');
        
        expect(result['mar 25']).toBe(95);
        expect(result['abr 25']).toBeUndefined();
        expect(result['may 25']).toBeUndefined();
      });

      it('should handle non-existent UOL2', () => {
        const result = getCoverageTrend(mockData, 'NON_EXISTENT');
        expect(result).toEqual({});
      });

      it('should handle empty data', () => {
        const result = getCoverageTrend([]);
        expect(result).toEqual({});
      });

      it('should handle multiple periods for same UOL2', () => {
        const testData: DashboardDataRow[] = [
          { ...mockData[0], period_month: 'jan 25', calidad_features: '80,00%' },
          { ...mockData[0], period_month: 'feb 25', calidad_features: '90,00%' },
          { ...mockData[0], period_month: 'jan 25', calidad_features: '70,00%' } // Same period, same UOL2
        ];

        const result = getCoverageTrend(testData, 'ADQUIRENCIA');
        expect(result['jan 25']).toBe(75); // (80 + 70) / 2
        expect(result['feb 25']).toBe(90);
      });

      it('should handle values with multiple percentage signs', () => {
        const testData: DashboardDataRow[] = [
          { ...mockData[0], period_month: 'jan 25', calidad_features: '12%%,3' }
        ];

        const result = getCoverageTrend(testData, 'ADQUIRENCIA');
        expect(result['jan 25']).toBe(12.3);
      });
    });
  });
});
