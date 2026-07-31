import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { Tooltip } from '../../../components/Helpers/Tooltip';

describe('Tooltip', () => {
  test('renders children correctly', () => {
    render(
      <Tooltip text="Test tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    expect(screen.getByRole('button', { name: 'Hover me' })).toBeInTheDocument();
  });

  test('renders tooltip text on hover', async () => {
    const { container } = render(
      <Tooltip text="Test tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    // Tooltip should not be visible initially
    expect(screen.queryByText('Test tooltip')).not.toBeInTheDocument();
    
    // Hover over the container
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    fireEvent.mouseEnter(tooltipContainer);
    
    // Tooltip should be visible
    await waitFor(() => {
      expect(screen.getByText('Test tooltip')).toBeInTheDocument();
    });
  });

  test('applies default CSS classes to container', () => {
    const { container } = render(
      <Tooltip text="Test tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    expect(tooltipContainer).toHaveClass('inline-block');
  });

  test('applies custom className when provided', () => {
    const { container } = render(
      <Tooltip text="Test tooltip" className="custom-class">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    expect(tooltipContainer).toHaveClass('custom-class');
  });

  test('tooltip text has correct styling classes', async () => {
    const { container } = render(
      <Tooltip text="Test tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    fireEvent.mouseEnter(tooltipContainer);
    
    await waitFor(() => {
      const tooltipText = screen.getByText('Test tooltip');
      expect(tooltipText).toHaveClass(
        'fixed',
        '-translate-x-1/2',
        '-translate-y-full',
        'bg-black',
        'text-white',
        'text-base',
        'rounded',
        'py-2',
        'px-3',
        'whitespace-nowrap',
        'pointer-events-none'
      );
    });
  });

  test('tooltip is initially hidden', () => {
    render(
      <Tooltip text="Test tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    expect(screen.queryByText('Test tooltip')).not.toBeInTheDocument();
  });

  test('tooltip shows on hover and hides on mouse leave', async () => {
    const { container } = render(
      <Tooltip text="Test tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    
    // Hover to show
    fireEvent.mouseEnter(tooltipContainer);
    await waitFor(() => {
      expect(screen.getByText('Test tooltip')).toBeInTheDocument();
    });
    
    // Mouse leave to hide
    fireEvent.mouseLeave(tooltipContainer);
    await waitFor(() => {
      expect(screen.queryByText('Test tooltip')).not.toBeInTheDocument();
    });
  });

  test('renders with complex children', () => {
    render(
      <Tooltip text="Complex tooltip">
        <div>
          <span>Complex content</span>
          <button>Button inside</button>
        </div>
      </Tooltip>
    );
    
    expect(screen.getByText('Complex content')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Button inside' })).toBeInTheDocument();
  });

  test('handles long tooltip text with whitespace-nowrap', async () => {
    const longText = 'This is a very long tooltip text that should not wrap to multiple lines';
    const { container } = render(
      <Tooltip text={longText}>
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    fireEvent.mouseEnter(tooltipContainer);
    
    await waitFor(() => {
      const tooltipText = screen.getByText(longText);
      expect(tooltipText).toHaveClass('whitespace-nowrap');
    });
  });

  test('tooltip positioning uses fixed positioning', async () => {
    const { container } = render(
      <Tooltip text="Positioned tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    fireEvent.mouseEnter(tooltipContainer);
    
    await waitFor(() => {
      const tooltipText = screen.getByText('Positioned tooltip');
      expect(tooltipText).toHaveClass('fixed', '-translate-x-1/2', '-translate-y-full');
    });
  });

  test('tooltip has high z-index for proper layering', async () => {
    const { container } = render(
      <Tooltip text="High z-index tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    fireEvent.mouseEnter(tooltipContainer);
    
    await waitFor(() => {
      const tooltipText = screen.getByText('High z-index tooltip');
      expect(tooltipText).toHaveClass('z-[9999]');
    });
  });

  test('container has correct aria-label', () => {
    const { container } = render(
      <Tooltip text="Accessibility tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]') as HTMLElement;
    expect(tooltipContainer).toHaveAttribute('aria-label', 'Accessibility tooltip');
  });

  test('container has tooltip role', () => {
    const { container } = render(
      <Tooltip text="Role tooltip">
        <button>Hover me</button>
      </Tooltip>
    );
    
    const tooltipContainer = container.querySelector('[role="tooltip"]');
    expect(tooltipContainer).toBeInTheDocument();
  });
});
