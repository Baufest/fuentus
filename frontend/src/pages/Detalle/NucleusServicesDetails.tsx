import { NavLink, useLocation, useParams } from 'react-router-dom';
import Breadcrumb from '../../components/Breadcrumbs/Breadcrumb.tsx';
import { SiSonarqube, SiBitbucket } from "react-icons/si";
import { useEffect, useState, useRef } from "react";
import Badge from '../../components/Badges/Badge.tsx';
import Button from '../../components/Buttons/Button.tsx';
import { TbShieldCheck, TbPackage, TbChevronLeft, TbChevronRight, TbSearch, TbX, TbLayoutGrid, TbList } from "react-icons/tb";
import LookUp from '../../../src/images/icon/look-up.svg';
import { Tooltip } from '../../components/Helpers/Tooltip.tsx';
import Loader from '../../common/Loader/index.tsx';
import { Apps } from '../../types/app.ts';
import ServiceSummaryCards from '../../components/Cards/ServiceSummaryCards.tsx';
import UuaaAppsCharts from '../../components/Charts/UuaaAppsCharts.tsx';
import ServiceProductivityCharts from '../../components/Charts/ServiceProductivityCharts.tsx';
import { useServiceSummaryById, useServiceAppsById } from '../../hooks/useServiceSummary.ts';

// Helper functions moved outside component
function getCoverage(coverage: number) {
  if (coverage >= 80) {
    return 'success';
  } else if (coverage >= 50) {
    return 'warning';
  } else {
    return 'error';
  }
}

function getBugs(bugs: number) {
  if (bugs <= 0) {
    return 'success';
  } else if (bugs < 10) {
    return 'warning';
  } else {
    return 'error';
  }
}

export type Appdetalle = {
  name: string;
  bitbucketUrl: string;
  sonar10Url: string;
  chimeraUrl: string;
  samuelUrl: string;
  monolith: string; 
  coverage: number; 
  bugs: number;
  language: string;
  totalLow: number;   
  totalMedium: number;
  totalHigh: number;
  servers?: Server[];
}

type Server = {
  serverName: string;
  serverUrl?: string;
}

