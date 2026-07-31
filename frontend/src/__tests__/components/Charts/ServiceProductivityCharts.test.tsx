import { render, screen, waitFor } from '@testing-library/react';
import ServiceProductivityCharts from '../../../components/Charts/ServiceProductivityCharts';
import { getProductividadByServiceId, getVelocidadByServiceId, ProductividadDTO, VelocidadDTO } from '../../../api/statsSummaryApi';
import '@testing-library/jest-dom';

// Mock de ApexCharts para evitar problemas de renderizado
const mockApexChart = jest.fn();
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: function MockApexChart(props: any) {
    mockApexChart(props);
    return (
      <div
        data-testid={`mock-chart-${props.type}`}
        data-series={JSON.stringify(props.series)}
        data-options={JSON.stringify(props.options)}
        data-type={props.type}
        data-height={props.height}
      >
        Mocked Chart - {props.type}
      </div>
    );
  }
}));

// Mock de las funciones de la API
jest.mock('../../../api/statsSummaryApi', () => ({
  getProductividadByServiceId: jest.fn(),
  getVelocidadByServiceId: jest.fn()
}));

const mockedGetProductividadByServiceId = getProductividadByServiceId as jest.MockedFunction<typeof getProductividadByServiceId>;
const mockedGetVelocidadByServiceId = getVelocidadByServiceId as jest.MockedFunction<typeof getVelocidadByServiceId>;

