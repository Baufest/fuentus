import { render } from '@testing-library/react';
import Loader from '../../../common/Loader';
import '@testing-library/jest-dom';

describe('Loader', () => {
  test('renders with default props', () => {
    const { container } = render(<Loader />);
    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toBeInTheDocument();
    expect(spinner).toHaveClass('h-8', 'w-8', 'border-primary-500', 'border-4');
  });

  test('renders with different sizes', () => {
    const { container, rerender } = render(<Loader size="xs" />);
    let spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('h-4', 'w-4');

    rerender(<Loader size="sm" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('h-6', 'w-6');

    rerender(<Loader size="lg" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('h-12', 'w-12');

    rerender(<Loader size="xl" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('h-16', 'w-16');
  });

  test('renders with different colors', () => {
    const { container, rerender } = render(<Loader color="success" />);
    let spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-green-500');

    rerender(<Loader color="danger" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-red-500');

    rerender(<Loader color="warning" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-yellow-500');

    rerender(<Loader color="info" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-blue-500');

    rerender(<Loader color="white" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-white');

    rerender(<Loader color="gray" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-gray-500');

    rerender(<Loader color="secondary" />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-secondary');
  });

  test('renders with different thickness values', () => {
    const { container, rerender } = render(<Loader thickness={1} />);
    let spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border');

    rerender(<Loader thickness={2} />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-2');

    rerender(<Loader thickness={8} />);
    spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('border-8');
  });

  test('renders with custom thickness using inline styles', () => {
    const { container } = render(<Loader thickness={6} />);
    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveStyle({ borderWidth: '6px' });
    expect(spinner).toHaveClass('border-4'); // fallback class
  });

  test('renders with custom className', () => {
    const { container } = render(<Loader className="custom-class" />);
    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('custom-class');
  });

  test('renders in fullScreen mode', () => {
    const { container } = render(<Loader fullScreen />);
    const loaderContainer = container.firstChild as HTMLElement;
    expect(loaderContainer).toHaveClass(
      'fixed', 
      'inset-0', 
      'z-50', 
      'flex', 
      'h-screen', 
      'items-center', 
      'justify-center', 
      'bg-white'
    );
  });

  test('renders without fullScreen mode', () => {
    const { container } = render(<Loader />);
    const loaderContainer = container.firstChild as HTMLElement;
    expect(loaderContainer).toHaveClass('flex', 'items-center', 'justify-center');
    expect(loaderContainer).not.toHaveClass('fixed', 'inset-0');
  });

  test('handles fullScreen as false explicitly', () => {
    const { container } = render(<Loader fullScreen={false} />);
    const loaderContainer = container.firstChild as HTMLElement;
    expect(loaderContainer).toHaveClass('flex', 'items-center', 'justify-center');
    expect(loaderContainer).not.toHaveClass('fixed', 'inset-0');
  });

  test('has proper animation classes', () => {
    const { container } = render(<Loader />);
    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass('animate-spin', 'rounded-full', 'border-solid', 'border-t-transparent');
  });

  test('fallback thickness works with invalid values', () => {
    const { container } = render(<Loader thickness={999} />);
    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveStyle({ borderWidth: '999px' });
    expect(spinner).toHaveClass('border-4'); // fallback class
  });

  test('combines all props correctly', () => {
    const { container } = render(
      <Loader 
        size="lg" 
        color="success" 
        thickness={2} 
        className="my-custom-class"
        fullScreen={true}
      />
    );
    
    const spinner = container.querySelector('.animate-spin');
    expect(spinner).toHaveClass(
      'h-12', 'w-12',           // size lg
      'border-green-500',       // color success  
      'border-2',               // thickness 2
      'my-custom-class',        // custom class
      'animate-spin',           // animation
      'rounded-full',           // shape
      'border-solid',           // border style
      'border-t-transparent'    // transparent top
    );
  });
});
