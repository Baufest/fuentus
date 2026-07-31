import React, { useState, useEffect } from 'react';
import Modal from './Modal';
import { createVertical, updateVertical, getAllOrgN2FabricasWithVerticales } from '../../api/verticalesApi';
import { VerticalRequestDto, VerticalResponseDto, OrgN2FabricaResponseDto } from '../../types/vertical';
import { MdDelete } from 'react-icons/md';
import { AiOutlineLoading3Quarters } from 'react-icons/ai';
import Badge from '../Badges/Badge';
import { Tooltip } from '../Helpers/Tooltip';
import { IoMdAdd, IoMdCheckmark } from 'react-icons/io';

interface CreateVerticalModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  verticalToEdit?: VerticalResponseDto | null;
}

const CreateVerticalModal: React.FC<CreateVerticalModalProps> = ({
  isOpen,
  onClose,
  onSuccess,
  verticalToEdit
}) => {
  const [formData, setFormData] = useState<VerticalRequestDto>({
    name: '',
    refVertical: '',
    orgN2fabricaList: []
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [fabricas, setFabricas] = useState<OrgN2FabricaResponseDto[]>([]);
  const [isLoadingFabricas, setIsLoadingFabricas] = useState(false);
  const isEditMode = !!verticalToEdit;

  useEffect(() => {
    if (isOpen) {
      loadFabricas();
      // Si hay una vertical para editar, cargar sus datos
      if (verticalToEdit) {
        setFormData({
          name: verticalToEdit.name,
          refVertical: verticalToEdit.orgN2fabrica?.[0]?.ownerFactory || '',
          orgN2fabricaList: verticalToEdit.orgN2fabrica?.map(f => f.name) || []
        });
      } else {
        setFormData({ name: '', refVertical: '', orgN2fabricaList: [] });
      }
    }
  }, [isOpen, verticalToEdit]);

  const loadFabricas = async () => {
    try {
      setIsLoadingFabricas(true);
      const data = await getAllOrgN2FabricasWithVerticales();
      setFabricas(data);
    } catch (err) {
      console.error('Error al cargar fábricas:', err);
      alert('Error al cargar las fábricas disponibles.');
    } finally {
      setIsLoadingFabricas(false);
    }
  };

  const handleClose = () => {
    if (!isSubmitting) {
      setFormData({ name: '', refVertical: '', orgN2fabricaList: [] });
      onClose();
    }
  };

  const handleToggleFabrica = (fabricaName: string) => {
    setFormData(prev => {
      const isSelected = prev.orgN2fabricaList.includes(fabricaName);
      if (isSelected) {
        return {
          ...prev,
          orgN2fabricaList: prev.orgN2fabricaList.filter(f => f !== fabricaName)
        };
      } else {
        return {
          ...prev,
          orgN2fabricaList: [...prev.orgN2fabricaList, fabricaName]
        };
      }
    });
  };

  const handleRemoveFabrica = (fabricaName: string) => {
    console.log('Removing fabrica:', fabricaName);
    setFormData(prev => ({
      ...prev,
      orgN2fabricaList: prev.orgN2fabricaList.filter(f => f !== fabricaName)
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.name.trim()) {
      alert('El nombre de la vertical es obligatorio');
      return;
    }

    try {
      setIsSubmitting(true);
      if (isEditMode && verticalToEdit) {
        // Modo edición
        await updateVertical(verticalToEdit.id, formData);
      } else {
        // Modo creación
        await createVertical(formData);
      }
      setFormData({ name: '', refVertical: '', orgN2fabricaList: [] });
      onSuccess();
      onClose();
    } catch (err) {
      console.error(`Error al ${isEditMode ? 'actualizar' : 'crear'} vertical:`, err);
      alert(`Error al ${isEditMode ? 'actualizar' : 'crear'} la vertical. Por favor, intente nuevamente.`);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={handleClose}
      title={isEditMode ? 'Editar Vertical' : 'Nueva Vertical'}
      size="2xl"
      headerColor={isEditMode ? 'from-blue-500 to-blue-600' : 'from-green-500 to-green-600'}
      disabled={isSubmitting}
    >
      <form onSubmit={handleSubmit}>
        <div className="mb-6">
          <label htmlFor="verticalName" className="block text-gray-700 font-semibold mb-2">
            Nombre de la Vertical *
          </label>
          <input
            id="verticalName"
            type="text"
            value={formData.name}
            onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            placeholder="Ingrese el nombre de la vertical"
            required
            disabled={isSubmitting}
          />
        </div>

        <div className="mb-6">
          <label htmlFor="refVertical" className="block text-gray-700 font-semibold mb-2">
            Referente de Vertical
          </label>
          <input
            id="refVertical"
            type="text"
            value={formData.refVertical || ''}
            onChange={(e) => setFormData(prev => ({ ...prev, refVertical: e.target.value }))}
            className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            placeholder="Ingrese el referente de la vertical"
            disabled={isSubmitting}
          />
        </div>
        
        <div className="mb-6">
          <div className="block text-gray-700 font-semibold mb-3">
            Fábricas Disponibles
          </div>
          {isLoadingFabricas ? (
            <div className="flex items-center justify-center py-8">
              <AiOutlineLoading3Quarters className="animate-spin h-8 w-8 text-green-500" />
              <span className="ml-3 text-gray-600">Cargando fábricas...</span>
            </div>
          ) : (
            <div className="flex flex-wrap gap-2 mb-4">
              {fabricas
                .toSorted((a, b) => {
                  const aHasVertical = a.verticales && a.verticales.length > 0;
                  const bHasVertical = b.verticales && b.verticales.length > 0;
                  // Primero las sin vertical (false < true), luego las con vertical
                  if (aHasVertical === bHasVertical) return 0;
                  return aHasVertical ? 1 : -1;
                })
                .map((fabrica) => {
                const isSelected = formData.orgN2fabricaList.includes(fabrica.orgN2);
                const hasVertical = fabrica.verticales && fabrica.verticales.length > 0;
                const verticalName = hasVertical ? fabrica.verticales[0].name : '';
                
                // Determinar color del badge
                let badgeColor: 'success' | 'warning' = 'success';
                if (hasVertical) {
                  badgeColor = 'warning';
                }
                
                const badgeContent = (
                  <button
                    type="button"
                    onClick={() => handleToggleFabrica(fabrica.orgN2)}
                    disabled={isSubmitting}
                  >
                    <Badge
                      color={badgeColor}
                      variant={isSelected ? 'solid' : 'light'}
                      size="md"
                      startIcon={isSelected ?  <IoMdCheckmark className="w-4 h-4" /> : <IoMdAdd className="w-4 h-4" />}
                    >
                      {fabrica.orgN2}
                    </Badge>
                  </button>
                );

                return (
                  <div key={fabrica.id}>
                    {hasVertical && !isSelected ? (
                      <Tooltip text={`Pertenece a: ${verticalName}`}>
                        {badgeContent}
                      </Tooltip>
                    ) : (
                      badgeContent
                    )}
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {formData.orgN2fabricaList.length > 0 && (
          <div className="mb-6">
            <label className="block text-gray-700 font-semibold mb-3">
              Fábricas Seleccionadas ({formData.orgN2fabricaList.length})
            </label>
            <div className="space-y-2">
              {formData.orgN2fabricaList.map((fabrica) => (
                <div
                  key={fabrica}
                  className="flex items-center justify-between bg-gray-50 p-3 rounded-md border border-gray-200"
                >
                  <span className="text-gray-700">{fabrica}</span>
                  <button
                    type="button"
                    onClick={() => handleRemoveFabrica(fabrica)}
                    className="text-red-500 hover:text-red-700 transition-colors disabled:text-gray-400"
                    disabled={isSubmitting}
                  >
                    <MdDelete className="w-5 h-5" />
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
        
        <div className="flex justify-end gap-3">
          <button
            type="button"
            onClick={handleClose}
            className="px-6 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors disabled:bg-gray-100 disabled:text-gray-400"
            disabled={isSubmitting}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className={`px-6 py-2 ${isEditMode ? 'bg-blue-500 hover:bg-blue-600' : 'bg-green-500 hover:bg-green-600'} text-white rounded-md transition-colors disabled:bg-gray-400 flex items-center gap-2`}
            disabled={isSubmitting}
          >
            {isSubmitting && (
              <AiOutlineLoading3Quarters className="animate-spin h-4 w-4" />
            )}
            {isSubmitting && (isEditMode ? 'Actualizando...' : 'Guardando...')}
            {!isSubmitting && (isEditMode ? 'Actualizar' : 'Guardar')}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default CreateVerticalModal;
