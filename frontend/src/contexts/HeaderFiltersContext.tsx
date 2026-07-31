import React, { createContext, useContext, useState, ReactNode } from 'react';

interface HeaderFiltersContextType {
  selectedSn1: string;
  selectedSn2: string;
  selectedServerName: string;
  setSelectedSn1: (value: string) => void;
  setSelectedSn2: (value: string) => void;
  setSelectedServerName: (value: string) => void;
  clearFilters: () => void;
}

const HeaderFiltersContext = createContext<HeaderFiltersContextType | undefined>(undefined);

export const HeaderFiltersProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [selectedSn1, setSelectedSn1] = useState('');
  const [selectedSn2, setSelectedSn2] = useState('');
  const [selectedServerName, setSelectedServerName] = useState('');

  const clearFilters = () => {
    setSelectedSn1('');
    setSelectedSn2('');
    setSelectedServerName('');
  };

  return (
    <HeaderFiltersContext.Provider
      value={{
        selectedSn1,
        selectedSn2,
        selectedServerName,
        setSelectedSn1,
        setSelectedSn2,
        setSelectedServerName,
        clearFilters,
      }}
    >
      {children}
    </HeaderFiltersContext.Provider>
  );
};

export const useHeaderFilters = (): HeaderFiltersContextType => {
  const context = useContext(HeaderFiltersContext);
  if (!context) {
    throw new Error('useHeaderFilters must be used within HeaderFiltersProvider');
  }
  return context;
};
