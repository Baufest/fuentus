import { renderHook, act } from '@testing-library/react';
import { DashboardFiltersProvider } from '../../contexts/DashboardFiltersContext';
import { useDashboardFilters } from '../../hooks/useDashboardFiltersGlobal';
import React from 'react';

// Wrapper component for testing the context
const ContextWrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <DashboardFiltersProvider>
    {children}
  </DashboardFiltersProvider>
);

describe('useDashboardFiltersGlobal', () => {
  it('should provide access to filters, options, actions and handlers', () => {
    const { result } = renderHook(() => useDashboardFilters(), {
      wrapper: ContextWrapper,
    });

    expect(result.current.filters).toBeDefined();
    expect(result.current.options).toBeDefined();
    expect(result.current.actions).toBeDefined();
    expect(result.current.handlers).toBeDefined();
  });

  it('should provide handler functions', () => {
    const { result } = renderHook(() => useDashboardFilters(), {
      wrapper: ContextWrapper,
    });

    expect(typeof result.current.handlers.handleGeographyChange).toBe('function');
    expect(typeof result.current.handlers.handleVerticalChange).toBe('function');
    expect(typeof result.current.handlers.setSelectedPeriod).toBe('function');
    expect(typeof result.current.handlers.setSelectedUOL2).toBe('function');
    expect(typeof result.current.handlers.setOptions).toBe('function');
    expect(typeof result.current.handlers.resetFilters).toBe('function');
  });

  it('should handle geography change events', () => {
    const { result } = renderHook(() => useDashboardFilters(), {
      wrapper: ContextWrapper,
    });

    const mockEvent = {
      target: { value: 'ESPAÑA' }
    } as React.ChangeEvent<HTMLSelectElement>;

    act(() => {
      result.current.handlers.handleGeographyChange(mockEvent);
    });

    expect(result.current.filters.selectedGeography).toBe('ESPAÑA');
  });

  it('should handle vertical change events', () => {
    const { result } = renderHook(() => useDashboardFilters(), {
      wrapper: ContextWrapper,
    });

    const mockEvent = {
      target: { value: 'INDIVIDUOS Y PYMES' }
    } as React.ChangeEvent<HTMLSelectElement>;

    act(() => {
      result.current.handlers.handleVerticalChange(mockEvent);
    });

    expect(result.current.filters.selectedVertical).toBe('INDIVIDUOS Y PYMES');
  });

  it('should provide direct access to actions', () => {
    const { result } = renderHook(() => useDashboardFilters(), {
      wrapper: ContextWrapper,
    });

    act(() => {
      result.current.actions.setSelectedPeriod('2024-01');
    });

    expect(result.current.filters.selectedPeriod).toBe('2024-01');
  });
});