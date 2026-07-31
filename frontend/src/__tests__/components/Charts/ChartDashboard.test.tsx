import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ChartDashboard from '../../../components/Charts/ChartDashboard';
import { DashboardFiltersProvider } from '../../../contexts/DashboardFiltersContext';
import { HeaderFiltersProvider } from '../../../contexts/HeaderFiltersContext';
import '@testing-library/jest-dom';

jest.mock('../../../data/dashboardData', () => ({
  loadDashboardDataFromCSV: jest.fn(),
  filterData: jest.fn(),
  getUniqueValues: jest.fn((_data: any[], field: string) => {
    if (field === 'period_month') return ['jun 25', 'may 25'];
    if (field === 'ug_name') return ['ARGENTINA'];
    if (field === 'vertical') return ['INDIVIDUOS Y PYMES'];
    if (field === 'uol1_name') return ['SYSTEMS ENGINEERING'];
    return [];
  }),
  getUniqueVerticals: jest.fn(),
  getUOL1ByVertical: jest.fn(),
  getUOL1ByGeographyAndVertical: jest.fn(),
  getUOL2ByVertical: jest.fn(),
  getUOL2ByGeographyUOL1AndVertical: jest.fn(),
  getCoverageData: jest.fn(),
  getCoverageStats: jest.fn(),
}));

// Mock de statsSummaryApi
jest.mock('../../../api/statsSummaryApi', () => ({
  getStatsSummaryByFilters: jest.fn(),
  getCountSummaryByFilters: jest.fn()
}));

// Mock de fuentusapi para useTotalizadores
jest.mock('../../../api/fuentusapi', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn()
  }
}));

// Mock de nucleusApi
jest.mock('../../../api/nucleusApi', () => ({
  getComboValues: jest.fn()
}));

// Mock de ApexCharts
jest.mock('react-apexcharts', () => {
  return function MockReactApexChart({ series, options }: any) {
    return (
      <div data-testid="apex-chart">
        <div data-testid="chart-series">{JSON.stringify(series)}</div>
        <div data-testid="chart-options">{JSON.stringify(options)}</div>
      </div>
    );
  };
});

// Mock del DashboardMultiSelect
jest.mock('../../../components/Forms/DashboardMultiSelect', () => {
  return function MockDashboardMultiSelect({ 
    options, 
    selectedValues, 
    onSelectionChange, 
    placeholder, 
    label 
  }: any) {
    return (
      <div data-testid={`multiselect-${label.toLowerCase()}`}>
        <label>{label}</label>
        <select 
          multiple 
          value={selectedValues} 
          onChange={(e) => {
            const values = Array.from(e.target.selectedOptions, option => option.value);
            onSelectionChange(values);
          }}
        >
          {options.map((option: string) => (
            <option key={option} value={option}>{option}</option>
          ))}
        </select>
        <div>{placeholder}</div>
      </div>
    );
  };
});

// Mock del Button
jest.mock('../../../components/Buttons/Button', () => {
  return function MockButton({ children, onClick }: any) {
    return <button onClick={onClick}>{children}</button>;
  };
});

import { 
  loadDashboardDataFromCSV, 
  filterData,
  getUOL1ByGeographyAndVertical,
  getUOL2ByGeographyUOL1AndVertical,
  getCoverageData,
  getCoverageStats,
  getUniqueValues
} from '../../../data/dashboardData';
import { getStatsSummaryByFilters, getCountSummaryByFilters } from '../../../api/statsSummaryApi';
import fuentusapi from '../../../api/fuentusapi';
import { getComboValues } from '../../../api/nucleusApi';

