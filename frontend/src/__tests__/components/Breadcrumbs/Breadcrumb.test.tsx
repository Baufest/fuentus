import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Breadcrumb from '../../../components/Breadcrumbs/Breadcrumb';

const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  );
};

describe('Breadcrumb', () => {
  test('renders breadcrumb with page name', () => {
    renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    expect(screen.getByText('Test Page')).toBeInTheDocument();
    expect(screen.getByText('Test Page | Actualizado: 01/05/25')).toBeInTheDocument();
  });

  test('renders dashboard link', () => {
    renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    const dashboardLink = screen.getByText('Dashboard /');
    expect(dashboardLink).toBeInTheDocument();
    expect(dashboardLink.closest('a')).toHaveAttribute('href', '/');
  });

  test('applies correct CSS classes to main container', () => {
    const { container } = renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    const mainContainer = container.firstChild as HTMLElement;
    expect(mainContainer).toHaveClass(
      'mb-6',
      'flex',
      'flex-col',
      'gap-3',
      'sm:flex-row',
      'sm:items-center',
      'sm:justify-between'
    );
  });

  test('applies correct classes to page title', () => {
    renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    const pageTitle = screen.getByRole('heading', { level: 2 });
    expect(pageTitle).toHaveClass(
      'text-title-md2',
      'font-semibold',
      'text-black',
      'dark:text-white'
    );
  });

  test('applies correct classes to dashboard link', () => {
    renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    const dashboardLink = screen.getByText('Dashboard /');
    expect(dashboardLink).toHaveClass('font-medium');
  });

  test('applies correct classes to current page item', () => {
    renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    const currentPageItem = screen.getByText('Test Page | Actualizado: 01/05/25');
    expect(currentPageItem).toHaveClass(
      'font-medium',
      'text-primary',
      'dark:text-white'
    );
  });

  test('renders navigation structure correctly', () => {
    renderWithRouter(<Breadcrumb pageName="Test Page" />);
    
    const nav = screen.getByRole('navigation');
    expect(nav).toBeInTheDocument();
    
    const list = screen.getByRole('list');
    expect(list).toBeInTheDocument();
    expect(list).toHaveClass('flex', 'items-center', 'gap-2');
    
    const listItems = screen.getAllByRole('listitem');
    expect(listItems).toHaveLength(2);
  });

  test('displays updated date correctly', () => {
    renderWithRouter(<Breadcrumb pageName="Custom Page" />);
    
    expect(screen.getByText('Custom Page | Actualizado: 01/05/25')).toBeInTheDocument();
  });
});
