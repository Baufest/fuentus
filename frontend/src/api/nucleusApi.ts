import fuentusapi from './fuentusapi';
import { ComboValuesDTO } from '../types/nucleus';

export interface ComboValuesParams {
  vertical?: string;
  uol2?: string;
  sn1?: string;
}

export const getComboValues = async (params?: ComboValuesParams): Promise<ComboValuesDTO> => {
  try {
    const urlParams = new URLSearchParams();
    if (params?.vertical && params.vertical.trim() !== '') {
      urlParams.append('vertical', params.vertical);
    }
    if (params?.uol2 && params.uol2.trim() !== '') {
      urlParams.append('uol2', params.uol2);
    }
    if (params?.sn1 && params.sn1.trim() !== '') {
      urlParams.append('sn1', params.sn1);
    }
  
    const url = urlParams.toString() ? `nucleus/combo-values?${urlParams.toString()}` : 'nucleus/combo-values';
    const response = await fuentusapi.get<ComboValuesDTO>(url);
    return response.data;
  } catch (error) {
    console.error('Error fetching combo values:', error);
    throw error;
  }
};