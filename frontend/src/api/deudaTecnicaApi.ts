import fuentusapi from './fuentusapi';
import { DeudaTecnicaItem, DeudaTecnicaFilters } from '../hooks/useDeudaTecnica';

export const fetchDeudaTecnica = async (filters: DeudaTecnicaFilters): Promise<DeudaTecnicaItem[]> => {
  const params = new URLSearchParams();
  
  if (filters.vertical) params.append('vertical', filters.vertical);
  if (filters.fabrica) params.append('fabrica', filters.fabrica);
  if (filters.sn1) params.append('sn1', filters.sn1);

  const response = await fuentusapi.get('/deuda-tecnica', { params });
  return response.data;
};
