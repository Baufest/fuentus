import { Velocidad } from '../types/velocidad';

const API_BASE_URL = 'http://localhost:8080/api/fuentus';

export const velocidadApi = {
  /**
   * Importa datos de velocidad desde un archivo CSV
   * @param file Archivo CSV con datos de velocidad
   * @returns Resultado de la importación
   */
  importCSV: async (file: File): Promise<any> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${API_BASE_URL}/velocidad/import`, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      throw new Error(`Error al importar velocidad: ${response.statusText}`);
    }

    return response.json();
  },

  /**
   * Obtiene todos los registros de velocidad
   */
  getAll: async (): Promise<Velocidad[]> => {
    const response = await fetch(`${API_BASE_URL}/velocidad`);
    
    if (!response.ok) {
      throw new Error(`Error al obtener velocidad: ${response.statusText}`);
    }

    return response.json();
  },

  /**
   * Obtiene registros de velocidad filtrados por fecha
   * @param date Fecha en formato yyyy-MM-dd
   */
  getByDate: async (date: string): Promise<Velocidad[]> => {
    const response = await fetch(`${API_BASE_URL}/velocidad/date/${date}`);
    
    if (!response.ok) {
      throw new Error(`Error al obtener velocidad por fecha: ${response.statusText}`);
    }

    return response.json();
  },
};