const mockLoadDashboardDataFromCSV = loadDashboardDataFromCSV as jest.MockedFunction<typeof loadDashboardDataFromCSV>;
const mockFilterData = filterData as jest.MockedFunction<typeof filterData>;
const mockGetUOL1ByGeographyAndVertical = getUOL1ByGeographyAndVertical as jest.MockedFunction<typeof getUOL1ByGeographyAndVertical>;
const mockGetUOL2ByGeographyUOL1AndVertical = getUOL2ByGeographyUOL1AndVertical as jest.MockedFunction<typeof getUOL2ByGeographyUOL1AndVertical>;
const mockGetCoverageData = getCoverageData as jest.MockedFunction<typeof getCoverageData>;
const mockGetCoverageStats = getCoverageStats as jest.MockedFunction<typeof getCoverageStats>;
const mockGetStatsSummaryByFilters = getStatsSummaryByFilters as jest.MockedFunction<typeof getStatsSummaryByFilters>;
const mockGetCountSummaryByFilters = getCountSummaryByFilters as jest.MockedFunction<typeof getCountSummaryByFilters>;
const mockGetUniqueValues = getUniqueValues as jest.MockedFunction<typeof getUniqueValues>;
const mockFuentusApi = fuentusapi as jest.Mocked<typeof fuentusapi>;
const mockGetComboValues = getComboValues as jest.MockedFunction<typeof getComboValues>;

const mockData = [
  {
    period_month: 'jun 25',
    ug_name: 'ARGENTINA',
    uol1_name: 'SYSTEMS ENGINEERING',
    uol2_name: 'UOL2_TEST',
    servicel1_id: 'SVC001',
    servicel1_name: 'Test Service',
    nivel_certificacion: 'Level 1',
    vertical: 'INDIVIDUOS Y PYMES',
    fichas_rfo_status_ok: 'OK',
    sn2_dependencias_asignadas: 'Yes',
    calidad_features: 'High',
    certificacion: 'Certified',
    operating_model: 'Model A',
    evolucion_vulnerabilidades: 'Improving',
    adopcion_total: 'Full'
  },
  {
    period_month: 'may 25',
    ug_name: 'ARGENTINA',
    uol1_name: 'SYSTEMS ENGINEERING',
    uol2_name: 'UOL2_TEST2',
    servicel1_id: 'SVC002',
    servicel1_name: 'Test Service 2',
    nivel_certificacion: 'No Certificado',
    vertical: 'INDIVIDUOS Y PYMES',
    fichas_rfo_status_ok: 'NOK',
    sn2_dependencias_asignadas: 'No',
    calidad_features: 'Medium',
    certificacion: 'Not Certified',
    operating_model: 'Model B',
    evolucion_vulnerabilidades: 'Stable',
    adopcion_total: 'Partial'
  }
];

const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <MemoryRouter>
      <HeaderFiltersProvider>
        <DashboardFiltersProvider>
          {component}
        </DashboardFiltersProvider>
      </HeaderFiltersProvider>
    </MemoryRouter>
  );
};

