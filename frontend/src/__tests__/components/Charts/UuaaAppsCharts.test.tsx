import { render, screen } from '@testing-library/react';
import UuaaAppsCharts from '../../../components/Charts/UuaaAppsCharts';
import { Apps } from '../../../types/app';

// Mock react-apexcharts
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: ({ options, series, type, height }: any) => (
    <div 
      data-testid="mock-chart" 
      data-options={JSON.stringify(options)} 
      data-series={JSON.stringify(series)} 
      data-type={type} 
      data-height={height}
    >
      Mock ApexChart
    </div>
  ),
}));

describe('UuaaAppsCharts', () => {
  const mockApps: Apps[] = [
    {
      id: 1,
      name: 'Application 1',
      uuaa: 'UUAA1',
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: 85.5,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: 5,
        totalMedium: 10,
        totalLow: 15,
      },
      chimeraSca: {
        totalCritical: 2,
        totalHigh: 3,
        totalMedium: 8,
        totalLow: 12,
      },
    },
    {
      id: 2,
      name: 'Application 2',
      uuaa: 'UUAA2',
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: 72.3,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: 3,
        totalMedium: 7,
        totalLow: 9,
      },
      chimeraSca: {
        totalCritical: 1,
        totalHigh: 2,
        totalMedium: 5,
        totalLow: 8,
      },
    },
    {
      id: 3,
      name: 'Very Long Application Name That Should Be Truncated',
      uuaa: 'UUAA3',
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: 91.2,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: 8,
        totalMedium: 15,
        totalLow: 20,
      },
      chimeraSca: {
        totalCritical: 4,
        totalHigh: 6,
        totalMedium: 10,
        totalLow: 14,
      },
    },
    {
      id: 4,
      name: 'Application 4',
      uuaa: 'UUAA4',
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: null,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: 2,
        totalMedium: 4,
        totalLow: 6,
      },
      chimeraSca: {
        totalCritical: 0,
        totalHigh: 1,
        totalMedium: 3,
        totalLow: 5,
      },
    },
    {
      id: 5,
      name: 'Application 5',
      uuaa: 'UUAA5',
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: 65.0,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: null,
        totalMedium: null,
        totalLow: null,
      },
      chimeraSca: {
        totalCritical: null,
        totalHigh: null,
        totalMedium: null,
        totalLow: null,
      },
    },
  ] as Apps[];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders loading state correctly', () => {
    render(<UuaaAppsCharts apps={[]} loading={true} />);

    const skeletons = document.querySelectorAll('.animate-pulse');
    expect(skeletons).toHaveLength(2);
    expect(screen.queryByTestId('mock-chart')).not.toBeInTheDocument();
  });

  test('renders nothing when apps array is empty and not loading', () => {
    const { container } = render(<UuaaAppsCharts apps={[]} loading={false} />);

    expect(container.firstChild).toBeNull();
    expect(screen.queryByTestId('mock-chart')).not.toBeInTheDocument();
  });

  test('renders both charts with data correctly', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    expect(charts).toHaveLength(2);
  });

  test('filters and sorts apps by coverage correctly', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const coverageChart = charts[0];
    
    const series = JSON.parse(coverageChart.getAttribute('data-series') || '[]');
    expect(series).toHaveLength(1);
    expect(series[0].name).toBe('Coverage');
    
    // Should only include apps with coverage (not null/undefined)
    // Should be sorted by coverage descending
    const coverageValues = series[0].data;
    expect(coverageValues).toEqual([91.2, 85.5, 72.3, 65.0]);
  });

  test('coverage chart has correct options', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const coverageChart = charts[0];
    
    const options = JSON.parse(coverageChart.getAttribute('data-options') || '{}');
    
    expect(options.chart.type).toBe('bar');
    expect(options.chart.fontFamily).toBe('Satoshi, sans-serif');
    expect(options.chart.toolbar.show).toBe(false);
    expect(options.colors).toEqual(['#0f766e']);
    expect(options.plotOptions.bar.horizontal).toBe(true);
    expect(options.xaxis.max).toBe(100);
    expect(options.title.text).toBe('Coverage por Aplicación (Top 10)');
  });

  test('truncates long app names in coverage chart', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const coverageChart = charts[0];
    
    const options = JSON.parse(coverageChart.getAttribute('data-options') || '{}');
    
    const categories = options.xaxis.categories;
    const truncatedName = categories.find((name: string) => name.includes('...'));
    
    expect(truncatedName).toBe('Very Long Appli...');
  });

  test('vulnerabilities chart filters apps without vulnerabilities', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const vulnChart = charts[1];
    
    const series = JSON.parse(vulnChart.getAttribute('data-series') || '[]');
    
    // Should have 4 series: Critical, High, Medium, Low
    expect(series).toHaveLength(4);
    
    // Application 5 has no vulnerabilities, should not be included
    // So we should have 4 apps in the vulnerability chart
    expect(series[0].data).toHaveLength(4);
  });

  test('vulnerabilities chart calculates totals correctly', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const vulnChart = charts[1];
    
    const series = JSON.parse(vulnChart.getAttribute('data-series') || '[]');
    
    // Critical series
    const criticalSeries = series.find((s: any) => s.name === 'Critical');
    expect(criticalSeries).toBeDefined();
    
    // High series (SAST High + SCA High)
    const highSeries = series.find((s: any) => s.name === 'High');
    expect(highSeries).toBeDefined();
    
    // Check first app (highest total vulnerabilities)
    // App 3 has: SAST(8H,15M,20L) + SCA(4C,6H,10M,14L) = 77 total
    const app3Index = 0; // Should be first as it has most vulnerabilities
    expect(criticalSeries.data[app3Index]).toBe(4); // SCA Critical
    expect(highSeries.data[app3Index]).toBe(14); // 8 SAST High + 6 SCA High
  });

  test('vulnerabilities chart has correct options', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const vulnChart = charts[1];
    
    const options = JSON.parse(vulnChart.getAttribute('data-options') || '{}');
    
    expect(options.chart.type).toBe('bar');
    expect(options.chart.stacked).toBe(true);
    expect(options.plotOptions.bar.horizontal).toBe(true);
    expect(options.colors).toEqual(['#dc2626', '#f87171', '#f59e0b', '#10b981']);
    expect(options.title.text).toBe('Vulnerabilidades por Aplicación (Top 10)');
    expect(options.legend.position).toBe('top');
  });

  test('vulnerabilities chart sorts by total vulnerabilities', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const vulnChart = charts[1];
    
    const options = JSON.parse(vulnChart.getAttribute('data-options') || '{}');
    const categories = options.xaxis.categories;
    
    // First should be the app with most vulnerabilities
    // App 3: 77 total, App 1: 40 total, App 2: 35 total, App 4: 21 total
    expect(categories[0]).toBe('Very Long Ap...');
    expect(categories[1]).toBe('Application ...');
  });

  test('limits to top 10 apps for coverage chart', () => {
    const manyApps: Apps[] = Array.from({ length: 15 }, (_, i) => ({
      id: i,
      name: `App ${i}`,
      uuaa: `UUAA${i}`,
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: 50 + i,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: null,
        totalMedium: null,
        totalLow: null,
      },
      chimeraSca: {
        totalCritical: null,
        totalHigh: null,
        totalMedium: null,
        totalLow: null,
      },
    })) as Apps[];

    render(<UuaaAppsCharts apps={manyApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const coverageChart = charts[0];
    
    const series = JSON.parse(coverageChart.getAttribute('data-series') || '[]');
    
    // Should only show top 10
    expect(series[0].data).toHaveLength(10);
  });

  test('limits to top 10 apps for vulnerabilities chart', () => {
    const manyApps: Apps[] = Array.from({ length: 15 }, (_, i) => ({
      id: i,
      name: `App ${i}`,
      uuaa: `UUAA${i}`,
      bitbucketUrl: null,
      sonarUrl: null,
      sonar10Url: '',
      chimeraUrl: '',
      samuelUrl: '',
      monolith: false,
      coverage: null,
      bugs: null,
      language: null,
      chimeraSast: {
        totalHigh: i + 1,
        totalMedium: (i + 1) * 2,
        totalLow: (i + 1) * 3,
      },
      chimeraSca: {
        totalCritical: i + 1,
        totalHigh: i + 1,
        totalMedium: i + 1,
        totalLow: i + 1,
      },
    })) as Apps[];

    render(<UuaaAppsCharts apps={manyApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    // When there's no coverage data, only vulnerabilities chart is rendered
    const vulnChart = charts.length === 2 ? charts[1] : charts[0];
    
    const series = JSON.parse(vulnChart.getAttribute('data-series') || '[]');
    
    // Should only show top 10
    expect(series[0].data).toHaveLength(10);
  });

  test('only renders coverage chart when no vulnerability data', () => {
    const appsWithoutVulns: Apps[] = [
      {
        id: 1,
        name: 'App 1',
        uuaa: 'UUAA1',
        bitbucketUrl: null,
        sonarUrl: null,
        sonar10Url: '',
        chimeraUrl: '',
        samuelUrl: '',
        monolith: false,
        coverage: 80,
        bugs: null,
        language: null,
        chimeraSast: {
          totalHigh: null,
          totalMedium: null,
          totalLow: null,
        },
        chimeraSca: {
          totalCritical: null,
          totalHigh: null,
          totalMedium: null,
          totalLow: null,
        },
      },
      {
        id: 2,
        name: 'App 2',
        uuaa: 'UUAA2',
        bitbucketUrl: null,
        sonarUrl: null,
        sonar10Url: '',
        chimeraUrl: '',
        samuelUrl: '',
        monolith: false,
        coverage: 70,
        bugs: null,
        language: null,
        chimeraSast: {
          totalHigh: null,
          totalMedium: null,
          totalLow: null,
        },
        chimeraSca: {
          totalCritical: null,
          totalHigh: null,
          totalMedium: null,
          totalLow: null,
        },
      },
    ] as Apps[];

    render(<UuaaAppsCharts apps={appsWithoutVulns} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    expect(charts).toHaveLength(1);
  });

  test('only renders vulnerabilities chart when no coverage data', () => {
    const appsWithoutCoverage: Apps[] = [
      {
        id: 1,
        name: 'App 1',
        uuaa: 'UUAA1',
        bitbucketUrl: null,
        sonarUrl: null,
        sonar10Url: '',
        chimeraUrl: '',
        samuelUrl: '',
        monolith: false,
        coverage: null,
        bugs: null,
        language: null,
        chimeraSast: {
          totalHigh: 5,
          totalMedium: 10,
          totalLow: 15,
        },
        chimeraSca: {
          totalCritical: 2,
          totalHigh: 3,
          totalMedium: 8,
          totalLow: 12,
        },
      },
      {
        id: 2,
        name: 'App 2',
        uuaa: 'UUAA2',
        bitbucketUrl: null,
        sonarUrl: null,
        sonar10Url: '',
        chimeraUrl: '',
        samuelUrl: '',
        monolith: false,
        coverage: null,
        bugs: null,
        language: null,
        chimeraSast: {
          totalHigh: 3,
          totalMedium: 7,
          totalLow: 9,
        },
        chimeraSca: {
          totalCritical: 1,
          totalHigh: 2,
          totalMedium: 5,
          totalLow: 8,
        },
      },
    ] as Apps[];

    render(<UuaaAppsCharts apps={appsWithoutCoverage} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    expect(charts).toHaveLength(1);
  });

  test('applies correct styling classes to containers', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const gridContainer = document.querySelector('.grid.grid-cols-1.lg\\:grid-cols-2');
    expect(gridContainer).toBeInTheDocument();
    expect(gridContainer).toHaveClass('gap-4', 'mb-6');
  });

  test('applies correct styling to chart containers', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const chartContainers = document.querySelectorAll('.rounded-lg.border.border-stroke.bg-white');
    expect(chartContainers.length).toBeGreaterThan(0);
    
    chartContainers.forEach(container => {
      expect(container).toHaveClass(
        'rounded-lg',
        'border',
        'border-stroke',
        'bg-white',
        'p-5',
        'shadow-default',
        'dark:border-strokedark',
        'dark:bg-boxdark'
      );
    });
  });

  test('handles apps with partial vulnerability data', () => {
    const partialApps: Apps[] = [
      {
        id: 1,
        name: 'App 1',
        uuaa: 'UUAA1',
        bitbucketUrl: null,
        sonarUrl: null,
        sonar10Url: '',
        chimeraUrl: '',
        samuelUrl: '',
        monolith: false,
        coverage: 80,
        bugs: null,
        language: null,
        chimeraSast: {
          totalHigh: 5,
          totalMedium: 10,
          totalLow: 15,
        },
        chimeraSca: {
          totalCritical: null,
          totalHigh: null,
          totalMedium: null,
          totalLow: null,
        },
      },
      {
        id: 2,
        name: 'App 2',
        uuaa: 'UUAA2',
        bitbucketUrl: null,
        sonarUrl: null,
        sonar10Url: '',
        chimeraUrl: '',
        samuelUrl: '',
        monolith: false,
        coverage: 70,
        bugs: null,
        language: null,
        chimeraSast: {
          totalHigh: null,
          totalMedium: null,
          totalLow: null,
        },
        chimeraSca: {
          totalCritical: 2,
          totalHigh: 3,
          totalMedium: 8,
          totalLow: 12,
        },
      },
    ] as Apps[];

    render(<UuaaAppsCharts apps={partialApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    expect(charts).toHaveLength(2); // Both coverage and vulnerability charts should render
    
    const vulnChart = charts[1];
    const series = JSON.parse(vulnChart.getAttribute('data-series') || '[]');
    
    // Should handle null values correctly
    expect(series).toHaveLength(4);
    expect(series[0].data).toHaveLength(2);
  });

  test('chart type and height are correct', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    
    charts.forEach(chart => {
      expect(chart).toHaveAttribute('data-type', 'bar');
      expect(chart).toHaveAttribute('data-height', '250');
    });
  });

  test('truncates app names to 12 chars in vulnerability chart', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const vulnChart = charts[1];
    
    const options = JSON.parse(vulnChart.getAttribute('data-options') || '{}');
    const categories = options.xaxis.categories;
    
    // Check that names longer than 12 chars are truncated
    const allTruncated = categories.every((name: string) => 
      name.length <= 15 // 12 chars + "..."
    );
    
    expect(allTruncated).toBe(true);
  });

  test('truncates app names to 15 chars in coverage chart', () => {
    render(<UuaaAppsCharts apps={mockApps} loading={false} />);

    const charts = screen.getAllByTestId('mock-chart');
    const coverageChart = charts[0];
    
    const options = JSON.parse(coverageChart.getAttribute('data-options') || '{}');
    const categories = options.xaxis.categories;
    
    // Check that names longer than 15 chars are truncated
    const allTruncated = categories.every((name: string) => 
      name.length <= 18 // 15 chars + "..."
    );
    
    expect(allTruncated).toBe(true);
  });
});
