import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Alert from '../../../components/Alert/Alert';

const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  );
};

describe('Alert', () => {
  const defaultProps = {
    variant: 'info' as const,
    title: 'Test Alert',
    message: 'This is a test message'
  };

  test('renders alert with title and message', () => {
    renderWithRouter(<Alert {...defaultProps} />);
    
    expect(screen.getByText('Test Alert')).toBeInTheDocument();
    expect(screen.getByText('This is a test message')).toBeInTheDocument();
  });

  test('renders success variant with correct styling', () => {
    const { container } = renderWithRouter(
      <Alert {...defaultProps} variant="success" />
    );
    
    const alertContainer = container.firstChild as HTMLElement;
    expect(alertContainer).toHaveClass('border-success-500', 'bg-success-50');
    
    const iconContainer = container.querySelector('[class*="text-success-500"]');
    expect(iconContainer).toBeInTheDocument();
  });

  test('renders error variant with correct styling', () => {
    const { container } = renderWithRouter(
      <Alert {...defaultProps} variant="error" />
    );
    
    const alertContainer = container.firstChild as HTMLElement;
    expect(alertContainer).toHaveClass('border-error-500', 'bg-error-50');
    
    const iconContainer = container.querySelector('[class*="text-error-500"]');
    expect(iconContainer).toBeInTheDocument();
  });

  test('renders warning variant with correct styling', () => {
    const { container } = renderWithRouter(
      <Alert {...defaultProps} variant="warning" />
    );
    
    const alertContainer = container.firstChild as HTMLElement;
    expect(alertContainer).toHaveClass('border-warning-500', 'bg-warning-50');
    
    const iconContainer = container.querySelector('[class*="text-warning-500"]');
    expect(iconContainer).toBeInTheDocument();
  });

  test('renders info variant with correct styling', () => {
    const { container } = renderWithRouter(
      <Alert {...defaultProps} variant="info" />
    );
    
    const alertContainer = container.firstChild as HTMLElement;
    expect(alertContainer).toHaveClass('border-blue-light-500', 'bg-blue-light-50');
    
    const iconContainer = container.querySelector('[class*="text-blue-light-500"]');
    expect(iconContainer).toBeInTheDocument();
  });

  test('shows link when showLink is true', () => {
    renderWithRouter(
      <Alert 
        {...defaultProps} 
        showLink={true}
        linkHref="/test"
        linkText="Test Link"
      />
    );
    
    const link = screen.getByText('Test Link');
    expect(link).toBeInTheDocument();
    expect(link.closest('a')).toHaveAttribute('href', '/test');
  });

  test('does not show link when showLink is false', () => {
    renderWithRouter(<Alert {...defaultProps} showLink={false} />);
    
    expect(screen.queryByText('Learn more')).not.toBeInTheDocument();
  });

  test('uses default link text when not provided', () => {
    renderWithRouter(
      <Alert {...defaultProps} showLink={true} linkHref="/test" />
    );
    
    expect(screen.getByText('Learn more')).toBeInTheDocument();
  });

  test('uses default link href when not provided', () => {
    renderWithRouter(
      <Alert {...defaultProps} showLink={true} />
    );
    
    const link = screen.getByText('Learn more');
    // NavLink normalizes '#' to '/' in testing environment
    expect(link.closest('a')).toHaveAttribute('href', '/');
  });

  test('renders appropriate SVG icon for each variant', () => {
    const variants = ['success', 'error', 'warning', 'info'] as const;
    
    variants.forEach((variant) => {
      const { container } = renderWithRouter(
        <Alert {...defaultProps} variant={variant} />
      );
      
      const svgElement = container.querySelector('svg');
      expect(svgElement).toBeInTheDocument();
      expect(svgElement).toHaveClass('fill-current');
    });
  });

  test('applies dark mode classes correctly', () => {
    const { container } = renderWithRouter(<Alert {...defaultProps} />);
    
    const alertContainer = container.firstChild as HTMLElement;
    expect(alertContainer).toHaveClass('dark:border-blue-light-500/30', 'dark:bg-blue-light-500/15');
    
    const titleElement = screen.getByText('Test Alert');
    expect(titleElement).toHaveClass('dark:text-white/90');
    
    const messageElement = screen.getByText('This is a test message');
    expect(messageElement).toHaveClass('dark:text-gray-400');
  });
});
