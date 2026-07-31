import { useState } from 'react';
import fuentusapi from '../api/fuentusapi';

export interface RankingItem {
  posicion: number;
  nombre: string;
  valor: number | null;
}

export interface RankingData {
  nivelTipo: string;
  metricaTipo: string;
  ranking: RankingItem[];
}

interface UseRankingParams {
  nivelTipo: string;
  metricaTipo: string;
  vertical?: string;
  fabrica?: string;
  sn1?: string;
  sn2?: string;
}

export const useRanking = () => {
  const [data, setData] = useState<RankingData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchRanking = async (params: UseRankingParams) => {
    try {
      setLoading(true);
      setError(null);

      const queryParams = new URLSearchParams();
      queryParams.append('nivelTipo', params.nivelTipo);
      queryParams.append('metricaTipo', params.metricaTipo);
      
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

      const response = await fuentusapi.get(`/api/totalizadores/ranking?${queryParams.toString()}`);
      setData(response.data);
      return response.data;
    } catch (err) {
      console.error('Error fetching ranking:', err);
      setError(err as Error);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { data, loading, error, fetchRanking };
};
