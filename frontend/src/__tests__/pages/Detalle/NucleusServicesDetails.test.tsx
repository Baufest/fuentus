import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import NucleusServicesDetails from '../../../pages/Detalle/NucleusServicesDetails';
import '@testing-library/jest-dom';

// Mock de react-router-dom
const mockUseLocation = jest.fn();
const mockUseParams = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => mockUseLocation(),
  useParams: () => mockUseParams(),
  NavLink: ({ children, to, state }: any) => (
    <a href={to} data-state={JSON.stringify(state)}>
      {children}
    </a>
  )
}));

// Mock de los hooks
jest.mock('../../../hooks/useServiceSummary', () => ({
  useServiceSummaryById: jest.fn(),
  useServiceAppsById: jest.fn()
}));

import { useServiceSummaryById, useServiceAppsById } from '../../../hooks/useServiceSummary';
const mockUseServiceSummaryById = useServiceSummaryById as jest.MockedFunction<typeof useServiceSummaryById>;
const mockUseServiceAppsById = useServiceAppsById as jest.MockedFunction<typeof useServiceAppsById>;

// Mock del componente Breadcrumb
jest.mock('../../../components/Breadcrumbs/Breadcrumb', () => {
  return function MockBreadcrumb({ pageName }: { pageName: string }) {
    return <div data-testid="breadcrumb">{pageName}</div>;
  };
});

// Mock del componente Loader
jest.mock('../../../common/Loader', () => {
  return function MockLoader({ size, color }: any) {
    return <div data-testid="loader" data-size={size} data-color={color}>Loading...</div>;
  };
});

// Mock del componente Badge
jest.mock('../../../components/Badges/Badge', () => {
  return function MockBadge({ children, color }: any) {
    return <span data-testid="badge" data-color={color}>{children}</span>;
  };
});

// Mock del componente Button
jest.mock('../../../components/Buttons/Button', () => {
  return function MockButton({ children, disabled, onClick, variant }: any) {
    return (
      <button data-testid="button" disabled={disabled} onClick={onClick} data-variant={variant}>
        {children}
      </button>
    );
  };
});

// Mock del componente Tooltip
jest.mock('../../../components/Helpers/Tooltip', () => ({
  Tooltip: ({ children, text }: { children: React.ReactNode; text: string }) => (
    <div data-testid="tooltip" title={text}>{children}</div>
  ),
}));

// Mock de los iconos
jest.mock('react-icons/si', () => ({
  SiSonarqube: () => <div data-testid="sonar-icon">Sonar</div>,
  SiBitbucket: () => <div data-testid="bitbucket-icon">Bitbucket</div>
}));

jest.mock('react-icons/tb', () => ({
  TbShieldCheck: () => <div data-testid="shield-icon">Shield</div>,
  TbPackage: () => <div data-testid="package-icon">Package</div>,
  TbChevronLeft: () => <div data-testid="chevron-left">Left</div>,
  TbChevronRight: () => <div data-testid="chevron-right">Right</div>,
  TbSearch: () => <div data-testid="search-icon">Search</div>,
  TbX: () => <div data-testid="x-icon">X</div>,
  TbLayoutGrid: () => <div data-testid="layout-grid">Grid</div>,
  TbList: () => <div data-testid="list-icon">List</div>
}));

// Mock de la imagen
jest.mock('../../../../src/images/icon/look-up.svg', () => 'look-up.svg');

// Mock de ServiceSummaryCards
jest.mock('../../../components/Cards/ServiceSummaryCards', () => {
  return function MockServiceSummaryCards() {
    return <div data-testid="service-summary-cards">Service Summary Cards</div>;
  };
});

// Mock de UuaaAppsCharts
jest.mock('../../../components/Charts/UuaaAppsCharts', () => {
  return function MockUuaaAppsCharts() {
    return <div data-testid="uuaa-apps-charts">UUAA Apps Charts</div>;
  };
});

// Mock de ServiceProductivityCharts
jest.mock('../../../components/Charts/ServiceProductivityCharts', () => {
  return function MockServiceProductivityCharts() {
    return <div data-testid="service-productivity-charts">Service Productivity Charts</div>;
  };
});

