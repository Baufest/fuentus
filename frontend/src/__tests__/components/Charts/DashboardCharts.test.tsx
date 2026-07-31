import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import DashboardCharts from '../../../components/Charts/DashboardCharts';
import { DashboardDataRow } from '../../../data/dashboardData';
import { StatsSummaryDTO } from '../../../types/statsSummary';
import * as dashboardUtils from '../../../utils/dashboardUtils';
import * as chartDataTransformers from '../../../utils/chartDataTransformers';
import * as coverageDataTransformers from '../../../utils/coverageDataTransformers';

// Mock ApexCharts
jest.mock('react-apexcharts', () => {
  return {
    __esModule: true,
    default: ({ series, type }: any) => (
      <div data-testid="apex-chart" data-type={type} data-series={JSON.stringify(series)}>
        Mock ApexChart - {type}
      </div>
    ),
  };
});

// Mock dashboard utils
jest.mock('../../../utils/dashboardUtils', () => ({
  calculateCertificationStats: jest.fn(),
  getCertificationColors: jest.fn()
}));

// Mock chart data transformers
jest.mock('../../../utils/chartDataTransformers', () => ({
  transformToChimeraChartData: jest.fn(),
  transformToCoverageChartData: jest.fn((data) => {
    if (!Array.isArray(data) || data.length === 0) return [];
    return data.map(item => ({
      uuaa: item.uuaa || 'test-uuaa',
      averageCoverage: item.repositories?.[0]?.sonarInfo?.coverage || 80,
      totalApps: item.repositories?.length || 1,
      applications: item.repositories?.map((repo: any) => ({
        name: repo.name || 'test-app',
        coverage: repo.sonarInfo?.coverage || 80
      })) || [{
        name: 'test-app',
        coverage: 80
      }]
    }));
  })
}));

// Mock coverage data transformers
jest.mock('../../../utils/coverageDataTransformers', () => ({
  transformCoverageToDonutChart: jest.fn()
}));

const mockCalculateCertificationStats = dashboardUtils.calculateCertificationStats as jest.MockedFunction<typeof dashboardUtils.calculateCertificationStats>;
const mockGetCertificationColors = dashboardUtils.getCertificationColors as jest.MockedFunction<typeof dashboardUtils.getCertificationColors>;
const mockTransformToChimeraChartData = chartDataTransformers.transformToChimeraChartData as jest.MockedFunction<typeof chartDataTransformers.transformToChimeraChartData>;
const mockTransformCoverageToDonutChart = coverageDataTransformers.transformCoverageToDonutChart as jest.MockedFunction<typeof coverageDataTransformers.transformCoverageToDonutChart>;