describe('ServiceProductivityCharts', () => {
  const mockProductividadData: ProductividadDTO[] = [
    {
      id: 1,
      nucleusId: 100,
      servicioN2: 'Service 1',
      fecha: '2024-01',
      features: 10,
      ftesDirectos: 5,
      ftesIndirectos: 3
    },
    {
      id: 2,
      nucleusId: 100,
      servicioN2: 'Service 1',
      fecha: '2024-02',
      features: 15,
      ftesDirectos: 6,
      ftesIndirectos: 2
    },
    {
      id: 3,
      nucleusId: 100,
      servicioN2: 'Service 1',
      fecha: '2024-03',
      features: 12,
      ftesDirectos: 4,
      ftesIndirectos: 4
    }
  ];

  const mockVelocidadData: VelocidadDTO[] = [
    {
      id: 1,
      nucleusId: 100,
      servicioN2: 'Service 1',
      date: '2024-01',
      lt: 15.5,
      ct: 8.3
    },
    {
      id: 2,
      nucleusId: 100,
      servicioN2: 'Service 1',
      date: '2024-02',
      lt: 12.8,
      ct: 7.1
    },
    {
      id: 3,
      nucleusId: 100,
      servicioN2: 'Service 1',
      date: '2024-03',
      lt: 18.2,
      ct: 9.5
    }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    mockApexChart.mockClear();
  });

  it('renders loading state', () => {
    mockedGetProductividadByServiceId.mockReturnValue(new Promise(() => {}));
    mockedGetVelocidadByServiceId.mockReturnValue(new Promise(() => {}));

    render(<ServiceProductivityCharts serviceId={1} />);

    const loadingElements = screen.getAllByText((_content, element) => {
      return element?.classList.contains('animate-pulse') || false;
    });
    expect(loadingElements.length).toBeGreaterThan(0);
  });

  it('renders error state when API calls fail', async () => {
    mockedGetProductividadByServiceId.mockRejectedValue(new Error('API Error'));
    mockedGetVelocidadByServiceId.mockRejectedValue(new Error('API Error'));

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByText('Error al cargar los datos de productividad')).toBeInTheDocument();
    });
  });

  it('renders empty state when no data is available', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue([]);
    mockedGetVelocidadByServiceId.mockResolvedValue([]);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByText('No hay datos de productividad y velocidad disponibles para este servicio')).toBeInTheDocument();
    });
  });

  it('renders both charts with data correctly', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByTestId('mock-chart-bar')).toBeInTheDocument();
      expect(screen.getByTestId('mock-chart-line')).toBeInTheDocument();
    });

    expect(screen.getByText('Productividad')).toBeInTheDocument();
    expect(screen.getByText('Velocidad (Lead Time / Cycle Time)')).toBeInTheDocument();
  });

  it('calls API with correct serviceId', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={123} />);

    await waitFor(() => {
      expect(mockedGetProductividadByServiceId).toHaveBeenCalledWith(123);
      expect(mockedGetVelocidadByServiceId).toHaveBeenCalledWith(123);
    });
  });

  it('does not fetch data when serviceId is 0', async () => {
    render(<ServiceProductivityCharts serviceId={0} />);

    await waitFor(() => {
      expect(mockedGetProductividadByServiceId).not.toHaveBeenCalled();
      expect(mockedGetVelocidadByServiceId).not.toHaveBeenCalled();
    });
  });

  it('renders productividad chart with correct data structure', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const barChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'bar');
    expect(barChartCall).toBeDefined();
    
    const series = barChartCall?.[0].series;
    expect(series).toBeDefined();
    expect(series[0].name).toBe('Productividad (Features/FTEs)');
    expect(series[0].data).toHaveLength(3);
    
    // Verificar cálculo: features / (ftesDirectos + ftesIndirectos)
    // Para el primer dato: 10 / (5 + 3) = 1.25
    expect(series[0].data[0]).toBeCloseTo(1.25, 3);
  });

  it('renders velocidad chart with correct data structure', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const lineChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'line');
    expect(lineChartCall).toBeDefined();
    
    const series = lineChartCall?.[0].series;
    expect(series).toBeDefined();
    expect(series).toHaveLength(2);
    expect(series[0].name).toBe('Lead Time (LT)');
    expect(series[1].name).toBe('Cycle Time (CT)');
    expect(series[0].data).toEqual([15.5, 12.8, 18.2]);
    expect(series[1].data).toEqual([8.3, 7.1, 9.5]);
  });

  it('handles division by zero in productividad calculation', async () => {
    const dataWithZeroFTEs: ProductividadDTO[] = [
      {
        id: 1,
        nucleusId: 100,
        servicioN2: 'Service 1',
        fecha: '2024-01',
        features: 10,
        ftesDirectos: 0,
        ftesIndirectos: 0
      }
    ];

    mockedGetProductividadByServiceId.mockResolvedValue(dataWithZeroFTEs);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const barChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'bar');
    const series = barChartCall?.[0].series;
    
    // Cuando los FTEs son 0, el resultado debe ser 0
    expect(series[0].data[0]).toBe(0);
  });

  it('renders only productividad chart when velocidad data is empty', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue([]);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByText('Productividad')).toBeInTheDocument();
      expect(screen.getByText('Velocidad (Lead Time / Cycle Time)')).toBeInTheDocument();
      expect(screen.getByText('No hay datos de velocidad disponibles')).toBeInTheDocument();
    });
  });

  it('renders only velocidad chart when productividad data is empty', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue([]);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByText('Productividad')).toBeInTheDocument();
      expect(screen.getByText('Velocidad (Lead Time / Cycle Time)')).toBeInTheDocument();
      expect(screen.getByText('No hay datos de productividad disponibles')).toBeInTheDocument();
    });
  });

  it('handles null or undefined values in productividad data', async () => {
    const dataWithNulls: ProductividadDTO[] = [
      {
        id: 1,
        nucleusId: 100,
        servicioN2: 'Service 1',
        fecha: '2024-01',
        features: undefined as any,
        ftesDirectos: null as any,
        ftesIndirectos: 3
      }
    ];

    mockedGetProductividadByServiceId.mockResolvedValue(dataWithNulls);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByTestId('mock-chart-bar')).toBeInTheDocument();
    });

    // No debe lanzar error, debe manejar los valores null/undefined
    const barChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'bar');
    expect(barChartCall).toBeDefined();
  });

  it('handles null or undefined values in velocidad data', async () => {
    const dataWithNulls: VelocidadDTO[] = [
      {
        id: 1,
        nucleusId: 100,
        servicioN2: 'Service 1',
        date: '2024-01',
        lt: undefined as any,
        ct: null as any
      }
    ];

    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(dataWithNulls);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(screen.getByTestId('mock-chart-line')).toBeInTheDocument();
    });

    const lineChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'line');
    const series = lineChartCall?.[0].series;
    
    // Debe convertir null/undefined a 0
    expect(series[0].data[0]).toBe(0);
    expect(series[1].data[0]).toBe(0);
  });

  it('refetches data when serviceId changes', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    const { rerender } = render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockedGetProductividadByServiceId).toHaveBeenCalledWith(1);
    });

    jest.clearAllMocks();

    rerender(<ServiceProductivityCharts serviceId={2} />);

    await waitFor(() => {
      expect(mockedGetProductividadByServiceId).toHaveBeenCalledWith(2);
      expect(mockedGetVelocidadByServiceId).toHaveBeenCalledWith(2);
    });
  });

  it('formats productividad categories correctly', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const barChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'bar');
    const options = barChartCall?.[0].options;
    
    expect(options.xaxis.categories).toEqual(['2024-01', '2024-02', '2024-03']);
  });

  it('formats velocidad categories correctly', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const lineChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'line');
    const options = lineChartCall?.[0].options;
    
    expect(options.xaxis.categories).toEqual(['2024-01', '2024-02', '2024-03']);
  });

  it('applies correct chart configuration for productividad', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const barChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'bar');
    const options = barChartCall?.[0].options;
    
    expect(options.chart.type).toBe('bar');
    expect(options.chart.height).toBe(250);
    expect(options.chart.toolbar.show).toBe(false);
    expect(options.yaxis.title.text).toBe('Features / FTEs');
  });

  it('applies correct chart configuration for velocidad', async () => {
    mockedGetProductividadByServiceId.mockResolvedValue(mockProductividadData);
    mockedGetVelocidadByServiceId.mockResolvedValue(mockVelocidadData);

    render(<ServiceProductivityCharts serviceId={1} />);

    await waitFor(() => {
      expect(mockApexChart).toHaveBeenCalled();
    });

    const lineChartCall = mockApexChart.mock.calls.find(call => call[0].type === 'line');
    const options = lineChartCall?.[0].options;
    
    expect(options.chart.type).toBe('line');
    expect(options.chart.height).toBe(250);
    expect(options.chart.toolbar.show).toBe(false);
    expect(options.yaxis.title.text).toBe('Días');
    expect(options.stroke.curve).toBe('smooth');
  });
});
