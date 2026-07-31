import { useState, useEffect } from 'react';
import { DashboardDataRow, getUOL1ByVertical, getUOL1ByGeographyAndVertical, getUOL2ByVertical, getUOL2ByGeographyUOL1AndVertical } from '../data/dashboardData';

interface FilterState {
  selectedPeriod: string;
  selectedGeography: string;
  selectedVertical: string;
  selectedUOL1: string[];
  selectedUOL2: string[];
  isVerticalDisabled: boolean;
}

interface AvailableOptions {
  availablePeriods: string[];
  availableGeographies: string[];
  availableVerticals: string[];
  availableUOL1: string[];
  availableUOL2: string[];
}

export const useDashboardFilters = (data: DashboardDataRow[]) => {
  const [filters, setFilters] = useState<FilterState>({
    selectedPeriod: '',
    selectedGeography: 'ARGENTINA',
    selectedVertical: '',
    selectedUOL1: [],
    selectedUOL2: [],
    isVerticalDisabled: false,
  });

  const [options, setOptions] = useState<AvailableOptions>({
    availablePeriods: [],
    availableGeographies: [],
    availableVerticals: [],
    availableUOL1: [],
    availableUOL2: [],
  });

  // Update UOL1 options based on geography and vertical
  useEffect(() => {
    if (data.length > 0) {
      const newUOL1Options = getUOL1ByGeographyAndVertical(data, filters.selectedGeography, filters.selectedVertical);
      setOptions(prev => ({ ...prev, availableUOL1: newUOL1Options }));

      const validUOL1 = filters.selectedUOL1.filter(uol1 => newUOL1Options.includes(uol1));
      if (validUOL1.length !== filters.selectedUOL1.length) {
        setFilters(prev => ({ ...prev, selectedUOL1: validUOL1 }));
      }
    }
  }, [data, filters.selectedGeography, filters.selectedVertical]);

  // Update UOL2 options based on other filters
  useEffect(() => {
    if (data.length > 0) {
      const newUOL2Options = getUOL2ByGeographyUOL1AndVertical(
        data,
        filters.selectedGeography || undefined,
        filters.selectedUOL1.length > 0 ? filters.selectedUOL1 : undefined,
        filters.selectedVertical || undefined
      );
      setOptions(prev => ({ ...prev, availableUOL2: newUOL2Options }));

      const validUOL2 = filters.selectedUOL2.filter(uol2 => newUOL2Options.includes(uol2));
      if (validUOL2.length !== filters.selectedUOL2.length) {
        setFilters(prev => ({ ...prev, selectedUOL2: validUOL2 }));
      }
    }
  }, [data, filters.selectedGeography, filters.selectedUOL1, filters.selectedVertical]);

  // Update vertical disabled state based on UOL1 selection
  useEffect(() => {
    const systemsEngineeringAvailable = options.availableUOL1.includes('SYSTEMS ENGINEERING');

    if (filters.selectedUOL1.length === 0) {
      setFilters(prev => ({ ...prev, isVerticalDisabled: false }));
    } else if (!systemsEngineeringAvailable && filters.selectedVertical) {
      setFilters(prev => ({ 
        ...prev, 
        selectedVertical: '',
        isVerticalDisabled: true 
      }));
    } else {
      const hasSystemsEngineering = filters.selectedUOL1.includes('SYSTEMS ENGINEERING');
      if (!hasSystemsEngineering && filters.selectedVertical) {
        setFilters(prev => ({ 
          ...prev, 
          selectedVertical: '',
          isVerticalDisabled: true 
        }));
      }
    }
  }, [filters.selectedUOL1, options.availableUOL1, filters.selectedVertical]);

  const handleGeographyChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newGeography = e.target.value;
    setFilters(prev => ({ ...prev, selectedGeography: newGeography }));
    
    if (!newGeography) {
      setOptions(prev => ({
        ...prev,
        availableUOL1: getUOL1ByVertical(data, filters.selectedVertical),
        availableUOL2: getUOL2ByVertical(data, filters.selectedVertical)
      }));
    }
  };

  const handleVerticalChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const newVertical = e.target.value;
    
    if (!filters.isVerticalDisabled) {
      setFilters(prev => ({ ...prev, selectedVertical: newVertical }));
      
      if (!newVertical) {
        setOptions(prev => ({
          ...prev,
          availableUOL1: getUOL1ByGeographyAndVertical(data, filters.selectedGeography, undefined),
          availableUOL2: getUOL2ByGeographyUOL1AndVertical(
            data, 
            filters.selectedGeography || undefined, 
            filters.selectedUOL1.length > 0 ? filters.selectedUOL1 : undefined,
            undefined
          )
        }));
      }
    }
  };

  const setSelectedPeriod = (period: string) => {
    setFilters(prev => ({ ...prev, selectedPeriod: period }));
  };

  const setSelectedUOL1 = (uol1: string[]) => {
    setFilters(prev => ({ ...prev, selectedUOL1: uol1 }));
  };

  const setSelectedUOL2 = (uol2: string[]) => {
    setFilters(prev => ({ ...prev, selectedUOL2: uol2 }));
  };

  const setOptions_helper = (newOptions: Partial<AvailableOptions>) => {
    setOptions(prev => ({ ...prev, ...newOptions }));
  };

  return {
    filters,
    options,
    handlers: {
      handleGeographyChange,
      handleVerticalChange,
      setSelectedPeriod,
      setSelectedUOL1,
      setSelectedUOL2,
      setOptions: setOptions_helper,
    }
  };
};
