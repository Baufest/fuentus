import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Alerts from '../../../pages/UiElements/Alerts';

// Mock del componente Breadcrumb
jest.mock('../../../components/Breadcrumbs/Breadcrumb', () => {
  return function MockBreadcrumb({ pageName }: { pageName: string }) {
    return <div data-testid="breadcrumb">Breadcrumb: {pageName}</div>;
  };
});

describe('Alerts Page', () => {
  const renderWithRouter = (component: React.ReactElement) => {
    return render(
      <BrowserRouter>
        {component}
      </BrowserRouter>
    );
  };

  test('renders without crashing', () => {
    renderWithRouter(<Alerts />);
    
    expect(screen.getByTestId('breadcrumb')).toBeInTheDocument();
    expect(screen.getByText('Breadcrumb: Alerts')).toBeInTheDocument();
  });

  test('displays warning alert', () => {
    renderWithRouter(<Alerts />);
    
    expect(screen.getByText('Attention needed')).toBeInTheDocument();
  });

  test('displays success alert', () => {
    renderWithRouter(<Alerts />);
    
    expect(screen.getByText('Message Sent Successfully')).toBeInTheDocument();
  });

  test('displays error alert', () => {
    renderWithRouter(<Alerts />);
    
    expect(screen.getByText('There were 1 errors with your submission')).toBeInTheDocument();
  });

  test('has correct CSS classes for main container', () => {
    renderWithRouter(<Alerts />);
    
    const container = document.querySelector('.rounded-sm.border.border-stroke');
    expect(container).toBeInTheDocument();
    expect(container).toHaveClass('bg-white', 'p-4', 'shadow-default');
  });

  test('renders alert types with correct styling', () => {
    renderWithRouter(<Alerts />);
    
    // Warning alert
    const warningAlert = screen.getByText('Attention needed').closest('.flex');
    expect(warningAlert).toHaveClass('border-warning');
    
    // Success alert (green)
    const successAlert = screen.getByText('Message Sent Successfully').closest('.flex');
    expect(successAlert).toHaveClass('border-[#34D399]');
    
    // Error alert (red) 
    const errorAlert = screen.getByText('There were 1 errors with your submission').closest('.flex');
    expect(errorAlert).toHaveClass('border-[#F87171]');
  });

  test('renders SVG icons for alert types', () => {
    renderWithRouter(<Alerts />);
    
    // Check that SVG elements are present
    const svgElements = document.querySelectorAll('svg');
    expect(svgElements.length).toBeGreaterThan(0);
  });

  test('applies dark mode classes correctly', () => {
    renderWithRouter(<Alerts />);
    
    // Check dark mode classes are present in the DOM
    const darkElements = document.querySelectorAll('.dark\\:bg-\\[\\#1B1B24\\]');
    expect(darkElements.length).toBeGreaterThan(0);
  });
});
