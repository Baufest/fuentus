import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import PageTitle from '../components/PageTitle';

/**
 * Test para el componente PageTitle
 */
describe('PageTitle Component', () => {
  test('actualiza el título del documento', () => {
    const testTitle = 'Test Page Title';
    
    render(
      <BrowserRouter>
        <PageTitle title={testTitle} />
      </BrowserRouter>
    );
    
    expect(document.title).toBe(testTitle);
  });

  test('no renderiza contenido visible', () => {
    const { container } = render(
      <BrowserRouter>
        <PageTitle title="Test" />
      </BrowserRouter>
    );
    
    expect(container.firstChild).toBeNull();
  });
});
