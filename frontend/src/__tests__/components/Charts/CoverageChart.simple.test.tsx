import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import CoverageChart from '../../../components/Charts/CoverageChart';
import { CoverageChartData } from '../../../types/statsSummary';
import '@testing-library/jest-dom';

// Mock de ApexCharts para evitar problemas de renderizado
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: ({ series, options, type, height }: any) => (
    <div 
      data-testid="coverage-chart"
      data-series={JSON.stringify(series)}
      data-options={JSON.stringify(options)}
      data-type={type}
      data-height={height}
      onClick={() => {
        // Simulate dataPointSelection event
        if (options.chart?.events?.dataPointSelection) {
          options.chart.events.dataPointSelection({}, {}, { dataPointIndex: 0 });
        }
      }}
    >
      Mocked Chart
    </div>
  )
}));

// Mock del componente ApplicationsCoverageChart
jest.mock('../../../components/Charts/ApplicationsCoverageChart', () => {
  return function MockApplicationsCoverageChart({ data, uuaaName, onClose }: any) {
    return (
      <div data-testid="applications-coverage-chart">
        <h4>Applications for {uuaaName}</h4>
        <div>Count: {data.length}</div>
        <button onClick={onClose} data-testid="close-detail">Close</button>
      </div>
    );
  };
});

const renderWithRouter = (component: React.ReactElement) => {
  return render(<Router>{component}</Router>);
};

