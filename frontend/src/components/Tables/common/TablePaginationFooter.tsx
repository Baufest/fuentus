import Button from '../../Buttons/Button';
import ShowMoreButton from './ShowMoreButton';
import { TbChevronLeft, TbChevronRight, TbChevronDown, TbChevronUp } from 'react-icons/tb';

interface TablePaginationFooterProps {
  currentPage: number;
  totalPages: number;
  totalElements: number;
  pageLoading?: boolean;
  onPreviousPage?: () => void;
  onNextPage?: () => void;
  hasMoreRows?: boolean;
  expanded: boolean;
  onToggleExpand: () => void;
  remainingCount?: number;
  pageSize?: number;
  mobileShowCount?: boolean;
  desktopShowCount?: boolean;
}

const TablePaginationFooter: React.FC<TablePaginationFooterProps> = ({
  currentPage,
  totalPages,
  totalElements,
  pageLoading = false,
  onPreviousPage,
  onNextPage,
  hasMoreRows = false,
  expanded,
  onToggleExpand,
  remainingCount,
  pageSize = 10,
  mobileShowCount = false,
  desktopShowCount = true,
}) => {
  const isFirstPage = currentPage === 0;
  const isLastPage = totalPages === 0 ? true : currentPage >= totalPages - 1;
  const startItem = currentPage * pageSize + 1;
  const endItem = Math.min((currentPage + 1) * pageSize, totalElements);

  return (
    <div className="flex items-center justify-between border-t border-stroke bg-white px-4 py-3 dark:border-strokedark dark:bg-boxdark sm:px-6">
      <div className="flex flex-1 justify-between sm:hidden">
        <Button
          onClick={onPreviousPage}
          disabled={isFirstPage || pageLoading}
          variant="outline"
          size="sm"
        >
          {pageLoading && currentPage > 0 ? 'Cargando...' : 'Anterior'}
        </Button>
        {hasMoreRows && (
          <ShowMoreButton
            expanded={expanded}
            onToggle={onToggleExpand}
            showCountLabel={mobileShowCount}
            remainingCount={remainingCount}
            expandIcon={<TbChevronDown className="h-4 w-4" />}
            collapseIcon={<TbChevronUp className="h-4 w-4" />}
          />
        )}
        <Button
          onClick={onNextPage}
          disabled={isLastPage || pageLoading}
          variant="outline"
          size="sm"
        >
          {pageLoading && currentPage < totalPages - 1 ? 'Cargando...' : 'Siguiente'}
        </Button>
      </div>
      <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
        <div>
          <p className="text-sm text-gray-700 dark:text-gray-400">
            {totalElements > 0 ? (
              <>
                Mostrando <span className="font-medium">{startItem}</span> a{' '}
                <span className="font-medium">{endItem}</span> de{' '}
                <span className="font-medium">{totalElements}</span> resultados
              </>
            ) : (
              <span>Sin resultados</span>
            )}
          </p>
        </div>
        {hasMoreRows && (
          <div className="flex justify-center">
            <ShowMoreButton
              expanded={expanded}
              onToggle={onToggleExpand}
              remainingCount={remainingCount}
              showCountLabel={desktopShowCount}
              expandIcon={<TbChevronDown className="h-4 w-4" />}
              collapseIcon={<TbChevronUp className="h-4 w-4" />}
            />
          </div>
        )}
        <div>
          <nav
            className="isolate inline-flex -space-x-px rounded-md shadow-sm"
            aria-label="Pagination"
          >
            <Button
              onClick={onPreviousPage}
              disabled={isFirstPage || pageLoading}
              variant="outline"
              size="sm"
              startIcon={<TbChevronLeft className="h-4 w-4" />}
              className="!rounded-r-none"
            >
              {pageLoading && currentPage > 0 ? 'Cargando...' : 'Anterior'}
            </Button>
            <span className="relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-900 ring-1 ring-inset ring-gray-300 dark:text-gray-400 dark:ring-gray-700">
              Página {currentPage + 1} de {totalPages}
            </span>
            <Button
              onClick={onNextPage}
              disabled={isLastPage || pageLoading}
              variant="outline"
              size="sm"
              endIcon={<TbChevronRight className="h-4 w-4" />}
              className="!rounded-l-none"
            >
              {pageLoading && currentPage < totalPages - 1 ? 'Cargando...' : 'Siguiente'}
            </Button>
          </nav>
        </div>
      </div>
    </div>
  );
};

export default TablePaginationFooter;
