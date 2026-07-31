import { productividadApi } from '../../api/productividadApi';

// Mock fetch
global.fetch = jest.fn();

describe('productividadApi', () => {
  beforeEach(() => {
    (global.fetch as jest.Mock).mockClear();
  });

  describe('importCSV', () => {
    test('sends CSV file with fecha parameter successfully', async () => {
      const mockResponse = {
        success: true,
        messages: ['Importación exitosa: 10 registros procesados']
      };

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockResponse
      });

      const file = new File(['test,data'], 'productividad.csv', { type: 'text/csv' });
      const fecha = '2025-12-30';

      const result = await productividadApi.importCSV(file, fecha);

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/productividad/import',
        expect.objectContaining({
          method: 'POST',
          body: expect.any(FormData)
        })
      );

      expect(result).toEqual(mockResponse);
    });

    test('includes fecha in FormData', async () => {
      const mockResponse = { success: true, messages: [] };
      let capturedFormData: any = null;

      (global.fetch as jest.Mock).mockImplementation((_url: string, options: any) => {
        capturedFormData = options.body;
        return Promise.resolve({
          ok: true,
          json: async () => mockResponse
        });
      });

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });
      await productividadApi.importCSV(file, '2025-01-15');

      expect(capturedFormData).toBeInstanceOf(FormData);
      expect(capturedFormData?.get('fecha')).toBe('2025-01-15');
      expect(capturedFormData?.get('file')).toBeInstanceOf(File);
    });

    test('handles server error response', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 500,
        statusText: 'Internal Server Error',
        json: async () => ({ message: 'Database error' })
      });

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });

      await expect(productividadApi.importCSV(file, '2025-12-30')).rejects.toThrow();
    });

    test('handles network error', async () => {
      (global.fetch as jest.Mock).mockRejectedValue(new Error('Network error'));

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });

      await expect(productividadApi.importCSV(file, '2025-12-30')).rejects.toThrow('Network error');
    });

    test('handles partial success response', async () => {
      const mockResponse = {
        success: false,
        messages: [
          'Importación parcial: 8 exitosos, 2 errores',
          'Error en fila 5: datos inválidos'
        ]
      };

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockResponse
      });

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });
      const result = await productividadApi.importCSV(file, '2025-12-30');

      expect(result.success).toBe(false);
      expect(result.messages).toHaveLength(2);
    });
  });

  describe('getAll', () => {
    test('fetches all productividad records', async () => {
      const mockData = [
        {
          id: 1,
          nucleusId: 100,
          features: 11,
          ftesDirectos: 10.58,
          ftesIndirectos: 4.90,
          fecha: '2025-12-30'
        },
        {
          id: 2,
          nucleusId: 101,
          features: 8,
          ftesDirectos: 7.25,
          ftesIndirectos: 3.15,
          fecha: '2025-12-30'
        }
      ];

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockData
      });

      const result = await productividadApi.getAll();

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/productividad'
      );
      expect(result).toEqual(mockData);
      expect(result).toHaveLength(2);
    });

    test('returns empty array when no records exist', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => []
      });

      const result = await productividadApi.getAll();

      expect(result).toEqual([]);
    });

    test('handles server error', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 500
      });

      await expect(productividadApi.getAll()).rejects.toThrow();
    });
  });

  describe('getByFecha', () => {
    test('fetches productividad records by specific date', async () => {
      const mockData = [
        {
          id: 1,
          nucleusId: 100,
          features: 11,
          ftesDirectos: 10.58,
          ftesIndirectos: 4.90,
          fecha: '2025-12-30'
        }
      ];

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockData
      });

      const result = await productividadApi.getByFecha('2025-12-30');

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/productividad/fecha/2025-12-30'
      );
      expect(result).toEqual(mockData);
      expect(result[0].fecha).toBe('2025-12-30');
    });

    test('formats date parameter correctly', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => []
      });

      await productividadApi.getByFecha('2025-01-15');

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/productividad/fecha/2025-01-15'
      );
    });

    test('returns empty array when no records for date', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => []
      });

      const result = await productividadApi.getByFecha('2025-01-01');

      expect(result).toEqual([]);
    });

    test('handles server error', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 404
      });

      await expect(productividadApi.getByFecha('2025-12-30')).rejects.toThrow();
    });
  });
});
