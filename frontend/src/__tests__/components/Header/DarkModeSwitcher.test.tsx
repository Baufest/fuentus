import { render, screen, fireEvent } from '@testing-library/react';
import DarkModeSwitcher from '../../../components/Header/DarkModeSwitcher';

// Mock useColorMode hook
const mockSetColorMode = jest.fn();
const mockColorMode = jest.fn();

jest.mock('../../../hooks/useColorMode', () => ({
  __esModule: true,
  default: () => [mockColorMode(), mockSetColorMode],
}));

describe('DarkModeSwitcher', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders with light mode by default', () => {
    mockColorMode.mockReturnValue('light');
    
    render(<DarkModeSwitcher />);
    
    expect(screen.getByText('Modo')).toBeInTheDocument();
    expect(screen.getByText('Claro')).toBeInTheDocument();
    
    const button = screen.getByRole('button');
    expect(button).toBeInTheDocument();
  });

  test('renders with dark mode', () => {
    mockColorMode.mockReturnValue('dark');
    
    render(<DarkModeSwitcher />);
    
    expect(screen.getByText('Oscuro')).toBeInTheDocument();
  });

  test('opens dropdown when clicked', () => {
    mockColorMode.mockReturnValue('light');
    
    render(<DarkModeSwitcher />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    // Should show both options in dropdown
    expect(screen.getAllByText('Claro')).toHaveLength(2); // One in button, one in dropdown
    expect(screen.getAllByText('Oscuro')).toHaveLength(1); // One in dropdown
  });

  test('closes dropdown when clicking outside', () => {
    mockColorMode.mockReturnValue('light');
    
    render(<DarkModeSwitcher />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    // Click outside
    fireEvent.mouseDown(document.body);
    
    // Should close dropdown
    expect(screen.getAllByText('Claro')).toHaveLength(1); // Only in button
  });

  test('switches to dark mode when dark option is clicked', () => {
    mockColorMode.mockReturnValue('light');
    
    render(<DarkModeSwitcher />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    const darkButtons = screen.getAllByText('Oscuro');
    const darkButton = darkButtons[0]; // The dropdown option
    fireEvent.click(darkButton);
    
    expect(mockSetColorMode).toHaveBeenCalledWith('dark');
  });

  test('switches to light mode when light option is clicked', () => {
    mockColorMode.mockReturnValue('dark');
    
    render(<DarkModeSwitcher />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    const lightButton = screen.getByText('Claro');
    fireEvent.click(lightButton);
    
    expect(mockSetColorMode).toHaveBeenCalledWith('light');
  });

  test('handles setColorMode not being a function gracefully', () => {
    mockColorMode.mockReturnValue('light');
    // Override the mock to return null for setColorMode
    const useColorMode = require('../../../hooks/useColorMode');
    useColorMode.default = jest.fn(() => ['light', null]);
    
    render(<DarkModeSwitcher />);
    
    const button = screen.getByRole('button');
    fireEvent.click(button);
    
    const darkButton = screen.getByText('Oscuro');
    fireEvent.click(darkButton);
    
    // Should not crash
    expect(screen.getByText('Claro')).toBeInTheDocument();
  });
});
