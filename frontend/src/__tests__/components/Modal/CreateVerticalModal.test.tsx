import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import CreateVerticalModal from '../../../components/Modal/CreateVerticalModal';
import * as verticalesApi from '../../../api/verticalesApi';
import { VerticalResponseDto, OrgN2FabricaResponseDto } from '../../../types/vertical';

// Mock de las funciones de la API
jest.mock('../../../api/verticalesApi');

const mockedVerticalesApi = verticalesApi as jest.Mocked<typeof verticalesApi>;

describe('CreateVerticalModal', () => {
  const mockOnClose = jest.fn();
  const mockOnSuccess = jest.fn();

  const mockFabricas: OrgN2FabricaResponseDto[] = [
    {
      id: 1,
      orgN2: 'F001',
      verticales: []
    },
    {
      id: 2,
      orgN2: 'F002',
      verticales: [
        {
          id: 10,
          name: 'Vertical Existente'
        }
      ]
    },
    {
      id: 3,
      orgN2: 'F003',
      verticales: []
    }
  ];

  const mockVerticalToEdit: VerticalResponseDto = {
    id: 1,
    name: 'Vertical Test',
    orgN2fabrica: [
      {
        name: 'F001',
        ownerFactory: 'Owner 1'
      }
    ]
  };

  beforeEach(() => {
    jest.clearAllMocks();
    mockedVerticalesApi.getAllOrgN2FabricasWithVerticales.mockResolvedValue(mockFabricas);
    global.alert = jest.fn();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('renders modal when isOpen is true', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });
  });

  test('does not render modal when isOpen is false', () => {
    render(
      <CreateVerticalModal
        isOpen={false}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    expect(screen.queryByText('Nueva Vertical')).not.toBeInTheDocument();
  });

  test('loads and displays fabricas when modal opens', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(mockedVerticalesApi.getAllOrgN2FabricasWithVerticales).toHaveBeenCalled();
    });

    await waitFor(() => {
      expect(screen.getByText('F001')).toBeInTheDocument();
      expect(screen.getByText('F002')).toBeInTheDocument();
      expect(screen.getByText('F003')).toBeInTheDocument();
    });
  });

  test('displays loading state while fetching fabricas', async () => {
    mockedVerticalesApi.getAllOrgN2FabricasWithVerticales.mockImplementation(
      () => new Promise(resolve => setTimeout(() => resolve(mockFabricas), 100))
    );

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    expect(screen.getByText('Cargando fábricas...')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.queryByText('Cargando fábricas...')).not.toBeInTheDocument();
    });
  });

  test('renders with edit mode title when verticalToEdit is provided', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
        verticalToEdit={mockVerticalToEdit}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Editar Vertical')).toBeInTheDocument();
    });
  });

  test('populates form with vertical data in edit mode', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
        verticalToEdit={mockVerticalToEdit}
      />
    );

    await waitFor(() => {
      const nameInput = screen.getByLabelText('Nombre de la Vertical *') as HTMLInputElement;
      expect(nameInput.value).toBe('Vertical Test');
    });

    await waitFor(() => {
      const refInput = screen.getByLabelText('Referente de Vertical') as HTMLInputElement;
      expect(refInput.value).toBe('Owner 1');
    });
  });

  test('allows user to input vertical name', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    const nameInput = screen.getByLabelText('Nombre de la Vertical *') as HTMLInputElement;
    fireEvent.change(nameInput, { target: { value: 'Nueva Vertical Test' } });

    expect(nameInput.value).toBe('Nueva Vertical Test');
  });

  test('allows user to input referente de vertical', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    const refInput = screen.getByLabelText('Referente de Vertical') as HTMLInputElement;
    fireEvent.change(refInput, { target: { value: 'Referente Test' } });

    expect(refInput.value).toBe('Referente Test');
  });

  test('allows user to select a fabrica', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('F001')).toBeInTheDocument();
    });

    const fabricaButton = screen.getByText('F001').closest('button');
    fireEvent.click(fabricaButton!);

    await waitFor(() => {
      expect(screen.getByText('Fábricas Seleccionadas (1)')).toBeInTheDocument();
    });
  });

  test('allows user to remove a selected fabrica', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('F001')).toBeInTheDocument();
    });

    // Seleccionar fábrica
    const fabricaButton = screen.getByText('F001').closest('button');
    fireEvent.click(fabricaButton!);

    await waitFor(() => {
      expect(screen.getByText('Fábricas Seleccionadas (1)')).toBeInTheDocument();
    });

    // Remover fábrica
    const removeButtons = screen.getAllByRole('button').filter(btn => 
      btn.querySelector('svg') && btn.querySelector('svg')?.getAttribute('class')?.includes('w-5')
    );
    
    const removeButton = removeButtons.find(btn => {
      const parent = btn.closest('div');
      return parent?.textContent?.includes('F001');
    });

    if (removeButton) {
      fireEvent.click(removeButton);
    }

    await waitFor(() => {
      expect(screen.queryByText('Fábricas Seleccionadas (1)')).not.toBeInTheDocument();
    });
  });

  test('creates a new vertical successfully', async () => {
    mockedVerticalesApi.createVertical.mockResolvedValue({
      id: 1,
      name: 'Nueva Vertical',
      orgN2fabrica: []
    });

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    // Llenar formulario
    const nameInput = screen.getByLabelText('Nombre de la Vertical *');
    fireEvent.change(nameInput, { target: { value: 'Nueva Vertical' } });

    // Seleccionar una fábrica
    await waitFor(() => {
      expect(screen.getByText('F001')).toBeInTheDocument();
    });

    const fabricaButton = screen.getByText('F001').closest('button');
    fireEvent.click(fabricaButton!);

    // Enviar formulario
    const submitButton = screen.getByText('Guardar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockedVerticalesApi.createVertical).toHaveBeenCalledWith({
        name: 'Nueva Vertical',
        refVertical: '',
        orgN2fabricaList: ['F001']
      });
    });

    await waitFor(() => {
      expect(mockOnSuccess).toHaveBeenCalled();
      expect(mockOnClose).toHaveBeenCalled();
    });
  });

  test('updates a vertical successfully in edit mode', async () => {
    mockedVerticalesApi.updateVertical.mockResolvedValue({
      id: 1,
      name: 'Vertical Actualizada',
      orgN2fabrica: []
    });

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
        verticalToEdit={mockVerticalToEdit}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Editar Vertical')).toBeInTheDocument();
    });

    // Modificar nombre
    const nameInput = screen.getByLabelText('Nombre de la Vertical *');
    fireEvent.change(nameInput, { target: { value: 'Vertical Actualizada' } });

    // Enviar formulario
    const submitButton = screen.getByText('Actualizar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockedVerticalesApi.updateVertical).toHaveBeenCalledWith(
        1,
        expect.objectContaining({
          name: 'Vertical Actualizada'
        })
      );
    });

    await waitFor(() => {
      expect(mockOnSuccess).toHaveBeenCalled();
      expect(mockOnClose).toHaveBeenCalled();
    });
  });

  test('shows alert when vertical name is empty', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    // Simular que el input tiene un valor vacío después de trim
    const nameInput = screen.getByLabelText('Nombre de la Vertical *') as HTMLInputElement;
    fireEvent.change(nameInput, { target: { value: '   ' } });

    const submitButton = screen.getByText('Guardar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith('El nombre de la vertical es obligatorio');
    });

    expect(mockedVerticalesApi.createVertical).not.toHaveBeenCalled();
  });

  test('handles API error when creating vertical', async () => {
    mockedVerticalesApi.createVertical.mockRejectedValue(new Error('API Error'));

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    // Llenar formulario
    const nameInput = screen.getByLabelText('Nombre de la Vertical *');
    fireEvent.change(nameInput, { target: { value: 'Test Vertical' } });

    // Enviar formulario
    const submitButton = screen.getByText('Guardar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith(
        'Error al crear la vertical. Por favor, intente nuevamente.'
      );
    });

    expect(mockOnSuccess).not.toHaveBeenCalled();
    expect(mockOnClose).not.toHaveBeenCalled();
  });

  test('handles API error when updating vertical', async () => {
    mockedVerticalesApi.updateVertical.mockRejectedValue(new Error('API Error'));

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
        verticalToEdit={mockVerticalToEdit}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Editar Vertical')).toBeInTheDocument();
    });

    // Enviar formulario
    const submitButton = screen.getByText('Actualizar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith(
        'Error al actualizar la vertical. Por favor, intente nuevamente.'
      );
    });

    expect(mockOnSuccess).not.toHaveBeenCalled();
    expect(mockOnClose).not.toHaveBeenCalled();
  });

  test('handles API error when loading fabricas', async () => {
    const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
    mockedVerticalesApi.getAllOrgN2FabricasWithVerticales.mockRejectedValue(
      new Error('API Error')
    );

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(global.alert).toHaveBeenCalledWith('Error al cargar las fábricas disponibles.');
    });

    consoleErrorSpy.mockRestore();
  });

  test('closes modal when cancel button is clicked', async () => {
    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    const cancelButton = screen.getByText('Cancelar');
    fireEvent.click(cancelButton);

    expect(mockOnClose).toHaveBeenCalled();
  });

  test('disables form inputs during submission', async () => {
    mockedVerticalesApi.createVertical.mockImplementation(
      () => new Promise(resolve => setTimeout(() => resolve({
        id: 1,
        name: 'Test',
        orgN2fabrica: []
      }), 100))
    );

    render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    // Llenar formulario
    const nameInput = screen.getByLabelText('Nombre de la Vertical *');
    fireEvent.change(nameInput, { target: { value: 'Test Vertical' } });

    // Enviar formulario
    const submitButton = screen.getByText('Guardar');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('Guardando...')).toBeInTheDocument();
    });

    // Verificar que los inputs están deshabilitados
    expect(nameInput).toBeDisabled();

    await waitFor(() => {
      expect(mockOnSuccess).toHaveBeenCalled();
    });
  });

  test('resets form when modal is closed and reopened', async () => {
    const { rerender } = render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    });

    // Llenar formulario
    const nameInput = screen.getByLabelText('Nombre de la Vertical *') as HTMLInputElement;
    fireEvent.change(nameInput, { target: { value: 'Test Vertical' } });

    expect(nameInput.value).toBe('Test Vertical');

    // Cerrar modal
    rerender(
      <CreateVerticalModal
        isOpen={false}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    // Reabrir modal
    rerender(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      const newNameInput = screen.getByLabelText('Nombre de la Vertical *') as HTMLInputElement;
      expect(newNameInput.value).toBe('');
    });
  });

  test('displays submit button text correctly based on mode and state', async () => {
    const { rerender } = render(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Guardar')).toBeInTheDocument();
    });

    // Modo edición
    rerender(
      <CreateVerticalModal
        isOpen={true}
        onClose={mockOnClose}
        onSuccess={mockOnSuccess}
        verticalToEdit={mockVerticalToEdit}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Actualizar')).toBeInTheDocument();
    });
  });
});
