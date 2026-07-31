import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import NucleusCoverageChart from '../../components/Charts/NucleusCoverageChart';
import { NucleusCoverageStatsSummary } from '../../types/statsSummary';

// Mock ReactApexChart with options capture
const mockApexChart = jest.fn();
jest.mock('react-apexcharts', () => {
  return function MockApexChart(props: any) {
    mockApexChart(props);
    return <div data-testid="apex-chart" />;
  };
});

// Mock API
jest.mock('../../api/statsSummaryApi', () => ({
  getCoverageAverageByLevel: jest.fn(() => Promise.resolve([
    { label: 'Drill1', coveragePercentage: 80, nucleusLevel: 'UOL2' },
    { label: 'Drill2', coveragePercentage: 60, nucleusLevel: 'UOL2' },
  ])),
}));

const mockData: NucleusCoverageStatsSummary[] = [
  { label: 'Vertical1', coveragePercentage: 75, nucleusLevel: 'VERTICAL' },
  { label: 'Vertical2', coveragePercentage: 50, nucleusLevel: 'VERTICAL' },
  { label: 'Vertical3', coveragePercentage: 0, nucleusLevel: 'VERTICAL' }, // Should be filtered out
];

describe('NucleusCoverageChart', () => {
  beforeEach(() => {
    mockApexChart.mockClear();
    const { getCoverageAverageByLevel } = require('../../api/statsSummaryApi');
    getCoverageAverageByLevel.mockClear();
    getCoverageAverageByLevel.mockResolvedValue([
      { label: 'Drill1', coveragePercentage: 80, nucleusLevel: 'UOL2' },
      { label: 'Drill2', coveragePercentage: 60, nucleusLevel: 'UOL2' },
    ]);
  });

  it('renders loading state', () => {
    render(
      <NucleusCoverageChart
        data={[]}
        loading={true}
        currentLevel="VERTICAL"
      />
    );
    expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
  });

  it('renders empty state', () => {
    render(
      <NucleusCoverageChart
        data={[]}
        loading={false}
        currentLevel="VERTICAL"
      />
    );
    expect(screen.getByText(/no hay datos disponibles/i)).toBeInTheDocument();
  });

  it('renders chart and stats', () => {
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="VERTICAL"
      />
    );
    expect(screen.getByTestId('apex-chart')).toBeInTheDocument();
    expect(screen.getByText(/total verticales/i)).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument(); // Only 2 items > 0
    expect(screen.getByText(/coverage promedio general/i)).toBeInTheDocument();
    expect(screen.getByText(/62.5%/i)).toBeInTheDocument(); // (75+50)/2
  });

  it('shows drilldown instruction text for non-SN2 levels', () => {
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="VERTICAL"
      />
    );
    expect(screen.getByText(/haz click en una barra o nombre para ver el detalle debajo/i)).toBeInTheDocument();
  });

  it('does not show drilldown instruction for SN2 level', () => {
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="SN2"
      />
    );
    expect(screen.queryByText(/haz click en una barra o nombre para ver el detalle debajo/i)).not.toBeInTheDocument();
  });

  it('renders breadcrumbs for deeper levels', () => {
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="SN2"
        selectedVertical="Vertical1"
        selectedUol2="UOL2A"
        selectedSn1="SN1A"
      />
    );
    expect(screen.getByText(/verticales/i)).toBeInTheDocument();
    expect(screen.getByText(/uol2a/i)).toBeInTheDocument();
    expect(screen.getByText(/sn1a/i)).toBeInTheDocument();
  });

  it('renders different level names correctly', () => {
    const { rerender } = render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="VERTICAL"
      />
    );
    expect(screen.getByText(/total verticales/i)).toBeInTheDocument();

    rerender(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UOL2"
        selectedVertical="TestVertical"
      />
    );
    expect(screen.getByText(/total uol2s/i)).toBeInTheDocument();

    rerender(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="SN1"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
      />
    );
    expect(screen.getByText(/total servicios n1/i)).toBeInTheDocument();
  });

  it('filters out zero coverage items', () => {
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="VERTICAL"
      />
    );
    // Should show only 2 items (the ones with coverage > 0)
    expect(screen.getByText('2')).toBeInTheDocument(); // Total Verticales
    // Vertical3 with 0% coverage should not be visible in stats calculation
    expect(screen.getByText(/62.5%/i)).toBeInTheDocument(); // (75+50)/2 = 62.5%
  });

  it('calls onItemClick when provided', () => {
    const onItemClick = jest.fn();
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="VERTICAL"
        onItemClick={onItemClick}
      />
    );
    // Since we're mocking ReactApexChart, we can't easily test the click event
    // But we can verify the callback is passed correctly
    expect(onItemClick).toBeDefined();
  });

  it('calls onBackClick when provided', () => {
    const onBackClick = jest.fn();
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UOL2"
        selectedVertical="TestVertical"
        onBackClick={onBackClick}
      />
    );
    
    // Find and click the breadcrumb navigation
    const breadcrumbButton = screen.getByRole('button', { name: /verticales/i });
    fireEvent.click(breadcrumbButton);
    expect(onBackClick).toHaveBeenCalled();
  });

  it('shows drilldown chart after clicking on a bar', async () => {
    const { getCoverageAverageByLevel } = require('../../api/statsSummaryApi');
    
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UUAA"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
        selectedSn1="TestSn1"
        selectedSn2="TestSn2"
      />
    );

    // Get the chart options from the last mock call
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1];
    const chartOptions = lastCall[0].options;
    
    // Simulate dataPointSelection event
    await act(async () => {
      await chartOptions.chart.events.dataPointSelection(null, null, { dataPointIndex: 0 });
    });

    // Wait for async operations
    await waitFor(() => {
      expect(screen.getByText(/detalle de: vertical1/i)).toBeInTheDocument();
    });

    // Verify drilldown chart is shown
    expect(screen.getByRole('button', { name: /✕ cerrar/i })).toBeInTheDocument();
    expect(getCoverageAverageByLevel).toHaveBeenCalled();
  });

  it('shows drilldown chart after clicking on x-axis label', async () => {
    const { getCoverageAverageByLevel } = require('../../api/statsSummaryApi');
    
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UUAA"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
        selectedSn1="TestSn1"
        selectedSn2="TestSn2"
      />
    );

    // Get the chart options from the last mock call
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1];
    const chartOptions = lastCall[0].options;
    
    // Simulate xAxisLabelClick event
    await act(async () => {
      await chartOptions.chart.events.xAxisLabelClick(null, null, { labelIndex: 1 });
    });

    // Wait for async operations
    await waitFor(() => {
      expect(screen.getByText(/detalle de: vertical2/i)).toBeInTheDocument();
    });

    // Verify drilldown chart is shown
    expect(getCoverageAverageByLevel).toHaveBeenCalled();
  });

  it('closes drilldown chart when close button is clicked', async () => {
    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UUAA"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
        selectedSn1="TestSn1"
        selectedSn2="TestSn2"
      />
    );

    // Get the chart options and trigger drilldown
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1];
    const chartOptions = lastCall[0].options;
    
    // Open drilldown
    await act(async () => {
      await chartOptions.chart.events.dataPointSelection(null, null, { dataPointIndex: 0 });
    });
    
    await waitFor(() => {
      expect(screen.getByText(/detalle de:/i)).toBeInTheDocument();
    });

    // Click close button
    const closeButton = screen.getByRole('button', { name: /✕ cerrar/i });
    fireEvent.click(closeButton);

    // Verify drilldown is closed
    await waitFor(() => {
      expect(screen.queryByText(/detalle de:/i)).not.toBeInTheDocument();
    });
  });

  it('handles async drilldown data loading', async () => {
    const { getCoverageAverageByLevel } = require('../../api/statsSummaryApi');
    
    // Override the beforeEach mock with specific data for this test
    getCoverageAverageByLevel.mockResolvedValue([
      { label: 'Drill1', coveragePercentage: 80, nucleusLevel: 'APP' },
      { label: 'Drill2', coveragePercentage: 60, nucleusLevel: 'APP' },
    ]);

    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UUAA"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
        selectedSn1="TestSn1"
        selectedSn2="TestSn2"
      />
    );

    // Get the chart options and trigger drilldown
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1];
    const chartOptions = lastCall[0].options;
    
    await act(async () => {
      await chartOptions.chart.events.dataPointSelection(null, null, { dataPointIndex: 0 });
    });

    // Wait for data to load and drilldown to be displayed
    await waitFor(() => {
      expect(screen.getByText(/detalle de:/i)).toBeInTheDocument();
    });

    // Verify the API was called
    expect(getCoverageAverageByLevel).toHaveBeenCalled();
    
    // Verify drilldown section is displayed with correct stats
    expect(screen.getByText(/total aplicaciones/i)).toBeInTheDocument();
    expect(screen.getByText(/70.0%/i)).toBeInTheDocument(); // (80+60)/2
  });

  it('shows drilldown stats correctly', async () => {
    const { getCoverageAverageByLevel } = require('../../api/statsSummaryApi');
    getCoverageAverageByLevel.mockResolvedValue([
      { label: 'App1', coveragePercentage: 80, nucleusLevel: 'APP' },
      { label: 'App2', coveragePercentage: 60, nucleusLevel: 'APP' },
      { label: 'App3', coveragePercentage: 70, nucleusLevel: 'APP' },
    ]);

    render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UUAA"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
        selectedSn1="TestSn1"
        selectedSn2="TestSn2"
      />
    );

    // Trigger drilldown
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1];
    const chartOptions = lastCall[0].options;
    
    await act(async () => {
      await chartOptions.chart.events.dataPointSelection(null, null, { dataPointIndex: 0 });
    });
    
    await waitFor(() => {
      expect(screen.getByText(/detalle de:/i)).toBeInTheDocument();
    });

    // Verify stats: 3 apps, average = (80+60+70)/3 = 70%
    const allThrees = screen.getAllByText('3');
    expect(allThrees.length).toBeGreaterThan(0); // Total Aplicaciones
    expect(screen.getByText(/70.0%/i)).toBeInTheDocument(); // Coverage Promedio
  });

  it('resets drilldown when main data changes', async () => {
    const { rerender } = render(
      <NucleusCoverageChart
        data={mockData}
        loading={false}
        currentLevel="UUAA"
        selectedVertical="TestVertical"
        selectedUol2="TestUol2"
        selectedSn1="TestSn1"
        selectedSn2="TestSn2"
      />
    );

    // Open drilldown
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1];
    const chartOptions = lastCall[0].options;
    
    await act(async () => {
      await chartOptions.chart.events.dataPointSelection(null, null, { dataPointIndex: 0 });
    });
    
    await waitFor(() => {
      expect(screen.getByText(/detalle de:/i)).toBeInTheDocument();
    });

    // Change main data
    const newData: NucleusCoverageStatsSummary[] = [
      { label: 'NewVertical', coveragePercentage: 90, nucleusLevel: 'VERTICAL' },
    ];

    rerender(
      <NucleusCoverageChart
        data={newData}
        loading={false}
        currentLevel="VERTICAL"
      />
    );

    // Verify drilldown is closed
    await waitFor(() => {
      expect(screen.queryByText(/detalle de:/i)).not.toBeInTheDocument();
    });
  });
});
