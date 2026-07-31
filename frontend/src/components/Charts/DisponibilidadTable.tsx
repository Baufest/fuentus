import React from 'react';

interface ServicioDisponibilidad {
  servicio: string;
  q1_2024_valor: number;
  q1_2024_senda: number;
  q2_2024_valor: number;
  q2_2024_senda: number;
  q3_2024_valor: number;
  q3_2024_senda: number;
  q4_2024_valor: number;
  q4_2024_senda: number;
}

// Datos hardcoded basados en la imagen de referencia
const DATOS_DISPONIBILIDAD: ServicioDisponibilidad[] = [
  {
    servicio: 'Servicio A',
    q1_2024_valor: 99.5,
    q1_2024_senda: 99.0,
    q2_2024_valor: 98.8,
    q2_2024_senda: 99.0,
    q3_2024_valor: 99.2,
    q3_2024_senda: 99.0,
    q4_2024_valor: 99.6,
    q4_2024_senda: 99.0
  },
  {
    servicio: 'Servicio B',
    q1_2024_valor: 98.5,
    q1_2024_senda: 99.0,
    q2_2024_valor: 99.1,
    q2_2024_senda: 99.0,
    q3_2024_valor: 99.3,
    q3_2024_senda: 99.0,
    q4_2024_valor: 99.5,
    q4_2024_senda: 99.0
  },
  {
    servicio: 'Servicio C',
    q1_2024_valor: 99.8,
    q1_2024_senda: 99.5,
    q2_2024_valor: 99.9,
    q2_2024_senda: 99.5,
    q3_2024_valor: 99.7,
    q3_2024_senda: 99.5,
    q4_2024_valor: 99.9,
    q4_2024_senda: 99.5
  },
  {
    servicio: 'Servicio D',
    q1_2024_valor: 97.5,
    q1_2024_senda: 98.0,
    q2_2024_valor: 98.2,
    q2_2024_senda: 98.0,
    q3_2024_valor: 98.5,
    q3_2024_senda: 98.0,
    q4_2024_valor: 98.8,
    q4_2024_senda: 98.0
  },
  {
    servicio: 'Servicio E',
    q1_2024_valor: 99.0,
    q1_2024_senda: 99.0,
    q2_2024_valor: 98.5,
    q2_2024_senda: 99.0,
    q3_2024_valor: 99.1,
    q3_2024_senda: 99.0,
    q4_2024_valor: 99.4,
    q4_2024_senda: 99.0
  },
  {
    servicio: 'Servicio F',
    q1_2024_valor: 99.2,
    q1_2024_senda: 99.0,
    q2_2024_valor: 99.5,
    q2_2024_senda: 99.0,
    q3_2024_valor: 99.6,
    q3_2024_senda: 99.0,
    q4_2024_valor: 99.8,
    q4_2024_senda: 99.0
  }
];

interface QuarterCellProps {
  valor: number;
  senda: number;
}

const QuarterCell: React.FC<QuarterCellProps> = ({ valor, senda }) => {
  const delta = valor - senda;
  const isDeltaNegative = delta < 0;
  
  return (
    <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
      <div className="flex flex-col gap-1">
        <div className="flex items-center justify-between">
          <span className="text-sm font-medium text-black dark:text-white">
            {valor.toFixed(2)}%
          </span>
          <span className="text-xs text-gray-500 dark:text-gray-400">
            Senda: {senda.toFixed(2)}%
          </span>
        </div>
        <div className={`text-sm font-semibold ${isDeltaNegative ? 'text-red-600 dark:text-red-400' : 'text-green-600 dark:text-green-400'}`}>
          <span className="inline-flex items-center gap-1">
            {isDeltaNegative ? '↓' : '↑'}
            {delta > 0 ? '+' : ''}{delta.toFixed(2)}
          </span>
        </div>
      </div>
    </td>
  );
};

