/**
 * Utilidades comunes para componentes de gráficos
 */

export interface TooltipData {
  uuaa: string;
  totalApps: number;
  [key: string]: any;
}

/**
 * Crea el contenido HTML para tooltips personalizados
 */
export const createCustomTooltip = (data: TooltipData, extraContent?: string): string => {
  return `
    <div class="font-semibold text-gray-900">${data.uuaa}</div>
    <div class="text-sm text-gray-600">Total Apps: ${data.totalApps}</div>
    ${extraContent ? `<div class="text-sm text-gray-600">${extraContent}</div>` : ''}
  `;
};

/**
 * Agrega event listeners para tooltips personalizados
 */
export const addTooltipEventListeners = (chartRef: any, onTooltipClick?: (data: any) => void) => {
  if (!chartRef?.current) return;

  const chart = chartRef.current.chart;
  if (!chart) return;

  chart.addEventListener('dataPointSelection', (_event: any, _chartContext: any, config: any) => {
    if (onTooltipClick) {
      onTooltipClick({
        dataPointIndex: config.dataPointIndex,
        seriesIndex: config.seriesIndex,
        w: config.w
      });
    }
  });
};

/**
 * Remueve event listeners de tooltips
 */
export const removeTooltipEventListeners = (chartRef: any) => {
  if (!chartRef?.current) return;

  const chart = chartRef.current.chart;
  if (!chart) return;

  // ApexCharts no tiene un método directo para remover listeners específicos
  // pero al destruir/recrear el chart se limpian automáticamente
};

/**
 * Formatea datos de cobertura para tooltips
 */
export const formatCoverageTooltip = (coverage: number, appName?: string): string => {
  let coverageClass = 'text-red-600';
  if (coverage >= 80) {
    coverageClass = 'text-green-600';
  } else if (coverage >= 60) {
    coverageClass = 'text-yellow-600';
  }
  
  return `
    <div class="p-2 bg-white border border-gray-200 rounded shadow-lg">
      ${appName ? `<div class="font-medium text-gray-900 mb-1">${appName}</div>` : ''}
      <div class="flex items-center">
        <span class="text-sm text-gray-600 mr-2">Cobertura:</span>
        <span class="font-semibold ${coverageClass}">${coverage}%</span>
      </div>
    </div>
  `;
};

/**
 * Formatea datos de vulnerabilidades para tooltips
 */
export const formatVulnerabilityTooltip = (sast: any, sca: any, appName?: string): string => {
  return `
    <div class="p-2 bg-white border border-gray-200 rounded shadow-lg">
      ${appName ? `<div class="font-medium text-gray-900 mb-2">${appName}</div>` : ''}
      <div class="space-y-1">
        <div class="text-sm">
          <span class="text-gray-600">SAST:</span>
          <span class="ml-2 text-red-600">${sast.totalHigh}H</span>
          <span class="ml-1 text-orange-600">${sast.totalMedium}M</span>
          <span class="ml-1 text-yellow-600">${sast.totalLow}L</span>
        </div>
        <div class="text-sm">
          <span class="text-gray-600">SCA:</span>
          <span class="ml-2 text-purple-600">${sca.totalCritical}C</span>
          <span class="ml-1 text-red-600">${sca.totalHigh}H</span>
          <span class="ml-1 text-orange-600">${sca.totalMedium}M</span>
          <span class="ml-1 text-yellow-600">${sca.totalLow}L</span>
        </div>
      </div>
    </div>
  `;
};

/**
 * Agrega estilos CSS para tooltips personalizados
 */
export const addTooltipStyles = (): string => {
  return `
    .custom-tooltip {
      background: white;
      border: 1px solid #e5e7eb;
      border-radius: 6px;
      padding: 8px 12px;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
      font-size: 14px;
      max-width: 300px;
      z-index: 1000;
    }
    
    .custom-tooltip .tooltip-title {
      font-weight: 600;
      color: #1f2937;
      margin-bottom: 4px;
    }
    
    .custom-tooltip .tooltip-content {
      color: #6b7280;
      line-height: 1.4;
    }
  `;
};

/**
 * Crea contenido para tooltip de cobertura específico
 */
export const createCoverageTooltipContent = (params: any): string => {
  const { series, dataPointIndex, w } = params;
  const coverage = series[0][dataPointIndex];
  const category = w.globals.labels[dataPointIndex];
  
  return formatCoverageTooltip(coverage, category);
};

/**
 * Crea contenido para tooltip de Chimera específico
 */
export const createChimeraTooltipContent = (params: any): string => {
  const { series, dataPointIndex, w } = params;
  const category = w.globals.labels[dataPointIndex];
  
  // Simular datos SAST/SCA basados en los valores del gráfico
  const sastValue = series[0] ? series[0][dataPointIndex] : 0;
  const scaValue = series[1] ? series[1][dataPointIndex] : 0;
  
  const sast = {
    totalHigh: Math.floor(sastValue * 0.3),
    totalMedium: Math.floor(sastValue * 0.5),
    totalLow: Math.floor(sastValue * 0.2)
  };
  
  const sca = {
    totalCritical: Math.floor(scaValue * 0.1),
    totalHigh: Math.floor(scaValue * 0.3),
    totalMedium: Math.floor(scaValue * 0.4),
    totalLow: Math.floor(scaValue * 0.2)
  };
  
  return formatVulnerabilityTooltip(sast, sca, category);
};

/**
 * Crea contenido para tooltip de Chimera con datos directos
 */
export const formatChimeraTooltipContent = (type: string, data: any): string => {
  if (type === 'SAST') {
    return `
      <div class="p-2 bg-white border border-gray-200 rounded shadow-lg">
        <div class="font-medium text-gray-900 mb-2">${type} Vulnerabilities</div>
        <div class="space-y-1">
          <div class="text-sm">
            <span class="text-red-600">${data.totalHigh}H</span>
            <span class="ml-1 text-orange-600">${data.totalMedium}M</span>
            <span class="ml-1 text-yellow-600">${data.totalLow}L</span>
          </div>
        </div>
      </div>
    `;
  } else if (type === 'SCA') {
    return `
      <div class="p-2 bg-white border border-gray-200 rounded shadow-lg">
        <div class="font-medium text-gray-900 mb-2">${type} Vulnerabilities</div>
        <div class="space-y-1">
          <div class="text-sm">
            <span class="text-purple-600">${data.totalCritical}C</span>
            <span class="ml-1 text-red-600">${data.totalHigh}H</span>
            <span class="ml-1 text-orange-600">${data.totalMedium}M</span>
            <span class="ml-1 text-yellow-600">${data.totalLow}L</span>
          </div>
        </div>
      </div>
    `;
  }
  return '';
};

/**
 * Maneja el scroll suave a un elemento
 */
export const scrollToElement = (elementId: string, delay: number = 100): void => {
  setTimeout(() => {
    const element = document.getElementById(elementId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }, delay);
};
