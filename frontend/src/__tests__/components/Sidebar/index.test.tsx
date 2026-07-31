import { render, screen, fireEvent } from '@testing-library/react';
import Sidebar from '../../../components/Sidebar';
import '@testing-library/jest-dom';

describe('Sidebar', () => {
  const mockSetSidebarOpen = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders sidebar when open', () => {
    render(<Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />);
    
    const sidebar = screen.getByRole('complementary');
    expect(sidebar).toBeInTheDocument();
    expect(sidebar).toHaveClass('block');
    expect(sidebar).not.toHaveClass('hidden');
  });

  test('hides sidebar when closed', () => {
    render(<Sidebar sidebarOpen={false} setSidebarOpen={mockSetSidebarOpen} />);
    
    const sidebar = screen.getByRole('complementary');
    expect(sidebar).toBeInTheDocument();
    expect(sidebar).toHaveClass('hidden');
    expect(sidebar).not.toHaveClass('block');
  });

  test('renders close button', () => {
    render(<Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />);
    
    const closeButton = screen.getByRole('button', { name: /close sidebar/i });
    expect(closeButton).toBeInTheDocument();
  });

  test('calls setSidebarOpen with false when close button is clicked', () => {
    render(<Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />);
    
    const closeButton = screen.getByRole('button', { name: /close sidebar/i });
    fireEvent.click(closeButton);
    
    expect(mockSetSidebarOpen).toHaveBeenCalledTimes(1);
    expect(mockSetSidebarOpen).toHaveBeenCalledWith(false);
  });

  test('close button works when sidebar is already closed', () => {
    render(<Sidebar sidebarOpen={false} setSidebarOpen={mockSetSidebarOpen} />);
    
    const closeButton = screen.getByRole('button', { name: /close sidebar/i });
    fireEvent.click(closeButton);
    
    expect(mockSetSidebarOpen).toHaveBeenCalledTimes(1);
    expect(mockSetSidebarOpen).toHaveBeenCalledWith(false);
  });

  test('has correct semantic HTML structure', () => {
    render(<Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />);
    
    const sidebar = screen.getByRole('complementary');
    expect(sidebar.tagName).toBe('ASIDE');
  });

  test('applies correct CSS classes based on state', () => {
    const { rerender } = render(
      <Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />
    );
    
    let sidebar = screen.getByRole('complementary');
    expect(sidebar.className).toBe('block');
    
    rerender(<Sidebar sidebarOpen={false} setSidebarOpen={mockSetSidebarOpen} />);
    sidebar = screen.getByRole('complementary');
    expect(sidebar.className).toBe('hidden');
  });

  test('handles rapid toggling correctly', () => {
    const { rerender } = render(
      <Sidebar sidebarOpen={false} setSidebarOpen={mockSetSidebarOpen} />
    );
    
    // Toggle multiple times
    rerender(<Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />);
    rerender(<Sidebar sidebarOpen={false} setSidebarOpen={mockSetSidebarOpen} />);
    rerender(<Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />);
    
    const sidebar = screen.getByRole('complementary');
    expect(sidebar).toHaveClass('block');
  });

  test('button functionality is independent of current state', () => {
    const { rerender } = render(
      <Sidebar sidebarOpen={true} setSidebarOpen={mockSetSidebarOpen} />
    );
    
    const closeButton = screen.getByRole('button');
    fireEvent.click(closeButton);
    
    expect(mockSetSidebarOpen).toHaveBeenCalledWith(false);
    
    // Reset mock and test with closed state
    mockSetSidebarOpen.mockClear();
    
    rerender(<Sidebar sidebarOpen={false} setSidebarOpen={mockSetSidebarOpen} />);
    fireEvent.click(closeButton);
    
    expect(mockSetSidebarOpen).toHaveBeenCalledWith(false);
  });
});
