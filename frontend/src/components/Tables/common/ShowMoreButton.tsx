import { ReactNode } from 'react';
import Button from '../../Buttons/Button';
import { ButtonSize, ButtonVariant } from '../../../types/sharedTypes';

interface ShowMoreButtonProps {
  expanded: boolean;
  onToggle: () => void;
  remainingCount?: number;
  showCountLabel?: boolean;
  className?: string;
  size?: ButtonSize;
  variant?: ButtonVariant;
  expandLabel?: string;
  collapseLabel?: string;
  expandIcon?: ReactNode;
  collapseIcon?: ReactNode;
  disabled?: boolean;
}

const ShowMoreButton: React.FC<ShowMoreButtonProps> = ({
  expanded,
  onToggle,
  remainingCount,
  showCountLabel = true,
  className,
  size = 'sm',
  variant = 'outline',
  expandLabel = 'Ver más',
  collapseLabel = 'Ver menos',
  expandIcon,
  collapseIcon,
  disabled = false,
}) => {
  const label = expanded
    ? collapseLabel
    : showCountLabel && typeof remainingCount === 'number'
    ? `${expandLabel} (${remainingCount} más)`
    : expandLabel;

  return (
    <Button
      onClick={onToggle}
      variant={variant}
      size={size}
      className={className}
      endIcon={expanded ? collapseIcon : expandIcon}
      disabled={disabled}
    >
      {label}
    </Button>
  );
};

export default ShowMoreButton;
