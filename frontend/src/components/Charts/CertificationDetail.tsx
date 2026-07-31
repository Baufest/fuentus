import React from 'react';
import { DashboardDataRow } from '../../data/dashboardData';
import Button from '../Buttons/Button';

interface CertificationDetailProps {
  data: DashboardDataRow[];
  selectedLevel: string;
  onClose: () => void;
}

const CertificationDetail: React.FC<CertificationDetailProps> = ({ 
  data, 
  selectedLevel, 
  onClose 
}) => {
  // Función para obtener el estilo del badge según el nivel de certificación
  const getCertificationBadgeStyle = (nivel: string) => {
    if (!nivel) {
      return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
    }
    if (nivel.includes('Level 3')) {
      return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
    } else if (nivel.includes('Level 2')) {
      return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
    } else if (nivel.includes('Level 1')) {
      return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
    } else {
      return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
    }
  };

  // Función para obtener el badge de certificación (Sí/No)
  const getCertificationStatus = (certificacion: string) => {
    // El campo certificacion contiene "1" para certificado y "0" para no certificado
    const isCertified = certificacion === "1";
    
    return {
      text: isCertified ? 'Sí' : 'No',
      style: isCertified 
        ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
        : 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
    };
  };

  // Filtrar datos por el nivel de certificación seleccionado
  const filteredData = data.filter(row => 
    (row.nivel_certificacion || 'No Certificado') === selectedLevel
  );

  if (filteredData.length === 0) {
    return (
      <div className="mt-6 p-4 border border-gray-200 dark:border-gray-700 rounded-lg">
        <div className="flex justify-between items-center mb-4">
          <h4 className="text-lg font-semibold text-gray-900 dark:text-white">
            Detalle: {selectedLevel}
          </h4>
          <button 
            onClick={onClose} 
            className="px-3 py-1 border border-gray-300 rounded text-sm hover:bg-gray-100 dark:border-gray-600 dark:hover:bg-gray-700"
          >
            ✕
          </button>
        </div>
        <p className="text-gray-600 dark:text-gray-400">
          No hay servicios con el nivel de certificación "{selectedLevel}".
        </p>
      </div>
    );
  }

  // Agrupar por UOL2 para mostrar un resumen por UUAA
  const groupedData = filteredData.reduce((acc, row) => {
    const key = row.uol2_name;
    if (!acc[key]) {
      acc[key] = {
        uol2_name: row.uol2_name,
        uol1_name: row.uol1_name,
        ug_name: row.ug_name,
        vertical: row.vertical,
        services: []
      };
    }
    acc[key].services.push(row);
    return acc;
  }, {} as Record<string, {
    uol2_name: string;
    uol1_name: string;
    ug_name: string;
    vertical: string;
    services: DashboardDataRow[];
  }>);

  const groupedArray = Object.values(groupedData);

  return (
    <div className="mt-6 p-4 border border-gray-200 dark:border-gray-700 rounded-lg bg-white dark:bg-boxdark">
      <div className="flex justify-between items-center mb-4">
        <h4 className="text-lg font-semibold text-gray-900 dark:text-white">
          Detalle: {selectedLevel} ({filteredData.length} servicios)
        </h4>
        <Button 
          onClick={onClose} 
          variant="outline" 
          size="sm"
          className="!px-3 !py-1"
        >
          ✕
        </Button>
      </div>

      {/* Estadísticas del nivel seleccionado */}
      <div className="grid grid-cols-1 sm:grid-cols-4 gap-4 mb-6">
        <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
          <p className="text-sm text-gray-600 dark:text-gray-400">Total Servicios</p>
          <p className="text-xl font-semibold text-gray-900 dark:text-white">{filteredData.length}</p>
        </div>
        <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
          <p className="text-sm text-gray-600 dark:text-gray-400">UOL2 Únicas</p>
          <p className="text-xl font-semibold text-gray-900 dark:text-white">{groupedArray.length}</p>
        </div>
        <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
          <p className="text-sm text-gray-600 dark:text-gray-400">Geografías</p>
          <p className="text-xl font-semibold text-gray-900 dark:text-white">
            {new Set(filteredData.map(row => row.ug_name)).size}
          </p>
        </div>
        <div className="bg-gray-50 dark:bg-gray-800 p-3 rounded-lg">
          <p className="text-sm text-gray-600 dark:text-gray-400">Verticales</p>
          <p className="text-xl font-semibold text-gray-900 dark:text-white">
            {new Set(filteredData.map(row => row.vertical)).size}
          </p>
        </div>
      </div>

      {/* Tabla de servicios individuales */}
      <div className="w-full">
        <table className="w-full table-fixed text-xs border-collapse">
          <thead>
            <tr className="bg-gray-50 dark:bg-gray-800">
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-16">
                Geo.
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-20">
                UOL1
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-28">
                UOL2
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-28">
                Servicio L1
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-12">
                RFO
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-16">
                Depend.
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-16">
                Features
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-16">
                Certif.
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-16">
                Op.Model
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-16">
                Vulner.
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-12">
                Adop.
              </th>
              <th className="px-2 py-2 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider w-20">
                Nivel
              </th>
            </tr>
          </thead>
          <tbody className="bg-white dark:bg-boxdark divide-y divide-gray-200 dark:divide-gray-700">
            {filteredData.map((service, index) => (
              <tr key={`${service.servicel1_id}-${service.period_month}-${index}`} className="hover:bg-gray-50 dark:hover:bg-gray-800">
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate" title={service.ug_name}>
                    {service.ug_name}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate" title={service.uol1_name}>
                    {service.uol1_name}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate" title={service.uol2_name}>
                    {service.uol2_name}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs font-medium text-gray-900 dark:text-white truncate" title={service.servicel1_name}>
                    {service.servicel1_name}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate">
                    {service.fichas_rfo_status_ok}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate">
                    {service.sn2_dependencias_asignadas}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate">
                    {service.calidad_features}
                  </div>
                </td>
                <td className="px-2 py-2">
                  {(() => {
                    const certStatus = getCertificationStatus(service.certificacion);
                    return (
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${certStatus.style}`}>
                        {certStatus.text}
                      </span>
                    );
                  })()}
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate">
                    {service.operating_model}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate">
                    {service.evolucion_vulnerabilidades}
                  </div>
                </td>
                <td className="px-2 py-2 truncate">
                  <div className="text-xs text-gray-900 dark:text-white truncate">
                    {service.adopcion_total}
                  </div>
                </td>
                <td className="px-2 py-2">
                  <span className={`inline-flex px-1 py-1 text-xs font-semibold rounded-full ${getCertificationBadgeStyle(service.nivel_certificacion)}`}>
                    {service.nivel_certificacion}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Lista detallada de todos los servicios */}
      <div className="mt-6">
        <h5 className="text-md font-semibold text-gray-900 dark:text-white mb-3">
          Lista Completa de Servicios
        </h5>
        <div className="bg-gray-50 dark:bg-gray-800 rounded-lg p-4 max-h-60 overflow-y-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
            {filteredData.map((service) => (
              <div key={`${service.servicel1_id}-${service.period_month}`} className="text-sm text-gray-700 dark:text-gray-300 py-1">
                <span className="font-medium">{service.servicel1_name}</span>
                <span className="text-gray-500 dark:text-gray-400 ml-2">
                  ({service.servicel1_id})
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CertificationDetail;
