import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../Buttons/Button';
import { TbExternalLink, TbEye } from 'react-icons/tb';
import TableCard from './common/TableCard';
import { TableLoadingRow, TableEmptyRow } from './common/TableStateRows';
import TablePaginationFooter from './common/TablePaginationFooter';

export type AppData = {
  id: number;
  name: string;
  bitbucketUrl: string;
  sn2: string;
  uuaa: string;
  nucleusServiceN2: string;
}

type Properties = {
  data: AppData[];
  loading?: boolean;
  currentPage?: number;
  totalPages?: number;
  totalElements?: number;
  pageLoading?: boolean;
  onPreviousPage?: () => void;
  onNextPage?: () => void;
};

const INITIAL_ROWS_TO_SHOW = 3;

const TableApps: React.FC<Properties> = ({
  data,
  loading = false,
  currentPage = 0,
  totalPages = 0,
  totalElements = 0,
  pageLoading = false,
  onPreviousPage,
  onNextPage
}) => {
  const normalizedData = Array.isArray(data) ? data : [data];
  const [expanded, setExpanded] = useState(false);
  const navigate = useNavigate();
  
  const displayedData = expanded ? normalizedData : normalizedData.slice(0, INITIAL_ROWS_TO_SHOW);
  const hasMoreRows = normalizedData.length > INITIAL_ROWS_TO_SHOW;

  const remainingRows = Math.max(normalizedData.length - INITIAL_ROWS_TO_SHOW, 0);

  return (
    <TableCard
      title="Aplicaciones"
      footer={
        !loading && (normalizedData.length > 0 || totalPages > 0)
          ? (
              <TablePaginationFooter
                currentPage={currentPage}
                totalPages={totalPages}
                totalElements={totalElements}
                pageLoading={pageLoading}
                onPreviousPage={onPreviousPage}
                onNextPage={onNextPage}
                hasMoreRows={hasMoreRows}
                expanded={expanded}
                onToggleExpand={() => setExpanded((prev) => !prev)}
                remainingCount={remainingRows}
                pageSize={10}
              />
            )
          : undefined
      }
    >
      <table className="w-full table-auto relative">
        <thead>
          <tr className="bg-gray-2 text-left dark:bg-meta-4">
            <th className="min-w-[200px] py-4 px-4 font-medium text-black dark:text-white">
              Nombre App
            </th>
            <th className="min-w-[200px] py-4 px-4 font-medium text-black dark:text-white">
              Bitbucket
            </th>
            <th className="min-w-[200px] py-4 px-4 font-medium text-black dark:text-white">
              Servicio N2
            </th>
            <th className="min-w-[120px] py-4 px-4 font-medium text-black dark:text-white text-center">
              Acciones
            </th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <TableLoadingRow colSpan={4} message="Cargando aplicaciones..." />
          ) : normalizedData.length === 0 ? (
            <TableEmptyRow
              colSpan={4}
              title="No se encontraron aplicaciones"
              description="Intenta con otros parámetros de búsqueda"
            />
          ) : (
            displayedData.map((item, index) => (
              <tr key={item.id || index}>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <h5 className="font-medium text-black dark:text-white">
                    {item.name}
                  </h5>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  {item.bitbucketUrl ? (
                    <a
                      href={item.bitbucketUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="flex items-center gap-2 text-primary hover:underline"
                    >
                      <span className="truncate max-w-[250px]">{item.bitbucketUrl}</span>
                      <TbExternalLink className="h-4 w-4 flex-shrink-0" />
                    </a>
                  ) : (
                    <span className="text-gray-400 dark:text-gray-500">-</span>
                  )}
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <p className="text-black dark:text-white">{item.nucleusServiceN2 || '-'}</p>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  <div className="flex justify-center">
                    <Button
                      variant="outline"
                      size="sm"
                      className="!px-3 !py-2"
                      startIcon={<TbEye className="h-4 w-4" />}
                      onClick={() => navigate(`/app/${item.id}`)}
                    >
                      Ver detalle
                    </Button>
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

export default TableApps;