const DisponibilidadTable: React.FC = () => {
  return (
    <div className="rounded-sm border border-stroke bg-white px-7.5 py-6 shadow-default dark:border-strokedark dark:bg-boxdark">
      <div className="mb-6">
        <h4 className="text-xl font-semibold text-black dark:text-white mb-2">
          📊 Niveles de Servicio - Disponibilidad
        </h4>
        <p className="text-sm text-gray-600 dark:text-gray-400">
          Evolución de la disponibilidad de servicios a lo largo de los trimestres. 
          El <strong>delta</strong> representa la diferencia entre el valor real y la senda (objetivo).
        </p>
      </div>

      <div className="mb-4 rounded-md bg-blue-50 dark:bg-blue-900/20 p-4 border border-blue-200 dark:border-blue-800">
        <p className="text-sm text-blue-800 dark:text-blue-300">
          ℹ️ <strong>Nota:</strong> Esta vista muestra información independiente de los filtros de búsqueda avanzada.
          Los valores en <span className="text-green-600 dark:text-green-400 font-semibold">verde</span> indican que el servicio superó la senda,
          mientras que los valores en <span className="text-red-600 dark:text-red-400 font-semibold">rojo</span> indican que estuvo por debajo.
        </p>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full table-auto">
          <thead>
            <tr className="bg-gray-2 text-left dark:bg-meta-4">
              <th className="min-w-[150px] py-4 px-4 font-semibold text-black dark:text-white border-b border-[#eee] dark:border-strokedark">
                Servicio
              </th>
              <th className="min-w-[180px] py-4 px-4 font-semibold text-black dark:text-white border-b border-[#eee] dark:border-strokedark text-center">
                Q1 2024
              </th>
              <th className="min-w-[180px] py-4 px-4 font-semibold text-black dark:text-white border-b border-[#eee] dark:border-strokedark text-center">
                Q2 2024
              </th>
              <th className="min-w-[180px] py-4 px-4 font-semibold text-black dark:text-white border-b border-[#eee] dark:border-strokedark text-center">
                Q3 2024
              </th>
              <th className="min-w-[180px] py-4 px-4 font-semibold text-black dark:text-white border-b border-[#eee] dark:border-strokedark text-center">
                Q4 2024
              </th>
            </tr>
          </thead>
          <tbody>
            {DATOS_DISPONIBILIDAD.map((servicio, index) => (
              <tr key={index} className="hover:bg-gray-50 dark:hover:bg-meta-4 transition-colors">
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-sm font-semibold text-black dark:text-white">
                    {servicio.servicio}
                  </p>
                </td>
                <QuarterCell 
                  valor={servicio.q1_2024_valor} 
                  senda={servicio.q1_2024_senda}
                />
                <QuarterCell 
                  valor={servicio.q2_2024_valor} 
                  senda={servicio.q2_2024_senda}
                />
                <QuarterCell 
                  valor={servicio.q3_2024_valor} 
                  senda={servicio.q3_2024_senda}
                />
                <QuarterCell 
                  valor={servicio.q4_2024_valor} 
                  senda={servicio.q4_2024_senda}
                />
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Resumen estadístico */}
      <div className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="rounded-md border border-stroke bg-white p-4 shadow-sm dark:border-strokedark dark:bg-boxdark-2">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-2xl">📈</span>
            <h5 className="text-sm font-medium text-gray-600 dark:text-gray-400">Promedio Q4 2024</h5>
          </div>
          <p className="text-2xl font-bold text-black dark:text-white">
            {(DATOS_DISPONIBILIDAD.reduce((acc, s) => acc + s.q4_2024_valor, 0) / DATOS_DISPONIBILIDAD.length).toFixed(2)}%
          </p>
        </div>
        
        <div className="rounded-md border border-stroke bg-white p-4 shadow-sm dark:border-strokedark dark:bg-boxdark-2">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-2xl">🎯</span>
            <h5 className="text-sm font-medium text-gray-600 dark:text-gray-400">Servicios sobre senda</h5>
          </div>
          <p className="text-2xl font-bold text-green-600 dark:text-green-400">
            {DATOS_DISPONIBILIDAD.filter(s => s.q4_2024_valor >= s.q4_2024_senda).length} / {DATOS_DISPONIBILIDAD.length}
          </p>
        </div>

        <div className="rounded-md border border-stroke bg-white p-4 shadow-sm dark:border-strokedark dark:bg-boxdark-2">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-2xl">⚠️</span>
            <h5 className="text-sm font-medium text-gray-600 dark:text-gray-400">Servicios bajo senda</h5>
          </div>
          <p className="text-2xl font-bold text-red-600 dark:text-red-400">
            {DATOS_DISPONIBILIDAD.filter(s => s.q4_2024_valor < s.q4_2024_senda).length} / {DATOS_DISPONIBILIDAD.length}
          </p>
        </div>
      </div>
    </div>
  );
};

export default DisponibilidadTable;
