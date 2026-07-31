import { useState, useEffect, useCallback } from 'react';
import { UuaaSummaryDTO } from '../types/statsSummary';
import { getUuaaSummary } from '../api/statsSummaryApi';

interface UseUuaaSummaryResult {
  summary: UuaaSummaryDTO | null;
  loading: boolean;
  error: Error | null;
  refetch: () => void;
}

export const useUuaaSummary = (uuaa: string | undefined): UseUuaaSummaryResult => {
  const [summary, setSummary] = useState<UuaaSummaryDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchSummary = useCallback(async () => {
    if (!uuaa) {
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const normalizedUuaa = uuaa.substring(0, 4).toUpperCase();
      const data = await getUuaaSummary(normalizedUuaa);
      setSummary(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('Error fetching UUAA summary'));
      setSummary(null);
    } finally {
      setLoading(false);
    }
  }, [uuaa]);

  useEffect(() => {
    fetchSummary();
  }, [fetchSummary]);

  const handleRefetch = useCallback(() => {
    fetchSummary();
  }, [fetchSummary]);

  return { summary, loading, error, refetch: handleRefetch };
};
