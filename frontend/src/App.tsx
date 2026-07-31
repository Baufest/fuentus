import { useEffect, useState } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';

import Loader from './common/Loader';
import PageTitle from './components/PageTitle';
import SearchResults from './pages/SearchResults/Tables';
import DefaultLayout from './layout/DefaultLayout';
import NucleusServicesDetails from './pages/Detalle/NucleusServicesDetails';
import AppDetail from './pages/Detalle/AppDetail';
import Dashboard from './pages/Dashboard/Dashboard';
import Verticales from './pages/Verticales/Verticales';
import DataImport from './pages/DataImport/DataImport';
import { DashboardFiltersProvider } from './contexts/DashboardFiltersContext';
import { HeaderFiltersProvider } from './contexts/HeaderFiltersContext';


function App() {
  const [loading, setLoading] = useState<boolean>(true);
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  useEffect(() => {
    setTimeout(() => setLoading(false), 1000);
  }, []);

  return loading ? (
    <Loader size="xl" color="primary" fullScreen={true}/>
  ) : (
    <HeaderFiltersProvider>
      <DashboardFiltersProvider>
        <DefaultLayout>
          <Routes>
          <Route
            index
            element={
              <>
                <PageTitle title="Fuentus" />
                <Dashboard />
              </>
            }
          />
           <Route
            path="/searchresults"
            element={
              <>
                <PageTitle title="Search Results | Fuentus" />
                <SearchResults />
              </>
            }
          />
         <Route
            path="/service/:serviceId"
            element={
              <>
                <PageTitle title="Detalle del Servicio" />
                <NucleusServicesDetails />
              </>
            }
          />
           <Route
            path="/app/:appId"
            element={
              <>
                <PageTitle title="Detalle del detalle" />
                <AppDetail />
              </>
            }
          />
          <Route
            path="/verticales"
            element={
              <>
                <PageTitle title="Verticales | Fuentus" />
                <Verticales />
              </>
            }
          />
          <Route
            path="/data-import"
            element={
              <>
                <PageTitle title="Importación de Datos | Fuentus" />
                <DataImport />
              </>
            }
          />

         </Routes>
      </DefaultLayout>
    </DashboardFiltersProvider>
    </HeaderFiltersProvider>
  );
}

export default App;
