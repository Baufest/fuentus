import { useState, useEffect } from 'react';
import fuentusapi from '../api/fuentusapi';

export interface TopLevelMetrics {
  nivelTipo: string; // VERTICAL, FABRICA, SN1, SN2
  metricaTipo: string; // PRODUCTIVIDAD, LT, CT
  nombre: string;
  valor: number | null;
}

export interface TotalizadoresData {
  mejoresNiveles: TopLevelMetrics[];
}

interface UseTotalizadoresParams {
  vertical?: string;
  fabrica?: string[];
  sn1?: string[];
  sn2?: string[];
  enabled?: boolean;
}

export const useTotalizadores = (params: UseTotalizadoresParams) => {
  const [data, setData] = useState<TotalizadoresData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    if (params.enabled === false) {
      return;
    }

    const fetchTotalizadores = async () => {
      try {
        setLoading(true);
        setError(null);

        const queryParams = new URLSearchParams();
        
        if (params.vertical) {
          queryParams.append('vertical', params.vertical);
        }
        if (params.fabrica && params.fabrica.length > 0) {
          queryParams.append('fabrica', params.fabrica[0]); // Tomar el primero
        }
        if (params.sn1 && params.sn1.length > 0) {
          queryParams.append('sn1', params.sn1[0]); // Tomar el primero
        }
        if (params.sn2 && params.sn2.length > 0) {
          queryParams.append('sn2', params.sn2[0]); // Tomar el primero
        }

        const response = await fuentusapi.get(`/api/totalizadores?${queryParams.toString()}`);
        setData(response.data);
      } catch (err) {
        console.error('Error fetching totalizadores:', err);
        setError(err as Error);
      } finally {
        setLoading(false);
      }
    };

    fetchTotalizadores();
  }, [params.vertical, JSON.stringify(params.fabrica), JSON.stringify(params.sn1), JSON.stringify(params.sn2), params.enabled]);

  return { data, loading, error };
};
