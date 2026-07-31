import { render, screen, fireEvent } from '@testing-library/react';
import DashboardMultiSelect from '../../../components/Forms/DashboardMultiSelect';

describe('DashboardMultiSelect', () => {
  const mockOptions = ['Option 1', 'Option 2', 'Option 3', 'Option 4'];
  const mockOnSelectionChange = jest.fn();

  const defaultProps = {
    options: mockOptions,
    selectedValues: [],
    onSelectionChange: mockOnSelectionChange,
    placeholder: 'Select options',
    label: 'Test Label'
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders with label and placeholder', () => {
    render(<DashboardMultiSelect {...defaultProps} />);
    
    expect(screen.getByText('Test Label')).toBeInTheDocument();
    expect(screen.getByText('Select options')).toBeInTheDocument();
  });

  test('opens dropdown when clicked', () => {
    render(<DashboardMultiSelect {...defaultProps} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(screen.getByText('0 de 4 seleccionados')).toBeInTheDocument();
    mockOptions.forEach(option => {
      expect(screen.getByText(option)).toBeInTheDocument();
    });
  });

  test('closes dropdown when clicking outside', () => {
    render(
      <div>
        <DashboardMultiSelect {...defaultProps} />
        <div data-testid="outside">Outside element</div>
      </div>
    );
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(screen.getByText('0 de 4 seleccionados')).toBeInTheDocument();
    
    // Click outside
    fireEvent.click(screen.getByTestId('outside'));
    
    expect(screen.queryByText('0 de 4 seleccionados')).not.toBeInTheDocument();
  });

  test('selects and deselects options', () => {
    render(<DashboardMultiSelect {...defaultProps} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    // Select Option 1
    const option1 = screen.getByText('Option 1');
    fireEvent.click(option1);
    
    expect(mockOnSelectionChange).toHaveBeenCalledWith(['Option 1']);
  });

  test('handles multiple selections', () => {
    const propsWithSelected = {
      ...defaultProps,
      selectedValues: ['Option 1']
    };
    
    render(<DashboardMultiSelect {...propsWithSelected} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    // Select Option 2
    const option2 = screen.getByText('Option 2');
    fireEvent.click(option2);
    
    expect(mockOnSelectionChange).toHaveBeenCalledWith(['Option 1', 'Option 2']);
  });

  test('deselects option when already selected', () => {
    const propsWithSelected = {
      ...defaultProps,
      selectedValues: ['Option 1', 'Option 2']
    };
    
    render(<DashboardMultiSelect {...propsWithSelected} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    // Deselect Option 1
    const option1 = screen.getByText('Option 1');
    fireEvent.click(option1);
    
    expect(mockOnSelectionChange).toHaveBeenCalledWith(['Option 2']);
  });

  test('shows correct display text for multiple selections', () => {
    const propsWithMultiple = {
      ...defaultProps,
      selectedValues: ['Option 1', 'Option 2', 'Option 3']
    };
    
    render(<DashboardMultiSelect {...propsWithMultiple} />);
    
    expect(screen.getByText('3 seleccionados')).toBeInTheDocument();
  });

  test('shows single option name when only one selected', () => {
    const propsWithSingle = {
      ...defaultProps,
      selectedValues: ['Option 1']
    };
    
    render(<DashboardMultiSelect {...propsWithSingle} />);
    
    expect(screen.getByText('Option 1')).toBeInTheDocument();
  });

  test('clears all selections', () => {
    const propsWithSelected = {
      ...defaultProps,
      selectedValues: ['Option 1', 'Option 2']
    };
    
    render(<DashboardMultiSelect {...propsWithSelected} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    const clearButton = screen.getByText('Limpiar todo');
    fireEvent.click(clearButton);
    
    expect(mockOnSelectionChange).toHaveBeenCalledWith([]);
  });

  test('shows correct selection count in dropdown', () => {
    const propsWithSelected = {
      ...defaultProps,
      selectedValues: ['Option 1', 'Option 2']
    };
    
    render(<DashboardMultiSelect {...propsWithSelected} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(screen.getByText('2 de 4 seleccionados')).toBeInTheDocument();
  });

  test('shows checkboxes with correct checked state', () => {
    const propsWithSelected = {
      ...defaultProps,
      selectedValues: ['Option 1', 'Option 3']
    };
    
    render(<DashboardMultiSelect {...propsWithSelected} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    const checkboxes = screen.getAllByRole('checkbox');
    expect(checkboxes[0]).toBeChecked(); // Option 1
    expect(checkboxes[1]).not.toBeChecked(); // Option 2
    expect(checkboxes[2]).toBeChecked(); // Option 3
    expect(checkboxes[3]).not.toBeChecked(); // Option 4
  });

  test('does not show clear button when no selections', () => {
    render(<DashboardMultiSelect {...defaultProps} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(screen.queryByText('Limpiar todo')).not.toBeInTheDocument();
  });

  test('handles empty options array', () => {
    const propsWithEmpty = {
      ...defaultProps,
      options: []
    };
    
    render(<DashboardMultiSelect {...propsWithEmpty} />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    expect(screen.getByText('0 de 0 seleccionados')).toBeInTheDocument();
  });

  test('rotates arrow icon when dropdown is open', () => {
    render(<DashboardMultiSelect {...defaultProps} />);
    
    const button = screen.getByRole('button');
    const arrow = button.querySelector('svg');
    
    expect(arrow).not.toHaveClass('rotate-180');
    
    fireEvent.click(button);
    
    expect(arrow).toHaveClass('rotate-180');
  });
});
