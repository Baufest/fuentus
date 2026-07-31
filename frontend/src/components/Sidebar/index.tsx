// import React from 'react';

interface SidebarProps {
  sidebarOpen: boolean;
  setSidebarOpen: (arg: boolean) => void;
}

const Sidebar: React.FC<SidebarProps> = ({ sidebarOpen, setSidebarOpen }) => {
  return (
    <aside className={sidebarOpen ? "block" : "hidden"}>
      <button onClick={() => setSidebarOpen(false)}>Close Sidebar</button>
    </aside>
  );
};

export default Sidebar;