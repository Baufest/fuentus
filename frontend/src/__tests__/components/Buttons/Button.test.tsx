import { render, screen, fireEvent } from '@testing-library/react';
import Button from '../../../components/Buttons/Button';

describe('Button', () => {
  test('renders button with children', () => {
    render(<Button>Test Button</Button>);
    
    expect(screen.getByRole('button', { name: 'Test Button' })).toBeInTheDocument();
  });

  test('applies default props correctly', () => {
    const { container } = render(<Button>Default Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass(
      'inline-flex',
      'items-center',
      'justify-center',
      'gap-2',
      'rounded-lg',
      'transition',
      'px-5', // md size
      'py-3.5',
      'text-sm',
      'bg-primary', // primary variant
      'text-white'
    );
    expect(button).toHaveAttribute('type', 'button');
  });

  test('renders with small size', () => {
    const { container } = render(<Button size="sm">Small Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('px-4', 'py-3', 'text-sm');
  });

  test('renders with medium size', () => {
    const { container } = render(<Button size="md">Medium Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('px-5', 'py-3.5', 'text-sm');
  });

  test('renders with custom size', () => {
    const { container } = render(<Button size="custom">Custom Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).not.toHaveClass('px-4', 'py-3', 'px-5', 'py-3.5');
  });

  test('renders primary variant correctly', () => {
    const { container } = render(<Button variant="primary">Primary Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('bg-primary', 'text-white', 'shadow-theme-xs');
  });

  test('renders outline variant correctly', () => {
    const { container } = render(<Button variant="outline">Outline Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('bg-white', 'text-gray-700', 'ring-1', 'ring-inset', 'ring-gray-300');
  });

  test('renders success variant correctly', () => {
    const { container } = render(<Button variant="success">Success Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('bg-green-500', 'text-white', 'shadow-theme-xs');
  });

  test('renders info variant correctly', () => {
    const { container } = render(<Button variant="info">Info Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('bg-blue-500', 'text-white', 'shadow-theme-xs');
  });

  test('renders warning variant correctly', () => {
    const { container } = render(<Button variant="warning">Warning Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('bg-warning-500', 'text-white', 'shadow-theme-xs');
  });

  test('renders danger variant correctly', () => {
    const { container } = render(<Button variant="danger">Danger Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('bg-danger-500', 'text-white', 'shadow-theme-xs');
  });

  test('handles click events', () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick}>Clickable Button</Button>);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  test('renders as disabled when disabled prop is true', () => {
    const { container } = render(<Button disabled>Disabled Button</Button>);
    
    const button = container.firstChild as HTMLElement;
    expect(button).toBeDisabled();
    expect(button).toHaveClass('cursor-not-allowed', 'opacity-50');
  });

  test('does not call onClick when disabled', () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick} disabled>Disabled Button</Button>);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(handleClick).not.toHaveBeenCalled();
  });

  test('renders with start icon', () => {
    const startIcon = <span data-testid="start-icon">🚀</span>;
    
    render(
      <Button startIcon={startIcon}>
        Button with Start Icon
      </Button>
    );
    
    expect(screen.getByTestId('start-icon')).toBeInTheDocument();
    expect(screen.getByText('Button with Start Icon')).toBeInTheDocument();
  });

  test('renders with end icon', () => {
    const endIcon = <span data-testid="end-icon">✨</span>;
    
    render(
      <Button endIcon={endIcon}>
        Button with End Icon
      </Button>
    );
    
    expect(screen.getByTestId('end-icon')).toBeInTheDocument();
    expect(screen.getByText('Button with End Icon')).toBeInTheDocument();
  });

  test('renders with both start and end icons', () => {
    const startIcon = <span data-testid="start-icon">🚀</span>;
    const endIcon = <span data-testid="end-icon">✨</span>;
    
    render(
      <Button startIcon={startIcon} endIcon={endIcon}>
        Button with Both Icons
      </Button>
    );
    
    expect(screen.getByTestId('start-icon')).toBeInTheDocument();
    expect(screen.getByTestId('end-icon')).toBeInTheDocument();
    expect(screen.getByText('Button with Both Icons')).toBeInTheDocument();
  });

  test('applies custom className', () => {
    const { container } = render(
      <Button className="custom-class">Custom Class Button</Button>
    );
    
    const button = container.firstChild as HTMLElement;
    expect(button).toHaveClass('custom-class');
  });

  test('renders with different button types', () => {
    const { rerender } = render(<Button type="submit">Submit Button</Button>);
    expect(screen.getByRole('button')).toHaveAttribute('type', 'submit');
    
    rerender(<Button type="reset">Reset Button</Button>);
    expect(screen.getByRole('button')).toHaveAttribute('type', 'reset');
    
    rerender(<Button type="button">Button</Button>);
    expect(screen.getByRole('button')).toHaveAttribute('type', 'button');
  });

  test('icon containers have correct flex classes', () => {
    const startIcon = <span data-testid="start-icon">🚀</span>;
    const endIcon = <span data-testid="end-icon">✨</span>;
    
    render(
      <Button startIcon={startIcon} endIcon={endIcon}>
        Test Button
      </Button>
    );
    
    const startIconContainer = screen.getByTestId('start-icon').parentElement;
    const endIconContainer = screen.getByTestId('end-icon').parentElement;
    
    expect(startIconContainer).toHaveClass('flex', 'items-center');
    expect(endIconContainer).toHaveClass('flex', 'items-center');
  });
});
