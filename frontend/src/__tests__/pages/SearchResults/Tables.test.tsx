import { render, screen, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Tables from '../../../pages/SearchResults/Tables';
import fuentusapi from '../../../api/fuentusapi';

// Mock del API
jest.mock('../../../api/fuentusapi', () => ({
  get: jest.fn()
}));

// Mock del componente TableNucleusServices
jest.mock('../../../components/Tables/TableNucleusServices', () => {
  return function MockTableNucleusServices({ data, loading }: any) {
    if (loading) {
      return <div data-testid="loading">Loading...</div>;
    }
    return (
      <div data-testid="table-nucleus-services">
        <div>Applications found: {data.length}</div>
        {data.map((app: any, index: number) => (
          <div key={index} data-testid={`app-${index}`}>
            {app.serviceN1} - {app.uuaa}
          </div>
        ))}
      </div>
    );
  };
});

// Mock de useLocation
const mockUseLocation = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => mockUseLocation()
}));

const mockApiResponse = {
  data: {
    content: [
      {
        serviceN1: 'Test Service 1',
        ownerServiceN1: 'Owner 1',
        serviceN2: 'Test Service 2',
        appOwner: 'App Owner 1',
        uuaa: 'TEST001'
      },
      {
        serviceN1: 'Test Service 2',
        ownerServiceN1: 'Owner 2',
        serviceN2: 'Test Service 3',
        appOwner: 'App Owner 2',
        uuaa: 'TEST002'
      }
    ],
    totalElements: 2,
    totalPages: 1,
    number: 0,
    size: 20,
    first: true,
    last: true,
    numberOfElements: 2
  }
};

describe('Tables - SearchResults', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockUseLocation.mockReturnValue({
      state: { searchValue: 'test search' }
    });
  });

  const renderWithRouter = (component: React.ReactElement) => {
    return render(
      <BrowserRouter>
        {component}
      </BrowserRouter>
    );
  };

  test('renders loading state initially', () => {
    (fuentusapi.get as jest.Mock).mockResolvedValue(mockApiResponse);
    
    renderWithRouter(<Tables />);
    
    expect(screen.getByTestId('loading')).toBeInTheDocument();
  });

  test('renders table with search results', async () => {
    (fuentusapi.get as jest.Mock).mockResolvedValue(mockApiResponse);
    
    renderWithRouter(<Tables />);
    
    await waitFor(() => {
      expect(screen.getByTestId('table-nucleus-services')).toBeInTheDocument();
    });
    
    expect(screen.getByText('Applications found: 2')).toBeInTheDocument();
    expect(screen.getByTestId('app-0')).toBeInTheDocument();
    expect(screen.getByTestId('app-1')).toBeInTheDocument();
  });

  test('calls API with correct search term', async () => {
    (fuentusapi.get as jest.Mock).mockResolvedValue(mockApiResponse);
    
    renderWithRouter(<Tables />);
    
    await waitFor(() => {
      expect(fuentusapi.get).toHaveBeenCalledWith('nucleus/search?page=0&q=test+search');
    });
  });

  test('handles API error gracefully', async () => {
    (fuentusapi.get as jest.Mock).mockRejectedValue(new Error('API Error'));
    
    renderWithRouter(<Tables />);
    
    await waitFor(() => {
      expect(screen.getByTestId('table-nucleus-services')).toBeInTheDocument();
    });
    
    // Should still render table even on error (with empty data)
    expect(screen.getByText('Applications found: 0')).toBeInTheDocument();
  });

  test('handles empty search results', async () => {
    const emptyResponse = {
      data: {
        ...mockApiResponse.data,
        content: [],
        totalElements: 0,
        numberOfElements: 0
      }
    };
    
    (fuentusapi.get as jest.Mock).mockResolvedValue(emptyResponse);
    
    renderWithRouter(<Tables />);
    
    await waitFor(() => {
      expect(screen.getByTestId('table-nucleus-services')).toBeInTheDocument();
    });
    
    expect(screen.getByText('Applications found: 0')).toBeInTheDocument();
  });

  test('handles filters instead of search term', async () => {
    mockUseLocation.mockReturnValue({
      state: { 
        searchValue: null,
        area: 'ARGENTINA',
        orgn1: 'SYSTEMS',
        orgn2: 'PAYMENTS'
      }
    });

    (fuentusapi.get as jest.Mock).mockResolvedValue(mockApiResponse);
    
    renderWithRouter(<Tables />);
    
    await waitFor(() => {
      expect(fuentusapi.get).toHaveBeenCalledWith(
        expect.stringContaining('nucleus/filters?area=ARGENTINA&orgN1=SYSTEMS&orgN2fabrica=PAYMENTS&page=0')
      );
    });
  });
});
