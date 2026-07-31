import React from 'react';
import { Link } from 'react-router-dom';

interface BackToHomeProps {
  className?: string;
}

const BackToHome: React.FC<BackToHomeProps> = ({ className = '' }) => {
  return (
    <Link
      to="/"
      className={`inline-flex items-center gap-2 text-primary hover:text-primary/80 transition-colors ${className}`}
      title="Volver al Dashboard"
    >
      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      <span className="font-medium">Volver al Dashboard</span>
    </Link>
  );
};

export default BackToHome;