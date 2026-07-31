import { renderHook } from '@testing-library/react';

// Mock the useLocalStorage hook before importing useColorMode
jest.mock('../../hooks/useLocalStorage', () => ({
  __esModule: true,
  default: jest.fn(),
}));

import useColorMode from '../../hooks/useColorMode';
import useLocalStorage from '../../hooks/useLocalStorage';

const mockUseLocalStorage = useLocalStorage as jest.MockedFunction<typeof useLocalStorage>;

describe('useColorMode', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    // Reset document classes
    document.body.className = '';
  });

  test('returns color mode and setter function', () => {
    // Mock useLocalStorage to return light mode
    mockUseLocalStorage.mockReturnValue(['light', jest.fn()]);

    const { result } = renderHook(() => useColorMode());

    expect(result.current[0]).toBe('light');
    expect(typeof result.current[1]).toBe('function');
  });

  test('applies dark class to body when dark mode', () => {
    mockUseLocalStorage.mockReturnValue(['dark', jest.fn()]);

    renderHook(() => useColorMode());

    expect(document.body.classList.contains('dark')).toBe(true);
  });

  test('removes dark class from body when light mode', () => {
    // Start with dark class
    document.body.classList.add('dark');
    
    mockUseLocalStorage.mockReturnValue(['light', jest.fn()]);

    renderHook(() => useColorMode());

    expect(document.body.classList.contains('dark')).toBe(false);
  });

  test('calls useLocalStorage with correct parameters', () => {
    mockUseLocalStorage.mockReturnValue(['light', jest.fn()]);

    renderHook(() => useColorMode());

    expect(mockUseLocalStorage).toHaveBeenCalledWith('color-theme', 'light');
  });

  test('updates body class when color mode changes', () => {
    const setColorMode = jest.fn();
    mockUseLocalStorage.mockReturnValue(['light', setColorMode]);

    const { rerender } = renderHook(() => useColorMode());

    expect(document.body.classList.contains('dark')).toBe(false);

    // Simulate color mode change to dark
    mockUseLocalStorage.mockReturnValue(['dark', setColorMode]);
    rerender();

    expect(document.body.classList.contains('dark')).toBe(true);

    // Simulate color mode change back to light
    mockUseLocalStorage.mockReturnValue(['light', setColorMode]);
    rerender();

    expect(document.body.classList.contains('dark')).toBe(false);
  });

  test('handles undefined color mode gracefully', () => {
    mockUseLocalStorage.mockReturnValue([undefined, jest.fn()]);

    renderHook(() => useColorMode());

    // Should not add dark class for undefined
    expect(document.body.classList.contains('dark')).toBe(false);
  });

  test('handles null color mode gracefully', () => {
    mockUseLocalStorage.mockReturnValue([null, jest.fn()]);

    renderHook(() => useColorMode());

    // Should not add dark class for null
    expect(document.body.classList.contains('dark')).toBe(false);
  });

  test('handles empty string color mode gracefully', () => {
    mockUseLocalStorage.mockReturnValue(['', jest.fn()]);

    renderHook(() => useColorMode());

    // Should not add dark class for empty string
    expect(document.body.classList.contains('dark')).toBe(false);
  });

  test('only adds dark class for exact "dark" value', () => {
    const testCases = ['Dark', 'DARK', 'darkmode', 'dark-mode'];
    
    testCases.forEach(colorMode => {
      // Reset body classes
      document.body.className = '';
      
      mockUseLocalStorage.mockReturnValue([colorMode, jest.fn()]);
      renderHook(() => useColorMode());
      
      // Only exact 'dark' should add the class
      expect(document.body.classList.contains('dark')).toBe(false);
    });
  });

  test('preserves other body classes when toggling color mode', () => {
    // Add some existing classes
    document.body.classList.add('existing-class', 'another-class');
    
    mockUseLocalStorage.mockReturnValue(['dark', jest.fn()]);
    renderHook(() => useColorMode());

    expect(document.body.classList.contains('dark')).toBe(true);
    expect(document.body.classList.contains('existing-class')).toBe(true);
    expect(document.body.classList.contains('another-class')).toBe(true);

    // Switch to light mode
    mockUseLocalStorage.mockReturnValue(['light', jest.fn()]);
    const { rerender } = renderHook(() => useColorMode());
    rerender();

    expect(document.body.classList.contains('dark')).toBe(false);
    expect(document.body.classList.contains('existing-class')).toBe(true);
    expect(document.body.classList.contains('another-class')).toBe(true);
  });
});
