import { render, screen } from '@testing-library/react';
import CardDataStats from '../../components/CardDataStats';

describe('CardDataStats', () => {
  const mockProps = {
    title: 'Test Title',
    total: '1,234',
    rate: '+5%',
    children: <div data-testid="icon">Icon</div>
  };

  test('renders component with required props', () => {
    render(<CardDataStats {...mockProps} />);
    
    expect(screen.getByText('Test Title')).toBeInTheDocument();
    expect(screen.getByText('1,234')).toBeInTheDocument();
    expect(screen.getByText('+5%')).toBeInTheDocument();
    expect(screen.getByTestId('icon')).toBeInTheDocument();
  });

  test('shows level up indicator when levelUp is true', () => {
    render(<CardDataStats {...mockProps} levelUp={true} />);
    
    const rateElement = screen.getByText('+5%');
    expect(rateElement).toHaveClass('text-meta-3');
    
    // Check for SVG arrow up
    const svgElement = rateElement.querySelector('svg');
    expect(svgElement).toBeInTheDocument();
    expect(svgElement).toHaveClass('fill-meta-3');
  });

  test('shows level down indicator when levelDown is true', () => {
    render(<CardDataStats {...mockProps} levelDown={true} />);
    
    const rateElement = screen.getByText('+5%');
    expect(rateElement).toHaveClass('text-meta-5');
    
    // Check for SVG arrow down
    const svgElement = rateElement.querySelector('svg');
    expect(svgElement).toBeInTheDocument();
    expect(svgElement).toHaveClass('fill-meta-5');
  });

  test('does not show indicators when neither levelUp nor levelDown is true', () => {
    render(<CardDataStats {...mockProps} />);
    
    const rateElement = screen.getByText('+5%');
    expect(rateElement).not.toHaveClass('text-meta-3');
    expect(rateElement).not.toHaveClass('text-meta-5');
    
    // Check no SVG is present
    const svgElement = rateElement.querySelector('svg');
    expect(svgElement).not.toBeInTheDocument();
  });

  test('applies dark mode classes correctly', () => {
    const { container } = render(<CardDataStats {...mockProps} />);
    
    // Check for dark mode classes in the main container
    const cardElement = container.firstChild as HTMLElement;
    expect(cardElement).toHaveClass('dark:border-strokedark', 'dark:bg-boxdark');
    
    // Check for dark mode classes in icon container
    const iconContainer = container.querySelector('.bg-meta-2');
    expect(iconContainer).toHaveClass('dark:bg-meta-4');
    
    // Check for dark mode classes in title
    const titleElement = screen.getByText('1,234');
    expect(titleElement).toHaveClass('dark:text-white');
  });
});
