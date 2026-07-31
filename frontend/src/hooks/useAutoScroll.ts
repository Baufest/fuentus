import { useEffect } from 'react';
import { scrollToElement } from '../utils/chartUtils';

/**
 * Hook personalizado para manejar scroll automático a elementos
 */
export const useAutoScroll = (
  elementId: string,
  trigger: any,
  delay: number = 100
) => {
  useEffect(() => {
    if (trigger) {
      return scrollToElement(elementId, delay);
    }
  }, [trigger, elementId, delay]);
};
