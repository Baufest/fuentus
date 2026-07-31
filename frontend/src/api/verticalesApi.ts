import fuentusapi from './fuentusapi';
import { VerticalResponseDto, VerticalRequestDto, VerticalUpdateDto, OrgN2FabricaResponseDto } from '../types/vertical';

export const getAllVerticalesWithOrgN2Fabrica = async (): Promise<VerticalResponseDto[]> => {
  try {
    const response = await fuentusapi.get<VerticalResponseDto[]>('verticales');
    return response.data;
  } catch (error) {
    console.error('Error fetching verticales:', error);
    throw error;
  }
};

export const createVertical = async (verticalData: VerticalRequestDto): Promise<VerticalResponseDto> => {
  try {
    const response = await fuentusapi.post<VerticalResponseDto>('verticales', verticalData);
    return response.data;
  } catch (error) {
    console.error('Error creating vertical:', error);
    throw error;
  }
};

export const updateVertical = async (id: number, verticalData: VerticalUpdateDto): Promise<VerticalResponseDto> => {
  try {
    const response = await fuentusapi.put<VerticalResponseDto>(`verticales/${id}`, verticalData);
    return response.data;
  } catch (error) {
    console.error('Error updating vertical:', error);
    throw error;
  }
};

export const getAllOrgN2FabricasWithVerticales = async (): Promise<OrgN2FabricaResponseDto[]> => {
  try {
    const response = await fuentusapi.get<OrgN2FabricaResponseDto[]>('verticales/factories');
    return response.data;
  } catch (error) {
    console.error('Error fetching factories:', error);
    throw error;
  }
};
