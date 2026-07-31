import { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { App } from '../../pages/SearchResults/Tables';
import RfoStatusIcon from '../RfoStatus/RfoStatusIcon';
import LookUp from '../../../src/images/icon/look-up.svg';
import TableCard from './common/TableCard';
import { TableLoadingRow, TableEmptyRow } from './common/TableStateRows';
import TablePaginationFooter from './common/TablePaginationFooter';

type Properties = {
  data: App[];
  loading?: boolean;
  currentPage?: number;
  totalPages?: number;
  totalElements?: number;
  pageLoading?: boolean;
  onPreviousPage?: () => void;
  onNextPage?: () => void;
};

const INITIAL_ROWS_TO_SHOW = 3;

const TableNucleusServices: React.FC<Properties> = ({
  data,
  loading = false,
  currentPage = 0,
  totalPages = 0,
  totalElements = 0,
  pageLoading = false,
  onPreviousPage,
  onNextPage
}) => {
  // Normaliza los datos a un arreglo
  const normalizedData = Array.isArray(data) ? data : [data];
  const [expanded, setExpanded] = useState(false);
  
  const displayedData = expanded ? normalizedData : normalizedData.slice(0, INITIAL_ROWS_TO_SHOW);
  const hasMoreRows = normalizedData.length > INITIAL_ROWS_TO_SHOW;

  console.log('Normalized Data:', normalizedData);

  const remainingRows = Math.max(normalizedData.length - INITIAL_ROWS_TO_SHOW, 0);

  return (
    <TableCard
      title="Resultado"
      footer={
        !loading && (normalizedData.length > 0 || totalPages > 0)
          ? (
              <TablePaginationFooter
                totalPages={totalPages}
                totalElements={totalElements}
                currentPage={currentPage}
                pageLoading={pageLoading}
                hasMoreRows={hasMoreRows}
                onPreviousPage={onPreviousPage}
                onNextPage={onNextPage}
                expanded={expanded}
                onToggleExpand={() => setExpanded((prev) => !prev)}
                pageSize={10}
                remainingCount={remainingRows}
              />
            )
          : undefined
      }
    >
      <table className="w-full table-auto relative">
        <thead>
          <tr className="bg-gray-2 text-left dark:bg-meta-4">
            <th className="min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
              UUAA
            </th>
            <th className="min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
              Servicio N1
            </th>
            <th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
              Service Owner
            </th>
            <th className="min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
              Servicio N2
            </th>
            <th className="min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
              App / Component Owner
            </th>
            <th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">
              Vertical
            </th>
            {/*<th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">*/}
            {/*  Owner N2*/}
            {/*</th>              */}
            {/*<th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">*/}
            {/*  Fabrica*/}
            {/*</th>*/}
            {/*<th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">*/}
            {/*  FTL*/}
            {/*</th>*/}
            {/*<th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white">*/}
            {/*  Rel_Type*/}
            {/*</th>*/}
            <th className="min-w-[150px] py-4 px-4 font-medium text-black dark:text-white">
              Estado RFO
            </th>
            <th className="py-4 px-4 font-medium text-black dark:text-white">
              Actions
            </th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <TableLoadingRow colSpan={7} message="Cargando resultados..." />
          ) : normalizedData.length === 0 ? (
            <TableEmptyRow
              colSpan={7}
              title="No se encontraron resultados"
              description="Intenta con otros parámetros de búsqueda"
            />
          ) : (
            displayedData.map((item, index) => (
              // Itera sobre los datos mostrados
              <tr key={index}>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <h5 className="font-medium text-black dark:text-white">
                    {Array.isArray(item.uuaa) ? item.uuaa.join(', ') : item.uuaa}
                  </h5>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-black dark:text-white">{item.serviceN1}</p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-black dark:text-white">{item.ownerServiceN1}</p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-black dark:text-white">{item.serviceN2}</p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-black dark:text-white">{item.appOwner}</p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-black dark:text-white">{item.verticalName || '-'}</p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <RfoStatusIcon status={item.rfoEstado as any} rfoId={item.rfoId} />
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <div className="dropdown-container relative flex items-center space-x-3.5">
                    {/* NavLink directo usando serviceId */}
                    <NavLink
                      to={`/service/${item.serviceId}`}
                      className="hover:text-primary"
                      state={{ serviceId: item.serviceId }}
                    >
                      <img
                        src={LookUp}
                        alt="Look Up"
                        className="lookup-icon transition-all duration-200"
                      />
                    </NavLink>
                  </div>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </TableCard>
  );
};

export default TableNucleusServices;