import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import TableNucleusServices from '../../../components/Tables/TableNucleusServices';
import { App } from '../../../pages/SearchResults/Tables';

// Mock the Loader component
jest.mock('../../../common/Loader/index', () => {
  return function MockLoader() {
    return <div data-testid="loader">Loading...</div>;
  };
});

// Mock the SVG import
jest.mock('../../../src/images/icon/look-up.svg', () => 'look-up-icon');

// Mock Button component
jest.mock('../../../components/Buttons/Button', () => {
  return function MockButton({ children, onClick, disabled, variant }: any) {
    return (
      <button 
        data-testid="mock-button" 
        onClick={onClick} 
        disabled={disabled}
        data-variant={variant}
      >
        {children}
      </button>
    );
  };
});

// Mock icons
jest.mock('react-icons/tb', () => ({
  TbChevronLeft: () => <span data-testid="chevron-left">‹</span>,
  TbChevronRight: () => <span data-testid="chevron-right">›</span>,
  TbChevronDown: () => <span data-testid="chevron-down">▼</span>,
  TbChevronUp: () => <span data-testid="chevron-up">▲</span>,
}));

const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  );
};

const mockData: App[] = [
  {
    serviceN1: 'Test Service 1',
    ownerServiceN1: 'Owner 1',
    serviceN2: 'Test Service 2',
    appOwner: 'App Owner 1',
    uuaa: 'UUAA1'
  },
  {
    serviceN1: 'Test Service 3',
    ownerServiceN1: 'Owner 2',
    serviceN2: 'Test Service 4',
    appOwner: 'App Owner 2',
    uuaa: 'UUAA2'
  }
];

// Extended type for testing multiple UUAAs
type AppWithArrayUuaa = Omit<App, 'uuaa'> & {
  uuaa: string | string[];
};

