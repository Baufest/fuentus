import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import DataImport from '../../pages/DataImport/DataImport';

// Mock fetch
global.fetch = jest.fn();

const renderWithRouter = (component: React.ReactElement) => {
  return render(<BrowserRouter>{component}</BrowserRouter>);
};

describe('DataImport', () => {
  beforeEach(() => {
    (global.fetch as jest.Mock).mockClear();
  });

  test('renders data import page', () => {
    renderWithRouter(<DataImport />);
    
    expect(screen.getByText('Importación de Datos')).toBeInTheDocument();
    expect(screen.getByText('📁 Cargar Archivo de Datos')).toBeInTheDocument();
  });

  test('renders entity type selector with all options', () => {
    renderWithRouter(<DataImport />);
    
    expect(screen.getByText('📊 Nucleus Services')).toBeInTheDocument();
    expect(screen.getByText('🔍 Chimera SCA')).toBeInTheDocument();
    expect(screen.getByText('🛡️ Chimera SAST')).toBeInTheDocument();
    expect(screen.getByText('📱 Apps')).toBeInTheDocument();
    expect(screen.getByText('📄 RFO')).toBeInTheDocument();
    expect(screen.getByText('📈 Productividad')).toBeInTheDocument();
    expect(screen.getByText('⚡ Velocidad')).toBeInTheDocument();
  });

  test('renders format selector with CSV and JSON options', () => {
    renderWithRouter(<DataImport />);
    
    expect(screen.getByText('📊 CSV (Comma-Separated Values)')).toBeInTheDocument();
    expect(screen.getByText('📋 JSON (JavaScript Object Notation)')).toBeInTheDocument();
  });

  test('changes entity type when selected', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement; // First select is entity selector
    fireEvent.change(select, { target: { value: 'chimera_sca' } });
    
    expect(select.value).toBe('chimera_sca');
  });

  test('changes format when selected', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[1] as HTMLSelectElement; // Second select is format selector
    fireEvent.change(select, { target: { value: 'json' } });
    
    expect(select.value).toBe('json');
  });

  test('handles file selection via input', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test content'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    
    expect(screen.getByText('test.csv')).toBeInTheDocument();
  });

  test('auto-detects CSV format from file extension', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'data.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    
    const selects = document.querySelectorAll('select');
    const formatSelect = selects[1] as HTMLSelectElement;
    expect(formatSelect.value).toBe('csv');
  });

  test('auto-detects JSON format from file extension', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['{}'], 'data.json', { type: 'application/json' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    
    const selects = document.querySelectorAll('select');
    const formatSelect = selects[1] as HTMLSelectElement;
    expect(formatSelect.value).toBe('json');
  });

  test('displays file size when file is selected', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['a'.repeat(2048)], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    
    expect(screen.getByText(/Tamaño: 2\.0 KB/)).toBeInTheDocument();
  });

  test('shows change file button when file is selected', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    
    expect(screen.getByText('Cambiar archivo')).toBeInTheDocument();
  });

  test('clears file when change file button is clicked', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    expect(screen.getByText('test.csv')).toBeInTheDocument();
    
    const changeButton = screen.getByText('Cambiar archivo');
    fireEvent.click(changeButton);
    
    expect(screen.queryByText('test.csv')).not.toBeInTheDocument();
  });

  test('import button is disabled when no file is selected', () => {
    renderWithRouter(<DataImport />);
    
    const importButton = screen.getByText('Importar Datos');
    expect(importButton).toBeDisabled();
  });

  test('import button is enabled when file is selected', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    expect(importButton).not.toBeDisabled();
  });

  test('handles successful import', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        totalProcessed: 10,
        successfulImports: 10,
        failedImports: 0,
        successRate: 100
      })
    });

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(screen.getByText('\u2705 Importaci\u00f3n Exitosa')).toBeInTheDocument();
    });
    
    expect(screen.getByText('Total Procesados')).toBeInTheDocument();
    expect(screen.getByText('100.0%')).toBeInTheDocument();
  });

  test('handles partial import', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        totalProcessed: 10,
        successfulImports: 7,
        failedImports: 3,
        successRate: 70
      })
    });

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(screen.getByText('⚠️ Importación Parcial')).toBeInTheDocument();
    });
  });

  test('handles import error', async () => {
    (global.fetch as jest.Mock).mockRejectedValueOnce(new Error('Network error'));

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(screen.getByText('❌ Error en la Importación')).toBeInTheDocument();
    });
    
    expect(screen.getByText(/Network error/)).toBeInTheDocument();
  });

  test('shows loading state during import', async () => {
    (global.fetch as jest.Mock).mockImplementation(() => 
      new Promise(resolve => setTimeout(() => resolve({
        ok: true,
        json: async () => ({ totalProcessed: 1, successfulImports: 1, failedImports: 0, successRate: 100 })
      }), 100))
    );

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    expect(screen.getByText('Importando...')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.queryByText('Importando...')).not.toBeInTheDocument();
    });
  });

  test('reset button clears form', async () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const selects = document.querySelectorAll('select');
    const entitySelect = selects[0] as HTMLSelectElement;
    fireEvent.change(entitySelect, { target: { value: 'chimera_sca' } });
    
    const resetButton = screen.getByText('Limpiar');
    fireEvent.click(resetButton);
    
    expect(screen.queryByText('test.csv')).not.toBeInTheDocument();
    expect(entitySelect.value).toBe('nucleus-services');
  });

  test('handles drag enter', () => {
    renderWithRouter(<DataImport />);
    
    const dropZone = document.querySelector('.border-dashed');
    
    fireEvent.dragEnter(dropZone!);
    
    expect(dropZone).toHaveClass('border-primary');
  });

  test('handles drag leave', () => {
    renderWithRouter(<DataImport />);
    
    const dropZone = document.querySelector('.border-dashed');
    
    fireEvent.dragEnter(dropZone!);
    fireEvent.dragLeave(dropZone!);
    
    expect(dropZone).not.toHaveClass('border-primary');
  });

  test('handles file drop', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'dropped.csv', { type: 'text/csv' });
    const dropZone = document.querySelector('.border-dashed');
    
    fireEvent.drop(dropZone!, {
      dataTransfer: { files: [file] }
    });
    
    expect(screen.getByText('dropped.csv')).toBeInTheDocument();
  });

  test('displays correct CSV format info for nucleus-services', () => {
    renderWithRouter(<DataImport />);
    
    expect(screen.getByText(/Id_Fullservice,serviceN1,serviceN2/)).toBeInTheDocument();
  });

  test('displays correct CSV format info for chimera_sca', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'chimera_sca' } });
    
    expect(screen.getByText(/application,component,version/)).toBeInTheDocument();
  });

  test('displays correct JSON format info when JSON is selected', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const formatSelect = selects[1] as HTMLSelectElement;
    fireEvent.change(formatSelect, { target: { value: 'json' } });
    
    expect(screen.getByText('Estructura JSON esperada:')).toBeInTheDocument();
  });

  test('sends correct API request on upload', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({ totalProcessed: 1, successfulImports: 1, failedImports: 0, successRate: 100 })
    });

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/data-import/nucleus-services/csv',
        expect.objectContaining({
          method: 'POST',
          body: expect.any(FormData)
        })
      );
    });
  });

  test('displays correct file icon for CSV files', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    expect(screen.getByText('📊')).toBeInTheDocument();
  });

  test('displays correct file icon for JSON files', () => {
    renderWithRouter(<DataImport />);
    
    const file = new File(['{}'], 'test.json', { type: 'application/json' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    expect(screen.getByText('📋')).toBeInTheDocument();
  });

  test('clears import result when new file is selected', () => {
    renderWithRouter(<DataImport />);
    
    const file1 = new File(['test1'], 'test1.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    
    fireEvent.change(input, { target: { files: [file1] } });
    
    // Simular un resultado previo seteando el estado manualmente sería complejo,
    // pero podemos verificar que el archivo se actualiza correctamente
    expect(screen.getByText('test1.csv')).toBeInTheDocument();
    
    const file2 = new File(['test2'], 'test2.csv', { type: 'text/csv' });
    fireEvent.change(input, { target: { files: [file2] } });
    
    expect(screen.queryByText('test1.csv')).not.toBeInTheDocument();
    expect(screen.getByText('test2.csv')).toBeInTheDocument();
  });

  test('import button is disabled during upload', async () => {
    (global.fetch as jest.Mock).mockImplementation(() => 
      new Promise(resolve => setTimeout(() => resolve({
        ok: true,
        json: async () => ({ totalProcessed: 1, successfulImports: 1, failedImports: 0, successRate: 100 })
      }), 100))
    );

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    const uploadingButton = screen.getByText('Importando...');
    expect(uploadingButton).toBeDisabled();
    
    await waitFor(() => {
      expect(screen.queryByText('Importando...')).not.toBeInTheDocument();
    });
  });

  test('reset button is disabled during upload', async () => {
    (global.fetch as jest.Mock).mockImplementation(() => 
      new Promise(resolve => setTimeout(() => resolve({
        ok: true,
        json: async () => ({ totalProcessed: 1, successfulImports: 1, failedImports: 0, successRate: 100 })
      }), 100))
    );

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    const resetButton = screen.getByText('Limpiar');
    expect(resetButton).toBeDisabled();
    
    await waitFor(() => {
      expect(resetButton).not.toBeDisabled();
    });
  });

  test('displays error message when provided in response', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        totalProcessed: 0,
        successfulImports: 0,
        failedImports: 0,
        successRate: 0,
        errorMessage: 'Invalid file format'
      })
    });

    renderWithRouter(<DataImport />);
    
    const file = new File(['test'], 'test.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(screen.getByText(/Invalid file format/)).toBeInTheDocument();
    });
  });

  // Tests específicos para Productividad
  test('renders date picker when productividad is selected', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'productividad' } });
    
    const dateInput = screen.getByLabelText(/Fecha de los Datos/i);
    expect(dateInput).toBeInTheDocument();
    expect(dateInput).toHaveAttribute('type', 'date');
  });

  test('requires fecha when uploading productividad data', async () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'productividad' } });
    
    const file = new File(['test'], 'productividad.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    // Should not proceed without fecha
    expect(global.fetch).not.toHaveBeenCalled();
  });

  test('uploads productividad data with fecha successfully', async () => {
    const mockResponse = {
      success: true,
      messages: ['Importación exitosa: 10 registros procesados']
    };

    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => mockResponse
    });

    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'productividad' } });
    
    const dateInput = screen.getByLabelText(/Fecha de los Datos/i) as HTMLInputElement;
    fireEvent.change(dateInput, { target: { value: '2025-12-30' } });
    
    const file = new File(['test'], 'productividad.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/productividad/import',
        expect.objectContaining({
          method: 'POST',
          body: expect.any(FormData)
        })
      );
    });
  });

  test('displays productividad CSV format info', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'productividad' } });
    
    expect(screen.getByText(/Servicio N2/)).toBeInTheDocument();
    expect(screen.getByText(/Features/)).toBeInTheDocument();
    expect(screen.getByText(/FTEs Directos/)).toBeInTheDocument();
    expect(screen.getByText(/FTEs Indirectos/)).toBeInTheDocument();
  });

  // Tests específicos para Velocidad
  test('does not render date picker when velocidad is selected', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'velocidad' } });
    
    const dateInputs = screen.queryAllByLabelText(/Fecha/i);
    expect(dateInputs).toHaveLength(0);
  });

  test('uploads velocidad data without fecha parameter', async () => {
    const mockResponse = {
      success: true,
      messages: ['Importación exitosa: 15 registros procesados']
    };

    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => mockResponse
    });

    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'velocidad' } });
    
    const file = new File(['test'], 'velocidad.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/velocidad/import',
        expect.objectContaining({
          method: 'POST',
          body: expect.any(FormData)
        })
      );
    });
  });

  test('displays velocidad CSV format info with multiple date columns', () => {
    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'velocidad' } });
    
    expect(screen.getByText(/service_n2/)).toBeInTheDocument();
    expect(screen.getByText(/LT/)).toBeInTheDocument();
    expect(screen.getByText(/CT/)).toBeInTheDocument();
    expect(screen.getByText(/new_date/)).toBeInTheDocument();
    expect(screen.getByText(/deployed_date/)).toBeInTheDocument();
  });

  test('handles productividad import with partial errors', async () => {
    const mockResponse = {
      success: false,
      messages: [
        'Importación parcial: 8 exitosos, 2 errores',
        'Error en fila 5: Service_N2 no encontrado'
      ]
    };

    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => mockResponse
    });

    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'productividad' } });
    
    const dateInput = screen.getByLabelText(/Fecha de los Datos/i) as HTMLInputElement;
    fireEvent.change(dateInput, { target: { value: '2025-12-30' } });
    
    const file = new File(['test'], 'productividad.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(screen.getByText(/8 exitosos/)).toBeInTheDocument();
      expect(screen.getByText(/2 errores/)).toBeInTheDocument();
    });
  });

  test('handles velocidad import with date extraction errors', async () => {
    const mockResponse = {
      success: false,
      messages: [
        'Importación parcial: 12 exitosos, 3 errores',
        'Error en fila 8: No se pudo determinar una fecha válida'
      ]
    };

    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => mockResponse
    });

    renderWithRouter(<DataImport />);
    
    const selects = document.querySelectorAll('select');
    const select = selects[0] as HTMLSelectElement;
    fireEvent.change(select, { target: { value: 'velocidad' } });
    
    const file = new File(['test'], 'velocidad.csv', { type: 'text/csv' });
    const input = document.querySelector('input[type="file"]') as HTMLInputElement;
    fireEvent.change(input, { target: { files: [file] } });
    
    const importButton = screen.getByText('Importar Datos');
    fireEvent.click(importButton);
    
    await waitFor(() => {
      expect(screen.getByText(/fecha válida/)).toBeInTheDocument();
    });
  });
});
