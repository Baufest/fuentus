import React, { createContext, useContext, useEffect, useReducer } from 'react';
import { useComboValues } from '../hooks/useComboValues';

interface FilterState {
  selectedPeriod: string;
  selectedGeography: string;
  selectedVertical: string;
  selectedUOL2: string[];
  isVerticalDisabled: boolean;
}

interface AvailableOptions {
  availablePeriods: string[];
  availableGeographies: string[];
  availableVerticals: string[];
  availableUOL2: string[];
}

interface DashboardFiltersState {
  filters: FilterState;
  options: AvailableOptions;
}

type DashboardFiltersAction =
  | { type: 'SET_PERIOD'; payload: string }
  | { type: 'SET_GEOGRAPHY'; payload: string }
  | { type: 'SET_VERTICAL'; payload: string }
  | { type: 'SET_UOL2'; payload: string[] }
  | { type: 'SET_OPTIONS'; payload: Partial<AvailableOptions> }
  | { type: 'SET_VERTICAL_DISABLED'; payload: boolean }
  | { type: 'RESET_FILTERS' };

interface DashboardFiltersContextType {
  state: DashboardFiltersState;
  dispatch: React.Dispatch<DashboardFiltersAction>;
  actions: {
    setSelectedPeriod: (period: string) => void;
    setSelectedGeography: (geography: string) => void;
    setSelectedVertical: (vertical: string) => void;
    setSelectedUOL2: (uol2: string[]) => void;
    setOptions: (options: Partial<AvailableOptions>) => void;
    resetFilters: () => void;
  };
}

const initialState: DashboardFiltersState = {
  filters: {
    selectedPeriod: '',
    selectedGeography: 'ARGENTINA',
    selectedVertical: '',
    selectedUOL2: [],
    isVerticalDisabled: false,
  },
  options: {
    availablePeriods: [],
    availableGeographies: [],
    availableVerticals: [],
    availableUOL2: [],
  }
};

function dashboardFiltersReducer(
  state: DashboardFiltersState,
  action: DashboardFiltersAction
): DashboardFiltersState {
  switch (action.type) {
    case 'SET_PERIOD':
      return {
        ...state,
        filters: {
          ...state.filters,
          selectedPeriod: action.payload,
        },
      };

    case 'SET_GEOGRAPHY':
      return {
        ...state,
        filters: {
          ...state.filters,
          selectedGeography: action.payload,
        },
      };

    case 'SET_VERTICAL':
      return {
        ...state,
        filters: {
          ...state.filters,
          selectedVertical: action.payload,
          selectedUOL2: action.payload !== state.filters.selectedVertical ? [] : state.filters.selectedUOL2,
        },
      };

    case 'SET_UOL2':
      return {
        ...state,
        filters: {
          ...state.filters,
          selectedUOL2: action.payload,
        },
      };

    case 'SET_OPTIONS':
      return {
        ...state,
        options: {
          ...state.options,
          ...action.payload,
        },
      };

    case 'SET_VERTICAL_DISABLED':
      return {
        ...state,
        filters: {
          ...state.filters,
          isVerticalDisabled: action.payload,
        },
      };

    case 'RESET_FILTERS':
      return {
        ...state,
        filters: {
          ...initialState.filters,
          selectedGeography: 'ARGENTINA', // Keep default geography
        },
      };

    default:
      return state;
  }
}

const DashboardFiltersContext = createContext<DashboardFiltersContextType | undefined>(undefined);

export const DashboardFiltersProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(dashboardFiltersReducer, initialState);
  
  // Determine which parameters to send to useComboValues based on current filters

  const { data: comboValuesData, loading: comboValuesLoading, refetch } = useComboValues({ 
    vertical: state.filters.selectedVertical
  });

  // Update combo options when API data is loaded
  useEffect(() => {
    if (comboValuesData && !comboValuesLoading) {
      dispatch({ 
        type: 'SET_OPTIONS', 
        payload: { 
          availableVerticals: comboValuesData.verticals,
          availableUOL2: comboValuesData.uol2Values
        } 
      });
    }
  }, [comboValuesData, comboValuesLoading]);

  // Refetch combo values when UOL2 changes
  useEffect(() => {
    if (state.filters.selectedUOL2.length > 0) {
      refetch();
    }
  }, [state.filters.selectedUOL2, refetch]);

  const actions = {
    setSelectedPeriod: (period: string) => {
      dispatch({ type: 'SET_PERIOD', payload: period });
    },
    setSelectedGeography: (geography: string) => {
      dispatch({ type: 'SET_GEOGRAPHY', payload: geography });
    },
    setSelectedVertical: (vertical: string) => {
      if (!state.filters.isVerticalDisabled) {
        dispatch({ type: 'SET_VERTICAL', payload: vertical });
      }
    },
    setSelectedUOL2: (uol2: string[]) => {
      dispatch({ type: 'SET_UOL2', payload: uol2 });
    },
    setOptions: (options: Partial<AvailableOptions>) => {
      dispatch({ type: 'SET_OPTIONS', payload: options });
    },
    resetFilters: () => {
      dispatch({ type: 'RESET_FILTERS' });
    },
  };

  const contextValue: DashboardFiltersContextType = {
    state,
    dispatch,
    actions,
  };

  return (
    <DashboardFiltersContext.Provider value={contextValue}>
      {children}
    </DashboardFiltersContext.Provider>
  );
};

export const useDashboardFiltersContext = (): DashboardFiltersContextType => {
  const context = useContext(DashboardFiltersContext);
  if (context === undefined) {
    throw new Error('useDashboardFiltersContext must be used within a DashboardFiltersProvider');
  }
  return context;
};

export default DashboardFiltersContext;