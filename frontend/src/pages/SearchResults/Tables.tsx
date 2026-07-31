import { useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import fuentusapi from '../../api/fuentusapi';
import TableNucleusServices from '../../components/Tables/TableNucleusServices';
import TableApps, { AppData } from '../../components/Tables/TableApps';
import TableServers, { ServerAppsData } from '../../components/Tables/TableServers';

export type App = {
  serviceId?: number;
  serviceN1: string;
  ownerServiceN1: string;
  serviceN2: string;
  appOwner: string;
  uuaa: string;
  rfoId?: number | null;
  rfoEstado?: string | null;
  verticalName?: string;
  //REL_TYPE: string;
}

type PagedResponse<T> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
}

const Tables = () => {
  const location = useLocation();
  const searchTerm = location.state?.searchValue;
  const vertical = location.state?.vertical;
  const uol2 = location.state?.uol2;
  const sn1 = location.state?.sn1;
  const sn2 = location.state?.sn2;
  const serverName = location.state?.serverName;
  const [data, setData] = useState<App[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [pageLoading, setPageLoading] = useState(false);

  // Estado para la tabla de Apps
  const [appsData, setAppsData] = useState<AppData[]>([]);
  const [appsLoading, setAppsLoading] = useState(true);
  const [appsCurrentPage, setAppsCurrentPage] = useState(0);
  const [appsTotalPages, setAppsTotalPages] = useState(0);
  const [appsTotalElements, setAppsTotalElements] = useState(0);
  const [appsPageLoading, setAppsPageLoading] = useState(false);

  const [serversData, setServersData] = useState<ServerAppsData[]>([]);
  const [serversLoading, setServersLoading] = useState(true);

  useEffect(() => {
    fetchData(0); // Iniciar desde la página 0
    fetchAppsData(0); // Cargar datos de apps
    fetchServersData(); // Cargar servidores con sus apps
  }, [searchTerm, vertical, uol2, sn1, sn2, serverName]);

  const fetchData = async (page: number) => {
    try {
      if (page !== 0) setPageLoading(true);
      else setLoading(true);
      
      let response;
      // Construir URL con todos los filtros disponibles
      const params = new URLSearchParams();
      params.append("page", page.toString());
      
      if (searchTerm) {
        params.append("q", searchTerm);
      }
      if (vertical) {
        params.append("vertical", vertical);
      }
      if (uol2) {
        params.append("uol2", uol2);
      }
      if (sn1) {
        params.append("sn1", sn1);
      }
      if (sn2) {
        params.append("sn2", sn2);
      }

      // Si hay searchTerm o filtros, usar el endpoint de search
      if (searchTerm || vertical || uol2 || sn1 || sn2) {
        response = await fuentusapi.get<PagedResponse<App>>(`nucleus/search?${params.toString()}`);
      } else {
        // Usar filtros legacy (area, orgn1, orgn2) si existen
        const area = location.state?.area;
        const orgn1 = location.state?.orgn1;
        const orgn2 = location.state?.orgn2;
        
        if (area || orgn1 || orgn2) {
          let url = new URLSearchParams();
          if (area) url.append("area", area);
          if (orgn1) url.append("orgN1", orgn1);
          if (orgn2) url.append("orgN2fabrica", orgn2);
          url.append("page", page.toString());

          const queryString = url.toString();
          const endpoint = `nucleus/filters?${queryString}`;
          response = await fuentusapi.get<PagedResponse<App>>(endpoint);
        } else {
          // Sin filtros, hacer búsqueda general
          response = await fuentusapi.get<PagedResponse<App>>(`nucleus/search?page=${page}`);
        }
      }
      
      const data = response.data;
      console.log(data);
      setData(data.content);
      setCurrentPage(data.number);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (error) {
      console.error('Error fetching data:', error);
      setData([]);
      setCurrentPage(0);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
      setPageLoading(false);
    }
  };

  const fetchAppsData = async (page: number) => {
    try {
      if (page !== 0) setAppsPageLoading(true);
      else setAppsLoading(true);
      
      const params = new URLSearchParams();
      params.append("page", page.toString());
      
      if (searchTerm) {
        params.append("q", searchTerm);
      }
      if (vertical) {
        params.append("vertical", vertical);
      }
      if (uol2) {
        params.append("uol2", uol2);
      }
      if (sn1) {
        params.append("sn1", sn1);
      }
      if (sn2) {
        params.append("sn2", sn2);
      }

      const response = await fuentusapi.get<PagedResponse<AppData>>(`apps/search?${params.toString()}`);
      
      const data = response.data;
      console.log('Apps data:', data);
      setAppsData(data.content);
      setAppsCurrentPage(data.number);
      setAppsTotalPages(data.totalPages);
      setAppsTotalElements(data.totalElements);
    } catch (error) {
      console.error('Error fetching apps data:', error);
      setAppsData([]);
      setAppsCurrentPage(0);
      setAppsTotalPages(0);
      setAppsTotalElements(0);
    } finally {
      setAppsLoading(false);
      setAppsPageLoading(false);
    }
  };

  const fetchServersData = async () => {
    try {
      setServersLoading(true);
      const params = new URLSearchParams();

      if (searchTerm) {
        params.append('appName', searchTerm);
      }
      if (serverName) {
        params.append('name', serverName);
      }

      const query = params.toString();
      const endpoint = query ? `servers?${query}` : 'servers';

      const response = await fuentusapi.get<ServerAppsData[]>(endpoint);
      setServersData(response.data);
    } catch (error) {
      console.error('Error fetching servers data:', error);
      setServersData([]);
    } finally {
      setServersLoading(false);
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      fetchData(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages - 1) {
      fetchData(currentPage + 1);
    }
  };

  const handleAppsPreviousPage = () => {
    if (appsCurrentPage > 0) {
      fetchAppsData(appsCurrentPage - 1);
    }
  };

  const handleAppsNextPage = () => {
    if (appsCurrentPage < appsTotalPages - 1) {
      fetchAppsData(appsCurrentPage + 1);
    }
  };

  return (
    <>
      <div className="flex flex-col gap-10">
        <TableNucleusServices 
          data={data} 
          loading={loading}
          currentPage={currentPage}
          totalPages={totalPages}
          totalElements={totalElements}
          pageLoading={pageLoading}
          onPreviousPage={handlePreviousPage}
          onNextPage={handleNextPage}
        />
        <TableApps 
          data={appsData} 
          loading={appsLoading}
          currentPage={appsCurrentPage}
          totalPages={appsTotalPages}
          totalElements={appsTotalElements}
          pageLoading={appsPageLoading}
          onPreviousPage={handleAppsPreviousPage}
          onNextPage={handleAppsNextPage}
        />
        <TableServers
          data={serversData}
          loading={serversLoading}
        />
      </div>
    </>
  );
};

export default Tables;
