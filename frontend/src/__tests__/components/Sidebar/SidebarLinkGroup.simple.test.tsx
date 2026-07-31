import { render, fireEvent } from '@testing-library/react';
import SidebarLinkGroup from '../../../components/Sidebar/SidebarLinkGroup';

describe('SidebarLinkGroup', () => {
  test('renders children with handleClick and open state', () => {
    const mockChildren = jest.fn((handleClick, open) => (
      <div>
        <button onClick={handleClick}>Toggle</button>
        <span>{open ? 'Open' : 'Closed'}</span>
      </div>
    ));

    render(
      <SidebarLinkGroup activeCondition={false}>
        {mockChildren}
      </SidebarLinkGroup>
    );

    expect(mockChildren).toHaveBeenCalledWith(expect.any(Function), false);
  });

  test('initializes with activeCondition value', () => {
    const mockChildren = jest.fn((_handleClick, open) => (
      <span>{open ? 'Open' : 'Closed'}</span>
    ));

    const { getByText } = render(
      <SidebarLinkGroup activeCondition={true}>
        {mockChildren}
      </SidebarLinkGroup>
    );

    expect(getByText('Open')).toBeInTheDocument();
  });

  test('toggles open state when handleClick is called', () => {
    const mockChildren = jest.fn((handleClick, open) => (
      <div>
        <button onClick={handleClick}>Toggle</button>
        <span>{open ? 'Open' : 'Closed'}</span>
      </div>
    ));

    const { getByText, getByRole } = render(
      <SidebarLinkGroup activeCondition={false}>
        {mockChildren}
      </SidebarLinkGroup>
    );

    // Initially closed
    expect(getByText('Closed')).toBeInTheDocument();

    // Click to open
    fireEvent.click(getByRole('button'));
    expect(getByText('Open')).toBeInTheDocument();

    // Click to close
    fireEvent.click(getByRole('button'));
    expect(getByText('Closed')).toBeInTheDocument();
  });

  test('renders as list item', () => {
    const mockChildren = jest.fn(() => <div>Content</div>);

    const { container } = render(
      <SidebarLinkGroup activeCondition={false}>
        {mockChildren}
      </SidebarLinkGroup>
    );

    const listItem = container.querySelector('li');
    expect(listItem).toBeInTheDocument();
    expect(listItem).toHaveTextContent('Content');
  });

  test('provides correct open state to children function', () => {
    let capturedOpen: boolean | undefined;
    let capturedHandleClick: (() => void) | undefined;

    const mockChildren = jest.fn((handleClick, open) => {
      capturedOpen = open;
      capturedHandleClick = handleClick;
      return <div>Test</div>;
    });

    render(
      <SidebarLinkGroup activeCondition={true}>
        {mockChildren}
      </SidebarLinkGroup>
    );

    expect(capturedOpen).toBe(true);
    expect(typeof capturedHandleClick).toBe('function');
  });
});
