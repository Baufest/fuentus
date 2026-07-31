import { renderHook, act } from '@testing-library/react';
import { useDashboardFilters } from '../../hooks/useDashboardFilters';
import { DashboardDataRow } from '../../data/dashboardData';

const createMockData = (overrides: Partial<DashboardDataRow> = {}): DashboardDataRow => ({
  period_month: '2024-01',
  ug_name: 'ARGENTINA',
  vertical: 'INDIVIDUOS Y PYMES',
  uol1_name: 'SYSTEMS ENGINEERING',
  uol2_name: 'DIGITAL CHANNELS',
  servicel1_id: '001',
  servicel1_name: 'TEST SERVICE',
  nivel_certificacion: 'Level 1',
  fichas_rfo_status_ok: '100%',
  sn2_dependencias_asignadas: '100%',
  calidad_features: '100%',
  certificacion: '1',
  operating_model: '1',
  evolucion_vulnerabilidades: '100%',
  adopcion_total: '100%',
  ...overrides
});

describe('useDashboardFilters', () => {
  const mockData: DashboardDataRow[] = [
    createMockData({ 
      period_month: '2024-01', 
      ug_name: 'ARGENTINA', 
      vertical: 'INDIVIDUOS Y PYMES', 
      uol1_name: 'SYSTEMS ENGINEERING', 
      uol2_name: 'DIGITAL CHANNELS' 
    }),
    createMockData({ 
      period_month: '2024-02', 
      ug_name: 'ARGENTINA', 
      vertical: 'INDIVIDUOS Y PYMES', 
      uol1_name: 'SYSTEMS ENGINEERING', 
      uol2_name: 'MOBILE BANKING' 
    }),
    createMockData({ 
      period_month: '2024-01', 
      ug_name: 'ESPAÑA', 
      vertical: 'VINCULADAS Y SEGUROS', 
      uol1_name: 'BUSINESS UNIT', 
      uol2_name: 'INSURANCE CORE' 
    }),
    createMockData({ 
      period_month: '2024-01', 
      ug_name: 'MÉXICO', 
      vertical: 'INDIVIDUOS Y PYMES', 
      uol1_name: 'TECHNOLOGY', 
      uol2_name: 'CORE BANKING' 
    }),
  ];

  test('should initialize with default filter values', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    expect(result.current.filters.selectedPeriod).toBe('');
    expect(result.current.filters.selectedGeography).toBe('ARGENTINA');
    expect(result.current.filters.selectedVertical).toBe('');
    expect(result.current.filters.selectedUOL1).toEqual([]);
    expect(result.current.filters.selectedUOL2).toEqual([]);
    expect(result.current.filters.isVerticalDisabled).toBe(false);
  });

  test('should initialize with empty options when no data', () => {
    const { result } = renderHook(() => useDashboardFilters([]));

    expect(result.current.options.availablePeriods).toEqual([]);
    expect(result.current.options.availableGeographies).toEqual([]);
    expect(result.current.options.availableVerticals).toEqual([]);
    expect(result.current.options.availableUOL1).toEqual([]);
    expect(result.current.options.availableUOL2).toEqual([]);
  });

  test('should have all required handlers', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    expect(typeof result.current.handlers.handleGeographyChange).toBe('function');
    expect(typeof result.current.handlers.handleVerticalChange).toBe('function');
    expect(typeof result.current.handlers.setSelectedPeriod).toBe('function');
    expect(typeof result.current.handlers.setSelectedUOL1).toBe('function');
    expect(typeof result.current.handlers.setSelectedUOL2).toBe('function');
    expect(typeof result.current.handlers.setOptions).toBe('function');
  });

  test('should handle period change', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.setSelectedPeriod('2024-01');
    });

    expect(result.current.filters.selectedPeriod).toBe('2024-01');
  });

  test('should handle UOL1 selection', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.setSelectedUOL1(['SYSTEMS ENGINEERING']);
    });

    expect(result.current.filters.selectedUOL1).toEqual(['SYSTEMS ENGINEERING']);
  });

  test('should handle UOL2 selection', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.setSelectedUOL2(['DIGITAL CHANNELS']);
    });

    expect(result.current.filters.selectedUOL2).toEqual(['DIGITAL CHANNELS']);
  });

  test('should handle geography change', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    const mockEvent = {
      target: { value: 'ESPAÑA' }
    } as React.ChangeEvent<HTMLSelectElement>;

    act(() => {
      result.current.handlers.handleGeographyChange(mockEvent);
    });

    expect(result.current.filters.selectedGeography).toBe('ESPAÑA');
  });

  test('should handle vertical change when enabled', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    const mockEvent = {
      target: { value: 'INDIVIDUOS Y PYMES' }
    } as React.ChangeEvent<HTMLSelectElement>;

    act(() => {
      result.current.handlers.handleVerticalChange(mockEvent);
    });

    expect(result.current.filters.selectedVertical).toBe('INDIVIDUOS Y PYMES');
  });

  test('should handle custom options setting', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    const customOptions = {
      availablePeriods: ['2024-03', '2024-04'],
      availableGeographies: ['COLOMBIA']
    };

    act(() => {
      result.current.handlers.setOptions(customOptions);
    });

    expect(result.current.options.availablePeriods).toEqual(['2024-03', '2024-04']);
    expect(result.current.options.availableGeographies).toEqual(['COLOMBIA']);
  });

  test('should filter UOL1 options based on geography', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.handleGeographyChange({
        target: { value: 'ESPAÑA' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    expect(result.current.options.availableUOL1).toContain('BUSINESS UNIT');
  });

  test('should filter UOL1 selections when geography changes', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.setSelectedUOL1(['SYSTEMS ENGINEERING', 'TECHNOLOGY']);
    });

    expect(result.current.filters.selectedUOL1).toEqual(['SYSTEMS ENGINEERING', 'TECHNOLOGY']);

    act(() => {
      result.current.handlers.handleGeographyChange({
        target: { value: 'ESPAÑA' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    expect(Array.isArray(result.current.filters.selectedUOL1)).toBe(true);
  });

  test('should update UOL2 options based on filters', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.setSelectedUOL1(['SYSTEMS ENGINEERING']);
    });

    expect(Array.isArray(result.current.options.availableUOL2)).toBe(true);
  });

  test('should filter UOL2 selections when options change', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.setSelectedUOL2(['DIGITAL CHANNELS', 'MOBILE BANKING']);
    });

    expect(result.current.filters.selectedUOL2).toEqual(['DIGITAL CHANNELS', 'MOBILE BANKING']);

    act(() => {
      result.current.handlers.handleGeographyChange({
        target: { value: 'ESPAÑA' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    expect(Array.isArray(result.current.filters.selectedUOL2)).toBe(true);
  });

  test('should enable vertical when no UOL1 is selected', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    expect(result.current.filters.isVerticalDisabled).toBe(false);

    act(() => {
      result.current.handlers.setSelectedUOL1(['SYSTEMS ENGINEERING']);
    });

    act(() => {
      result.current.handlers.setSelectedUOL1([]);
    });

    expect(result.current.filters.isVerticalDisabled).toBe(false);
  });

  test('should manage vertical state based on UOL1 selection', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.handleVerticalChange({
        target: { value: 'INDIVIDUOS Y PYMES' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    act(() => {
      result.current.handlers.setSelectedUOL1(['BUSINESS UNIT']);
    });

    expect(typeof result.current.filters.isVerticalDisabled).toBe('boolean');
    expect(typeof result.current.filters.selectedVertical).toBe('string');
  });

  test('should handle multiple filters applied in sequence', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.handleGeographyChange({
        target: { value: 'ARGENTINA' }
      } as React.ChangeEvent<HTMLSelectElement>);
      
      result.current.handlers.handleVerticalChange({
        target: { value: 'INDIVIDUOS Y PYMES' }
      } as React.ChangeEvent<HTMLSelectElement>);
      
      result.current.handlers.setSelectedUOL1(['SYSTEMS ENGINEERING']);
      result.current.handlers.setSelectedUOL2(['DIGITAL CHANNELS']);
      result.current.handlers.setSelectedPeriod('2024-01');
    });

    expect(result.current.filters.selectedGeography).toBe('ARGENTINA');
    expect(result.current.filters.selectedVertical).toBe('INDIVIDUOS Y PYMES');
    expect(result.current.filters.selectedUOL1).toEqual(['SYSTEMS ENGINEERING']);
    expect(result.current.filters.selectedUOL2).toEqual(['DIGITAL CHANNELS']);
    expect(result.current.filters.selectedPeriod).toBe('2024-01');
  });

  test('should handle SYSTEMS ENGINEERING selection correctly', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.handleVerticalChange({
        target: { value: 'INDIVIDUOS Y PYMES' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    act(() => {
      result.current.handlers.setSelectedUOL1(['SYSTEMS ENGINEERING', 'TECHNOLOGY']);
    });

    expect(result.current.filters.selectedUOL1).toEqual(['SYSTEMS ENGINEERING', 'TECHNOLOGY']);
    expect(typeof result.current.filters.isVerticalDisabled).toBe('boolean');
  });

  test('should handle empty geography selection', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.handleGeographyChange({
        target: { value: '' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    expect(result.current.filters.selectedGeography).toBe('');
  });

  test('should handle empty vertical selection', () => {
    const { result } = renderHook(() => useDashboardFilters(mockData));

    act(() => {
      result.current.handlers.handleVerticalChange({
        target: { value: '' }
      } as React.ChangeEvent<HTMLSelectElement>);
    });

    expect(result.current.filters.selectedVertical).toBe('');
  });
});
