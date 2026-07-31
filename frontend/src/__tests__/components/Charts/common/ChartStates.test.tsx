import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import {
  ChartLoadingState,
  ChartEmptyState,
  FilteredDataBanner,
  InfoPanel
} from '../../../../components/Charts/common/ChartStates';

describe('ChartStates Components', () => {
  describe('ChartLoadingState', () => {
    it('renders loading spinner correctly', () => {
      render(<ChartLoadingState />);
      
      const spinner = screen.getByTestId('loading-spinner');
      expect(spinner).toBeInTheDocument();
      expect(spinner).toHaveClass('animate-spin', 'rounded-full', 'h-16', 'w-16', 'border-b-2', 'border-blue-600');
    });

    it('has proper container styling', () => {
      const { container } = render(<ChartLoadingState />);
      
      const loadingContainer = container.firstChild;
      expect(loadingContainer).toHaveClass('flex', 'items-center', 'justify-center', 'h-64');
    });
  });

  describe('ChartEmptyState', () => {
    it('renders with default props', () => {
      render(<ChartEmptyState />);
      
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
      expect(screen.getByText('No se encontraron datos con los filtros aplicados')).toBeInTheDocument();
    });

    it('renders with custom title and message', () => {
      const customTitle = 'Custom Empty Title';
      const customMessage = 'Custom empty message';
      
      render(<ChartEmptyState title={customTitle} message={customMessage} />);
      
      expect(screen.getByText(customTitle)).toBeInTheDocument();
      expect(screen.getByText(customMessage)).toBeInTheDocument();
    });

    it('has proper styling classes', () => {
      const { container } = render(<ChartEmptyState />);
      
      const emptyContainer = container.firstChild;
      expect(emptyContainer).toHaveClass(
        'flex', 'items-center', 'justify-center', 'h-64', 
        'text-gray-500', 'dark:text-gray-400'
      );
    });

    it('renders title with correct styling', () => {
      render(<ChartEmptyState />);
      
      const title = screen.getByText('No hay datos disponibles');
      expect(title).toHaveClass('text-lg', 'font-medium', 'mb-2');
    });
  });

  describe('FilteredDataBanner', () => {
    it('renders with required props', () => {
      render(<FilteredDataBanner visibleCount={5} totalCount={10} />);
      
      expect(screen.getByText(/Se están mostrando 5 de 10 elementos/)).toBeInTheDocument();
      expect(screen.getByText(/Los elementos sin datos válidos han sido ocultados/)).toBeInTheDocument();
    });

    it('renders with custom entity name', () => {
      render(<FilteredDataBanner visibleCount={3} totalCount={7} entityName="aplicaciones" />);
      
      expect(screen.getByText(/Se están mostrando 3 de 7 aplicaciones/)).toBeInTheDocument();
      expect(screen.getByText(/Los aplicaciones sin datos válidos han sido ocultados/)).toBeInTheDocument();
    });

    it('has proper banner styling', () => {
      const { container } = render(<FilteredDataBanner visibleCount={5} totalCount={10} />);
      
      const banner = container.firstChild;
      expect(banner).toHaveClass(
        'bg-amber-50', 'dark:bg-amber-900/20', 'border', 'border-amber-200', 
        'dark:border-amber-800', 'rounded-lg', 'p-3', 'mb-4'
      );
    });

    it('displays correct counts and entity names', () => {
      render(<FilteredDataBanner visibleCount={2} totalCount={8} entityName="servicios" />);
      
      const text = screen.getByText(/Se están mostrando 2 de 8 servicios/);
      expect(text).toHaveClass('text-sm', 'text-amber-800', 'dark:text-amber-200');
    });

    it('handles zero visible count', () => {
      render(<FilteredDataBanner visibleCount={0} totalCount={5} />);
      
      expect(screen.getByText(/Se están mostrando 0 de 5 elementos/)).toBeInTheDocument();
    });

    it('handles equal visible and total counts', () => {
      render(<FilteredDataBanner visibleCount={5} totalCount={5} />);
      
      expect(screen.getByText(/Se están mostrando 5 de 5 elementos/)).toBeInTheDocument();
    });
  });

  describe('InfoPanel', () => {
    it('renders with basic props', () => {
      render(
        <InfoPanel title="Test Title">
          <p>Test content</p>
        </InfoPanel>
      );
      
      expect(screen.getByText('Test Title')).toBeInTheDocument();
      expect(screen.getByText('Test content')).toBeInTheDocument();
    });

    it('renders with blue variant by default', () => {
      const { container } = render(
        <InfoPanel title="Blue Panel">
          <p>Blue content</p>
        </InfoPanel>
      );
      
      const panel = container.firstChild;
      expect(panel).toHaveClass(
        'bg-blue-50', 'dark:bg-blue-900/20', 'border-blue-200', 
        'dark:border-blue-800', 'text-blue-800', 'dark:text-blue-200'
      );
    });

    it('renders with amber variant', () => {
      const { container } = render(
        <InfoPanel title="Amber Panel" variant="amber">
          <p>Amber content</p>
        </InfoPanel>
      );
      
      const panel = container.firstChild;
      expect(panel).toHaveClass(
        'bg-amber-50', 'dark:bg-amber-900/20', 'border-amber-200', 
        'dark:border-amber-800', 'text-amber-800', 'dark:text-amber-200'
      );
    });

    it('renders with green variant', () => {
      const { container } = render(
        <InfoPanel title="Green Panel" variant="green">
          <p>Green content</p>
        </InfoPanel>
      );
      
      const panel = container.firstChild;
      expect(panel).toHaveClass(
        'bg-green-50', 'dark:bg-green-900/20', 'border-green-200', 
        'dark:border-green-800', 'text-green-800', 'dark:text-green-200'
      );
    });

    it('renders icon with correct colors for each variant', () => {
      const { rerender, container } = render(
        <InfoPanel title="Blue Panel" variant="blue">
          <p>Blue content</p>
        </InfoPanel>
      );
      
      let icon = container.querySelector('.flex-shrink-0 svg');
      expect(icon).toHaveClass('text-blue-400');

      rerender(
        <InfoPanel title="Amber Panel" variant="amber">
          <p>Amber content</p>
        </InfoPanel>
      );
      
      icon = container.querySelector('.flex-shrink-0 svg');
      expect(icon).toHaveClass('text-amber-400');

      rerender(
        <InfoPanel title="Green Panel" variant="green">
          <p>Green content</p>
        </InfoPanel>
      );
      
      icon = container.querySelector('.flex-shrink-0 svg');
      expect(icon).toHaveClass('text-green-400');
    });

    it('has proper structure and styling', () => {
      render(
        <InfoPanel title="Structure Test">
          <div data-testid="panel-content">Complex content</div>
        </InfoPanel>
      );
      
      const title = screen.getByText('Structure Test');
      expect(title).toHaveClass('text-sm', 'font-medium');
      
      const content = screen.getByTestId('panel-content');
      expect(content).toBeInTheDocument();
    });

    it('renders complex children correctly', () => {
      render(
        <InfoPanel title="Complex Panel">
          <div>
            <p>First paragraph</p>
            <ul>
              <li>List item 1</li>
              <li>List item 2</li>
            </ul>
          </div>
        </InfoPanel>
      );
      
      expect(screen.getByText('Complex Panel')).toBeInTheDocument();
      expect(screen.getByText('First paragraph')).toBeInTheDocument();
      expect(screen.getByText('List item 1')).toBeInTheDocument();
      expect(screen.getByText('List item 2')).toBeInTheDocument();
    });

    it('has correct SVG icon structure', () => {
      const { container } = render(
        <InfoPanel title="Icon Test">
          <p>Icon content</p>
        </InfoPanel>
      );
      
      const svg = container.querySelector('svg');
      expect(svg).toHaveAttribute('viewBox', '0 0 20 20');
      expect(svg).toHaveClass('h-5', 'w-5');
    });
  });

  describe('Component Integration', () => {
    it('components can be rendered together', () => {
      render(
        <div>
          <ChartLoadingState />
          <ChartEmptyState title="No Data" message="Empty state" />
          <FilteredDataBanner visibleCount={3} totalCount={10} />
          <InfoPanel title="Info" variant="blue">
            <p>Information panel</p>
          </InfoPanel>
        </div>
      );
      
      expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
      expect(screen.getByText('No Data')).toBeInTheDocument();
      expect(screen.getByText(/Se están mostrando 3 de 10 elementos/)).toBeInTheDocument();
      expect(screen.getByText('Info')).toBeInTheDocument();
    });

    it('components maintain proper styling when combined', () => {
      const { container } = render(
        <div>
          <ChartLoadingState />
          <FilteredDataBanner visibleCount={1} totalCount={1} />
        </div>
      );
      
      const spinner = screen.getByTestId('loading-spinner');
      expect(spinner).toHaveClass('border-blue-600');
      
      const banner = container.querySelector('.bg-amber-50');
      expect(banner).toBeInTheDocument();
    });
  });
});
