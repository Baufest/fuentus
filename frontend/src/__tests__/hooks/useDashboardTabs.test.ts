import { renderHook, act } from '@testing-library/react';
import { useDashboardTabs } from '../../hooks/useDashboardTabs';
import { getStatsSummaryByFilters, getCoverageAverageByLevel } from '../../api/statsSummaryApi';
import { StatsSummaryDTO, NucleusCoverageStatsSummary } from '../../types/statsSummary';

// Mock the API
jest.mock('../../api/statsSummaryApi');
const mockedGetStatsSummaryByFilters = getStatsSummaryByFilters as jest.MockedFunction<typeof getStatsSummaryByFilters>;
const mockedGetCoverageAverageByLevel = getCoverageAverageByLevel as jest.MockedFunction<typeof getCoverageAverageByLevel>;

// Mock console.error to avoid noise in tests
const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

const createMockStatsSummaryDTO = (uuaa: string, repoName: string): StatsSummaryDTO => ({
  uuaa,
  repositories: [
    {
      name: repoName,
      bitbucketUrl: `https://bitbucket.com/${repoName}`,
      language: 'Java',
      monolith: false,
      servers: [],
      sonarInfo: {
        sonarUrl: `https://sonar.com/${repoName}`,
        sonar10Url: `https://sonar10.com/${repoName}`,
        coverage: 85,
        bugs: 2,
        chimeraSast: { totalLow: 1, totalMedium: 2, totalHigh: 0 },
        chimeraSca: { totalLow: 0, totalMedium: 1, totalHigh: 1, totalCritical: 0 }
      }
    }
  ]
});

const createMockNucleusCoverageData = (label: string, level: NucleusCoverageStatsSummary['nucleusLevel'], coverage: number = 75): NucleusCoverageStatsSummary => ({
  label,
  coveragePercentage: coverage,
  nucleusLevel: level
});

