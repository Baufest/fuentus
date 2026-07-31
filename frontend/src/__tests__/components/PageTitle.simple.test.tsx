import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import PageTitle from '../../components/PageTitle';

// Mock useLocation
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => ({
    pathname: '/test'
  })
}));

describe('PageTitle', () => {
  beforeEach(() => {
    // Reset document title before each test
    document.title = '';
  });

  test('sets document title', () => {
    render(
      <BrowserRouter>
        <PageTitle title="Test Title" />
      </BrowserRouter>
    );
    expect(document.title).toBe('Test Title');
  });

  test('updates document title when title changes', () => {
    const { rerender } = render(
      <BrowserRouter>
        <PageTitle title="First Title" />
      </BrowserRouter>
    );
    expect(document.title).toBe('First Title');

    rerender(
      <BrowserRouter>
        <PageTitle title="Second Title" />
      </BrowserRouter>
    );
    expect(document.title).toBe('Second Title');
  });

  test('renders nothing', () => {
    const { container } = render(
      <BrowserRouter>
        <PageTitle title="Test" />
      </BrowserRouter>
    );
    expect(container.firstChild).toBeNull();
  });
});
