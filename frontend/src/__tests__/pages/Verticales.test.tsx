import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Verticales from '../../pages/Verticales/Verticales';
import * as verticalesApi from '../../api/verticalesApi';
import { VerticalResponseDto } from '../../types/vertical';
import '@testing-library/jest-dom';

// Mock de las funciones de la API
jest.mock('../../api/verticalesApi');

// Mock del componente VerticalColumn
jest.mock('../../components/VerticalColumn/VerticalColumn', () => {
  return function MockVerticalColumn({ vertical, onEdit }: any) {
    return (
      <div data-testid={`vertical-column-${vertical.id}`}>
        <h3>{vertical.name}</h3>
        <button onClick={() => onEdit(vertical)}>Editar {vertical.name}</button>
      </div>
    );
  };
});

// Mock del componente CreateVerticalModal
jest.mock('../../components/Modal/CreateVerticalModal', () => {
  return function MockCreateVerticalModal({ isOpen, onClose, onSuccess, verticalToEdit }: any) {
    if (!isOpen) return null;
    return (
      <div data-testid="create-vertical-modal">
        <h2>{verticalToEdit ? 'Editar Vertical' : 'Nueva Vertical'}</h2>
        <button onClick={onClose}>Cerrar Modal</button>
        <button onClick={() => { onSuccess(); onClose(); }}>Guardar</button>
        {verticalToEdit && <p>Editando: {verticalToEdit.name}</p>}
      </div>
    );
  };
});

// Mock de react-icons
jest.mock('react-icons/io', () => ({
  IoMdAdd: () => <div>Add Icon</div>
}));

const mockedVerticalesApi = verticalesApi as jest.Mocked<typeof verticalesApi>;