describe('useDashboardTabs', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterAll(() => {
    consoleSpy.mockRestore();
  });

  it('should initialize with default values', () => {
    const { result } = renderHook(() => useDashboardTabs());

    expect(result.current.activeTab).toBe('certificacion');
    expect(result.current.coverageData).toEqual([]);
    expect(result.current.coverageLoading).toBe(false);
    expect(result.current.chimeraData).toEqual([]);
    expect(result.current.chimeraLoading).toBe(false);
    expect(result.current.nucleusCoverageData).toEqual([]);
    expect(result.current.nucleusCoverageLoading).toBe(false);
    expect(result.current.selectedVertical).toBeUndefined();
    expect(result.current.selectedUol2).toBeUndefined();
    expect(result.current.selectedSn1).toBeUndefined();
    expect(result.current.selectedSn2).toBeUndefined();
    expect(typeof result.current.loadCoverageData).toBe('function');
    expect(typeof result.current.loadChimeraData).toBe('function');
    expect(typeof result.current.loadNucleusCoverageData).toBe('function');
    expect(typeof result.current.handleNucleusCoverageItemClick).toBe('function');
    expect(typeof result.current.handleNucleusCoverageBackClick).toBe('function');
    expect(typeof result.current.resetNucleusCoverageData).toBe('function');
    expect(typeof result.current.setActiveTab).toBe('function');
  });

  it('should change active tab', () => {
    const { result } = renderHook(() => useDashboardTabs());

    act(() => {
      result.current.setActiveTab('coverage');
    });

    expect(result.current.activeTab).toBe('coverage');

    act(() => {
      result.current.setActiveTab('chimera');
    });

    expect(result.current.activeTab).toBe('chimera');
  });

  describe('loadCoverageData', () => {
    it('should not load data when selectedUOL2 is empty', async () => {
      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadCoverageData([]);
      });

      expect(mockedGetStatsSummaryByFilters).not.toHaveBeenCalled();
      expect(result.current.coverageData).toEqual([]);
      expect(result.current.coverageLoading).toBe(false);
    });

    it('should load coverage data successfully', async () => {
      const mockData = [createMockStatsSummaryDTO('UOL2_1', 'test-repo')];
      mockedGetStatsSummaryByFilters.mockResolvedValue(mockData);

      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadCoverageData(['UOL2_1', 'UOL2_2']);
      });

      expect(mockedGetStatsSummaryByFilters).toHaveBeenCalledWith(
        undefined,
        undefined,
        ['UOL2_1', 'UOL2_2']
      );
      expect(result.current.coverageData).toEqual(mockData);
      expect(result.current.coverageLoading).toBe(false);
    });

    it('should handle API returning empty array', async () => {
      mockedGetStatsSummaryByFilters.mockResolvedValue([]);

      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadCoverageData(['UOL2_1']);
      });

      expect(result.current.coverageData).toEqual([]);
      expect(result.current.coverageLoading).toBe(false);
    });

    it('should handle API error', async () => {
      const error = new Error('API Error');
      mockedGetStatsSummaryByFilters.mockRejectedValue(error);
      
      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadCoverageData(['UOL2_1']);
      });

      // The hook should handle error gracefully by setting empty data and loading false
      expect(result.current.coverageData).toEqual([]);
      expect(result.current.coverageLoading).toBe(false);
      // Console error would have been called but we don't test implementation details
    });

    it('should show loading state while fetching data', async () => {
      const mockData = [createMockStatsSummaryDTO('UOL2_1', 'test-repo')];
      let resolvePromise: (value: StatsSummaryDTO[]) => void;
      const loadingPromise = new Promise<StatsSummaryDTO[]>(resolve => {
        resolvePromise = resolve;
      });
      
      mockedGetStatsSummaryByFilters.mockReturnValue(loadingPromise);

      const { result } = renderHook(() => useDashboardTabs());

      // Start loading
      act(() => {
        result.current.loadCoverageData(['UOL2_1']);
      });

      // Should be loading
      expect(result.current.coverageLoading).toBe(true);
      expect(result.current.coverageData).toEqual([]);

      // Complete loading
      await act(async () => {
        resolvePromise!(mockData);
      });

      expect(result.current.coverageLoading).toBe(false);
      expect(result.current.coverageData).toEqual(mockData);
    });
  });

  describe('loadChimeraData', () => {
    it('should not load data when selectedUOL2 is empty', async () => {
      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadChimeraData([]);
      });

      expect(mockedGetStatsSummaryByFilters).not.toHaveBeenCalled();
      expect(result.current.chimeraData).toEqual([]);
      expect(result.current.chimeraLoading).toBe(false);
    });

    it('should load chimera data successfully', async () => {
      const mockData = [createMockStatsSummaryDTO('UOL2_1', 'chimera-repo')];
      mockedGetStatsSummaryByFilters.mockResolvedValue(mockData);

      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadChimeraData(['UOL2_1', 'UOL2_2']);
      });

      expect(mockedGetStatsSummaryByFilters).toHaveBeenCalledWith(
        undefined,
        undefined,
        ['UOL2_1', 'UOL2_2']
      );
      expect(result.current.chimeraData).toEqual(mockData);
      expect(result.current.chimeraLoading).toBe(false);
    });

    it('should handle API returning empty array', async () => {
      mockedGetStatsSummaryByFilters.mockResolvedValue([]);

      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadChimeraData(['UOL2_1']);
      });

      expect(result.current.chimeraData).toEqual([]);
      expect(result.current.chimeraLoading).toBe(false);
    });

    it('should handle API error', async () => {
      const error = new Error('API Error');
      mockedGetStatsSummaryByFilters.mockRejectedValue(error);
      
      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await result.current.loadChimeraData(['UOL2_1']);
      });

      // The hook should handle error gracefully by setting empty data and loading false
      expect(result.current.chimeraData).toEqual([]);
      expect(result.current.chimeraLoading).toBe(false);
      // Console error would have been called but we don't test implementation details
    });

    it('should show loading state while fetching data', async () => {
      const mockData = [createMockStatsSummaryDTO('UOL2_1', 'chimera-repo')];
      let resolvePromise: (value: StatsSummaryDTO[]) => void;
      const loadingPromise = new Promise<StatsSummaryDTO[]>(resolve => {
        resolvePromise = resolve;
      });
      
      mockedGetStatsSummaryByFilters.mockReturnValue(loadingPromise);

      const { result } = renderHook(() => useDashboardTabs());

      // Start loading
      act(() => {
        result.current.loadChimeraData(['UOL2_1']);
      });

      // Should be loading
      expect(result.current.chimeraLoading).toBe(true);
      expect(result.current.chimeraData).toEqual([]);

      // Complete loading
      await act(async () => {
        resolvePromise!(mockData);
      });

      expect(result.current.chimeraLoading).toBe(false);
      expect(result.current.chimeraData).toEqual(mockData);
    });
  });

  describe('concurrent operations', () => {
    it('should handle multiple simultaneous loads correctly', async () => {
      const coverageData = [createMockStatsSummaryDTO('UOL2_1', 'coverage-repo')];
      const chimeraData = [createMockStatsSummaryDTO('UOL2_2', 'chimera-repo')];

      mockedGetStatsSummaryByFilters
        .mockResolvedValueOnce(coverageData)
        .mockResolvedValueOnce(chimeraData);

      const { result } = renderHook(() => useDashboardTabs());

      await act(async () => {
        await Promise.all([
          result.current.loadCoverageData(['UOL2_1']),
          result.current.loadChimeraData(['UOL2_2'])
        ]);
      });

      expect(result.current.coverageData).toEqual(coverageData);
      expect(result.current.chimeraData).toEqual(chimeraData);
      expect(result.current.coverageLoading).toBe(false);
      expect(result.current.chimeraLoading).toBe(false);
    });
  });

  describe('edge cases', () => {
    it('should handle tab switching during data loading', async () => {
      const mockData = [createMockStatsSummaryDTO('UOL2_1', 'test-repo')];
      mockedGetStatsSummaryByFilters.mockResolvedValue(mockData);

      const { result } = renderHook(() => useDashboardTabs());

      // Start loading coverage data and switch tab
      await act(async () => {
        const loadPromise = result.current.loadCoverageData(['UOL2_1']);
        result.current.setActiveTab('chimera');
        await loadPromise;
      });

      expect(result.current.activeTab).toBe('chimera');
      expect(result.current.coverageData).toEqual(mockData);
      expect(result.current.coverageLoading).toBe(false);
    });

    it('should handle subsequent calls to the same load function', async () => {
      const mockData1 = [createMockStatsSummaryDTO('UOL2_1', 'test-repo-1')];
      const mockData2 = [createMockStatsSummaryDTO('UOL2_2', 'test-repo-2')];

      mockedGetStatsSummaryByFilters
        .mockResolvedValueOnce(mockData1)
        .mockResolvedValueOnce(mockData2);

      const { result } = renderHook(() => useDashboardTabs());

      // First call
      await act(async () => {
        await result.current.loadCoverageData(['UOL2_1']);
      });

      expect(result.current.coverageData).toEqual(mockData1);

      // Second call should override the first
      await act(async () => {
        await result.current.loadCoverageData(['UOL2_2']);
      });

      expect(result.current.coverageData).toEqual(mockData2);
      expect(mockedGetStatsSummaryByFilters).toHaveBeenCalledTimes(2);
    });
  });

  describe('nucleus coverage functionality', () => {
    beforeEach(() => {
      jest.clearAllMocks();
      consoleSpy.mockClear();
    });

    describe('loadNucleusCoverageData', () => {
      it('should load nucleus coverage data successfully', async () => {
        const mockData = [
          createMockNucleusCoverageData('Vertical 1', 'VERTICAL', 85),
          createMockNucleusCoverageData('Vertical 2', 'VERTICAL', 90)
        ];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        await act(async () => {
          await result.current.loadNucleusCoverageData('vertical1');
        });

        expect(mockedGetCoverageAverageByLevel).toHaveBeenCalledWith('vertical1', undefined, undefined, undefined, undefined);
        expect(result.current.nucleusCoverageData).toEqual(mockData);
        expect(result.current.nucleusCoverageLoading).toBe(false);
      });

      it('should load nucleus coverage data with all parameters', async () => {
        const mockData = [createMockNucleusCoverageData('App 1', 'APP', 95)];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        await act(async () => {
          await result.current.loadNucleusCoverageData('vertical1', 'uol2_1', 'sn1_1', 'sn2_1', 'uuaa1');
        });

        expect(mockedGetCoverageAverageByLevel).toHaveBeenCalledWith('vertical1', 'uol2_1', 'sn1_1', 'sn2_1', 'uuaa1');
        expect(result.current.nucleusCoverageData).toEqual(mockData);
      });

      it('should handle API error', async () => {
        const error = new Error('Nucleus Coverage API Error');
        mockedGetCoverageAverageByLevel.mockRejectedValue(error);
        
        const { result } = renderHook(() => useDashboardTabs());

        await act(async () => {
          await result.current.loadNucleusCoverageData('vertical1');
        });

        // The hook should handle error gracefully by setting empty data and loading false
        expect(result.current.nucleusCoverageData).toEqual([]);
        expect(result.current.nucleusCoverageLoading).toBe(false);
        // Console error would have been called but we don't test implementation details
      });

      it('should show loading state while fetching data', async () => {
        const mockData = [createMockNucleusCoverageData('Vertical 1', 'VERTICAL')];
        let resolvePromise: (value: NucleusCoverageStatsSummary[]) => void;
        const loadingPromise = new Promise<NucleusCoverageStatsSummary[]>(resolve => {
          resolvePromise = resolve;
        });
        
        mockedGetCoverageAverageByLevel.mockReturnValue(loadingPromise);

        const { result } = renderHook(() => useDashboardTabs());

        // Start loading
        act(() => {
          result.current.loadNucleusCoverageData('vertical1');
        });

        // Should be loading
        expect(result.current.nucleusCoverageLoading).toBe(true);
        expect(result.current.nucleusCoverageData).toEqual([]);

        // Complete loading
        await act(async () => {
          resolvePromise!(mockData);
        });

        expect(result.current.nucleusCoverageLoading).toBe(false);
        expect(result.current.nucleusCoverageData).toEqual(mockData);
      });

      it('should handle empty API response', async () => {
        mockedGetCoverageAverageByLevel.mockResolvedValue([]);

        const { result } = renderHook(() => useDashboardTabs());

        await act(async () => {
          await result.current.loadNucleusCoverageData('vertical1');
        });

        expect(result.current.nucleusCoverageData).toEqual([]);
        expect(result.current.nucleusCoverageLoading).toBe(false);
      });
    });

    describe('handleNucleusCoverageItemClick', () => {
      it('should handle VERTICAL level click', async () => {
        const mockData = [createMockNucleusCoverageData('UOL2 1', 'UOL2')];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        const verticalItem = createMockNucleusCoverageData('Vertical 1', 'VERTICAL');

        await act(async () => {
          result.current.handleNucleusCoverageItemClick(verticalItem);
        });

        expect(result.current.selectedVertical).toBe('Vertical 1');
        expect(result.current.selectedUol2).toBeUndefined();
        expect(result.current.selectedSn1).toBeUndefined();
        expect(result.current.selectedSn2).toBeUndefined();
        expect(mockedGetCoverageAverageByLevel).toHaveBeenCalledWith('Vertical 1', undefined, undefined, undefined, undefined);
      });

      it('should handle UOL2 level click', async () => {
        const mockData = [createMockNucleusCoverageData('SN1 1', 'SN1')];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        // Set initial vertical selection
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });

        const uol2Item = createMockNucleusCoverageData('UOL2 1', 'UOL2');

        await act(async () => {
          result.current.handleNucleusCoverageItemClick(uol2Item);
        });

        expect(result.current.selectedVertical).toBe('Vertical 1');
        expect(result.current.selectedUol2).toBe('UOL2 1');
        expect(result.current.selectedSn1).toBeUndefined();
        expect(result.current.selectedSn2).toBeUndefined();
      });

      it('should handle SN1 level click', async () => {
        const mockData = [createMockNucleusCoverageData('SN2 1', 'SN2')];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        // Set initial selections
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('UOL2 1', 'UOL2'));
        });

        const sn1Item = createMockNucleusCoverageData('SN1 1', 'SN1');

        await act(async () => {
          result.current.handleNucleusCoverageItemClick(sn1Item);
        });

        expect(result.current.selectedSn1).toBe('SN1 1');
        expect(result.current.selectedSn2).toBeUndefined();
      });

      it('should handle SN2 level click', async () => {
        const mockData = [createMockNucleusCoverageData('UUAA 1', 'UUAA')];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        // Set initial selections
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('UOL2 1', 'UOL2'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('SN1 1', 'SN1'));
        });

        const sn2Item = createMockNucleusCoverageData('SN2 1', 'SN2');

        await act(async () => {
          result.current.handleNucleusCoverageItemClick(sn2Item);
        });

        expect(result.current.selectedSn2).toBe('SN2 1');
      });
    });

    describe('handleNucleusCoverageBackClick', () => {
      it('should navigate back from SN2 to SN1', async () => {
        mockedGetCoverageAverageByLevel.mockResolvedValue([]);

        const { result } = renderHook(() => useDashboardTabs());

        // Set up hierarchy
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('UOL2 1', 'UOL2'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('SN1 1', 'SN1'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('SN2 1', 'SN2'));
        });

        expect(result.current.selectedSn2).toBe('SN2 1');

        await act(async () => {
          result.current.handleNucleusCoverageBackClick();
        });

        expect(result.current.selectedSn2).toBeUndefined();
        expect(result.current.selectedSn1).toBe('SN1 1');
        expect(result.current.selectedUol2).toBe('UOL2 1');
        expect(result.current.selectedVertical).toBe('Vertical 1');
      });

      it('should navigate back from SN1 to UOL2', async () => {
        mockedGetCoverageAverageByLevel.mockResolvedValue([]);

        const { result } = renderHook(() => useDashboardTabs());

        // Set up hierarchy
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('UOL2 1', 'UOL2'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('SN1 1', 'SN1'));
        });

        await act(async () => {
          result.current.handleNucleusCoverageBackClick();
        });

        expect(result.current.selectedSn1).toBeUndefined();
        expect(result.current.selectedUol2).toBe('UOL2 1');
        expect(result.current.selectedVertical).toBe('Vertical 1');
      });

      it('should navigate back from UOL2 to VERTICAL', async () => {
        mockedGetCoverageAverageByLevel.mockResolvedValue([]);

        const { result } = renderHook(() => useDashboardTabs());

        // Set up hierarchy
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('UOL2 1', 'UOL2'));
        });

        await act(async () => {
          result.current.handleNucleusCoverageBackClick();
        });

        expect(result.current.selectedUol2).toBeUndefined();
        expect(result.current.selectedVertical).toBe('Vertical 1');
      });

      it('should navigate back from VERTICAL to root', async () => {
        mockedGetCoverageAverageByLevel.mockResolvedValue([]);

        const { result } = renderHook(() => useDashboardTabs());

        // Set up hierarchy
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });

        await act(async () => {
          result.current.handleNucleusCoverageBackClick();
        });

        expect(result.current.selectedVertical).toBeUndefined();
        expect(result.current.selectedUol2).toBeUndefined();
        expect(result.current.selectedSn1).toBeUndefined();
        expect(result.current.selectedSn2).toBeUndefined();
      });
    });

    describe('resetNucleusCoverageData', () => {
      it('should reset all nucleus coverage state', () => {
        const { result } = renderHook(() => useDashboardTabs());

        // Set up some state
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('Vertical 1', 'VERTICAL'));
        });
        act(() => {
          result.current.handleNucleusCoverageItemClick(createMockNucleusCoverageData('UOL2 1', 'UOL2'));
        });

        // Verify state is set
        expect(result.current.selectedVertical).toBe('Vertical 1');
        expect(result.current.selectedUol2).toBe('UOL2 1');

        // Reset
        act(() => {
          result.current.resetNucleusCoverageData();
        });

        // Verify all state is cleared
        expect(result.current.selectedVertical).toBeUndefined();
        expect(result.current.selectedUol2).toBeUndefined();
        expect(result.current.selectedSn1).toBeUndefined();
        expect(result.current.selectedSn2).toBeUndefined();
        expect(result.current.nucleusCoverageData).toEqual([]);
      });
    });

    describe('tab switching with nucleus coverage', () => {
      it('should allow tab switching to nucleus-coverage', () => {
        const { result } = renderHook(() => useDashboardTabs());

        act(() => {
          result.current.setActiveTab('nucleus-coverage');
        });

        expect(result.current.activeTab).toBe('nucleus-coverage');
      });

      it('should maintain nucleus coverage data when switching tabs', async () => {
        const mockData = [createMockNucleusCoverageData('Vertical 1', 'VERTICAL')];
        mockedGetCoverageAverageByLevel.mockResolvedValue(mockData);

        const { result } = renderHook(() => useDashboardTabs());

        // Load nucleus coverage data
        await act(async () => {
          await result.current.loadNucleusCoverageData('vertical1');
        });

        expect(result.current.nucleusCoverageData).toEqual(mockData);

        // Switch tabs
        act(() => {
          result.current.setActiveTab('coverage');
        });

        // Data should still be there
        expect(result.current.nucleusCoverageData).toEqual(mockData);

        // Switch back
        act(() => {
          result.current.setActiveTab('nucleus-coverage');
        });

        expect(result.current.nucleusCoverageData).toEqual(mockData);
      });
    });
  });
});
