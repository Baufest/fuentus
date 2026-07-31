import React from 'react';
import { TopLevelMetrics } from '../../hooks/useTotalizadores';

interface TotalizadorCardProps {
  metrics: TopLevelMetrics;
}

const TotalizadorCard: React.FC<TotalizadorCardProps> = ({ metrics }) => {
  const getNivelLabel = (tipo: string): string => {
    const labels: { [key: string]: string } = {
      'VERTICAL': 'Mejor Vertical',
      'FABRICA': 'Mejor Fábrica',
      'SN1': 'Mejor SN1',
      'SN2': 'Mejor SN2'
    };
    return labels[tipo] || tipo;
  };

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

  return (
    <div className="rounded-sm border border-stroke bg-white px-5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark">
      {/* Header con tipo de nivel */}
      <div className="mb-4">
        <span className={`inline-block rounded px-3 py-1 text-sm font-medium text-white ${getNivelColor(metrics.nivelTipo)}`}>
          {getNivelLabel(metrics.nivelTipo)}
        </span>
      </div>

      {/* Nombre del nivel */}
      <div className="mb-4">
        <h4 className="text-lg font-bold text-black dark:text-white truncate" title={metrics.nombre}>
          {metrics.nombre}
        </h4>
      </div>

      {/* Métricas */}
      <div className="space-y-3">
        {/* Productividad */}
        <div className="flex justify-between items-center">
          <span className="text-sm text-gray-600 dark:text-gray-400">Productividad:</span>
          <span className="text-sm font-semibold text-black dark:text-white">
            {formatNumber(metrics.productividad, 3)}
          </span>
        </div>

        {/* Promedio LT */}
        <div className="flex justify-between items-center">
          <span className="text-sm text-gray-600 dark:text-gray-400">Promedio LT:</span>
          <span className="text-sm font-semibold text-black dark:text-white">
            {formatNumber(metrics.promedioLT, 1)} días
          </span>
        </div>

        {/* Promedio CT */}
        <div className="flex justify-between items-center">
          <span className="text-sm text-gray-600 dark:text-gray-400">Promedio CT:</span>
          <span className="text-sm font-semibold text-black dark:text-white">
            {formatNumber(metrics.promedioCT, 1)} días
          </span>
        </div>
      </div>
    </div>
  );
};

export default TotalizadorCard;
