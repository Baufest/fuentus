import { ApexOptions } from 'apexcharts';

/**
 * Configuración base común para todos los gráficos ApexCharts
 */
export const getBaseChartOptions = (): Partial<ApexOptions> => ({
  chart: {
    fontFamily: 'Satoshi, sans-serif',
    toolbar: {
      show: false,
    },
  },
  dataLabels: {
    enabled: false
  },
  xaxis: {
    labels: {
      style: {
        colors: '#6b7280',
        fontSize: '12px'
      }
    }
  },
  yaxis: {
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
  tooltip: {
    shared: true,
    intersect: false
  },
  responsive: [{
    breakpoint: 480,
    options: {
      legend: {
        position: 'bottom',
        offsetX: -10,
        offsetY: 0
      }
    }
  }],
  theme: {
    mode: 'light'
  }
});

/**
 * Configuración específica para gráficos de barras
 */
export const getBarChartOptions = (
  title: string,
  chartId: string,
  yAxisTitle: string,
  onDataPointSelection?: (event: any, chartContext: any, config: any) => void,
  onXAxisLabelClick?: (event: any, chartContext: any, config: any) => void
): ApexOptions => ({
  ...getBaseChartOptions(),
  chart: {
    ...getBaseChartOptions().chart,
    type: 'bar',
    id: chartId,
    events: {
      dataPointSelection: onDataPointSelection,
      xAxisLabelClick: onXAxisLabelClick
    }
  },
  title: {
    text: title,
    align: 'left',
    style: {
      fontSize: '16px',
      fontWeight: 'bold',
      color: '#374151'
    }
  },
  plotOptions: {
    bar: {
      horizontal: false,
      columnWidth: '50%',
      borderRadius: 4
    },
  },
  yaxis: {
    ...getBaseChartOptions().yaxis,
    title: {
      text: yAxisTitle,
      style: {
        color: '#6b7280'
      }
    }
  }
});

/**
 * Configuración para gráficos de barras apiladas
 */
export const getStackedBarChartOptions = (
  title: string,
  chartId: string,
  yAxisTitle: string,
  onDataPointSelection?: (event: any, chartContext: any, config: any) => void,
  onXAxisLabelClick?: (event: any, chartContext: any, config: any) => void
): ApexOptions => ({
  ...getBarChartOptions(title, chartId, yAxisTitle, onDataPointSelection, onXAxisLabelClick),
  chart: {
    ...getBarChartOptions(title, chartId, yAxisTitle, onDataPointSelection, onXAxisLabelClick).chart,
    stacked: true,
  }
});

/**
 * Configuración para gráficos donut
 */
export const getDonutChartOptions = (
  colors: string[],
  labels: string[],
  onDataPointSelection?: (event: any, chartContext: any, config: any) => void
): ApexOptions => ({
  chart: {
    fontFamily: 'Satoshi, sans-serif',
    type: 'donut',
    events: {
      dataPointSelection: onDataPointSelection
    }
  },
  colors: colors,
  labels: labels,
  legend: {
    show: false,
    position: 'bottom',
  },
  plotOptions: {
    pie: {
      donut: {
        size: '65%',
        background: 'transparent',
      },
    },
  },
  dataLabels: {
    enabled: false,
  },
  responsive: [
    {
      breakpoint: 2600,
      options: {
        chart: {
          width: 380,
        },
      },
    },
    {
      breakpoint: 640,
      options: {
        chart: {
          width: 200,
        },
      },
    },
  ],
});

/**
 * Colores predefinidos para diferentes niveles de severidad
 */
export const SEVERITY_COLORS = {
  success: '#10b981',     // green-500
  warning: '#f59e0b',     // amber-500  
  error: '#f87171',       // red-400
  critical: '#dc2626',    // red-600
  primary: '#1973b8'      // blue-600
} as const;
