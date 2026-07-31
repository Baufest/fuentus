import { render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import AppDetail from '../../../pages/Detalle/AppDetail';
import fuentusapi from '../../../api/fuentusapi';

const mockUseParams = useParams as jest.MockedFunction<typeof useParams>;
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: jest.fn(),
}));

jest.mock('../../../api/fuentusapi', () => ({
  get: jest.fn(),
}));

// Mock Icons
jest.mock('react-icons/fa', () => ({
  FaJava: () => <div data-testid="java-icon">Java Icon</div>,
}));

jest.mock('react-icons/tb', () => ({
  TbShieldCheck: () => <div data-testid="shield-icon">Shield Icon</div>,
  TbPackage: () => <div data-testid="package-icon">Package Icon</div>,
}));

// Mock Badge component
jest.mock('../../../components/Badges/Badge', () => {
  return function MockBadge({ children, variant, color }: any) {
    return (
      <div data-testid="mock-badge" data-variant={variant} data-color={color}>
        {children}
      </div>
    );
  };
});

// Mock Tooltip component
jest.mock('../../../components/Helpers/Tooltip', () => ({
  Tooltip: function MockTooltip({ children, content }: any) {
    return (
      <div data-testid="mock-tooltip" title={content}>
        {children}
      </div>
    );
  },
}));

// Mock Button component
jest.mock('../../../components/Buttons/Button', () => {
  return function MockButton({ children, onClick }: any) {
    return (
      <button data-testid="mock-button" onClick={onClick}>
        {children}
      </button>
    );
  };
});

const mockApiGet = fuentusapi.get as jest.MockedFunction<typeof fuentusapi.get>;

const renderWithRouter = (component: React.ReactElement) => render(<BrowserRouter>{component}</BrowserRouter>);

describe('AppDetail', () => {
  const mockAppData = {
    id: 1,
    name: 'Test Application',
    uuaa: 'TEST',
    coverage: 85,
    bugs: 3,
    bitbucketUrl: 'https://bitbucket.com/test',
    sonarUrl: 'https://sonar.com/test',
    sonar10Url: 'https://sonar10.com/test',
    chimeraUrl: 'https://chimera.com/sast/test',
    samuelUrl: 'https://samuel.com/test',
    monolith: false,
    language: 'java' as any,
    chimeraSast: { totalHigh: 2, totalMedium: 3, totalLow: 1 },
    chimeraSca: { totalCritical: 0, totalHigh: 1, totalMedium: 2, totalLow: 3 },
    servers: [
      { serverName: 'test-server', serverUrl: 'https://test-server.com' }
    ]
  };

  beforeEach(() => {
    jest.clearAllMocks();
    mockUseParams.mockReturnValue({ appId: '123' } as never);
    mockApiGet.mockResolvedValue({ data: mockAppData });
  });

  test('renders server information when provided', async () => {
    renderWithRouter(<AppDetail />);

    await waitFor(() => expect(screen.getByText('FUENTUS')).toBeInTheDocument());
    expect(mockApiGet).toHaveBeenCalledWith('apps/details/123');
    expect(screen.getByText(/test-server/i)).toBeInTheDocument();
  });

  test('getCoverage function displays success badge for high coverage', async () => {
    renderWithRouter(<AppDetail />);

    const coverageBadge = await screen.findByText(/85%/i);
    expect(coverageBadge.closest('[data-testid="mock-badge"]')).toHaveAttribute('data-color', 'success');
  });

  test('renders warning badge for medium coverage', async () => {
    mockApiGet.mockResolvedValueOnce({ data: { ...mockAppData, coverage: 60 } });

    renderWithRouter(<AppDetail />);

    const coverageBadge = await screen.findByText(/60%/i);
    expect(coverageBadge.closest('[data-testid="mock-badge"]')).toHaveAttribute('data-color', 'warning');
  });

  test('renders error badge for low coverage', async () => {
    mockApiGet.mockResolvedValueOnce({ data: { ...mockAppData, coverage: 30 } });

    renderWithRouter(<AppDetail />);

    const coverageBadge = await screen.findByText(/30%/i);
    expect(coverageBadge.closest('[data-testid="mock-badge"]')).toHaveAttribute('data-color', 'error');
  });

  test('shows error message if API fails', async () => {
    mockApiGet.mockRejectedValueOnce(new Error('Network error'));

    renderWithRouter(<AppDetail />);

    await waitFor(() => expect(screen.getByText(/error al cargar/i)).toBeInTheDocument());
  });
});
