import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import DeudaTecnicaChart from '../../../components/Charts/DeudaTecnicaChart';
import { DeudaTecnicaItem } from '../../../hooks/useDeudaTecnica';
import '@testing-library/jest-dom';

// Mock de ApexCharts para evitar problemas de renderizado
const mockApexChart = jest.fn();
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: function MockApexChart(props: any) {
    mockApexChart(props);
    return (
      <div
        data-testid="deuda-tecnica-chart"
        data-series={JSON.stringify(props.series)}
        data-options={JSON.stringify(props.options)}
        data-type={props.type}
        data-height={props.height}
      >
        Mocked Chart
      </div>
    );
  }
}));

// Mock del hook useDeudaTecnica
const mockUseDeudaTecnica = jest.fn();
jest.mock('../../../hooks/useDeudaTecnica', () => ({
  useDeudaTecnica: (filters: any) => mockUseDeudaTecnica(filters),
  DeudaTecnicaItem: {} as any
}));

describe('DeudaTecnicaChart', () => {
  const mockData: DeudaTecnicaItem[] = [
    {
      label: 'Vertical1',
      nivelTipo: 'VERTICAL',
      totalBugs: 45,
      totalVulnerabilities: 30,
      totalCodeSmells: 120,
      totalApps: 10
    },
    {
      label: 'Vertical2',
      nivelTipo: 'VERTICAL',
      totalBugs: 38,
      totalVulnerabilities: 25,
      totalCodeSmells: 95,
      totalApps: 8
    },
    {
      label: 'Vertical3',
      nivelTipo: 'VERTICAL',
      totalBugs: 52,
      totalVulnerabilities: 40,
      totalCodeSmells: 150,
      totalApps: 12
    }
  ];

  beforeEach(() => {
    mockApexChart.mockClear();
    mockUseDeudaTecnica.mockClear();
    mockUseDeudaTecnica.mockReturnValue({
      data: mockData,
      loading: false,
      error: null
    });
  });

  it('renders loading state', () => {
    mockUseDeudaTecnica.mockReturnValue({
      data: [],
      loading: true,
      error: null
    });

    render(<DeudaTecnicaChart />);
    
    expect(screen.getByText((_content, element) => {
      return element?.classList.contains('animate-spin') || false;
    })).toBeInTheDocument();
  });

  it('renders error state', () => {
    mockUseDeudaTecnica.mockReturnValue({
      data: [],
      loading: false,
      error: 'Error al cargar datos de Deuda Técnica'
    });

    render(<DeudaTecnicaChart />);
    
    expect(screen.getByText('Error al cargar datos de Deuda Técnica:')).toBeInTheDocument();
    expect(screen.getByText('Error al cargar datos de Deuda Técnica')).toBeInTheDocument();
  });

  it('renders empty state when no data', () => {
    mockUseDeudaTecnica.mockReturnValue({
      data: [],
      loading: false,
      error: null
    });

    render(<DeudaTecnicaChart />);
    
    expect(screen.getByText(/no hay datos disponibles para los filtros seleccionados/i)).toBeInTheDocument();
  });

  it('renders chart with data correctly', () => {
    render(<DeudaTecnicaChart />);
    
    expect(screen.getByTestId('deuda-tecnica-chart')).toBeInTheDocument();
    expect(screen.getByText(/comparación por vertical/i)).toBeInTheDocument();
    expect(screen.getByText(/haz clic en una barra para ver el desglose del siguiente nivel/i)).toBeInTheDocument();
  });

  it('renders metric selector with correct options', () => {
    render(<DeudaTecnicaChart />);
    
    const select = screen.getByRole('combobox');
    expect(select).toBeInTheDocument();
    expect(screen.getByText('Bugs')).toBeInTheDocument();
    expect(screen.getByText('Vulnerabilidades')).toBeInTheDocument();
    expect(screen.getByText('Code Smells')).toBeInTheDocument();
  });

  it('changes metric when selecting different option', () => {
    render(<DeudaTecnicaChart />);
    
    const select = screen.getByRole('combobox');
    
    // Default is bugs
    expect(select).toHaveValue('bugs');
    
    // Change to vulnerabilities
    fireEvent.change(select, { target: { value: 'vulnerabilities' } });
    expect(select).toHaveValue('vulnerabilities');
    
    // Change to codeSmells
    fireEvent.change(select, { target: { value: 'codeSmells' } });
    expect(select).toHaveValue('codeSmells');
  });

  it('displays correct chart data for bugs metric', () => {
    render(<DeudaTecnicaChart />);
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].name).toBe('Bugs');
    expect(series[0].data).toEqual([45, 38, 52]);
  });

  it('displays correct chart data for vulnerabilities metric', () => {
    render(<DeudaTecnicaChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'vulnerabilities' } });
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].name).toBe('Vulnerabilidades');
    expect(series[0].data).toEqual([30, 25, 40]);
  });

  it('displays correct chart data for codeSmells metric', () => {
    render(<DeudaTecnicaChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'codeSmells' } });
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].name).toBe('Code Smells');
    expect(series[0].data).toEqual([120, 95, 150]);
  });

  it('renders different level labels correctly', () => {
    const fabricaData: DeudaTecnicaItem[] = [
      {
        label: 'Fabrica1',
        nivelTipo: 'FABRICA',
        totalBugs: 20,
        totalVulnerabilities: 15,
        totalCodeSmells: 80,
        totalApps: 5
      }
    ];

    mockUseDeudaTecnica.mockReturnValue({
      data: fabricaData,
      loading: false,
      error: null
    });

    const { rerender } = render(<DeudaTecnicaChart />);
    expect(screen.getByText(/comparación por fábrica/i)).toBeInTheDocument();

    // Test SN1 level
    const sn1Data: DeudaTecnicaItem[] = [
      {
        label: 'SN1-1',
        nivelTipo: 'SN1',
        totalBugs: 10,
        totalVulnerabilities: 8,
        totalCodeSmells: 40,
        totalApps: 3
      }
    ];

    mockUseDeudaTecnica.mockReturnValue({
      data: sn1Data,
      loading: false,
      error: null
    });

    rerender(<DeudaTecnicaChart />);
    expect(screen.getByText(/comparación por sn1/i)).toBeInTheDocument();

    // Test SN2 level
    const sn2Data: DeudaTecnicaItem[] = [
      {
        label: 'SN2-1',
        nivelTipo: 'SN2',
        totalBugs: 5,
        totalVulnerabilities: 4,
        totalCodeSmells: 20,
        totalApps: 2
      }
    ];

    mockUseDeudaTecnica.mockReturnValue({
      data: sn2Data,
      loading: false,
      error: null
    });

    rerender(<DeudaTecnicaChart />);
    expect(screen.getByText(/comparación por sn2/i)).toBeInTheDocument();
  });

  it('shows back button when filters are applied', () => {
    render(
      <DeudaTecnicaChart
        selectedVertical="Vertical1"
        onBackClick={jest.fn()}
      />
    );
    
    expect(screen.getByRole('button', { name: /volver/i })).toBeInTheDocument();
  });

  it('does not show back button when no filters are applied', () => {
    render(<DeudaTecnicaChart />);
    
    expect(screen.queryByRole('button', { name: /volver/i })).not.toBeInTheDocument();
  });

  it('calls onBackClick when back button is clicked', () => {
    const mockOnBackClick = jest.fn();
    
    render(
      <DeudaTecnicaChart
        selectedVertical="Vertical1"
        onBackClick={mockOnBackClick}
      />
    );
    
    const backButton = screen.getByRole('button', { name: /volver/i });
    fireEvent.click(backButton);
    
    expect(mockOnBackClick).toHaveBeenCalledTimes(1);
  });

  it('shows back button with UOL2 filter', () => {
    render(
      <DeudaTecnicaChart
        selectedUol2="Fabrica1"
        onBackClick={jest.fn()}
      />
    );
    
    expect(screen.getByRole('button', { name: /volver/i })).toBeInTheDocument();
  });

  it('shows back button with SN1 filter', () => {
    render(
      <DeudaTecnicaChart
        selectedSn1="SN1-1"
        onBackClick={jest.fn()}
      />
    );
    
    expect(screen.getByRole('button', { name: /volver/i })).toBeInTheDocument();
  });

  it('passes correct filters to useDeudaTecnica hook', () => {
    render(
      <DeudaTecnicaChart
        selectedVertical="Vertical1"
        selectedUol2="Fabrica1"
        selectedSn1="SN1-1"
      />
    );
    
    expect(mockUseDeudaTecnica).toHaveBeenCalledWith({
      vertical: 'Vertical1',
      fabrica: 'Fabrica1',
      sn1: 'SN1-1'
    });
  });

  it('chart options are configured correctly', () => {
    render(<DeudaTecnicaChart />);
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    expect(options.chart.type).toBe('bar');
    expect(options.plotOptions.bar.horizontal).toBe(true);
    expect(options.plotOptions.bar.distributed).toBe(true);
    expect(options.legend.show).toBe(false);
  });

  it('chart categories match data labels', () => {
    render(<DeudaTecnicaChart />);
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    expect(options.xaxis.categories).toEqual(['Vertical1', 'Vertical2', 'Vertical3']);
  });

  it('chart height adjusts based on data length', () => {
    const { rerender } = render(<DeudaTecnicaChart />);
    
    let chart = screen.getByTestId('deuda-tecnica-chart');
    let height = parseInt(chart.getAttribute('data-height') || '0');
    
    // With 3 items: max(300, 3 * 50) = 300
    expect(height).toBe(300);
    
    // Test with more items
    const moreData: DeudaTecnicaItem[] = Array.from({ length: 10 }, (_, i) => ({
      label: `Vertical${i + 1}`,
      nivelTipo: 'VERTICAL',
      totalBugs: 10 + i,
      totalVulnerabilities: 5 + i,
      totalCodeSmells: 50 + i,
      totalApps: 2 + i
    }));
    
    mockUseDeudaTecnica.mockReturnValue({
      data: moreData,
      loading: false,
      error: null
    });
    
    rerender(<DeudaTecnicaChart />);
    
    chart = screen.getByTestId('deuda-tecnica-chart');
    height = parseInt(chart.getAttribute('data-height') || '0');
    
    // With 10 items: max(300, 10 * 50) = 500
    expect(height).toBe(500);
  });

  it('tooltip displays all metrics', () => {
    render(<DeudaTecnicaChart />);
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    // Verify tooltip is configured
    expect(options.tooltip).toBeDefined();
    
    // The tooltip.custom function is defined in the component
    // We verify the structure exists
    expect(mockApexChart).toHaveBeenCalled();
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0];
    expect(lastCall.options.tooltip).toBeDefined();
    expect(lastCall.options.tooltip.custom).toBeDefined();
    
    // Test tooltip generation with the actual function
    const tooltipHtml = lastCall.options.tooltip.custom({ dataPointIndex: 0 });
    expect(tooltipHtml).toContain('Vertical1');
    expect(tooltipHtml).toContain('Bugs: 45');
    expect(tooltipHtml).toContain('Vulnerabilidades: 30');
    expect(tooltipHtml).toContain('Code Smells: 120');
    expect(tooltipHtml).toContain('Total Apps: 10');
  });

  it('calls onDrillDown when bar is clicked', () => {
    const mockOnDrillDown = jest.fn();
    
    render(<DeudaTecnicaChart onDrillDown={mockOnDrillDown} />);
    
    // Get the actual function from the mock call
    expect(mockApexChart).toHaveBeenCalled();
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0];
    
    // Simulate bar click
    const config = { dataPointIndex: 1 };
    lastCall.options.chart.events.dataPointSelection({}, {}, config);
    
    expect(mockOnDrillDown).toHaveBeenCalledWith('Vertical2', 'VERTICAL');
  });

  it('does not call onDrillDown when callback is not provided', () => {
    render(<DeudaTecnicaChart />);
    
    // Get the actual function from the mock call
    expect(mockApexChart).toHaveBeenCalled();
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0];
    
    // Simulate bar click - should not throw error
    const config = { dataPointIndex: 0 };
    expect(() => {
      lastCall.options.chart.events.dataPointSelection({}, {}, config);
    }).not.toThrow();
  });

  it('renders with all props provided', () => {
    const mockOnDrillDown = jest.fn();
    const mockOnBackClick = jest.fn();
    
    render(
      <DeudaTecnicaChart
        selectedVertical="Vertical1"
        selectedUol2="Fabrica1"
        selectedSn1="SN1-1"
        onDrillDown={mockOnDrillDown}
        onBackClick={mockOnBackClick}
      />
    );
    
    expect(screen.getByTestId('deuda-tecnica-chart')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /volver/i })).toBeInTheDocument();
    expect(mockUseDeudaTecnica).toHaveBeenCalledWith({
      vertical: 'Vertical1',
      fabrica: 'Fabrica1',
      sn1: 'SN1-1'
    });
  });

  it('updates chart when metric changes', async () => {
    render(<DeudaTecnicaChart />);
    
    const select = screen.getByRole('combobox');
    
    // Initial state - bugs
    let chart = screen.getByTestId('deuda-tecnica-chart');
    let series = JSON.parse(chart.getAttribute('data-series') || '[]');
    expect(series[0].data).toEqual([45, 38, 52]);
    
    // Change to vulnerabilities
    fireEvent.change(select, { target: { value: 'vulnerabilities' } });
    
    await waitFor(() => {
      chart = screen.getByTestId('deuda-tecnica-chart');
      series = JSON.parse(chart.getAttribute('data-series') || '[]');
      expect(series[0].data).toEqual([30, 25, 40]);
    });
    
    // Change to code smells
    fireEvent.change(select, { target: { value: 'codeSmells' } });
    
    await waitFor(() => {
      chart = screen.getByTestId('deuda-tecnica-chart');
      series = JSON.parse(chart.getAttribute('data-series') || '[]');
      expect(series[0].data).toEqual([120, 95, 150]);
    });
  });

  it('xaxis title updates when metric changes', () => {
    const { rerender } = render(<DeudaTecnicaChart />);
    
    const select = screen.getByRole('combobox');
    
    // Check initial title for bugs
    let chart = screen.getByTestId('deuda-tecnica-chart');
    let options = JSON.parse(chart.getAttribute('data-options') || '{}');
    expect(options.xaxis.title.text).toBe('Bugs');
    
    // Change to vulnerabilities
    fireEvent.change(select, { target: { value: 'vulnerabilities' } });
    rerender(<DeudaTecnicaChart />);
    
    chart = screen.getByTestId('deuda-tecnica-chart');
    options = JSON.parse(chart.getAttribute('data-options') || '{}');
    expect(options.xaxis.title.text).toBe('Vulnerabilidades');
    
    // Change to code smells
    fireEvent.change(select, { target: { value: 'codeSmells' } });
    rerender(<DeudaTecnicaChart />);
    
    chart = screen.getByTestId('deuda-tecnica-chart');
    options = JSON.parse(chart.getAttribute('data-options') || '{}');
    expect(options.xaxis.title.text).toBe('Code Smells');
  });

  it('handles zero values correctly', () => {
    const zeroData: DeudaTecnicaItem[] = [
      {
        label: 'Vertical1',
        nivelTipo: 'VERTICAL',
        totalBugs: 0,
        totalVulnerabilities: 0,
        totalCodeSmells: 0,
        totalApps: 0
      }
    ];

    mockUseDeudaTecnica.mockReturnValue({
      data: zeroData,
      loading: false,
      error: null
    });

    render(<DeudaTecnicaChart />);
    
    const chart = screen.getByTestId('deuda-tecnica-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].data).toEqual([0]);
  });
});
