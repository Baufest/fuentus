import fuentusapi from './fuentusapi';
import { StatsSummaryDTO, CountSummaryDTO, NucleusCoverageStatsSummary, UuaaSummaryDTO, ServiceSummaryDTO } from '../types/statsSummary';

//import { UuaaSummaryDTO, ServiceSummaryDTO } from '../types/statsSummary';

export const getStatsSummaryByFilters = async (
  area?: string,
  orgN1?: string | string[],
  orgN2fabrica?: string | string[],
  page: number = 0
): Promise<StatsSummaryDTO[]> => {
  try {
    const params = new URLSearchParams();
    
    if (area) {
      params.append('area', area);
    }
    
    if (orgN1) {
      if (Array.isArray(orgN1)) {
        orgN1.forEach(value => params.append('orgN1', value));
      } else {
        params.append('orgN1', orgN1);
      }
    }
    
    if (orgN2fabrica) {
      if (Array.isArray(orgN2fabrica)) {
        orgN2fabrica.forEach(value => params.append('orgN2fabrica', value));
      } else {
        params.append('orgN2fabrica', orgN2fabrica);
      }
    }
    
    params.append('page', page.toString());
    
    const response = await fuentusapi.get<StatsSummaryDTO[]>(`stats-summary/filters?${params.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching stats summary:', error);
    throw error;
  }
};

export const getCountSummaryByFilters = async (
  vertical?: string,
  fabrica?: string | string[],
  sn1?: string | string[],
  sn2?: string | string[],
  uuaa?: string | string[]
): Promise<CountSummaryDTO> => {
  try {
    const params = new URLSearchParams();
    
    if (vertical) {
      params.append('vertical', vertical);
    }
    
    if (fabrica) {
      if (Array.isArray(fabrica)) {
        fabrica.forEach(value => params.append('fabrica', value));
      } else {
        params.append('fabrica', fabrica);
      }
    }
    
    if (sn1) {
      if (Array.isArray(sn1)) {
        sn1.forEach(value => params.append('sn1', value));
      } else {
        params.append('sn1', sn1);
      }
    }
    
    if (sn2) {
      if (Array.isArray(sn2)) {
        sn2.forEach(value => params.append('sn2', value));
      } else {
        params.append('sn2', sn2);
      }
    }
    
    if (uuaa) {
      if (Array.isArray(uuaa)) {
        uuaa.forEach(value => params.append('uuaa', value));
      } else {
        params.append('uuaa', uuaa);
      }
    }
    
    const response = await fuentusapi.get<CountSummaryDTO>(`stats-summary/count?${params.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching count summary:', error);
    throw error;
  }
};

export const getCoverageAverageByLevel = async (
  vertical?: string,
  uol2?: string,
  sn1?: string,
  sn2?: string,
  uuaa?: string
): Promise<NucleusCoverageStatsSummary[]> => {
  try {
    const params = new URLSearchParams();
    
    if (vertical) {
      params.append('vertical', vertical);
    }
    
    if (uol2) {
      params.append('uol2', uol2);
    }
    
    if (sn1) {
      params.append('sn1', sn1);
    }
    
    if (sn2) {
      params.append('sn2', sn2);
    }
    
    if (uuaa) {
      params.append('uuaa', uuaa);
    }
    
    const response = await fuentusapi.get<NucleusCoverageStatsSummary[]>(`stats-summary/coverage-average?${params.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching coverage average by level:', error);
    throw error;
  }
};

export const getUuaaSummary = async (uuaa: string): Promise<UuaaSummaryDTO> => {
  try {
    const response = await fuentusapi.get<UuaaSummaryDTO>(`apps/${uuaa}/summary`);
    return response.data;
  } catch (error) {
    console.error('Error fetching UUAA summary:', error);
    throw error;
  }
};

export const getServiceSummary = async (
  uuaas: string[],
  serviceN1: string,
  serviceN2: string,
  ownerServiceN1: string,
  rfoId?: number,
  rfoEstado?: string
): Promise<ServiceSummaryDTO> => {
  try {
    const params = new URLSearchParams();
    
    uuaas.forEach(uuaa => params.append('uuaas', uuaa));
    params.append('serviceN1', serviceN1);
    params.append('serviceN2', serviceN2);
    params.append('ownerServiceN1', ownerServiceN1);
    
    if (rfoId !== undefined && rfoId !== null) {
      params.append('rfoId', rfoId.toString());
    }
    
    if (rfoEstado) {
      params.append('rfoEstado', rfoEstado);
    }
    
    const response = await fuentusapi.get<ServiceSummaryDTO>(`apps/service/summary?${params.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching service summary:', error);
    throw error;
  }
};

export const getAppsByMultipleUUAAs = async (
  uuaas: string[],
  page: number = 0,
  search: string = ''
): Promise<any> => {
  try {
    const params = new URLSearchParams();
    
    uuaas.forEach(uuaa => params.append('uuaas', uuaa));
    params.append('page', page.toString());
    
    if (search) {
      params.append('search', search);
    }
    
    const response = await fuentusapi.get(`apps/by-uuaas?${params.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching apps by UUAAs:', error);
    throw error;
  }
};

export const getServiceSummaryById = async (serviceId: number): Promise<ServiceSummaryDTO> => {
  try {
    const response = await fuentusapi.get<ServiceSummaryDTO>(`apps/service/${serviceId}/summary`);
    return response.data;
  } catch (error) {
    console.error('Error fetching service summary by ID:', error);
    throw error;
  }
};

export const getAppsByServiceId = async (
  serviceId: number,
  page: number = 0,
  search: string = ''
): Promise<any> => {
  try {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    
    if (search) {
      params.append('search', search);
    }
    
    const response = await fuentusapi.get(`apps/service/${serviceId}/apps?${params.toString()}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching apps by service ID:', error);
    throw error;
  }
};

export const getNucleusServiceById = async (serviceId: number): Promise<any> => {
  try {
    const response = await fuentusapi.get(`nucleus/service/${serviceId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching nucleus service by ID:', error);
    throw error;
  }
};

export interface ProductividadDTO {
  id: number;
  nucleusId: number;
  servicioN2: string;
  features: number;
  ftesDirectos: number;
  ftesIndirectos: number;
  fecha: string;
}

export interface VelocidadDTO {
  id: number;
  nucleusId: number;
  servicioN2: string;
  lt: number;
  ct: number;
  date: string;
}

export const getProductividadByServiceId = async (serviceId: number): Promise<ProductividadDTO[]> => {
  try {
    const response = await fuentusapi.get<ProductividadDTO[]>(`productividad/service/${serviceId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching productividad by service ID:', error);
    throw error;
  }
};

export const getVelocidadByServiceId = async (serviceId: number): Promise<VelocidadDTO[]> => {
  try {
    const response = await fuentusapi.get<VelocidadDTO[]>(`velocidad/service/${serviceId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching velocidad by service ID:', error);
    throw error;
  }
};
