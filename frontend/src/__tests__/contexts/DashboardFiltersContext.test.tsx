import { renderHook, act, waitFor } from '@testing-library/react';
import { render, screen } from '@testing-library/react';
import { DashboardFiltersProvider, useDashboardFiltersContext } from '../../contexts/DashboardFiltersContext';
import React from 'react';
import * as useComboValuesModule from '../../hooks/useComboValues';

// Mock the useComboValues hook
jest.mock('../../hooks/useComboValues');

const mockUseComboValues = useComboValuesModule.useComboValues as jest.MockedFunction<typeof useComboValuesModule.useComboValues>;

// Test component to verify provider functionality
const TestComponent: React.FC = () => {
  const { state, actions } = useDashboardFiltersContext();
  
  return (
    <div>
      <div data-testid="selected-period">{state.filters.selectedPeriod}</div>
      <div data-testid="selected-geography">{state.filters.selectedGeography}</div>
      <div data-testid="selected-vertical">{state.filters.selectedVertical}</div>
      <div data-testid="selected-uol2">{JSON.stringify(state.filters.selectedUOL2)}</div>
      <div data-testid="is-vertical-disabled">{state.filters.isVerticalDisabled.toString()}</div>
      <div data-testid="available-verticals">{JSON.stringify(state.options.availableVerticals)}</div>
      <div data-testid="available-uol2">{JSON.stringify(state.options.availableUOL2)}</div>
      
      <button 
        data-testid="set-period-btn"
        onClick={() => actions.setSelectedPeriod('2024-01')}
      >
        Set Period
      </button>
      <button 
        data-testid="set-geography-btn"
        onClick={() => actions.setSelectedGeography('ESPAÑA')}
      >
        Set Geography
      </button>
      <button 
        data-testid="set-vertical-btn"
        onClick={() => actions.setSelectedVertical('INDIVIDUOS Y PYMES')}
      >
        Set Vertical
      </button>
      <button 
        data-testid="reset-btn"
        onClick={() => actions.resetFilters()}
      >
        Reset
      </button>
    </div>
  );
};

// Wrapper component for testing the context
const ContextWrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <DashboardFiltersProvider>
    {children}
  </DashboardFiltersProvider>
);

