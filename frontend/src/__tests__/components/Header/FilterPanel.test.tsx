import { render, screen, fireEvent } from '@testing-library/react';
import FilterPanel from '../../../components/Header/FilterPanel';
import { ComboValuesDTO } from '../../../types/nucleus';
import { useDashboardFilters } from '../../../hooks/useDashboardFiltersGlobal';

// Mock the useDashboardFilters hook
jest.mock('../../../hooks/useDashboardFiltersGlobal', () => ({
  useDashboardFilters: jest.fn(),
}));

const mockUseDashboardFilters = useDashboardFilters as jest.MockedFunction<typeof useDashboardFilters>;

describe('FilterPanel', () => {
  const mockComboValues: ComboValuesDTO = {
    verticals: ['Vertical 1', 'Vertical 2', 'Vertical 3'],
    uol2Values: ['UOL2-A', 'UOL2-B', 'UOL2-C'],
    sn1Values: ['SN1-X', 'SN1-Y', 'SN1-Z'],
    sn2Values: ['SN2-1', 'SN2-2', 'SN2-3'],
  };

  const mockDashboardHandlers = {
    handleGeographyChange: jest.fn(),
    handleVerticalChange: jest.fn(),
    setSelectedPeriod: jest.fn(),
    setSelectedUOL2: jest.fn(),
    setOptions: jest.fn(),
    resetFilters: jest.fn(),
  };

  const mockDashboardFilters = {
    selectedPeriod: '',
    selectedGeography: '',
    selectedVertical: '',
    selectedUOL2: [] as string[],
    isVerticalDisabled: false,
  };

  const mockDashboardOptions = {
    availablePeriods: ['2024-01', '2024-02'],
    availableGeographies: ['ARGENTINA', 'MEXICO'],
    availableVerticals: ['Retail', 'Empresas'],
    availableUOL2: ['UOL2-1', 'UOL2-2'],
  };

  const defaultProps = {
    selectedSn1: '',
    setSelectedSn1: jest.fn(),
    selectedSn2: '',
    setSelectedSn2: jest.fn(),
    selectedServerName: '',
    setSelectedServerName: jest.fn(),
    serverNames: ['Server A', 'Server B'],
    serverNamesLoading: false,
    comboValues: mockComboValues,
    comboLoading: false,
    hasActiveFilters: false,
    onClearFilters: jest.fn(),
  };

  beforeEach(() => {
    jest.clearAllMocks();
    mockUseDashboardFilters.mockReturnValue({
      filters: mockDashboardFilters,
      options: mockDashboardOptions,
      handlers: mockDashboardHandlers,
      actions: {} as any,
    });
  });

  it('renders filter panel with title', () => {
    render(<FilterPanel {...defaultProps} />);
    expect(screen.getByText('Filtros')).toBeInTheDocument();
  });

  it('renders all filter labels', () => {
    render(<FilterPanel {...defaultProps} />);
    expect(screen.getByText('Período')).toBeInTheDocument();
    expect(screen.getByText('Geografía')).toBeInTheDocument();
    expect(screen.getByText('Vertical')).toBeInTheDocument();
    expect(screen.getByText('Fábrica')).toBeInTheDocument();
    expect(screen.getByText('Service N1')).toBeInTheDocument();
    expect(screen.getByText('Service N2')).toBeInTheDocument();
    expect(screen.getByText('Servidor')).toBeInTheDocument();
  });

  it('renders all select dropdowns with default options', () => {
    render(<FilterPanel {...defaultProps} />);
    expect(screen.getByText('Todos los períodos')).toBeInTheDocument();
    expect(screen.getByText('Todas las geografías')).toBeInTheDocument();
    expect(screen.getByText('Todas las verticales')).toBeInTheDocument();
    expect(screen.getByText('Todas las fábricas')).toBeInTheDocument();
    expect(screen.getByText('Todos los Service N1')).toBeInTheDocument();
    expect(screen.getByText('Todos los Service N2')).toBeInTheDocument();
    expect(screen.getByText('Todos los servidores')).toBeInTheDocument();
  });

  it('renders SN1 options from comboValues', () => {
    render(<FilterPanel {...defaultProps} />);
    mockComboValues.sn1Values.forEach((sn1) => {
      expect(screen.getByRole('option', { name: sn1 })).toBeInTheDocument();
    });
  });

  it('renders SN2 options from comboValues', () => {
    render(<FilterPanel {...defaultProps} />);
    mockComboValues.sn2Values.forEach((sn2) => {
      expect(screen.getByRole('option', { name: sn2 })).toBeInTheDocument();
    });
  });

  it('renders server options from props', () => {
    render(<FilterPanel {...defaultProps} />);
    defaultProps.serverNames.forEach((server) => {
      expect(screen.getByRole('option', { name: server })).toBeInTheDocument();
    });
  });

  it('calls dashboard handler when vertical is changed', () => {
    render(<FilterPanel {...defaultProps} />);
    const verticalSelect = screen.getByLabelText(/vertical/i);
    fireEvent.change(verticalSelect, { target: { value: 'Retail' } });
    expect(mockDashboardHandlers.handleVerticalChange).toHaveBeenCalled();
  });

  it('calls dashboard handler when UOL2 is changed', () => {
    render(<FilterPanel {...defaultProps} />);
    const uol2Select = screen.getByLabelText(/fábrica/i);
    fireEvent.change(uol2Select, { target: { value: 'UOL2-1' } });
    expect(mockDashboardHandlers.setSelectedUOL2).toHaveBeenCalledWith(['UOL2-1']);
  });

  it('calls setSelectedSn1 when SN1 is changed', () => {
    render(<FilterPanel {...defaultProps} />);
    const sn1Select = screen.getByLabelText(/service n1/i);
    fireEvent.change(sn1Select, { target: { value: 'SN1-X' } });
    expect(defaultProps.setSelectedSn1).toHaveBeenCalledWith('SN1-X');
  });

  it('calls setSelectedSn2 when SN2 is changed', () => {
    render(<FilterPanel {...defaultProps} />);
    const sn2Select = screen.getByLabelText(/service n2/i);
    fireEvent.change(sn2Select, { target: { value: 'SN2-1' } });
    expect(defaultProps.setSelectedSn2).toHaveBeenCalledWith('SN2-1');
  });

  it('calls setSelectedServerName when server is changed', () => {
    render(<FilterPanel {...defaultProps} />);
    const serverSelect = screen.getByLabelText(/servidor/i);
    fireEvent.change(serverSelect, { target: { value: 'Server A' } });
    expect(defaultProps.setSelectedServerName).toHaveBeenCalledWith('Server A');
  });

  it('shows "Limpiar filtros" button when hasActiveFilters is true', () => {
    render(<FilterPanel {...defaultProps} hasActiveFilters={true} />);
    expect(screen.getByText('Limpiar filtros')).toBeInTheDocument();
  });

  it('shows "Limpiar filtros" button when dashboard has active filters', () => {
    mockUseDashboardFilters.mockReturnValue({
      filters: { ...mockDashboardFilters, selectedVertical: 'Retail' },
      options: mockDashboardOptions,
      handlers: mockDashboardHandlers,
      actions: {} as any,
    });
    render(<FilterPanel {...defaultProps} hasActiveFilters={false} />);
    expect(screen.getByText('Limpiar filtros')).toBeInTheDocument();
  });

  it('calls onClearFilters and resetFilters when "Limpiar filtros" is clicked', () => {
    render(<FilterPanel {...defaultProps} hasActiveFilters={true} />);
    const clearButton = screen.getByText('Limpiar filtros');
    fireEvent.click(clearButton);
    expect(defaultProps.onClearFilters).toHaveBeenCalledTimes(1);
    expect(mockDashboardHandlers.resetFilters).toHaveBeenCalledTimes(1);
  });

  it('shows loading message when comboLoading is true', () => {
    render(<FilterPanel {...defaultProps} comboLoading={true} />);
    expect(screen.getByText('Cargando opciones...')).toBeInTheDocument();
  });

  it('shows loading message when serverNamesLoading is true', () => {
    render(<FilterPanel {...defaultProps} serverNamesLoading={true} />);
    expect(screen.getByText('Cargando opciones...')).toBeInTheDocument();
  });

  it('does not show loading message when both loading flags are false', () => {
    render(<FilterPanel {...defaultProps} comboLoading={false} serverNamesLoading={false} />);
    expect(screen.queryByText('Cargando opciones...')).not.toBeInTheDocument();
  });

  it('displays selected SN1 value', () => {
    render(<FilterPanel {...defaultProps} selectedSn1="SN1-Y" />);
    const sn1Select = screen.getByLabelText(/service n1/i) as HTMLSelectElement;
    expect(sn1Select.value).toBe('SN1-Y');
  });

  it('displays selected SN2 value', () => {
    render(<FilterPanel {...defaultProps} selectedSn2="SN2-2" />);
    const sn2Select = screen.getByLabelText(/service n2/i) as HTMLSelectElement;
    expect(sn2Select.value).toBe('SN2-2');
  });

  it('displays selected server value', () => {
    render(<FilterPanel {...defaultProps} selectedServerName="Server B" />);
    const serverSelect = screen.getByLabelText(/servidor/i) as HTMLSelectElement;
    expect(serverSelect.value).toBe('Server B');
  });

  it('handles null comboValues gracefully', () => {
    render(<FilterPanel {...defaultProps} comboValues={null} />);
    expect(screen.getByText('Filtros')).toBeInTheDocument();
  });

  it('handles empty comboValues arrays gracefully', () => {
    const emptyComboValues: ComboValuesDTO = {
      verticals: [],
      uol2Values: [],
      sn1Values: [],
      sn2Values: [],
    };
    render(<FilterPanel {...defaultProps} comboValues={emptyComboValues} />);
    expect(screen.getByText('Filtros')).toBeInTheDocument();
    // Should have 7 selects (period, geography, vertical, uol2, sn1, sn2, server)
    const selects = screen.getAllByRole('combobox');
    expect(selects).toHaveLength(7);
  });

  it('renders with correct CSS classes for dark mode support', () => {
    const { container } = render(<FilterPanel {...defaultProps} />);
    expect(container.querySelector('.dark\\:bg-boxdark')).toBeInTheDocument();
  });

  it('renders panel with correct positioning classes', () => {
    const { container } = render(<FilterPanel {...defaultProps} />);
    const panel = container.firstChild as HTMLElement;
    expect(panel).toHaveClass('absolute');
    expect(panel).toHaveClass('top-full');
    expect(panel).toHaveClass('left-0');
  });
});
