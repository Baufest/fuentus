import React from 'react';
import ReactApexChart from 'react-apexcharts';
import { ApexOptions } from 'apexcharts';
import { ChimeraChartData } from '../../types/statsSummary';
import Badge from '../Badges/Badge';
import { SiBitbucket } from 'react-icons/si';
import { TbShieldCheck, TbPackage } from 'react-icons/tb';
import { Tooltip } from '../Helpers/Tooltip';
import Button from '../Buttons/Button';

interface ApplicationsChimeraChartProps {
  uuaaData: ChimeraChartData;
}

const ApplicationsChimeraChart: React.FC<ApplicationsChimeraChartProps> = ({ uuaaData }) => {
  if (uuaaData.applications.length === 0) {
    return (
      <div className="flex items-center justify-center h-32 text-gray-500 dark:text-gray-400">
        <p>No hay aplicaciones disponibles para esta UUAA</p>
      </div>
    );
  }

  // Handler para click en barra SAST
  const handleSastBarClick = (_event: any, _chartContext: any, config: any) => {
    const appIndex = config.dataPointIndex;
    const app = uuaaData.applications[appIndex];
    if (app && app.chimeraUrl) {
      window.open(app.chimeraUrl, '_blank', 'noopener,noreferrer');
    }
  };

  // Handler para click en barra SCA
  const handleScaBarClick = (_event: any, _chartContext: any, config: any) => {
    const appIndex = config.dataPointIndex;
    const app = uuaaData.applications[appIndex];
    if (app && app.chimeraUrl) {
      // Si la URL contiene "sast", reemplazar por "sca"
      const scaUrl = app.chimeraUrl.replace('sast', 'sca');
      window.open(scaUrl, '_blank', 'noopener,noreferrer');
    }
  };

  // Preparar datos para el gráfico de aplicaciones SAST (orden: Low, Medium, High)
  const sastAppSeries = [
    {
      name: 'Low',
      data: uuaaData.applications.map(app => app.chimeraSast.totalLow || 0),
      color: '#10b981' // green-500 (success)
    },
    {
      name: 'Medium', 
      data: uuaaData.applications.map(app => app.chimeraSast.totalMedium || 0),
      color: '#f59e0b' // amber-500 (warning)
    },
    {
      name: 'High',
      data: uuaaData.applications.map(app => app.chimeraSast.totalHigh || 0),
      color: '#f87171' // red-400 (más claro que critical)
    }
  ];

  // Preparar datos para el gráfico de aplicaciones SCA (orden: Low, Medium, High, Critical)
  const scaAppSeries = [
    {
      name: 'Low',
      data: uuaaData.applications.map(app => app.chimeraSca.totalLow || 0),
      color: '#10b981' // green-500 (success)
    },
    {
      name: 'Medium',
      data: uuaaData.applications.map(app => app.chimeraSca.totalMedium || 0),
      color: '#f59e0b' // amber-500 (warning)
    },
    {
      name: 'High',
      data: uuaaData.applications.map(app => app.chimeraSca.totalHigh || 0),
      color: '#f87171' // red-400 (más claro que critical)
    },
    {
      name: 'Critical',
      data: uuaaData.applications.map(app => app.chimeraSca.totalCritical || 0),
      color: '#dc2626' // red-600 (más oscuro para critical)
    }
  ];

  const appCategories = uuaaData.applications.map(app => app.name);

  const commonAppOptions: Partial<ApexOptions> = {
    chart: {
      type: 'bar',
      fontFamily: 'Satoshi, sans-serif',
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
      stacked: true,
    },
    plotOptions: {
      bar: {
        horizontal: true,
        columnWidth: '70%',
        borderRadius: 2
      },
    },
    dataLabels: {
      enabled: false
    },
    xaxis: {
      categories: appCategories,
      labels: {
        style: {
          colors: '#6b7280',
          fontSize: '11px'
        }
      }
    },
    yaxis: {
      title: {
        text: 'Vulnerabilidades',
        style: {
          color: '#6b7280'
        }
      },
      labels: {
        style: {
          colors: '#6b7280'
        }
      }
    },
    legend: {
      position: 'top',
      horizontalAlign: 'left',
      labels: {
        colors: '#6b7280'
      }
    },
    grid: {
      borderColor: '#e5e7eb',
      strokeDashArray: 0,
    },
    responsive: [{
      breakpoint: 768,
      options: {
        legend: {
          position: 'bottom'
        }
      }
    }]
  };

  const sastAppOptions: ApexOptions = {
    ...commonAppOptions,
    title: {
      text: 'Vulnerabilidades SAST por Aplicación',
      align: 'left',
      style: {
        fontSize: '14px',
        fontWeight: 'bold',
        color: '#374151'
      }
    },
    chart: {
      ...commonAppOptions.chart,
      events: {
        dataPointSelection: handleSastBarClick
      }
    }
  };

  const scaAppOptions: ApexOptions = {
    ...commonAppOptions,
    title: {
      text: 'Vulnerabilidades SCA por Aplicación',
      align: 'left',
      style: {
        fontSize: '14px',
        fontWeight: 'bold',
        color: '#374151'
      }
    },
    chart: {
      ...commonAppOptions.chart,
      events: {
        dataPointSelection: handleScaBarClick
      }
    }
  };

  const getBadgeColor = (severity: string): 'primary' | 'success' | 'error' | 'warning' | 'info' | 'light' | 'dark' => {
    switch (severity.toLowerCase()) {
      case 'critical':
        return 'error';
      case 'high':
        return 'error';
      case 'medium':
        return 'warning';
      case 'low':
        return 'success';
      default:
        return 'light';
    }
  };

  return (
    <div className="space-y-6">
      {/* Gráficos de aplicaciones */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* SAST Applications Chart */}
        <div className="bg-white dark:bg-boxdark rounded-lg border border-stroke dark:border-strokedark p-4">
          <ReactApexChart
            options={sastAppOptions}
            series={sastAppSeries}
            type="bar"
            height={Math.max(300, uuaaData.applications.length * 40)}
          />
        </div>

        {/* SCA Applications Chart */}
        <div className="bg-white dark:bg-boxdark rounded-lg border border-stroke dark:border-strokedark p-4">
          <ReactApexChart
            options={scaAppOptions}
            series={scaAppSeries}
            type="bar"
            height={Math.max(300, uuaaData.applications.length * 40)}
          />
        </div>
      </div>

      {/* Tabla detallada de aplicaciones */}
      <div className="bg-white dark:bg-boxdark rounded-lg border border-stroke dark:border-strokedark p-4">
        <h4 className="text-lg font-semibold text-black dark:text-white mb-4">
          Detalle de Aplicaciones
        </h4>
        
        <div className="overflow-x-auto">
          <table className="min-w-full">
            <thead>
              <tr className="bg-gray-50 dark:bg-gray-800">
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Aplicación
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  Enlaces
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  SAST
                </th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                  SCA
                </th>
              </tr>
            </thead>
            <tbody className="bg-white dark:bg-boxdark divide-y divide-gray-200 dark:divide-gray-700">
              {uuaaData.applications.map((app, index) => (
                <tr key={index} className="hover:bg-gray-50 dark:hover:bg-gray-800">
                  <td className="px-4 py-4 whitespace-nowrap">
                    <div className="text-sm font-medium text-gray-900 dark:text-white">
                      {app.name}
                    </div>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    <div className="flex items-center space-x-2">
                      <Tooltip text="Ver repositorio Bitbucket">
                        <Button variant="outline" size="sm" className="!px-2 !py-2">
                          <a href={app.bitbucketUrl} target="_blank" rel="noopener noreferrer">
                            <SiBitbucket className="h-4 w-4" />
                          </a>
                        </Button>
                      </Tooltip>
                      
                      <Tooltip text="Ver reporte Chimera SAST">
                        <Button variant="outline" size="sm" className="!px-2 !py-2">
                          <a href={app.chimeraUrl} target="_blank" rel="noopener noreferrer">
                            <TbShieldCheck className="h-4 w-4" />
                          </a>
                        </Button>
                      </Tooltip>
                      
                      <Tooltip text="Ver reporte Chimera SCA">
                        <Button variant="outline" size="sm" className="!px-2 !py-2">
                          <a 
                            href={app.chimeraUrl?.replace('sast', 'sca') ?? "#"} 
                            target="_blank" 
                            rel="noopener noreferrer"
                          >
                            <TbPackage className="h-4 w-4" />
                          </a>
                        </Button>
                      </Tooltip>
                    </div>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    <div className="flex items-center space-x-1">
                      <Badge size="sm" color={getBadgeColor('high')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSast.totalHigh || 0}H
                        </span>
                      </Badge>
                      <Badge size="sm" color={getBadgeColor('medium')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSast.totalMedium || 0}M
                        </span>
                      </Badge>
                      <Badge size="sm" color={getBadgeColor('low')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSast.totalLow || 0}L
                        </span>
                      </Badge>
                    </div>
                  </td>
                  <td className="px-4 py-4 whitespace-nowrap">
                    <div className="flex items-center space-x-1">
                      <Badge size="sm" color={getBadgeColor('critical')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSca.totalCritical || 0}C
                        </span>
                      </Badge>
                      <Badge size="sm" color={getBadgeColor('high')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSca.totalHigh || 0}H
                        </span>
                      </Badge>
                      <Badge size="sm" color={getBadgeColor('medium')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSca.totalMedium || 0}M
                        </span>
                      </Badge>
                      <Badge size="sm" color={getBadgeColor('low')}>
                        <span className="w-8 text-center inline-block">
                          {app.chimeraSca.totalLow || 0}L
                        </span>
                      </Badge>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ApplicationsChimeraChart;
