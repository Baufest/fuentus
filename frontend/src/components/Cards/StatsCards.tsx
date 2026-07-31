import React, { useState } from 'react';
import { FaExternalLinkAlt } from 'react-icons/fa';
import { useCountSummary } from '../../hooks/useCountSummary';
import { useTotalizadores } from '../../hooks/useTotalizadores';
import { useHeaderFilters } from '../../contexts/HeaderFiltersContext';
import { useDashboardFilters } from '../../hooks/useDashboardFiltersGlobal';
import { Link } from 'react-router-dom';
import RankingModal from '../Modal/RankingModal';

interface StatsCardsProps {
  filteredData: any[];
  filters?: {
    selectedVertical?: string;
    selectedUOL1?: string[];
    selectedUOL2?: string[];
    selectedSN1?: string[];
    selectedSN2?: string[];
    selectedUUAA?: string[];
  };
  className?: string;
}

const StatsCards: React.FC<StatsCardsProps> = ({ filteredData, filters, className = "" }) => {
  // Get filters from Header context
  const headerFilters = useHeaderFilters();
  // Get filters from Dashboard context (for vertical and uol2)
  const { filters: dashboardFilters } = useDashboardFilters();
  
  // Modal state
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedNivel, setSelectedNivel] = useState<{ nivelTipo: string; metricaTipo: string } | null>(null);
  
  // Use the API to get count summary
  const { countSummary, loading } = useCountSummary({
    vertical: filters?.selectedVertical,
    fabrica: filters?.selectedUOL2,
    sn1: filters?.selectedSN1,
    sn2: filters?.selectedSN2,
    uuaa: filters?.selectedUUAA,
    enabled: true
  });

  // Get totalizadores data using Dashboard and Header filters
  const { data: totalizadores, loading: totalizadoresLoading } = useTotalizadores({
    vertical: dashboardFilters.selectedVertical,
    fabrica: dashboardFilters.selectedUOL2.length > 0 ? dashboardFilters.selectedUOL2 : [],
    sn1: headerFilters.selectedSn1 ? [headerFilters.selectedSn1] : [],
    sn2: headerFilters.selectedSn2 ? [headerFilters.selectedSn2] : [],
    enabled: true
  });

  // Fallback to calculated values if API fails or is loading
  const totalServicios = countSummary && !loading ? 
    (countSummary.totalSn1 + countSummary.totalSn2) : 
    filteredData.length;

  const totalUOL2 = countSummary && !loading ? 
    countSummary.totalFabricas : 
    new Set(filteredData.map(row => row.uol2_name)).size;

  const totalVerticales = countSummary && !loading ? 
    countSummary.totalVerticales : 
    new Set(filteredData.map(row => row.vertical)).size;

  const formatNumber = (value: number | null, decimals: number = 2): string => {
    if (value === null || value === undefined) return 'N/A';
    return value.toFixed(decimals);
  };

  const getNivelColor = (tipo: string): string => {
    const colors: { [key: string]: string } = {
      'VERTICAL': 'bg-purple-500',
      'FABRICA': 'bg-blue-500',
      'SN1': 'bg-green-500',
      'SN2': 'bg-orange-500'
    };
    return colors[tipo] || 'bg-gray-500';
  };

  const getMetricaIcon = (metrica: string): string => {
    const icons: { [key: string]: string } = {
      'PRODUCTIVIDAD': '📊',
      'LT': '⏱️',
      'CT': '🚀'
    };
    return icons[metrica] || '📈';
  };

  const mejoresNiveles = totalizadores?.mejoresNiveles || [];

  // Agrupar los mejores niveles por tipo de métrica para mejor visualización
  const mejoresPorMetrica = {
    productividad: mejoresNiveles.filter(n => n.metricaTipo === 'PRODUCTIVIDAD'),
    lt: mejoresNiveles.filter(n => n.metricaTipo === 'LT'),
    ct: mejoresNiveles.filter(n => n.metricaTipo === 'CT')
  };

  // Función para abrir el modal con el ranking
  const handleOpenRanking = (nivelTipo: string, metricaTipo: string) => {
    setSelectedNivel({ nivelTipo, metricaTipo });
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setSelectedNivel(null);
  };

  return (
    <>
      {/* Modal de Ranking */}
      {selectedNivel && (
        <RankingModal
          isOpen={modalOpen}
          onClose={handleCloseModal}
          nivelTipo={selectedNivel.nivelTipo}
          metricaTipo={selectedNivel.metricaTipo}
          filters={{
            vertical: dashboardFilters.selectedVertical,
            fabrica: dashboardFilters.selectedUOL2.length > 0 ? dashboardFilters.selectedUOL2[0] : undefined,
            sn1: headerFilters.selectedSn1,
            sn2: headerFilters.selectedSn2
          }}
        />
      )}

      <div className={`col-span-3 grid grid-cols-1 gap-4 md:grid-cols-3 ${className}`}>
      {/* Card 1 - Total Servicios con mejores en Productividad */}
      <div className="rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-end justify-between">
          <div>
            <h4 className="text-title-md font-bold text-black dark:text-white mb-2">
              {loading ? "..." : totalServicios}
            </h4>
            <span className="text-sm font-medium">Total de Servicios</span>
          </div>
        </div>
        
        {/* Mostrar mejores en Productividad */}
        {!totalizadoresLoading && mejoresPorMetrica.productividad.length > 0 && (
          <div className="mt-4 pt-4 border-t border-stroke dark:border-strokedark">
            <h5 className="text-xs font-semibold text-gray-600 dark:text-gray-400 mb-2">
              {getMetricaIcon('PRODUCTIVIDAD')} Mejores en Productividad
            </h5>
            <div className="space-y-2">
              {mejoresPorMetrica.productividad.map((nivel, idx) => (
                <div 
                  key={idx} 
                  className="text-xs cursor-pointer hover:bg-gray-50 dark:hover:bg-gray-800 p-2 rounded transition-colors"
                  onClick={() => handleOpenRanking(nivel.nivelTipo, 'PRODUCTIVIDAD')}
                  title="Click para ver ranking completo"
                >
                  <span className={`inline-block rounded px-2 py-0.5 text-xs font-medium text-white ${getNivelColor(nivel.nivelTipo)} mb-1`}>
                    {nivel.nivelTipo}
                  </span>
                  <p className="font-semibold text-black dark:text-white truncate hover:text-primary" title={nivel.nombre}>
                    {nivel.nombre} 🔍
                  </p>
                  <p className="text-green-600 dark:text-green-400 font-medium">
                    {formatNumber(nivel.valor, 3)} features/FTE
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Card 2 - UOL2 Únicas con mejores en LT */}
      <div className="rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-end justify-between">
          <div>
            <h4 className="text-title-md font-bold text-black dark:text-white mb-2">
              {loading ? "..." : totalUOL2}
            </h4>
            <span className="text-sm font-medium">UOL2 Únicas</span>
          </div>
        </div>
        
        {/* Mostrar mejores en LT */}
        {!totalizadoresLoading && mejoresPorMetrica.lt.length > 0 && (
          <div className="mt-4 pt-4 border-t border-stroke dark:border-strokedark">
            <h5 className="text-xs font-semibold text-gray-600 dark:text-gray-400 mb-2">
              {getMetricaIcon('LT')} Mejores en Lead Time
            </h5>
            <div className="space-y-2">
              {mejoresPorMetrica.lt.map((nivel, idx) => (
                <div 
                  key={idx} 
                  className="text-xs cursor-pointer hover:bg-gray-50 dark:hover:bg-gray-800 p-2 rounded transition-colors"
                  onClick={() => handleOpenRanking(nivel.nivelTipo, 'LT')}
                  title="Click para ver ranking completo"
                >
                  <span className={`inline-block rounded px-2 py-0.5 text-xs font-medium text-white ${getNivelColor(nivel.nivelTipo)} mb-1`}>
                    {nivel.nivelTipo}
                  </span>
                  <p className="font-semibold text-black dark:text-white truncate hover:text-primary" title={nivel.nombre}>
                    {nivel.nombre} 🔍
                  </p>
                  <p className="text-blue-600 dark:text-blue-400 font-medium">
                    {formatNumber(nivel.valor, 1)} días
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Card 3 - Verticales con mejores en CT y link */}
      <div className="rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-end justify-between">
          <div>
            <h4 className="text-title-md font-bold text-black dark:text-white mb-2">
              {loading ? "..." : totalVerticales}
            </h4>
            <span className="text-sm font-medium">Verticales</span>
          </div>
          <Link to="/verticales">
            <FaExternalLinkAlt className='text-meta-3 size-7 mt-3' />
          </Link>
        </div>
        
        {/* Mostrar mejores en CT */}
        {!totalizadoresLoading && mejoresPorMetrica.ct.length > 0 && (
          <div className="mt-4 pt-4 border-t border-stroke dark:border-strokedark">
            <h5 className="text-xs font-semibold text-gray-600 dark:text-gray-400 mb-2">
              {getMetricaIcon('CT')} Mejores en Cycle Time
            </h5>
            <div className="space-y-2">
              {mejoresPorMetrica.ct.map((nivel, idx) => (
                <div 
                  key={idx} 
                  className="text-xs cursor-pointer hover:bg-gray-50 dark:hover:bg-gray-800 p-2 rounded transition-colors"
                  onClick={() => handleOpenRanking(nivel.nivelTipo, 'CT')}
                  title="Click para ver ranking completo"
                >
                  <span className={`inline-block rounded px-2 py-0.5 text-xs font-medium text-white ${getNivelColor(nivel.nivelTipo)} mb-1`}>
                    {nivel.nivelTipo}
                  </span>
                  <p className="font-semibold text-black dark:text-white truncate hover:text-primary" title={nivel.nombre}>
                    {nivel.nombre} 🔍
                  </p>
                  <p className="text-purple-600 dark:text-purple-400 font-medium">
                    {formatNumber(nivel.valor, 1)} días
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
    </>
  );
};

export default StatsCards;