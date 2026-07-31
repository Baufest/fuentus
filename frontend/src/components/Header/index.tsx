import { Link, useNavigate } from 'react-router-dom';
// import LogoIcon from '../../images/logo/Fuentus.jpg';
import DarkModeSwitcher from './DarkModeSwitcher';
import FilterPanel from './FilterPanel';
import { useState, useEffect, useRef } from 'react';
import Search from '../../../src/images/icon/search.svg';
import { useComboValues } from '../../hooks/useComboValues';
import fuentusapi from '../../api/fuentusapi';
import { useHeaderFilters } from '../../contexts/HeaderFiltersContext';
import { useDashboardFilters } from '../../hooks/useDashboardFiltersGlobal';

const Header = (props: {
  sidebarOpen: string | boolean | undefined;
  setSidebarOpen: (arg0: boolean) => void;
}) => {
  const [searchValue, setSearchValue] = useState('');
  const [errorMessage, setErrorMessage] = useState(''); // Estado para el mensaje de error
  const [filtersOpen, setFiltersOpen] = useState(false);
  
  // Dashboard global filters (for vertical and uol2)
  const { filters: dashboardFilters } = useDashboardFilters();
  
  const {
    selectedSn1,
    selectedSn2,
    selectedServerName,
    setSelectedSn1,
    setSelectedSn2,
    setSelectedServerName,
    clearFilters
  } = useHeaderFilters();
  const [serverNames, setServerNames] = useState<string[]>([]);
  const [serverNamesLoading, setServerNamesLoading] = useState(false);
  const filtersRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  
  // Obtener valores para los combos - pasa todos los filtros para filtrado encadenado
  const { data: comboValues, loading: comboLoading } = useComboValues({ 
    vertical: dashboardFilters.selectedVertical,
    uol2: dashboardFilters.selectedUOL2[0] || '',
    sn1: selectedSn1
  });

  // Cerrar el desplegable al hacer clic fuera
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (filtersRef.current && !filtersRef.current.contains(event.target as Node)) {
        setFiltersOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Resetear SN2 cuando cambia SN1
  useEffect(() => {
    setSelectedSn2('');
  }, [selectedSn1]);

  useEffect(() => {
    const fetchServerNames = async () => {
      try {
        setServerNamesLoading(true);
        const response = await fuentusapi.get<Array<{ id: number; name: string }>>('servers/names');
        const names = response.data.map((server) => server.name).filter(Boolean);
        setServerNames(names);
      } catch (error) {
        console.error('Error fetching server names:', error);
        setServerNames([]);
      } finally {
        setServerNamesLoading(false);
      }
    };

    fetchServerNames();
  }, []);
  
  const handleSearch = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (searchValue.trim().toLowerCase() !== ' ') {
      navigate('/searchresults', {
        state: { 
          searchValue,
          vertical: dashboardFilters.selectedVertical || undefined,
          uol2: dashboardFilters.selectedUOL2[0] || undefined,
          sn1: selectedSn1 || undefined,
          sn2: selectedSn2 || undefined,
          serverName: selectedServerName || undefined,
        }
      }); // Navega a la página de resultados con los filtros
      setErrorMessage(''); // Limpia el mensaje de error si la búsqueda es válida
    } else {
      setErrorMessage('No se encontraron resultados para la búsqueda.'); // Establece el mensaje de error
    }
  };

  const activeFiltersCount = [
    dashboardFilters.selectedPeriod,
    dashboardFilters.selectedGeography,
    dashboardFilters.selectedVertical,
    dashboardFilters.selectedUOL2[0],
    selectedSn1, 
    selectedSn2, 
    selectedServerName
  ].filter(Boolean).length;
  const hasActiveFilters = activeFiltersCount > 0;
  
  return (
    <header className="sticky top-0 z-999 flex w-full bg-[#070E46] drop-shadow-1 dark:bg-[#070E46] dark:drop-shadow-none">
      <div className="flex flex-grow items-center justify-between px-4 py-4 shadow-2 md:px-6 2xl:px-11">
        <Link to="/" className="cursor-pointer">
          <img src="/src/images/logo/fuentus-white.png" alt="Fuentus Logo" className="w-40 mx-2 hover:opacity-80 transition-opacity" />
        </Link>
        <div className="flex items-center gap-2 sm:gap-4 lg:hidden">
          {/* <!-- Hamburger Toggle BTN --> */}
          <button
            aria-controls="sidebar"
            onClick={(e) => {
              e.stopPropagation();
              props.setSidebarOpen(!props.sidebarOpen);
            }}
            className="z-99999 block rounded-sm border border-stroke bg-white p-1.5 shadow-sm dark:border-strokedark dark:bg-boxdark lg:hidden"
          >
            <span className="relative block h-5.5 w-5.5 cursor-pointer">
              <span className="du-block absolute right-0 h-full w-full">
                <span
                  className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-[0] duration-200 ease-in-out dark:bg-white ${
                    !props.sidebarOpen && '!w-full delay-300'
                  }`}
                ></span>
                <span
                  className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-150 duration-200 ease-in-out dark:bg-white ${
                    !props.sidebarOpen && 'delay-400 !w-full'
                  }`}
                ></span>
                <span
                  className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-200 duration-200 ease-in-out dark:bg-white ${
                    !props.sidebarOpen && '!w-full delay-500'
                  }`}
                ></span>
              </span>
              <span className="absolute right-0 h-full w-full rotate-45">
                <span
                  className={`absolute left-2.5 top-0 block h-full w-0.5 rounded-sm bg-black delay-300 duration-200 ease-in-out dark:bg-white ${
                    !props.sidebarOpen && '!h-0 !delay-[0]'
                  }`}
                ></span>
                <span
                  className={`delay-400 absolute left-0 top-2.5 block h-0.5 w-full rounded-sm bg-black duration-200 ease-in-out dark:bg-white ${
                    !props.sidebarOpen && '!h-0 !delay-200'
                  }`}
                ></span>
              </span>
            </span>
          </button>
          {/* <!-- Hamburger Toggle BTN --> */}

          {/* <Link className="block flex-shrink-0 lg:hidden" to="/">
            <img src={LogoIcon} alt="Logo" />
          </Link> */}
        </div>

        <div className="hidden sm:flex items-center gap-4">
          {/* Desplegable de Filtros */}
          <div className="relative" ref={filtersRef}>
            <button
              onClick={() => setFiltersOpen(!filtersOpen)}
              className={`flex items-center gap-2 rounded-md px-3 py-2 text-sm font-medium text-white transition ${
                hasActiveFilters ? 'bg-primary' : 'bg-white/10 hover:bg-white/20'
              }`}
              title="Filtros"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
              </svg>
              <span>Filtros</span>
              {hasActiveFilters && (
                <span className="flex h-5 w-5 items-center justify-center rounded-full bg-white text-xs font-bold text-primary">
                  {activeFiltersCount}
                </span>
              )}
              <svg className={`w-3 h-3 transition-transform ${filtersOpen ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
              </svg>
            </button>

            {/* Panel de Filtros Desplegable */}
            {filtersOpen && (
              <FilterPanel
                selectedSn1={selectedSn1}
                setSelectedSn1={setSelectedSn1}
                selectedSn2={selectedSn2}
                setSelectedSn2={setSelectedSn2}
                selectedServerName={selectedServerName}
                setSelectedServerName={setSelectedServerName}
                serverNames={serverNames}
                serverNamesLoading={serverNamesLoading}
                comboValues={comboValues}
                comboLoading={comboLoading}
                hasActiveFilters={hasActiveFilters}
                onClearFilters={clearFilters}
              />
            )}
          </div>

          {/* Barra de búsqueda */}
          <form action="https://formbold.com/s/unique_form_id" method="POST" onSubmit={handleSearch}>
            <div className="relative">
              <button className="absolute left-0 top-1/2 -translate-y-1/2">
                <img 
                  src={Search} 
                  alt="Search" 
                  className="brightness-0 invert"
                />
              </button>
              <input
                type="text"
                value={searchValue}
                onChange={(e) => setSearchValue(e.target.value)}
                placeholder="Buscar..."
                className="w-full bg-transparent pl-9 pr-4 text-white placeholder-white focus:outline-none xl:w-125 text-xl"
              />
            </div>
          </form>
          {errorMessage && ( // Renderiza el mensaje de error si existe
            <p className="mt-2 text-sm text-red-500">{errorMessage}</p>
          )}
        </div>

        <div className="flex items-center gap-3 2xsm:gap-7">
          <ul className="flex items-center gap-2 2xsm:gap-4">
            {/* <!-- Import Data Button --> */}
            <li>
              <Link
                to="/data-import"
                className="flex items-center gap-2 rounded-md bg-primary px-4 py-2 text-sm font-medium text-white transition hover:bg-primary/90"
                title="Importar Datos"
              >
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                </svg>
                <span className="hidden sm:inline">Importar</span>
              </Link>
            </li>
            {/* <!-- Dark Mode Toggler --> */}
            <DarkModeSwitcher />
            {/* <!-- Dark Mode Toggler --> */}
          </ul>
        </div>
      </div>
    </header>
  );
};

export default Header;
