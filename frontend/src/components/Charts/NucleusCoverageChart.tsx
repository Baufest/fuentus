import React, { useState, useEffect } from 'react';
import ReactApexChart from 'react-apexcharts';
import { NucleusCoverageStatsSummary } from '../../types/statsSummary';
import { getCoverageAverageByLevel } from '../../api/statsSummaryApi';
import { getBarChartOptions, SEVERITY_COLORS } from '../../utils/chartConfig';
import { ChartLoadingState, ChartEmptyState } from './common/ChartStates';

interface NucleusCoverageChartProps {
  data: NucleusCoverageStatsSummary[];
  loading: boolean;
  currentLevel: 'VERTICAL' | 'UOL2' | 'SN1' | 'SN2' | 'UUAA' | 'APP';
  selectedVertical?: string;
  selectedUol2?: string;
  selectedSn1?: string;
  selectedSn2?: string;
  onItemClick?: (item: NucleusCoverageStatsSummary) => void;
  onBackClick?: () => void;
}

const NucleusCoverageChart: React.FC<NucleusCoverageChartProps> = ({
  data,
  loading,
  currentLevel,
  selectedVertical,
  selectedUol2,
  selectedSn1,
  selectedSn2,
  onItemClick,
  onBackClick
}) => {
  // Estado para el segundo gráfico (drill-down)
  const [drilldownData, setDrilldownData] = useState<NucleusCoverageStatsSummary[]>([]);
  const [drilldownLoading, setDrilldownLoading] = useState(false);
  const [selectedItem, setSelectedItem] = useState<string | null>(null);
  const [drilldownLevel, setDrilldownLevel] = useState<'UOL2' | 'SN1' | 'SN2' | 'UUAA' | 'APP' | null>(null);

  // Reset drill-down cuando cambian los datos principales
  useEffect(() => {
    setSelectedItem(null);
    setDrilldownData([]);
    setDrilldownLevel(null);
  }, [data, currentLevel]);

  // Función para cargar datos del drill-down
  const loadDrilldownData = async (clickedItem: NucleusCoverageStatsSummary) => {
    try {
      console.log('Loading drill-down data for:', clickedItem);
      
      setDrilldownLoading(true);
      setSelectedItem(clickedItem.label);

      const response = await getCoverageAverageByLevel(selectedVertical, selectedUol2, selectedSn1, selectedSn2, clickedItem.label);
      setDrilldownData(response || []);
      setDrilldownLevel('APP');
      
    } catch (error) {
      console.error('Error loading drill-down data:', error);
      setDrilldownData([]);
    } finally {
      setDrilldownLoading(false);
    }
  };

  // Verificar loading primero
  if (loading) {
    return <ChartLoadingState />;
  }

  // Verificar que data existe antes de usarlo
  if (!data || data.length === 0) {
    return <ChartEmptyState title="No hay datos disponibles" message="No se encontraron datos con los filtros aplicados" />;
  }

  // Filtrar y ordenar datos
  const filteredData = data
    .filter(item => item.coveragePercentage > 0)
    .sort((a, b) => b.coveragePercentage - a.coveragePercentage); // Orden descendente

  const chartData = filteredData.map(item => ({
    x: item.label,
    y: Math.round(item.coveragePercentage * 100) / 100, // Redondear a 2 decimales
  }));

  const handleDataPointSelection = (dataPointIndex: number) => {
    if (dataPointIndex >= 0 && dataPointIndex < filteredData.length) {
      const selectedItemData = filteredData[dataPointIndex];
      
      // Cargar datos del drill-down
      loadDrilldownData(selectedItemData);
      
      // Ejecutar callback original si existe
      if (onItemClick) {
        onItemClick(selectedItemData);
      }
    }
  };

  const handleXAxisLabelClick = (categoryIndex: number) => {
    if (categoryIndex >= 0 && categoryIndex < filteredData.length) {
      const selectedItemData = filteredData[categoryIndex];
      
      // Cargar datos del drill-down
      loadDrilldownData(selectedItemData);
      
      // Ejecutar callback original si existe
      if (onItemClick) {
        onItemClick(selectedItemData);
      }
    }
  };

  // Generar título dinámico según el nivel
  const getChartTitle = () => {
    switch (currentLevel) {
      case 'VERTICAL':
        return 'Coverage Promedio por Vertical';
      case 'UOL2':
        return `Coverage Promedio por UOL2 - ${selectedVertical}`;
      case 'SN1':
        return `Coverage Promedio por Servicio N1 - ${selectedVertical}/${selectedUol2}`;
      case 'SN2':
        return `Coverage Promedio por Servicio N2 - ${selectedVertical}/${selectedUol2}/${selectedSn1}`;
      case 'UUAA':
        return `Coverage Promedio por UUAA - ${selectedVertical}/${selectedUol2}/${selectedSn1}/${selectedSn2}`;
      case 'APP':
        return `Coverage por Aplicación - ${selectedVertical}/${selectedUol2}/${selectedSn1}/${selectedSn2}`;
      default:
        return 'Coverage Promedio por Nivel';
    }
  };

  // Generar breadcrumbs
  const renderBreadcrumbs = () => {
    const breadcrumbs = [];
    
    if (currentLevel !== 'VERTICAL') {
      breadcrumbs.push(
        <button
          key="vertical"
          onClick={onBackClick}
          className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 underline"
        >
          Verticales
        </button>
      );
    }
    
    if (selectedVertical && currentLevel !== 'UOL2') {
      if (breadcrumbs.length > 0) breadcrumbs.push(' > ');
      breadcrumbs.push(
        <button
          key="vertical-selected"
          onClick={onBackClick}
          className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 underline"
        >
          {selectedVertical}
        </button>
      );
    }
    
    if (selectedUol2 && currentLevel !== 'SN1') {
      if (breadcrumbs.length > 0) breadcrumbs.push(' > ');
      breadcrumbs.push(
        <button
          key="uol2-selected"
          onClick={onBackClick}
          className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 underline"
        >
          {selectedUol2}
        </button>
      );
    }
    
    if (selectedSn1 && (currentLevel === 'SN2' || currentLevel === 'UUAA' || currentLevel === 'APP')) {
      if (breadcrumbs.length > 0) breadcrumbs.push(' > ');
      breadcrumbs.push(
        <button
          key="sn1-selected"
          onClick={onBackClick}
          className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 underline"
        >
          {selectedSn1}
        </button>
      );
    }
    
    if (selectedSn2 && currentLevel !== 'UUAA' && currentLevel !== 'APP') {
      if (breadcrumbs.length > 0) breadcrumbs.push(' > ');
      breadcrumbs.push(
        <button
          key="sn2-selected"
          onClick={onBackClick}
          className="text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300 underline"
        >
          {selectedSn2}
        </button>
      );
    }
    
    if (currentLevel === 'UUAA') {
      if (breadcrumbs.length > 0) breadcrumbs.push(' > ');
      breadcrumbs.push(
        <span key="current" className="text-gray-700 dark:text-gray-300">
          UUAAs
        </span>
      );
    } else if (currentLevel === 'APP') {
      if (breadcrumbs.length > 0) breadcrumbs.push(' > ');
      breadcrumbs.push(
        <span key="current" className="text-gray-700 dark:text-gray-300">
          Aplicaciones
        </span>
      );
    }
    
    return breadcrumbs.length > 0 ? (
      <div className="mb-4 text-sm">
        <span className="text-gray-500 dark:text-gray-400">Navegación: </span>
        {breadcrumbs}
      </div>
    ) : null;
  };

  const options = getBarChartOptions(
    getChartTitle(),
    'nucleus-coverage-chart',
    'Coverage Promedio (%)',
    (_, __, config) => handleDataPointSelection(config.dataPointIndex),
    (_, __, config) => handleXAxisLabelClick(config.labelIndex)
  );

  // Configuraciones específicas para nucleus coverage
  options.colors = [SEVERITY_COLORS.success];
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
  options.plotOptions!.bar!.columnWidth = '60%';
  options.stroke = {
    show: true,
    width: 2,
    colors: ['transparent'],
  };
  options.tooltip = {
    custom: ({ seriesIndex, dataPointIndex, w }: any) => {
      const data = w.config.series[seriesIndex].data[dataPointIndex];
      const label = data.x;
      const coverage = data.y;
      
      return `
        <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg">
          <div class="font-semibold text-gray-900">${label}</div>
          <div class="text-sm text-gray-600">Coverage: ${coverage}%</div>
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
  const totalItems = filteredData.length;
  const averageCoverageOverall = totalItems > 0 
    ? filteredData.reduce((sum, item) => sum + item.coveragePercentage, 0) / totalItems 
    : 0;

  const getLevelDisplayName = () => {
    switch (currentLevel) {
      case 'VERTICAL': return 'Verticales';
      case 'UOL2': return 'UOL2s';
      case 'SN1': return 'Servicios N1';
      case 'SN2': return 'Servicios N2';
      case 'UUAA': return 'UUAAs';
      case 'APP': return 'Aplicaciones';
      default: return 'Items';
    }
  };

  // Función para renderizar el gráfico de drill-down
  const renderDrilldownChart = () => {
    if (!selectedItem || !drilldownLevel) return null;

    // Filtrar y ordenar datos del drill-down
    const filteredDrilldownData = drilldownData
      .filter(item => item.coveragePercentage > 0)
      .sort((a, b) => b.coveragePercentage - a.coveragePercentage);

    if (filteredDrilldownData.length === 0 && !drilldownLoading) return null;

    const chartData = filteredDrilldownData.map(item => ({
      x: item.label,
      y: Math.round(item.coveragePercentage * 100) / 100,
    }));

    const getDrilldownTitle = () => {
      switch (drilldownLevel) {
        case 'UOL2':
          return `Coverage por UOL2 - ${selectedItem}`;
        case 'SN1':
          return `Coverage por Servicio N1 - ${selectedItem}`;
        case 'SN2':
          return `Coverage por Servicio N2 - ${selectedItem}`;
        case 'UUAA':
          return `Coverage por UUAA - ${selectedItem}`;
        case 'APP':
          return `Coverage por Aplicación - ${selectedItem}`;
        default:
          return 'Coverage Detalle';
      }
    };

    const options = getBarChartOptions(
      getDrilldownTitle(),
      'nucleus-coverage-drilldown-chart',
      'Coverage Promedio (%)',
      () => {}, // No hay más drill-down
      () => {}
    );

    // Configuraciones específicas para el drill-down
    options.colors = [SEVERITY_COLORS.primary]; // Color diferente para distinguir
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
    options.plotOptions!.bar!.columnWidth = '60%';
    options.stroke = {
      show: true,
      width: 2,
      colors: ['transparent'],
    };
    options.tooltip = {
      custom: ({ seriesIndex, dataPointIndex, w }: any) => {
        const data = w.config.series[seriesIndex].data[dataPointIndex];
        const label = data.x;
        const coverage = data.y;
        
        return `
          <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg">
            <div class="font-semibold text-gray-900">${label}</div>
            <div class="text-sm text-gray-600">Coverage: ${coverage}%</div>
          </div>
        `;
      },
    };
    options.legend = { show: false };

    const series = [
      {
        name: 'Coverage Detalle',
        data: chartData,
      },
    ];

    const totalItems = filteredDrilldownData.length;
    const averageCoverage = totalItems > 0 
      ? filteredDrilldownData.reduce((sum, item) => sum + item.coveragePercentage, 0) / totalItems 
      : 0;

    const getDrilldownLevelName = () => {
      switch (drilldownLevel) {
        case 'UOL2': return 'UOL2s';
        case 'SN1': return 'Servicios N1';
        case 'SN2': return 'Servicios N2';
        case 'UUAA': return 'UUAAs';
        case 'APP': return 'Aplicaciones';
        default: return 'Items';
      }
    };

    return (
      <div className="mt-6 pt-6 border-t border-gray-200 dark:border-gray-700">
        <div className="mb-4">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
              Detalle de: {selectedItem}
            </h3>
            <button
              onClick={() => {
                setSelectedItem(null);
                setDrilldownData([]);
                setDrilldownLevel(null);
              }}
              className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
            >
              ✕ Cerrar
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-center mb-4">
            <div className="bg-blue-50 dark:bg-blue-900/20 p-3 rounded-lg">
              <p className="text-sm text-blue-600 dark:text-blue-400">Total {getDrilldownLevelName()}</p>
              <p className="text-xl font-semibold text-blue-900 dark:text-blue-200">{totalItems}</p>
            </div>
            <div className="bg-blue-50 dark:bg-blue-900/20 p-3 rounded-lg">
              <p className="text-sm text-blue-600 dark:text-blue-400">Coverage Promedio</p>
              <p className="text-xl font-semibold text-blue-900 dark:text-blue-200">{averageCoverage.toFixed(1)}%</p>
            </div>
          </div>
        </div>

        {drilldownLoading ? (
          <ChartLoadingState />
        ) : (
          <ReactApexChart
            options={options}
            series={series}
            type="bar"
            height={300}
          />
        )}
      </div>
    );
  };

  return (
    <div className="w-full">
      {renderBreadcrumbs()}

      <div className="mb-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-center">
          <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Total {getLevelDisplayName()}</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">{totalItems}</p>
          </div>
          <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Coverage Promedio General</p>
            <p className="text-xl font-semibold text-gray-900 dark:text-white">{averageCoverageOverall.toFixed(1)}%</p>
          </div>
        </div>
      </div>

      <div className="w-full">
        {currentLevel !== 'APP' && currentLevel !== 'SN2' && (
          <div className="mb-2">
            <p className="text-sm text-gray-600 dark:text-gray-400 text-center">
              Haz click en una barra o nombre para ver el detalle debajo
            </p>
          </div>
        )}
        <ReactApexChart
          options={options}
          series={series}
          type="bar"
          height={400}
        />
      </div>

      {/* Gráfico de drill-down */}
      {renderDrilldownChart()}
    </div>
  );
};

export default NucleusCoverageChart;