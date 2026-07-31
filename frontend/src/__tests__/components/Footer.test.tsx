import { render, screen } from '@testing-library/react';
import Footer from '../../components/Footer';

describe('Footer', () => {
  test('renders footer component', () => {
    render(<Footer />);
    
    // Check if footer element is present
    const footerElement = screen.getByRole('contentinfo');
    expect(footerElement).toBeInTheDocument();
  });

  test('displays BBVA logo', () => {
    render(<Footer />);
    
    const logoImage = screen.getByAltText('BBVA Logo');
    expect(logoImage).toBeInTheDocument();
    expect(logoImage).toHaveAttribute('src', '/src/images/logo/BBVA_RGB.png');
    expect(logoImage).toHaveClass('h-8', 'mx-2');
  });

  test('displays current year copyright', () => {
    render(<Footer />);
    
    const currentYear = new Date().getFullYear();
    const copyrightText = screen.getByText(`© ${currentYear}`);
    expect(copyrightText).toBeInTheDocument();
  });

  test('applies correct CSS classes', () => {
    const { container } = render(<Footer />);
    
    const footerElement = container.firstChild as HTMLElement;
    expect(footerElement).toHaveClass(
      'w-full',
      'py-4',
      'bg-white',
      'dark:bg-boxdark-2',
      'border-t',
      'border-gray-200',
      'dark:border-strokedark',
      'flex',
      'justify-center',
      'items-center',
      'mt-auto'
    );
  });

  test('applies dark mode classes for copyright text', () => {
    render(<Footer />);
    
    const currentYear = new Date().getFullYear();
    const copyrightElement = screen.getByText(`© ${currentYear}`);
    expect(copyrightElement).toHaveClass('text-gray-600', 'dark:text-gray-300');
  });
});
