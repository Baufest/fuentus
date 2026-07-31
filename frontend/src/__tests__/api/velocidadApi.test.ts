import { velocidadApi } from '../../api/velocidadApi';

// Mock fetch
global.fetch = jest.fn();

describe('velocidadApi', () => {
  beforeEach(() => {
    (global.fetch as jest.Mock).mockClear();
  });

  describe('importCSV', () => {
    test('sends CSV file without fecha parameter', async () => {
      const mockResponse = {
        success: true,
        messages: ['Importación exitosa: 15 registros procesados']
      };

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockResponse
      });

      const file = new File(['test,data'], 'velocidad.csv', { type: 'text/csv' });

      const result = await velocidadApi.importCSV(file);

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/velocidad/import',
        expect.objectContaining({
          method: 'POST',
          body: expect.any(FormData)
        })
      );

      expect(result).toEqual(mockResponse);
    });

    test('includes only file in FormData (no fecha)', async () => {
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
      await velocidadApi.importCSV(file);

      expect(capturedFormData).toBeInstanceOf(FormData);
      expect(capturedFormData?.get('file')).toBeInstanceOf(File);
      expect(capturedFormData?.get('fecha')).toBeNull(); // No fecha parameter
    });

    test('handles server error response', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 500,
        statusText: 'Internal Server Error',
        json: async () => ({ message: 'Database error' })
      });

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });

      await expect(velocidadApi.importCSV(file)).rejects.toThrow();
    });

    test('handles network error', async () => {
      (global.fetch as jest.Mock).mockRejectedValue(new Error('Network error'));

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });

      await expect(velocidadApi.importCSV(file)).rejects.toThrow('Network error');
    });

    test('handles partial success with date extraction errors', async () => {
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

      const file = new File(['test'], 'test.csv', { type: 'text/csv' });
      const result = await velocidadApi.importCSV(file);

      expect(result.success).toBe(false);
      expect(result.messages).toHaveLength(2);
      expect(result.messages[1]).toContain('fecha válida');
    });

    test('handles empty file', async () => {
      const mockResponse = {
        success: false,
        message: 'El archivo está vacío'
      };

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 400,
        json: async () => mockResponse
      });

      const file = new File([''], 'empty.csv', { type: 'text/csv' });

      await expect(velocidadApi.importCSV(file)).rejects.toThrow();
    });
  });

  describe('getAll', () => {
    test('fetches all velocidad records', async () => {
      const mockData = [
        {
          id: 1,
          nucleusId: 100,
          lt: 203,
          ct: 173,
          date: '2025-01-15'
        },
        {
          id: 2,
          nucleusId: 101,
          lt: 180,
          ct: 150,
          date: '2025-01-14'
        }
      ];

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockData
      });

      const result = await velocidadApi.getAll();

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/velocidad'
      );
      expect(result).toEqual(mockData);
      expect(result).toHaveLength(2);
    });

    test('returns empty array when no records exist', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => []
      });

      const result = await velocidadApi.getAll();

      expect(result).toEqual([]);
    });

    test('handles server error', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 500
      });

      await expect(velocidadApi.getAll()).rejects.toThrow();
    });

    test('verifies correct data structure', async () => {
      const mockData = [
        {
          id: 1,
          nucleusId: 100,
          lt: 203,
          ct: 173,
          date: '2025-01-15'
        }
      ];

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockData
      });

      const result = await velocidadApi.getAll();

      expect(result[0]).toHaveProperty('id');
      expect(result[0]).toHaveProperty('lt');
      expect(result[0]).toHaveProperty('ct');
      expect(result[0]).toHaveProperty('date');
    });
  });

  describe('getByDate', () => {
    test('fetches velocidad records by specific date', async () => {
      const mockData = [
        {
          id: 1,
          nucleusId: 100,
          lt: 203,
          ct: 173,
          date: '2025-01-15'
        }
      ];

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockData
      });

      const result = await velocidadApi.getByDate('2025-01-15');

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/velocidad/date/2025-01-15'
      );
      expect(result).toEqual(mockData);
      expect(result[0].date).toBe('2025-01-15');
    });

    test('formats date parameter correctly', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => []
      });

      await velocidadApi.getByDate('2025-12-30');

      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/fuentus/velocidad/date/2025-12-30'
      );
    });

    test('returns empty array when no records for date', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => []
      });

      const result = await velocidadApi.getByDate('2025-01-01');

      expect(result).toEqual([]);
    });

    test('handles server error', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 404
      });

      await expect(velocidadApi.getByDate('2025-01-15')).rejects.toThrow();
    });

    test('handles multiple records for same date', async () => {
      const mockData = [
        {
          id: 1,
          nucleusId: 100,
          lt: 203,
          ct: 173,
          date: '2025-01-15'
        },
        {
          id: 2,
          nucleusId: 101,
          lt: 180,
          ct: 150,
          date: '2025-01-15'
        }
      ];

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: async () => mockData
      });

      const result = await velocidadApi.getByDate('2025-01-15');

      expect(result).toHaveLength(2);
      expect(result.every(r => r.date === '2025-01-15')).toBe(true);
    });
  });
});
