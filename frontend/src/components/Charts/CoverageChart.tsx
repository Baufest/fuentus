import React, { useState } from 'react';
import ReactApexChart from 'react-apexcharts';
import { CoverageChartData } from '../../types/statsSummary';
import ApplicationsCoverageChart from './ApplicationsCoverageChart';
import { useAutoScroll } from '../../hooks/useAutoScroll';
import { useChartTooltips } from '../../hooks/useChartTooltips';
import { getBarChartOptions, SEVERITY_COLORS } from '../../utils/chartConfig';
import { createCustomTooltip, createCoverageTooltipContent } from '../../utils/chartUtils';
import { ChartLoadingState, ChartEmptyState, FilteredDataBanner } from './common/ChartStates';

interface CoverageChartProps {
  data: CoverageChartData[];
  loading: boolean;
}

const CoverageChart: React.FC<CoverageChartProps> = ({ data, loading }) => {
  const [selectedUuaa, setSelectedUuaa] = useState<CoverageChartData | null>(null);

  // Hook para scroll automático cuando se selecciona una UUAA
  useAutoScroll('coverage-detail', selectedUuaa);

  // Preparar datos para filtros y tooltips (puede ser vacío si no hay datos)
  const safeData = data || [];
  const filteredData = safeData
    .filter(item => item.averageCoverage > 0 || item.applications.some(app => app.coverage > 0))
    .sort((a, b) => b.averageCoverage - a.averageCoverage); // Orden descendente

  // Hook para tooltips personalizados
  useChartTooltips(
    '.apexcharts-xaxis-texts-g text',
    (index) => createCustomTooltip(
      filteredData[index],
      createCoverageTooltipContent(filteredData[index].averageCoverage)
    ),
    filteredData.length,
    [filteredData]
  );

  // Verificar loading primero
  if (loading) {
    return <ChartLoadingState />;
  }

  // Verificar que data existe antes de usarlo
  if (!data || data.length === 0) {
    return <ChartEmptyState title="No hay datos disponibles" message="No se encontraron UUAAs con los filtros aplicados" />;
  }

  const chartData = filteredData.map(item => ({
    x: item.uuaa,
    y: item.averageCoverage,
    totalApps: item.totalApps
  }));

  const handleDataPointSelection = (dataPointIndex: number) => {
    if (dataPointIndex >= 0 && dataPointIndex < filteredData.length) {
      setSelectedUuaa(filteredData[dataPointIndex]);
    }
  };

  const handleXAxisLabelClick = (categoryIndex: number) => {
    if (categoryIndex >= 0 && categoryIndex < filteredData.length) {
      setSelectedUuaa(filteredData[categoryIndex]);
    }
  };

  const options = getBarChartOptions(
    'Coverage Promedio por UUAA',
    'coverage-chart',
    'Coverage Promedio (%)',
    (_, __, config) => handleDataPointSelection(config.dataPointIndex),
    (_, __, config) => handleXAxisLabelClick(config.labelIndex)
  );

  // Configuraciones específicas para coverage
  options.colors = [SEVERITY_COLORS.primary];
  options.xaxis!.categories = chartData.map(item => item.x);
  options.xaxis!.labels!.rotate = -45;
  options.yaxis = {
    ...(options.yaxis as any),
    min: 0,
    max: 100,
    labels: {
      formatter: (value: any) => `${value}%`,
    },
  };
  options.plotOptions!.bar!.columnWidth = '55%';
  options.stroke = {
    show: true,
    width: 2,
    colors: ['transparent'],
  };
  options.tooltip = {
    custom: ({ seriesIndex, dataPointIndex, w }: any) => {
      const data = w.config.series[seriesIndex].data[dataPointIndex];
      const uuaa = data.x;
      const coverage = data.y;
      const totalApps = data.totalApps;
      
      return `
        <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg">
          <div class="font-semibold text-gray-900">${uuaa}</div>
          <div class="text-sm text-gray-600">Coverage: ${coverage}%</div>
          <div class="text-sm text-gray-600">Total Apps: ${totalApps}</div>
        </div>
      `;
    },
  };
  options.legend = { show: false };
  options.responsive = [
    {
      breakpoint: 640,
      options: {
        chart: {
          height: 300,
        },
        xaxis: {
          labels: {
            rotate: -90,
          },
        },
      },
    },
  ];

  const series = [
    {
      name: 'Coverage Promedio',
      data: chartData,
    },
  ];

  // Calcular estadísticas generales
  const totalUUAAs = filteredData.length;
  const totalApps = filteredData.reduce((sum, item) => sum + item.totalApps, 0);
  const averageCoverageOverall = totalUUAAs > 0 
    ? filteredData.reduce((sum, item) => sum + item.averageCoverage, 0) / totalUUAAs 
    : 0;

  return (
    <div className="w-full">
      {/* Mensaje informativo sobre filtrado */}
      {data.length > filteredData.length && (
        <FilteredDataBanner 
          visibleCount={filteredData.length} 
          totalCount={data.length} 
          entityName="UUAAs"
        />
      )}

      <div className="mb-4">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 text-center">
          <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Total UUAAs</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">{totalUUAAs}</p>
          </div>
          <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Total Apps</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">{totalApps}</p>
          </div>
          <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Coverage Promedio General</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">{averageCoverageOverall.toFixed(1)}%</p>
          </div>
        </div>
      </div>

      <div className="w-full">
        <div className="mb-2">
          <p className="text-sm text-gray-600 dark:text-gray-400 text-center">
            Haz click en una barra o en el nombre de la UUAA para ver el detalle de las aplicaciones
          </p>
        </div>
        <ReactApexChart
          options={options}
          series={series}
          type="bar"
          height={400}
        />
      </div>

      {/* Gráfico de aplicaciones individuales */}
      {selectedUuaa && (
        <div id="coverage-detail">
          <ApplicationsCoverageChart
            data={selectedUuaa.applications}
            uuaaName={selectedUuaa.uuaa}
            onClose={() => setSelectedUuaa(null)}
          />
        </div>
      )}
    </div>
  );
};

export default CoverageChart;
