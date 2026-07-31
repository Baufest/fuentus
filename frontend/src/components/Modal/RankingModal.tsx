import React, { useEffect } from 'react';
import { useRanking } from '../../hooks/useRanking';

interface RankingModalProps {
  isOpen: boolean;
  onClose: () => void;
  nivelTipo: string;
  metricaTipo: string;
  filters: {
    vertical?: string;
    fabrica?: string;
    sn1?: string;
    sn2?: string;
  };
}

const RankingModal: React.FC<RankingModalProps> = ({
  isOpen,
  onClose,
  nivelTipo,
  metricaTipo,
  filters
}) => {
  const { data, loading, error, fetchRanking } = useRanking();

  useEffect(() => {
    if (isOpen) {
      fetchRanking({
        nivelTipo,
        metricaTipo,
        vertical: filters.vertical,
        fabrica: filters.fabrica,
        sn1: filters.sn1,
        sn2: filters.sn2
      });
    }
  }, [isOpen, nivelTipo, metricaTipo, filters.vertical, filters.fabrica, filters.sn1, filters.sn2]);

  if (!isOpen) return null;

  const getMedalEmoji = (posicion: number): string => {
    switch (posicion) {
      case 1: return '🥇';
      case 2: return '🥈';
      case 3: return '🥉';
      default: return '';
    }
  };

  const getMetricaLabel = (metrica: string): string => {
    const labels: { [key: string]: string } = {
      'PRODUCTIVIDAD': 'Productividad',
      'LT': 'Lead Time',
      'CT': 'Cycle Time'
    };
    return labels[metrica] || metrica;
  };

  const getNivelLabel = (nivel: string): string => {
    const labels: { [key: string]: string } = {
      'VERTICAL': 'Vertical',
      'FABRICA': 'Fábrica',
      'SN1': 'Service N1',
      'SN2': 'Service N2'
    };
    return labels[nivel] || nivel;
  };

  const formatValue = (metrica: string, value: number | null): string => {
    if (value === null || value === undefined) return 'N/A';
    
    if (metrica === 'PRODUCTIVIDAD') {
      return `${value.toFixed(3)} features/FTE`;
    } else {
      return `${value.toFixed(1)} días`;
    }
  };

  const getValueColor = (posicion: number): string => {
    switch (posicion) {
      case 1: return 'text-yellow-600 dark:text-yellow-400';
      case 2: return 'text-gray-500 dark:text-gray-400';
      case 3: return 'text-amber-700 dark:text-amber-500';
      default: return 'text-gray-600 dark:text-gray-400';
    }
  };

  return (
    <div className="fixed inset-0 z-9999 flex items-center justify-center bg-black bg-opacity-50" onClick={onClose}>
      <div 
        className="relative w-full max-w-md rounded-lg bg-white p-6 shadow-xl dark:bg-boxdark"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div className="mb-4 flex items-start justify-between">
          <div>
            <h3 className="text-xl font-bold text-black dark:text-white">
              🏆 Top 3 Ranking
            </h3>
            <p className="text-sm text-gray-600 dark:text-gray-400 mt-1">
              {getNivelLabel(nivelTipo)} - {getMetricaLabel(metricaTipo)}
            </p>
          </div>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 text-2xl font-bold"
          >
            ×
          </button>
        </div>

        {/* Content */}
        <div className="space-y-3">
          {loading && (
            <div className="flex items-center justify-center py-8">
              <div className="h-8 w-8 animate-spin rounded-full border-4 border-solid border-primary border-t-transparent"></div>
            </div>
          )}

          {error && (
            <div className="rounded-md bg-red-50 p-4 text-sm text-red-800 dark:bg-red-900/20 dark:text-red-400">
              Error al cargar el ranking
            </div>
          )}

          {!loading && !error && data && (
            <>
              {data.ranking.length === 0 ? (
                <div className="rounded-md bg-gray-50 p-4 text-center text-sm text-gray-600 dark:bg-gray-800 dark:text-gray-400">
                  No hay datos disponibles para este ranking
                </div>
              ) : (
                <div className="space-y-3">
                  {data.ranking.map((item, idx) => (
                    <div
                      key={idx}
                      className={`flex items-center justify-between rounded-lg border p-4 transition-all ${
                        item.posicion === 1
                          ? 'border-yellow-400 bg-yellow-50 dark:border-yellow-600 dark:bg-yellow-900/20'
                          : item.posicion === 2
                          ? 'border-gray-300 bg-gray-50 dark:border-gray-600 dark:bg-gray-800/50'
                          : 'border-amber-300 bg-amber-50 dark:border-amber-600 dark:bg-amber-900/20'
                      }`}
                    >
                      <div className="flex items-center gap-3">
                        <span className="text-3xl">{getMedalEmoji(item.posicion)}</span>
                        <div>
                          <div className="text-xs font-medium text-gray-500 dark:text-gray-400">
                            #{item.posicion}
                          </div>
                          <div className="font-semibold text-black dark:text-white">
                            {item.nombre}
                          </div>
                        </div>
                      </div>
                      <div className={`text-right font-bold ${getValueColor(item.posicion)}`}>
                        {formatValue(metricaTipo, item.valor)}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </>
          )}
        </div>

        {/* Footer */}
        <div className="mt-6 flex justify-end">
          <button
            onClick={onClose}
            className="rounded-md bg-primary px-4 py-2 text-sm font-medium text-white hover:bg-opacity-90 transition"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
};

export default RankingModal;
