import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Buttons from '../../../pages/UiElements/Buttons';

// Mock del componente Breadcrumb
jest.mock('../../../components/Breadcrumbs/Breadcrumb', () => {
  return function MockBreadcrumb({ pageName }: { pageName: string }) {
    return <div data-testid="breadcrumb">Breadcrumb: {pageName}</div>;
  };
});

describe('Buttons Page', () => {
  const renderWithRouter = (component: React.ReactElement) => {
    return render(
      <BrowserRouter>
        {component}
      </BrowserRouter>
    );
  };

  test('renders without crashing', () => {
    renderWithRouter(<Buttons />);
    
    expect(screen.getByTestId('breadcrumb')).toBeInTheDocument();
    expect(screen.getByText('Breadcrumb: Buttons')).toBeInTheDocument();
  });

  test('displays section headers', () => {
    renderWithRouter(<Buttons />);
    
    expect(screen.getByText('Normal Button')).toBeInTheDocument();
    expect(screen.getAllByText('Button With Icon')[0]).toBeInTheDocument(); // Hay múltiples elementos con este texto
  });

  test('renders normal buttons section', () => {
    renderWithRouter(<Buttons />);
    
    // Check for button text content
    const buttonElements = screen.getAllByText('Button');
    expect(buttonElements.length).toBeGreaterThan(0);
  });

  test('renders buttons with icon section', () => {
    renderWithRouter(<Buttons />);
    
    expect(screen.getAllByText('Button With Icon')[0]).toBeInTheDocument(); // Hay múltiples elementos con este texto
  });

  test('has correct structure with multiple button sections', () => {
    renderWithRouter(<Buttons />);
    
    // Check for section containers - ajustado a la realidad
    const sectionContainers = document.querySelectorAll('.rounded-sm.border.border-stroke');
    expect(sectionContainers.length).toBeGreaterThan(1); // Ajustado
  });

  test('renders Link elements with correct href attributes', () => {
    renderWithRouter(<Buttons />);
    
    const linkElements = document.querySelectorAll('a[href="/"]'); // Corregido href
    expect(linkElements.length).toBeGreaterThan(0);
  });

  test('applies correct CSS classes to button containers', () => {
    renderWithRouter(<Buttons />);
    
    const mainContainer = document.querySelector('.mb-10.rounded-sm.border');
    expect(mainContainer).toBeInTheDocument();
    expect(mainContainer).toHaveClass('bg-white', 'shadow-default');
  });

  test('renders button sections with proper headers', () => {
    renderWithRouter(<Buttons />);
    
    // Check that section headers are within bordered divs
    const sectionHeaders = screen.getAllByText(/Button/);
    expect(sectionHeaders.length).toBeGreaterThan(5); // Multiple instances
  });

  test('contains SVG icons in button with icon section', () => {
    renderWithRouter(<Buttons />);
    
    // Check for SVG elements (icons in buttons)
    const svgElements = document.querySelectorAll('svg');
    expect(svgElements.length).toBeGreaterThan(0);
  });

  test('applies dark mode classes correctly', () => {
    renderWithRouter(<Buttons />);
    
    // Check for dark mode classes
    const darkElements = document.querySelectorAll('.dark\\:border-strokedark');
    expect(darkElements.length).toBeGreaterThan(0);
  });

  test('renders proper button layouts', () => {
    renderWithRouter(<Buttons />);
    
    // Check for flex layout containers
    const flexContainers = document.querySelectorAll('.flex.flex-wrap');
    expect(flexContainers.length).toBeGreaterThan(0);
  });

  test('contains proper padding classes', () => {
    renderWithRouter(<Buttons />);
    
    // Check for responsive padding
    const paddedElements = document.querySelectorAll('.p-4.md\\:p-6.xl\\:p-9');
    expect(paddedElements.length).toBeGreaterThan(0);
  });
});