describe('Verticales', () => {
  const mockVerticalesData: VerticalResponseDto[] = [
    {
      id: 1,
      name: 'Vertical A',
      orgN2fabrica: [
        {
          name: 'F001',
          ownerFactory: 'Owner 1'
        },
        {
          name: 'F002',
          ownerFactory: 'Owner 2'
        }
      ]
    },
    {
      id: 2,
      name: 'Vertical B',
      orgN2fabrica: [
        {
          name: 'F003',
          ownerFactory: 'Owner 3'
        }
      ]
    },
    {
      id: 3,
      name: 'Vertical C',
      orgN2fabrica: []
    }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    console.error = jest.fn();
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica.mockResolvedValue(mockVerticalesData);
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('renders the page title', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Verticales')).toBeInTheDocument();
    });
  });

  it('displays loading state initially', () => {
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica.mockImplementation(
      () => new Promise(() => {})
    );

    render(<Verticales />);
    
    expect(screen.getByText('Cargando verticales...')).toBeInTheDocument();
    expect(screen.getByText((_content, element) => {
      return element?.classList.contains('animate-spin') || false;
    })).toBeInTheDocument();
  });

  it('fetches and displays verticales on mount', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica).toHaveBeenCalledTimes(1);
      expect(screen.getByTestId('vertical-column-1')).toBeInTheDocument();
    });

    expect(screen.getByTestId('vertical-column-2')).toBeInTheDocument();
    expect(screen.getByTestId('vertical-column-3')).toBeInTheDocument();
  });

  it('displays vertical names correctly', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Vertical A')).toBeInTheDocument();
      expect(screen.getByText('Vertical B')).toBeInTheDocument();
      expect(screen.getByText('Vertical C')).toBeInTheDocument();
    });
  });

  it('displays error state when API call fails', async () => {
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica.mockRejectedValue(
      new Error('API Error')
    );

    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText(/Error al cargar las verticales/i)).toBeInTheDocument();
    });
  });

  it('displays retry button on error', async () => {
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica.mockRejectedValue(
      new Error('API Error')
    );

    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Reintentar')).toBeInTheDocument();
    });
  });

  it('retries fetching data when retry button is clicked', async () => {
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica
      .mockRejectedValueOnce(new Error('API Error'))
      .mockResolvedValueOnce(mockVerticalesData);

    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Reintentar')).toBeInTheDocument();
    });

    const retryButton = screen.getByText('Reintentar');
    fireEvent.click(retryButton);

    await waitFor(() => {
      expect(mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica).toHaveBeenCalledTimes(2);
      expect(screen.getByText('Vertical A')).toBeInTheDocument();
    });
  });

  it('displays empty state when no verticales are available', async () => {
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica.mockResolvedValue([]);

    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('No hay verticales disponibles.')).toBeInTheDocument();
    });
  });

  it('displays add new vertical button', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });
  });

  it('opens modal when add vertical button is clicked', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.click(addButton);

    expect(screen.getByTestId('create-vertical-modal')).toBeInTheDocument();
    expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
  });

  it('opens modal when pressing Enter on add vertical button', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.keyDown(addButton, { key: 'Enter' });

    expect(screen.getByTestId('create-vertical-modal')).toBeInTheDocument();
  });

  it('does not open modal when pressing other keys on add vertical button', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.keyDown(addButton, { key: 'Space' });

    expect(screen.queryByTestId('create-vertical-modal')).not.toBeInTheDocument();
  });

  it('closes modal when close button is clicked', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.click(addButton);

    expect(screen.getByTestId('create-vertical-modal')).toBeInTheDocument();

    const closeButton = screen.getByText('Cerrar Modal');
    fireEvent.click(closeButton);

    expect(screen.queryByTestId('create-vertical-modal')).not.toBeInTheDocument();
  });

  it('opens modal in edit mode when edit button is clicked', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Vertical A')).toBeInTheDocument();
    });

    const editButton = screen.getByText('Editar Vertical A');
    fireEvent.click(editButton);

    expect(screen.getByTestId('create-vertical-modal')).toBeInTheDocument();
    expect(screen.getByText('Editar Vertical')).toBeInTheDocument();
    expect(screen.getByText('Editando: Vertical A')).toBeInTheDocument();
  });

  it('refetches data when modal save is successful', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    expect(mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica).toHaveBeenCalledTimes(1);

    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.click(addButton);

    const saveButton = screen.getByText('Guardar');
    fireEvent.click(saveButton);

    await waitFor(() => {
      expect(mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica).toHaveBeenCalledTimes(2);
    });
  });

  it('clears verticalToEdit when opening modal to create new vertical', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Vertical A')).toBeInTheDocument();
    });

    // First, open in edit mode
    const editButton = screen.getByText('Editar Vertical A');
    fireEvent.click(editButton);
    expect(screen.getByText('Editando: Vertical A')).toBeInTheDocument();

    // Close modal
    const closeButton = screen.getByText('Cerrar Modal');
    fireEvent.click(closeButton);

    // Open in create mode
    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.click(addButton);

    expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
    expect(screen.queryByText(/Editando:/)).not.toBeInTheDocument();
  });

  it('renders verticales in a grid layout', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Vertical A')).toBeInTheDocument();
    });

    const grid = screen.getByText('Vertical A').closest('.grid');
    expect(grid).toBeInTheDocument();
    expect(grid).toHaveClass('grid-cols-1', 'lg:grid-cols-2');
  });

  it('passes vertical data to VerticalColumn component', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByTestId('vertical-column-1')).toBeInTheDocument();
      expect(screen.getByTestId('vertical-column-2')).toBeInTheDocument();
      expect(screen.getByTestId('vertical-column-3')).toBeInTheDocument();
    });
  });

  it('handles edit callback from VerticalColumn', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Editar Vertical B')).toBeInTheDocument();
    });

    const editButton = screen.getByText('Editar Vertical B');
    fireEvent.click(editButton);

    expect(screen.getByTestId('create-vertical-modal')).toBeInTheDocument();
    expect(screen.getByText('Editando: Vertical B')).toBeInTheDocument();
  });

  it('logs error to console when API call fails', async () => {
    const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();
    const error = new Error('API Error');
    mockedVerticalesApi.getAllVerticalesWithOrgN2Fabrica.mockRejectedValue(error);

    render(<Verticales />);
    
    await waitFor(() => {
      expect(consoleErrorSpy).toHaveBeenCalledWith('Error al cargar verticales:', error);
    });

    consoleErrorSpy.mockRestore();
  });

  it('closes modal and clears verticalToEdit after successful save', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Editar Vertical A')).toBeInTheDocument();
    });

    // Open in edit mode
    const editButton = screen.getByText('Editar Vertical A');
    fireEvent.click(editButton);
    expect(screen.getByText('Editando: Vertical A')).toBeInTheDocument();

    // Save
    const saveButton = screen.getByText('Guardar');
    fireEvent.click(saveButton);

    await waitFor(() => {
      expect(screen.queryByTestId('create-vertical-modal')).not.toBeInTheDocument();
    });

    // Wait for data to be loaded again
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    // Open modal again to verify verticalToEdit is cleared
    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.click(addButton);
    expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
  });

  it('maintains correct state through multiple modal open/close cycles', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Agregar Nueva Vertical')).toBeInTheDocument();
    });

    // Open for create
    const addButton = screen.getByText('Agregar Nueva Vertical');
    fireEvent.click(addButton);
    expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();

    // Close
    fireEvent.click(screen.getByText('Cerrar Modal'));
    expect(screen.queryByTestId('create-vertical-modal')).not.toBeInTheDocument();

    // Open for edit
    fireEvent.click(screen.getByText('Editar Vertical A'));
    expect(screen.getByText('Editando: Vertical A')).toBeInTheDocument();

    // Close
    fireEvent.click(screen.getByText('Cerrar Modal'));
    expect(screen.queryByTestId('create-vertical-modal')).not.toBeInTheDocument();

    // Open for create again
    fireEvent.click(addButton);
    expect(screen.getByText('Nueva Vertical')).toBeInTheDocument();
  });

  it('renders add icon in add vertical button', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      expect(screen.getByText('Add Icon')).toBeInTheDocument();
    });
  });

  it('applies correct styling to add vertical button', async () => {
    render(<Verticales />);
    
    await waitFor(() => {
      const addButton = screen.getByText('Agregar Nueva Vertical');
      const buttonContainer = addButton.closest('button');
      expect(buttonContainer).toHaveClass('cursor-pointer');
    });
  });
});
