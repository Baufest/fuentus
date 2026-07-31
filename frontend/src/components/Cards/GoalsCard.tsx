import React from 'react';
import { useGoals } from '../../hooks/useGoals';
import { GoalDTO } from '../../types/nucleus';

interface GoalsCardProps {
  className?: string;
}

const GoalsCard: React.FC<GoalsCardProps> = ({ className = "" }) => {
  const { groupedGoals, loading, error } = useGoals();

  const getCategoryIcon = (category: string): string => {
    const icons: { [key: string]: string } = {
      'Cobertura': '📊',
      'Calidad': '✅',
      'Productividad': '🚀',
      'Velocidad': '⏱️',
      'Seguridad': '🔒',
      'Disponibilidad': '🔄',
      'General': '🎯'
    };
    return icons[category] || '🎯';
  };

  const getCategoryColor = (category: string): string => {
    const colors: { [key: string]: string } = {
      'Cobertura': 'bg-blue-500',
      'Calidad': 'bg-green-500',
      'Productividad': 'bg-purple-500',
      'Velocidad': 'bg-orange-500',
      'Seguridad': 'bg-red-500',
      'Disponibilidad': 'bg-cyan-500',
      'General': 'bg-gray-500'
    };
    return colors[category] || 'bg-primary';
  };

  const formatValue = (goal: GoalDTO): string => {
    if (goal.numericValue !== null && goal.numericValue !== undefined) {
      const value = goal.numericValue;
      const unit = goal.unit || '';
      
      // Formatear según el tipo de unidad
      if (unit === '%') {
        return `${value}%`;
      } else if (unit) {
        return `${value} ${unit}`;
      }
      return value.toString();
    }
    
    if (goal.categoricalValue) {
      return goal.categoricalValue;
    }
    
    return 'N/A';
  };

  if (loading) {
    return (
      <div className={`rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark ${className}`}>
        <div className="flex items-center justify-center h-32">
          <div className="animate-pulse text-gray-400">Cargando objetivos...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={`rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark ${className}`}>
        <div className="flex items-center justify-center h-32">
          <div className="text-red-500">Error al cargar objetivos</div>
        </div>
      </div>
    );
  }

  if (groupedGoals.length === 0) {
    return (
      <div className={`rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark ${className}`}>
        <h4 className="text-title-md font-bold text-black dark:text-white mb-4">
          🎯 Objetivos
        </h4>
        <div className="flex items-center justify-center h-20">
          <div className="text-gray-400">No hay objetivos configurados</div>
        </div>
      </div>
    );
  }

  return (
    <div className={`rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark ${className}`}>
      <h4 className="text-title-md font-bold text-black dark:text-white mb-4">
        🎯 Objetivos
      </h4>
      
      <div className="space-y-4 max-h-[300px] overflow-y-auto pr-2">
        {groupedGoals.map((categoryGroup) => (
          <div key={categoryGroup.category} className="border-b border-stroke dark:border-strokedark pb-3 last:border-b-0">
            <div className="flex items-center gap-2 mb-2">
              <span className={`inline-flex h-6 w-6 items-center justify-center rounded-full text-xs ${getCategoryColor(categoryGroup.category)}`}>
                {getCategoryIcon(categoryGroup.category)}
              </span>
              <span className="text-sm font-semibold text-black dark:text-white">
                {categoryGroup.category}
              </span>
            </div>
            
            <div className="space-y-2 ml-8">
              {categoryGroup.goals.map((goal) => (
                <div key={goal.id} className="flex items-center justify-between">
                  <div className="flex-1">
                    <span className="text-sm text-gray-600 dark:text-gray-300" title={goal.description}>
                      {goal.name}
                    </span>
                  </div>
                  <div className="flex items-center">
                    <span className="text-sm font-bold text-primary">
                      {formatValue(goal)}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default GoalsCard;
