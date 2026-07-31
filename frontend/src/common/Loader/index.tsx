import React from 'react';

interface LoaderProps {
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  color?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'white' | 'gray';
  className?: string;
  fullScreen?: boolean;
  thickness?: number;
}

const Loader: React.FC<LoaderProps> = ({ 
  size = 'md', 
  color = 'primary', 
  className = '',
  fullScreen,
  thickness = 4
}) => {
  // Tamaños del loader
  const sizeClasses = {
    xs: 'h-4 w-4',
    sm: 'h-6 w-6', 
    md: 'h-8 w-8',
    lg: 'h-12 w-12',
    xl: 'h-16 w-16'
  };

  // Colores del loader
  const colorClasses = {
    primary: 'border-primary-500', // Usar el shade 500 para compatibilidad con el nuevo tailwind.config
    secondary: 'border-secondary',
    success: 'border-green-500',
    danger: 'border-red-500',
    warning: 'border-yellow-500',
    info: 'border-blue-500',
    white: 'border-white',
    gray: 'border-gray-500'
  };

  // Contenedor del loader - usar Boolean() para manejar undefined
  const containerClasses = Boolean(fullScreen)
    ? 'fixed inset-0 z-50 flex h-screen items-center justify-center bg-white'
    : 'flex items-center justify-center';

  // Clases de grosor del borde válidas en Tailwind
  const getThicknessClass = (thickness: number) => {
    switch (thickness) {
      case 1: return 'border';
      case 2: return 'border-2';
      case 4: return 'border-4';
      case 8: return 'border-8';
      default: return 'border-4'; // fallback
    }
  };

  // Usar style inline para thickness que no tiene clase en Tailwind
  const useInlineStyle = ![1, 2, 4, 8].includes(thickness);
  const thicknessClass = useInlineStyle ? 'border-4' : getThicknessClass(thickness);

  // Estilos del spinner
  const spinnerClasses = `
    ${sizeClasses[size]} 
    animate-spin 
    rounded-full 
    border-solid 
    ${colorClasses[color]} 
    border-t-transparent
    ${thicknessClass}
    ${className}
  `.trim();

  return (
    <div className={containerClasses}>
      <div 
        className={spinnerClasses}
        style={useInlineStyle ? { borderWidth: `${thickness}px` } : {}}
      ></div>
    </div>
  );
};

export default Loader;
