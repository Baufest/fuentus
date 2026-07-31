import { ReactNode } from 'react';
import Breadcrumb from '../../Breadcrumbs/Breadcrumb';

interface TableCardProps {
  title: string;
  children: ReactNode;
  footer?: ReactNode;
  className?: string;
}

const baseClasses = 'rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1';

const TableCard: React.FC<TableCardProps> = ({ title, children, footer, className }) => {
  return (
    <div className={`${baseClasses} ${className ?? ''}`}>
      <Breadcrumb pageName={title} />
      <div className="max-w-full">{children}</div>
      {footer}
    </div>
  );
};

export default TableCard;
