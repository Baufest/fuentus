import React, { useEffect, useState } from 'react';
import { getAllVerticalesWithOrgN2Fabrica } from '../../api/verticalesApi';
import { VerticalResponseDto } from '../../types/vertical';
import CreateVerticalModal from '../../components/Modal/CreateVerticalModal';
import VerticalColumn from '../../components/VerticalColumn/VerticalColumn';
import { IoMdAdd } from 'react-icons/io';

const Verticales: React.FC = () => {
  const [verticales, setVerticales] = useState<VerticalResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [verticalToEdit, setVerticalToEdit] = useState<VerticalResponseDto | null>(null);

  useEffect(() => {
    fetchVerticales();
  }, []);

  const fetchVerticales = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getAllVerticalesWithOrgN2Fabrica();
      setVerticales(data);
    } catch (err) {
      console.error('Error al cargar verticales:', err);
      setError('Error al cargar las verticales. Por favor, intente nuevamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = () => {
    setVerticalToEdit(null);
    setIsModalOpen(true);
  };

  const handleEditVertical = (vertical: VerticalResponseDto) => {
    setVerticalToEdit(vertical);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setVerticalToEdit(null);
  };

  const handleSuccess = () => {
    fetchVerticales();
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto"></div>
          <p className="mt-4 text-gray-600">Cargando verticales...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="flex flex-col bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
          <strong className="font-bold">Error: </strong>
          <span className="block sm:inline">{error}</span>
          <button
            onClick={fetchVerticales}
            className="mt-4 bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
          >
            Reintentar
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8 text-gray-800">Verticales</h1>
      
      {verticales.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">No hay verticales disponibles.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {verticales.map((vertical) => (
            <VerticalColumn key={vertical.id} vertical={vertical} onEdit={handleEditVertical} />
          ))}
          
          {/* Tarjeta para agregar nueva vertical */}
          <button
            onClick={handleOpenModal}
            onKeyDown={(e) => e.key === 'Enter' && handleOpenModal()}
            tabIndex={0}
            className="bg-white rounded-lg shadow-lg overflow-hidden border-2 border-green-400 hover:border-green-500 hover:shadow-xl transition-all duration-300 cursor-pointer"
          >
            <div className="bg-gradient-to-r from-green-500 to-green-600 p-6 h-full flex flex-col items-center justify-center min-h-[300px]">
              <div className="w-24 h-24 rounded-full bg-white flex items-center justify-center mb-4 shadow-lg hover:scale-110 transition-transform duration-300">
                <IoMdAdd className="w-12 h-12 text-green-600" />
              </div>
              <h2 className="text-2xl font-bold text-white text-center">
                Agregar Nueva Vertical
              </h2>
            </div>
          </button>
        </div>
      )}
      
      {/* Modal para crear/editar vertical */}
      <CreateVerticalModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSuccess={handleSuccess}
        verticalToEdit={verticalToEdit}
      />
    </div>
  );
};

export default Verticales;