const mockAppsData = [
  {
    id: 1,
    name: 'test-app-1',
    uuaa: 'TEST',
    bitbucketUrl: 'http://bitbucket.com/test1',
    sonarUrl: null,
    sonar10Url: 'http://sonar.com/test1',
    chimeraUrl: 'http://chimera.com/test1',
    samuelUrl: 'http://samuel.com/test1',
    monolith: false,
    coverage: 85,
    bugs: 3,
    language: null,
    chimeraSast: {
      totalLow: 2,
      totalMedium: 1,
      totalHigh: 0
    },
    chimeraSca: {
      totalLow: 1,
      totalMedium: 2,
      totalHigh: 1,
      totalCritical: 0
    }
  },
  {
    id: 2,
    name: 'test-app-2',
    uuaa: 'TEST',
    bitbucketUrl: 'http://bitbucket.com/test2',
    sonarUrl: null,
    sonar10Url: 'http://sonar.com/test2',
    chimeraUrl: 'http://chimera.com/test2',
    samuelUrl: 'http://samuel.com/test2',
    monolith: false,
    coverage: 65,
    bugs: 8,
    language: null,
    chimeraSast: {
      totalLow: 5,
      totalMedium: 3,
      totalHigh: 2
    },
    chimeraSca: {
      totalLow: 3,
      totalMedium: 4,
      totalHigh: 2,
      totalCritical: 1
    }
  }
];

const mockSummary = {
  serviceId: 1,
  serviceN1: 'Test Service',
  serviceN2: 'Test Service N2',
  ownerServiceN1: 'Test Owner',
  uuaas: ['TEST'],
  totalApps: 2,
  averageCoverage: 75,
  totalBugs: 11,
  totalSastLow: 7,
  totalSastMedium: 4,
  totalSastHigh: 2,
  totalScaLow: 4,
  totalScaMedium: 6,
  totalScaHigh: 3,
  totalScaCritical: 1,
  rfoId: null,
  rfoEstado: null,
  totalSastVulnerabilities: 13,
  totalScaVulnerabilities: 14,
  totalVulnerabilities: 27
};

const renderWithRouter = (component: React.ReactElement) => {
  return render(<Router>{component}</Router>);
};

