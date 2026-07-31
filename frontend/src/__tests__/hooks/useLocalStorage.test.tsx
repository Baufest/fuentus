import { renderHook, act } from '@testing-library/react';
import useLocalStorage from '../../hooks/useLocalStorage';

// Mock localStorage
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
};

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
});

describe('useLocalStorage', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorageMock.getItem.mockClear();
    localStorageMock.setItem.mockClear();
  });

  test('returns initial value when localStorage is empty', () => {
    localStorageMock.getItem.mockReturnValue(null);

    const { result } = renderHook(() => useLocalStorage('test-key', 'initial-value'));

    expect(result.current[0]).toBe('initial-value');
    expect(localStorageMock.getItem).toHaveBeenCalledWith('test-key');
  });

  test('returns stored value from localStorage', () => {
    localStorageMock.getItem.mockReturnValue(JSON.stringify('stored-value'));

    const { result } = renderHook(() => useLocalStorage('test-key', 'initial-value'));

    expect(result.current[0]).toBe('stored-value');
    expect(localStorageMock.getItem).toHaveBeenCalledWith('test-key');
  });

  test('returns initial value when localStorage contains invalid JSON', () => {
    localStorageMock.getItem.mockReturnValue('invalid-json');
    const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});

    const { result } = renderHook(() => useLocalStorage('test-key', 'initial-value'));

    expect(result.current[0]).toBe('initial-value');
    expect(consoleSpy).toHaveBeenCalled();
    
    consoleSpy.mockRestore();
  });

  test('setValue updates localStorage and state', () => {
    localStorageMock.getItem.mockReturnValue(null);

    const { result } = renderHook(() => useLocalStorage('test-key', 'initial-value'));

    act(() => {
      result.current[1]('new-value');
    });

    expect(result.current[0]).toBe('new-value');
    expect(localStorageMock.setItem).toHaveBeenCalledWith('test-key', JSON.stringify('new-value'));
  });

  test('setValue handles function values', () => {
    localStorageMock.getItem.mockReturnValue(JSON.stringify('initial-value'));

    const { result } = renderHook(() => useLocalStorage('test-key', 'default'));

    act(() => {
      result.current[1]((prevValue: string) => prevValue + '-updated');
    });

    expect(result.current[0]).toBe('initial-value-updated');
    expect(localStorageMock.setItem).toHaveBeenCalledWith('test-key', JSON.stringify('initial-value-updated'));
  });

  test('setValue handles error in localStorage.setItem', () => {
    localStorageMock.getItem.mockReturnValue(null);
    localStorageMock.setItem.mockImplementation(() => {
      throw new Error('Storage quota exceeded');
    });
    const consoleSpy = jest.spyOn(console, 'log').mockImplementation(() => {});

    const { result } = renderHook(() => useLocalStorage('test-key', 'initial-value'));

    act(() => {
      result.current[1]('new-value');
    });

    // State should still update even if localStorage fails
    expect(result.current[0]).toBe('new-value');
    expect(consoleSpy).toHaveBeenCalled();
    
    consoleSpy.mockRestore();
  });

  test('works with complex objects', () => {
    const complexObject = { foo: 'bar', nested: { value: 123 } };
    localStorageMock.getItem.mockReturnValue(JSON.stringify(complexObject));

    const { result } = renderHook(() => useLocalStorage('test-key', {}));

    expect(result.current[0]).toEqual(complexObject);

    const newObject = { ...complexObject, updated: true };
    act(() => {
      result.current[1](newObject);
    });

    expect(result.current[0]).toEqual(newObject);
    expect(localStorageMock.setItem).toHaveBeenCalledWith('test-key', JSON.stringify(newObject));
  });

  test('works with arrays', () => {
    const arrayValue = ['item1', 'item2', 'item3'];
    localStorageMock.getItem.mockReturnValue(JSON.stringify(arrayValue));

    const { result } = renderHook(() => useLocalStorage<string[]>('test-key', []));

    expect(result.current[0]).toEqual(arrayValue);

    const newArray = [...arrayValue, 'item4'];
    act(() => {
      result.current[1](newArray);
    });

    expect(result.current[0]).toEqual(newArray);
  });

  test('works with boolean values', () => {
    localStorageMock.getItem.mockReturnValue(JSON.stringify(true));

    const { result } = renderHook(() => useLocalStorage('test-key', false));

    expect(result.current[0]).toBe(true);

    act(() => {
      result.current[1](false);
    });

    expect(result.current[0]).toBe(false);
  });

  test('works with number values', () => {
    localStorageMock.getItem.mockReturnValue(JSON.stringify(42));

    const { result } = renderHook(() => useLocalStorage('test-key', 0));

    expect(result.current[0]).toBe(42);

    act(() => {
      result.current[1](100);
    });

    expect(result.current[0]).toBe(100);
  });
});
