import React from 'react';
import { UuaaSummaryDTO } from '../../types/statsSummary';
import RfoStatusIcon from '../RfoStatus/RfoStatusIcon';
import { TbApps, TbShieldCheck, TbPackage, TbBug } from 'react-icons/tb';
import { FiPercent } from 'react-icons/fi';

interface UuaaSummaryCardsProps {
  summary: UuaaSummaryDTO | null;
  loading: boolean;
}

const UuaaSummaryCards: React.FC<UuaaSummaryCardsProps> = ({ summary, loading }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 mb-6">
        {[...Array(5)].map((_, index) => (
          <div key={index} className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark animate-pulse">
            <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4 mb-3"></div>
            <div className="h-8 bg-gray-200 dark:bg-gray-700 rounded w-1/2"></div>
          </div>
        ))}
      </div>
    );
  }

  if (!summary) {
    return null;
  }

  const getCoverageColor = (coverage: number): string => {
    if (coverage >= 80) return 'text-green-500';
    if (coverage >= 50) return 'text-yellow-500';
    return 'text-red-500';
  };

  const getCoverageBg = (coverage: number): string => {
    if (coverage >= 80) return 'bg-green-50 dark:bg-green-900/20';
    if (coverage >= 50) return 'bg-yellow-50 dark:bg-yellow-900/20';
    return 'bg-red-50 dark:bg-red-900/20';
  };

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 mb-6">
      {/* Total Apps */}
      <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-center justify-between">
          <div>
            <span className="text-sm font-medium text-gray-500 dark:text-gray-400">
              Total Aplicaciones
            </span>
            <h4 className="mt-2 text-2xl font-bold text-black dark:text-white">
              {summary.totalApps}
            </h4>
          </div>
          <div className="flex items-center justify-center w-12 h-12 rounded-full bg-blue-50 dark:bg-blue-900/20">
            <TbApps className="w-6 h-6 text-blue-500" />
          </div>
        </div>
      </div>

      {/* Average Coverage */}
      <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-center justify-between">
          <div>
            <span className="text-sm font-medium text-gray-500 dark:text-gray-400">
              Cobertura Promedio
            </span>
            <h4 className={`mt-2 text-2xl font-bold ${getCoverageColor(summary.averageCoverage)}`}>
              {summary.averageCoverage.toFixed(1)}%
            </h4>
          </div>
          <div className={`flex items-center justify-center w-12 h-12 rounded-full ${getCoverageBg(summary.averageCoverage)}`}>
            <FiPercent className={`w-6 h-6 ${getCoverageColor(summary.averageCoverage)}`} />
          </div>
        </div>
      </div>

      {/* SAST Vulnerabilities */}
      <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-center justify-between">
          <div>
            <span className="text-sm font-medium text-gray-500 dark:text-gray-400">
              Vulnerabilidades SAST
            </span>
            <h4 className="mt-2 text-2xl font-bold text-black dark:text-white">
              {summary.totalSastVulnerabilities}
            </h4>
            <div className="flex gap-2 mt-1 text-xs">
              <span className="text-red-500">{summary.totalSastHigh}H</span>
              <span className="text-yellow-500">{summary.totalSastMedium}M</span>
              <span className="text-green-500">{summary.totalSastLow}L</span>
            </div>
          </div>
          <div className="flex items-center justify-center w-12 h-12 rounded-full bg-purple-50 dark:bg-purple-900/20">
            <TbShieldCheck className="w-6 h-6 text-purple-500" />
          </div>
        </div>
      </div>

      {/* SCA Vulnerabilities */}
      <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-center justify-between">
          <div>
            <span className="text-sm font-medium text-gray-500 dark:text-gray-400">
              Vulnerabilidades SCA
            </span>
            <h4 className="mt-2 text-2xl font-bold text-black dark:text-white">
              {summary.totalScaVulnerabilities}
            </h4>
            <div className="flex gap-2 mt-1 text-xs">
              <span className="text-red-700">{summary.totalScaCritical}C</span>
              <span className="text-red-500">{summary.totalScaHigh}H</span>
              <span className="text-yellow-500">{summary.totalScaMedium}M</span>
              <span className="text-green-500">{summary.totalScaLow}L</span>
            </div>
          </div>
          <div className="flex items-center justify-center w-12 h-12 rounded-full bg-orange-50 dark:bg-orange-900/20">
            <TbPackage className="w-6 h-6 text-orange-500" />
          </div>
        </div>
      </div>

      {/* RFO Status */}
      <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex items-center justify-between">
          <div>
            <span className="text-sm font-medium text-gray-500 dark:text-gray-400">
              Estado RFO
            </span>
            <div className="mt-2">
              <RfoStatusIcon 
                status={summary.rfoEstado as any} 
                rfoId={summary.rfoId}
              />
            </div>
          </div>
          {summary.totalBugs > 0 && (
            <div className="flex flex-col items-end">
              <span className="text-xs text-gray-400">Bugs</span>
              <span className="flex items-center gap-1 text-red-500 font-semibold">
                <TbBug className="w-4 h-4" />
                {summary.totalBugs}
              </span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default UuaaSummaryCards;
