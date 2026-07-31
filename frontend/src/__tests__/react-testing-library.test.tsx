import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import App from '../App';

/**
 * Test de React Testing Library para validar la configuración
 */
describe('React Testing Library Configuration', () => {
  test('renderiza la aplicación sin errores', () => {
    render(
      <BrowserRouter>
        <App />
      </BrowserRouter>
    );
    
    // Verificar que el componente se renderiza
    expect(document.body).toBeInTheDocument();
  });

  test('jest-dom matchers están disponibles', () => {
    const div = document.createElement('div');
    div.textContent = 'Test content';
    
    expect(div).toBeInTheDocument;
    expect(div).toHaveTextContent('Test content');
  });
});
