import { 
  getUniqueValues, 
  getUniqueVerticals, 
  filterData,
  DashboardDataRow 
} from '../../data/dashboardData';

describe('dashboardData - Simple Tests', () => {
  const mockData: DashboardDataRow[] = [
    {
      period_month: 'jun 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'SYSTEMS ENGINEERING',
      uol2_name: 'RETAIL',
      servicel1_id: '1',
      servicel1_name: 'Service 1',
      nivel_certificacion: 'Level 1',
      vertical: 'INDIVIDUOS Y PYMES',
      fichas_rfo_status_ok: '5',
      sn2_dependencias_asignadas: '3',
      calidad_features: 'High',
      certificacion: '1',
      operating_model: 'Model A',
      evolucion_vulnerabilidades: 'Improving',
      adopcion_total: '80%'
    },
    {
      period_month: 'may 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'SYSTEMS ENGINEERING',
      uol2_name: 'ENTERPRISE',
      servicel1_id: '2',
      servicel1_name: 'Service 2',
      nivel_certificacion: 'Level 2',
      vertical: 'EMPRESAS',
      fichas_rfo_status_ok: '8',
      sn2_dependencias_asignadas: '5',
      calidad_features: 'Medium',
      certificacion: '1',
      operating_model: 'Model B',
      evolucion_vulnerabilidades: 'Stable',
      adopcion_total: '70%'
    }
  ];

  describe('getUniqueValues', () => {
    test('returns unique values from data', () => {
      const periods = getUniqueValues(mockData, 'period_month');
      expect(periods).toEqual(['jun 25', 'may 25']);
      
      const geographies = getUniqueValues(mockData, 'ug_name');
      expect(geographies).toEqual(['ARGENTINA']);
    });

    test('handles empty data', () => {
      const result = getUniqueValues([], 'ug_name');
      expect(result).toEqual([]);
    });
  });

  describe('getUniqueVerticals', () => {
    test('returns unique verticals from data', () => {
      const result = getUniqueVerticals(mockData);
      expect(result).toEqual(['EMPRESAS', 'INDIVIDUOS Y PYMES']);
    });

    test('handles empty data', () => {
      const result = getUniqueVerticals([]);
      expect(result).toEqual([]);
    });
  });

  describe('filterData', () => {
    test('filters by period', () => {
      const result = filterData(mockData, 'jun 25');
      expect(result).toHaveLength(1);
      expect(result[0].period_month).toBe('jun 25');
    });

    test('filters by geography', () => {
      const result = filterData(mockData, undefined, 'ARGENTINA');
      expect(result).toHaveLength(2);
    });

    test('filters by UOL2', () => {
      const result = filterData(mockData, undefined, undefined, ['RETAIL']);
      expect(result).toHaveLength(1);
    });

    test('filters by vertical', () => {
      const result = filterData(mockData, undefined, undefined, undefined, 'EMPRESAS');
      expect(result).toHaveLength(1);
      expect(result[0].vertical).toBe('EMPRESAS');
    });

    test('combines filters', () => {
      const result = filterData(mockData, 'may 25', 'ARGENTINA', undefined, 'EMPRESAS');
      expect(result).toHaveLength(1);
      expect(result[0].servicel1_name).toBe('Service 2');
    });

    test('returns empty array when no matches', () => {
      const result = filterData(mockData, 'nonexistent');
      expect(result).toHaveLength(0);
    });
  });
});
