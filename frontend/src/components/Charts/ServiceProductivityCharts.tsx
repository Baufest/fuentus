import React, { useState, useEffect } from 'react';
import ReactApexChart from 'react-apexcharts';
import { ApexOptions } from 'apexcharts';
import { getProductividadByServiceId, getVelocidadByServiceId, ProductividadDTO, VelocidadDTO } from '../../api/statsSummaryApi';

interface ServiceProductivityChartsProps {
  serviceId: number;
}

const ServiceProductivityCharts: React.FC<ServiceProductivityChartsProps> = ({ serviceId }) => {
  const [productividadData, setProductividadData] = useState<ProductividadDTO[]>([]);
  const [velocidadData, setVelocidadData] = useState<VelocidadDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!serviceId) {
        setLoading(false);
        return;
      }
      
      setLoading(true);
      setError(null);
      
      try {
        const [prodData, velData] = await Promise.all([
          getProductividadByServiceId(serviceId),
          getVelocidadByServiceId(serviceId)
        ]);
        
        setProductividadData(prodData || []);
        setVelocidadData(velData || []);
      } catch (err) {
        console.error('Error fetching productivity data:', err);
        setError('Error al cargar los datos de productividad');
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, [serviceId]);

  if (loading) {
    return (
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        {[1, 2].map((i) => (
          <div key={i} className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark animate-pulse">
            <div className="h-6 bg-gray-200 dark:bg-gray-700 rounded w-1/3 mb-4"></div>
            <div className="h-48 bg-gray-200 dark:bg-gray-700 rounded"></div>
          </div>
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark">
        <p className="text-red-500 text-center">{error}</p>
      </div>
    );
  }

  // Si no hay datos, mostrar mensaje
  if (productividadData.length === 0 && velocidadData.length === 0) {
    return (
      <div className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark">
        <p className="text-gray-500 dark:text-gray-400 text-center">
          No hay datos de productividad y velocidad disponibles para este servicio
        </p>
      </div>
    );
  }

  // Preparar datos para el gráfico de Productividad (features / (ftesDirectos + ftesIndirectos))
  const productividadSeries = productividadData.length > 0 ? [
    {
      name: 'Productividad (Features/FTEs)',
      data: productividadData.map(p => {
        const totalFtes = (p.ftesDirectos || 0) + (p.ftesIndirectos || 0);
        if (totalFtes === 0) return 0;
        return Number(((p.features || 0) / totalFtes).toFixed(3));
      })
    }
  ] : [];

  const productividadCategories = productividadData.map(p => p.fecha || 'Sin fecha');

  const productividadOptions: ApexOptions = {
    chart: {
      type: 'bar',
      height: 250,
      toolbar: {
        show: false
      }
    },
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: '55%',
        borderRadius: 4
      }
    },
    dataLabels: {
      enabled: true,
      formatter: function (val) {
        return Number(val).toFixed(3);
      }
    },
    stroke: {
      show: true,
      width: 2,
      colors: ['transparent']
    },
    xaxis: {
      categories: productividadCategories,
      labels: {
        style: {
          colors: '#64748b'
        }
      }
    },
    yaxis: {
      title: {
        text: 'Features / FTEs',
        style: {
          color: '#64748b'
        }
      },
      labels: {
        style: {
          colors: '#64748b'
        },
        formatter: function (val) {
          return val.toFixed(3);
        }
      }
    },
    fill: {
      opacity: 1
    },
    tooltip: {
      y: {
        formatter: function (val) {
          return val.toFixed(3) + ' features/FTE';
        }
      }
    },
    colors: ['#3C50E0'],
    legend: {
      position: 'top',
      horizontalAlign: 'left',
      labels: {
        colors: '#64748b'
      }
    }
  };

  // Preparar datos para el gráfico de Velocidad (LT y CT)
  const velocidadSeries = velocidadData.length > 0 ? [
    {
      name: 'Lead Time (LT)',
      data: velocidadData.map(v => v.lt || 0)
    },
    {
      name: 'Cycle Time (CT)',
      data: velocidadData.map(v => v.ct || 0)
    }
  ] : [];

  const velocidadCategories = velocidadData.map(v => v.date || 'Sin fecha');

  const velocidadOptions: ApexOptions = {
    chart: {
      type: 'line',
      height: 250,
      toolbar: {
        show: false
      }
    },
    stroke: {
      curve: 'smooth',
      width: 3
    },
    xaxis: {
      categories: velocidadCategories,
      labels: {
        style: {
          colors: '#64748b'
        }
      }
    },
    yaxis: {
      title: {
        text: 'Días',
        style: {
          color: '#64748b'
        }
      },
      labels: {
        style: {
          colors: '#64748b'
        }
      }
    },
    tooltip: {
      y: {
        formatter: function (val) {
          return val + ' días';
        }
      }
    },
    colors: ['#8B5CF6', '#EC4899'],
    legend: {
      position: 'top',
      horizontalAlign: 'left',
      labels: {
        colors: '#64748b'
      }
    },
    markers: {
      size: 4,
      strokeWidth: 0,
      hover: {
        size: 6
      }
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
      {/* Gráfico de Productividad */}
      <div className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark">
        <h4 className="text-lg font-semibold text-black dark:text-white mb-4">
          Productividad
        </h4>
        {productividadData.length > 0 ? (
          <ReactApexChart
            options={productividadOptions}
            series={productividadSeries}
            type="bar"
            height={250}
          />
        ) : (
          <div className="flex items-center justify-center h-[250px] text-gray-500 dark:text-gray-400">
            No hay datos de productividad disponibles
          </div>
        )}
      </div>

      {/* Gráfico de Velocidad */}
      <div className="rounded-lg border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark">
        <h4 className="text-lg font-semibold text-black dark:text-white mb-4">
          Velocidad (Lead Time / Cycle Time)
        </h4>
        {velocidadData.length > 0 ? (
          <ReactApexChart
            options={velocidadOptions}
            series={velocidadSeries}
            type="line"
            height={250}
          />
        ) : (
          <div className="flex items-center justify-center h-[250px] text-gray-500 dark:text-gray-400">
            No hay datos de velocidad disponibles
          </div>
        )}
      </div>
    </div>
  );
};

export default ServiceProductivityCharts;
