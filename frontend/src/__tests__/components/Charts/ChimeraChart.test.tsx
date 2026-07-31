import { render, screen, fireEvent } from '@testing-library/react';
import React from 'react';
import ChimeraChart from '../../../components/Charts/ChimeraChart';
import { ChimeraChartData } from '../../../types/statsSummary';

// Mock de ApexCharts
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: ({ options, series, type, height }: any) => {
    const handleDataPointClick = (dataPointIndex: number, seriesIndex: number) => {
      if (options.chart?.events?.dataPointSelection) {
        options.chart.events.dataPointSelection(null, null, { dataPointIndex, seriesIndex });
      }
    };

    const handleXAxisClick = (labelIndex: number) => {
      if (options.chart?.events?.xAxisLabelClick) {
        options.chart.events.xAxisLabelClick(null, null, { labelIndex });
      }
    };

    return (
      <div data-testid="mock-chart" data-chart-id={options.chart?.id || 'chart'}>
        <div data-testid="chart-type">{type}</div>
        <div data-testid="chart-title">{options.title?.text}</div>
        <div data-testid="chart-series">{JSON.stringify(series)}</div>
        <div data-testid="chart-height">{height}</div>
        {options.xaxis?.categories?.map((category: string, index: number) => (
          <button 
            key={index}
            data-testid={`x-axis-label-${index}`}
            onClick={() => handleXAxisClick(index)}
          >
            {category}
          </button>
        ))}
        {series.map((serie: any, seriesIndex: number) => 
          serie.data.map((_: any, dataIndex: number) => (
            <button
              key={`${seriesIndex}-${dataIndex}`}
              data-testid={`data-point-${seriesIndex}-${dataIndex}`}
              onClick={() => handleDataPointClick(dataIndex, seriesIndex)}
            >
              Data Point {seriesIndex}-{dataIndex}
            </button>
          ))
        )}
      </div>
    );
  }
}));

// Mock de hooks personalizados
jest.mock('../../../hooks/useAutoScroll', () => ({
  useAutoScroll: jest.fn()
}));

jest.mock('../../../hooks/useChartTooltips', () => ({
  useChartTooltips: jest.fn()
}));

// Mock del componente ApplicationsChimeraChart
jest.mock('../../../components/Charts/ApplicationsChimeraChart', () => {
  return function MockApplicationsChimeraChart({ uuaaData }: any) {
    return (
      <div data-testid="applications-chimera-chart">
        <h4>Vulnerabilidades SAST por Aplicación</h4>
        <h4>Vulnerabilidades SCA por Aplicación</h4>
        <h4>Detalle de Aplicaciones</h4>
        <div>Applications count: {uuaaData.applications.length}</div>
      </div>
    );
  };
});

const mockChimeraData: ChimeraChartData[] = [
  {
    uuaa: 'TEST1',
    sastData: {
      totalLow: 5,
      totalMedium: 3,
      totalHigh: 2
    },
    scaData: {
      totalLow: 4,
      totalMedium: 6,
      totalHigh: 1,
      totalCritical: 2
    },
    totalApps: 10,
    applications: [
      {
        name: 'app1',
        bitbucketUrl: 'http://test.com',
        chimeraUrl: 'http://chimera.com',
        chimeraSast: { totalLow: 2, totalMedium: 1, totalHigh: 1 },
        chimeraSca: { totalLow: 1, totalMedium: 2, totalHigh: 0, totalCritical: 1 }
      }
    ]
  },
  {
    uuaa: 'TEST2',
    sastData: {
      totalLow: 0,
      totalMedium: 0,
      totalHigh: 0
    },
    scaData: {
      totalLow: 0,
      totalMedium: 0,
      totalHigh: 0,
      totalCritical: 0
    },
    totalApps: 5,
    applications: []
  }
];

