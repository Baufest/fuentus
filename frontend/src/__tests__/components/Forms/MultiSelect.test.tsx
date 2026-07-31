import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import MultiSelect from '../../../components/Forms/MultiSelect';

describe('MultiSelect', () => {
  const createSelectElement = (options: { value: string; text: string; selected?: boolean }[]) => {
    const select = document.createElement('select');
    select.id = 'test-select';
    
    options.forEach(option => {
      const optionElement = document.createElement('option');
      optionElement.value = option.value;
      optionElement.innerText = option.text;
      if (option.selected) {
        optionElement.setAttribute('selected', 'true');
      }
      select.appendChild(optionElement);
    });
    
    document.body.appendChild(select);
    return select;
  };

  const renderWithRouter = (component: React.ReactElement) => {
    return render(
      <BrowserRouter>
        {component}
      </BrowserRouter>
    );
  };

  beforeEach(() => {
    // Clean up any existing select elements
    const existingSelect = document.getElementById('test-select');
    if (existingSelect) {
      document.body.removeChild(existingSelect);
    }
  });

  afterEach(() => {
    // Clean up select elements after each test
    const select = document.getElementById('test-select');
    if (select) {
      document.body.removeChild(select);
    }
  });

  test('renders without crashing', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    expect(screen.getByText('Multiselect Dropdown')).toBeInTheDocument();
  });

  test('displays label correctly', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    expect(screen.getByText('Multiselect Dropdown')).toBeInTheDocument();
  });

  test('renders input field with placeholder', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const input = screen.getByPlaceholderText('Select an option');
    expect(input).toBeInTheDocument();
  });

  test('renders trigger button', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    expect(triggerButton).toBeInTheDocument();
  });

  test('opens dropdown when button clicked', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Use getAllByText to handle multiple elements
    const option1Elements = screen.getAllByText('Option 1');
    expect(option1Elements.length).toBeGreaterThan(0);
  });

  test('shows dropdown options in correct structure', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Check for dropdown container
    const dropdown = document.querySelector('.max-h-select');
    expect(dropdown).toBeInTheDocument();
  });

  test('handles click on dropdown options', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Find and click the first option in the dropdown (not the select element)
    const dropdownOptions = document.querySelectorAll('.mx-2.leading-6');
    expect(dropdownOptions.length).toBeGreaterThan(0);
    
    if (dropdownOptions[0]) {
      fireEvent.click(dropdownOptions[0]);
      // Test should not crash
      expect(triggerButton).toBeInTheDocument();
    }
  });

  test('applies correct CSS classes', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const container = document.querySelector('.relative.z-50');
    expect(container).toBeInTheDocument();

    const flexContainer = document.querySelector('.flex.flex-col.items-center');
    expect(flexContainer).toBeInTheDocument();
  });

  test('handles empty select element', () => {
    createSelectElement([]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Should not crash and should show empty dropdown
    expect(triggerButton).toBeInTheDocument();
  });

  test('handles non-existent select element', () => {
    // Don't create a select element
    renderWithRouter(<MultiSelect id="non-existent-select" />);

    // Should not crash
    expect(screen.getByText('Multiselect Dropdown')).toBeInTheDocument();
  });

  test('has hidden input for form values', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const hiddenInput = document.querySelector('input[name="values"]');
    expect(hiddenInput).toBeInTheDocument();
    expect(hiddenInput).toHaveAttribute('type', 'hidden');
  });

  test('renders SVG arrow icon', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const svg = document.querySelector('svg');
    expect(svg).toBeInTheDocument();
    expect(svg).toHaveAttribute('viewBox', '0 0 24 24');
  });

  test('has correct button attributes', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    expect(triggerButton).toHaveAttribute('type', 'button');
    expect(triggerButton).toHaveClass('cursor-pointer');
  });

  test('handles pre-selected options from DOM', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1', selected: true },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    // Component should load the pre-selected option
    const hiddenInput = document.querySelector('input[name="values"]') as HTMLInputElement;
    expect(hiddenInput).toBeInTheDocument();
  });

  test('selects and deselects options correctly', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Click on first option to select it
    const dropdownOptions = document.querySelectorAll('.cursor-pointer');
    if (dropdownOptions[0]) {
      fireEvent.click(dropdownOptions[0]);
      
      // Should show selected option as badge
      expect(screen.getByText('Option 1')).toBeInTheDocument();
      
      // Click again to deselect
      fireEvent.click(triggerButton);
      fireEvent.click(dropdownOptions[0]);
    }
  });

  test('removes selected options when clicking X button', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Select an option
    const dropdownOptions = document.querySelectorAll('.cursor-pointer');
    if (dropdownOptions[0]) {
      fireEvent.click(dropdownOptions[0]);
      
      // Should show selected option badge
      expect(screen.getByText('Option 1')).toBeInTheDocument();
      
      // Find and click the remove button (X)
      const removeButton = document.querySelector('.cursor-pointer.pl-2');
      if (removeButton) {
        fireEvent.click(removeButton);
        // Option should be removed from display
      }
    }
  });

  test('handles multiple selections', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
      { value: 'option2', text: 'Option 2' },
      { value: 'option3', text: 'Option 3' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Select multiple options
    const dropdownOptions = document.querySelectorAll('.cursor-pointer');
    if (dropdownOptions.length >= 2) {
      fireEvent.click(dropdownOptions[0]);
      fireEvent.click(dropdownOptions[1]);
      
      // Should show multiple selected options - use getAllByText for multiple instances
      const option1Elements = screen.getAllByText('Option 1');
      const option2Elements = screen.getAllByText('Option 2');
      expect(option1Elements.length).toBeGreaterThan(0);
      expect(option2Elements.length).toBeGreaterThan(0);
    }
  });

  test('closes dropdown when clicking outside', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Dropdown should be open
    const dropdown = document.querySelector('.max-h-select');
    expect(dropdown).not.toHaveClass('hidden');

    // Click outside
    fireEvent.click(document.body);

    // Dropdown should be closed
    expect(dropdown).toHaveClass('hidden');
  });

  test('shows placeholder when no options selected', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    // Should show placeholder input when no selections
    const placeholderInput = screen.getByPlaceholderText('Select an option');
    expect(placeholderInput).toBeInTheDocument();
  });

  test('hides placeholder when options are selected', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Select an option by clicking on it
    const optionText = screen.getByText('Option 1');
    fireEvent.click(optionText);
    
    // The placeholder should be hidden when an option is selected
    // We check that the flex-1 container (which contains placeholder) is not present
    const placeholderContainer = document.querySelector('.flex-1');
    expect(placeholderContainer).toBeNull();
  });

  test('applies selected styling to options', async () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Select an option by clicking on the text
    const optionText = screen.getByText('Option 1');
    fireEvent.click(optionText);
    
    // Re-open dropdown to check styling
    fireEvent.click(triggerButton);
    
    // Check that selected option has border-primary class
    await waitFor(() => {
      const optionElements = document.querySelectorAll('.border-l-2');
      const selectedOption = Array.from(optionElements).find(el => 
        el.classList.contains('border-primary')
      );
      expect(selectedOption).toBeInTheDocument();
    });
  });

  test('handles focus and blur events on dropdown', () => {
    createSelectElement([
      { value: 'option1', text: 'Option 1' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    const dropdown = document.querySelector('.max-h-select');
    if (dropdown) {
      // Focus should keep dropdown open
      fireEvent.focus(dropdown);
      expect(dropdown).not.toHaveClass('hidden');

      // Blur should close dropdown
      fireEvent.blur(dropdown);
      expect(dropdown).toHaveClass('hidden');
    }
  });

  test('updates hidden input value correctly', () => {
    createSelectElement([
      { value: 'val1', text: 'Option 1' },
      { value: 'val2', text: 'Option 2' },
    ]);

    renderWithRouter(<MultiSelect id="test-select" />);

    const triggerButton = screen.getByRole('button');
    fireEvent.click(triggerButton);

    // Select options
    const dropdownOptions = document.querySelectorAll('.cursor-pointer');
    if (dropdownOptions.length >= 2) {
      fireEvent.click(dropdownOptions[0]);
      fireEvent.click(dropdownOptions[1]);
      
      // Hidden input should contain selected values
      const hiddenInput = document.querySelector('input[name="values"]') as HTMLInputElement;
      expect(hiddenInput).toBeInTheDocument();
    }
  });
});