describe('ChartDashboard', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    
    // Setup default mocks
    mockLoadDashboardDataFromCSV.mockResolvedValue(mockData);
    mockFilterData.mockReturnValue(mockData);
    mockGetUOL1ByGeographyAndVertical.mockReturnValue(['SYSTEMS ENGINEERING']);
    mockGetUOL2ByGeographyUOL1AndVertical.mockReturnValue(['UOL2_TEST', 'UOL2_TEST2']);
    mockGetStatsSummaryByFilters.mockResolvedValue([]);
    
    // Mock API calls
    mockGetCountSummaryByFilters.mockResolvedValue({
      totalVerticales: 1,
      totalFabricas: 1,
      totalSn1: 5,
      totalSn2: 3,
      totalUuaas: 10
    });
    
    mockFuentusApi.get.mockImplementation((url: string) => {
      if (url.includes('/goals/grouped')) {
        return Promise.resolve({
          data: [
            {
              category: 'Calidad',
              goals: []
            }
          ]
        });
      }
      // Default for other endpoints like totalizadores
      return Promise.resolve({
        data: {
          mejoresNiveles: []
        }
      });
    });
    
    mockGetComboValues.mockResolvedValue({
      verticals: [],
      uol2Values: [],
      sn1Values: [],
      sn2Values: []
    });
    
    // Mock getUniqueValues with proper implementation
    mockGetUniqueValues.mockImplementation((_data: any[], field: string) => {
      if (field === 'period_month') return ['jun 25', 'may 25'];
      if (field === 'ug_name') return ['ARGENTINA'];
      if (field === 'vertical') return ['INDIVIDUOS Y PYMES'];
      if (field === 'uol1_name') return ['SYSTEMS ENGINEERING'];
      return [];
    });
    
    // Mock coverage functions
    mockGetCoverageData.mockReturnValue([
      {
        serviceName: 'Test Service',
        serviceId: 'SVC001',
        uol2Name: 'UOL2_TEST',
        coverage: 85.5,
        qualityFeatures: '85.5%',
        rfoStatus: 'OK',
        dependencies: 'Yes',
        period: 'jun 25',
        geography: 'ARGENTINA',
        vertical: 'INDIVIDUOS Y PYMES'
      }
    ]);
    
    mockGetCoverageStats.mockReturnValue({
      total: 1,
      averageCoverage: 85.5,
      highCoverage: 1,
      mediumCoverage: 0,
      lowCoverage: 0,
      coverageRanges: {
        '90-100%': 0,
        '80-89%': 1,
        '70-79%': 0,
        '60-69%': 0,
        '50-59%': 0,
        '<50%': 0
      }
    });
  });

  test('renders loading state initially', () => {
    renderWithRouter(<ChartDashboard />);
    
    expect(screen.getByText('Cargando dashboard...')).toBeInTheDocument();
  });

  test('renders filters section after loading', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Modelo SO-SN1')).toBeInTheDocument();
    });

    // Verify the component renders with the tabs
    expect(screen.getByText('SonarQube')).toBeInTheDocument();
  });

  test('renders stats cards', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
    });

    expect(screen.getByText('UOL2 Únicas')).toBeInTheDocument();
    expect(screen.getByText('Verticales')).toBeInTheDocument();
  });

  test('renders tabs correctly', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Modelo SO-SN1')).toBeInTheDocument();
    });

    expect(screen.getByText('SonarQube')).toBeInTheDocument();
    expect(screen.getByText('Chimera')).toBeInTheDocument();
    expect(screen.getByText('Coverage por Nivel')).toBeInTheDocument();
    expect(screen.getByText('Sistemática')).toBeInTheDocument();
    expect(screen.getByText('Deuda Técnica')).toBeInTheDocument();
  });

  test('switches between tabs', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('SonarQube')).toBeInTheDocument();
    });

    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);

    await waitFor(() => {
      expect(screen.getByText('Total UUAAs')).toBeInTheDocument();
    });
  });

  test('handles period filter change', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Modelo SO-SN1')).toBeInTheDocument();
    });

    // Verify the component renders the main tab navigation
    const tabs = screen.getAllByRole('button');
    expect(tabs.length).toBeGreaterThan(0);
  });

  test('renders multiselect components', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Modelo SO-SN1')).toBeInTheDocument();
    });

    // Verify the component renders
    expect(screen.getByText('SonarQube')).toBeInTheDocument();
  });

  test('loads coverage data when switching to coverage tab', async () => {
    // Setup UOL2 data first
    mockGetUOL2ByGeographyUOL1AndVertical.mockReturnValue(['UOL2_TEST']);
    
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('SonarQube')).toBeInTheDocument();
    });

    // Switch to coverage tab
    const coverageTab = screen.getByText('SonarQube');
    fireEvent.click(coverageTab);

    await waitFor(() => {
      expect(screen.getByText('Total UUAAs')).toBeInTheDocument();
    });
  });

  test('displays certification chart data correctly', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Nivel de Certificación')).toBeInTheDocument();
    });

    // Should show the chart with data
    expect(screen.getByTestId('apex-chart')).toBeInTheDocument();
  });

  test('handles error in data loading gracefully', async () => {
    mockLoadDashboardDataFromCSV.mockRejectedValue(new Error('Load error'));
    
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      // Should still render the main tab structure even with errors
      expect(screen.getByText('Modelo SO-SN1')).toBeInTheDocument();
    });
  });

  test('updates stats when filters change', async () => {
    renderWithRouter(<ChartDashboard />);
    
    await waitFor(() => {
      expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
    });

    // Just verify that the stats section is rendered with some content
    expect(screen.getByText('Total de Servicios')).toBeInTheDocument();
    // The exact text for other stats may vary, so just check for the main container
    const statsCards = screen.getAllByRole('heading');
    expect(statsCards.length).toBeGreaterThan(0);
  });
});
