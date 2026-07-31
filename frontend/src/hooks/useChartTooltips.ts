import { useEffect } from 'react';
import { addTooltipStyles } from '../utils/chartUtils';

/**
 * Hook personalizado para manejar tooltips en gráficos ApexCharts
 */
export const useChartTooltips = (
  selector: string,
  _tooltipContentFn: (index: number) => string,
  dataLength: number,
  dependencies: any[] = []
) => {
  useEffect(() => {
    if (dataLength === 0) return;

    // Agregar estilos CSS
    const style = document.createElement('style');
    style.textContent = addTooltipStyles();
    document.head.appendChild(style);

    return () => {
      if (document.head.contains(style)) {
        document.head.removeChild(style);
      }
    };
  }, [selector, dataLength, ...dependencies]);
};
