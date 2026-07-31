import React from 'react';
import { VerticalResponseDto } from '../../types/vertical';
import { HiOfficeBuilding } from 'react-icons/hi';
import { FaEdit } from 'react-icons/fa';

interface VerticalColumnProps {
  vertical: VerticalResponseDto;
  onEdit: (vertical: VerticalResponseDto) => void;
}

const VerticalColumn: React.FC<VerticalColumnProps> = ({ vertical, onEdit }) => {
  return (
    <div
      key={vertical.id}
      className="bg-white rounded-lg shadow-lg overflow-hidden border border-gray-200 hover:shadow-xl transition-shadow duration-300"
    >
      {/* Datos de la Vertical */}
      <div className="flex justify-between bg-gradient-to-r from-blue-500 to-blue-600 p-6">
        <div>
        <h2 className="text-2xl font-bold text-white mb-2">
          {vertical.name}
        </h2>
        <p className="text-blue-100 text-sm">
          ID: {vertical.id}
        </p>
        </div>
        <button
          onClick={() => onEdit(vertical)}
          className="p-2 rounded-lg hover:bg-blue-700 transition-colors"
          aria-label="Editar vertical"
        >
            <FaEdit className="size-7 text-white hover:text-gray-200 transition-colors" />
        </button>
      </div>

      {/* Datos de las Fábricas */}
      <div className="p-6">
        <h3 className="text-lg font-semibold text-gray-700 mb-4 flex items-center">
          <HiOfficeBuilding className="w-5 h-5 mr-2" />
          Fábricas ({vertical.orgN2fabrica?.length || 0})
        </h3>

        {!vertical.orgN2fabrica || vertical.orgN2fabrica.length === 0 ? (
          <p className="text-gray-500 text-sm italic">
            No hay fábricas asociadas a esta vertical.
          </p>
        ) : (
          <div className="space-y-4">
            {vertical.orgN2fabrica.map((fabrica) => (
              <div
                key={fabrica.name}
                className="border border-gray-200 rounded-md p-4 bg-gray-50 hover:bg-gray-100 transition-colors duration-200"
              >
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <h4 className="font-semibold text-gray-800 mb-1">
                      {fabrica.name}
                    </h4>
                    <p className="text-sm text-gray-600 mb-2">
                      FTL: {fabrica.ownerFactory}
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default VerticalColumn;
