import { ComboValuesDTO } from '../../types/nucleus';
import { useDashboardFilters } from '../../hooks/useDashboardFiltersGlobal';

interface FilterPanelProps {
  selectedSn1: string;
  setSelectedSn1: (value: string) => void;
  selectedSn2: string;
  setSelectedSn2: (value: string) => void;
  selectedServerName: string;
  setSelectedServerName: (value: string) => void;
  serverNames: string[];
  serverNamesLoading: boolean;
  comboValues: ComboValuesDTO | null;
  comboLoading: boolean;
  hasActiveFilters: boolean;
  onClearFilters: () => void;
}

const FilterPanel = ({
  selectedSn1,
  setSelectedSn1,
  selectedSn2,
  setSelectedSn2,
  selectedServerName,
  setSelectedServerName,
  serverNames,
  serverNamesLoading,
  comboValues,
  comboLoading,
  hasActiveFilters,
  onClearFilters,
}: FilterPanelProps) => {
  // Dashboard global filters (shared between dashboard and search)
  const { filters: dashboardFilters, options: dashboardOptions, handlers: dashboardHandlers } = useDashboardFilters();
  
  const hasDashboardFilters = dashboardFilters.selectedPeriod || 
    dashboardFilters.selectedGeography || 
    dashboardFilters.selectedVertical || 
    dashboardFilters.selectedUOL2.length > 0;

  const handleClearAllFilters = () => {
    onClearFilters();
    dashboardHandlers.resetFilters();
  };

  // Handler for vertical change - updates both dashboard and clears dependent filters
  const handleVerticalChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    dashboardHandlers.handleVerticalChange(e);
    // Clear dependent filters when vertical changes
    setSelectedSn1('');
    setSelectedSn2('');
  };

  // Handler for UOL2 change - single select that updates dashboard
  const handleUol2Change = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value;
    dashboardHandlers.setSelectedUOL2(value ? [value] : []);
    // Clear dependent filters when UOL2 changes
    setSelectedSn1('');
    setSelectedSn2('');
  };

  return (
    <div className="absolute top-full left-0 mt-2 w-96 rounded-lg bg-white p-5 shadow-lg dark:bg-boxdark z-50 max-h-[80vh] overflow-y-auto">
      <div className="mb-5 flex items-center justify-between">
        <h3 className="text-base font-semibold text-black dark:text-white">Filtros</h3>
        {(hasActiveFilters || hasDashboardFilters) && (
          <button
            onClick={handleClearAllFilters}
            className="text-sm text-primary hover:underline"
          >
            Limpiar filtros
          </button>
        )}
      </div>

      {/* Filtro Período */}
      <div className="mb-4">
        <label htmlFor="filter-period" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Período
        </label>
        <select
          id="filter-period"
          value={dashboardFilters.selectedPeriod}
          onChange={(e) => dashboardHandlers.setSelectedPeriod(e.target.value)}
          className="w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white"
        >
          <option value="">Todos los períodos</option>
          {dashboardOptions.availablePeriods.map((period: string) => (
            <option key={period} value={period}>
              {period}
            </option>
          ))}
        </select>
      </div>

      {/* Filtro Geografía */}
      <div className="mb-4">
        <label htmlFor="filter-geography" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Geografía
        </label>
        <select
          id="filter-geography"
          value={dashboardFilters.selectedGeography}
          onChange={dashboardHandlers.handleGeographyChange}
          className="w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white"
        >
          <option value="">Todas las geografías</option>
          {dashboardOptions.availableGeographies.map((geo: string) => (
            <option key={geo} value={geo}>
              {geo}
            </option>
          ))}
        </select>
      </div>

      {/* Filtro Vertical (unificado) */}
      <div className="mb-4">
        <label htmlFor="filter-vertical" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Vertical
        </label>
        <select
          id="filter-vertical"
          value={dashboardFilters.selectedVertical}
          onChange={handleVerticalChange}
          disabled={dashboardFilters.isVerticalDisabled || comboLoading}
          className={`w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white ${
            dashboardFilters.isVerticalDisabled ? 'opacity-50 cursor-not-allowed' : ''
          }`}
        >
          <option value="">Todas las verticales</option>
          {dashboardOptions.availableVerticals.map((vertical: string) => (
            <option key={vertical} value={vertical}>
              {vertical}
            </option>
          ))}
        </select>
      </div>

      {/* Filtro Fábrica/UOL2 (unificado) */}
      <div className="mb-4">
        <label htmlFor="filter-uol2" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Fábrica
        </label>
        <select
          id="filter-uol2"
          value={dashboardFilters.selectedUOL2[0] || ''}
          onChange={handleUol2Change}
          disabled={comboLoading}
          className="w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white"
        >
          <option value="">Todas las fábricas</option>
          {dashboardOptions.availableUOL2.map((uol2: string) => (
            <option key={uol2} value={uol2}>
              {uol2}
            </option>
          ))}
        </select>
      </div>

      {/* Filtro SN1 */}
      <div className="mb-4">
        <label htmlFor="filter-sn1" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Service N1
        </label>
        <select
          id="filter-sn1"
          value={selectedSn1}
          onChange={(e) => {
            setSelectedSn1(e.target.value);
            setSelectedSn2(''); // Clear SN2 when SN1 changes
          }}
          disabled={comboLoading}
          className="w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white"
        >
          <option value="">Todos los Service N1</option>
          {comboValues?.sn1Values.map((sn1) => (
            <option key={sn1} value={sn1}>
              {sn1}
            </option>
          ))}
        </select>
      </div>

      {/* Filtro SN2 */}
      <div className="mb-4">
        <label htmlFor="filter-sn2" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Service N2
        </label>
        <select
          id="filter-sn2"
          value={selectedSn2}
          onChange={(e) => setSelectedSn2(e.target.value)}
          disabled={comboLoading}
          className="w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white"
        >
          <option value="">Todos los Service N2</option>
          {comboValues?.sn2Values.map((sn2) => (
            <option key={sn2} value={sn2}>
              {sn2}
            </option>
          ))}
        </select>
      </div>

      {/* Filtro Servidor */}
      <div className="mb-4">
        <label htmlFor="filter-server" className="mb-1.5 block text-sm font-medium text-gray-600 dark:text-gray-300">
          Servidor
        </label>
        <select
          id="filter-server"
          value={selectedServerName}
          onChange={(e) => setSelectedServerName(e.target.value)}
          disabled={comboLoading || serverNamesLoading}
          className="w-full rounded border border-gray-300 bg-white px-3 py-2.5 text-base text-black focus:border-primary focus:outline-none dark:border-gray-600 dark:bg-boxdark dark:text-white"
        >
          <option value="">Todos los servidores</option>
          {serverNames.map((name) => (
            <option key={name} value={name}>
              {name}
            </option>
          ))}
        </select>
      </div>

      {(comboLoading || serverNamesLoading) && (
        <p className="text-sm text-gray-500 dark:text-gray-400">Cargando opciones...</p>
      )}
    </div>
  );
};

export default FilterPanel;
