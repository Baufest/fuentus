import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ProductividadChart from '../../../components/Charts/ProductividadChart';
import { ComparativaData, GraficoNivel, ItemGrafico } from '../../../hooks/useComparativaProductividad';
import '@testing-library/jest-dom';

// Mock de ApexCharts para evitar problemas de renderizado
const mockApexChart = jest.fn();
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: function MockApexChart(props: any) {
    mockApexChart(props);
    return (
      <div
        data-testid="productividad-chart"
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

// Mock del hook useComparativaProductividad
const mockUseComparativaProductividad = jest.fn();
jest.mock('../../../hooks/useComparativaProductividad', () => ({
  useComparativaProductividad: (filters: any) => mockUseComparativaProductividad(filters),
  ItemGrafico: {} as any,
  GraficoNivel: {} as any,
  ComparativaData: {} as any
}));

// Mock del componente DisponibilidadTable
jest.mock('../../../components/Charts/DisponibilidadTable', () => ({
  __esModule: true,
  default: function MockDisponibilidadTable() {
    return <div data-testid="disponibilidad-table">Mocked DisponibilidadTable</div>;
  }
}));

describe('ProductividadChart', () => {
  const mockItems: ItemGrafico[] = [
    {
      nombre: 'Vertical1',
      productividad: 2.345,
      promedioLT: 15.5,
      promedioCT: 8.3,
      totalFeatures: 150,
      totalFTEs: 64.00
    },
    {
      nombre: 'Vertical2',
      productividad: 1.876,
      promedioLT: 20.1,
      promedioCT: 10.5,
      totalFeatures: 98,
      totalFTEs: 52.25
    },
    {
      nombre: 'Vertical3',
      productividad: 3.012,
      promedioLT: 12.3,
      promedioCT: 6.8,
      totalFeatures: 210,
      totalFTEs: 69.75
    }
  ];

  const mockGraficos: GraficoNivel[] = [
    {
      nivelTipo: 'VERTICAL',
      titulo: 'Comparación por Vertical',
      items: mockItems
    }
  ];

  const mockData: ComparativaData = {
    nivelFiltrado: 'VERTICAL',
    graficos: mockGraficos
  };

  beforeEach(() => {
    mockApexChart.mockClear();
    mockUseComparativaProductividad.mockClear();
    mockUseComparativaProductividad.mockReturnValue({
      data: mockData,
      loading: false,
      error: null
    });
  });

  it('renders loading state', () => {
    mockUseComparativaProductividad.mockReturnValue({
      data: null,
      loading: true,
      error: null
    });

    render(<ProductividadChart />);
    
    expect(screen.getByText((_content, element) => {
      return element?.classList.contains('animate-spin') || false;
    })).toBeInTheDocument();
  });

  it('renders error state', () => {
    mockUseComparativaProductividad.mockReturnValue({
      data: null,
      loading: false,
      error: new Error('Error al cargar datos')
    });

    render(<ProductividadChart />);
    
    expect(screen.getByText(/error al cargar los datos/i)).toBeInTheDocument();
  });

  it('renders empty state when no data', () => {
    mockUseComparativaProductividad.mockReturnValue({
      data: { nivelFiltrado: 'VERTICAL', graficos: [] },
      loading: false,
      error: null
    });

    render(<ProductividadChart />);
    
    expect(screen.getByText(/no hay datos disponibles para los filtros seleccionados/i)).toBeInTheDocument();
  });

  it('renders chart with data correctly', () => {
    render(<ProductividadChart />);
    
    expect(screen.getByTestId('productividad-chart')).toBeInTheDocument();
    expect(screen.getByText('Comparación por Vertical')).toBeInTheDocument();
  });

  it('renders metric selector with correct options', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    expect(select).toBeInTheDocument();
    expect(screen.getByText('📊 Productividad')).toBeInTheDocument();
    expect(screen.getByText('⏱️ Lead Time')).toBeInTheDocument();
    expect(screen.getByText('🚀 Cycle Time')).toBeInTheDocument();
    expect(screen.getByText('🎯 Disponibilidad')).toBeInTheDocument();
  });

  it('changes metric when selecting different option', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    
    // Default is productividad
    expect(select).toHaveValue('productividad');
    
    // Change to lt
    fireEvent.change(select, { target: { value: 'lt' } });
    expect(select).toHaveValue('lt');
    
    // Change to ct
    fireEvent.change(select, { target: { value: 'ct' } });
    expect(select).toHaveValue('ct');
  });

  it('displays correct chart data for productividad metric', () => {
    render(<ProductividadChart />);
    
    const chart = screen.getByTestId('productividad-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].name).toBe('Productividad (features/FTE)');
    expect(series[0].data).toEqual([2.345, 1.876, 3.012]);
  });

  it('displays correct chart data for lead time metric', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'lt' } });
    
    const chart = screen.getByTestId('productividad-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].name).toBe('Lead Time (días)');
    expect(series[0].data).toEqual([15.5, 20.1, 12.3]);
  });

  it('displays correct chart data for cycle time metric', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'ct' } });
    
    const chart = screen.getByTestId('productividad-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].name).toBe('Cycle Time (días)');
    expect(series[0].data).toEqual([8.3, 10.5, 6.8]);
  });

  it('shows DisponibilidadTable when disponibilidad metric is selected', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'disponibilidad' } });
    
    expect(screen.getByTestId('disponibilidad-table')).toBeInTheDocument();
    expect(screen.queryByTestId('productividad-chart')).not.toBeInTheDocument();
  });

  it('hides DisponibilidadTable when other metrics are selected', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    
    // Initially, disponibilidad table should not be visible
    expect(screen.queryByTestId('disponibilidad-table')).not.toBeInTheDocument();
    
    // Select disponibilidad
    fireEvent.change(select, { target: { value: 'disponibilidad' } });
    expect(screen.getByTestId('disponibilidad-table')).toBeInTheDocument();
    
    // Select productividad again
    fireEvent.change(select, { target: { value: 'productividad' } });
    expect(screen.queryByTestId('disponibilidad-table')).not.toBeInTheDocument();
  });

  it('passes correct filters to useComparativaProductividad hook', () => {
    render(
      <ProductividadChart
        selectedVertical="Vertical1"
        selectedUol2="Fabrica1"
        selectedSn1="SN1-1"
        selectedSn2="SN2-1"
      />
    );
    
    expect(mockUseComparativaProductividad).toHaveBeenCalledWith({
      vertical: 'Vertical1',
      fabrica: 'Fabrica1',
      sn1: 'SN1-1',
      sn2: 'SN2-1',
      enabled: true
    });
  });

  it('chart options are configured correctly', () => {
    render(<ProductividadChart />);
    
    const chart = screen.getByTestId('productividad-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    expect(options.chart.type).toBe('bar');
    expect(options.plotOptions.bar.horizontal).toBe(true);
    expect(options.plotOptions.bar.distributed).toBe(true);
    expect(options.legend.show).toBe(false);
  });

  it('chart categories match data labels', () => {
    render(<ProductividadChart />);
    
    const chart = screen.getByTestId('productividad-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    expect(options.xaxis.categories).toEqual(['Vertical1', 'Vertical2', 'Vertical3']);
  });

  it('chart height adjusts based on data length', () => {
    const { rerender } = render(<ProductividadChart />);
    
    let chart = screen.getByTestId('productividad-chart');
    let height = parseInt(chart.getAttribute('data-height') || '0');
    
    // With 3 items: max(300, 3 * 50) = 300
    expect(height).toBe(300);
    
    // Test with more items
    const moreItems: ItemGrafico[] = Array.from({ length: 10 }, (_, i) => ({
      nombre: `Vertical${i + 1}`,
      productividad: 2.0 + i * 0.1,
      promedioLT: 15.0 + i,
      promedioCT: 8.0 + i,
      totalFeatures: 100 + i * 10,
      totalFTEs: 50.0 + i * 5
    }));
    
    const moreData: ComparativaData = {
      nivelFiltrado: 'VERTICAL',
      graficos: [{
        nivelTipo: 'VERTICAL',
        titulo: 'Comparación por Vertical',
        items: moreItems
      }]
    };

    mockUseComparativaProductividad.mockReturnValue({
      data: moreData,
      loading: false,
      error: null
    });
    
    rerender(<ProductividadChart />);
    
    chart = screen.getByTestId('productividad-chart');
    height = parseInt(chart.getAttribute('data-height') || '0');
    
    // With 10 items: max(300, 10 * 50) = 500
    expect(height).toBe(500);
  });

  it('tooltip displays all metrics', () => {
    render(<ProductividadChart />);
    
    const chart = screen.getByTestId('productividad-chart');
    const options = JSON.parse(chart.getAttribute('data-options') || '{}');
    
    // Verify tooltip is configured
    expect(options.tooltip).toBeDefined();
    
    // The tooltip.custom function is defined in the component
    expect(mockApexChart).toHaveBeenCalled();
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0];
    expect(lastCall.options.tooltip).toBeDefined();
    expect(lastCall.options.tooltip.custom).toBeDefined();
    
    // Test tooltip generation with the actual function
    const tooltipHtml = lastCall.options.tooltip.custom({ dataPointIndex: 0 });
    expect(tooltipHtml).toContain('Vertical1');
    expect(tooltipHtml).toContain('Productividad:');
    expect(tooltipHtml).toContain('2.345');
    expect(tooltipHtml).toContain('Promedio LT:');
    expect(tooltipHtml).toContain('15.5');
    expect(tooltipHtml).toContain('Promedio CT:');
    expect(tooltipHtml).toContain('8.3');
    expect(tooltipHtml).toContain('Total Features:');
    expect(tooltipHtml).toContain('150');
    expect(tooltipHtml).toContain('Total FTEs:');
    expect(tooltipHtml).toContain('64.00');
  });

  it('updates chart when metric changes', async () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    
    // Initial state - productividad
    let chart = screen.getByTestId('productividad-chart');
    let series = JSON.parse(chart.getAttribute('data-series') || '[]');
    expect(series[0].data).toEqual([2.345, 1.876, 3.012]);
    
    // Change to lt
    fireEvent.change(select, { target: { value: 'lt' } });
    
    await waitFor(() => {
      chart = screen.getByTestId('productividad-chart');
      series = JSON.parse(chart.getAttribute('data-series') || '[]');
      expect(series[0].data).toEqual([15.5, 20.1, 12.3]);
    });
    
    // Change to ct
    fireEvent.change(select, { target: { value: 'ct' } });
    
    await waitFor(() => {
      chart = screen.getByTestId('productividad-chart');
      series = JSON.parse(chart.getAttribute('data-series') || '[]');
      expect(series[0].data).toEqual([8.3, 10.5, 6.8]);
    });
  });

  it('xaxis title updates when metric changes', () => {
    const { rerender } = render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    
    // Check initial title for productividad
    let chart = screen.getByTestId('productividad-chart');
    let options = JSON.parse(chart.getAttribute('data-options') || '{}');
    expect(options.xaxis.title.text).toBe('Productividad (features/FTE)');
    
    // Change to lt
    fireEvent.change(select, { target: { value: 'lt' } });
    rerender(<ProductividadChart />);
    
    chart = screen.getByTestId('productividad-chart');
    options = JSON.parse(chart.getAttribute('data-options') || '{}');
    expect(options.xaxis.title.text).toBe('Lead Time (días)');
    
    // Change to ct
    fireEvent.change(select, { target: { value: 'ct' } });
    rerender(<ProductividadChart />);
    
    chart = screen.getByTestId('productividad-chart');
    options = JSON.parse(chart.getAttribute('data-options') || '{}');
    expect(options.xaxis.title.text).toBe('Cycle Time (días)');
  });

  it('handles null values correctly', () => {
    const nullData: ComparativaData = {
      nivelFiltrado: 'VERTICAL',
      graficos: [{
        nivelTipo: 'VERTICAL',
        titulo: 'Comparación por Vertical',
        items: [
          {
            nombre: 'Vertical1',
            productividad: null,
            promedioLT: null,
            promedioCT: null,
            totalFeatures: 0,
            totalFTEs: 0
          }
        ]
      }]
    };

    mockUseComparativaProductividad.mockReturnValue({
      data: nullData,
      loading: false,
      error: null
    });

    render(<ProductividadChart />);
    
    const chart = screen.getByTestId('productividad-chart');
    const series = JSON.parse(chart.getAttribute('data-series') || '[]');
    
    expect(series[0].data).toEqual([0]);
  });

  it('renders table with data correctly', () => {
    render(<ProductividadChart />);
    
    // Check table headers
    expect(screen.getByText('Nombre')).toBeInTheDocument();
    expect(screen.getByText('Productividad')).toBeInTheDocument();
    expect(screen.getByText('Lead Time (días)')).toBeInTheDocument();
    expect(screen.getByText('Cycle Time (días)')).toBeInTheDocument();
    expect(screen.getByText('Features')).toBeInTheDocument();
    expect(screen.getByText('FTEs')).toBeInTheDocument();
    
    // Check table data
    expect(screen.getByText('Vertical1')).toBeInTheDocument();
    expect(screen.getByText('Vertical2')).toBeInTheDocument();
    expect(screen.getByText('Vertical3')).toBeInTheDocument();
  });

  it('renders multiple graficos when available', () => {
    const multipleGraficos: ComparativaData = {
      nivelFiltrado: 'VERTICAL',
      graficos: [
        {
          nivelTipo: 'VERTICAL',
          titulo: 'Comparación por Vertical',
          items: [mockItems[0]]
        },
        {
          nivelTipo: 'FABRICA',
          titulo: 'Comparación por Fábrica',
          items: [mockItems[1]]
        }
      ]
    };

    mockUseComparativaProductividad.mockReturnValue({
      data: multipleGraficos,
      loading: false,
      error: null
    });

    render(<ProductividadChart />);
    
    expect(screen.getByText('Comparación por Vertical')).toBeInTheDocument();
    expect(screen.getByText('Comparación por Fábrica')).toBeInTheDocument();
  });

  it('shows active filters when provided', () => {
    render(
      <ProductividadChart
        selectedVertical="Vertical1"
        selectedUol2="Fabrica1"
        selectedSn1="SN1-1"
        selectedSn2="SN2-1"
      />
    );
    
    expect(screen.getByText('Filtros activos:')).toBeInTheDocument();
    expect(screen.getByText('Vertical: Vertical1')).toBeInTheDocument();
    expect(screen.getByText('Fábrica: Fabrica1')).toBeInTheDocument();
    expect(screen.getByText('SN1: SN1-1')).toBeInTheDocument();
    expect(screen.getByText('SN2: SN2-1')).toBeInTheDocument();
  });

  it('does not show active filters when none are provided', () => {
    render(<ProductividadChart />);
    
    expect(screen.queryByText('Filtros activos:')).not.toBeInTheDocument();
  });

  it('formatValue formats productividad with 3 decimals', () => {
    render(<ProductividadChart />);
    
    // Get the formatter from dataLabels
    const formatter = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0].options.dataLabels.formatter;
    
    expect(formatter(2.345678)).toBe('2.346');
  });

  it('formatValue formats lead time with 1 decimal', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'lt' } });
    
    // Get the formatter from dataLabels
    const formatter = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0].options.dataLabels.formatter;
    
    expect(formatter(15.567)).toBe('15.6');
  });

  it('renders chart with custom colors', () => {
    render(<ProductividadChart />);
    
    expect(screen.getByTestId('productividad-chart')).toBeInTheDocument();
    
    // Verify colors are configured in the mock call
    expect(mockApexChart).toHaveBeenCalled();
    const lastCall = mockApexChart.mock.calls[mockApexChart.mock.calls.length - 1][0];
    expect(lastCall.options.colors).toBeDefined();
    expect(lastCall.options.colors.length).toBeGreaterThan(0);
  });

  it('renders with all props provided', () => {
    render(
      <ProductividadChart
        selectedVertical="Vertical1"
        selectedUol2="Fabrica1"
        selectedSn1="SN1-1"
        selectedSn2="SN2-1"
      />
    );
    
    expect(screen.getByTestId('productividad-chart')).toBeInTheDocument();
    expect(mockUseComparativaProductividad).toHaveBeenCalledWith({
      vertical: 'Vertical1',
      fabrica: 'Fabrica1',
      sn1: 'SN1-1',
      sn2: 'SN2-1',
      enabled: true
    });
  });

  it('renders title and description correctly', () => {
    render(<ProductividadChart />);
    
    expect(screen.getByText('📊 Sistemática')).toBeInTheDocument();
    expect(screen.getByText(/visualiza y compara las métricas de productividad/i)).toBeInTheDocument();
  });

  it('changes description when disponibilidad metric is selected', () => {
    render(<ProductividadChart />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: 'disponibilidad' } });
    
    expect(screen.getByText(/monitorea la evolución de los niveles de servicio y disponibilidad/i)).toBeInTheDocument();
  });
});
