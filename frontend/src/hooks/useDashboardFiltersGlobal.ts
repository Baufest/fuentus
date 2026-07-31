import { useDashboardFiltersContext } from '../contexts/DashboardFiltersContext';

/**
 * Convenient hook that provides access to the global dashboard filters
 * This is a simplified interface to the DashboardFiltersContext
 */
export const useDashboardFilters = () => {
  const { state, actions } = useDashboardFiltersContext();
  const { filters, options } = state;

  // Create handler functions for form events
  const handleGeographyChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    actions.setSelectedGeography(e.target.value);
  };

  const handleVerticalChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    actions.setSelectedVertical(e.target.value);
  };

  return {
    // State
    filters,
    options,
    // Actions (for direct calls)
    actions,
    
    // Handlers (for form events)
    handlers: {
      handleGeographyChange,
      handleVerticalChange,
      setSelectedPeriod: actions.setSelectedPeriod,
      setSelectedUOL2: actions.setSelectedUOL2,
      setOptions: actions.setOptions,
      resetFilters: actions.resetFilters,
    }
  };
};

export default useDashboardFilters;