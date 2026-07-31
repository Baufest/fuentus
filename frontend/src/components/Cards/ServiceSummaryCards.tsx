import React from 'react';
import { ServiceSummaryDTO } from '../../types/statsSummary';
import { TbApps, TbShieldCheck, TbPackage, TbChartPie } from 'react-icons/tb';
import RfoStatusIcon, { RfoStatus } from '../RfoStatus/RfoStatusIcon';

interface ServiceSummaryCardsProps {
  summary: ServiceSummaryDTO | null;
  loading: boolean;
}

const ServiceSummaryCards: React.FC<ServiceSummaryCardsProps> = ({ summary, loading }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4">
        {[...Array(5)].map((_, index) => (
          <div 
            key={index}
            className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark animate-pulse"
          >
            <div className="h-10 w-10 rounded-full bg-gray-200 dark:bg-gray-700 mb-3"></div>
            <div className="h-6 bg-gray-200 dark:bg-gray-700 rounded w-1/2 mb-2"></div>
            <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4"></div>
          </div>
        ))}
      </div>
    );
  }

  if (!summary) {
    return null;
  }

  const cards = [
    {
      title: 'Total Apps',
      value: summary.totalApps,
      icon: <TbApps className="h-6 w-6" />,
      color: 'bg-blue-500',
      textColor: 'text-blue-600 dark:text-blue-400',
      description: `En ${summary.uuaas?.length || 0} UUAA(s)`,
    },
    {
      title: 'Cobertura Promedio',
      value: `${summary.averageCoverage?.toFixed(1) || 0}%`,
      icon: <TbChartPie className="h-6 w-6" />,
      color: summary.averageCoverage >= 80 ? 'bg-green-500' : summary.averageCoverage >= 50 ? 'bg-yellow-500' : 'bg-red-500',
      textColor: summary.averageCoverage >= 80 ? 'text-green-600 dark:text-green-400' : summary.averageCoverage >= 50 ? 'text-yellow-600 dark:text-yellow-400' : 'text-red-600 dark:text-red-400',
      description: 'Coverage promedio del servicio',
    },
    {
      title: 'SAST Vulnerabilities',
      value: (summary.totalSastHigh || 0) + (summary.totalSastMedium || 0) + (summary.totalSastLow || 0),
      icon: <TbShieldCheck className="h-6 w-6" />,
      color: 'bg-purple-500',
      textColor: 'text-purple-600 dark:text-purple-400',
      description: `H:${summary.totalSastHigh || 0} M:${summary.totalSastMedium || 0} L:${summary.totalSastLow || 0}`,
    },
    {
      title: 'SCA Vulnerabilities',
      value: (summary.totalScaCritical || 0) + (summary.totalScaHigh || 0) + (summary.totalScaMedium || 0) + (summary.totalScaLow || 0),
      icon: <TbPackage className="h-6 w-6" />,
      color: 'bg-orange-500',
      textColor: 'text-orange-600 dark:text-orange-400',
      description: `C:${summary.totalScaCritical || 0} H:${summary.totalScaHigh || 0} M:${summary.totalScaMedium || 0} L:${summary.totalScaLow || 0}`,
    },
    {
      title: 'Estado RFO',
      value: summary.rfoEstado || 'N/A',
      icon: <RfoStatusIcon status={summary.rfoEstado as RfoStatus} />,
      color: 'bg-gray-500',
      textColor: 'text-gray-600 dark:text-gray-400',
      description: summary.rfoId ? `RFO #${summary.rfoId}` : 'Sin RFO asignado',
      isRfo: true,
    },
  ];

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4">
      {cards.map((card, index) => (
        <div
          key={index}
          className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark hover:shadow-lg transition-shadow"
        >
          <div className="flex items-center gap-3 mb-3">
            <div className={`flex items-center justify-center h-10 w-10 rounded-full ${card.color} bg-opacity-20`}>
              <span className={card.textColor}>{card.icon}</span>
            </div>
            <span className="text-sm font-medium text-gray-500 dark:text-gray-400">
              {card.title}
            </span>
          </div>
          <div className={`text-2xl font-bold ${card.textColor} mb-1`}>
            {card.isRfo ? (
              <span className="flex items-center gap-2">
                {card.value}
              </span>
            ) : (
              card.value
            )}
          </div>
          <p className="text-xs text-gray-500 dark:text-gray-400">
            {card.description}
          </p>
        </div>
      ))}
    </div>
  );
};

export default ServiceSummaryCards;
