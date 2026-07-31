import { Productividad } from '../types/productividad';

const API_BASE_URL = 'http://localhost:8080/api/fuentus';

export const productividadApi = {
  /**
   * Importa datos de productividad desde un archivo CSV
   * @param file Archivo CSV con datos de productividad
   * @param fecha Fecha que se asignará a todos los registros (formato: yyyy-MM-dd)
   * @returns Resultado de la importación
   */
  importCSV: async (file: File, fecha: string): Promise<any> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fecha', fecha);

    const response = await fetch(`${API_BASE_URL}/productividad/import`, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      throw new Error(`Error al importar productividad: ${response.statusText}`);
    }

    return response.json();
  },

  /**
   * Obtiene todos los registros de productividad
   */
  getAll: async (): Promise<Productividad[]> => {
    const response = await fetch(`${API_BASE_URL}/productividad`);
    
    if (!response.ok) {
      throw new Error(`Error al obtener productividad: ${response.statusText}`);
    }

    return response.json();
  },

  /**
   * Obtiene registros de productividad filtrados por fecha
   * @param fecha Fecha en formato yyyy-MM-dd
   */
  getByFecha: async (fecha: string): Promise<Productividad[]> => {
    const response = await fetch(`${API_BASE_URL}/productividad/fecha/${fecha}`);
    
    if (!response.ok) {
      throw new Error(`Error al obtener productividad por fecha: ${response.statusText}`);
    }

    return response.json();
  },
};
