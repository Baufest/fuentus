import { render, screen } from '@testing-library/react';
import Badge from '../../../components/Badges/Badge';

describe('Badge', () => {
  test('renders badge with children', () => {
    render(<Badge>Test Badge</Badge>);
    
    expect(screen.getByText('Test Badge')).toBeInTheDocument();
  });

  test('applies default props correctly', () => {
    const { container } = render(<Badge>Default Badge</Badge>);
    
    const badge = container.firstChild as HTMLElement;
    expect(badge).toHaveClass(
      'inline-flex',
      'items-center',
      'px-2.5',
      'py-0.5',
      'justify-center',
      'gap-1',
      'rounded-full',
      'font-medium',
      'text-sm', // default size md
      'bg-brand-50', // default light primary
      'text-brand-500',
      'ml-2'
    );
  });

  test('renders with small size', () => {
    const { container } = render(<Badge size="sm">Small Badge</Badge>);
    
    const badge = container.firstChild as HTMLElement;
    expect(badge).toHaveClass('text-theme-xs');
  });

  test('renders with medium size', () => {
    const { container } = render(<Badge size="md">Medium Badge</Badge>);
    
    const badge = container.firstChild as HTMLElement;
    expect(badge).toHaveClass('text-sm');
  });

  test('renders light variant with different colors', () => {
    const colors = ['primary', 'success', 'error', 'warning', 'info', 'light', 'dark'] as const;
    
    colors.forEach((color) => {
      const { container } = render(
        <Badge variant="light" color={color}>
          {color} Badge
        </Badge>
      );
      
      const badge = container.firstChild as HTMLElement;
      expect(badge).toBeInTheDocument();
      expect(screen.getByText(`${color} Badge`)).toBeInTheDocument();
    });
  });

  test('renders solid variant with different colors', () => {
    const colors = ['primary', 'success', 'error', 'warning', 'info', 'light', 'dark'] as const;
    
    colors.forEach((color) => {
      const { container } = render(
        <Badge variant="solid" color={color}>
          {color} Badge
        </Badge>
      );
      
      const badge = container.firstChild as HTMLElement;
      expect(badge).toBeInTheDocument();
      expect(screen.getByText(`${color} Badge`)).toBeInTheDocument();
    });
  });

  test('renders success light variant with correct classes', () => {
    const { container } = render(
      <Badge variant="light" color="success">
        Success Badge
      </Badge>
    );
    
    const badge = container.firstChild as HTMLElement;
    expect(badge).toHaveClass('bg-success-50', 'text-success-600');
  });

  test('renders error solid variant with correct classes', () => {
    const { container } = render(
      <Badge variant="solid" color="error">
        Error Badge
      </Badge>
    );
    
    const badge = container.firstChild as HTMLElement;
    expect(badge).toHaveClass('bg-error-500', 'text-white');
  });

  test('renders with start icon', () => {
    const startIcon = <span data-testid="start-icon">🚀</span>;
    
    render(
      <Badge startIcon={startIcon}>
        Badge with Start Icon
      </Badge>
    );
    
    expect(screen.getByTestId('start-icon')).toBeInTheDocument();
    expect(screen.getByText('Badge with Start Icon')).toBeInTheDocument();
  });

  test('renders with end icon', () => {
    const endIcon = <span data-testid="end-icon">✨</span>;
    
    render(
      <Badge endIcon={endIcon}>
        Badge with End Icon
      </Badge>
    );
    
    expect(screen.getByTestId('end-icon')).toBeInTheDocument();
    expect(screen.getByText('Badge with End Icon')).toBeInTheDocument();
  });

  test('renders with both start and end icons', () => {
    const startIcon = <span data-testid="start-icon">🚀</span>;
    const endIcon = <span data-testid="end-icon">✨</span>;
    
    render(
      <Badge startIcon={startIcon} endIcon={endIcon}>
        Badge with Both Icons
      </Badge>
    );
    
    expect(screen.getByTestId('start-icon')).toBeInTheDocument();
    expect(screen.getByTestId('end-icon')).toBeInTheDocument();
    expect(screen.getByText('Badge with Both Icons')).toBeInTheDocument();
  });

  test('applies dark mode classes correctly', () => {
    const { container } = render(
      <Badge variant="light" color="primary">
        Dark Mode Badge
      </Badge>
    );
    
    const badge = container.firstChild as HTMLElement;
    expect(badge).toHaveClass('dark:bg-brand-500/15', 'dark:text-brand-400');
  });

  test('icon containers have correct spacing classes', () => {
    const startIcon = <span data-testid="start-icon">🚀</span>;
    const endIcon = <span data-testid="end-icon">✨</span>;
    
    render(
      <Badge startIcon={startIcon} endIcon={endIcon}>
        Test Badge
      </Badge>
    );
    
    const startIconContainer = screen.getByTestId('start-icon').parentElement;
    const endIconContainer = screen.getByTestId('end-icon').parentElement;
    
    expect(startIconContainer).toHaveClass('mr-1');
    expect(endIconContainer).toHaveClass('ml-1');
  });
});