const NucleusServicesDetails = () => {
  const { state } = useLocation();
  const { serviceId: serviceIdParam } = useParams<{ serviceId: string }>();
  
  // Obtener el serviceId de la URL o del estado
  const serviceId = serviceIdParam ? parseInt(serviceIdParam, 10) : (state?.serviceId || null);
  
  const [allAppsForCharts, setAllAppsForCharts] = useState<Apps[]>([]);
  const [viewMode, setViewMode] = useState<'table' | 'cards'>('table');
  const [showCharts, setShowCharts] = useState(true);
  const [searchInput, setSearchInput] = useState('');
  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);
  
  // Fetch Service summary using the custom hook with serviceId
  const { summary, loading: summaryLoading, error: summaryError } = useServiceSummaryById(serviceId);
  
  // Fetch apps using the service apps hook with serviceId
  const { 
    apps: dataaps, 
    loading, 
    totalElements, 
    totalPages, 
    currentPage, 
    setPage, 
    setSearch 
  } = useServiceAppsById(serviceId, 0, '');
  
  // Datos del servicio desde el summary
  const serviceN1 = summary?.serviceN1 || '';
  const serviceN2 = summary?.serviceN2 || '';
  const ownerServiceN1 = summary?.ownerServiceN1 || '';
  const uuaas = summary?.uuaas || [];
  
  // Update allAppsForCharts when dataaps changes (for charts we need more data)
  useEffect(() => {
    // Use the current apps for charts - in a real scenario you might want to fetch all apps
    if (dataaps && dataaps.length > 0) {
      setAllAppsForCharts(dataaps);
    }
  }, [dataaps]);

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      setPage(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages - 1) {
      setPage(currentPage + 1);
    }
  };

  const handleClearSearch = () => {
    setSearchInput('');
    setSearch('');
  };
  
  // Generate breadcrumb title from summary data
  const pageTitle = summary?.serviceN2 
    ? `Servicio: ${summary.serviceN2}` 
    : summary?.serviceN1 
      ? `Servicio: ${summary.serviceN1}` 
      : serviceId 
        ? `Servicio ID: ${serviceId}`
        : 'Detalle del Servicio';
  
  // Handle error state
  if (summaryError && !summaryLoading) {
    return (
      <div className="space-y-6">
        <div className="rounded-lg border border-stroke bg-white px-5 pt-6 pb-4 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5">
          <Breadcrumb pageName="Error" />
          <div className="mt-4 text-center py-8">
            <p className="text-red-500 dark:text-red-400 text-lg mb-2">
              No se pudo cargar el servicio
            </p>
            <p className="text-gray-500 dark:text-gray-400 text-sm">
              {summaryError || 'El servicio solicitado no existe o no está disponible'}
            </p>
          </div>
        </div>
      </div>
    );
  }
  
  return (
    <div className="space-y-6">
      {/* Header with Breadcrumb and Service Title */}
      <div className="rounded-lg border border-stroke bg-white px-5 pt-6 pb-4 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5">
        <Breadcrumb pageName={pageTitle} />
        {/* Service Info */}
        <div className="mt-4 flex flex-wrap gap-4 text-sm text-gray-600 dark:text-gray-400">
          {serviceN1 && (
            <span><strong>SN1:</strong> {serviceN1}</span>
          )}
          {serviceN2 && (
            <span><strong>SN2:</strong> {serviceN2}</span>
          )}
          {ownerServiceN1 && (
            <span><strong>Owner:</strong> {ownerServiceN1}</span>
          )}
          <span><strong>UUAAs:</strong> {uuaas.map(u => u.substring(0, 4).toUpperCase()).join(', ')}</span>
        </div>
      </div>

      {/* Summary Cards Section */}
      <ServiceSummaryCards summary={summary} loading={summaryLoading} />

      {/* Productivity & Velocity Charts Section */}
      {serviceId && (
        <ServiceProductivityCharts serviceId={serviceId} />
      )}

      {/* Charts Section - Collapsible */}
      <div className="rounded-lg border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
        <button 
          onClick={() => setShowCharts(!showCharts)}
          className="w-full px-5 py-4 flex items-center justify-between text-left hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors rounded-t-lg"
        >
          <h3 className="text-lg font-semibold text-black dark:text-white">
            Gráficos de Métricas
          </h3>
          <span className={`transform transition-transform ${showCharts ? 'rotate-180' : ''}`}>
            ▼
          </span>
        </button>
        {showCharts && (
          <div className="px-5 pb-5">
            <UuaaAppsCharts apps={allAppsForCharts} loading={loading && allAppsForCharts.length === 0} />
          </div>
        )}
      </div>

      {/* Applications Table/Cards Section */}
      <div className="rounded-lg border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
        {/* Search and View Toggle */}
        <div className="mb-6 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          {/* Search Input */}
          <div className="relative max-w-md flex-1">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <TbSearch className="h-5 w-5 text-gray-400" />
            </div>
            <input
              type="text"
              placeholder="Buscar por nombre de aplicación..."
              value={searchInput}
              onChange={(e) => {
                const value = e.target.value;
                setSearchInput(value);
                if (searchTimeoutRef.current) {
                  clearTimeout(searchTimeoutRef.current);
                }
                searchTimeoutRef.current = setTimeout(() => {
                  setSearch(value);
                }, 500);
              }}
              className="block w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-primary focus:border-primary dark:bg-gray-800 dark:border-gray-600 dark:text-white dark:placeholder-gray-400"
            />
            {searchInput && (
              <button
                onClick={handleClearSearch}
                className="absolute inset-y-0 right-0 pr-3 flex items-center"
              >
                <TbX className="h-5 w-5 text-gray-400 hover:text-gray-600" />
              </button>
            )}
          </div>
          
          {/* View Toggle Buttons */}
          <div className="flex items-center gap-2">
            <span className="text-sm text-gray-500 dark:text-gray-400 mr-2">Vista:</span>
            <button
              onClick={() => setViewMode('table')}
              className={`p-2 rounded-md transition-colors ${
                viewMode === 'table' 
                  ? 'bg-primary text-white' 
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200 dark:bg-gray-700 dark:text-gray-300'
              }`}
              title="Vista tabla"
            >
              <TbList className="h-5 w-5" />
            </button>
            <button
              onClick={() => setViewMode('cards')}
              className={`p-2 rounded-md transition-colors ${
                viewMode === 'cards' 
                  ? 'bg-primary text-white' 
                  : 'bg-gray-100 text-gray-600 hover:bg-gray-200 dark:bg-gray-700 dark:text-gray-300'
              }`}
              title="Vista tarjetas"
            >
              <TbLayoutGrid className="h-5 w-5" />
            </button>
          </div>
        </div>
        
        {searchInput && (
          <p className="mb-4 text-sm text-gray-600 dark:text-gray-400">
            Filtrando por: "{searchInput}"
          </p>
        )}

        {/* Cards View */}
        {viewMode === 'cards' ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-4">
            {loading ? (
              [...Array(6)].map((_, index) => (
                <div key={index} className="rounded-lg border border-stroke bg-white p-4 shadow-sm dark:border-strokedark dark:bg-boxdark animate-pulse">
                  <div className="h-5 bg-gray-200 dark:bg-gray-700 rounded w-3/4 mb-3"></div>
                  <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-1/2 mb-2"></div>
                  <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-2/3"></div>
                </div>
              ))
            ) : dataaps.length === 0 ? (
              <div className="col-span-full text-center py-8">
                <p className="text-gray-500 dark:text-gray-400">
                  {searchInput 
                    ? `No se encontraron aplicaciones que coincidan con "${searchInput}"`
                    : 'No se encontraron aplicaciones para este servicio'
                  }
                </p>
              </div>
            ) : (
              dataaps.map((app, index) => (
                <div key={index} className="rounded-lg border border-stroke bg-white p-4 shadow-sm hover:shadow-md transition-shadow dark:border-strokedark dark:bg-boxdark">
                  <div className="flex items-start justify-between mb-3">
                    <div>
                      <h4 className="font-semibold text-black dark:text-white truncate" title={app.name}>
                        {app.name}
                      </h4>
                      <span className="text-sm text-gray-500 dark:text-gray-400">{app.language || 'N/A'}</span>
                    </div>
                    <NavLink
                      to={`/app/${app.id}`}
                      className="text-primary hover:text-primary-dark"
                    >
                      <img src={LookUp} alt="Ver detalle" className="w-5 h-5" />
                    </NavLink>
                  </div>
                  
                  {/* Metrics Row */}
                  <div className="flex items-center gap-2 mb-3">
                    <Badge size="sm" color={getCoverage(app.coverage || 0)}>
                      <span className="text-xs">{app.coverage || 0}%</span>
                    </Badge>
                    <Badge size="sm" color={getBugs(app.bugs || 0)}>
                      <span className="text-xs">{app.bugs || 0} bugs</span>
                    </Badge>
                  </div>
                  
                  {/* Chimera Stats */}
                  <div className="text-xs space-y-1">
                    <div className="flex items-center gap-1">
                      <TbShieldCheck className="w-4 h-4 text-purple-500" />
                      <span className="text-red-500">{app.chimeraSast?.totalHigh || 0}H</span>
                      <span className="text-yellow-500">{app.chimeraSast?.totalMedium || 0}M</span>
                      <span className="text-green-500">{app.chimeraSast?.totalLow || 0}L</span>
                    </div>
                    <div className="flex items-center gap-1">
                      <TbPackage className="w-4 h-4 text-orange-500" />
                      <span className="text-red-700">{app.chimeraSca?.totalCritical || 0}C</span>
                      <span className="text-red-500">{app.chimeraSca?.totalHigh || 0}H</span>
                      <span className="text-yellow-500">{app.chimeraSca?.totalMedium || 0}M</span>
                      <span className="text-green-500">{app.chimeraSca?.totalLow || 0}L</span>
                    </div>
                  </div>
                  
                  {/* Quick Links */}
                  <div className="flex items-center gap-2 mt-3 pt-3 border-t border-gray-100 dark:border-gray-700">
                    {app.bitbucketUrl && (
                      <Tooltip text="Bitbucket">
                        <a href={app.bitbucketUrl} target="_blank" rel="noopener noreferrer" className="text-gray-500 hover:text-primary">
                          <SiBitbucket className="w-4 h-4" />
                        </a>
                      </Tooltip>
                    )}
                    {app.sonar10Url && (
                      <Tooltip text="Sonar">
                        <a href={app.sonar10Url} target="_blank" rel="noopener noreferrer" className="text-gray-500 hover:text-primary">
                          <SiSonarqube className="w-4 h-4" />
                        </a>
                      </Tooltip>
                    )}
                    {app.chimeraUrl && (
                      <Tooltip text="Chimera SAST">
                        <a href={app.chimeraUrl} target="_blank" rel="noopener noreferrer" className="text-gray-500 hover:text-primary">
                          <TbShieldCheck className="w-4 h-4" />
                        </a>
                      </Tooltip>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        ) : (
          /* Table View */
          <div className="max-w-full overflow-x-auto" style={{ maxHeight: '60vh' }}>
            <table className="w-full table-auto min-w-[1200px]">
              <thead className="sticky top-0 z-20 bg-gray-2 dark:bg-meta-4">
                <tr className="bg-gray-2 text-left dark:bg-meta-4">
                  <th className="min-w-[120px] py-4 px-3 font-medium text-black dark:text-white">
                    UUAA
                  </th>
                  <th className="min-w-[180px] py-4 px-3 font-medium text-black dark:text-white">
                    Aplicación
                  </th>
                  <th className="min-w-[80px] py-4 px-3 font-medium text-black dark:text-white">
                    Tech
                  </th>
                  <th className="min-w-[80px] py-4 px-3 font-medium text-black dark:text-white">
                    Bitbucket
                  </th>
                  <th className="min-w-[200px] py-4 px-3 font-medium text-black dark:text-white">
                    Sonar
                  </th>
                  <th className="min-w-[240px] py-4 px-3 font-medium text-black dark:text-white">
                    Chimera
                  </th>
                  <th className="min-w-[80px] py-4 px-3 font-medium text-black dark:text-white">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan={7} className="border-b border-[#eee] py-12 px-3 dark:border-strokedark text-center">
                      <div className="flex flex-col items-center justify-center">
                        <Loader size="lg" color="primary" />
                        <p className="text-gray-500 dark:text-gray-400 text-sm mt-4">
                          Cargando aplicaciones...
                        </p>
                      </div>
                    </td>
                  </tr>
                ) : dataaps.length === 0 ? (
                  <tr>
                    <td colSpan={7} className="border-b border-[#eee] py-8 px-3 dark:border-strokedark text-center">
                      <div className="flex flex-col items-center justify-center">
                        {searchInput ? (
                          <>
                            <p className="text-gray-500 dark:text-gray-400 text-lg mb-2">
                              No se encontraron aplicaciones que coincidan con "{searchInput}"
                            </p>
                            <p className="text-gray-400 dark:text-gray-500 text-sm">
                              Intenta con otros términos de búsqueda o{' '}
                              <button 
                                onClick={handleClearSearch}
                                className="text-primary hover:underline"
                              >
                                borra el filtro
                              </button>
                            </p>
                          </>
                        ) : (
                          <>
                            <p className="text-gray-500 dark:text-gray-400 text-lg mb-2">
                              No se encontraron aplicaciones para este servicio
                            </p>
                            <p className="text-gray-400 dark:text-gray-500 text-sm">
                              Verifica las UUAAs o contacta al administrador
                            </p>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ) : (
                  dataaps.map((apps, index) => (
                    <tr key={index} className="hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors">
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <h5 className="font-medium text-black dark:text-white">
                          {apps.uuaa?.substring(0, 4).toUpperCase() || 'N/A'}
                        </h5>
                      </td>
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <p className="text-black dark:text-white">{apps.name}</p>
                      </td>
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <p className="text-black dark:text-white">{apps.language}</p>
                      </td>
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <div className="flex items-center">
                          <Tooltip text="Ver repositorio Bitbucket">
                            <Button variant='outline' size='sm' className="!px-2 !py-2">
                              <a href={apps.bitbucketUrl ?? undefined} target="_blank" rel="noopener noreferrer">
                                <SiBitbucket className="size-4 text-primary dark:text-white" />
                              </a>
                            </Button>
                          </Tooltip>
                        </div>
                      </td>
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <div className="flex items-center gap-1">
                          <Tooltip text="Ver análisis Sonar">
                            <Button variant='outline' size='sm' className="!px-2 !py-2">
                              <a href={apps.sonar10Url} target="_blank" rel="noopener noreferrer">
                                <SiSonarqube className="size-4 text-primary dark:text-white" />
                              </a>
                            </Button>
                          </Tooltip>
                          <Tooltip text="% de cobertura de código">
                            <Badge size="sm" color={getCoverage(apps.coverage || 0)}>
                              <span className="w-10 text-center inline-block">{apps.coverage || 0}%</span>
                            </Badge>
                          </Tooltip>
                          <Tooltip text="Bugs detectados por Sonar">
                            <Badge size="sm" color={getBugs(apps.bugs || 0)}>
                              <span className="w-10 text-center inline-block">{apps.bugs || 0}</span>
                            </Badge>
                          </Tooltip>
                        </div>
                      </td>
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <div className="flex items-center gap-1">
                          <Tooltip text="Ver reporte Chimera SAST">
                            <Button variant='outline' size='sm' className="!px-2 !py-2">
                              <a href={apps.chimeraUrl} target="_blank" rel="noopener noreferrer">
                                <TbShieldCheck className="size-4 text-primary dark:text-white" />
                              </a>
                            </Button>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades High (SAST)">
                            <Badge size="sm" color={"error"}>
                              <span className="w-8 text-center inline-block">{apps.chimeraSast?.totalHigh || 0}H</span>
                            </Badge>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades Medium (SAST)">
                            <Badge size="sm" color={"warning"}>
                              <span className="w-8 text-center inline-block">{apps.chimeraSast?.totalMedium || 0}M</span>
                            </Badge>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades Low (SAST)">
                            <Badge size="sm" color={"success"}>
                              <span className="w-8 text-center inline-block">{apps.chimeraSast?.totalLow || 0}L</span>
                            </Badge>
                          </Tooltip>
                        </div>
                        <div className="flex items-center gap-1 mt-2">
                          <Tooltip text="Ver reporte Chimera SCA">
                            <Button variant='outline' size='sm' className="!px-2 !py-2">
                              <a href={apps?.chimeraUrl?.replace('sast','sca') ?? "#"} target="_blank" rel="noopener noreferrer">
                                <TbPackage className="size-4 text-primary dark:text-white" />
                              </a>
                            </Button>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades Critical (SCA)">
                            <Badge size="sm" color={"error"} variant="solid">
                              <span className="w-8 text-center inline-block">{apps.chimeraSca?.totalCritical || 0}C</span>
                            </Badge>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades High (SCA)">
                            <Badge size="sm" color={"error"}>
                              <span className="w-8 text-center inline-block">{apps.chimeraSca?.totalHigh || 0}H</span>
                            </Badge>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades Medium (SCA)">
                            <Badge size="sm" color={"warning"}>
                              <span className="w-8 text-center inline-block">{apps.chimeraSca?.totalMedium || 0}M</span>
                            </Badge>
                          </Tooltip>
                          <Tooltip text="Total de vulnerabilidades Low (SCA)">
                            <Badge size="sm" color={"success"}>
                              <span className="w-8 text-center inline-block">{apps.chimeraSca?.totalLow || 0}L</span>
                            </Badge>
                          </Tooltip>
                        </div>
                      </td>
                      <td className="border-b border-[#eee] py-4 px-3 dark:border-strokedark">
                        <div className="flex items-center justify-center">
                          <Tooltip text="Ver detalle de la app">
                            <NavLink to={`/app/${apps.id}`} className="hover:text-primary">
                              <img src={LookUp} alt="Look Up" className="lookup-icon transition-all duration-200" />
                            </NavLink>
                          </Tooltip>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      
        {/* Pagination Controls */}
        {!loading && (dataaps.length > 0 || totalPages > 0) && (
          <div className="flex items-center justify-between border-t border-stroke bg-white px-4 py-3 dark:border-strokedark dark:bg-boxdark sm:px-6 mt-4" data-testid="pagination-container">
            <div className="flex flex-1 justify-between sm:hidden">
              <Button
                onClick={handlePreviousPage}
                disabled={currentPage === 0 || loading}
                variant="outline"
                size="sm"
                data-testid="mobile-prev-button"
              >
                {loading && currentPage > 0 ? 'Cargando...' : 'Anterior'}
              </Button>
              <Button
                onClick={handleNextPage}
                disabled={currentPage >= totalPages - 1 || loading}
                variant="outline"
                size="sm"
                data-testid="mobile-next-button"
              >
                {loading && currentPage < totalPages - 1 ? 'Cargando...' : 'Siguiente'}
              </Button>
            </div>
            <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
              <div>
                <p className="text-sm text-gray-700 dark:text-gray-400" data-testid="pagination-info">
                  {totalElements > 0 ? (
                    <>
                      Mostrando{' '}
                      <span className="font-medium">{currentPage * 20 + 1}</span>
                      {' '} a{' '}
                      <span className="font-medium">
                        {Math.min((currentPage + 1) * 20, totalElements)}
                      </span>
                      {' '} de{' '}
                      <span className="font-medium">{totalElements}</span>
                      {' '} resultados
                      {searchInput && (
                        <span className="text-gray-500">
                          {' '}para "{searchInput}"
                        </span>
                      )}
                    </>
                  ) : (
                    <span>
                      {searchInput 
                        ? `Sin resultados para "${searchInput}"`
                        : 'Sin resultados'
                      }
                    </span>
                  )}
                </p>
              </div>
              <div>
                <nav className="isolate inline-flex -space-x-px rounded-md shadow-sm" aria-label="Pagination">
                  <Button
                    onClick={handlePreviousPage}
                    disabled={currentPage === 0 || loading}
                    variant="outline"
                    size="sm"
                    startIcon={<TbChevronLeft className="h-4 w-4" />}
                    className="!rounded-r-none"
                    data-testid="desktop-prev-button"
                  >
                    {loading && currentPage > 0 ? 'Cargando...' : 'Anterior'}
                  </Button>
                  <span className="relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-900 ring-1 ring-inset ring-gray-300 dark:text-gray-400 dark:ring-gray-700">
                    Página {currentPage + 1} de {totalPages}
                  </span>
                  <Button
                    onClick={handleNextPage}
                    disabled={currentPage >= totalPages - 1 || loading}
                    variant="outline"
                    size="sm"
                    endIcon={<TbChevronRight className="h-4 w-4" />}
                    className="!rounded-l-none"
                    data-testid="desktop-next-button"
                  >
                    {loading && currentPage < totalPages - 1 ? 'Cargando...' : 'Siguiente'}
                  </Button>
                </nav>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default NucleusServicesDetails;
