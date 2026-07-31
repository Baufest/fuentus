import React from 'react';
import TotalizadorCard from './TotalizadorCard';
import { useTotalizadores } from '../../hooks/useTotalizadores';

interface TotalizadoresCardsProps {
  filters?: {
    selectedVertical?: string;
    selectedUOL2?: string[];
    selectedSN1?: string[];
    selectedSN2?: string[];
  };
  className?: string;
}

const TotalizadoresCards: React.FC<TotalizadoresCardsProps> = ({ filters, className = "" }) => {
  const { data, loading, error } = useTotalizadores({
    vertical: filters?.selectedVertical,
    fabrica: filters?.selectedUOL2,
    sn1: filters?.selectedSN1,
    sn2: filters?.selectedSN2,
    enabled: true
  });

  if (loading) {
    return (
      <div className={`grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4 ${className}`}>
        {[1, 2, 3, 4].map((i) => (
          <div 
            key={i}
            className="rounded-sm border border-stroke bg-white px-5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark animate-pulse"
          >
            <div className="h-6 bg-gray-200 dark:bg-gray-700 rounded mb-4 w-1/2"></div>
            <div className="h-8 bg-gray-200 dark:bg-gray-700 rounded mb-4"></div>
            <div className="space-y-3">
              <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded"></div>
              <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded"></div>
              <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded"></div>
            </div>
          </div>
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className={`rounded-sm border border-red-300 bg-red-50 px-5 py-6 dark:bg-red-900/20 ${className}`}>
        <p className="text-red-600 dark:text-red-400">
          Error al cargar los totalizadores: {error.message}
        </p>
      </div>
    );
  }

  if (!data || !data.mejoresNiveles || data.mejoresNiveles.length === 0) {
    return (
      <div className={`rounded-sm border border-stroke bg-white px-5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark ${className}`}>
        <p className="text-gray-600 dark:text-gray-400 text-center">
          No hay datos disponibles para los filtros seleccionados
        </p>
      </div>
    );
  }

  return (
    <div className={`grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-${Math.min(data.mejoresNiveles.length, 4)} ${className}`}>
      {data.mejoresNiveles.map((metrics, index) => (
        <TotalizadorCard key={`${metrics.nivelTipo}-${index}`} metrics={metrics} />
      ))}
    </div>
  );
};

export default TotalizadoresCards;
