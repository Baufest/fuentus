import React from 'react';
import { FiCheckCircle, FiAlertCircle, FiClock, FiXCircle } from 'react-icons/fi';

export type RfoStatus = 'COMPLETADO AL 100%' | 'EN CURSO' | 'BLOQUEO PARCIAL' | null;

interface RfoStatusIconProps {
  status?: RfoStatus;
  rfoId?: number | null;
}


function normalizeStatus(status: string | null): RfoStatus {
  if (!status) return null;
  const s = status.normalize('NFD').replace(/\p{Diacritic}/gu, '').toUpperCase().trim();
  if (s === 'COMPLETADO AL 100%') return 'COMPLETADO AL 100%';
  if (s === 'EN CURSO') return 'EN CURSO';
  if (s === 'BLOQUEO PARCIAL') return 'BLOQUEO PARCIAL';
  return null;
}

const RfoStatusIcon: React.FC<RfoStatusIconProps> = ({ status, rfoId }) => {
  const normalized = normalizeStatus(status || null);
  if (!normalized) {
    return <FiXCircle className="w-5 h-5 text-gray-400" title="Estado desconocido" />;
  }

  const getStatusConfig = (status: RfoStatus) => {
    switch (status) {
      case 'COMPLETADO AL 100%':
        return {
          icon: <FiCheckCircle className="w-5 h-5" />,
          color: 'text-green-500',
          bgColor: 'bg-green-50 dark:bg-green-900/20',
          tooltipText: 'Completado al 100%'
        };
      case 'EN CURSO':
        return {
          icon: <FiClock className="w-5 h-5" />,
          color: 'text-blue-500',
          bgColor: 'bg-blue-50 dark:bg-blue-900/20',
          tooltipText: 'En Curso'
        };
      case 'BLOQUEO PARCIAL':
        return {
          icon: <FiAlertCircle className="w-5 h-5" />,
          color: 'text-amber-500',
          bgColor: 'bg-amber-50 dark:bg-amber-900/20',
          tooltipText: 'Bloqueo Parcial'
        };
      default:
        return {
          icon: null,
          color: 'text-gray-400',
          bgColor: 'bg-gray-50 dark:bg-gray-900/20',
          tooltipText: 'Estado desconocido'
        };
    }
  };

  const config = getStatusConfig(normalized);

  return (
    <div
      className={`inline-flex items-center justify-center p-2 rounded ${config.bgColor} group relative cursor-help`}
      title={config.tooltipText}
    >
      <span className={`${config.color}`}>
        {config.icon}
      </span>
      
      {/* Tooltip */}
      <div className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 px-2 py-1 bg-gray-900 dark:bg-gray-800 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-50">
        {config.tooltipText}
      </div>

      {/* Link to RFO Manager if rfoId exists */}
      {rfoId && (
        <a
          href={`https://bbva-continuum.appspot.com/rfo-manager/capturaRFO/${rfoId}`}
          target="_blank"
          rel="noopener noreferrer"
          className="ml-1 text-gray-400 hover:text-primary dark:text-gray-500 dark:hover:text-primary transition-colors"
          title="Abrir en RFO Manager"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
          </svg>
        </a>
      )}
    </div>
  );
};

export default RfoStatusIcon;
