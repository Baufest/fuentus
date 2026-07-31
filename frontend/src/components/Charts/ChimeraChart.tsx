import React, { useState } from 'react';
import ReactApexChart from 'react-apexcharts';
import { ChimeraChartData } from '../../types/statsSummary';
import ApplicationsChimeraChart from './ApplicationsChimeraChart';
import { useAutoScroll } from '../../hooks/useAutoScroll';
import { useChartTooltips } from '../../hooks/useChartTooltips';
import { getStackedBarChartOptions, SEVERITY_COLORS } from '../../utils/chartConfig';
import { createCustomTooltip, formatChimeraTooltipContent } from '../../utils/chartUtils';
import { ChartLoadingState, ChartEmptyState, FilteredDataBanner, InfoPanel } from './common/ChartStates';

interface ChimeraChartProps {
  data: ChimeraChartData[];
  loading: boolean;
}

const ChimeraChart: React.FC<ChimeraChartProps> = ({ data, loading }) => {
  const [selectedUuaa, setSelectedUuaa] = useState<ChimeraChartData | null>(null);

  // Hook para scroll automático cuando se selecciona una UUAA
  useAutoScroll('chimera-detail', selectedUuaa);

  // Los datos ya vienen filtrados y ordenados por transformToChimeraChartData
  const filteredData = data;

  // Crear ordenamientos independientes para cada gráfico
  const sastOrderedData = [...filteredData].sort((a, b) => {
    const totalA = a.sastData.totalLow + a.sastData.totalMedium + a.sastData.totalHigh;
    const totalB = b.sastData.totalLow + b.sastData.totalMedium + b.sastData.totalHigh;
    return totalB - totalA; // Mayor a menor
  });

  const scaOrderedData = [...filteredData].sort((a, b) => {
    const totalA = a.scaData.totalLow + a.scaData.totalMedium + a.scaData.totalHigh + a.scaData.totalCritical;
    const totalB = b.scaData.totalLow + b.scaData.totalMedium + b.scaData.totalHigh + b.scaData.totalCritical;
    return totalB - totalA; // Mayor a menor
  });

  // Hook para tooltips personalizados
  useChartTooltips(
    '#sast-chart .apexcharts-xaxis-texts-g text',
    (index) => createCustomTooltip(
      sastOrderedData[index],
      formatChimeraTooltipContent('SAST', sastOrderedData[index].sastData)
    ),
    sastOrderedData.length,
    [sastOrderedData]
  );

  useChartTooltips(
    '#sca-chart .apexcharts-xaxis-texts-g text',
    (index) => createCustomTooltip(
      scaOrderedData[index],
      formatChimeraTooltipContent('SCA', scaOrderedData[index].scaData)
    ),
    scaOrderedData.length,
    [scaOrderedData]
  );

  if (loading) {
    return <ChartLoadingState />;
  }

  if (data.length === 0) {
    return <ChartEmptyState title="No hay datos disponibles" message="No se encontraron UUAAs con los filtros aplicados" />;
  }

  // Preparar datos para SAST (orden: Low, Medium, High - de abajo hacia arriba)
  const sastSeries = [
    {
      name: 'Low',
      data: sastOrderedData.map(item => item.sastData.totalLow),
      color: SEVERITY_COLORS.success
    },
    {
      name: 'Medium',
      data: sastOrderedData.map(item => item.sastData.totalMedium),
      color: SEVERITY_COLORS.warning
    },
    {
      name: 'High',
      data: sastOrderedData.map(item => item.sastData.totalHigh),
      color: SEVERITY_COLORS.error
    }
  ];

  // Preparar datos para SCA (orden: Low, Medium, High, Critical - de abajo hacia arriba)
  const scaSeries = [
    {
      name: 'Low',
      data: scaOrderedData.map(item => item.scaData.totalLow),
      color: SEVERITY_COLORS.success
    },
    {
      name: 'Medium',
      data: scaOrderedData.map(item => item.scaData.totalMedium),
      color: SEVERITY_COLORS.warning
    },
    {
      name: 'High',
      data: scaOrderedData.map(item => item.scaData.totalHigh),
      color: SEVERITY_COLORS.error
    },
    {
      name: 'Critical',
      data: scaOrderedData.map(item => item.scaData.totalCritical),
      color: SEVERITY_COLORS.critical
    }
  ];

  // Categorías independientes para cada gráfico
  const sastCategories = sastOrderedData.map(item => item.uuaa);
  const scaCategories = scaOrderedData.map(item => item.uuaa);

  // Manejadores de eventos para cada gráfico
  const handleSastDataPointSelection = (dataPointIndex: number) => {
    if (dataPointIndex >= 0 && dataPointIndex < sastOrderedData.length) {
      setSelectedUuaa(sastOrderedData[dataPointIndex]);
    }
  };

  const handleSastXAxisLabelClick = (categoryIndex: number) => {
    if (categoryIndex >= 0 && categoryIndex < sastOrderedData.length) {
      setSelectedUuaa(sastOrderedData[categoryIndex]);
    }
  };

  const handleScaDataPointSelection = (dataPointIndex: number) => {
    if (dataPointIndex >= 0 && dataPointIndex < scaOrderedData.length) {
      setSelectedUuaa(scaOrderedData[dataPointIndex]);
    }
  };

  const handleScaXAxisLabelClick = (categoryIndex: number) => {
    if (categoryIndex >= 0 && categoryIndex < scaOrderedData.length) {
      setSelectedUuaa(scaOrderedData[categoryIndex]);
    }
  };

  const sastOptions = getStackedBarChartOptions(
    'Vulnerabilidades SAST por UUAA',
    'sast-chart',
    'Vulnerabilidades',
    (_, __, config) => handleSastDataPointSelection(config.dataPointIndex),
    (_, __, config) => handleSastXAxisLabelClick(config.labelIndex)
  );
  sastOptions.xaxis!.categories = sastCategories;

  const scaOptions = getStackedBarChartOptions(
    'Vulnerabilidades SCA por UUAA',
    'sca-chart',
    'Vulnerabilidades',
    (_, __, config) => handleScaDataPointSelection(config.dataPointIndex),
    (_, __, config) => handleScaXAxisLabelClick(config.labelIndex)
  );
  scaOptions.xaxis!.categories = scaCategories;

  return (
    <div className="space-y-6">
      {/* Mensaje informativo sobre filtrado */}
      {data.length > filteredData.length && (
        <FilteredDataBanner 
          visibleCount={filteredData.length} 
          totalCount={data.length} 
          entityName="UUAAs"
        />
      )}

      {/* Gráficos principales SAST y SCA */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Gráfico SAST */}
        <div className="bg-white dark:bg-boxdark rounded-lg border border-stroke dark:border-strokedark p-4">
          <ReactApexChart
            options={sastOptions}
            series={sastSeries}
            type="bar"
            height={350}
          />
        </div>

        {/* Gráfico SCA */}
        <div className="bg-white dark:bg-boxdark rounded-lg border border-stroke dark:border-strokedark p-4">
          <ReactApexChart
            options={scaOptions}
            series={scaSeries}
            type="bar"
            height={350}
          />
        </div>
      </div>

      {/* Información adicional */}
      <InfoPanel title="Información sobre Chimera" variant="blue">
        <p>Haz clic en cualquier barra o en el nombre de la UUAA para ver el detalle por aplicación.</p>
        <p className="mt-1">
          <strong>SAST:</strong> Análisis estático de código fuente.{' '}
          <strong>SCA:</strong> Análisis de componentes de software.
        </p>
      </InfoPanel>

      {/* Detalle de aplicaciones para la UUAA seleccionada */}
      {selectedUuaa && (
        <div id="chimera-detail" className="mt-8">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-xl font-semibold text-black dark:text-white">
              Detalle de Vulnerabilidades - {selectedUuaa.uuaa}
            </h3>
            <button
              onClick={() => setSelectedUuaa(null)}
              className="text-sm text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 bg-gray-100 dark:bg-gray-800 px-3 py-1 rounded-md"
            >
              Cerrar detalle
            </button>
          </div>
          
          <ApplicationsChimeraChart uuaaData={selectedUuaa} />
        </div>
      )}
    </div>
  );
};

export default ChimeraChart;
