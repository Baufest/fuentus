import { render, screen, fireEvent } from '@testing-library/react';
import RankingModal from '../../../components/Modal/RankingModal';
import { useRanking } from '../../../hooks/useRanking';
import '@testing-library/jest-dom';

// Mock del hook useRanking
jest.mock('../../../hooks/useRanking');

const mockedUseRanking = useRanking as jest.MockedFunction<typeof useRanking>;

describe('RankingModal', () => {
  const mockOnClose = jest.fn();
  const mockFetchRanking = jest.fn();

  const defaultProps = {
    isOpen: true,
    onClose: mockOnClose,
    nivelTipo: 'VERTICAL',
    metricaTipo: 'PRODUCTIVIDAD',
    filters: {
      vertical: 'Vertical 1',
      fabrica: 'Fabrica 1',
      sn1: 'SN1',
      sn2: 'SN2'
    }
  };

  const mockRankingData = {
    nivelTipo: 'VERTICAL',
    metricaTipo: 'PRODUCTIVIDAD',
    ranking: [
      {
        posicion: 1,
        nombre: 'Vertical A',
        valor: 3.456
      },
      {
        posicion: 2,
        nombre: 'Vertical B',
        valor: 2.789
      },
      {
        posicion: 3,
        nombre: 'Vertical C',
        valor: 2.123
      }
    ]
  };

  beforeEach(() => {
    jest.clearAllMocks();
    mockedUseRanking.mockReturnValue({
      data: mockRankingData,
      loading: false,
      error: null,
      fetchRanking: mockFetchRanking
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('does not render when isOpen is false', () => {
    render(<RankingModal {...defaultProps} isOpen={false} />);
    
    expect(screen.queryByText('🏆 Top 3 Ranking')).not.toBeInTheDocument();
  });

  it('renders modal when isOpen is true', () => {
    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('🏆 Top 3 Ranking')).toBeInTheDocument();
  });

  it('calls fetchRanking when modal opens', () => {
    render(<RankingModal {...defaultProps} />);
    
    expect(mockFetchRanking).toHaveBeenCalledWith({
      nivelTipo: 'VERTICAL',
      metricaTipo: 'PRODUCTIVIDAD',
      vertical: 'Vertical 1',
      fabrica: 'Fabrica 1',
      sn1: 'SN1',
      sn2: 'SN2'
    });
  });

  it('does not call fetchRanking when modal is closed', () => {
    render(<RankingModal {...defaultProps} isOpen={false} />);
    
    expect(mockFetchRanking).not.toHaveBeenCalled();
  });

  it('calls onClose when clicking outside the modal', () => {
    render(<RankingModal {...defaultProps} />);
    
    const overlay = screen.getByText('🏆 Top 3 Ranking').closest('.fixed');
    fireEvent.click(overlay!);
    
    expect(mockOnClose).toHaveBeenCalled();
  });

  it('does not call onClose when clicking inside the modal content', () => {
    render(<RankingModal {...defaultProps} />);
    
    const modalContent = screen.getByText('🏆 Top 3 Ranking').closest('.relative');
    fireEvent.click(modalContent!);
    
    expect(mockOnClose).not.toHaveBeenCalled();
  });

  it('calls onClose when clicking the X button', () => {
    render(<RankingModal {...defaultProps} />);
    
    const closeButton = screen.getByRole('button', { name: '×' });
    fireEvent.click(closeButton);
    
    expect(mockOnClose).toHaveBeenCalled();
  });

  it('calls onClose when clicking the Cerrar button', () => {
    render(<RankingModal {...defaultProps} />);
    
    const cerrarButton = screen.getByRole('button', { name: 'Cerrar' });
    fireEvent.click(cerrarButton);
    
    expect(mockOnClose).toHaveBeenCalled();
  });

  it('displays loading state', () => {
    mockedUseRanking.mockReturnValue({
      data: null,
      loading: true,
      error: null,
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} />);
    
    const spinner = screen.getByText((_content, element) => {
      return element?.classList.contains('animate-spin') || false;
    });
    expect(spinner).toBeInTheDocument();
  });

  it('displays error state', () => {
    mockedUseRanking.mockReturnValue({
      data: null,
      loading: false,
      error: new Error('Test error'),
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('Error al cargar el ranking')).toBeInTheDocument();
  });

  it('displays empty state when no ranking data', () => {
    mockedUseRanking.mockReturnValue({
      data: {
        nivelTipo: 'VERTICAL',
        metricaTipo: 'PRODUCTIVIDAD',
        ranking: []
      },
      loading: false,
      error: null,
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('No hay datos disponibles para este ranking')).toBeInTheDocument();
  });

  it('displays ranking data correctly', () => {
    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('Vertical A')).toBeInTheDocument();
    expect(screen.getByText('Vertical B')).toBeInTheDocument();
    expect(screen.getByText('Vertical C')).toBeInTheDocument();
  });

  it('displays correct medal emojis', () => {
    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('🥇')).toBeInTheDocument();
    expect(screen.getByText('🥈')).toBeInTheDocument();
    expect(screen.getByText('🥉')).toBeInTheDocument();
  });

  it('displays position numbers correctly', () => {
    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('#1')).toBeInTheDocument();
    expect(screen.getByText('#2')).toBeInTheDocument();
    expect(screen.getByText('#3')).toBeInTheDocument();
  });

  it('formats productividad values correctly', () => {
    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('3.456 features/FTE')).toBeInTheDocument();
    expect(screen.getByText('2.789 features/FTE')).toBeInTheDocument();
    expect(screen.getByText('2.123 features/FTE')).toBeInTheDocument();
  });

  it('formats LT values correctly', () => {
    mockedUseRanking.mockReturnValue({
      data: {
        nivelTipo: 'VERTICAL',
        metricaTipo: 'LT',
        ranking: [
          { posicion: 1, nombre: 'Vertical A', valor: 12.5 },
          { posicion: 2, nombre: 'Vertical B', valor: 15.3 },
          { posicion: 3, nombre: 'Vertical C', valor: 18.7 }
        ]
      },
      loading: false,
      error: null,
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} metricaTipo="LT" />);
    
    expect(screen.getByText('12.5 días')).toBeInTheDocument();
    expect(screen.getByText('15.3 días')).toBeInTheDocument();
    expect(screen.getByText('18.7 días')).toBeInTheDocument();
  });

  it('formats CT values correctly', () => {
    mockedUseRanking.mockReturnValue({
      data: {
        nivelTipo: 'VERTICAL',
        metricaTipo: 'CT',
        ranking: [
          { posicion: 1, nombre: 'Vertical A', valor: 8.2 },
          { posicion: 2, nombre: 'Vertical B', valor: 9.5 },
          { posicion: 3, nombre: 'Vertical C', valor: 11.1 }
        ]
      },
      loading: false,
      error: null,
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} metricaTipo="CT" />);
    
    expect(screen.getByText('8.2 días')).toBeInTheDocument();
    expect(screen.getByText('9.5 días')).toBeInTheDocument();
    expect(screen.getByText('11.1 días')).toBeInTheDocument();
  });

  it('handles null values correctly', () => {
    mockedUseRanking.mockReturnValue({
      data: {
        nivelTipo: 'VERTICAL',
        metricaTipo: 'PRODUCTIVIDAD',
        ranking: [
          { posicion: 1, nombre: 'Vertical A', valor: null },
          { posicion: 2, nombre: 'Vertical B', valor: 2.5 },
          { posicion: 3, nombre: 'Vertical C', valor: null }
        ]
      },
      loading: false,
      error: null,
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} />);
    
    const naElements = screen.getAllByText('N/A');
    expect(naElements).toHaveLength(2);
  });

  it('displays correct nivel label for VERTICAL', () => {
    render(<RankingModal {...defaultProps} nivelTipo="VERTICAL" />);
    
    expect(screen.getByText(/Vertical - Productividad/)).toBeInTheDocument();
  });

  it('displays correct nivel label for FABRICA', () => {
    render(<RankingModal {...defaultProps} nivelTipo="FABRICA" />);
    
    expect(screen.getByText(/Fábrica - Productividad/)).toBeInTheDocument();
  });

  it('displays correct nivel label for SN1', () => {
    render(<RankingModal {...defaultProps} nivelTipo="SN1" />);
    
    expect(screen.getByText(/Service N1 - Productividad/)).toBeInTheDocument();
  });

  it('displays correct nivel label for SN2', () => {
    render(<RankingModal {...defaultProps} nivelTipo="SN2" />);
    
    expect(screen.getByText(/Service N2 - Productividad/)).toBeInTheDocument();
  });

  it('displays correct metrica label for PRODUCTIVIDAD', () => {
    render(<RankingModal {...defaultProps} metricaTipo="PRODUCTIVIDAD" />);
    
    expect(screen.getByText(/Productividad/)).toBeInTheDocument();
  });

  it('displays correct metrica label for LT', () => {
    render(<RankingModal {...defaultProps} metricaTipo="LT" />);
    
    expect(screen.getByText(/Lead Time/)).toBeInTheDocument();
  });

  it('displays correct metrica label for CT', () => {
    render(<RankingModal {...defaultProps} metricaTipo="CT" />);
    
    expect(screen.getByText(/Cycle Time/)).toBeInTheDocument();
  });

  it('refetches data when nivelTipo changes', () => {
    const { rerender } = render(<RankingModal {...defaultProps} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(1);

    rerender(<RankingModal {...defaultProps} nivelTipo="FABRICA" />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(2);
    expect(mockFetchRanking).toHaveBeenLastCalledWith({
      nivelTipo: 'FABRICA',
      metricaTipo: 'PRODUCTIVIDAD',
      vertical: 'Vertical 1',
      fabrica: 'Fabrica 1',
      sn1: 'SN1',
      sn2: 'SN2'
    });
  });

  it('refetches data when metricaTipo changes', () => {
    const { rerender } = render(<RankingModal {...defaultProps} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(1);

    rerender(<RankingModal {...defaultProps} metricaTipo="LT" />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(2);
    expect(mockFetchRanking).toHaveBeenLastCalledWith({
      nivelTipo: 'VERTICAL',
      metricaTipo: 'LT',
      vertical: 'Vertical 1',
      fabrica: 'Fabrica 1',
      sn1: 'SN1',
      sn2: 'SN2'
    });
  });

  it('refetches data when filters change', () => {
    const { rerender } = render(<RankingModal {...defaultProps} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(1);

    rerender(<RankingModal {...defaultProps} filters={{ vertical: 'Vertical 2' }} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(2);
  });

  it('applies correct styling for first position', () => {
    render(<RankingModal {...defaultProps} />);
    
    const firstItem = screen.getByText('Vertical A').closest('div[class*="border-yellow"]');
    expect(firstItem).toBeInTheDocument();
  });

  it('applies correct styling for second position', () => {
    render(<RankingModal {...defaultProps} />);
    
    const secondItem = screen.getByText('Vertical B').closest('div[class*="border-gray"]');
    expect(secondItem).toBeInTheDocument();
  });

  it('applies correct styling for third position', () => {
    render(<RankingModal {...defaultProps} />);
    
    const thirdItem = screen.getByText('Vertical C').closest('div[class*="border-amber"]');
    expect(thirdItem).toBeInTheDocument();
  });

  it('handles filters with undefined values', () => {
    render(
      <RankingModal
        {...defaultProps}
        filters={{}}
      />
    );

    expect(mockFetchRanking).toHaveBeenCalledWith({
      nivelTipo: 'VERTICAL',
      metricaTipo: 'PRODUCTIVIDAD',
      vertical: undefined,
      fabrica: undefined,
      sn1: undefined,
      sn2: undefined
    });
  });

  it('handles filters with partial values', () => {
    render(
      <RankingModal
        {...defaultProps}
        filters={{ vertical: 'Vertical 1', fabrica: 'Fabrica 1' }}
      />
    );

    expect(mockFetchRanking).toHaveBeenCalledWith({
      nivelTipo: 'VERTICAL',
      metricaTipo: 'PRODUCTIVIDAD',
      vertical: 'Vertical 1',
      fabrica: 'Fabrica 1',
      sn1: undefined,
      sn2: undefined
    });
  });

  it('renders all ranking items when less than 3', () => {
    mockedUseRanking.mockReturnValue({
      data: {
        nivelTipo: 'VERTICAL',
        metricaTipo: 'PRODUCTIVIDAD',
        ranking: [
          { posicion: 1, nombre: 'Vertical A', valor: 3.456 },
          { posicion: 2, nombre: 'Vertical B', valor: 2.789 }
        ]
      },
      loading: false,
      error: null,
      fetchRanking: mockFetchRanking
    });

    render(<RankingModal {...defaultProps} />);
    
    expect(screen.getByText('Vertical A')).toBeInTheDocument();
    expect(screen.getByText('Vertical B')).toBeInTheDocument();
    expect(screen.queryByText('Vertical C')).not.toBeInTheDocument();
  });

  it('does not refetch when modal closes', () => {
    const { rerender } = render(<RankingModal {...defaultProps} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(1);

    rerender(<RankingModal {...defaultProps} isOpen={false} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(1);
  });

  it('refetches when modal reopens', () => {
    const { rerender } = render(<RankingModal {...defaultProps} isOpen={false} />);

    expect(mockFetchRanking).not.toHaveBeenCalled();

    rerender(<RankingModal {...defaultProps} isOpen={true} />);

    expect(mockFetchRanking).toHaveBeenCalledTimes(1);
  });
});