describe('CoverageChart - Simple Tests', () => {
  const mockData: CoverageChartData[] = [
    { 
      uuaa: 'TEST001', 
      averageCoverage: 85.5, 
      totalApps: 3,
      applications: [
        { name: 'App1', coverage: 80, bitbucketUrl: 'http://example.com/app1' },
        { name: 'App2', coverage: 85, bitbucketUrl: 'http://example.com/app2' },
        { name: 'App3', coverage: 92, bitbucketUrl: 'http://example.com/app3' }
      ]
    },
    { 
      uuaa: 'TEST002', 
      averageCoverage: 72.3, 
      totalApps: 2,
      applications: [
        { name: 'App4', coverage: 70, bitbucketUrl: 'http://example.com/app4' },
        { name: 'App5', coverage: 75, bitbucketUrl: 'http://example.com/app5' }
      ]
    }
  ];

  test('renders without crashing', () => {
    const { container } = renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    expect(container).toBeInTheDocument();
  });

  test('shows loading state', () => {
    renderWithRouter(<CoverageChart data={[]} loading={true} />);
    // Should show loading spinner
    expect(document.querySelector('.animate-spin')).toBeInTheDocument();
    expect(screen.queryByTestId('coverage-chart')).not.toBeInTheDocument();
  });

  test('renders chart when not loading', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    // Should render the mocked chart
    expect(screen.getByTestId('coverage-chart')).toBeInTheDocument();
  });

  test('shows no data message when empty', () => {
    renderWithRouter(<CoverageChart data={[]} loading={false} />);
    
    // Should show no data message
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    expect(screen.getByText('No se encontraron UUAAs con los filtros aplicados')).toBeInTheDocument();
  });

  test('displays statistics correctly', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    // Should show statistics
    expect(screen.getByText('Total UUAAs')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument(); // Total UUAAs
    expect(screen.getByText('Total Apps')).toBeInTheDocument();
    expect(screen.getByText('5')).toBeInTheDocument(); // Total Apps (3+2)
    expect(screen.getByText('Coverage Promedio General')).toBeInTheDocument();
    expect(screen.getByText('78.9%')).toBeInTheDocument(); // Average (85.5+72.3)/2
  });

  test('renders instruction text', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    expect(screen.getByText('Haz click en una barra o en el nombre de la UUAA para ver el detalle de las aplicaciones')).toBeInTheDocument();
  });

  test('chart receives correct data', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const chart = screen.getByTestId('coverage-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series).toHaveLength(1);
    expect(series[0].name).toBe('Coverage Promedio');
    expect(series[0].data).toHaveLength(2);
    expect(series[0].data[0]).toEqual({ x: 'TEST001', y: 85.5, totalApps: 3 });
    expect(series[0].data[1]).toEqual({ x: 'TEST002', y: 72.3, totalApps: 2 });
  });

  test('chart has correct type and height', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const chart = screen.getByTestId('coverage-chart');
    expect(chart.getAttribute('data-type')).toBe('bar');
    expect(chart.getAttribute('data-height')).toBe('400');
  });

  test('chart options are configured correctly', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const chart = screen.getByTestId('coverage-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    expect(options.chart.type).toBe('bar');
    expect(options.chart.fontFamily).toBe('Satoshi, sans-serif');
    expect(options.colors).toEqual(['#1973b8']);
    expect(options.yaxis.min).toBe(0);
    expect(options.yaxis.max).toBe(100);
  });

  test('clicking chart shows application details', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const chart = screen.getByTestId('coverage-chart');
    fireEvent.click(chart);
    
    expect(screen.getByTestId('applications-coverage-chart')).toBeInTheDocument();
    expect(screen.getByText('Applications for TEST001')).toBeInTheDocument();
    expect(screen.getByText('Count: 3')).toBeInTheDocument();
  });

  test('can close application details', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    // Click to show details
    const chart = screen.getByTestId('coverage-chart');
    fireEvent.click(chart);
    
    expect(screen.getByTestId('applications-coverage-chart')).toBeInTheDocument();
    
    // Click to close details
    const closeButton = screen.getByTestId('close-detail');
    fireEvent.click(closeButton);
    
    expect(screen.queryByTestId('applications-coverage-chart')).not.toBeInTheDocument();
  });

  test('loading state has correct structure', () => {
    renderWithRouter(<CoverageChart data={[]} loading={true} />);
    
    const loadingContainer = document.querySelector('.flex.items-center.justify-center.h-64');
    expect(loadingContainer).toBeInTheDocument();
    
    const spinner = document.querySelector('.animate-spin.rounded-full.h-16.w-16.border-b-2.border-blue-600');
    expect(spinner).toBeInTheDocument();
  });

  test('empty state has correct structure', () => {
    renderWithRouter(<CoverageChart data={[]} loading={false} />);
    
    const emptyContainer = document.querySelector('.flex.items-center.justify-center.h-64.text-gray-500');
    expect(emptyContainer).toBeInTheDocument();
  });

  test('statistics grid has correct classes', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const statsGrid = screen.getByText('Total UUAAs').closest('.grid');
    expect(statsGrid).toHaveClass('grid-cols-1', 'sm:grid-cols-3', 'gap-4', 'text-center');
  });

  test('handles single UUAA data', () => {
    const singleData = [mockData[0]];
    renderWithRouter(<CoverageChart data={singleData} loading={false} />);
    
    expect(screen.getByText('1')).toBeInTheDocument(); // Total UUAAs
    expect(screen.getByText('3')).toBeInTheDocument(); // Total Apps
    expect(screen.getByText('85.5%')).toBeInTheDocument(); // Average coverage
  });

  test('handles zero coverage data', () => {
    const zeroData: CoverageChartData[] = [
      { 
        uuaa: 'ZERO_UUAA', 
        averageCoverage: 0, 
        totalApps: 1,
        applications: []
      }
    ];
    
    renderWithRouter(<CoverageChart data={zeroData} loading={false} />);
    
    expect(screen.getByText('0.0%')).toBeInTheDocument();
  });

  test('renders without applications detail initially', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    expect(screen.queryByTestId('applications-coverage-chart')).not.toBeInTheDocument();
  });

  test('tooltip configuration is properly set', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const chartElement = screen.getByTestId('coverage-chart');
    const chartOptionsStr = chartElement.getAttribute('data-options') || '{}';
    
    // Verify chart options include tooltip configuration
    expect(chartOptionsStr).toContain('tooltip');
  });

  test('chart series contains correct data structure for tooltip', () => {
    renderWithRouter(<CoverageChart data={mockData} loading={false} />);
    
    const chartElement = screen.getByTestId('coverage-chart');
    const chartSeriesStr = chartElement.getAttribute('data-series') || '[]';
    const chartSeries = JSON.parse(chartSeriesStr);
    
    expect(chartSeries[0]).toBeDefined();
    expect(chartSeries[0].data).toBeDefined();
    expect(chartSeries[0].data.length).toBeGreaterThan(0);
    
    // Check data structure for tooltip functionality
    const dataPoint = chartSeries[0].data[0];
    expect(dataPoint).toHaveProperty('x');
    expect(dataPoint).toHaveProperty('y');
    expect(dataPoint).toHaveProperty('totalApps');
  });

  test('handles data with decimal coverage values', () => {
    const decimalData: CoverageChartData[] = [
      { 
        uuaa: 'DECIMAL_UUAA', 
        averageCoverage: 67.45, 
        totalApps: 3,
        applications: []
      }
    ];
    
    renderWithRouter(<CoverageChart data={decimalData} loading={false} />);
    
    expect(screen.getByText('67.5%')).toBeInTheDocument(); // Should be rounded
  });

  test('displays correct application count in statistics', () => {
    const multiAppData: CoverageChartData[] = [
      { 
        uuaa: 'MULTI_UUAA', 
        averageCoverage: 50, 
        totalApps: 10,
        applications: []
      }
    ];
    
    renderWithRouter(<CoverageChart data={multiAppData} loading={false} />);
    
    expect(screen.getByText('10')).toBeInTheDocument(); // Total Apps count
  });
});
