import { useState, useEffect, useCallback } from 'react';
import { getServiceSummary, getAppsByMultipleUUAAs, getServiceSummaryById, getAppsByServiceId } from '../api/statsSummaryApi';
import { ServiceSummaryDTO } from '../types/statsSummary';
import { Apps as App } from '../types/app';

interface ServiceAppsResponse {
  content: App[];
  totalElements: number;
  totalPages: number;
  number: number;
}

interface UseServiceSummaryReturn {
  summary: ServiceSummaryDTO | null;
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

interface UseServiceAppsReturn {
  apps: App[];
  loading: boolean;
  error: string | null;
  totalElements: number;
  totalPages: number;
  currentPage: number;
  refetch: () => void;
  setPage: (page: number) => void;
  setSearch: (search: string) => void;
}

export const useServiceSummary = (
  uuaas: string[],
  serviceN1: string,
  serviceN2: string,
  ownerServiceN1: string,
  rfoId?: number,
  rfoEstado?: string
): UseServiceSummaryReturn => {
  const [summary, setSummary] = useState<ServiceSummaryDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchSummary = useCallback(async () => {
    if (!uuaas || uuaas.length === 0) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data = await getServiceSummary(uuaas, serviceN1, serviceN2, ownerServiceN1, rfoId, rfoEstado);
      setSummary(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al obtener el resumen del servicio');
    } finally {
      setLoading(false);
    }
  }, [uuaas, serviceN1, serviceN2, ownerServiceN1, rfoId, rfoEstado]);

  useEffect(() => {
    fetchSummary();
  }, [fetchSummary]);

  return { summary, loading, error, refetch: fetchSummary };
};

export const useServiceApps = (
  uuaas: string[],
  initialPage: number = 0,
  initialSearch: string = ''
): UseServiceAppsReturn => {
  const [apps, setApps] = useState<App[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [totalElements, setTotalElements] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [currentPage, setCurrentPage] = useState<number>(initialPage);
  const [search, setSearch] = useState<string>(initialSearch);

  const fetchApps = useCallback(async () => {
    if (!uuaas || uuaas.length === 0) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data: ServiceAppsResponse = await getAppsByMultipleUUAAs(uuaas, currentPage, search);
      setApps(data.content);
      setTotalElements(data.totalElements);
      setTotalPages(data.totalPages);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al obtener las aplicaciones');
    } finally {
      setLoading(false);
    }
  }, [uuaas, currentPage, search]);

  useEffect(() => {
    fetchApps();
  }, [fetchApps]);

  const setPage = (page: number) => {
    setCurrentPage(page);
  };

  const handleSetSearch = (newSearch: string) => {
    setSearch(newSearch);
    setCurrentPage(0); // Reset to first page when searching
  };

  return {
    apps,
    loading,
    error,
    totalElements,
    totalPages,
    currentPage,
    refetch: fetchApps,
    setPage,
    setSearch: handleSetSearch,
  };
};

/**
 * Hook para obtener el resumen de un servicio por su ID
 */
export const useServiceSummaryById = (serviceId: number | null): UseServiceSummaryReturn => {
  const [summary, setSummary] = useState<ServiceSummaryDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchSummary = useCallback(async () => {
    if (!serviceId) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data = await getServiceSummaryById(serviceId);
      setSummary(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al obtener el resumen del servicio');
    } finally {
      setLoading(false);
    }
  }, [serviceId]);

  useEffect(() => {
    fetchSummary();
  }, [fetchSummary]);

  return { summary, loading, error, refetch: fetchSummary };
};

/**
 * Hook para obtener las apps de un servicio por su ID con paginación y búsqueda
 */
export const useServiceAppsById = (
  serviceId: number | null,
  initialPage: number = 0,
  initialSearch: string = ''
): UseServiceAppsReturn => {
  const [apps, setApps] = useState<App[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [totalElements, setTotalElements] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [currentPage, setCurrentPage] = useState<number>(initialPage);
  const [search, setSearch] = useState<string>(initialSearch);

  const fetchApps = useCallback(async () => {
    if (!serviceId) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const data: ServiceAppsResponse = await getAppsByServiceId(serviceId, currentPage, search);
      setApps(data.content);
      setTotalElements(data.totalElements);
      setTotalPages(data.totalPages);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al obtener las aplicaciones');
    } finally {
      setLoading(false);
    }
  }, [serviceId, currentPage, search]);

  useEffect(() => {
    fetchApps();
  }, [fetchApps]);

  const setPage = (page: number) => {
    setCurrentPage(page);
  };

  const handleSetSearch = (newSearch: string) => {
    setSearch(newSearch);
    setCurrentPage(0);
  };

  return {
    apps,
    loading,
    error,
    totalElements,
    totalPages,
    currentPage,
    refetch: fetchApps,
    setPage,
    setSearch: handleSetSearch,
  };
};
