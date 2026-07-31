import { useEffect, useState } from 'react';
import { getComboValues, ComboValuesParams } from '../api/nucleusApi';
import { ComboValuesDTO } from '../types/nucleus';

export interface UseComboValuesResult {
  data: ComboValuesDTO | null;
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export const useComboValues = (params?: ComboValuesParams): UseComboValuesResult => {
  const [data, setData] = useState<ComboValuesDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchComboValues = async () => {
    try {
      setLoading(true);
      setError(null);
      const result = await getComboValues(params);
      setData(result);
      console.log('Fetched combo values with params:', params, 'result:', result);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error fetching combo values');
      console.error('Error fetching combo values:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchComboValues();
  }, [params?.vertical, params?.uol2, params?.sn1]);

  const refetch = () => {
    fetchComboValues();
  };

  return {
    data,
    loading,
    error,
    refetch
  };
};