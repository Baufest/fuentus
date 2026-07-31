import { ReactNode } from "react";
import { ButtonVariant, ButtonSize } from "../../types/sharedTypes";

interface ButtonProps {
  children?: ReactNode;
  size?: ButtonSize;
  variant?: ButtonVariant;
  startIcon?: ReactNode;
  endIcon?: ReactNode;
  onClick?: () => void;
  disabled?: boolean;
  className?: string;
  type?: 'button' | 'submit' | 'reset';
}

const Button: React.FC<ButtonProps> = ({
  children,
  size = "md",
  variant = "primary",
  startIcon,
  endIcon,
  onClick,
  className = "",
  disabled = false,
  type = 'button',
}) => {
  const sizeClasses = {
    sm: "px-4 py-3 text-sm",
    md: "px-5 py-3.5 text-sm",
    custom: ""
  };

  // Variant Classes
  const variantClasses = {
    primary:
      "bg-primary text-white shadow-theme-xs hover:bg-primary-600 disabled:bg-primary",
    outline:
      "bg-white text-gray-700 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 dark:bg-gray-800 dark:text-gray-400 dark:ring-gray-700 dark:hover:bg-white/[0.03] dark:hover:text-gray-300",
    success:
      "bg-green-500 text-white shadow-theme-xs hover:bg-green-600 disabled:bg-success",
    info:
      "bg-blue-500 text-white shadow-theme-xs hover:bg-blue-600 disabled:bg-info",
    warning:
      "bg-warning-500 text-white shadow-theme-xs hover:bg-warning-600 disabled:bg-warning",
    danger:
      "bg-danger-500 text-white shadow-theme-xs hover:bg-danger-600 disabled:bg-danger",
    // primary-soft:
    //   "border-primary-500 bg-primary-50 dark:border-primary-500/30 dark:bg-primary-500/15 border p-1 text-primary dark:text-primary",
    // outline:
    //   "bg-white text-gray-700 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 dark:bg-gray-800 dark:text-gray-400 dark:ring-gray-700 dark:hover:bg-white/[0.03] dark:hover:text-gray-300",
    // success-soft:
    //   "border-success-500 bg-success-50 dark:border-success-500/30 dark:bg-success-500/15 border p-1",
    // info-soft:
    //   "border-blue-light-500 bg-blue-light-50 dark:border-blue-light-500/30 dark:bg-blue-light-500/15 border p-1",
    // warning-soft:
    //   "border-warning-500 bg-warning-50 dark:border-warning-500/30 dark:bg-warning-500/15 border p-1 text-warning dark:text-warning",
  };

  return (
    <button
      type={type} // Usa la propiedad type
      className={`inline-flex items-center justify-center gap-2 rounded-lg transition ${className} ${
        sizeClasses[size]
      } ${variantClasses[variant]} ${
        disabled ? "cursor-not-allowed opacity-50" : ""
      }`}
      onClick={onClick}
      disabled={disabled}
    >
      {startIcon && <span className="flex items-center">{startIcon}</span>}
      {children}
      {endIcon && <span className="flex items-center">{endIcon}</span>}
    </button>
  );
};

export default Button;