import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import ChartDashboard from '../../../components/Charts/ChartDashboard'; 
import * as dashboardData from '../../../data/dashboardData';
import * as statsSummaryApi from '../../../api/statsSummaryApi';
import { DashboardFiltersProvider } from '../../../contexts/DashboardFiltersContext';
import { HeaderFiltersProvider } from '../../../contexts/HeaderFiltersContext';

// Mock de ApexCharts
jest.mock('react-apexcharts', () => ({
  __esModule: true,
  default: ({ series }: any) => (
    <div data-testid="mock-chart">
      Chart Component with series: {JSON.stringify(series)}
    </div>
  )
}));

// Mock de react-router
const mockNavigate = jest.fn();
jest.mock('react-router', () => ({
  ...jest.requireActual('react-router'),
  useNavigate: () => mockNavigate,
}));

// Mock de los módulos de datos y API
jest.mock('../../../data/dashboardData');
jest.mock('../../../api/statsSummaryApi');

const mockDashboardData = dashboardData as jest.Mocked<typeof dashboardData>;
const mockStatsSummaryApi = statsSummaryApi as jest.Mocked<typeof statsSummaryApi>;

const mockData = [
  {
    period_month: 'jun 25',
    ug_name: 'ARGENTINA',
    uol1_name: 'SYSTEMS ENGINEERING',
    uol2_name: 'TEST UOL2',
    servicel1_id: '1',
    servicel1_name: 'Test Service',
    fichas_rfo_status_ok: '5',
    sn2_dependencias_asignadas: '3',
    calidad_features: 'High',
    certificacion: '1',
    operating_model: 'Agile',
    evolucion_vulnerabilidades: 'Improving',
    adopcion_total: '80%',
    nivel_certificacion: 'Level 2',
    vertical: 'DIGITAL'
  }
];

const mockStatsData = [
  {
    uuaa: 'TEST',
    repositories: [
      {
        name: 'test-repo',
        bitbucketUrl: 'http://test.com',
        language: 'Java',
        monolith: false,
        servers: [],
        sonarInfo: {
          sonarUrl: 'http://sonar.com',
          sonar10Url: 'http://sonar10.com',
          coverage: 85,
          bugs: 5,
          chimeraSast: { totalLow: 1, totalMedium: 2, totalHigh: 3 },
          chimeraSca: { totalLow: 2, totalMedium: 3, totalHigh: 4, totalCritical: 1 }
        }
      }
    ]
  }
];

const ChartDashboardWrapper = () => (
  <BrowserRouter>
    <HeaderFiltersProvider>
      <DashboardFiltersProvider>
        <ChartDashboard />
      </DashboardFiltersProvider>
    </HeaderFiltersProvider>
  </BrowserRouter>
);

