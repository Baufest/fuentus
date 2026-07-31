import React from 'react';

// Usar ruta pública para la imagen
const Footer: React.FC = () => {
  return (
    <footer className="w-full py-4 bg-white dark:bg-boxdark-2 border-t border-gray-200 dark:border-strokedark flex justify-center items-center mt-auto">
      <img src="/src/images/logo/BBVA_RGB.png" alt="BBVA Logo" className="h-8 mx-2" />
      <span className="mx-2 text-gray-600 dark:text-gray-300">&copy; {new Date().getFullYear()}</span>
    </footer>
  );
};

export default Footer;
