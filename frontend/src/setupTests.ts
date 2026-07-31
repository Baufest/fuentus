import '@testing-library/jest-dom';

// Mock window.scrollTo
Object.defineProperty(window, 'scrollTo', {
  value: jest.fn(),
  writable: true
});

// Mock console methods to reduce noise in tests
const originalConsoleWarn = console.warn;
console.warn = (...args) => {
  // Suppress React Router warnings in tests
  if (
    typeof args[0] === 'string' && 
    args[0].includes('React Router Future Flag Warning')
  ) {
    return;
  }
  originalConsoleWarn(...args);
};
