import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Dashboard from '../../pages/Dashboard/Dashboard';

// Mock del componente ChartDashboard para evitar dependencias complejas
jest.mock('../../components/Charts/ChartDashboard', () => {
  return function MockChartDashboard() {
    return <div data-testid="chart-dashboard">Mocked Chart Dashboard</div>;
  };
});

const renderWithRouter = (component: React.ReactElement) => {
  return render(<BrowserRouter>{component}</BrowserRouter>);
};

describe('Dashboard', () => {
  test('renders Dashboard component', () => {
    const { getByTestId } = renderWithRouter(<Dashboard />);
    
    expect(getByTestId('chart-dashboard')).toBeInTheDocument();
  });

  test('renders ChartDashboard component', () => {
    const { getByText } = renderWithRouter(<Dashboard />);
    
    expect(getByText('Mocked Chart Dashboard')).toBeInTheDocument();
  });

  test('has correct component structure', () => {
    const { container } = renderWithRouter(<Dashboard />);
    
    // Verificar que el componente se renderiza dentro de un Fragment
    expect(container.firstChild).toBeInTheDocument();
  });

  test('renders without errors', () => {
    expect(() => {
      renderWithRouter(<Dashboard />);
    }).not.toThrow();
  });
});
