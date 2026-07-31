import Loader from '../../../common/Loader';

const baseCellClasses = 'border-b border-[#eee] px-3 dark:border-strokedark text-center';

interface TableLoadingRowProps {
  colSpan: number;
  message: string;
}

interface TableEmptyRowProps {
  colSpan: number;
  title: string;
  description?: string;
}

export const TableLoadingRow: React.FC<TableLoadingRowProps> = ({ colSpan, message }) => (
  <tr>
    <td className={`${baseCellClasses} py-12`} colSpan={colSpan}>
      <div className="flex flex-col items-center justify-center">
        <Loader size="lg" color="primary" />
        <p className="mt-4 text-sm text-gray-500 dark:text-gray-400">{message}</p>
      </div>
    </td>
  </tr>
);

export const TableEmptyRow: React.FC<TableEmptyRowProps> = ({ colSpan, title, description }) => (
  <tr>
    <td className={`${baseCellClasses} py-8`} colSpan={colSpan}>
      <div className="flex flex-col items-center justify-center">
        <p className="mb-2 text-lg text-gray-500 dark:text-gray-400">{title}</p>
        {description && <p className="text-sm text-gray-400 dark:text-gray-500">{description}</p>}
      </div>
    </td>
  </tr>
);
