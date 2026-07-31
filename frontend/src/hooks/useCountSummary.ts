import { useState, useEffect } from 'react';
import { getCountSummaryByFilters } from '../api/statsSummaryApi';
import { CountSummaryDTO } from '../types/statsSummary';

interface UseCountSummaryProps {
  vertical?: string;
  fabrica?: string[];
  sn1?: string[];
  sn2?: string[];
  uuaa?: string[];
  enabled?: boolean; // To control when to fetch
}

interface UseCountSummaryReturn {
  countSummary: CountSummaryDTO | null;
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export const useCountSummary = ({ 
  vertical, 
  fabrica, 
  sn1,
  sn2,
  uuaa,
  enabled = true 
}: UseCountSummaryProps): UseCountSummaryReturn => {
  const [countSummary, setCountSummary] = useState<CountSummaryDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchCountSummary = async () => {
    if (!enabled) return;
    
    setLoading(true);
    setError(null);
    
    try {
      const result = await getCountSummaryByFilters(vertical, fabrica, sn1, sn2, uuaa);
      setCountSummary(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error fetching count summary');
      console.error('Error fetching count summary:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCountSummary();
  }, [vertical, fabrica, sn1, sn2, uuaa, enabled]);

  const refetch = () => {
    fetchCountSummary();
  };

  return {
    countSummary,
    loading,
    error,
    refetch
  };
};

export default useCountSummary;