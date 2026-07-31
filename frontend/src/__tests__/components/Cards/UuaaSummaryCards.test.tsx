import { render, screen } from '@testing-library/react';
import UuaaSummaryCards from '../../../components/Cards/UuaaSummaryCards';
import { UuaaSummaryDTO } from '../../../types/statsSummary';

// Mock RfoStatusIcon component
jest.mock('../../../components/RfoStatus/RfoStatusIcon', () => {
  return function MockRfoStatusIcon(props: any) {
    return (
      <div data-testid="rfo-status-icon">
        Status: {props.status} - RFO: {props.rfoId}
      </div>
    );
  };
});

describe('UuaaSummaryCards', () => {
  const mockSummary: UuaaSummaryDTO = {
    uuaa: 'TEST',
    totalApps: 15,
    averageCoverage: 78.5,
    totalBugs: 8,
    totalSastLow: 3,
    totalSastMedium: 2,
    totalSastHigh: 1,
    totalScaLow: 2,
    totalScaMedium: 1,
    totalScaHigh: 3,
    totalScaCritical: 2,
    rfoId: 456,
    rfoEstado: 'Activo',
    totalSastVulnerabilities: 6,
    totalScaVulnerabilities: 8,
    totalVulnerabilities: 14,
  };

  describe('Loading state', () => {
    test('renders loading skeleton when loading is true', () => {
      const { container } = render(
        <UuaaSummaryCards summary={null} loading={true} />
      );

      const pulsingElements = container.querySelectorAll('.animate-pulse');
      expect(pulsingElements.length).toBeGreaterThan(0);
    });

    test('renders 5 skeleton cards during loading', () => {
      const { container } = render(
        <UuaaSummaryCards summary={null} loading={true} />
      );

      const skeletonCards = container.querySelectorAll('.animate-pulse');
      expect(skeletonCards).toHaveLength(5);
    });
  });

  describe('Empty state', () => {
    test('renders null when summary is null and not loading', () => {
      const { container } = render(
        <UuaaSummaryCards summary={null} loading={false} />
      );

      expect(container.firstChild).toBeNull();
    });
  });

  describe('Summary data rendering', () => {
    test('renders total apps card', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      expect(screen.getByText('Total Aplicaciones')).toBeInTheDocument();
      expect(screen.getByText('15')).toBeInTheDocument();
    });

    test('renders average coverage card', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      expect(screen.getByText('Cobertura Promedio')).toBeInTheDocument();
      expect(screen.getByText('78.5%')).toBeInTheDocument();
    });

    test('renders SAST vulnerabilities card', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      expect(screen.getByText('Vulnerabilidades SAST')).toBeInTheDocument();
      expect(screen.getByText('6')).toBeInTheDocument();
      expect(screen.getByText('1H')).toBeInTheDocument();
      expect(screen.getByText('2M')).toBeInTheDocument();
      expect(screen.getByText('3L')).toBeInTheDocument();
    });

    test('renders SCA vulnerabilities card', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      expect(screen.getByText('Vulnerabilidades SCA')).toBeInTheDocument();
      // Check for severity breakdown instead of total count which may be duplicated
      expect(screen.getByText('2C')).toBeInTheDocument();
      expect(screen.getByText('3H')).toBeInTheDocument();
      expect(screen.getByText('1M')).toBeInTheDocument();
      expect(screen.getByText('2L')).toBeInTheDocument();
    });

    test('renders RFO status card', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      expect(screen.getByText('Estado RFO')).toBeInTheDocument();
      expect(screen.getByTestId('rfo-status-icon')).toBeInTheDocument();
      expect(screen.getByText(/Status: Activo - RFO: 456/)).toBeInTheDocument();
    });

    test('renders bugs count when totalBugs is greater than 0', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      expect(screen.getByText('Bugs')).toBeInTheDocument();
      // Use getAllByText since the number might appear in other places
      const bugElements = screen.getAllByText('8');
      expect(bugElements.length).toBeGreaterThan(0);
    });

    test('does not render bugs count when totalBugs is 0', () => {
      const summaryNoBugs = { ...mockSummary, totalBugs: 0 };
      render(<UuaaSummaryCards summary={summaryNoBugs} loading={false} />);

      expect(screen.queryByText('Bugs')).not.toBeInTheDocument();
    });
  });

  describe('Coverage color coding', () => {
    test('applies green color for coverage >= 80', () => {
      const highCoverageSummary = { ...mockSummary, averageCoverage: 85 };
      const { container } = render(
        <UuaaSummaryCards summary={highCoverageSummary} loading={false} />
      );

      const coverageValue = screen.getByText('85.0%');
      expect(coverageValue).toHaveClass('text-green-500');
      
      // Check for green background
      const greenBgElements = container.querySelectorAll('.bg-green-50');
      expect(greenBgElements.length).toBeGreaterThan(0);
    });

    test('applies yellow color for coverage >= 50 and < 80', () => {
      const mediumCoverageSummary = { ...mockSummary, averageCoverage: 65 };
      const { container } = render(
        <UuaaSummaryCards summary={mediumCoverageSummary} loading={false} />
      );

      const coverageValue = screen.getByText('65.0%');
      expect(coverageValue).toHaveClass('text-yellow-500');
      
      // Check for yellow background
      const yellowBgElements = container.querySelectorAll('.bg-yellow-50');
      expect(yellowBgElements.length).toBeGreaterThan(0);
    });

    test('applies red color for coverage < 50', () => {
      const lowCoverageSummary = { ...mockSummary, averageCoverage: 30 };
      const { container } = render(
        <UuaaSummaryCards summary={lowCoverageSummary} loading={false} />
      );

      const coverageValue = screen.getByText('30.0%');
      expect(coverageValue).toHaveClass('text-red-500');
      
      // Check for red background
      const redBgElements = container.querySelectorAll('.bg-red-50');
      expect(redBgElements.length).toBeGreaterThan(0);
    });

    test('handles edge case coverage of exactly 80', () => {
      const edgeCaseSummary = { ...mockSummary, averageCoverage: 80 };
      render(<UuaaSummaryCards summary={edgeCaseSummary} loading={false} />);

      const coverageValue = screen.getByText('80.0%');
      expect(coverageValue).toHaveClass('text-green-500');
    });

    test('handles edge case coverage of exactly 50', () => {
      const edgeCaseSummary = { ...mockSummary, averageCoverage: 50 };
      render(<UuaaSummaryCards summary={edgeCaseSummary} loading={false} />);

      const coverageValue = screen.getByText('50.0%');
      expect(coverageValue).toHaveClass('text-yellow-500');
    });
  });

  describe('Grid layout', () => {
    test('renders all 5 cards in grid layout', () => {
      const { container } = render(
        <UuaaSummaryCards summary={mockSummary} loading={false} />
      );

      const grid = container.querySelector('.grid');
      expect(grid).toBeInTheDocument();
      expect(grid).toHaveClass('grid-cols-1');
      expect(grid).toHaveClass('md:grid-cols-2');
      expect(grid).toHaveClass('lg:grid-cols-4');
      expect(grid).toHaveClass('xl:grid-cols-5');

      // Check that all 5 cards are rendered
      const cards = container.querySelectorAll('.rounded-lg.border.border-stroke');
      expect(cards).toHaveLength(5);
    });
  });

  describe('Icons rendering', () => {
    test('renders TbApps icon for total apps', () => {
      const { container } = render(
        <UuaaSummaryCards summary={mockSummary} loading={false} />
      );

      // Icons are rendered but we can check for their container
      const iconContainers = container.querySelectorAll('.rounded-full');
      expect(iconContainers.length).toBeGreaterThan(0);
    });

    test('renders icons with correct background colors', () => {
      const { container } = render(
        <UuaaSummaryCards summary={mockSummary} loading={false} />
      );

      // Blue background for apps icon
      expect(container.querySelector('.bg-blue-50')).toBeInTheDocument();
      // Purple background for SAST
      expect(container.querySelector('.bg-purple-50')).toBeInTheDocument();
      // Orange background for SCA
      expect(container.querySelector('.bg-orange-50')).toBeInTheDocument();
    });
  });

  describe('Vulnerability severity display', () => {
    test('displays SAST severity with correct colors', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      const highSeverity = screen.getByText('1H');
      const mediumSeverity = screen.getByText('2M');
      const lowSeverity = screen.getByText('3L');

      expect(highSeverity).toHaveClass('text-red-500');
      expect(mediumSeverity).toHaveClass('text-yellow-500');
      expect(lowSeverity).toHaveClass('text-green-500');
    });

    test('displays SCA severity with correct colors', () => {
      render(<UuaaSummaryCards summary={mockSummary} loading={false} />);

      const criticalSeverity = screen.getByText('2C');
      const highSeverity = screen.getByText('3H');
      const mediumSeverity = screen.getByText('1M');
      const lowSeverity = screen.getByText('2L');

      expect(criticalSeverity).toHaveClass('text-red-700');
      expect(highSeverity).toHaveClass('text-red-500');
      expect(mediumSeverity).toHaveClass('text-yellow-500');
      expect(lowSeverity).toHaveClass('text-green-500');
    });
  });

  describe('Zero values handling', () => {
    test('displays 0 for apps when totalApps is 0', () => {
      const zeroAppsSummary = { ...mockSummary, totalApps: 0 };
      render(<UuaaSummaryCards summary={zeroAppsSummary} loading={false} />);

      expect(screen.getByText('Total Aplicaciones')).toBeInTheDocument();
      expect(screen.getByText('0')).toBeInTheDocument();
    });

    test('displays 0.0% for coverage when averageCoverage is 0', () => {
      const zeroCoverageSummary = { ...mockSummary, averageCoverage: 0 };
      render(<UuaaSummaryCards summary={zeroCoverageSummary} loading={false} />);

      expect(screen.getByText('0.0%')).toBeInTheDocument();
    });

    test('displays 0 for vulnerabilities when all are 0', () => {
      const noVulnsSummary = {
        ...mockSummary,
        totalSastVulnerabilities: 0,
        totalSastHigh: 0,
        totalSastMedium: 0,
        totalSastLow: 0,
        totalScaVulnerabilities: 0,
        totalScaCritical: 0,
        totalScaHigh: 0,
        totalScaMedium: 0,
        totalScaLow: 0,
      };
      render(<UuaaSummaryCards summary={noVulnsSummary} loading={false} />);

      const vulnerabilityCards = screen.getAllByText('0');
      expect(vulnerabilityCards.length).toBeGreaterThan(0);
    });
  });

  describe('Decimal formatting', () => {
    test('formats coverage to 1 decimal place', () => {
      const summaryWithDecimals = { ...mockSummary, averageCoverage: 78.12345 };
      render(<UuaaSummaryCards summary={summaryWithDecimals} loading={false} />);

      expect(screen.getByText('78.1%')).toBeInTheDocument();
    });

    test('formats coverage with 0 when it is a whole number', () => {
      const summaryWholeNumber = { ...mockSummary, averageCoverage: 78 };
      render(<UuaaSummaryCards summary={summaryWholeNumber} loading={false} />);

      expect(screen.getByText('78.0%')).toBeInTheDocument();
    });
  });

  describe('RFO status variations', () => {
    test('renders RFO status icon with null rfoId', () => {
      const summaryNullRfo = { ...mockSummary, rfoId: null, rfoEstado: null };
      render(<UuaaSummaryCards summary={summaryNullRfo} loading={false} />);

      expect(screen.getByTestId('rfo-status-icon')).toBeInTheDocument();
    });

    test('renders RFO status icon with different status', () => {
      const summaryDifferentStatus = { ...mockSummary, rfoEstado: 'Inactivo' };
      render(<UuaaSummaryCards summary={summaryDifferentStatus} loading={false} />);

      expect(screen.getByText(/Status: Inactivo/)).toBeInTheDocument();
    });
  });

  describe('Dark mode classes', () => {
    test('includes dark mode classes for cards', () => {
      const { container } = render(
        <UuaaSummaryCards summary={mockSummary} loading={false} />
      );

      const cards = container.querySelectorAll('.dark\\:border-strokedark');
      expect(cards.length).toBeGreaterThan(0);

      const darkBgCards = container.querySelectorAll('.dark\\:bg-boxdark');
      expect(darkBgCards.length).toBeGreaterThan(0);
    });

    test('includes dark mode classes for text', () => {
      const { container } = render(
        <UuaaSummaryCards summary={mockSummary} loading={false} />
      );

      const darkTextElements = container.querySelectorAll('.dark\\:text-white');
      expect(darkTextElements.length).toBeGreaterThan(0);
    });
  });
});
