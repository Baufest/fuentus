import { render, screen } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import ApplicationsChimeraChart from '../../../components/Charts/ApplicationsChimeraChart';
import { ChimeraChartData } from '../../../types/statsSummary';
import '@testing-library/jest-dom';

// Mock de ApexCharts para evitar problemas de renderizado
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: ({ series, options, type, height }: any) => (
    <div 
      data-testid="applications-chimera-chart"
      data-series={JSON.stringify(series)}
      data-options={JSON.stringify(options)}
      data-type={type}
      data-height={height}
    >
      Mocked Chart
    </div>
  )
}));

const renderWithRouter = (component: React.ReactElement) => {
  return render(<Router>{component}</Router>);
};

describe('ApplicationsChimeraChart', () => {
  const mockUuaaData: ChimeraChartData = {
    uuaa: 'TEST001',
    sastData: {
      totalLow: 8,
      totalMedium: 5,
      totalHigh: 3
    },
    scaData: {
      totalLow: 12,
      totalMedium: 6,
      totalHigh: 4,
      totalCritical: 2
    },
    totalApps: 3,
    applications: [
      {
        name: 'Application 1',
        bitbucketUrl: 'https://bitbucket.org/test/app1',
        chimeraUrl: 'https://chimera.test/sast/app1',
        chimeraSast: {
          totalLow: 3,
          totalMedium: 2,
          totalHigh: 1
        },
        chimeraSca: {
          totalLow: 5,
          totalMedium: 3,
          totalHigh: 2,
          totalCritical: 1
        }
      },
      {
        name: 'Application 2',
        bitbucketUrl: 'https://bitbucket.org/test/app2',
        chimeraUrl: 'https://chimera.test/sast/app2',
        chimeraSast: {
          totalLow: 5,
          totalMedium: 3,
          totalHigh: 2
        },
        chimeraSca: {
          totalLow: 7,
          totalMedium: 3,
          totalHigh: 2,
          totalCritical: 1
        }
      }
    ]
  };

  test('renders charts with data correctly', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const charts = screen.getAllByTestId('applications-chimera-chart');
    expect(charts).toHaveLength(2); // SAST and SCA charts
    
    // Check SAST chart
    const sastChart = charts[0];
    const sastSeries = JSON.parse(sastChart.getAttribute('data-series') || '[]');
    expect(sastSeries).toHaveLength(3); // Low, Medium, High
    
    // Check SCA chart
    const scaChart = charts[1];
    const scaSeries = JSON.parse(scaChart.getAttribute('data-series') || '[]');
    expect(scaSeries).toHaveLength(4); // Low, Medium, High, Critical
  });

  test('renders empty state when no applications', () => {
    const emptyData: ChimeraChartData = {
      ...mockUuaaData,
      applications: []
    };

    renderWithRouter(<ApplicationsChimeraChart uuaaData={emptyData} />);
    
    expect(screen.getByText('No hay aplicaciones disponibles para esta UUAA')).toBeInTheDocument();
  });

  test('displays application details table', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    expect(screen.getByText('Detalle de Aplicaciones')).toBeInTheDocument();
    expect(screen.getByText('Application 1')).toBeInTheDocument();
    expect(screen.getByText('Application 2')).toBeInTheDocument();
  });

  test('renders external links correctly', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    // Check for Bitbucket links
    const bitbucketLinks = screen.getAllByRole('link');
    expect(bitbucketLinks.length).toBeGreaterThan(0);
    
    // Verify some links have correct href attributes
    const firstBitbucketLink = bitbucketLinks.find(link => 
      link.getAttribute('href') === 'https://bitbucket.org/test/app1'
    );
    expect(firstBitbucketLink).toBeInTheDocument();
  });

  test('chart options are configured correctly', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const charts = screen.getAllByTestId('applications-chimera-chart');
    const sastChart = charts[0];
    const options = JSON.parse(sastChart.getAttribute('data-options') || '{}');
    
    expect(options.chart.type).toBe('bar');
    expect(options.chart.stacked).toBe(true);
    expect(options.plotOptions.bar.horizontal).toBe(true);
    expect(options.title.text).toBe('Vulnerabilidades SAST por Aplicación');
  });

  test('chart data includes correct vulnerability counts', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const charts = screen.getAllByTestId('applications-chimera-chart');
    
    // SAST chart data
    const sastChart = charts[0];
    const sastSeries = JSON.parse(sastChart.getAttribute('data-series') || '[]');
    
    expect(sastSeries[0].name).toBe('Low');
    expect(sastSeries[0].data).toEqual([3, 5]); // Low counts for App1, App2
    expect(sastSeries[1].name).toBe('Medium');
    expect(sastSeries[1].data).toEqual([2, 3]); // Medium counts for App1, App2
    expect(sastSeries[2].name).toBe('High');
    expect(sastSeries[2].data).toEqual([1, 2]); // High counts for App1, App2
  });

  test('displays vulnerability badges in table', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    // Should display SAST badges (use getAllByText for multiple occurrences)
    expect(screen.getAllByText('1H')).toHaveLength(1); // High for App1 SAST
    expect(screen.getAllByText('2M')).toHaveLength(1); // Medium for App1 SAST
    expect(screen.getAllByText('3L')).toHaveLength(1); // Low for App1 SAST
    
    // Should display SCA badges
    expect(screen.getAllByText('1C')).toHaveLength(2); // Critical for App1 and App2
    expect(screen.getAllByText('2H')).toHaveLength(3); // High for App1 SAST, App1 SCA, App2 SCA, App2 SAST
  });

  test('chart height adjusts based on number of applications', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const charts = screen.getAllByTestId('applications-chimera-chart');
    const expectedHeight = Math.max(300, mockUuaaData.applications.length * 40);
    
    charts.forEach(chart => {
      expect(chart.getAttribute('data-height')).toBe(expectedHeight.toString());
    });
  });

  test('handles single application data', () => {
    const singleAppData: ChimeraChartData = {
      ...mockUuaaData,
      applications: [mockUuaaData.applications[0]]
    };

    renderWithRouter(<ApplicationsChimeraChart uuaaData={singleAppData} />);
    
    const charts = screen.getAllByTestId('applications-chimera-chart');
    const sastChart = charts[0];
    const sastSeries = JSON.parse(sastChart.getAttribute('data-series') || '[]');
    
    // Should have data for only one application
    expect(sastSeries[0].data).toHaveLength(1);
    expect(sastSeries[0].data[0]).toBe(3); // Low count for single app
  });

  test('handles applications without vulnerabilities', () => {
    const noVulnData: ChimeraChartData = {
      ...mockUuaaData,
      applications: [
        {
          name: 'Clean App',
          bitbucketUrl: 'https://bitbucket.org/test/clean',
          chimeraUrl: 'https://chimera.test/sast/clean',
          chimeraSast: {
            totalLow: 0,
            totalMedium: 0,
            totalHigh: 0
          },
          chimeraSca: {
            totalLow: 0,
            totalMedium: 0,
            totalHigh: 0,
            totalCritical: 0
          }
        }
      ]
    };

    renderWithRouter(<ApplicationsChimeraChart uuaaData={noVulnData} />);
    
    expect(screen.getByText('Clean App')).toBeInTheDocument();
    expect(screen.getAllByText('0H')).toHaveLength(2); // High = 0 (SAST and SCA)
    expect(screen.getAllByText('0M')).toHaveLength(2); // Medium = 0 (SAST and SCA)
    expect(screen.getAllByText('0L')).toHaveLength(2); // Low = 0 (SAST and SCA)
    expect(screen.getAllByText('0C')).toHaveLength(1); // Critical = 0 (only SCA)
  });

  test('applies correct styling classes', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const chartContainers = screen.getAllByText('Mocked Chart');
    chartContainers.forEach(container => {
      const parentDiv = container.parentElement;
      expect(parentDiv).toHaveClass('bg-white', 'dark:bg-boxdark', 'rounded-lg');
    });
    
    // Check table styling
    const table = screen.getByRole('table');
    expect(table).toHaveClass('min-w-full');
  });

  test('table headers are displayed correctly', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    expect(screen.getByText('Aplicación')).toBeInTheDocument();
    expect(screen.getByText('Enlaces')).toBeInTheDocument();
    expect(screen.getByText('SAST')).toBeInTheDocument();
    expect(screen.getByText('SCA')).toBeInTheDocument();
  });

  test('button tooltips are rendered', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    // Tooltip components should be rendered with aria-labels
    expect(screen.getAllByLabelText('Ver repositorio Bitbucket')).toHaveLength(2); // 2 applications
    expect(screen.getAllByLabelText('Ver reporte Chimera SAST')).toHaveLength(2);
    expect(screen.getAllByLabelText('Ver reporte Chimera SCA')).toHaveLength(2);
  });

  test('SCA URL is correctly modified from SAST URL', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const scaLinks = screen.getAllByRole('link').filter(link => 
      link.getAttribute('href')?.includes('sca')
    );
    
    expect(scaLinks.length).toBeGreaterThan(0);
    // Should replace 'sast' with 'sca' in URL
    expect(scaLinks[0].getAttribute('href')).toBe('https://chimera.test/sca/app1');
  });

  test('colors are configured correctly for vulnerability series', () => {
    renderWithRouter(<ApplicationsChimeraChart uuaaData={mockUuaaData} />);
    
    const charts = screen.getAllByTestId('applications-chimera-chart');
    const sastChart = charts[0];
    const sastSeries = JSON.parse(sastChart.getAttribute('data-series') || '[]');
    
    expect(sastSeries[0].color).toBe('#10b981'); // Low - green
    expect(sastSeries[1].color).toBe('#f59e0b'); // Medium - amber
    expect(sastSeries[2].color).toBe('#f87171'); // High - red
    
    const scaChart = charts[1];
    const scaSeries = JSON.parse(scaChart.getAttribute('data-series') || '[]');
    
    expect(scaSeries[3].color).toBe('#dc2626'); // Critical - dark red
  });
});
