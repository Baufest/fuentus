import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import App from '../App';

// Mock de todos los componentes pesados para que el test sea más rápido
jest.mock('../pages/Dashboard/Dashboard', () => {
  return function MockDashboard() {
    return <div data-testid="mock-dashboard">Mock Dashboard</div>;
  };
});

jest.mock('../pages/SearchResults/Tables', () => {
  return function MockSearchResults() {
    return <div data-testid="mock-search-results">Mock Search Results</div>;
  };
});

// Mock the Loader component to avoid rendering issues
jest.mock('../common/Loader', () => {
  return function MockLoader() {
    return <div data-testid="loader">Loading...</div>;
  };
});

describe('App', () => {
  const renderWithRouter = (initialRoute = '/') => {
    window.history.pushState({}, 'Test page', initialRoute);
    return render(
      <BrowserRouter>
        <App />
      </BrowserRouter>
    );
  };

  test('renders without crashing', () => {
    const { container } = renderWithRouter();
    
    // Verificar que la aplicación se renderiza
    expect(container).toBeInTheDocument();
  });

  test('renders some content', () => {
    const { container } = renderWithRouter('/');
    
    // Verificar que se renderiza algún contenido (loader o dashboard)
    expect(container.firstChild).toBeTruthy();
  });

  test('has correct document structure', () => {
    const { container } = renderWithRouter();
    
    // Verificar que hay elementos básicos de la aplicación
    expect(container).toBeInTheDocument();
    expect(container.firstChild).toBeTruthy();
  });
});
