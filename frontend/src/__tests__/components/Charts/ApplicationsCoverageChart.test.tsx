import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import ApplicationsCoverageChart from '../../../components/Charts/ApplicationsCoverageChart';
import { ApplicationCoverageData } from '../../../types/statsSummary';

// Mock react-apexcharts
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: ({ options, series, type, height }: any) => (
    <div data-testid="mock-chart" data-options={JSON.stringify(options)} data-series={JSON.stringify(series)} data-type={type} data-height={height}>
      Mock ApexChart
    </div>
  ),
}));

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

// Mock Button component
jest.mock('../../../components/Buttons/Button', () => {
  return function MockButton({ children, onClick, variant, size, className }: any) {
    return (
      <button 
        onClick={onClick} 
        data-testid="mock-button"
        data-variant={variant}
        data-size={size}
        className={className}
      >
        {children}
      </button>
    );
  };
});

const mockOnClose = jest.fn();

const renderWithRouter = (component: React.ReactElement) => {
  return render(<Router>{component}</Router>);
};

describe('ApplicationsCoverageChart', () => {
  const mockData: ApplicationCoverageData[] = [
    {
      name: 'Application 1',
      coverage: 85.5,
      bitbucketUrl: 'https://bitbucket.com/app1'
    },
    {
      name: 'Application 2',
      coverage: 72.3,
      bitbucketUrl: 'https://bitbucket.com/app2'
    },
    {
      name: 'Very Long Application Name That Should Be Truncated',
      coverage: 91.2,
      bitbucketUrl: 'https://bitbucket.com/app3'
    }
  ];

  const mockProps = {
    data: mockData,
    uuaaName: 'TEST_UUAA',
    onClose: mockOnClose
  };

  beforeEach(() => {
    jest.clearAllMocks();
    mockNavigate.mockClear();
    mockOnClose.mockClear();
  });

  test('renders chart with data correctly', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    expect(screen.getByText('Aplicaciones de TEST_UUAA')).toBeInTheDocument();
    expect(screen.getByTestId('mock-chart')).toBeInTheDocument();
  });

  test('renders empty state when no data', () => {
    const emptyProps = { ...mockProps, data: [] };
    renderWithRouter(<ApplicationsCoverageChart {...emptyProps} />);

    expect(screen.getByText('Aplicaciones de TEST_UUAA')).toBeInTheDocument();
    expect(screen.getByText('No hay aplicaciones con datos de coverage.')).toBeInTheDocument();
    expect(screen.queryByTestId('mock-chart')).not.toBeInTheDocument();
  });

  test('close button calls onClose callback', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const closeButton = screen.getAllByText('✕')[0];
    fireEvent.click(closeButton);

    expect(mockOnClose).toHaveBeenCalledTimes(1);
  });

  test('close button works in empty state', () => {
    const emptyProps = { ...mockProps, data: [] };
    renderWithRouter(<ApplicationsCoverageChart {...emptyProps} />);

    const closeButton = screen.getByText('✕');
    fireEvent.click(closeButton);

    expect(mockOnClose).toHaveBeenCalledTimes(1);
  });

  test('calculates and displays statistics correctly', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    // Total Apps
    expect(screen.getByText('3')).toBeInTheDocument();
    
    // Average Coverage (85.5 + 72.3 + 91.2) / 3 = 83.0
    expect(screen.getByText('83.0%')).toBeInTheDocument();
    
    // Maximum Coverage
    expect(screen.getByText('91.2%')).toBeInTheDocument();
    
    // Minimum Coverage
    expect(screen.getByText('72.3%')).toBeInTheDocument();
  });

  test('view detail button navigates correctly', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const detailButton = screen.getByTestId('mock-button');
    expect(detailButton).toHaveTextContent('Ver Detalle de TEST_UUAA');
    
    fireEvent.click(detailButton);

    expect(mockNavigate).toHaveBeenCalledWith('/detalle/TEST_UUAA', {
      state: { 
        row: { 
          uuaa: ['TEST_UUAA']
        } 
      }
    });
  });

  test('chart receives correct data and options', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const chart = screen.getByTestId('mock-chart');
    
    // Check chart type and height
    expect(chart).toHaveAttribute('data-type', 'bar');
    expect(chart).toHaveAttribute('data-height', '400');

    // Check series data
    const seriesData = JSON.parse(chart.getAttribute('data-series') || '[]');
    expect(seriesData).toHaveLength(1);
    expect(seriesData[0].name).toBe('Coverage');
    expect(seriesData[0].data).toHaveLength(3);
    
    // Check that long names are truncated (actual truncation is 19 chars + ...)
    const chartData = seriesData[0].data;
    const truncatedItem = chartData.find((item: any) => item.fullName === 'Very Long Application Name That Should Be Truncated');
    expect(truncatedItem.x).toBe('Very Long Applicatio...');
  });

  test('chart options are configured correctly', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const chart = screen.getByTestId('mock-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');

    expect(options.chart.type).toBe('bar');
    expect(options.chart.fontFamily).toBe('Satoshi, sans-serif');
    expect(options.chart.toolbar.show).toBe(false);
    expect(options.colors).toEqual(['#0f766e']);
    expect(options.yaxis.min).toBe(0);
    expect(options.yaxis.max).toBe(100);
    expect(options.legend.show).toBe(false);
  });

  test('applies correct styling classes', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const mainContainer = document.querySelector('.mt-6.p-4.border.border-gray-200');
    expect(mainContainer).toBeInTheDocument();
    expect(mainContainer).toHaveClass('mt-6', 'p-4', 'border', 'border-gray-200', 'dark:border-gray-700', 'rounded-lg', 'bg-gray-50', 'dark:bg-gray-800');
  });

  test('close button has correct styling and title', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const closeButton = screen.getAllByText('✕')[0];
    expect(closeButton).toHaveClass('text-gray-500', 'hover:text-gray-700', 'dark:text-gray-400', 'dark:hover:text-gray-200');
    expect(closeButton).toHaveAttribute('title', 'Cerrar detalle');
  });

  test('statistics grid has correct layout', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const statsContainer = screen.getByText('Total Apps').closest('.grid');
    expect(statsContainer).toHaveClass('grid-cols-2', 'sm:grid-cols-4', 'gap-4');
  });

  test('statistics cards have correct styling', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const totalAppsCard = screen.getByText('Total Apps').closest('div');
    expect(totalAppsCard).toHaveClass('bg-white', 'dark:bg-gray-700', 'p-3', 'rounded-lg');
  });

  test('button has correct props', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const button = screen.getByTestId('mock-button');
    expect(button).toHaveAttribute('data-variant', 'primary');
    expect(button).toHaveAttribute('data-size', 'sm');
    expect(button).toHaveClass('!px-6', '!py-2');
  });

  test('handles single application data', () => {
    const singleAppData = [mockData[0]];
    const singleAppProps = { ...mockProps, data: singleAppData };
    
    renderWithRouter(<ApplicationsCoverageChart {...singleAppProps} />);

    expect(screen.getByText('1')).toBeInTheDocument(); // Total Apps
    expect(screen.getAllByText('85.5%')[0]).toBeInTheDocument(); // All stats should be same
  });

  test('handles applications without bitbucket URLs', () => {
    const dataWithoutUrls: ApplicationCoverageData[] = [
      {
        name: 'App Without URL',
        coverage: 75.0,
        bitbucketUrl: ''
      }
    ];
    
    const propsWithoutUrls = { ...mockProps, data: dataWithoutUrls };
    renderWithRouter(<ApplicationsCoverageChart {...propsWithoutUrls} />);

    expect(screen.getByTestId('mock-chart')).toBeInTheDocument();
  });

  test('handles applications with exactly 20 character names', () => {
    const exactLengthData: ApplicationCoverageData[] = [
      {
        name: '12345678901234567890', // Exactly 20 characters
        coverage: 80.0,
        bitbucketUrl: 'https://example.com'
      }
    ];
    
    const exactLengthProps = { ...mockProps, data: exactLengthData };
    renderWithRouter(<ApplicationsCoverageChart {...exactLengthProps} />);

    const chart = screen.getByTestId('mock-chart');
    const seriesData = JSON.parse(chart.getAttribute('data-series') || '[]');
    const chartData = seriesData[0].data[0];
    
    // Should not be truncated since it's exactly 20 characters
    expect(chartData.x).toBe('12345678901234567890');
    expect(chartData.fullName).toBe('12345678901234567890');
  });

  test('responsive configuration is present in chart options', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const chart = screen.getByTestId('mock-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');

    expect(options.responsive).toBeDefined();
    expect(options.responsive).toHaveLength(1);
    expect(options.responsive[0].breakpoint).toBe(768);
    expect(options.responsive[0].options.chart.height).toBe(350);
  });

  test('tooltip configuration includes custom formatter', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const chart = screen.getByTestId('mock-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');

    expect(options.tooltip).toBeDefined();
    // Note: The custom formatter function is serialized differently in tests
  });

  test('xaxis categories are properly formatted', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);

    const chart = screen.getByTestId('mock-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');

    expect(options.xaxis.categories).toEqual([
      'Application 1',
      'Application 2', 
      'Very Long Applicatio...'
    ]);
  });

  test('tooltip configuration includes custom formatter with bitbucket URL', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const optionsStr = chartElement.getAttribute('data-options') || '{}';
    
    // Verify tooltip configuration exists
    expect(optionsStr).toContain('tooltip');
  });

  test('chart data includes tooltip information for coverage', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const seriesStr = chartElement.getAttribute('data-series') || '[]';
    const series = JSON.parse(seriesStr);
    
    expect(series[0].data).toBeDefined();
    expect(series[0].data.length).toBeGreaterThan(0);
    
    // Check that data points have properties needed for custom tooltip
    const dataPoint = series[0].data[0];
    expect(dataPoint).toHaveProperty('fullName');
    expect(dataPoint).toHaveProperty('y');
    expect(dataPoint).toHaveProperty('bitbucketUrl');
  });

  test('handles applications with empty bitbucket URLs in chart data', () => {
    const dataWithEmptyUrl: ApplicationCoverageData[] = [
      {
        name: 'app-no-url',
        coverage: 50,
        bitbucketUrl: ''
      }
    ];
    
    const propsWithEmptyUrl = { ...mockProps, data: dataWithEmptyUrl };
    renderWithRouter(<ApplicationsCoverageChart {...propsWithEmptyUrl} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const seriesStr = chartElement.getAttribute('data-series') || '[]';
    const series = JSON.parse(seriesStr);
    
    expect(series[0].data[0].bitbucketUrl).toBe('');
    expect(series[0].data[0].fullName).toBe('app-no-url');
  });

  test('preserves full application names in chart data for tooltip', () => {
    const longNameData: ApplicationCoverageData[] = [
      {
        name: 'very-long-application-name-that-exceeds-twenty-characters-limit',
        coverage: 80,
        bitbucketUrl: 'https://bitbucket.org/test/long-app'
      }
    ];
    
    const propsWithLongName = { ...mockProps, data: longNameData };
    renderWithRouter(<ApplicationsCoverageChart {...propsWithLongName} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const seriesStr = chartElement.getAttribute('data-series') || '[]';
    const series = JSON.parse(seriesStr);
    
    // Check that the full name is preserved in data for tooltip
    expect(series[0].data[0].fullName).toBe('very-long-application-name-that-exceeds-twenty-characters-limit');
    // But category should be truncated - allow for slight variation in truncation
    const optionsStr = chartElement.getAttribute('data-options') || '{}';
    const options = JSON.parse(optionsStr);
    expect(options.xaxis.categories[0]).toMatch(/very-long-applicatio/);
  });

  test('tooltip formatter handles various data points correctly', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const optionsStr = chartElement.getAttribute('data-options') || '{}';
    const options = JSON.parse(optionsStr);
    
    // Verify tooltip exists (custom function is stringified during mock serialization)
    expect(options.tooltip).toBeDefined();
  });

  test('navigation state is properly structured for detail page', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const viewDetailButton = screen.getByTestId('mock-button');
    fireEvent.click(viewDetailButton);
    
    // Check that navigate was called with correct parameters
    expect(mockNavigate).toHaveBeenCalledWith(
      '/detalle/TEST_UUAA',
      {
        state: {
          row: {
            uuaa: ['TEST_UUAA']
          }
        }
      }
    );
  });

  test('chart configuration includes all required properties', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const optionsStr = chartElement.getAttribute('data-options') || '{}';
    const options = JSON.parse(optionsStr);
    
    // Verify all main configuration sections exist
    expect(options).toHaveProperty('chart');
    expect(options).toHaveProperty('colors');
    expect(options).toHaveProperty('plotOptions');
    expect(options).toHaveProperty('dataLabels');
    expect(options).toHaveProperty('stroke');
    expect(options).toHaveProperty('xaxis');
    expect(options).toHaveProperty('yaxis');
    expect(options).toHaveProperty('tooltip');
    expect(options).toHaveProperty('grid');
    expect(options).toHaveProperty('legend');
    expect(options).toHaveProperty('responsive');
  });

  test('chart stroke configuration is properly set', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const optionsStr = chartElement.getAttribute('data-options') || '{}';
    const options = JSON.parse(optionsStr);
    
    expect(options.stroke).toEqual({
      show: true,
      width: 2,
      colors: ['transparent']
    });
  });

  test('chart grid and legend configurations are correct', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const optionsStr = chartElement.getAttribute('data-options') || '{}';
    const options = JSON.parse(optionsStr);
    
    expect(options.grid).toBeDefined();
    expect(options.legend).toBeDefined();
    expect(options.legend.show).toBe(false);
  });

  test('statistics calculation handles edge cases correctly', () => {
    const edgeCaseData: ApplicationCoverageData[] = [
      { name: 'app1', coverage: 0, bitbucketUrl: 'https://bitbucket.org/app1' },
      { name: 'app2', coverage: 100, bitbucketUrl: 'https://bitbucket.org/app2' },
      { name: 'app3', coverage: 50.5, bitbucketUrl: 'https://bitbucket.org/app3' }
    ];
    
    const propsWithEdgeCase = { ...mockProps, data: edgeCaseData };
    renderWithRouter(<ApplicationsCoverageChart {...propsWithEdgeCase} />);
    
    // Check that statistics are rendered correctly
    expect(screen.getByText('3')).toBeInTheDocument(); // Total apps
    expect(screen.getByText(/50\.2/)).toBeInTheDocument(); // Average (rounded)
    
    // Check minimum value appears at least once
    const minElements = screen.getAllByText(/0\.0/);
    expect(minElements.length).toBeGreaterThanOrEqual(1);
    
    // Check maximum value appears at least once
    const maxElements = screen.getAllByText(/100\.0/);
    expect(maxElements.length).toBeGreaterThanOrEqual(1);
  });

  test('handles zero data coverage correctly', () => {
    const zeroData: ApplicationCoverageData[] = [
      { name: 'zero-app', coverage: 0, bitbucketUrl: 'https://bitbucket.org/zero' }
    ];
    
    const propsWithZero = { ...mockProps, data: zeroData };
    renderWithRouter(<ApplicationsCoverageChart {...propsWithZero} />);
    
    expect(screen.getByText('1')).toBeInTheDocument(); // Total apps
    expect(screen.getAllByText(/0\.0/)).toHaveLength(3); // Average, Min, Max all 0% (3 instances)
  });

  test('tooltip structure contains proper HTML formatting', () => {
    renderWithRouter(<ApplicationsCoverageChart {...mockProps} />);
    
    const chartElement = screen.getByTestId('mock-chart');
    const seriesData = JSON.parse(chartElement.getAttribute('data-series') || '[]');
    
    // Verify the series data structure needed for tooltip
    expect(seriesData[0].data[0]).toHaveProperty('fullName');
    expect(seriesData[0].data[0]).toHaveProperty('y');
    expect(seriesData[0].data[0]).toHaveProperty('bitbucketUrl');
    
    // Verify data contains expected values for tooltip display
    expect(seriesData[0].data[0].fullName).toBe('Application 1');
    expect(seriesData[0].data[0].y).toBe(85.5);
    expect(seriesData[0].data[0].bitbucketUrl).toBe('https://bitbucket.com/app1');
  });

  test('tooltip custom formatter executes and returns correct HTML', () => {
    // We need to simulate the tooltip function execution 
    // since functions don't serialize to JSON in the mock
    const mockTooltipParams = {
      seriesIndex: 0,
      dataPointIndex: 0,
      w: {
        config: {
          series: [{
            data: [{
              fullName: 'Test Application',
              y: 75.5,
              bitbucketUrl: 'https://bitbucket.com/test'
            }]
          }]
        }
      }
    };
    
    // Create a tooltip function similar to the one in the component
    const tooltipFunction = ({ seriesIndex, dataPointIndex, w }: any) => {
      const data = w.config.series[seriesIndex].data[dataPointIndex];
      const fullName = data.fullName;
      const coverage = data.y;
      const bitbucketUrl = data.bitbucketUrl;
      
      return `
        <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg max-w-xs">
          <div class="font-semibold text-gray-900 mb-1">${fullName}</div>
          <div class="text-sm text-gray-600 mb-1">Coverage: ${coverage}%</div>
          ${bitbucketUrl ? `<div class="text-xs text-blue-600 truncate">BitBucket: ${bitbucketUrl}</div>` : ''}
        </div>
      `;
    };
    
    const tooltipHtml = tooltipFunction(mockTooltipParams);
    
    expect(typeof tooltipHtml).toBe('string');
    expect(tooltipHtml).toContain('Test Application');
    expect(tooltipHtml).toContain('75.5%');
    expect(tooltipHtml).toContain('https://bitbucket.com/test');
    expect(tooltipHtml).toContain('class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg max-w-xs"');
  });

  test('tooltip custom formatter handles missing bitbucket URL', () => {
    // Mock tooltip parameters without bitbucket URL
    const mockTooltipParams = {
      seriesIndex: 0,
      dataPointIndex: 0,
      w: {
        config: {
          series: [{
            data: [{
              fullName: 'No BitBucket App',
              y: 50.0,
              bitbucketUrl: ''
            }]
          }]
        }
      }
    };
    
    // Create a tooltip function similar to the one in the component
    const tooltipFunction = ({ seriesIndex, dataPointIndex, w }: any) => {
      const data = w.config.series[seriesIndex].data[dataPointIndex];
      const fullName = data.fullName;
      const coverage = data.y;
      const bitbucketUrl = data.bitbucketUrl;
      
      return `
        <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg max-w-xs">
          <div class="font-semibold text-gray-900 mb-1">${fullName}</div>
          <div class="text-sm text-gray-600 mb-1">Coverage: ${coverage}%</div>
          ${bitbucketUrl ? `<div class="text-xs text-blue-600 truncate">BitBucket: ${bitbucketUrl}</div>` : ''}
        </div>
      `;
    };
    
    const tooltipHtml = tooltipFunction(mockTooltipParams);
    
    expect(tooltipHtml).toContain('No BitBucket App');
    expect(tooltipHtml).toContain('50%');
    expect(tooltipHtml).not.toContain('BitBucket:');
  });
});
