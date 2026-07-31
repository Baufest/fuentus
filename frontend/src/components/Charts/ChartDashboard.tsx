import React, { useState, useEffect } from 'react';
import { 
  loadDashboardDataFromCSV, 
  filterData
} from '../../data/dashboardData';
import { useDashboardFilters } from '../../hooks/useDashboardFiltersGlobal';
import { useDashboardTabs } from '../../hooks/useDashboardTabs';
import { initializeDashboardData } from '../../utils/dashboardUtils';
import DashboardCharts from './DashboardCharts';
import StatsCards from '../Cards/StatsCards';
import GoalsCard from '../Cards/GoalsCard';

const ChartDashboard: React.FC = () => {
  const [loading, setLoading] = useState(true);
  
  // Use global context for filters
  const { filters, handlers } = useDashboardFilters();
  const [data, setData] = useState<any[]>([]);
  const { 
    activeTab, 
    setActiveTab, 
    coverageData, 
    coverageLoading, 
    chimeraData, 
    chimeraLoading,
    nucleusCoverageData,
    nucleusCoverageLoading,
    selectedVertical,
    selectedUol2,
    selectedSn1,
    selectedSn2,
    loadCoverageData,
    loadChimeraData,
    loadNucleusCoverageData,
    handleNucleusCoverageItemClick,
    handleNucleusCoverageBackClick,
    resetNucleusCoverageData,
    deudaTecnicaVertical,
    deudaTecnicaFabrica,
    deudaTecnicaSn1,
    handleDeudaTecnicaDrillDown,
    handleDeudaTecnicaBackClick,
    resetDeudaTecnicaData
  } = useDashboardTabs();

  // Load initial data
  useEffect(() => {
    console.log('Loading initial dashboard data');
    const loadData = async () => {
      try {
        const csvData = await loadDashboardDataFromCSV();
        setData(csvData);
        const {
          periods,
          geographies,
          verticals,
          defaultPeriod
        } = initializeDashboardData(csvData);
        
        // Set initial options
        handlers.setOptions({
          availablePeriods: periods,
          availableGeographies: geographies,
          availableVerticals: verticals,
          availableUOL2: []
        });
        
        // Set default values
        handlers.setSelectedPeriod(defaultPeriod);
        
        setLoading(false);
      } catch (error) {
        console.error('Error loading data:', error);
        setLoading(false);
      }
    };

    loadData();
  }, []);

  // Load coverage data when tab changes
  useEffect(() => {
    if (filters.selectedUOL2.length > 0 && activeTab === 'coverage') {
      loadCoverageData(filters.selectedUOL2);
    }
  }, [filters.selectedUOL2, activeTab]);

  // Load chimera data when tab changes
  useEffect(() => {
    if (filters.selectedUOL2.length > 0 && activeTab === 'chimera') {
      loadChimeraData(filters.selectedUOL2);
    }
  }, [filters.selectedUOL2, activeTab]);

  // Load nucleus coverage data when filters change or tab changes
  useEffect(() => {
    if (activeTab === 'nucleus-coverage') {
      // Reset navigation when filters change
      resetNucleusCoverageData();
      
      // Determine which filters to use based on current selections
      const verticalFilter = filters.selectedVertical || undefined;
      const uol2Filter = filters.selectedUOL2.length === 1 ? filters.selectedUOL2[0] : undefined;
      
      // Load data with appropriate filters
      loadNucleusCoverageData(verticalFilter, uol2Filter);
    }
  }, [filters.selectedVertical, filters.selectedUOL2, activeTab]);

  // Reset deuda tecnica data when tab changes
  useEffect(() => {
    if (activeTab !== 'deuda-tecnica') {
      resetDeudaTecnicaData();
    }
  }, [activeTab]);

  // Handler functions are now provided by the hook

  // Filter data based on selected filters
  const filteredData = filterData(
    data, 
    filters.selectedPeriod, 
    filters.selectedGeography,
    filters.selectedUOL2, 
    filters.selectedVertical
  );

  const handleShowTable = () => {
    // This function can be used for chart click events if needed
    console.log('Chart data point clicked');
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-lg">Cargando dashboard...</div>
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-1 gap-4 md:gap-6 xl:grid-cols-4 2xl:gap-7.5 mb-6">
        {/* Goals Card */}
        <GoalsCard />
        
        {/* Stats Cards */}
        <StatsCards 
          filteredData={filteredData} 
          filters={{
            selectedVertical: filters.selectedVertical,
            selectedUOL2: filters.selectedUOL2
          }}
        />
      </div>

      {/* Tabs */}
      <div className="mb-6">
        <div className="border-b border-stroke dark:border-strokedark">
          <nav className="-mb-px flex space-x-8">
            <button
              onClick={() => setActiveTab('certificacion')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'certificacion'
                  ? 'border-primary text-primary'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Modelo SO-SN1
            </button>
            <button
              onClick={() => setActiveTab('coverage')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'coverage'
                  ? 'border-primary text-primary'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              SonarQube
            </button>
            <button
              onClick={() => setActiveTab('chimera')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'chimera'
                  ? 'border-primary text-primary'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Chimera
            </button>
            <button
              onClick={() => {
                if (activeTab !== 'nucleus-coverage') {
                  setActiveTab('nucleus-coverage');
                  // El useEffect se encargará de cargar los datos
                }
              }}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'nucleus-coverage'
                  ? 'border-primary text-primary'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Coverage por Nivel
            </button>
            <button
              onClick={() => setActiveTab('sistematica')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'sistematica'
                  ? 'border-primary text-primary'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Sistemática
            </button>
            <button
              onClick={() => setActiveTab('deuda-tecnica')}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'deuda-tecnica'
                  ? 'border-primary text-primary'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Deuda Técnica
            </button>
          </nav>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-12 gap-4 md:gap-6 2xl:gap-7.5">
        <DashboardCharts
          activeTab={activeTab}
          filteredData={filteredData}
          coverageData={coverageData}
          coverageLoading={coverageLoading}
          chimeraData={chimeraData}
          chimeraLoading={chimeraLoading}
          nucleusCoverageData={nucleusCoverageData}
          nucleusCoverageLoading={nucleusCoverageLoading}
          selectedVertical={selectedVertical}
          selectedUol2={selectedUol2}
          selectedSn1={selectedSn1}
          selectedSn2={selectedSn2}
          deudaTecnicaVertical={deudaTecnicaVertical}
          deudaTecnicaFabrica={deudaTecnicaFabrica}
          deudaTecnicaSn1={deudaTecnicaSn1}
          onDataPointSelection={handleShowTable}
          onNucleusCoverageItemClick={handleNucleusCoverageItemClick}
          onNucleusCoverageBackClick={handleNucleusCoverageBackClick}
          onDeudaTecnicaDrillDown={handleDeudaTecnicaDrillDown}
          onDeudaTecnicaBackClick={handleDeudaTecnicaBackClick}
        />
      </div>
    </>
  );
};

export default ChartDashboard;
