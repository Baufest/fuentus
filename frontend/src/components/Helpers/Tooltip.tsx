import { twMerge } from "tailwind-merge";
import { useState, useRef, useEffect } from "react";
import { createPortal } from "react-dom";

type TooltipProps = {
  text: string;
  children: React.ReactNode;
  className?: string;
};

export const Tooltip = ({ text, children, className }: TooltipProps) => {
  const [isVisible, setIsVisible] = useState(false);
  const [position, setPosition] = useState({ top: 0, left: 0 });
  const [adjustedClasses, setAdjustedClasses] = useState("-translate-x-1/2 -translate-y-full");
  const containerRef = useRef<HTMLDivElement>(null);
  const tooltipRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!isVisible || !containerRef.current) {
      return;
    }

    const rect = containerRef.current.getBoundingClientRect();

    // Si el entorno no provee métricas reales (como en JSDOM), mantenemos la posición y clases por defecto
    const hasValidRect = rect.width !== 0 || rect.height !== 0 || rect.top !== 0 || rect.left !== 0;
    if (!hasValidRect) {
      setPosition({ top: 0, left: 0 });
      setAdjustedClasses("-translate-x-1/2 -translate-y-full");
      return;
    }

    const viewportWidth = window.innerWidth;

    // Estimamos el ancho del tooltip basado en el texto (aproximadamente 8px por caracter + padding)
    const estimatedTooltipWidth = Math.min(text.length * 8 + 24, 400); // máximo 400px

    let newTop = rect.top - 8; // 8px de margen por defecto (arriba)
    let newLeft = rect.left + rect.width / 2;
    let newClasses = "-translate-x-1/2 -translate-y-full";

    // Verificar si el tooltip se sale por la izquierda
    if (newLeft - estimatedTooltipWidth / 2 < 10) {
      newLeft = rect.left;
      newClasses = "-translate-y-full";
    }
    // Verificar si el tooltip se sale por la derecha
    else if (newLeft + estimatedTooltipWidth / 2 > viewportWidth - 10) {
      newLeft = rect.right;
      newClasses = "-translate-x-full -translate-y-full";
    }

    // Verificar si el tooltip se sale por arriba
    if (newTop < 10) {
      newTop = rect.bottom + 8; // Moverlo abajo del elemento
      if (newClasses.includes("-translate-y-full")) {
        newClasses = newClasses.replace("-translate-y-full", "");
      }
    }

    setPosition({
      top: newTop,
      left: newLeft,
    });
    setAdjustedClasses(newClasses);
  }, [isVisible, text]);

  return (
    <>
      <div
        ref={containerRef}
        className={twMerge(`inline-block ${className ?? ""}`)}
        onMouseEnter={() => setIsVisible(true)}
        onMouseLeave={() => setIsVisible(false)}
        role="tooltip"
        aria-label={text}
      >
        {children}
      </div>
      {isVisible &&
        createPortal(
          <div
            ref={tooltipRef}
            className={twMerge(
              "fixed bg-black text-white text-base rounded py-2 px-3 whitespace-nowrap pointer-events-none z-[9999]",
              adjustedClasses
            )}
            style={{
              top: `${position.top}px`,
              left: `${position.left}px`,
            }}
          >
            {text}
          </div>,
          document.body
        )}
    </>
  );
};