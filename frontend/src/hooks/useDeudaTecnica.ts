import { useState, useEffect } from 'react';
import { fetchDeudaTecnica } from '../api/deudaTecnicaApi';

export interface DeudaTecnicaItem {
  label: string;
  nivelTipo: string;
  totalBugs: number;
  totalVulnerabilities: number;
  totalCodeSmells: number;
  totalApps: number;
}

export interface DeudaTecnicaFilters {
  vertical?: string;
  fabrica?: string;
  sn1?: string;
}

export const useDeudaTecnica = (filters: DeudaTecnicaFilters) => {
  const [data, setData] = useState<DeudaTecnicaItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      setError(null);
      try {
        const result = await fetchDeudaTecnica(filters);
        setData(result);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Error al cargar datos de deuda técnica');
        setData([]);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [filters.vertical, filters.fabrica, filters.sn1]);

  return { data, loading, error };
};
