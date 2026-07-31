import React from 'react';

/**
 * Componente de loading común
 */
export const ChartLoadingState: React.FC = () => (
  <div className="flex items-center justify-center h-64">
    <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600" data-testid="loading-spinner"></div>
  </div>
);

/**
 * Componente de estado vacío común
 */
interface EmptyStateProps {
  title?: string;
  message?: string;
}

export const ChartEmptyState: React.FC<EmptyStateProps> = ({ 
  title = "No hay datos disponibles",
  message = "No se encontraron datos con los filtros aplicados"
}) => (
  <div className="flex items-center justify-center h-64 text-gray-500 dark:text-gray-400">
    <div className="text-center">
      <h3 className="text-lg font-medium mb-2">{title}</h3>
      <p>{message}</p>
    </div>
  </div>
);

/**
 * Banner informativo para datos filtrados
 */
interface FilteredDataBannerProps {
  visibleCount: number;
  totalCount: number;
  entityName?: string;
}

export const FilteredDataBanner: React.FC<FilteredDataBannerProps> = ({ 
  visibleCount, 
  totalCount, 
  entityName = "elementos"
}) => (
  <div className="bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800 rounded-lg p-3 mb-4">
    <p className="text-sm text-amber-800 dark:text-amber-200">
      <strong>Información:</strong> Se están mostrando {visibleCount} de {totalCount} {entityName}. 
      Los {entityName} sin datos válidos han sido ocultados para mejorar la visualización.
    </p>
  </div>
);

/**
 * Panel informativo común
 */
interface InfoPanelProps {
  title: string;
  children: React.ReactNode;
  variant?: 'blue' | 'amber' | 'green';
}

export const InfoPanel: React.FC<InfoPanelProps> = ({ 
  title, 
  children, 
  variant = 'blue' 
}) => {
  const variantClasses = {
    blue: 'bg-blue-50 dark:bg-blue-900/20 border-blue-200 dark:border-blue-800 text-blue-800 dark:text-blue-200',
    amber: 'bg-amber-50 dark:bg-amber-900/20 border-amber-200 dark:border-amber-800 text-amber-800 dark:text-amber-200',
    green: 'bg-green-50 dark:bg-green-900/20 border-green-200 dark:border-green-800 text-green-800 dark:text-green-200'
  };

  const iconColor = {
    blue: 'text-blue-400',
    amber: 'text-amber-400', 
    green: 'text-green-400'
  };

  return (
    <div className={`border rounded-lg p-4 ${variantClasses[variant]}`}>
      <div className="flex items-center mb-2">
        <div className="flex-shrink-0">
          <svg className={`h-5 w-5 ${iconColor[variant]}`} fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
          </svg>
        </div>
        <div className="ml-3">
          <h3 className="text-sm font-medium">{title}</h3>
          <div className="mt-1 text-sm">
            {children}
          </div>
        </div>
      </div>
    </div>
  );
};
