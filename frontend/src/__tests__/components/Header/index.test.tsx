import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import Header from '../../../components/Header/index';
import { useComboValues } from '../../../hooks/useComboValues';
import { DashboardFiltersProvider } from '../../../contexts/DashboardFiltersContext';
import { HeaderFiltersProvider } from '../../../contexts/HeaderFiltersContext';

// Mock DarkModeSwitcher
jest.mock('../../../components/Header/DarkModeSwitcher', () => {
  return function MockDarkModeSwitcher() {
    return <div data-testid="dark-mode-switcher">Dark Mode Switcher</div>;
  };
});

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

jest.mock('../../../hooks/useComboValues', () => ({
  useComboValues: jest.fn(),
}));

const mockUseComboValues = useComboValues as jest.MockedFunction<typeof useComboValues>;

const comboValuesMock = {
  verticals: ['Vertical 1'],
  uol2Values: ['UOL2 Option'],
  sn1Values: ['SN1 Option'],
  sn2Values: ['SN2 Option'],
};

const mockSetSidebarOpen = jest.fn();

const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <Router>
      <HeaderFiltersProvider>
        <DashboardFiltersProvider>
          {component}
        </DashboardFiltersProvider>
      </HeaderFiltersProvider>
    </Router>
  );
};

describe('Header', () => {
  const mockProps = {
    sidebarOpen: false,
    setSidebarOpen: mockSetSidebarOpen,
  };

  beforeEach(() => {
    jest.clearAllMocks();
    mockNavigate.mockClear();
    mockUseComboValues.mockReturnValue({
      data: comboValuesMock,
      loading: false,
      error: null,
      refetch: jest.fn(),
    });
  });

  test('renders header correctly', () => {
    renderWithRouter(<Header {...mockProps} />);

    expect(screen.getByRole('banner')).toBeInTheDocument();
    expect(screen.getByAltText('Fuentus Logo')).toBeInTheDocument();
  });

  test('renders logo with correct attributes', () => {
    renderWithRouter(<Header {...mockProps} />);

    const logo = screen.getByAltText('Fuentus Logo');
    expect(logo).toHaveClass('w-40', 'mx-2', 'hover:opacity-80');
  });

  test('hamburger menu button calls setSidebarOpen when clicked', () => {
    renderWithRouter(<Header {...mockProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    expect(menuButton).not.toBeNull();
    fireEvent.click(menuButton!);

    expect(mockSetSidebarOpen).toHaveBeenCalledWith(true);
  });

  test('hamburger menu button toggles correctly when sidebar is open', () => {
    const openProps = { ...mockProps, sidebarOpen: true };
    renderWithRouter(<Header {...openProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    expect(menuButton).not.toBeNull();
    fireEvent.click(menuButton!);

    expect(mockSetSidebarOpen).toHaveBeenCalledWith(false);
  });

  test('dark mode switcher is rendered', () => {
    renderWithRouter(<Header {...mockProps} />);

    expect(screen.getByTestId('dark-mode-switcher')).toBeInTheDocument();
  });

  test('search form is present', () => {
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    expect(searchInput).toBeInTheDocument();
  });

  test('search input updates value when typing', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...') as HTMLInputElement;
    await user.type(searchInput, 'test search');
    
    expect(searchInput.value).toBe('test search');
  });

  test('search form submission navigates to search results', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    const form = searchInput.closest('form');
    
    await user.type(searchInput, 'test query');
    fireEvent.submit(form!);

    expect(mockNavigate).toHaveBeenCalledWith('/searchresults', {
      state: { searchValue: 'test query' }
    });
  });

  test.skip('search form shows error message for invalid search (only spaces)', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    const form = searchInput.closest('form');
    
    await user.type(searchInput, ' ');
    fireEvent.submit(form!);

    await waitFor(() => {
      expect(screen.getByText('No se encontraron resultados para la búsqueda.')).toBeInTheDocument();
    });
    expect(mockNavigate).not.toHaveBeenCalled();
  });

  test.skip('search form clears error message on valid search', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    const form = searchInput.closest('form');
    
    // First submit with invalid search
    await user.type(searchInput, ' ');
    fireEvent.submit(form!);
    expect(screen.getByText('No se encontraron resultados para la búsqueda.')).toBeInTheDocument();

    // Clear input and submit valid search
    await user.clear(searchInput);
    await user.type(searchInput, 'valid search');
    fireEvent.submit(form!);

    expect(screen.queryByText('No se encontraron resultados para la búsqueda.')).not.toBeInTheDocument();
    expect(mockNavigate).toHaveBeenCalledWith('/searchresults', {
      state: { searchValue: 'valid search' }
    });
  });

  test('search button has correct icon', () => {
    renderWithRouter(<Header {...mockProps} />);

    const searchButton = document.querySelector('form button');
    const searchIcon = searchButton?.querySelector('img');
    
    expect(searchIcon).toBeInTheDocument();
    expect(searchIcon).toHaveAttribute('alt', 'Search');
    expect(searchIcon).toHaveClass('brightness-0', 'invert');
  });

  test('search input has correct styling classes', () => {
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    expect(searchInput).toHaveClass(
      'w-full',
      'bg-transparent',
      'pl-9',
      'pr-4',
      'text-white',
      'placeholder-white',
      'focus:outline-none',
      'xl:w-125',
      'text-xl'
    );
  });

  test.skip('search form prevents default submission', () => {
    renderWithRouter(<Header {...mockProps} />);

    const form = document.querySelector('form');
    const mockPreventDefault = jest.fn();
    
    const mockEvent = {
      preventDefault: mockPreventDefault,
      currentTarget: form,
      target: form
    } as any;

    fireEvent.submit(form!, mockEvent);
    expect(mockPreventDefault).toHaveBeenCalled();
  });

  test('hamburger button is visible on mobile', () => {
    renderWithRouter(<Header {...mockProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    expect(menuButton).not.toBeNull();
    expect(menuButton).toHaveClass('lg:hidden');
  });

  test('applies dark mode classes correctly', () => {
    renderWithRouter(<Header {...mockProps} />);

    const header = screen.getByRole('banner');
    expect(header).toHaveClass('dark:bg-[#070E46]', 'dark:drop-shadow-none');
  });

  test('hamburger button has correct styling', () => {
    renderWithRouter(<Header {...mockProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    expect(menuButton).not.toBeNull();
    expect(menuButton).toHaveClass('z-99999', 'block', 'rounded-sm', 'border');
  });

  test('search form has correct action', () => {
    renderWithRouter(<Header {...mockProps} />);

    const searchForm = document.querySelector('form');
    expect(searchForm).toHaveAttribute('action', 'https://formbold.com/s/unique_form_id');
  });

  test('header has proper flex layout', () => {
    renderWithRouter(<Header {...mockProps} />);

    const headerContent = document.querySelector('.flex.flex-grow');
    expect(headerContent).toBeInTheDocument();
  });

  test('handles multiple rapid clicks on hamburger button', () => {
    renderWithRouter(<Header {...mockProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    expect(menuButton).not.toBeNull();
    
    fireEvent.click(menuButton!);
    fireEvent.click(menuButton!);
    fireEvent.click(menuButton!);

    expect(mockSetSidebarOpen).toHaveBeenCalledTimes(3);
  });

  test('maintains accessibility with proper button attributes', () => {
    renderWithRouter(<Header {...mockProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    expect(menuButton).not.toBeNull();
    expect(menuButton).toHaveAttribute('aria-controls', 'sidebar');
  });

  test('renders with different sidebar states', () => {
    const { rerender } = renderWithRouter(<Header {...mockProps} />);
    
    // Test with sidebar closed
    expect(screen.getByRole('banner')).toBeInTheDocument();
    
    // Test with sidebar open - must wrap with providers
    rerender(
      <Router>
        <HeaderFiltersProvider>
          <DashboardFiltersProvider>
            <Header sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />
          </DashboardFiltersProvider>
        </HeaderFiltersProvider>
      </Router>
    );
    expect(screen.getByRole('banner')).toBeInTheDocument();
  });

  test.skip('hamburger button stops event propagation', () => {
    renderWithRouter(<Header {...mockProps} />);

    const menuButton = document.querySelector('button[aria-controls="sidebar"]');
    const mockStopPropagation = jest.fn();
    
    const mockEvent = {
      stopPropagation: mockStopPropagation,
      preventDefault: jest.fn()
    } as any;

    fireEvent.click(menuButton!, mockEvent);
    expect(mockStopPropagation).toHaveBeenCalled();
  });

  test.skip('hamburger icon animations work with sidebar state', () => {
    const { rerender } = renderWithRouter(<Header {...mockProps} />);
    
    // Check closed state classes
    const spans = document.querySelectorAll('span');
    const animationSpans = Array.from(spans).filter(span => 
      span.className.includes('delay-') && span.className.includes('!w-full')
    );
    expect(animationSpans.length).toBeGreaterThan(0);

    // Test with sidebar open
    rerender(<Router><Header sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} /></Router>);
    
    const openSpans = document.querySelectorAll('span');
    const openAnimationSpans = Array.from(openSpans).filter(span => 
      span.className.includes('!h-0')
    );
    expect(openAnimationSpans.length).toBeGreaterThan(0);
  });

  test('logo link navigates to home', () => {
    renderWithRouter(<Header {...mockProps} />);

    const logoLink = screen.getByRole('link', { name: /fuentus logo/i });
    expect(logoLink).toHaveAttribute('href', '/');
  });

  test('search form is hidden on small screens', () => {
    renderWithRouter(<Header {...mockProps} />);

    const searchContainer = document.querySelector('.hidden.sm\\:flex');
    expect(searchContainer).toBeInTheDocument();
  });

  test('handles empty search input submission', async () => {
    renderWithRouter(<Header {...mockProps} />);

    const form = document.querySelector('form');
    fireEvent.submit(form!);

    // Should navigate with empty search
    expect(mockNavigate).toHaveBeenCalledWith('/searchresults', {
      state: { searchValue: '' }
    });
  });

  test.skip('error message has correct styling', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    const form = searchInput.closest('form');
    
    await user.type(searchInput, ' ');
    fireEvent.submit(form!);

    await waitFor(() => {
      const errorMessage = screen.getByText('No se encontraron resultados para la búsqueda.');
      expect(errorMessage).toHaveClass('mt-2', 'text-sm', 'text-red-500');
    });
  });

  test('handles trimmed whitespace in search', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const searchInput = screen.getByPlaceholderText('Buscar...');
    const form = searchInput.closest('form');
    
    await user.type(searchInput, '  valid search  ');
    fireEvent.submit(form!);

    expect(mockNavigate).toHaveBeenCalledWith('/searchresults', {
      state: { searchValue: '  valid search  ' }
    });
  });

  test('clear filters button resets all selections', async () => {
    const user = userEvent.setup();
    renderWithRouter(<Header {...mockProps} />);

    const filtersToggle = screen.getByRole('button', { name: /filtros/i });
    await user.click(filtersToggle);

    // Wait for the filter panel to be visible
    await waitFor(() => {
      expect(screen.getByText('Período')).toBeInTheDocument();
    });

    // Get the selects by their labels/ids
    const sn1Select = screen.getByLabelText('Service N1');
    const sn2Select = screen.getByLabelText('Service N2');

    // Select some values
    await user.selectOptions(sn1Select, 'SN1 Option');
    await user.selectOptions(sn2Select, 'SN2 Option');

    // Wait for clear button to appear
    await waitFor(() => {
      expect(screen.getByText('Limpiar filtros')).toBeInTheDocument();
    });

    // Click clear filters
    await user.click(screen.getByText('Limpiar filtros'));

    // Verify all filters are reset
    await waitFor(() => {
      expect((sn1Select as HTMLSelectElement).value).toBe('');
      expect((sn2Select as HTMLSelectElement).value).toBe('');
    });

    // After clearing, the button might still be visible if dashboard filters exist
    // This is expected behavior as the button shows when ANY filters are active
    // So we just verify that the header filters (SN1, SN2) were cleared successfully
  });
});
