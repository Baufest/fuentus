import React, { ReactNode } from 'react';
import { Link } from 'react-router-dom';

interface StatsCardProps {
  value: string | number;
  label: string;
  linkTo?: string;
  linkIcon?: ReactNode;
  className?: string;
}

const StatsCard: React.FC<StatsCardProps> = ({ 
  value, 
  label, 
  linkTo, 
  linkIcon, 
  className = "" 
}) => {
  const baseClasses = "rounded-sm border border-stroke bg-white py-6 px-7.5 shadow-default dark:border-strokedark dark:bg-boxdark";
  
  if (linkTo) {
    return (
      <div className={`flex justify-between ${baseClasses} ${className}`}>
        <div>
          <p className="text-title-md font-bold text-black dark:text-white">
            {value}
          </p>
          <p className="text-sm text-meta-3">{label}</p>
        </div>
        <Link to={linkTo} className="text-blue-500 hover:underline">
          {linkIcon}
        </Link>
      </div>
    );
  }

  return (
    <div className={`${baseClasses} ${className}`}>
      <div className="text-title-md font-bold text-black dark:text-white">
        {value}
      </div>
      <div className="text-sm text-meta-3">{label}</div>
    </div>
  );
};

export default StatsCard;