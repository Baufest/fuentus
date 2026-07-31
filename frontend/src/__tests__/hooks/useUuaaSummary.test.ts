import { renderHook, act, waitFor } from '@testing-library/react';
import { useUuaaSummary } from '../../hooks/useUuaaSummary';
import { UuaaSummaryDTO } from '../../types/statsSummary';
import * as statsSummaryApi from '../../api/statsSummaryApi';

// Mock del módulo statsSummaryApi
jest.mock('../../api/statsSummaryApi', () => ({
  getUuaaSummary: jest.fn(),
}));

describe('useUuaaSummary', () => {
  const mockGetUuaaSummary = statsSummaryApi.getUuaaSummary as jest.MockedFunction<typeof statsSummaryApi.getUuaaSummary>;

  beforeEach(() => {
    jest.clearAllMocks();
    // Configure default resolved values to prevent timeouts
    mockGetUuaaSummary.mockResolvedValue(mockSummaryData);
  });

  const mockSummaryData: UuaaSummaryDTO = {
    uuaa: 'TEST',
    totalApps: 15,
    averageCoverage: 78.5,
    totalBugs: 8,
    totalSastLow: 3,
    totalSastMedium: 2,
    totalSastHigh: 1,
    totalScaLow: 2,
    totalScaMedium: 1,
    totalScaHigh: 0,
    totalScaCritical: 0,
    rfoId: 456,
    rfoEstado: 'Activo',
    totalSastVulnerabilities: 6,
    totalScaVulnerabilities: 3,
    totalVulnerabilities: 9,
  };

  describe('useUuaaSummary hook', () => {
    test('should initialize with loading state', () => {
      mockGetUuaaSummary.mockImplementation(() => new Promise(() => {}));
      const uuaa = 'TEST001';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      expect(result.current.loading).toBe(true);
      expect(result.current.summary).toBe(null);
      expect(result.current.error).toBe(null);
    });

    test('should fetch summary successfully', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);
      const uuaa = 'TEST001';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.summary).toEqual(mockSummaryData);
      expect(result.current.error).toBe(null);
      expect(mockGetUuaaSummary).toHaveBeenCalledWith('TEST');
    });

    test('should normalize UUAA to first 4 characters uppercase', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);
      const uuaa = 'test001abc';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetUuaaSummary).toHaveBeenCalledWith('TEST');
      expect(result.current.summary).toEqual(mockSummaryData);
    });

    test('should normalize lowercase UUAA to uppercase', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);
      const uuaa = 'abcd1234';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetUuaaSummary).toHaveBeenCalledWith('ABCD');
    });

    test('should handle error when fetching summary', async () => {
      const error = new Error('API Error');
      mockGetUuaaSummary.mockRejectedValue(error);
      const uuaa = 'TEST001';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.summary).toBe(null);
      expect(result.current.error).toEqual(error);
    });

    test('should handle non-Error exception', async () => {
      mockGetUuaaSummary.mockRejectedValue('String error');
      const uuaa = 'TEST001';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(result.current.error?.message).toBe('Error fetching UUAA summary');
    });

    test('should not fetch when uuaa is undefined', async () => {
      const { result } = renderHook(() => useUuaaSummary(undefined));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetUuaaSummary).not.toHaveBeenCalled();
      expect(result.current.summary).toBe(null);
      expect(result.current.error).toBe(null);
    });

    test('should refetch summary when refetch is called', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);
      const uuaa = 'TEST001';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));

      expect(mockGetUuaaSummary).toHaveBeenCalledTimes(1);

      act(() => {
        result.current.refetch();
      });

      await waitFor(() => expect(mockGetUuaaSummary).toHaveBeenCalledTimes(2));
    });

    test('should refetch when uuaa changes', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);

      const { result, rerender } = renderHook(
        ({ uuaa }) => useUuaaSummary(uuaa),
        { initialProps: { uuaa: 'TEST001' } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetUuaaSummary).toHaveBeenCalledTimes(1);
      expect(mockGetUuaaSummary).toHaveBeenLastCalledWith('TEST');

      rerender({ uuaa: 'DEMO999' });

      await waitFor(() => expect(mockGetUuaaSummary).toHaveBeenCalledTimes(2));
      expect(mockGetUuaaSummary).toHaveBeenLastCalledWith('DEMO');
    });

    test('should handle transition from defined to undefined uuaa', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);

      const { result, rerender } = renderHook(
        ({ uuaa }) => useUuaaSummary(uuaa),
        { initialProps: { uuaa: 'TEST001' as string | undefined } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetUuaaSummary).toHaveBeenCalledTimes(1);

      rerender({ uuaa: undefined });

      await waitFor(() => expect(result.current.loading).toBe(false));
      
      // Should not call the API when uuaa becomes undefined
      expect(mockGetUuaaSummary).toHaveBeenCalledTimes(1);
    });

    test('should handle transition from undefined to defined uuaa', async () => {
      mockGetUuaaSummary.mockResolvedValue(mockSummaryData);

      const { result, rerender } = renderHook(
        ({ uuaa }) => useUuaaSummary(uuaa),
        { initialProps: { uuaa: undefined as string | undefined } }
      );

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetUuaaSummary).not.toHaveBeenCalled();

      rerender({ uuaa: 'TEST001' });

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(mockGetUuaaSummary).toHaveBeenCalledTimes(1);
      expect(mockGetUuaaSummary).toHaveBeenCalledWith('TEST');
    });

    test('should clear error on successful refetch', async () => {
      // First call fails
      const error = new Error('Initial error');
      mockGetUuaaSummary.mockRejectedValueOnce(error);
      const uuaa = 'TEST001';

      const { result } = renderHook(() => useUuaaSummary(uuaa));

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(result.current.error).toEqual(error);
      expect(result.current.summary).toBe(null);

      // Second call succeeds
      mockGetUuaaSummary.mockResolvedValueOnce(mockSummaryData);

      act(() => {
        result.current.refetch();
      });

      await waitFor(() => expect(result.current.loading).toBe(false));
      expect(result.current.error).toBe(null);
      expect(result.current.summary).toEqual(mockSummaryData);
    });
  });
});
