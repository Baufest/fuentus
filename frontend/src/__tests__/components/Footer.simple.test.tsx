import { render } from '@testing-library/react';
import Footer from '../../components/Footer';

describe('Footer', () => {
  test('renders without crashing', () => {
    const { container } = render(<Footer />);
    expect(container).toBeInTheDocument();
  });

  test('has correct structure', () => {
    const { container } = render(<Footer />);
    expect(container.firstChild).toBeInTheDocument();
  });
});
