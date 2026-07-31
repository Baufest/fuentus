import React from 'react';
import ReactApexChart from 'react-apexcharts';
import { ApexOptions } from 'apexcharts';
import { ApplicationCoverageData } from '../../types/statsSummary';
import { useNavigate } from 'react-router-dom';
import Button from '../Buttons/Button';

interface ApplicationsCoverageChartProps {
  data: ApplicationCoverageData[];
  uuaaName: string;
  onClose: () => void;
}

const ApplicationsCoverageChart: React.FC<ApplicationsCoverageChartProps> = ({ 
  data, 
  uuaaName, 
  onClose 
}) => {
  const navigate = useNavigate();

  const handleViewDetail = () => {
    // Navegar a la página de detalle con la UUAA como parámetro
    navigate(`/detalle/${uuaaName}`, {
      state: { 
        row: { 
          uuaa: [uuaaName] // Formato esperado por la página de detalle
        } 
      }
    });
  };

  if (data.length === 0) {
    return (
      <div className="mt-6 p-4 border border-gray-200 dark:border-gray-700 rounded-lg">
        <div className="flex justify-between items-center mb-4">
          <h4 className="text-lg font-semibold text-black dark:text-white">
            Aplicaciones de {uuaaName}
          </h4>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
          >
            ✕
          </button>
        </div>
        <p className="text-gray-500 dark:text-gray-400">No hay aplicaciones con datos de coverage.</p>
      </div>
    );
  }

  // Preparar datos para el gráfico
  const chartData = data.map(app => ({
    x: app.name.length > 20 ? `${app.name.substring(0, 20)}...` : app.name,
    y: app.coverage,
    fullName: app.name,
    bitbucketUrl: app.bitbucketUrl
  }));

  const options: ApexOptions = {
    chart: {
      type: 'bar',
      fontFamily: 'Satoshi, sans-serif',
      toolbar: {
        show: false,
      },
      height: 400,
    },
    colors: ['#0f766e'], // Color diferente para distinguir del gráfico principal
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: '60%',
        borderRadius: 4,
      },
    },
    dataLabels: {
      enabled: false,
    },
    stroke: {
      show: true,
      width: 2,
      colors: ['transparent'],
    },
    xaxis: {
      categories: chartData.map(item => item.x),
      title: {
        text: 'Aplicaciones',
        style: {
          fontSize: '14px',
          fontWeight: 'bold',
        },
      },
      labels: {
        rotate: -45,
        style: {
          fontSize: '11px',
        },
        maxHeight: 120,
      },
    },
    yaxis: {
      title: {
        text: 'Coverage (%)',
        style: {
          fontSize: '14px',
          fontWeight: 'bold',
        },
      },
      min: 0,
      max: 100,
      labels: {
        formatter: (value) => `${value}%`,
      },
    },
    tooltip: {
      custom: ({ seriesIndex, dataPointIndex, w }) => {
        const data = w.config.series[seriesIndex].data[dataPointIndex];
        const fullName = data.fullName;
        const coverage = data.y;
        const bitbucketUrl = data.bitbucketUrl;
        
        return `
          <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg max-w-xs">
            <div class="font-semibold text-gray-900 mb-1">${fullName}</div>
            <div class="text-sm text-gray-600 mb-1">Coverage: ${coverage}%</div>
            ${bitbucketUrl ? `<div class="text-xs text-blue-600 truncate">BitBucket: ${bitbucketUrl}</div>` : ''}
          </div>
        `;
      },
    },
    grid: {
      borderColor: '#f1f5f9',
      strokeDashArray: 4,
    },
    legend: {
      show: false,
    },
    responsive: [
      {
        breakpoint: 768,
        options: {
          chart: {
            height: 350,
          },
          xaxis: {
            labels: {
              rotate: -90,
            },
          },
        },
      },
    ],
  };

  const series = [
    {
      name: 'Coverage',
      data: chartData,
    },
  ];

  // Calcular estadísticas
  const totalApps = data.length;
  const averageCoverage = data.reduce((sum, app) => sum + app.coverage, 0) / totalApps;
  const maxCoverage = Math.max(...data.map(app => app.coverage));
  const minCoverage = Math.min(...data.map(app => app.coverage));

  return (
    <div className="mt-6 p-4 border border-gray-200 dark:border-gray-700 rounded-lg bg-gray-50 dark:bg-gray-800">
      <div className="flex justify-between items-center mb-4">
        <h4 className="text-lg font-semibold text-black dark:text-white">
          Aplicaciones de {uuaaName}
        </h4>
        <button
          onClick={onClose}
          className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 text-xl font-bold w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-200 dark:hover:bg-gray-600"
          title="Cerrar detalle"
        >
          ✕
        </button>
      </div>

      <div className="mb-4">
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-center">
          <div className="bg-white dark:bg-gray-700 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Total Apps</p>
            <p className="text-lg font-semibold text-gray-900 dark:text-white">{totalApps}</p>
          </div>
          <div className="bg-white dark:bg-gray-700 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Promedio</p>
            <p className="text-lg font-semibold text-gray-900 dark:text-white">{averageCoverage.toFixed(1)}%</p>
          </div>
          <div className="bg-white dark:bg-gray-700 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Máximo</p>
            <p className="text-lg font-semibold text-gray-900 dark:text-white">{maxCoverage.toFixed(1)}%</p>
          </div>
          <div className="bg-white dark:bg-gray-700 p-3 rounded-lg">
            <p className="text-sm text-gray-600 dark:text-gray-400">Mínimo</p>
            <p className="text-lg font-semibold text-gray-900 dark:text-white">{minCoverage.toFixed(1)}%</p>
          </div>
        </div>
      </div>

      <div className="mb-4 flex justify-center">
        <Button 
          onClick={handleViewDetail}
          variant="primary"
          size="sm"
          className="!px-6 !py-2"
        >
          Ver Detalle de {uuaaName}
        </Button>
      </div>

      <div className="w-full">
        <ReactApexChart
          options={options}
          series={series}
          type="bar"
          height={400}
        />
      </div>
    </div>
  );
};

export default ApplicationsCoverageChart;
