import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import DefaultLayout from '../../layout/DefaultLayout';

// Mock de los componentes hijos para evitar problemas de dependencias
jest.mock('../../components/Header/index', () => {
  return function MockHeader({ sidebarOpen, setSidebarOpen }: any) {
    return (
      <header data-testid="header">
        <button onClick={() => setSidebarOpen(!sidebarOpen)}>
          Header {sidebarOpen ? 'Open' : 'Closed'}
        </button>
      </header>
    );
  };
});

jest.mock('../../components/Sidebar/index', () => {
  return function MockSidebar({ sidebarOpen, setSidebarOpen }: any) {
    return (
      <aside data-testid="sidebar">
        <button onClick={() => setSidebarOpen(!sidebarOpen)}>
          Sidebar {sidebarOpen ? 'Open' : 'Closed'}
        </button>
      </aside>
    );
  };
});

jest.mock('../../components/Footer', () => {
  return function MockFooter() {
    return <footer data-testid="footer">Footer Content</footer>;
  };
});

const renderWithRouter = (children: React.ReactNode) => {
  return render(<BrowserRouter>{children}</BrowserRouter>);
};

describe('DefaultLayout', () => {
  test('renders all main components', () => {
    renderWithRouter(
      <DefaultLayout>
        <div>Test Content</div>
      </DefaultLayout>
    );

    expect(screen.getByTestId('header')).toBeInTheDocument();
    expect(screen.getByTestId('sidebar')).toBeInTheDocument();
    expect(screen.getByTestId('footer')).toBeInTheDocument();
    expect(screen.getByText('Test Content')).toBeInTheDocument();
  });

  test('renders children content in main area', () => {
    renderWithRouter(
      <DefaultLayout>
        <div data-testid="child-content">Child Component</div>
      </DefaultLayout>
    );

    const mainContent = screen.getByTestId('child-content');
    expect(mainContent).toBeInTheDocument();
    expect(mainContent).toHaveTextContent('Child Component');
  });

  test('initializes with sidebar closed', () => {
    renderWithRouter(
      <DefaultLayout>
        <div>Content</div>
      </DefaultLayout>
    );

    expect(screen.getByText('Header Closed')).toBeInTheDocument();
    expect(screen.getByText('Sidebar Closed')).toBeInTheDocument();
  });

  test('has correct layout structure', () => {
    const { container } = renderWithRouter(
      <DefaultLayout>
        <div>Content</div>
      </DefaultLayout>
    );

    // Verificar la estructura básica del layout
    const pageWrapper = container.querySelector('.flex.h-screen.overflow-hidden');
    expect(pageWrapper).toBeInTheDocument();

    const contentArea = container.querySelector('.relative.flex.flex-1.flex-col.overflow-y-auto.overflow-x-hidden');
    expect(contentArea).toBeInTheDocument();

    const mainElement = container.querySelector('main');
    expect(mainElement).toBeInTheDocument();
  });

  test('applies dark mode classes', () => {
    const { container } = renderWithRouter(
      <DefaultLayout>
        <div>Content</div>
      </DefaultLayout>
    );

    const rootDiv = container.querySelector('.dark\\:bg-boxdark-2.dark\\:text-bodydark');
    expect(rootDiv).toBeInTheDocument();
  });

  test('renders with complex children', () => {
    renderWithRouter(
      <DefaultLayout>
        <div>
          <h1>Page Title</h1>
          <p>Page content</p>
          <button>Action Button</button>
        </div>
      </DefaultLayout>
    );

    expect(screen.getByText('Page Title')).toBeInTheDocument();
    expect(screen.getByText('Page content')).toBeInTheDocument();
    expect(screen.getByText('Action Button')).toBeInTheDocument();
  });

  test('main content has correct container classes', () => {
    const { container } = renderWithRouter(
      <DefaultLayout>
        <div data-testid="test-content">Test</div>
      </DefaultLayout>
    );

    const mainContentContainer = container.querySelector('.max-w-full.mx-10.mt-10');
    expect(mainContentContainer).toBeInTheDocument();
    expect(mainContentContainer).toContainElement(screen.getByTestId('test-content'));
  });
});
