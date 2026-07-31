import React, { useState } from 'react';
import { useComparativaProductividad, ItemGrafico, GraficoNivel } from '../../hooks/useComparativaProductividad';
import ReactApexChart from 'react-apexcharts';
import { ApexOptions } from 'apexcharts';
import DisponibilidadTable from './DisponibilidadTable';

interface ProductividadChartProps {
  selectedVertical?: string;
  selectedUol2?: string;
  selectedSn1?: string;
  selectedSn2?: string;
}

const COLORS = [
  '#3C50E0', '#6577F3', '#8FD0EF', '#0FADCF',
  '#10B981', '#F59E0B', '#EF4444', '#8B5CF6',
  '#EC4899', '#14B8A6'
];

const ProductividadChart: React.FC<ProductividadChartProps> = ({
  selectedVertical,
  selectedUol2,
  selectedSn1,
  selectedSn2
}) => {
  const [metricaSeleccionada, setMetricaSeleccionada] = useState<'productividad' | 'lt' | 'ct' | 'disponibilidad'>('productividad');

  const { data, loading, error } = useComparativaProductividad({
    vertical: selectedVertical,
    fabrica: selectedUol2,
    sn1: selectedSn1,
    sn2: selectedSn2,
    enabled: true
  });

  const formatValue = (value: number | null, metrica: string): string => {
    if (value === null || value === undefined) return 'N/A';
    
    if (metrica === 'productividad') {
      return value.toFixed(3);
    } else {
      return value.toFixed(1);
    }
  };

  const getMetricaLabel = (metrica: string): string => {
    const labels: { [key: string]: string } = {
      'productividad': 'Productividad (features/FTE)',
      'lt': 'Lead Time (días)',
      'ct': 'Cycle Time (días)'
    };
    return labels[metrica] || metrica;
  };

  const getMetricaValue = (item: ItemGrafico): number => {
    switch(metricaSeleccionada) {
      case 'productividad':
        return item.productividad || 0;
      case 'lt':
        return item.promedioLT || 0;
      case 'ct':
        return item.promedioCT || 0;
      default:
        return 0;
    }
  };

  const renderGrafico = (grafico: GraficoNivel, index: number) => {
    const categories = grafico.items.map(item => item.nombre);
    const seriesData = grafico.items.map(item => getMetricaValue(item));

    const chartOptions: ApexOptions = {
      chart: {
        type: 'bar',
        height: Math.max(300, grafico.items.length * 50),
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
        formatter: (val: number) => formatValue(val, metricaSeleccionada),
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
        },
        labels: {
          formatter: (val: string) => formatValue(parseFloat(val), metricaSeleccionada)
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
        custom: function({ dataPointIndex }) {
          const item = grafico.items[dataPointIndex];
          return `
            <div style="padding: 12px; background: white; border: 1px solid #e3e3e3; border-radius: 4px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
              <div style="font-weight: 600; margin-bottom: 8px; font-size: 14px; color: #1f2937;">${item.nombre}</div>
              <div style="font-size: 12px; color: #4b5563; line-height: 1.6;">
                <div style="margin-bottom: 4px;"><strong>Productividad:</strong> ${item.productividad !== null ? item.productividad.toFixed(3) : 'N/A'}</div>
                <div style="margin-bottom: 4px;"><strong>Promedio LT:</strong> ${item.promedioLT?.toFixed(1) || 'N/A'} días</div>
                <div style="margin-bottom: 4px;"><strong>Promedio CT:</strong> ${item.promedioCT?.toFixed(1) || 'N/A'} días</div>
                <div style="margin-bottom: 4px;"><strong>Total Features:</strong> ${item.totalFeatures || 0}</div>
                <div><strong>Total FTEs:</strong> ${item.totalFTEs?.toFixed(2) || 'N/A'}</div>
              </div>
            </div>
          `;
        }
      },
      grid: {
        borderColor: '#e5e7eb',
        strokeDashArray: 3
      }
    };

    const series = [{
      name: getMetricaLabel(metricaSeleccionada),
      data: seriesData
    }];

    return (
      <div 
        key={index} 
        className="rounded-sm border border-stroke bg-white p-7.5 shadow-default dark:border-strokedark dark:bg-boxdark mb-6"
      >
        <div className="mb-6">
          <h4 className="text-xl font-semibold text-black dark:text-white mb-2">
            {grafico.titulo}
          </h4>
          <p className="text-sm text-gray-600 dark:text-gray-400">
            Comparación de {getMetricaLabel(metricaSeleccionada).toLowerCase()} por {grafico.nivelTipo.toLowerCase()}
          </p>
        </div>
        
        <ReactApexChart
          options={chartOptions}
          series={series}
          type="bar"
          height={Math.max(300, grafico.items.length * 50)}
        />

        {/* Tabla resumen */}
        <div className="mt-6 overflow-x-auto">
          <table className="w-full table-auto">
            <thead>
              <tr className="bg-gray-2 text-left dark:bg-meta-4">
                <th className="px-4 py-3 font-medium text-black dark:text-white">
                  Nombre
                </th>
                <th className="px-4 py-3 font-medium text-black dark:text-white text-right">
                  Productividad
                </th>
                <th className="px-4 py-3 font-medium text-black dark:text-white text-right">
                  Lead Time (días)
                </th>
                <th className="px-4 py-3 font-medium text-black dark:text-white text-right">
                  Cycle Time (días)
                </th>
                <th className="px-4 py-3 font-medium text-black dark:text-white text-right">
                  Features
                </th>
                <th className="px-4 py-3 font-medium text-black dark:text-white text-right">
                  FTEs
                </th>
              </tr>
            </thead>
            <tbody>
              {grafico.items.map((item, idx) => (
                <tr key={idx} className="border-b border-stroke dark:border-strokedark">
                  <td className="px-4 py-3 text-black dark:text-white">
                    {item.nombre}
                  </td>
                  <td className="px-4 py-3 text-right font-medium text-black dark:text-white">
                    {formatValue(item.productividad, 'productividad')}
                  </td>
                  <td className="px-4 py-3 text-right text-gray-600 dark:text-gray-400">
                    {formatValue(item.promedioLT, 'lt')}
                  </td>
                  <td className="px-4 py-3 text-right text-gray-600 dark:text-gray-400">
                    {formatValue(item.promedioCT, 'ct')}
                  </td>
                  <td className="px-4 py-3 text-right text-gray-600 dark:text-gray-400">
                    {item.totalFeatures || 0}
                  </td>
                  <td className="px-4 py-3 text-right text-gray-600 dark:text-gray-400">
                    {item.totalFTEs?.toFixed(2) || 'N/A'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  return (
    <div className="rounded-sm border border-stroke bg-white px-5 pt-7.5 pb-5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
        <div>
          <h3 className="text-xl font-bold text-black dark:text-white mb-2">
            📊 Sistemática
          </h3>
          <p className="text-sm text-gray-600 dark:text-gray-400">
            {metricaSeleccionada === 'disponibilidad' 
              ? 'Monitorea la evolución de los niveles de servicio y disponibilidad'
              : 'Visualiza y compara las métricas de productividad entre diferentes niveles organizacionales'}
          </p>
        </div>

        {/* Selector de Métrica */}
        <div className="mt-4 md:mt-0">
          <label className="mb-2 block text-sm font-medium text-black dark:text-white">
            Métrica a visualizar
          </label>
          <select
            value={metricaSeleccionada}
            onChange={(e) => setMetricaSeleccionada(e.target.value as 'productividad' | 'lt' | 'ct' | 'disponibilidad')}
            className="w-full rounded border border-stroke bg-transparent py-2 px-4 text-black outline-none transition focus:border-primary active:border-primary dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
          >
            <option value="productividad">📊 Productividad</option>
            <option value="lt">⏱️ Lead Time</option>
            <option value="ct">🚀 Cycle Time</option>
            <option value="disponibilidad">🎯 Disponibilidad</option>
          </select>
        </div>
      </div>

      {/* Vista de Disponibilidad */}
      {metricaSeleccionada === 'disponibilidad' && (
        <DisponibilidadTable />
      )}

      {/* Vista de Productividad */}
      {metricaSeleccionada !== 'disponibilidad' && (
        <>
          {/* Filtros activos */}
          {(selectedVertical || selectedUol2 || selectedSn1 || selectedSn2) && (
            <div className="mb-6 flex flex-wrap gap-2">
              <span className="text-sm text-gray-600 dark:text-gray-400">Filtros activos:</span>
              {selectedVertical && (
                <span className="inline-flex items-center rounded bg-purple-100 px-2.5 py-0.5 text-xs font-medium text-purple-800 dark:bg-purple-900 dark:text-purple-300">
                  Vertical: {selectedVertical}
                </span>
              )}
              {selectedUol2 && (
                <span className="inline-flex items-center rounded bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800 dark:bg-blue-900 dark:text-blue-300">
                  Fábrica: {selectedUol2}
                </span>
              )}
              {selectedSn1 && (
                <span className="inline-flex items-center rounded bg-green-100 px-2.5 py-0.5 text-xs font-medium text-green-800 dark:bg-green-900 dark:text-green-300">
                  SN1: {selectedSn1}
                </span>
              )}
              {selectedSn2 && (
                <span className="inline-flex items-center rounded bg-amber-100 px-2.5 py-0.5 text-xs font-medium text-amber-800 dark:bg-amber-900 dark:text-amber-300">
                  SN2: {selectedSn2}
                </span>
              )}
            </div>
          )}

          {/* Loading */}
          {loading && (
            <div className="flex items-center justify-center py-16">
              <div className="h-16 w-16 animate-spin rounded-full border-4 border-solid border-primary border-t-transparent"></div>
            </div>
          )}

          {/* Error */}
          {error && (
            <div className="rounded-sm border border-red-300 bg-red-50 p-4 text-red-800 dark:border-red-700 dark:bg-red-900/20 dark:text-red-400 mb-6">
              ⚠️ Error al cargar los datos. Por favor, intenta nuevamente.
            </div>
          )}

          {/* Gráficos */}
          {!loading && !error && data && (
            <div>
              {data.graficos.length === 0 ? (
                <div className="rounded-sm border border-stroke bg-white p-8 text-center shadow-default dark:border-strokedark dark:bg-boxdark">
                  <p className="text-gray-600 dark:text-gray-400">
                    📭 No hay datos disponibles para los filtros seleccionados
                  </p>
                </div>
              ) : (
                data.graficos.map((grafico, index) => renderGrafico(grafico, index))
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default ProductividadChart;