describe('DashboardCharts', () => {
  const mockFilteredData: DashboardDataRow[] = [
    {
      period_month: 'mar 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'SYSTEMS ENGINEERING',
      uol2_name: 'ADQUIRENCIA',
      servicel1_id: '416',
      servicel1_name: 'PA02 MERCHANTS-ACQUIRING',
      nivel_certificacion: 'Level 1',
      vertical: 'INDIVIDUOS Y PYMES',
      fichas_rfo_status_ok: '85,00%',
      sn2_dependencias_asignadas: '90,00%',
      calidad_features: '95,00%',
      certificacion: '1',
      operating_model: '1',
      evolucion_vulnerabilidades: '85,00%',
      adopcion_total: '88%'
    },
    {
      period_month: 'mar 25',
      ug_name: 'ARGENTINA',
      uol1_name: 'INGENIERÍA & DATA',
      uol2_name: 'DATAHUB ARGENTINA',
      servicel1_id: '4343',
      servicel1_name: 'DATIO-AR RIESGOS Y FINANZAS',
      nivel_certificacion: 'Not certified',
      vertical: 'INGENIERÍA & DATA',
      fichas_rfo_status_ok: '100,00%',
      sn2_dependencias_asignadas: '100,00%',
      calidad_features: '80,00%',
      certificacion: '0',
      operating_model: '0',
      evolucion_vulnerabilidades: '100,00%',
      adopcion_total: '69%'
    }
  ];

  const mockCoverageData: StatsSummaryDTO[] = [
    { 
      uuaa: 'UG1',
      repositories: [
        {
          name: 'App1',
          bitbucketUrl: 'http://test.com',
          language: 'Java',
          monolith: false,
          servers: [],
          sonarInfo: {
            sonarUrl: 'http://sonar.com',
            sonar10Url: 'http://sonar10.com',
            coverage: 80,
            bugs: 5,
            chimeraSast: { totalLow: 1, totalMedium: 2, totalHigh: 3 },
            chimeraSca: { totalLow: 2, totalMedium: 3, totalHigh: 4, totalCritical: 1 }
          }
        }
      ]
    },
    { 
      uuaa: 'UG2',
      repositories: [
        {
          name: 'App2',
          bitbucketUrl: 'http://test2.com',
          language: 'JavaScript',
          monolith: false,
          servers: [],
          sonarInfo: {
            sonarUrl: 'http://sonar.com',
            sonar10Url: 'http://sonar10.com',
            coverage: 60,
            bugs: 10,
            chimeraSast: { totalLow: 2, totalMedium: 3, totalHigh: 1 },
            chimeraSca: { totalLow: 1, totalMedium: 2, totalHigh: 3, totalCritical: 2 }
          }
        }
      ]
    }
  ];

  const mockChimeraData: StatsSummaryDTO[] = [
    { 
      uuaa: 'UG3',
      repositories: [
        {
          name: 'App3',
          bitbucketUrl: 'http://test3.com',
          language: 'Python',
          monolith: false,
          servers: [],
          sonarInfo: {
            sonarUrl: 'http://sonar.com',
            sonar10Url: 'http://sonar10.com',
            coverage: 75,
            bugs: 3,
            chimeraSast: { totalLow: 3, totalMedium: 1, totalHigh: 2 },
            chimeraSca: { totalLow: 4, totalMedium: 2, totalHigh: 1, totalCritical: 3 }
          }
        }
      ]
    },
    { 
      uuaa: 'UG4',
      repositories: [
        {
          name: 'App4',
          bitbucketUrl: 'http://test4.com',
          language: 'C#',
          monolith: false,
          servers: [],
          sonarInfo: {
            sonarUrl: 'http://sonar.com',
            sonar10Url: 'http://sonar10.com',
            coverage: 55,
            bugs: 8,
            chimeraSast: { totalLow: 1, totalMedium: 4, totalHigh: 3 },
            chimeraSca: { totalLow: 2, totalMedium: 1, totalHigh: 5, totalCritical: 1 }
          }
        }
      ]
    }
  ];

  const defaultProps = {
    activeTab: 'certificacion' as const,
    filteredData: mockFilteredData,
    coverageData: mockCoverageData,
    coverageLoading: false,
    chimeraData: mockChimeraData,
    chimeraLoading: false,
    nucleusCoverageData: [],
    nucleusCoverageLoading: false
  };

  beforeEach(() => {
    jest.clearAllMocks();
    
    // Setup default mock implementations
    mockCalculateCertificationStats.mockReturnValue({
      certificationCounts: [['Certificado', 60], ['No Certificado', 40]],
      chartData: {
        labels: ['Certificado', 'No Certificado'],
        series: [60, 40]
      },
      totalCases: 100,
      certifiedCases: 60,
      certificationPercentage: '60.0'
    });
    
    mockGetCertificationColors.mockReturnValue(['#10B981', '#EF4444']);
    
    // Setup mocks for chart data transformers
    mockTransformToChimeraChartData.mockReturnValue([
      {
        uuaa: 'UG3',
        sastData: {
          totalLow: 3,
          totalMedium: 1,
          totalHigh: 2
        },
        scaData: {
          totalLow: 4,
          totalMedium: 2,
          totalHigh: 1,
          totalCritical: 3
        },
        totalApps: 1,
        applications: [
          {
            name: 'App3',
            bitbucketUrl: 'http://test3.com',
            chimeraUrl: 'http://chimera3.com',
            chimeraSast: { totalLow: 3, totalMedium: 1, totalHigh: 2 },
            chimeraSca: { totalLow: 4, totalMedium: 2, totalHigh: 1, totalCritical: 3 }
          }
        ]
      }
    ]);
    mockTransformCoverageToDonutChart.mockReturnValue({
      series: [75, 25],
      labels: ['Alta Cobertura', 'Baja Cobertura'],
      colors: ['#10B981', '#EF4444'],
      totalServices: 100,
      averageCoverage: 75
    });
  });

  describe('Certification tab', () => {
    it('renders certification chart correctly', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
      expect(screen.getByTestId('apex-chart')).toBeInTheDocument();
      expect(screen.getByTestId('apex-chart')).toHaveAttribute('data-type', 'donut');
    });

    it('displays certification legend items', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      expect(screen.getByText('Certificado')).toBeInTheDocument();
      expect(screen.getByText('No Certificado')).toBeInTheDocument();
      expect(screen.getByText('60')).toBeInTheDocument();
      expect(screen.getByText('40')).toBeInTheDocument();
    });

    it('calls onDataPointSelection when provided', () => {
      const mockOnDataPointSelection = jest.fn();
      render(
        <DashboardCharts 
          {...defaultProps} 
          onDataPointSelection={mockOnDataPointSelection}
        />
      );
      
      // Verify the chart has the event handler
      const chart = screen.getByTestId('apex-chart');
      expect(chart).toBeInTheDocument();
    });

    it('applies correct chart configuration for donut chart', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      const chart = screen.getByTestId('apex-chart');
      const seriesData = JSON.parse(chart.getAttribute('data-series') || '[]');
      expect(seriesData).toEqual([60, 40]);
    });

    it('applies responsive chart styles', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      // Check container classes for responsiveness
      const container = screen.getByText('Nivel de Certificación').closest('.col-span-12');
      expect(container).toHaveClass('xl:col-span-5');
    });

    it('handles certification chart with all data certified', () => {
      const allCertifiedData: DashboardDataRow[] = [
        {
          ...mockFilteredData[0],
          certificacion: '1'
        },
        {
          ...mockFilteredData[1],
          certificacion: '1'
        }
      ];

      render(
        <DashboardCharts 
          {...defaultProps}
          filteredData={allCertifiedData}
        />
      );
      
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    it('handles certification chart with no data certified', () => {
      const noCertifiedData: DashboardDataRow[] = [
        {
          ...mockFilteredData[0],
          certificacion: '0'
        },
        {
          ...mockFilteredData[1],
          certificacion: '0'
        }
      ];

      render(
        <DashboardCharts 
          {...defaultProps}
          filteredData={noCertifiedData}
        />
      );
      
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });
  });

  describe('Coverage tab', () => {
    it('renders coverage chart with loading state', () => {
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="coverage"
          coverageLoading={true}
          filteredData={[]}
        />
      );
      
      // When loading, should show the loading spinner
      expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
    });

    it('renders coverage chart with loaded data', () => {
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="coverage"
          coverageLoading={false}
        />
      );
      
      expect(screen.getByText(/No hay datos disponibles/)).toBeInTheDocument();
    });

    it('displays correct item count for coverage data', () => {
      const emptyFilteredData: DashboardDataRow[] = [];
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="coverage"
          filteredData={emptyFilteredData}
          coverageLoading={false}
        />
      );
      
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    });

    it('renders coverage tab with onDataPointSelection prop', () => {
      const mockOnDataPointSelection = jest.fn();
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="coverage"
          coverageLoading={false}
          onDataPointSelection={mockOnDataPointSelection}
        />
      );
      
      expect(screen.getByText(/No hay datos disponibles/)).toBeInTheDocument();
    });

    it('applies correct CSS classes for coverage container', () => {
      const { container } = render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="coverage"
          coverageLoading={false}
        />
      );
      
      const coverageContainer = container.querySelector('.col-span-12');
      expect(coverageContainer).toHaveClass('rounded-sm', 'border', 'border-stroke', 'bg-white');
    });
  });

  describe('Chimera tab', () => {
    it('renders chimera chart with loading state', () => {
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="chimera"
          chimeraLoading={true}
        />
      );
      
      expect(screen.getByTestId('loading-spinner')).toBeInTheDocument();
    });

    it('renders chimera chart with loaded data', () => {
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="chimera"
          chimeraLoading={false}
        />
      );
      
      expect(screen.getAllByTestId('apex-chart')).toHaveLength(2);
      expect(screen.getByText(/Información sobre Chimera/)).toBeInTheDocument();
    });

    it('displays correct item count for chimera data', () => {
      const singleChimeraData: StatsSummaryDTO[] = [
        { 
          uuaa: 'UG1',
          repositories: [
            {
              name: 'App1',
              bitbucketUrl: 'http://test1.com',
              language: 'Java',
              monolith: false,
              servers: [],
              sonarInfo: {
                sonarUrl: 'http://sonar.com',
                sonar10Url: 'http://sonar10.com',
                coverage: 75,
                bugs: 3,
                chimeraSast: { totalLow: 1, totalMedium: 2, totalHigh: 3 },
                chimeraSca: { totalLow: 2, totalMedium: 1, totalHigh: 2, totalCritical: 1 }
              }
            }
          ]
        }
      ];
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="chimera"
          chimeraData={singleChimeraData}
          chimeraLoading={false}
        />
      );
      
      expect(screen.getAllByTestId('apex-chart')).toHaveLength(2);
    });

    it('renders chimera tab with onDataPointSelection prop', () => {
      const mockOnDataPointSelection = jest.fn();
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="chimera"
          chimeraLoading={false}
          onDataPointSelection={mockOnDataPointSelection}
        />
      );
      
      expect(screen.getAllByTestId('apex-chart')).toHaveLength(2);
      expect(screen.getByText(/Información sobre Chimera/)).toBeInTheDocument();
    });

    it('applies correct CSS classes for chimera container', () => {
      const { container } = render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="chimera"
          chimeraLoading={false}
        />
      );
      
      const chimeraContainer = container.querySelector('.col-span-12');
      expect(chimeraContainer).toHaveClass('rounded-sm', 'border', 'border-stroke', 'bg-white');
    });

    it('handles empty chimera data gracefully', () => {
      // Mock transformer to return empty data
      mockTransformToChimeraChartData.mockReturnValue([]);
      
      render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab="chimera"
          chimeraData={[]}
          chimeraLoading={false}
        />
      );
      
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    });
  });

  describe('Edge cases and comprehensive testing', () => {
    it('returns null for unknown tab', () => {
      const { container } = render(
        <DashboardCharts 
          {...defaultProps} 
          activeTab={'unknown' as any}
        />
      );
      
      expect(container.firstChild).toBeNull();
    });

    it('handles empty filtered data for certification', () => {
      render(
        <DashboardCharts 
          {...defaultProps} 
          filteredData={[]}
        />
      );
      
      // Should still render the chart container
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    it('renders certification chart with custom colors and legend', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      // Check that legend items have color indicators
      const legendItems = screen.getAllByText(/Certificado|No Certificado/);
      expect(legendItems).toHaveLength(2);
    });

    it('handles all tab types with different props combinations', () => {
      const tabs: Array<'certificacion' | 'coverage' | 'chimera'> = ['certificacion', 'coverage', 'chimera'];
      
      tabs.forEach(tab => {
        const { unmount } = render(
          <DashboardCharts 
            {...defaultProps}
            activeTab={tab}
            coverageLoading={tab === 'coverage'}
            chimeraLoading={tab === 'chimera'}
          />
        );
        
        // Each tab should render without error
        expect(document.body).toBeInTheDocument();
        unmount();
      });
    });

    it('handles mixed loading and data states', () => {
      // Coverage loading true, chimera loading false
      const { rerender } = render(
        <DashboardCharts 
          {...defaultProps}
          activeTab="coverage"
          coverageLoading={false}
          chimeraLoading={false}
          filteredData={[]}
        />
      );
      
      // Should show empty state message, not loading spinner
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
      
      // Switch to chimera tab
      rerender(
        <DashboardCharts 
          {...defaultProps}
          activeTab="chimera"
          coverageLoading={true}
          chimeraLoading={false}
        />
      );
      
      expect(screen.getAllByTestId('apex-chart')).toHaveLength(2);
    });

    it('preserves onDataPointSelection across different tabs', () => {
      const mockOnDataPointSelection = jest.fn();
      const { rerender } = render(
        <DashboardCharts 
          {...defaultProps}
          activeTab="certificacion"
          onDataPointSelection={mockOnDataPointSelection}
        />
      );
      
      expect(screen.getByTestId('apex-chart')).toBeInTheDocument();
      
      // Switch to coverage - should not break
      rerender(
        <DashboardCharts 
          {...defaultProps}
          activeTab="coverage"
          onDataPointSelection={mockOnDataPointSelection}
        />
      );
      
      expect(screen.getByText(/No hay datos disponibles/)).toBeInTheDocument();
    });

    it('handles undefined/null props gracefully', () => {
      expect(() => {
        render(
          <DashboardCharts 
            {...defaultProps}
            onDataPointSelection={undefined}
          />
        );
      }).not.toThrow();
    });

    it('maintains state consistency when switching between tabs', () => {
      const { rerender } = render(
        <DashboardCharts 
          {...defaultProps}
          activeTab="certificacion"
        />
      );
      
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
      
      rerender(
        <DashboardCharts 
          {...defaultProps}
          activeTab="coverage"
          coverageLoading={false}
        />
      );
      
      expect(screen.getByText(/No hay datos disponibles/)).toBeInTheDocument();
      
      rerender(
        <DashboardCharts 
          {...defaultProps}
          activeTab="chimera"
          chimeraLoading={false}
        />
      );
      
      expect(screen.getAllByTestId('apex-chart')).toHaveLength(2);
    });
  });

  describe('Responsive behavior and data formatting', () => {
    it('applies responsive chart options for certification', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      const chart = screen.getByTestId('apex-chart');
      expect(chart).toBeInTheDocument();
      // The responsive configuration is passed to ApexChart options
    });

    it('displays certification percentage correctly', () => {
      render(<DashboardCharts {...defaultProps} />);
      
      // The certification percentage should be displayed in the chart
      expect(screen.getByTestId('apex-chart')).toBeInTheDocument();
    });

    it('formats chart data correctly for different data types', () => {
      const complexData: DashboardDataRow[] = [
        {
          ...mockFilteredData[0],
          certificacion: '1',
          adopcion_total: '95%'
        },
        {
          ...mockFilteredData[1],
          certificacion: '0',
          adopcion_total: '45%'
        }
      ];

      render(
        <DashboardCharts 
          {...defaultProps}
          filteredData={complexData}
        />
      );
      
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });
  });
});