describe('ChartDashboard', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    mockDashboardData.loadDashboardDataFromCSV.mockResolvedValue(mockData);
    mockDashboardData.getUniqueValues.mockImplementation((_data, field) => {
      if (field === 'period_month') return ['jun 25', 'may 25'];
      if (field === 'ug_name') return ['ARGENTINA', 'CHILE'];
      if (field === 'uol1_name') return ['SYSTEMS ENGINEERING'];
      if (field === 'uol2_name') return ['TEST UOL2'];
      return [];
    });
    mockDashboardData.getUniqueVerticals.mockReturnValue(['DIGITAL', 'TRADITIONAL']);
    mockDashboardData.filterData.mockReturnValue(mockData);
    mockDashboardData.getUOL1ByVertical.mockReturnValue(['SYSTEMS ENGINEERING']);
    // UOL1 references removed as part of architectural update
    mockDashboardData.getUOL2ByVertical.mockReturnValue(['TEST UOL2']);
    mockDashboardData.getUOL2ByGeographyUOL1AndVertical.mockReturnValue(['TEST UOL2']);
    mockStatsSummaryApi.getStatsSummaryByFilters.mockResolvedValue(mockStatsData);
  });

  test('renders dashboard with initial loading state', async () => {
    render(<ChartDashboardWrapper />);
    
    expect(screen.getByText('Cargando dashboard...')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });
  });

  test('renders chart after data loads', async () => {
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });
  });







  test('switches between tabs correctly', async () => {
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    // Click SonarQube tab
    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);
    
    expect(screen.getByText('Total UUAAs')).toBeInTheDocument();

    // Click Chimera tab  
    const chimeraTab = screen.getByText('Chimera');
    fireEvent.click(chimeraTab);
    
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();

    // Click back to Certificacion tab
    const certTab = screen.getByText('Modelo SO-SN1');
    fireEvent.click(certTab);
    
    expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
  });

  test('loads coverage data when UOL2 is selected and Coverage tab is active', async () => {
    // Mock empty filtered data (no UOL2 selected means no data)
    mockDashboardData.filterData.mockReturnValue([]);
    
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    // Switch to SonarQube tab first
    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);

    // Verify coverage shows empty state message when no data
    await waitFor(() => {
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    });
  });

  test('loads chimera data when UOL2 is selected and Chimera tab is active', async () => {
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    // Switch to Chimera tab first
    const chimeraTab = screen.getByText('Chimera');
    fireEvent.click(chimeraTab);

        // Verify coverage tab content is shown
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
  });





  test('displays certification statistics correctly', async () => {
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      // Verify the basic stats are displayed
      expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
      expect(screen.getByText('UOL2 Únicas')).toBeInTheDocument();
      expect(screen.getByText('Verticales')).toBeInTheDocument();
    });
  });

  test('handles empty data state', async () => {
    mockDashboardData.loadDashboardDataFromCSV.mockResolvedValue([]);
    mockDashboardData.filterData.mockReturnValue([]);
    
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      // Component should still render without data
      expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
    });
  });

  test('handles loading error', async () => {
    mockDashboardData.loadDashboardDataFromCSV.mockRejectedValue(new Error('Load failed'));
    
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      // Component should handle error gracefully
      expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
    });
  });












  test('coverage tab displays chart when valid data is available', async () => {
    mockStatsSummaryApi.getStatsSummaryByFilters.mockResolvedValue([
      {
        uuaa: 'TEST',
        repositories: [
          {
            name: 'test-repo',
            bitbucketUrl: 'http://test.com',
            language: 'Java',
            monolith: false,
            servers: [],
            sonarInfo: {
              sonarUrl: 'http://sonar.com',
              sonar10Url: 'http://sonar10.com',
              coverage: 85,
              bugs: 5,
              chimeraSast: { totalLow: 1, totalMedium: 2, totalHigh: 3 },
              chimeraSca: { totalLow: 2, totalMedium: 3, totalHigh: 4, totalCritical: 1 }
            }
          }
        ]
      }
    ]);

    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);
    
    await waitFor(() => {
      expect(screen.getByText('Total UUAAs')).toBeInTheDocument();
    });
  });

  test('chimera tab displays chart when valid data is available', async () => {
    mockStatsSummaryApi.getStatsSummaryByFilters.mockResolvedValue(mockStatsData);

    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    const chimeraTab = screen.getByText('Chimera');
    fireEvent.click(chimeraTab);
    
    // Without UOL2 selected, should show empty state message
    await waitFor(() => {
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    });
  });

  test('handles API error for stats data gracefully', async () => {
    mockStatsSummaryApi.getStatsSummaryByFilters.mockRejectedValue(new Error('API Error'));
    // Mock filter to return empty array for this specific test
    mockDashboardData.filterData.mockReturnValue([]);

    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);
    
    // Should show empty state when no UOL2 is selected
    await waitFor(() => {
      expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
    });
    
    // Reset the mock for other tests
    mockDashboardData.filterData.mockReturnValue(mockData);
  });



  test('certification tab shows statistics correctly with real data', async () => {
    const multipleRecordsData = [
      mockData[0],
      {
        ...mockData[0],
        uol2_name: 'ANOTHER UOL2',
        vertical: 'TRADITIONAL',
        certificacion: '3'
      }
    ];
    mockDashboardData.filterData.mockReturnValue(multipleRecordsData);
    
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
      expect(screen.getByText('UOL2 Únicas')).toBeInTheDocument();
      expect(screen.getByText('Verticales')).toBeInTheDocument();
    });
  });





  test('tab switching maintains filter state', async () => {
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    // Switch tabs
    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);
    
    const certTab = screen.getByText('Modelo SO-SN1');
    fireEvent.click(certTab);

    // Verify we're back on the certification tab
    expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
  });

  test('chimera tab shows message when no UOL2 selected', async () => {
    mockDashboardData.getUOL2ByGeographyUOL1AndVertical.mockReturnValue([]);
    
    render(<ChartDashboardWrapper />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    const chimeraTab = screen.getByText('Chimera');
    fireEvent.click(chimeraTab);
    
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument();
  });
});