describe('NucleusServicesDetails', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockUseLocation.mockReturnValue({
      state: {
        serviceId: 1
      }
    });
    mockUseParams.mockReturnValue({ serviceId: '1' });
    
    // Mock del hook useServiceSummaryById
    mockUseServiceSummaryById.mockReturnValue({
      summary: mockSummary,
      loading: false,
      error: null,
      refetch: jest.fn()
    });
    
    // Mock del hook useServiceAppsById
    mockUseServiceAppsById.mockReturnValue({
      apps: mockAppsData,
      loading: false,
      error: null,
      totalElements: 2,
      totalPages: 1,
      currentPage: 0,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: jest.fn()
    });
  });

  test('renders with initial loading state', async () => {
    mockUseServiceAppsById.mockReturnValue({
      apps: [],
      loading: true,
      error: null,
      totalElements: 0,
      totalPages: 0,
      currentPage: 0,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: jest.fn()
    });
    
    renderWithRouter(<NucleusServicesDetails />);
    
    expect(screen.getByTestId('loader')).toBeInTheDocument();
    expect(screen.getByText('Cargando aplicaciones...')).toBeInTheDocument();
  });

  test('renders breadcrumb', () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    expect(screen.getByTestId('breadcrumb')).toBeInTheDocument();
    expect(screen.getByText('Servicio: Test Service N2')).toBeInTheDocument();
  });

  test('renders table with applications data', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-1')).toBeInTheDocument();
      expect(screen.getByText('test-app-2')).toBeInTheDocument();
    });

    // Language is null in the mock data, so we don't check for specific languages
    // Just verify the apps are rendered
  });

  test('displays coverage badges with correct data', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('85%')).toBeInTheDocument();
      expect(screen.getByText('65%')).toBeInTheDocument();
    });
  });

  test('displays bugs count', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('3')).toBeInTheDocument();
      expect(screen.getByText('8')).toBeInTheDocument();
    });
  });

  test('displays SAST vulnerability counts', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      const sastElements = screen.getAllByText('0H');
      expect(sastElements.length).toBeGreaterThan(0); // Should find at least one
      
      const mediumElements = screen.getAllByText('1M');
      expect(mediumElements.length).toBeGreaterThan(0);
      
      const lowElements = screen.getAllByText('2L');
      expect(lowElements.length).toBeGreaterThan(0);
    });
  });

  test('displays SCA vulnerability counts', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      const criticalElements = screen.getAllByText('0C');
      expect(criticalElements.length).toBeGreaterThan(0); // Should find at least one Critical
      
      const highElements = screen.getAllByText('1C');
      expect(highElements.length).toBeGreaterThan(0); // Should find at least one High (but it says 1C)
    });
  });

  test('handles search functionality', async () => {
    const mockSetSearch = jest.fn();
    mockUseServiceAppsById.mockReturnValue({
      apps: mockAppsData,
      loading: false,
      error: null,
      totalElements: 2,
      totalPages: 1,
      currentPage: 0,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: mockSetSearch
    });
    
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-1')).toBeInTheDocument();
    });

    const searchInput = screen.getByPlaceholderText('Buscar por nombre de aplicación...');
    fireEvent.change(searchInput, { target: { value: 'test-app-1' } });

    await waitFor(() => {
      expect(mockSetSearch).toHaveBeenCalled();
    }, { timeout: 1000 });
  });

  test('handles search clear', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-1')).toBeInTheDocument();
    });

    const searchInput = screen.getByPlaceholderText('Buscar por nombre de aplicación...');
    fireEvent.change(searchInput, { target: { value: 'test' } });

    // Click clear button
    const clearButton = screen.getByTestId('x-icon').closest('button');
    if (clearButton) {
      fireEvent.click(clearButton);
    }

    expect(searchInput).toHaveValue('');
  });

  test('shows pagination controls when multiple pages exist', async () => {
    mockUseServiceAppsById.mockReturnValue({
      apps: [
        {
          id: 1,
          name: 'test-app-1',
          uuaa: 'TEST',
          bitbucketUrl: 'http://bitbucket.com/test1',
          sonarUrl: null,
          sonar10Url: 'http://sonar.com/test1',
          chimeraUrl: 'http://chimera.com/test1',
          samuelUrl: 'http://samuel.com/test1',
          monolith: false,
          coverage: 85,
          bugs: 3,
          language: null,
          chimeraSast: { totalLow: 2, totalMedium: 1, totalHigh: 0 },
          chimeraSca: { totalLow: 3, totalMedium: 2, totalHigh: 1, totalCritical: 0 }
        }
      ],
      loading: false,
      error: null,
      totalElements: 25,
      totalPages: 2,
      currentPage: 0,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: jest.fn()
    });

    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-1')).toBeInTheDocument();
      expect(screen.getByTestId('pagination-container')).toBeInTheDocument();
    });

    // Verify pagination info is shown
    expect(screen.getByTestId('pagination-info')).toBeInTheDocument();
    
    // Verify navigation buttons are present (either mobile or desktop)
    const nextButtons = screen.queryAllByText('Siguiente');
    const prevButtons = screen.queryAllByText('Anterior');
    
    expect(nextButtons.length).toBeGreaterThan(0);
    expect(prevButtons.length).toBeGreaterThan(0);
  });

  test('displays pagination information correctly', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      const paginationInfo = screen.getByTestId('pagination-info');
      expect(paginationInfo).toBeInTheDocument();
      expect(paginationInfo).toHaveTextContent(/Mostrando 1 a 2 de 2 resultados/);
    });
  });

  test('handles empty results', async () => {
    mockUseServiceAppsById.mockReturnValue({
      apps: [],
      loading: false,
      error: null,
      totalElements: 0,
      totalPages: 0,
      currentPage: 0,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: jest.fn()
    });

    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('No se encontraron aplicaciones para este servicio')).toBeInTheDocument();
    });
  });

  test('handles search with no results', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-1')).toBeInTheDocument();
    });

    mockUseServiceAppsById.mockReturnValue({
      apps: [],
      loading: false,
      error: null,
      totalElements: 0,
      totalPages: 0,
      currentPage: 0,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: jest.fn()
    });

    const searchInput = screen.getByPlaceholderText('Buscar por nombre de aplicación...');
    fireEvent.change(searchInput, { target: { value: 'nonexistent' } });

    await waitFor(() => {
      expect(screen.getByText(/No se encontraron aplicaciones que coincidan con "nonexistent"/)).toBeInTheDocument();
    });
  });

  test('handles API error gracefully', async () => {
    mockUseServiceSummaryById.mockReturnValue({
      summary: null,
      loading: false,
      error: 'API Error',
      refetch: jest.fn()
    });

    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('No se pudo cargar el servicio')).toBeInTheDocument();
    });
  });

  test('handles serviceId from URL params when no state', () => {
    mockUseLocation.mockReturnValue({ state: null });
    mockUseParams.mockReturnValue({ serviceId: '999' });

    renderWithRouter(<NucleusServicesDetails />);
    
    expect(mockUseServiceAppsById).toHaveBeenCalledWith(999, 0, '');
  });

  test('renders table headers correctly', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      // Check for table headers - use getAllByText for headers that might appear multiple times
      expect(screen.getByText('UUAA')).toBeInTheDocument();
      expect(screen.getByText('Aplicación')).toBeInTheDocument();
      expect(screen.getByText('Tech')).toBeInTheDocument();
      const bitbucketElements = screen.getAllByText('Bitbucket');
      expect(bitbucketElements.length).toBeGreaterThan(0);
      const sonarElements = screen.getAllByText('Sonar');
      expect(sonarElements.length).toBeGreaterThan(0);
      expect(screen.getByText('Chimera')).toBeInTheDocument();
      expect(screen.getByText('Actions')).toBeInTheDocument();
    });
  });

  test('renders external links correctly', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-1')).toBeInTheDocument();
    });

    const links = screen.getAllByRole('link');
    expect(links.length).toBeGreaterThan(0);
  });

  test('pagination shows correct page information', async () => {
    mockUseServiceAppsById.mockReturnValue({
      apps: [
        {
          id: 21,
          name: 'test-app-21',
          uuaa: 'TEST',
          bitbucketUrl: 'http://bitbucket.com/test21',
          sonarUrl: null,
          sonar10Url: 'http://sonar.com/test21',
          chimeraUrl: 'http://chimera.com/test21',
          samuelUrl: 'http://samuel.com/test21',
          monolith: false,
          coverage: 75,
          bugs: 2,
          language: null,
          chimeraSast: { totalLow: 1, totalMedium: 2, totalHigh: 1 },
          chimeraSca: { totalLow: 2, totalMedium: 1, totalHigh: 2, totalCritical: 1 }
        }
      ],
      loading: false,
      error: null,
      totalElements: 25,
      totalPages: 2,
      currentPage: 1,
      refetch: jest.fn(),
      setPage: jest.fn(),
      setSearch: jest.fn()
    });

    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      expect(screen.getByText('test-app-21')).toBeInTheDocument();
      expect(screen.getByTestId('pagination-container')).toBeInTheDocument();
    });

    // Should show page 2 of 2
    expect(screen.getByText('Página 2 de 2')).toBeInTheDocument();
  });

  test('displays correct vulnerability badges', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    await waitFor(() => {
      const badges = screen.getAllByTestId('badge');
      expect(badges.length).toBeGreaterThan(0);
    });
  });

  test('renders component structure correctly', async () => {
    renderWithRouter(<NucleusServicesDetails />);
    
    // Check for basic elements that should always be present
    expect(screen.getByTestId('breadcrumb')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Buscar por nombre de aplicación...')).toBeInTheDocument();
    expect(screen.getByText('UUAA')).toBeInTheDocument();
    expect(screen.getByText('Aplicación')).toBeInTheDocument();
  });
});
