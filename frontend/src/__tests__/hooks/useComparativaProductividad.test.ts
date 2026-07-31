import { renderHook, waitFor } from '@testing-library/react';
import { useComparativaProductividad, ComparativaData, GraficoNivel, ItemGrafico } from '../../hooks/useComparativaProductividad';
import fuentusapi from '../../api/fuentusapi';

// Mock del módulo fuentusapi
jest.mock('../../api/fuentusapi', () => ({
  get: jest.fn(),
}));

describe('useComparativaProductividad', () => {
  const mockGet = fuentusapi.get as jest.MockedFunction<typeof fuentusapi.get>;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  const mockItem: ItemGrafico = {
    nombre: 'Equipo A',
    productividad: 85.5,
    promedioLT: 75.2,
    promedioCT: 90.8,
    totalFeatures: 120,
    totalFTEs: 15,
  };

  const mockGraficoNivel: GraficoNivel = {
    nivelTipo: 'FABRICA',
    titulo: 'Comparativa por Fábrica',
    items: [mockItem],
  };

  const mockComparativaData: ComparativaData = {
    nivelFiltrado: 'FABRICA',
    graficos: [mockGraficoNivel],
  };

  describe('initial state', () => {
    test('should initialize with null data, loading false, and no error', () => {
      mockGet.mockImplementation(() => new Promise(() => {}));

      const { result } = renderHook(() => 
        useComparativaProductividad({ enabled: false })
      );

      expect(result.current.data).toBe(null);
      expect(result.current.loading).toBe(false);
      expect(result.current.error).toBe(null);
    });

    test('should initialize with loading state when enabled', () => {
      mockGet.mockImplementation(() => new Promise(() => {}));

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      expect(result.current.loading).toBe(true);
    });
  });

  describe('data fetching', () => {
    test('should fetch comparativa data successfully without filters', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({})
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?');
    });

    test('should fetch comparativa data with vertical filter', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS Y PYMES' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?vertical=INDIVIDUOS+Y+PYMES');
    });

    test('should fetch comparativa data with fabrica filter', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ fabrica: 'DIGITAL CHANNELS' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?fabrica=DIGITAL+CHANNELS');
    });

    test('should fetch comparativa data with sn1 filter', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ sn1: 'Service Level 1' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?sn1=Service+Level+1');
    });

    test('should fetch comparativa data with sn2 filter', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ sn2: 'Service Level 2' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?sn2=Service+Level+2');
    });

    test('should fetch comparativa data with multiple filters', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({
          vertical: 'INDIVIDUOS',
          fabrica: 'DIGITAL',
          sn1: 'SN1',
          sn2: 'SN2',
        })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?vertical=INDIVIDUOS&fabrica=DIGITAL&sn1=SN1&sn2=SN2');
    });
  });

  describe('error handling', () => {
    test('should handle API errors', async () => {
      const mockError = new Error('API Error');
      mockGet.mockRejectedValue(mockError);
      
      // Mock console.error to avoid test output pollution
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toBe(null);
      expect(result.current.error).toEqual(mockError);
      expect(consoleSpy).toHaveBeenCalledWith('Error fetching comparativa:', mockError);

      consoleSpy.mockRestore();
    });

    test('should clear previous error on successful fetch', async () => {
      const mockError = new Error('API Error');
      
      // Mock console.error
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

      // First call fails
      mockGet.mockRejectedValueOnce(mockError);
      
      const { result, rerender } = renderHook(
        ({ params }) => useComparativaProductividad(params),
        { initialProps: { params: { vertical: 'INDIVIDUOS' } } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(result.current.error).toEqual(mockError);

      // Second call succeeds
      mockGet.mockResolvedValueOnce({ data: mockComparativaData });
      
      rerender({ params: { vertical: 'PYMES' } });

      await waitFor(() => expect(result.current.loading).toBe(false));
      
      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);

      consoleSpy.mockRestore();
    });
  });

  describe('enabled parameter', () => {
    test('should not fetch data when enabled is false', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS', enabled: false })
      );

      // Wait a bit to ensure no call is made
      await new Promise(resolve => setTimeout(resolve, 100));

      expect(result.current.loading).toBe(false);
      expect(result.current.data).toBe(null);
      expect(result.current.error).toBe(null);
      expect(mockGet).not.toHaveBeenCalled();
    });

    test('should fetch data when enabled is true', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS', enabled: true })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(mockGet).toHaveBeenCalled();
    });

    test('should fetch data when enabled is undefined', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(mockComparativaData);
      expect(mockGet).toHaveBeenCalled();
    });
  });

  describe('parameter changes', () => {
    test('should refetch data when vertical changes', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result, rerender } = renderHook(
        ({ params }) => useComparativaProductividad(params),
        { initialProps: { params: { vertical: 'INDIVIDUOS' } } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGet).toHaveBeenCalledTimes(1);

      mockGet.mockClear();
      mockGet.mockResolvedValue({ data: mockComparativaData });

      rerender({ params: { vertical: 'PYMES' } });

      await waitFor(() => expect(mockGet).toHaveBeenCalledTimes(1));
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?vertical=PYMES');
    });

    test('should refetch data when fabrica changes', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result, rerender } = renderHook(
        ({ params }) => useComparativaProductividad(params),
        { initialProps: { params: { fabrica: 'DIGITAL' } } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGet).toHaveBeenCalledTimes(1);

      mockGet.mockClear();
      mockGet.mockResolvedValue({ data: mockComparativaData });

      rerender({ params: { fabrica: 'CHANNELS' } });

      await waitFor(() => expect(mockGet).toHaveBeenCalledTimes(1));
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?fabrica=CHANNELS');
    });

    test('should refetch data when sn1 changes', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result, rerender } = renderHook(
        ({ params }) => useComparativaProductividad(params),
        { initialProps: { params: { sn1: 'SN1-A' } } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGet).toHaveBeenCalledTimes(1);

      mockGet.mockClear();
      mockGet.mockResolvedValue({ data: mockComparativaData });

      rerender({ params: { sn1: 'SN1-B' } });

      await waitFor(() => expect(mockGet).toHaveBeenCalledTimes(1));
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?sn1=SN1-B');
    });

    test('should refetch data when sn2 changes', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result, rerender } = renderHook(
        ({ params }) => useComparativaProductividad(params),
        { initialProps: { params: { sn2: 'SN2-A' } } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGet).toHaveBeenCalledTimes(1);

      mockGet.mockClear();
      mockGet.mockResolvedValue({ data: mockComparativaData });

      rerender({ params: { sn2: 'SN2-B' } });

      await waitFor(() => expect(mockGet).toHaveBeenCalledTimes(1));
      expect(mockGet).toHaveBeenCalledWith('/api/comparativa-productividad?sn2=SN2-B');
    });

    test('should refetch data when enabled changes from false to true', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result, rerender } = renderHook(
        ({ params }) => useComparativaProductividad(params),
        { initialProps: { params: { vertical: 'INDIVIDUOS', enabled: false } } }
      );

      expect(mockGet).not.toHaveBeenCalled();

      rerender({ params: { vertical: 'INDIVIDUOS', enabled: true } });

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGet).toHaveBeenCalledTimes(1);
      expect(result.current.data).toEqual(mockComparativaData);
    });
  });

  describe('loading state management', () => {
    test('should set loading to true while fetching', async () => {
      let resolver: (value: any) => void;
      const promise = new Promise((resolve) => {
        resolver = resolve;
      });
      
      mockGet.mockReturnValue(promise as any);

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      expect(result.current.loading).toBe(true);
      expect(result.current.data).toBe(null);

      resolver!({ data: mockComparativaData });

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(result.current.data).toEqual(mockComparativaData);
    });

    test('should set loading to false after successful fetch', async () => {
      mockGet.mockResolvedValue({ data: mockComparativaData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      
      expect(result.current.data).toEqual(mockComparativaData);
      expect(result.current.error).toBe(null);
    });

    test('should set loading to false after failed fetch', async () => {
      const mockError = new Error('API Error');
      mockGet.mockRejectedValue(mockError);
      
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      
      expect(result.current.data).toBe(null);
      expect(result.current.error).toEqual(mockError);

      consoleSpy.mockRestore();
    });
  });

  describe('data structure', () => {
    test('should handle empty graficos array', async () => {
      const emptyData: ComparativaData = {
        nivelFiltrado: 'VERTICAL',
        graficos: [],
      };
      
      mockGet.mockResolvedValue({ data: emptyData });

      const { result } = renderHook(() => 
        useComparativaProductividad({ vertical: 'INDIVIDUOS' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(emptyData);
      expect(result.current.data?.graficos).toHaveLength(0);
    });

    test('should handle multiple graficos', async () => {
      const multipleGraficos: ComparativaData = {
        nivelFiltrado: 'MULTIPLE',
        graficos: [
          {
            nivelTipo: 'FABRICA',
            titulo: 'Por Fábrica',
            items: [mockItem],
          },
          {
            nivelTipo: 'VERTICAL',
            titulo: 'Por Vertical',
            items: [mockItem, { ...mockItem, nombre: 'Equipo B' }],
          },
        ],
      };
      
      mockGet.mockResolvedValue({ data: multipleGraficos });

      const { result } = renderHook(() => 
        useComparativaProductividad({})
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(multipleGraficos);
      expect(result.current.data?.graficos).toHaveLength(2);
      expect(result.current.data?.graficos[1].items).toHaveLength(2);
    });

    test('should handle null values in items', async () => {
      const itemWithNulls: ItemGrafico = {
        nombre: 'Equipo C',
        productividad: null,
        promedioLT: null,
        promedioCT: null,
        totalFeatures: 0,
        totalFTEs: 0,
      };

      const dataWithNulls: ComparativaData = {
        nivelFiltrado: 'FABRICA',
        graficos: [{
          nivelTipo: 'FABRICA',
          titulo: 'Con nulos',
          items: [itemWithNulls],
        }],
      };
      
      mockGet.mockResolvedValue({ data: dataWithNulls });

      const { result } = renderHook(() => 
        useComparativaProductividad({ fabrica: 'TEST' })
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.data).toEqual(dataWithNulls);
      expect(result.current.data?.graficos[0].items[0].productividad).toBe(null);
    });
  });
});
