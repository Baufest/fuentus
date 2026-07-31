import { useEffect, useState } from 'react';
import fuentusapi from '../api/fuentusapi';
import { GoalDTO, GoalCategoryDTO } from '../types/nucleus';

export interface UseGoalsResult {
  goals: GoalDTO[];
  groupedGoals: GoalCategoryDTO[];
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export const useGoals = (): UseGoalsResult => {
  const [goals, setGoals] = useState<GoalDTO[]>([]);
  const [groupedGoals, setGroupedGoals] = useState<GoalCategoryDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchGoals = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Fetch grouped goals
      const response = await fuentusapi.get<GoalCategoryDTO[]>('/goals/grouped');
      setGroupedGoals(response.data);
      
      // Flatten goals for simple access
      const allGoals = response.data.flatMap(category => category.goals);
      setGoals(allGoals);
      
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error fetching goals');
      console.error('Error fetching goals:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchGoals();
  }, []);

  const refetch = () => {
    fetchGoals();
  };

  return {
    goals,
    groupedGoals,
    loading,
    error,
    refetch
  };
};
