import { useState, useEffect } from 'react';
import fuentusapi from '../api/fuentusapi';

export interface ItemGrafico {
  nombre: string;
  productividad: number | null;
  promedioLT: number | null;
  promedioCT: number | null;
  totalFeatures: number;
  totalFTEs: number;
}

export interface GraficoNivel {
  nivelTipo: string;
  titulo: string;
  items: ItemGrafico[];
}

export interface ComparativaData {
  nivelFiltrado: string;
  graficos: GraficoNivel[];
}

interface UseComparativaParams {
  vertical?: string;
  fabrica?: string;
  sn1?: string;
  sn2?: string;
  enabled?: boolean;
}

export const useComparativaProductividad = (params: UseComparativaParams) => {
  const [data, setData] = useState<ComparativaData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    if (params.enabled === false) {
      return;
    }

    const fetchComparativa = async () => {
      try {
        setLoading(true);
        setError(null);

        const queryParams = new URLSearchParams();
        
        if (params.vertical) {
          queryParams.append('vertical', params.vertical);
        }
        if (params.fabrica) {
          queryParams.append('fabrica', params.fabrica);
        }
        if (params.sn1) {
          queryParams.append('sn1', params.sn1);
        }
        if (params.sn2) {
          queryParams.append('sn2', params.sn2);
        }

        const response = await fuentusapi.get(`/api/comparativa-productividad?${queryParams.toString()}`);
        setData(response.data);
      } catch (err) {
        console.error('Error fetching comparativa:', err);
        setError(err as Error);
      } finally {
        setLoading(false);
      }
    };

    fetchComparativa();
  }, [params.vertical, params.fabrica, params.sn1, params.sn2, params.enabled]);

  return { data, loading, error };
};