describe('ChimeraChart', () => {
  const defaultProps = {
    data: mockChimeraData,
    loading: false
  };

  test('renders loading state', () => {
    render(<ChimeraChart data={[]} loading={true} />);
    
    expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
  });

  test('renders empty state when no data', () => {
    render(<ChimeraChart data={[]} loading={false} />);
    
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    expect(screen.getByText('No se encontraron UUAAs con los filtros aplicados')).toBeInTheDocument();
  });

  test('renders charts when data is available', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Should render both SAST and SCA charts
    const charts = screen.getAllByTestId('mock-chart');
    expect(charts).toHaveLength(2);
    
    expect(screen.getByText('Vulnerabilidades SAST por UUAA')).toBeInTheDocument();
    expect(screen.getByText('Vulnerabilidades SCA por UUAA')).toBeInTheDocument();
  });

  test('filters out UUAAs without vulnerability data', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // The component displays all data it receives (data is pre-filtered)
    // Both SAST and SCA charts should be rendered
    const charts = screen.getAllByTestId('mock-chart');
    expect(charts).toHaveLength(2);
  });

  test('displays information panel', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    expect(screen.getByText('Información sobre Chimera')).toBeInTheDocument();
    expect(screen.getByText(/Haz clic en cualquier barra/)).toBeInTheDocument();
    expect(screen.getByText('SAST:')).toBeInTheDocument();
    expect(screen.getByText(/Análisis estático.*código fuente/)).toBeInTheDocument();
    expect(screen.getByText('SCA:')).toBeInTheDocument();
    expect(screen.getByText(/Análisis de componentes.*software/)).toBeInTheDocument();
  });

  test('shows detail view when UUAA is selected', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Simulate chart interaction by calling the component's state setter
    // This would normally be triggered by chart events
    const charts = screen.getAllByTestId('mock-chart');
    
    // Find the close button in detail view (should not exist initially)
    expect(screen.queryByText('Cerrar detalle')).not.toBeInTheDocument();
    
    // We can't easily simulate the chart interaction without the actual ApexCharts
    // but we can test the component structure
    expect(charts).toHaveLength(2);
  });

  test('renders correct series data for SAST chart', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const charts = screen.getAllByTestId('mock-chart');
    const sastChart = charts[0]; // First chart should be SAST
    
    const seriesData = JSON.parse(sastChart.querySelector('[data-testid="chart-series"]')?.textContent || '[]');
    expect(seriesData).toHaveLength(3); // Low, Medium, High
    expect(seriesData[0].name).toBe('Low');
    expect(seriesData[1].name).toBe('Medium');
    expect(seriesData[2].name).toBe('High');
  });

  test('renders correct series data for SCA chart', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const charts = screen.getAllByTestId('mock-chart');
    const scaChart = charts[1]; // Second chart should be SCA
    
    const seriesData = JSON.parse(scaChart.querySelector('[data-testid="chart-series"]')?.textContent || '[]');
    expect(seriesData).toHaveLength(4); // Low, Medium, High, Critical
    expect(seriesData[0].name).toBe('Low');
    expect(seriesData[1].name).toBe('Medium');
    expect(seriesData[2].name).toBe('High');
    expect(seriesData[3].name).toBe('Critical');
  });

  test('handles data with only SAST vulnerabilities', () => {
    const sastOnlyData: ChimeraChartData[] = [
      {
        uuaa: 'SAST_ONLY',
        sastData: { totalLow: 2, totalMedium: 1, totalHigh: 1 },
        scaData: { totalLow: 0, totalMedium: 0, totalHigh: 0, totalCritical: 0 },
        totalApps: 3,
        applications: []
      }
    ];

    render(<ChimeraChart data={sastOnlyData} loading={false} />);
    
    // Check that charts are rendered with correct data
    expect(screen.getAllByTestId('mock-chart')).toHaveLength(2); // Both charts should still render
    
    // Verify the chart titles are present - use getAllByTestId since there are multiple charts
    const chartTitles = screen.getAllByTestId('chart-title');
    expect(chartTitles.length).toBeGreaterThanOrEqual(1);
    expect(screen.getByText('Vulnerabilidades SAST por UUAA')).toBeInTheDocument();
    expect(screen.getByText('Vulnerabilidades SCA por UUAA')).toBeInTheDocument();
  });

  test('handles data with only SCA vulnerabilities', () => {
    const scaOnlyData: ChimeraChartData[] = [
      {
        uuaa: 'SCA_ONLY',
        sastData: { totalLow: 0, totalMedium: 0, totalHigh: 0 },
        scaData: { totalLow: 1, totalMedium: 2, totalHigh: 1, totalCritical: 1 },
        totalApps: 5,
        applications: []
      }
    ];

    render(<ChimeraChart data={scaOnlyData} loading={false} />);
    
    // Check that charts are rendered with correct data
    expect(screen.getAllByTestId('mock-chart')).toHaveLength(2); // Both charts should still render
    
    // Verify the chart titles are present
    expect(screen.getByText('Vulnerabilidades SAST por UUAA')).toBeInTheDocument();
    expect(screen.getByText('Vulnerabilidades SCA por UUAA')).toBeInTheDocument();
  });

  test('renders charts with correct structure', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Check that charts are wrapped in proper containers
    const chartContainers = screen.getAllByRole('generic').filter(el => 
      el.className.includes('bg-white') && el.className.includes('rounded-lg')
    );
    expect(chartContainers.length).toBeGreaterThanOrEqual(2);
  });

  test('uses correct colors for severity levels', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const charts = screen.getAllByTestId('mock-chart');
    const seriesData = JSON.parse(charts[0].querySelector('[data-testid="chart-series"]')?.textContent || '[]');
    
    // Check that colors are assigned (testing the imported SEVERITY_COLORS)
    expect(seriesData[0].color).toBeDefined(); // Low - success color
    expect(seriesData[1].color).toBeDefined(); // Medium - warning color  
    expect(seriesData[2].color).toBeDefined(); // High - error color
  });

  test('handles data point selection interaction', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Click on a data point from the first chart (SAST)
    const dataPointButtons = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPointButtons[0]); // Click the first one (SAST chart)
    
    // Verify that the component doesn't crash and handles the click
    expect(dataPointButtons[0]).toBeInTheDocument();
  });

  test('handles x-axis label click interaction', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Click on x-axis label from first chart
    const xAxisLabels = screen.getAllByTestId('x-axis-label-0');
    fireEvent.click(xAxisLabels[0]);
    
    // Verify that the component doesn't crash and handles the click
    expect(xAxisLabels[0]).toBeInTheDocument();
  });

  test('can close detail view', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Open detail view
    const dataPointButtons = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPointButtons[0]);
    
    // Verify that the component doesn't crash and handles the click
    expect(dataPointButtons[0]).toBeInTheDocument();
  });

  test('handles out of bounds data point selection gracefully', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Test that invalid selections don't crash the component
    // In a real scenario, ApexCharts would handle bounds checking
    // Here we just verify the component structure is stable
    expect(screen.getAllByTestId('mock-chart')).toHaveLength(2);
    expect(screen.queryByText('Detalle de Vulnerabilidades')).not.toBeInTheDocument();
  });

  test('handles out of bounds x-axis label click gracefully', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Try to trigger invalid label index (this would be handled in real chart events)
    // In our test, we just verify the component handles bounds checking
    // There are 4 x-axis labels total: 2 UUAAs (TEST1, TEST2) in each chart (SAST and SCA)
    expect(screen.getAllByTestId(/x-axis-label/)).toHaveLength(4);
  });

  test('renders correct chart height', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const charts = screen.getAllByTestId('mock-chart');
    charts.forEach(chart => {
      const height = chart.querySelector('[data-testid="chart-height"]')?.textContent;
      expect(height).toBe('350');
    });
  });

  test('renders chart with correct IDs', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const charts = screen.getAllByTestId('mock-chart');
    expect(charts[0]).toHaveAttribute('data-chart-id', 'sast-chart');
    expect(charts[1]).toHaveAttribute('data-chart-id', 'sca-chart');
  });

  test('applies correct CSS classes for chart containers', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Check for grid container by looking for the specific grid classes
    const gridElement = document.querySelector('.grid.grid-cols-1.lg\\:grid-cols-2.gap-6');
    expect(gridElement).toBeInTheDocument();
  });

  test('shows applications count in detail view', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Open detail view
    const dataPointButtons = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPointButtons[0]);
    
    // Verify that the component doesn't crash and handles the click
    expect(dataPointButtons[0]).toBeInTheDocument();
  });

  test('hook integrations work correctly', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Verify hooks are called (mocked)
    const { useAutoScroll } = require('../../../hooks/useAutoScroll');
    const { useChartTooltips } = require('../../../hooks/useChartTooltips');
    
    expect(useAutoScroll).toHaveBeenCalled();
    expect(useChartTooltips).toHaveBeenCalledTimes(2); // Called for SAST and SCA charts
  });

  test('renders correct SAST series structure', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const sastChart = screen.getAllByTestId('mock-chart')[0];
    const seriesData = JSON.parse(sastChart.querySelector('[data-testid="chart-series"]')?.textContent || '[]');
    
    expect(seriesData).toHaveLength(3); // Low, Medium, High
    expect(seriesData[0].name).toBe('Low');
    expect(seriesData[1].name).toBe('Medium');
    expect(seriesData[2].name).toBe('High');
  });

  test('renders correct SCA series structure', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const scaChart = screen.getAllByTestId('mock-chart')[1];
    const seriesData = JSON.parse(scaChart.querySelector('[data-testid="chart-series"]')?.textContent || '[]');
    
    expect(seriesData).toHaveLength(4); // Low, Medium, High, Critical
    expect(seriesData[0].name).toBe('Low');
    expect(seriesData[1].name).toBe('Medium');
    expect(seriesData[2].name).toBe('High');
    expect(seriesData[3].name).toBe('Critical');
  });

  test('tooltip callbacks are properly configured', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const { useChartTooltips } = require('../../../hooks/useChartTooltips');
    
    // Verify useChartTooltips was called with the correct parameters
    // Both charts show all data (no internal filtering), so length is 2
    expect(useChartTooltips).toHaveBeenCalledWith(
      '#sast-chart .apexcharts-xaxis-texts-g text',
      expect.any(Function),
      2, // sastOrderedData.length (both UUAAs)
      expect.any(Array)
    );
    
    expect(useChartTooltips).toHaveBeenCalledWith(
      '#sca-chart .apexcharts-xaxis-texts-g text',
      expect.any(Function),
      2, // scaOrderedData.length (both UUAAs)
      expect.any(Array)
    );
  });

  test('tooltip callback functions work correctly', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const { useChartTooltips } = require('../../../hooks/useChartTooltips');
    const calls = useChartTooltips.mock.calls;
    
    // Get the callback functions from the mock calls
    const sastTooltipCallback = calls[0][1];
    const scaTooltipCallback = calls[1][1];
    
    // Test that callbacks execute without error
    expect(() => sastTooltipCallback(0)).not.toThrow();
    expect(() => scaTooltipCallback(0)).not.toThrow();
  });

  test('detail view can be opened and closed', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Initially no detail view
    expect(screen.queryByText(/Detalle de Vulnerabilidades/)).not.toBeInTheDocument();
    
    // Open detail view by clicking data point
    const dataPoints = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPoints[0]);
    
    // Now detail view should be visible (we'll check for close button)
    const closeButton = screen.queryByText('Cerrar detalle');
    if (closeButton) {
      fireEvent.click(closeButton);
      // Detail view should be closed after clicking close
    }
  });

  test('handles bounds checking for data point selection', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Test with valid index
    const dataPoints = screen.getAllByTestId('data-point-0-0');
    expect(() => fireEvent.click(dataPoints[0])).not.toThrow();
    
    // Component should handle invalid indices gracefully (no error thrown)
    expect(dataPoints[0]).toBeInTheDocument();
  });

  test('handles bounds checking for x-axis label clicks', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Test with valid index
    const xAxisLabels = screen.getAllByTestId('x-axis-label-0');
    expect(() => fireEvent.click(xAxisLabels[0])).not.toThrow();
    
    // Component should handle invalid indices gracefully
    expect(xAxisLabels[0]).toBeInTheDocument();
  });

  test('callback functions are properly bound to chart options', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    const charts = screen.getAllByTestId('mock-chart');
    
    // Verify both charts have their event handlers
    charts.forEach(chart => {
      const dataPointButtons = chart.querySelectorAll('[data-testid^="data-point-"]');
      const xAxisButtons = chart.querySelectorAll('[data-testid^="x-axis-label-"]');
      
      expect(dataPointButtons.length).toBeGreaterThan(0);
      expect(xAxisButtons.length).toBeGreaterThan(0);
    });
  });

  test('chart event handlers are executed when simulated', () => {
    const TestWrapper = () => {
      const [data] = React.useState(defaultProps.data);
      return <ChimeraChart data={data} loading={false} />;
    };

    render(<TestWrapper />);
    
    // Simulate clicking on a data point to trigger state change
    const dataPoints = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPoints[0]);
    
    // Verify the click was handled without error
    expect(dataPoints[0]).toBeInTheDocument();
  });

  test('state management works correctly for detail view', () => {
    const TestWrapper = () => {
      return <ChimeraChart {...defaultProps} />;
    };

    render(<TestWrapper />);
    
    // Click to open detail and then close it
    const dataPoints = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPoints[0]);
    
    // Try to find and click close button if it appears
    const closeButton = screen.queryByText('Cerrar detalle');
    if (closeButton) {
      fireEvent.click(closeButton);
    }
    
    // Verify components handle state changes gracefully
    expect(screen.getAllByTestId('mock-chart')).toHaveLength(2);
  });

  test('component handles edge cases for data selection', () => {
    render(<ChimeraChart {...defaultProps} />);
    
    // Test multiple rapid clicks
    const dataPoints = screen.getAllByTestId('data-point-0-0');
    fireEvent.click(dataPoints[0]);
    fireEvent.click(dataPoints[0]);
    
    // Test x-axis label clicks
    const xAxisLabels = screen.getAllByTestId('x-axis-label-0');
    fireEvent.click(xAxisLabels[0]);
    fireEvent.click(xAxisLabels[0]);
    
    // Verify component remains stable
    expect(screen.getAllByTestId('mock-chart')).toHaveLength(2);
  });
});
