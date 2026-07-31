import React, { useState, useEffect, useRef } from 'react';

interface DashboardMultiSelectProps {
  options: string[];
  selectedValues: string[];
  onSelectionChange: (selected: string[]) => void;
  placeholder: string;
  label: string;
}

const DashboardMultiSelect: React.FC<DashboardMultiSelectProps> = ({
  options,
  selectedValues,
  onSelectionChange,
  placeholder,
  label
}) => {
  const [show, setShow] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const clickHandler = ({ target }: MouseEvent) => {
      if (!dropdownRef.current) return;
      if (
        !show ||
        dropdownRef.current.contains(target as Node)
      )
        return;
      setShow(false);
    };
    document.addEventListener('click', clickHandler);
    return () => document.removeEventListener('click', clickHandler);
  });

  const handleSelect = (value: string) => {
    let newSelected;
    if (selectedValues.includes(value)) {
      newSelected = selectedValues.filter(item => item !== value);
    } else {
      newSelected = [...selectedValues, value];
    }
    onSelectionChange(newSelected);
  };

  const handleClearAll = () => {
    onSelectionChange([]);
  };

  const getDisplayText = () => {
    if (selectedValues.length === 0) return placeholder;
    if (selectedValues.length === 1) return selectedValues[0];
    return `${selectedValues.length} seleccionados`;
  };

  return (
    <div className="relative" ref={dropdownRef}>
      <label className="mb-3 block text-sm font-medium text-black dark:text-white">
        {label}
      </label>
      <div>
        <button
          type="button"
          onClick={() => setShow(!show)}
          className="relative w-full rounded-lg border border-stroke bg-transparent py-4 pl-6 pr-10 text-black outline-none focus:border-primary focus-visible:shadow-none dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary text-left flex items-center justify-between"
        >
          <span className="truncate">{getDisplayText()}</span>
          <span className="absolute right-4 top-1/2 z-30 -translate-y-1/2">
            <svg
              className={`fill-current transition-transform duration-200 ${
                show ? 'rotate-180' : ''
              }`}
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <g opacity="0.8">
                <path
                  fillRule="evenodd"
                  clipRule="evenodd"
                  d="M5.29289 8.29289C5.68342 7.90237 6.31658 7.90237 6.70711 8.29289L12 13.5858L17.2929 8.29289C17.6834 7.90237 18.3166 7.90237 18.7071 8.29289C19.0976 8.68342 19.0976 9.31658 18.7071 9.70711L12.7071 15.7071C12.3166 16.0976 11.6834 16.0976 11.2929 15.7071L5.29289 9.70711C4.90237 9.31658 4.90237 8.68342 5.29289 8.29289Z"
                  fill=""
                ></path>
              </g>
            </svg>
          </span>
        </button>

        {show && (
          <div className="absolute top-full left-0 z-40 w-full rounded-md border border-stroke bg-white py-3 shadow-lg dark:border-strokedark dark:bg-boxdark">
            <div className="flex items-center justify-between px-4 py-2">
              <span className="text-sm font-medium text-black dark:text-white">
                {selectedValues.length} de {options.length} seleccionados
              </span>
              {selectedValues.length > 0 && (
                <button
                  type="button"
                  onClick={handleClearAll}
                  className="text-sm text-primary hover:text-primary-dark"
                >
                  Limpiar todo
                </button>
              )}
            </div>
            <div className="max-h-60 overflow-y-auto">
              {options.map((option) => (
                <div
                  key={option}
                  className="flex cursor-pointer select-none items-center px-4 py-2 text-sm text-black hover:bg-gray-100 dark:text-white dark:hover:bg-meta-4"
                  onClick={() => handleSelect(option)}
                >
                  <input
                    type="checkbox"
                    className="mr-3 h-4 w-4 rounded border-gray-300 text-primary focus:ring-primary"
                    checked={selectedValues.includes(option)}
                    readOnly
                  />
                  <span className="flex-1">{option}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default DashboardMultiSelect;
