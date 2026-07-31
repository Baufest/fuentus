import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import TableServers, { ServerAppsData } from '../../../components/Tables/TableServers';

const mockNavigate = jest.fn();

jest.mock('react-router-dom', () => {
  const actual = jest.requireActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

const renderWithRouter = (component: React.ReactElement) => {
  return render(<BrowserRouter>{component}</BrowserRouter>);
};

const createServer = (id: number, appCount = 1): ServerAppsData => ({
  id,
  name: `Servidor ${id}`,
  apps: Array.from({ length: appCount }, (_, idx) => ({
    id: Number(`${id}${idx + 1}`),
    name: `App ${id}.${idx + 1}`,
  })),
});

describe('TableServers', () => {
  beforeEach(() => {
    mockNavigate.mockClear();
  });

  it('shows loading state when loading is true', () => {
    renderWithRouter(<TableServers data={[]} loading />);
    expect(screen.getByText('Cargando servidores...')).toBeInTheDocument();
  });

  it('shows empty state when there are no servers', () => {
    renderWithRouter(<TableServers data={[]} loading={false} />);
    expect(screen.getByText('No se encontraron servidores')).toBeInTheDocument();
    expect(
      screen.getByText('Ajusta los filtros de búsqueda para ver resultados')
    ).toBeInTheDocument();
  });

  it('renders server rows with app chips', () => {
    const data = [createServer(1)];
    renderWithRouter(<TableServers data={data} />);

    expect(screen.getByText('Servidor 1')).toBeInTheDocument();
    expect(screen.getByText('App 1.1')).toBeInTheDocument();
    // total apps badge should show the amount of apps per server
    expect(screen.getByText('1')).toBeInTheDocument();
  });

  it('navigates to app detail when clicking an app chip', () => {
    const data = [createServer(1)];
    renderWithRouter(<TableServers data={data} />);

    fireEvent.click(screen.getByRole('button', { name: 'App 1.1' }));
    expect(mockNavigate).toHaveBeenCalledWith('/app/11');
  });

  it('shows show-more button when there are more than five servers', () => {
    const data = Array.from({ length: 7 }, (_, idx) => createServer(idx + 1));
    renderWithRouter(<TableServers data={data} />);

    expect(screen.queryByText('Servidor 6')).not.toBeInTheDocument();
    const showMoreButton = screen.getByRole('button', { name: 'Ver más (2 más)' });
    fireEvent.click(showMoreButton);

    expect(screen.getByText('Servidor 6')).toBeInTheDocument();
    expect(screen.getByText('Servidor 7')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Ver menos' })).toBeInTheDocument();
  });

  it('hides show-more button when servers are five or fewer', () => {
    const data = Array.from({ length: 5 }, (_, idx) => createServer(idx + 1));
    renderWithRouter(<TableServers data={data} />);

    expect(screen.queryByRole('button', { name: /Ver más/ })).not.toBeInTheDocument();
  });
});
