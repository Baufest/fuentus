import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TableCard from './common/TableCard';
import { TableLoadingRow, TableEmptyRow } from './common/TableStateRows';
import ShowMoreButton from './common/ShowMoreButton';

export type ServerAppsData = {
  id: number;
  name: string;
  apps: Array<{
    id: number;
    name: string;
  }>;
};

interface Properties {
  data: ServerAppsData[];
  loading?: boolean;
}

const INITIAL_SERVERS_TO_SHOW = 5;

const TableServers: React.FC<Properties> = ({ data, loading = false }) => {
  const normalizedData = Array.isArray(data) ? data : [];
  const [expanded, setExpanded] = useState(false);
  const displayedData = expanded ? normalizedData : normalizedData.slice(0, INITIAL_SERVERS_TO_SHOW);
  const hasMoreServers = normalizedData.length > INITIAL_SERVERS_TO_SHOW;
  const navigate = useNavigate();

  return (
    <TableCard
      title="Servidores"
      footer={
        !loading && hasMoreServers ? (
          <div className="flex justify-center border-t border-stroke pt-4 dark:border-strokedark">
            <ShowMoreButton
              expanded={expanded}
              onToggle={() => setExpanded((prev) => !prev)}
              remainingCount={normalizedData.length - INITIAL_SERVERS_TO_SHOW}
            />
          </div>
        ) : undefined
      }
    >
      <table className="w-full table-auto relative">
        <thead>
          <tr className="bg-gray-2 text-left dark:bg-meta-4">
            <th className="min-w-[200px] py-4 px-4 font-medium text-black dark:text-white">
              Servidor
            </th>
            <th className="min-w-[140px] py-4 px-4 font-medium text-black dark:text-white text-center">
              Total Apps
            </th>
            <th className="py-4 px-4 font-medium text-black dark:text-white">
              Aplicaciones
            </th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <TableLoadingRow colSpan={3} message="Cargando servidores..." />
          ) : normalizedData.length === 0 ? (
            <TableEmptyRow
              colSpan={3}
              title="No se encontraron servidores"
              description="Ajusta los filtros de búsqueda para ver resultados"
            />
          ) : (
            displayedData.map((server) => (
              <tr key={server.id}>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark align-top">
                  <h5 className="font-medium text-black dark:text-white">
                    {server.name}
                  </h5>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark align-top text-center">
                  <span className="rounded-full bg-primary/10 px-3 py-1 text-sm font-medium text-primary">
                    {server.apps?.length ?? 0}
                  </span>
                </td>
                <td className="border-b border-[#eee] py-5 px-4 dark:border-strokedark">
                  {server.apps?.length ? (
                    <div className="flex flex-wrap gap-2">
                      {server.apps.map((app) => (
                        <button
                          key={app.id}
                          onClick={() => navigate(`/app/${app.id}`)}
                          className="inline-flex items-center rounded-full bg-gray-100 px-3 py-1 text-sm text-gray-700 transition hover:bg-primary/10 hover:text-primary focus:outline-none focus-visible:ring focus-visible:ring-primary/50 dark:bg-gray-700 dark:text-gray-100"
                        >
                          {app.name}
                        </button>
                      ))}
                    </div>
                  ) : (
                    <span className="text-gray-400 dark:text-gray-500">Sin aplicaciones asociadas</span>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </TableCard>
  );
};

export default TableServers;
