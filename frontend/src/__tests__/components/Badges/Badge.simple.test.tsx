import { render } from '@testing-library/react';
import Badge from '../../../components/Badges/Badge';

describe('Badge', () => {
  test('renders with children', () => {
    const { getByText } = render(<Badge>Test Badge</Badge>);
    expect(getByText('Test Badge')).toBeInTheDocument();
  });

  test('renders with different colors', () => {
    const { getByText } = render(<Badge color="success">Success Badge</Badge>);
    expect(getByText('Success Badge')).toBeInTheDocument();
  });

  test('renders with different variants', () => {
    const { getByText } = render(<Badge variant="solid" color="error">Error Badge</Badge>);
    expect(getByText('Error Badge')).toBeInTheDocument();
  });

  test('renders with different sizes', () => {
    const { getByText } = render(<Badge size="sm">Small Badge</Badge>);
    expect(getByText('Small Badge')).toBeInTheDocument();
  });

  test('renders with default props', () => {
    const { getByText } = render(<Badge>Default</Badge>);
    expect(getByText('Default')).toBeInTheDocument();
  });
});
