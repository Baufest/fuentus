import { DashboardDataRow, getUniqueValues } from '../data/dashboardData';

/**
 * Sorts periods in descending order (most recent first)
 */
export const sortPeriods = (periods: string[]): string[] => {
  const getMonthNumber = (monthAbbr: string): number => {
    const months: { [key: string]: number } = {
      'ene': 0, 'feb': 1, 'mar': 2, 'abr': 3, 'may': 4, 'jun': 5,
      'jul': 6, 'ago': 7, 'sep': 8, 'oct': 9, 'nov': 10, 'dic': 11
    };
    return months[monthAbbr.toLowerCase()] ?? 0;
  };

  return periods.sort((a, b) => {
    const partsA = a.trim().split(' ');
    const partsB = b.trim().split(' ');
    
    if (partsA.length !== 2 || partsB.length !== 2) {
      return 0;
    }
    
    const [monthA, yearA] = partsA;
    const [monthB, yearB] = partsB;
    
    const fullYearA = parseInt(`20${yearA}`);
    const fullYearB = parseInt(`20${yearB}`);
    
    if (fullYearA !== fullYearB) {
      return fullYearB - fullYearA;
    }
    
    return getMonthNumber(monthB) - getMonthNumber(monthA);
  });
};

/**
 * Calculates certification statistics from filtered data
 */
export const calculateCertificationStats = (filteredData: DashboardDataRow[]) => {
  const certificationCounts = Object.entries(filteredData.reduce((acc, row) => {
    const level = row.nivel_certificacion || 'No Certificado';
    acc[level] = (acc[level] || 0) + 1;
    return acc;
  }, {} as Record<string, number>));

  const chartData = {
    labels: certificationCounts.map(([level]) => level),
    series: certificationCounts.map(([, count]) => count),
  };

  const totalCases = chartData.series.reduce((a, b) => a + b, 0);
  const certifiedCases = certificationCounts
    .filter(([level]) => level.includes('Level'))
    .reduce((sum, [, count]) => sum + count, 0);
  const certificationPercentage = totalCases > 0 ? (certifiedCases / totalCases * 100).toFixed(1) : '0.0';

  return {
    certificationCounts,
    chartData,
    totalCases,
    certifiedCases,
    certificationPercentage
  };
};

/**
 * Initialize dashboard data and options
 */
export const initializeDashboardData = (csvData: DashboardDataRow[]) => {
  const periods = getUniqueValues(csvData, 'period_month');
  const sortedPeriods = sortPeriods(periods);
  
  const geographies = getUniqueValues(csvData, 'ug_name');
  const verticals = getUniqueValues(csvData, 'vertical');
  
  // Set default period to most recent
  const defaultPeriod = sortedPeriods.length > 0 ? sortedPeriods[0] : '';
  
  // Check if SYSTEMS ENGINEERING should be preselected
  const shouldPreselectSystemsEngineering = getUniqueValues(csvData, 'uol1_name').includes('SYSTEMS ENGINEERING');
  
  return {
    periods: sortedPeriods,
    geographies,
    verticals,
    defaultPeriod,
    shouldPreselectSystemsEngineering
  };
};

/**
 * Gets the certification colors for charts
 */
export const getCertificationColors = (labelCount: number): string[] => {
  const colors = ['#8093b3', '#1973b8', '#004481', '#0f3669'];
  return colors.slice(0, labelCount);
};
