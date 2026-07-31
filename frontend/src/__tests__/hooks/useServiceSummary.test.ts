import { renderHook, act, waitFor } from '@testing-library/react';
import { useServiceSummary, useServiceApps, useServiceSummaryById, useServiceAppsById } from '../../hooks/useServiceSummary';
import { ServiceSummaryDTO } from '../../types/statsSummary';
import { Apps as App } from '../../types/app';
import * as statsSummaryApi from '../../api/statsSummaryApi';

// Mock del módulo statsSummaryApi
jest.mock('../../api/statsSummaryApi', () => ({
  getServiceSummary: jest.fn(),
  getAppsByMultipleUUAAs: jest.fn(),
  getServiceSummaryById: jest.fn(),
  getAppsByServiceId: jest.fn(),
}));

describe('useServiceSummary', () => {
  const mockGetServiceSummary = statsSummaryApi.getServiceSummary as jest.MockedFunction<typeof statsSummaryApi.getServiceSummary>;
  const mockGetAppsByMultipleUUAAs = statsSummaryApi.getAppsByMultipleUUAAs as jest.MockedFunction<typeof statsSummaryApi.getAppsByMultipleUUAAs>;
  const mockGetServiceSummaryById = statsSummaryApi.getServiceSummaryById as jest.MockedFunction<typeof statsSummaryApi.getServiceSummaryById>;
  const mockGetAppsByServiceId = statsSummaryApi.getAppsByServiceId as jest.MockedFunction<typeof statsSummaryApi.getAppsByServiceId>;

  beforeEach(() => {
    jest.clearAllMocks();
    // Configure default resolved values to prevent timeouts
    mockGetServiceSummary.mockResolvedValue(mockSummaryData);
    mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
    mockGetServiceSummaryById.mockResolvedValue(mockSummaryData);
    mockGetAppsByServiceId.mockResolvedValue(mockAppsData);
  });

  const mockSummaryData: ServiceSummaryDTO = {
    serviceId: 1,
    serviceN1: 'Service N1',
    serviceN2: 'Service N2',
    ownerServiceN1: 'Owner',
    uuaas: ['TEST001', 'TEST002'],
    totalApps: 10,
    averageCoverage: 85.5,
    totalBugs: 5,
    totalSastLow: 2,
    totalSastMedium: 1,
    totalSastHigh: 0,
    totalScaLow: 1,
    totalScaMedium: 0,
    totalScaHigh: 0,
    totalScaCritical: 0,
    rfoId: 123,
    rfoEstado: 'Activo',
    totalSastVulnerabilities: 3,
    totalScaVulnerabilities: 1,
    totalVulnerabilities: 4,
  };

  const mockAppsData = {
    content: [
      {
        id: 1,
        name: 'App 1',
        uuaa: 'TEST001',
        bitbucketUrl: 'https://bitbucket.com/app1',
        sonarUrl: 'https://sonar.com/app1',
        sonar10Url: 'https://sonar10.com/app1',
        chimeraUrl: 'https://chimera.com/app1',
        samuelUrl: 'https://samuel.com/app1',
        monolith: false,
        coverage: 80,
        bugs: 2,
        language: null,
        chimeraSast: {
          totalLow: 1,
          totalMedium: 0,
          totalHigh: 0,
        },
        chimeraSca: {
          totalLow: 0,
          totalMedium: 0,
          totalHigh: 0,
          totalCritical: 0,
        },
      },
      {
        id: 2,
        name: 'App 2',
        uuaa: 'TEST002',
        bitbucketUrl: 'https://bitbucket.com/app2',
        sonarUrl: 'https://sonar.com/app2',
        sonar10Url: 'https://sonar10.com/app2',
        chimeraUrl: 'https://chimera.com/app2',
        samuelUrl: 'https://samuel.com/app2',
        monolith: true,
        coverage: 90,
        bugs: 1,
        language: null,
        chimeraSast: {
          totalLow: 0,
          totalMedium: 1,
          totalHigh: 0,
        },
        chimeraSca: {
          totalLow: 0,
          totalMedium: 0,
          totalHigh: 0,
          totalCritical: 0,
        },
      },
    ] as App[],
    totalElements: 2,
    totalPages: 1,
    number: 0,
  };

  describe('useServiceSummary hook', () => {
    test('should initialize with loading state', () => {
      mockGetServiceSummary.mockImplementation(() => new Promise(() => {}));
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => 
        useServiceSummary(uuaas, 'Service N1', 'Service N2', 'Owner')
      );

      expect(result.current.loading).toBe(true);
      expect(result.current.summary).toBe(null);
      expect(result.current.error).toBe(null);
    });

    test('should fetch summary successfully', async () => {
      mockGetServiceSummary.mockResolvedValue(mockSummaryData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => 
        useServiceSummary(uuaas, 'Service N1', 'Service N2', 'Owner')
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.summary).toEqual(mockSummaryData);
      expect(result.current.error).toBe(null);
      expect(mockGetServiceSummary).toHaveBeenCalledWith(
        ['TEST001'],
        'Service N1',
        'Service N2',
        'Owner',
        undefined,
        undefined
      );
    });

    test('should fetch summary with optional rfoId and rfoEstado', async () => {
      mockGetServiceSummary.mockResolvedValue(mockSummaryData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => 
        useServiceSummary(uuaas, 'Service N1', 'Service N2', 'Owner', 123, 'Activo')
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetServiceSummary).toHaveBeenCalledWith(
        ['TEST001'],
        'Service N1',
        'Service N2',
        'Owner',
        123,
        'Activo'
      );
    });

    test('should handle error when fetching summary', async () => {
      const error = new Error('API Error');
      mockGetServiceSummary.mockRejectedValue(error);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => 
        useServiceSummary(uuaas, 'Service N1', 'Service N2', 'Owner')
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.summary).toBe(null);
      expect(result.current.error).toBe('API Error');
    });

    test('should handle non-Error exception', async () => {
      mockGetServiceSummary.mockRejectedValue('String error');
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => 
        useServiceSummary(uuaas, 'Service N1', 'Service N2', 'Owner')
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.error).toBe('Error al obtener el resumen del servicio');
    });

    test('should not fetch when uuaas is empty', async () => {
      const { result } = renderHook(() => 
        useServiceSummary([], 'Service N1', 'Service N2', 'Owner')
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetServiceSummary).not.toHaveBeenCalled();
      expect(result.current.summary).toBe(null);
    });

    test('should refetch summary when refetch is called', async () => {
      mockGetServiceSummary.mockResolvedValue(mockSummaryData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => 
        useServiceSummary(uuaas, 'Service N1', 'Service N2', 'Owner')
      );

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetServiceSummary).toHaveBeenCalledTimes(1);

      act(() => {
        result.current.refetch();
      });

      await waitFor(() => expect(mockGetServiceSummary).toHaveBeenCalledTimes(2));
    });

    test('should refetch when dependencies change', async () => {
      mockGetServiceSummary.mockResolvedValue(mockSummaryData);

      const { result, rerender } = renderHook(
        ({ uuaas, serviceN1 }) => useServiceSummary(uuaas, serviceN1, 'Service N2', 'Owner'),
        { initialProps: { uuaas: ['TEST001'], serviceN1: 'Service N1' } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetServiceSummary).toHaveBeenCalledTimes(1);

      rerender({ uuaas: ['TEST002'], serviceN1: 'Service N1 Updated' });

      await waitFor(() => expect(mockGetServiceSummary).toHaveBeenCalledTimes(2));
      expect(mockGetServiceSummary).toHaveBeenLastCalledWith(
        ['TEST002'],
        'Service N1 Updated',
        'Service N2',
        'Owner',
        undefined,
        undefined
      );
    });
  });

  describe('useServiceApps hook', () => {
    test('should initialize with loading state', () => {
      mockGetAppsByMultipleUUAAs.mockImplementation(() => new Promise(() => {}));
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      expect(result.current.loading).toBe(true);
      expect(result.current.apps).toEqual([]);
      expect(result.current.error).toBe(null);
    });

    test('should fetch apps successfully', async () => {
      mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.apps).toEqual(mockAppsData.content);
      expect(result.current.totalElements).toBe(2);
      expect(result.current.totalPages).toBe(1);
      expect(result.current.currentPage).toBe(0);
      expect(result.current.error).toBe(null);
      expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledWith(['TEST001'], 0, '');
    });

    test('should fetch apps with initial page and search', async () => {
      mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas, 2, 'test search'));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledWith(['TEST001'], 2, 'test search');
      expect(result.current.currentPage).toBe(2);
    });

    test('should handle error when fetching apps', async () => {
      const error = new Error('Apps API Error');
      mockGetAppsByMultipleUUAAs.mockRejectedValue(error);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.apps).toEqual([]);
      expect(result.current.error).toBe('Apps API Error');
    });

    test('should handle non-Error exception when fetching apps', async () => {
      mockGetAppsByMultipleUUAAs.mockRejectedValue('String error');
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.error).toBe('Error al obtener las aplicaciones');
    });

    test('should not fetch when uuaas is empty', async () => {
      const { result } = renderHook(() => useServiceApps([]));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetAppsByMultipleUUAAs).not.toHaveBeenCalled();
      expect(result.current.apps).toEqual([]);
    });

    test('should change page when setPage is called', async () => {
      mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      await waitFor(() => expect(result.current.loading).toBe(false));

      act(() => {
        result.current.setPage(1);
      });

      await waitFor(() => {
        expect(result.current.currentPage).toBe(1);
        expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledWith(['TEST001'], 1, '');
      });
    });

    test('should update search and reset page when setSearch is called', async () => {
      mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas, 2));

      await waitFor(() => expect(result.current.loading).toBe(false));

      act(() => {
        result.current.setSearch('new search');
      });

      await waitFor(() => {
        expect(result.current.currentPage).toBe(0);
        expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledWith(['TEST001'], 0, 'new search');
      });
    });

    test('should refetch apps when refetch is called', async () => {
      mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledTimes(1);

      act(() => {
        result.current.refetch();
      });

      await waitFor(() => expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledTimes(2));
    });

    test('should refetch when page changes', async () => {
      mockGetAppsByMultipleUUAAs.mockResolvedValue(mockAppsData);
      const uuaas = ['TEST001'];

      const { result } = renderHook(() => useServiceApps(uuaas));

      await waitFor(() => expect(result.current.loading).toBe(false));

      act(() => {
        result.current.setPage(2);
      });

      await waitFor(() => expect(mockGetAppsByMultipleUUAAs).toHaveBeenCalledTimes(2));
    });
  });

  describe('useServiceSummaryById hook', () => {
    test('should initialize with loading state', () => {
      mockGetServiceSummaryById.mockImplementation(() => new Promise(() => {}));

      const { result } = renderHook(() => useServiceSummaryById(123));

      expect(result.current.loading).toBe(true);
      expect(result.current.summary).toBe(null);
      expect(result.current.error).toBe(null);
    });

    test('should fetch summary by id successfully', async () => {
      mockGetServiceSummaryById.mockResolvedValue(mockSummaryData);

      const { result } = renderHook(() => useServiceSummaryById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.summary).toEqual(mockSummaryData);
      expect(result.current.error).toBe(null);
      expect(mockGetServiceSummaryById).toHaveBeenCalledWith(123);
    });

    test('should handle error when fetching summary by id', async () => {
      const error = new Error('Service not found');
      mockGetServiceSummaryById.mockRejectedValue(error);

      const { result } = renderHook(() => useServiceSummaryById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.summary).toBe(null);
      expect(result.current.error).toBe('Service not found');
    });

    test('should handle non-Error exception', async () => {
      mockGetServiceSummaryById.mockRejectedValue('String error');

      const { result } = renderHook(() => useServiceSummaryById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.error).toBe('Error al obtener el resumen del servicio');
    });

    test('should not fetch when serviceId is null', async () => {
      const { result } = renderHook(() => useServiceSummaryById(null));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetServiceSummaryById).not.toHaveBeenCalled();
      expect(result.current.summary).toBe(null);
    });

    test('should refetch when refetch is called', async () => {
      mockGetServiceSummaryById.mockResolvedValue(mockSummaryData);

      const { result } = renderHook(() => useServiceSummaryById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetServiceSummaryById).toHaveBeenCalledTimes(1);

      act(() => {
        result.current.refetch();
      });

      await waitFor(() => expect(mockGetServiceSummaryById).toHaveBeenCalledTimes(2));
    });

    test('should refetch when serviceId changes', async () => {
      mockGetServiceSummaryById.mockResolvedValue(mockSummaryData);

      const { result, rerender } = renderHook(
        ({ serviceId }) => useServiceSummaryById(serviceId),
        { initialProps: { serviceId: 123 } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetServiceSummaryById).toHaveBeenCalledTimes(1);

      rerender({ serviceId: 456 });

      await waitFor(() => expect(mockGetServiceSummaryById).toHaveBeenCalledTimes(2));
      expect(mockGetServiceSummaryById).toHaveBeenLastCalledWith(456);
    });

    test('should stop fetching when serviceId changes to null', async () => {
      mockGetServiceSummaryById.mockResolvedValue(mockSummaryData);

      const { result, rerender } = renderHook(
        ({ serviceId }) => useServiceSummaryById(serviceId),
        { initialProps: { serviceId: 123 as number | null } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetServiceSummaryById).toHaveBeenCalledTimes(1);

      rerender({ serviceId: null });

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetServiceSummaryById).toHaveBeenCalledTimes(1);
    });
  });

  describe('useServiceAppsById hook', () => {
    test('should initialize with loading state', () => {
      mockGetAppsByServiceId.mockImplementation(() => new Promise(() => {}));

      const { result } = renderHook(() => useServiceAppsById(123));

      expect(result.current.loading).toBe(true);
      expect(result.current.apps).toEqual([]);
      expect(result.current.error).toBe(null);
    });

    test('should fetch apps by service id successfully', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result } = renderHook(() => useServiceAppsById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.apps).toEqual(mockAppsData.content);
      expect(result.current.totalElements).toBe(2);
      expect(result.current.totalPages).toBe(1);
      expect(result.current.currentPage).toBe(0);
      expect(result.current.error).toBe(null);
      expect(mockGetAppsByServiceId).toHaveBeenCalledWith(123, 0, '');
    });

    test('should fetch apps with initial page and search', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result } = renderHook(() => useServiceAppsById(123, 3, 'search term'));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetAppsByServiceId).toHaveBeenCalledWith(123, 3, 'search term');
      expect(result.current.currentPage).toBe(3);
    });

    test('should handle error when fetching apps by service id', async () => {
      const error = new Error('Service apps error');
      mockGetAppsByServiceId.mockRejectedValue(error);

      const { result } = renderHook(() => useServiceAppsById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.apps).toEqual([]);
      expect(result.current.error).toBe('Service apps error');
    });

    test('should handle non-Error exception when fetching apps', async () => {
      mockGetAppsByServiceId.mockRejectedValue('String error');

      const { result } = renderHook(() => useServiceAppsById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.error).toBe('Error al obtener las aplicaciones');
    });

    test('should not fetch when serviceId is null', async () => {
      const { result } = renderHook(() => useServiceAppsById(null));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetAppsByServiceId).not.toHaveBeenCalled();
      expect(result.current.apps).toEqual([]);
    });

    test('should change page when setPage is called', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result } = renderHook(() => useServiceAppsById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      act(() => {
        result.current.setPage(2);
      });

      await waitFor(() => {
        expect(result.current.currentPage).toBe(2);
        expect(mockGetAppsByServiceId).toHaveBeenCalledWith(123, 2, '');
      });
    });

    test('should update search and reset page when setSearch is called', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result } = renderHook(() => useServiceAppsById(123, 5));

      await waitFor(() => expect(result.current.loading).toBe(false));

      act(() => {
        result.current.setSearch('updated search');
      });

      await waitFor(() => {
        expect(result.current.currentPage).toBe(0);
        expect(mockGetAppsByServiceId).toHaveBeenCalledWith(123, 0, 'updated search');
      });
    });

    test('should refetch apps when refetch is called', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result } = renderHook(() => useServiceAppsById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetAppsByServiceId).toHaveBeenCalledTimes(1);

      act(() => {
        result.current.refetch();
      });

      await waitFor(() => expect(mockGetAppsByServiceId).toHaveBeenCalledTimes(2));
    });

    test('should refetch when serviceId changes', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result, rerender } = renderHook(
        ({ serviceId }) => useServiceAppsById(serviceId),
        { initialProps: { serviceId: 123 as number | null } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetAppsByServiceId).toHaveBeenCalledTimes(1);

      rerender({ serviceId: 789 });

      await waitFor(() => expect(mockGetAppsByServiceId).toHaveBeenCalledTimes(2));
      expect(mockGetAppsByServiceId).toHaveBeenLastCalledWith(789, 0, '');
    });

    test('should stop fetching when serviceId changes to null', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result, rerender } = renderHook(
        ({ serviceId }) => useServiceAppsById(serviceId),
        { initialProps: { serviceId: 123 as number | null } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetAppsByServiceId).toHaveBeenCalledTimes(1);

      rerender({ serviceId: null });

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetAppsByServiceId).toHaveBeenCalledTimes(1);
    });

    test('should handle multiple page changes correctly', async () => {
      mockGetAppsByServiceId.mockResolvedValue(mockAppsData);

      const { result } = renderHook(() => useServiceAppsById(123));

      await waitFor(() => expect(result.current.loading).toBe(false));

      act(() => {
        result.current.setPage(1);
      });

      await waitFor(() => expect(result.current.currentPage).toBe(1));

      act(() => {
        result.current.setPage(2);
      });

      await waitFor(() => expect(result.current.currentPage).toBe(2));

      expect(mockGetAppsByServiceId).toHaveBeenCalledWith(123, 2, '');
    });
  });
});