describe('DashboardFiltersContext', () => {
  const mockRefetch = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    mockUseComboValues.mockReturnValue({
      data: null,
      loading: false,
      error: null,
      refetch: mockRefetch
    });
  });

  describe('Initial State', () => {
    it('should initialize with default values', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      expect(result.current.state.filters.selectedPeriod).toBe('');
      expect(result.current.state.filters.selectedGeography).toBe('ARGENTINA');
      expect(result.current.state.filters.selectedVertical).toBe('');
      expect(result.current.state.filters.selectedUOL2).toEqual([]);
      expect(result.current.state.filters.isVerticalDisabled).toBe(false);
      
      expect(result.current.state.options.availablePeriods).toEqual([]);
      expect(result.current.state.options.availableGeographies).toEqual([]);
      expect(result.current.state.options.availableVerticals).toEqual([]);
      expect(result.current.state.options.availableUOL2).toEqual([]);
    });

    it('should provide all action functions', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      expect(typeof result.current.actions.setSelectedPeriod).toBe('function');
      expect(typeof result.current.actions.setSelectedGeography).toBe('function');
      expect(typeof result.current.actions.setSelectedVertical).toBe('function');
      expect(typeof result.current.actions.setSelectedUOL2).toBe('function');
      expect(typeof result.current.actions.setOptions).toBe('function');
      expect(typeof result.current.actions.resetFilters).toBe('function');
    });
  });

  describe('Filter Updates', () => {
    it('should update selected period', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedPeriod('2024-01');
      });

      expect(result.current.state.filters.selectedPeriod).toBe('2024-01');
    });

    it('should update selected geography', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedGeography('ESPAÑA');
      });

      expect(result.current.state.filters.selectedGeography).toBe('ESPAÑA');
    });

    it('should update selected vertical', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedVertical('INDIVIDUOS Y PYMES');
      });

      expect(result.current.state.filters.selectedVertical).toBe('INDIVIDUOS Y PYMES');
    });

    it('should not update vertical when disabled', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      // First set vertical as disabled
      act(() => {
        result.current.dispatch({ type: 'SET_VERTICAL_DISABLED', payload: true });
      });

      // Try to set vertical - should not update
      act(() => {
        result.current.actions.setSelectedVertical('INDIVIDUOS Y PYMES');
      });

      expect(result.current.state.filters.selectedVertical).toBe('');
      expect(result.current.state.filters.isVerticalDisabled).toBe(true);
    });

    it('should clear UOL2 when vertical changes to a different value', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      // Set initial vertical and UOL2
      act(() => {
        result.current.actions.setSelectedVertical('VERTICAL_1');
        result.current.actions.setSelectedUOL2(['UOL2_1', 'UOL2_2']);
      });

      expect(result.current.state.filters.selectedUOL2).toEqual(['UOL2_1', 'UOL2_2']);

      // Change vertical - should clear UOL2
      act(() => {
        result.current.actions.setSelectedVertical('VERTICAL_2');
      });

      expect(result.current.state.filters.selectedVertical).toBe('VERTICAL_2');
      expect(result.current.state.filters.selectedUOL2).toEqual([]);
    });

    it('should keep UOL2 when setting the same vertical', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      // Set initial vertical and UOL2
      act(() => {
        result.current.actions.setSelectedVertical('VERTICAL_1');
        result.current.actions.setSelectedUOL2(['UOL2_1', 'UOL2_2']);
      });

      // Set the same vertical - should keep UOL2
      act(() => {
        result.current.actions.setSelectedVertical('VERTICAL_1');
      });

      expect(result.current.state.filters.selectedVertical).toBe('VERTICAL_1');
      expect(result.current.state.filters.selectedUOL2).toEqual(['UOL2_1', 'UOL2_2']);
    });

    it('should update selected UOL2', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedUOL2(['SYSTEMS ENGINEERING', 'DATA ANALYTICS']);
      });

      expect(result.current.state.filters.selectedUOL2).toEqual(['SYSTEMS ENGINEERING', 'DATA ANALYTICS']);
    });
  });

  describe('Options Updates', () => {
    it('should update options partially', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setOptions({
          availablePeriods: ['2024-01', '2024-02'],
          availableGeographies: ['ARGENTINA', 'ESPAÑA']
        });
      });

      expect(result.current.state.options.availablePeriods).toEqual(['2024-01', '2024-02']);
      expect(result.current.state.options.availableGeographies).toEqual(['ARGENTINA', 'ESPAÑA']);
      expect(result.current.state.options.availableVerticals).toEqual([]);
      expect(result.current.state.options.availableUOL2).toEqual([]);
    });

    it('should update all options', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      const completeOptions = {
        availablePeriods: ['2024-01', '2024-02'],
        availableGeographies: ['ARGENTINA', 'ESPAÑA'],
        availableVerticals: ['VERTICAL_1', 'VERTICAL_2'],
        availableUOL2: ['UOL2_1', 'UOL2_2']
      };

      act(() => {
        result.current.actions.setOptions(completeOptions);
      });

      expect(result.current.state.options).toEqual(completeOptions);
    });
  });

  describe('Reset Functionality', () => {
    it('should reset filters to initial state but keep default geography', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      // Set some values first
      act(() => {
        result.current.actions.setSelectedPeriod('2024-01');
        result.current.actions.setSelectedGeography('ESPAÑA');
        result.current.actions.setSelectedVertical('INDIVIDUOS Y PYMES');
        result.current.actions.setSelectedUOL2(['SYSTEMS ENGINEERING']);
        result.current.dispatch({ type: 'SET_VERTICAL_DISABLED', payload: true });
      });

      // Verify values were set
      expect(result.current.state.filters.selectedPeriod).toBe('2024-01');
      expect(result.current.state.filters.selectedGeography).toBe('ESPAÑA');
      expect(result.current.state.filters.selectedVertical).toBe('INDIVIDUOS Y PYMES');
      expect(result.current.state.filters.selectedUOL2).toEqual(['SYSTEMS ENGINEERING']);
      expect(result.current.state.filters.isVerticalDisabled).toBe(true);

      // Reset filters
      act(() => {
        result.current.actions.resetFilters();
      });

      expect(result.current.state.filters.selectedPeriod).toBe('');
      expect(result.current.state.filters.selectedGeography).toBe('ARGENTINA'); // Should keep default
      expect(result.current.state.filters.selectedVertical).toBe('');
      expect(result.current.state.filters.selectedUOL2).toEqual([]);
      expect(result.current.state.filters.isVerticalDisabled).toBe(false);
    });
  });

  describe('useComboValues Integration', () => {
    it('should call useComboValues with correct parameters', () => {
      renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      expect(mockUseComboValues).toHaveBeenCalledWith({ 
        vertical: '' 
      });
    });

    it('should update options when combo values data is loaded', async () => {
      const mockComboData = {
        verticals: ['VERTICAL_1', 'VERTICAL_2'],
        uol2Values: ['UOL2_1', 'UOL2_2'],
        sn1Values: ['SN1_1'],
        sn2Values: ['SN2_1']
      };

      mockUseComboValues.mockReturnValue({
        data: mockComboData,
        loading: false,
        error: null,
        refetch: mockRefetch
      });

      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      await waitFor(() => {
        expect(result.current.state.options.availableVerticals).toEqual(['VERTICAL_1', 'VERTICAL_2']);
        expect(result.current.state.options.availableUOL2).toEqual(['UOL2_1', 'UOL2_2']);
      });
    });

    it('should not update options when combo values are still loading', () => {
      mockUseComboValues.mockReturnValue({
        data: {
          verticals: ['VERTICAL_1'],
          uol2Values: ['UOL2_1'],
          sn1Values: ['SN1_1'],
          sn2Values: ['SN2_1']
        },
        loading: true,
        error: null,
        refetch: mockRefetch
      });

      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      expect(result.current.state.options.availableVerticals).toEqual([]);
      expect(result.current.state.options.availableUOL2).toEqual([]);
    });

    it('should call refetch when UOL2 changes', async () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedUOL2(['UOL2_1']);
      });

      await waitFor(() => {
        expect(mockRefetch).toHaveBeenCalled();
      });
    });

    it('should not call refetch when UOL2 is empty', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedUOL2([]);
      });

      expect(mockRefetch).not.toHaveBeenCalled();
    });

    it('should call useComboValues with updated vertical when vertical changes', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.actions.setSelectedVertical('NEW_VERTICAL');
      });

      expect(mockUseComboValues).toHaveBeenLastCalledWith({ 
        vertical: 'NEW_VERTICAL' 
      });
    });
  });

  describe('Reducer Edge Cases', () => {
    it('should handle unknown action types gracefully', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      const initialState = { ...result.current.state };

      act(() => {
        // @ts-expect-error - Testing unknown action type
        result.current.dispatch({ type: 'UNKNOWN_ACTION', payload: 'test' });
      });

      expect(result.current.state).toEqual(initialState);
    });

    it('should handle SET_VERTICAL_DISABLED action', () => {
      const { result } = renderHook(() => useDashboardFiltersContext(), {
        wrapper: ContextWrapper,
      });

      act(() => {
        result.current.dispatch({ type: 'SET_VERTICAL_DISABLED', payload: true });
      });

      expect(result.current.state.filters.isVerticalDisabled).toBe(true);

      act(() => {
        result.current.dispatch({ type: 'SET_VERTICAL_DISABLED', payload: false });
      });

      expect(result.current.state.filters.isVerticalDisabled).toBe(false);
    });
  });

  describe('Context Provider', () => {
    it('should throw error when hook is used outside of provider', () => {
      const consoleError = jest.spyOn(console, 'error').mockImplementation(() => {});
      
      expect(() => {
        renderHook(() => useDashboardFiltersContext());
      }).toThrow('useDashboardFiltersContext must be used within a DashboardFiltersProvider');

      consoleError.mockRestore();
    });

    it('should render provider with children correctly', () => {
      render(
        <ContextWrapper>
          <TestComponent />
        </ContextWrapper>
      );

      expect(screen.getByTestId('selected-geography')).toHaveTextContent('ARGENTINA');
      expect(screen.getByTestId('selected-period')).toHaveTextContent('');
      expect(screen.getByTestId('is-vertical-disabled')).toHaveTextContent('false');
    });
  });
});