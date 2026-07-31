import React, { useState } from 'react';
import { useDeudaTecnica, DeudaTecnicaItem } from '../../hooks/useDeudaTecnica';
import ReactApexChart from 'react-apexcharts';
import { ApexOptions } from 'apexcharts';

interface DeudaTecnicaChartProps {
  selectedVertical?: string;
  selectedUol2?: string;
  selectedSn1?: string;
  onDrillDown?: (label: string, level: string) => void;
  onBackClick?: () => void;
}

type MetricType = 'bugs' | 'vulnerabilities' | 'codeSmells';

const COLORS = [
  '#3C50E0', '#6577F3', '#8FD0EF', '#0FADCF',
  '#10B981', '#F59E0B', '#EF4444', '#8B5CF6',
  '#EC4899', '#14B8A6'
];

const DeudaTecnicaChart: React.FC<DeudaTecnicaChartProps> = ({
  selectedVertical,
  selectedUol2,
  selectedSn1,
  onDrillDown,
  onBackClick
}) => {
  const [metricaSeleccionada, setMetricaSeleccionada] = useState<MetricType>('bugs');

  const { data, loading, error } = useDeudaTecnica({
    vertical: selectedVertical,
    fabrica: selectedUol2,
    sn1: selectedSn1
  });

  const getMetricaLabel = (metrica: MetricType): string => {
    const labels: { [key in MetricType]: string } = {
      'bugs': 'Bugs',
      'vulnerabilities': 'Vulnerabilidades',
      'codeSmells': 'Code Smells'
    };
    return labels[metrica];
  };

  const getMetricaValue = (item: DeudaTecnicaItem): number => {
    switch(metricaSeleccionada) {
      case 'bugs':
        return item.totalBugs || 0;
      case 'vulnerabilities':
        return item.totalVulnerabilities || 0;
      case 'codeSmells':
        return item.totalCodeSmells || 0;
      default:
        return 0;
    }
  };

  const handleBarClick = (_event: any, _chartContext: any, config: any) => {
    if (onDrillDown && data[config.dataPointIndex]) {
      const selectedItem = data[config.dataPointIndex];
      onDrillDown(selectedItem.label, selectedItem.nivelTipo);
    }
  };

  const categories = data.map(item => item.label);
  const seriesData = data.map(item => getMetricaValue(item));

  const chartOptions: ApexOptions = {
    chart: {
      type: 'bar',
      height: Math.max(300, data.length * 50),
      toolbar: {
        show: true,
        tools: {
          download: true,
          selection: false,
          zoom: false,
          zoomin: false,
          zoomout: false,
          pan: false,
          reset: false
        }
      },
      events: {
        dataPointSelection: handleBarClick
      }
    },
    plotOptions: {
      bar: {
        horizontal: true,
        distributed: true,
        dataLabels: {
          position: 'top'
        },
        barHeight: '70%'
      }
    },
    colors: COLORS,
    dataLabels: {
      enabled: true,
      formatter: (val: number) => val.toString(),
      offsetX: 30,
      style: {
        fontSize: '12px',
        colors: ['#304758'],
        fontWeight: 600
      }
    },
    xaxis: {
      categories: categories,
      title: {
        text: getMetricaLabel(metricaSeleccionada),
        style: {
          fontSize: '14px',
          fontWeight: 600
        }
      }
    },
    yaxis: {
      title: {
        text: ''
      },
      labels: {
        style: {
          fontSize: '12px'
        }
      }
    },
    legend: {
      show: false
    },
    tooltip: {
      custom: ({ dataPointIndex }) => {
        const item = data[dataPointIndex];
        return `
          <div class="apexcharts-tooltip-custom" style="padding: 10px; background: #fff; border: 1px solid #e3e3e3;">
            <div style="font-weight: 600; margin-bottom: 5px;">${item.label}</div>
            <div>Bugs: ${item.totalBugs}</div>
            <div>Vulnerabilidades: ${item.totalVulnerabilities}</div>
            <div>Code Smells: ${item.totalCodeSmells}</div>
            <div style="margin-top: 5px; border-top: 1px solid #e3e3e3; padding-top: 5px;">
              Total Apps: ${item.totalApps}
            </div>
          </div>
        `;
      }
    },
    grid: {
      borderColor: '#e7e7e7',
      strokeDashArray: 5
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-800 rounded-lg p-4">
        <p className="font-semibold">Error al cargar datos de Deuda Técnica:</p>
        <p className="text-sm">{error}</p>
      </div>
    );
  }

  if (!data || data.length === 0) {
    return (
      <div className="bg-yellow-50 border border-yellow-200 text-yellow-800 rounded-lg p-4">
        <p>No hay datos disponibles para los filtros seleccionados.</p>
      </div>
    );
  }

  const nivelTipo = data.length > 0 ? data[0].nivelTipo : '';
  const getNivelLabel = (): string => {
    switch(nivelTipo) {
      case 'VERTICAL': return 'Comparación por Vertical';
      case 'FABRICA': return 'Comparación por Fábrica';
      case 'SN1': return 'Comparación por SN1';
      case 'SN2': return 'Comparación por SN2';
      default: return 'Comparación';
    }
  };

  const showBackButton = selectedVertical || selectedUol2 || selectedSn1;

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      {showBackButton && onBackClick && (
        <div className="mb-4">
          <button
            onClick={onBackClick}
            className="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-md transition-colors duration-200 flex items-center gap-2"
          >
            <span>←</span>
            <span>Volver</span>
          </button>
        </div>
      )}

      <div className="flex justify-between items-center mb-6">
        <h3 className="text-xl font-bold text-gray-800">{getNivelLabel()}</h3>
        
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium text-gray-700">Métrica:</label>
          <select
            value={metricaSeleccionada}
            onChange={(e) => setMetricaSeleccionada(e.target.value as MetricType)}
            className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="bugs">Bugs</option>
            <option value="vulnerabilities">Vulnerabilidades</option>
            <option value="codeSmells">Code Smells</option>
          </select>
        </div>
      </div>

      <div className="mb-4 text-sm text-gray-600">
        <p>Haz clic en una barra para ver el desglose del siguiente nivel.</p>
      </div>

      <ReactApexChart
        options={chartOptions}
        series={[{ name: getMetricaLabel(metricaSeleccionada), data: seriesData }]}
        type="bar"
        height={Math.max(300, data.length * 50)}
      />
    </div>
  );
};

export default DeudaTecnicaChart;