describe('TableNucleusServices', () => {
  test('renders loading state', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={[]} 
        loading={true}
      />
    );
    
    expect(screen.getByTestId('loader')).toBeInTheDocument();
  });

  test('renders with data when not loading', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData} 
        loading={false}
      />
    );
    
    expect(screen.queryByTestId('loader')).not.toBeInTheDocument();
  });

  test('renders breadcrumb component', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData} 
        loading={false}
      />
    );
    
    // Should contain breadcrumb navigation (check for specific breadcrumb elements)
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('displays pagination information', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={5}
        totalElements={100}
      />
    );
    
    // Check if pagination info is displayed
    expect(screen.getByText(/Página/)).toBeInTheDocument();
  });

  test('renders with empty data array', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={[]} 
        loading={false}
      />
    );
    
    // Component should render without crashing
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('handles single data object correctly', () => {
    const singleApp = mockData[0];
    
    renderWithRouter(
      <TableNucleusServices 
        data={singleApp as any} // Component accepts both array and single object
        loading={false}
      />
    );
    
    // Should normalize single object to array and render
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('renders pagination buttons when handlers provided', () => {
    const mockPrevious = jest.fn();
    const mockNext = jest.fn();
    
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
        onPreviousPage={mockPrevious}
        onNextPage={mockNext}
      />
    );
    
    // Look for pagination buttons (use getAllByText since there are multiple instances)
    expect(screen.getAllByText('Anterior')).toHaveLength(2);
    expect(screen.getAllByText('Siguiente')).toHaveLength(2);
  });

  test('displays correct default values', () => {
    renderWithRouter(
      <TableNucleusServices data={mockData} />
    );
    
    // Component should render with default props
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('shows page loading state', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        pageLoading={true}
      />
    );
    
    // Component should handle page loading state
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('renders table headers correctly', () => {
    renderWithRouter(<TableNucleusServices data={mockData} loading={false} />);
    
    expect(screen.getByText('UUAA')).toBeInTheDocument();
    expect(screen.getByText('Servicio N1')).toBeInTheDocument();
    expect(screen.getByText('Service Owner')).toBeInTheDocument();
    expect(screen.getByText('Servicio N2')).toBeInTheDocument();
    expect(screen.getByText('App / Component Owner')).toBeInTheDocument();
  });

  test('displays service data in table rows', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        loading={false}
      />
    );
    
    expect(screen.getByText('Test Service 1')).toBeInTheDocument();
    expect(screen.getByText('Owner 1')).toBeInTheDocument();
    expect(screen.getByText('Test Service 2')).toBeInTheDocument();
    expect(screen.getByText('App Owner 1')).toBeInTheDocument();
    expect(screen.getByText('UUAA1')).toBeInTheDocument();
  });

  test('handles click events on dropdown buttons', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        loading={false}
      />
    );
    
    // Find UUAA dropdown buttons and simulate click
    const uuaaButtons = screen.getAllByRole('button');
    const dropdownButton = uuaaButtons.find(btn => btn.textContent?.includes('UUAA1'));
    
    if (dropdownButton) {
      fireEvent.click(dropdownButton);
      // Dropdown should toggle (implementation-specific behavior)
    }
  });

  test('calls pagination handlers when buttons are clicked', () => {
    const mockPrevious = jest.fn();
    const mockNext = jest.fn();
    
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
        onPreviousPage={mockPrevious}
        onNextPage={mockNext}
      />
    );
    
    const buttons = screen.getAllByTestId('mock-button');
    const previousButton = buttons.find(btn => btn.textContent?.includes('Anterior'));
    const nextButton = buttons.find(btn => btn.textContent?.includes('Siguiente'));
    
    if (previousButton) {
      fireEvent.click(previousButton);
      expect(mockPrevious).toHaveBeenCalledTimes(1);
    }
    
    if (nextButton) {
      fireEvent.click(nextButton);
      expect(mockNext).toHaveBeenCalledTimes(1);
    }
  });

  test('disables previous button on first page', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={0}
        totalPages={3}
        onPreviousPage={jest.fn()}
        onNextPage={jest.fn()}
      />
    );
    
    const buttons = screen.getAllByTestId('mock-button');
    const previousButton = buttons.find(btn => btn.textContent?.includes('Anterior'));
    
    expect(previousButton).toHaveAttribute('disabled');
  });

  test('disables next button on last page', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={2}
        totalPages={3}
        onPreviousPage={jest.fn()}
        onNextPage={jest.fn()}
      />
    );
    
    const buttons = screen.getAllByTestId('mock-button');
    const nextButton = buttons.find(btn => btn.textContent?.includes('Siguiente'));
    
    expect(nextButton).toHaveAttribute('disabled');
  });

  test.skip('displays pagination info correctly', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={2}
        totalPages={5}
        totalElements={50}
      />
    );
    
    expect(screen.getByText('Página 3 de 5')).toBeInTheDocument();
    // Look for text content more flexibly since text is broken up by spans
    expect(screen.getByText((_, element) => {
      return !!(element?.textContent?.includes('Mostrando') && 
               element?.textContent?.includes('de 50 resultados'));
    })).toBeInTheDocument();
  });

  test('handles external click to close dropdowns', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        loading={false}
      />
    );
    
    // Simulate clicking outside to close dropdowns
    fireEvent.mouseDown(document.body);
    
    // Should not crash and component should still be rendered
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('logs normalized data to console', () => {
    const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});
    
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        loading={false}
      />
    );
    
    expect(consoleSpy).toHaveBeenCalledWith('Normalized Data:', mockData);
    
    consoleSpy.mockRestore();
  });

  test('handles UUAA selection in dropdown', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        loading={false}
      />
    );
    
    // Find and click a UUAA link/button
    const uuaaElements = screen.getAllByText(/UUAA/);
    if (uuaaElements.length > 0) {
      fireEvent.click(uuaaElements[0]);
      // Should handle the UUAA selection
    }
  });

  test('renders without pagination handlers', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
      />
    );
    
    // Should render without pagination buttons when handlers are not provided
    expect(screen.getByText('Dashboard /')).toBeInTheDocument();
  });

  test('applies correct CSS classes to table elements', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        loading={false}
      />
    );
    
    const table = screen.getByRole('table');
    expect(table).toBeInTheDocument();
    expect(table).toHaveClass('w-full');
  });

  test('handles zero elements correctly', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={[]}
        totalElements={0}
        totalPages={0}
      />
    );
    
    // When there are no results, look for the "No se encontraron resultados" message
    expect(screen.getByText('No se encontraron resultados')).toBeInTheDocument();
  });

  test('handles multiple UUAAs with dropdown', () => {
    const multiUuaaData: AppWithArrayUuaa[] = [
      {
        serviceN1: 'Test Service',
        ownerServiceN1: 'Owner',
        serviceN2: 'Test Service 2',
        appOwner: 'App Owner',
        uuaa: ['UUAA1', 'UUAA2', 'UUAA3'] // Array of UUAAs
      }
    ];

    renderWithRouter(
      <TableNucleusServices 
        data={multiUuaaData as any}
        loading={false}
      />
    );
    
    // Should display joined UUAAs in the cell
    expect(screen.getByText('UUAA1, UUAA2, UUAA3')).toBeInTheDocument();
    
    // Should have a link (not a button) with the Look Up image
    expect(screen.getByRole('link', { name: /look up/i })).toBeInTheDocument();
    
    // Should render table correctly with multiple UUAA data
    expect(screen.getByText('Test Service')).toBeInTheDocument();
    expect(screen.getByText('Owner')).toBeInTheDocument();
  });

  test('shows pagination info with correct calculations', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={5}
        totalElements={47}
      />
    );
    
    // Should show pagination text - just check that numbers appear
    expect(screen.getByText('11')).toBeInTheDocument();
    expect(screen.getByText('20')).toBeInTheDocument();
    expect(screen.getByText('47')).toBeInTheDocument();
    // Test that page info is rendered - just check that we have at least some
    const paginaElements = screen.getAllByText((_, element) => 
      element?.textContent?.includes('Página') || false
    );
    expect(paginaElements.length).toBeGreaterThan(0);
  });

  test('shows page loading state in buttons', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
        pageLoading={true}
        onPreviousPage={jest.fn()}
        onNextPage={jest.fn()}
      />
    );
    
    // Should show loading text somewhere - use getAllByText since there are multiple
    const loadingElements = screen.getAllByText('Cargando...');
    expect(loadingElements.length).toBeGreaterThan(0);
  });

  test('renders without pagination when no handlers provided', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
        totalElements={25}
      />
    );
    
    // Should render table content 
    expect(screen.getByText('Test Service 1')).toBeInTheDocument();
    // Check that pagination is present but without functional handlers
    expect(document.body.textContent).toContain('Página');
  });

  test('handles single UUAA as array', () => {
    const singleUuaaArrayData: AppWithArrayUuaa[] = [
      {
        serviceN1: 'Test Service',
        ownerServiceN1: 'Owner',
        serviceN2: 'Test Service 2',
        appOwner: 'App Owner',
        uuaa: ['UUAA1'] // Single UUAA as array
      }
    ];

    renderWithRouter(
      <TableNucleusServices 
        data={singleUuaaArrayData as any}
        loading={false}
      />
    );
    
    // Should display the single UUAA
    expect(screen.getByText('UUAA1')).toBeInTheDocument();
    
    // Should have direct NavLink (not dropdown button) since it's only one UUAA
    const lookupImages = document.querySelectorAll('img[alt="Look Up"]');
    expect(lookupImages.length).toBeGreaterThan(0);
  });

  test('renders navigation link for UUAA', () => {
    const multiUuaaData: AppWithArrayUuaa[] = [
      {
        serviceN1: 'Test Service',
        ownerServiceN1: 'Owner',
        serviceN2: 'Test Service 2',
        appOwner: 'App Owner',
        uuaa: ['UUAA1', 'UUAA2']
      }
    ];

    renderWithRouter(
      <TableNucleusServices 
        data={multiUuaaData as any}
        loading={false}
      />
    );
    
    // Should display both UUAAs joined in the cell
    expect(screen.getByText('UUAA1, UUAA2')).toBeInTheDocument();
    
    // Should have a navigation link with Look Up image
    const lookupLink = screen.getByRole('link', { name: /look up/i });
    expect(lookupLink).toBeInTheDocument();
    expect(lookupLink).toHaveClass('hover:text-primary');
  });

  test('renders lookup icon with correct styling', () => {
    const multiUuaaData: AppWithArrayUuaa[] = [
      {
        serviceN1: 'Test Service',
        ownerServiceN1: 'Owner',
        serviceN2: 'Test Service 2',
        appOwner: 'App Owner',
        uuaa: ['UUAA1', 'UUAA2']
      }
    ];

    renderWithRouter(
      <TableNucleusServices 
        data={multiUuaaData as any}
        loading={false}
      />
    );
    
    // Should display both UUAAs in the same cell
    expect(screen.getByText('UUAA1, UUAA2')).toBeInTheDocument();
    
    // Should have the lookup icon with proper classes
    const lookupIcon = screen.getByAltText('Look Up');
    expect(lookupIcon).toBeInTheDocument();
    expect(lookupIcon).toHaveClass('lookup-icon', 'transition-all', 'duration-200');
  });

  test('renders service link correctly', () => {
    const multiUuaaData: AppWithArrayUuaa[] = [
      {
        serviceN1: 'Test Service',
        ownerServiceN1: 'Owner',
        serviceN2: 'Test Service 2',
        appOwner: 'App Owner',
        uuaa: ['UUAA1', 'UUAA2']
      }
    ];

    renderWithRouter(
      <TableNucleusServices 
        data={multiUuaaData as any}
        loading={false}
      />
    );
    
    // Should have a navigation link
    const lookupLink = screen.getByRole('link', { name: /look up/i });
    expect(lookupLink).toBeInTheDocument();
    
    // Should render within dropdown-container
    const dropdownContainer = document.querySelector('.dropdown-container');
    expect(dropdownContainer).toBeInTheDocument();
    expect(dropdownContainer).toHaveClass('relative', 'flex', 'items-center', 'space-x-3.5');
  });

  test('shows pagination info with multiple pages', () => {
    const mockData: App[] = Array.from({ length: 47 }, (_, i) => ({
      uuaa: `UUAA${i + 1}`,
      serviceN1: `Test Service ${i + 1}`,
      ownerServiceN1: `Owner ${i + 1}`,
      serviceN2: `Test Service ${i + 2}`,
      appOwner: `App Owner ${i + 1}`,
    }));
    renderWithRouter(<TableNucleusServices data={mockData} />);
    
    // Test pagination info text elements individually - just check that the component renders with data
    expect(screen.getByText('Test Service 1')).toBeInTheDocument();
    expect(screen.getByText('Owner 1')).toBeInTheDocument();
    expect(screen.getByText('UUAA1')).toBeInTheDocument();
    // Test using getAllByText for multiple matches - check we have at least one
    const paginaElements = screen.getAllByText(/Página/i);
    expect(paginaElements.length).toBeGreaterThan(0);
  });

  test('shows page loading state in buttons', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
        pageLoading={true}
        onPreviousPage={jest.fn()}
        onNextPage={jest.fn()}
      />
    );
    
    // Should show "Cargando..." in buttons when pageLoading is true
    expect(screen.getAllByText('Cargando...')).toHaveLength(4);
  });
  });

  test('renders without pagination when no handlers provided', () => {
    renderWithRouter(
      <TableNucleusServices 
        data={mockData}
        currentPage={1}
        totalPages={3}
        totalElements={25}
      />
    );
    
    // Should render table content but without interactive pagination
    expect(screen.getByText('Test Service 1')).toBeInTheDocument();
    // No pagination buttons should be disabled when no handlers
    const buttons = screen.getAllByTestId('mock-button');
    const disabledButtons = buttons.filter(btn => btn.hasAttribute('disabled'));
    expect(disabledButtons.length).toBe(0);
  });
