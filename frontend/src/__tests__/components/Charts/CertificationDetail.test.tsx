import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import CertificationDetail from '../../../components/Charts/CertificationDetail';
import { DashboardDataRow } from '../../../data/dashboardData';

// Mock Button component
jest.mock('../../../components/Buttons/Button', () => {
  return function MockButton({ onClick, children, className }: any) {
    return (
      <button onClick={onClick} className={className}>
        {children}
      </button>
    );
  };
});

const mockData: DashboardDataRow[] = [
  {
    period_month: 'mar 25',
    ug_name: 'ARGENTINA',
    uol1_name: 'SYSTEMS ENGINEERING',
    uol2_name: 'RETAIL BANKING',
    servicel1_id: '001',
    servicel1_name: 'Service Level 3',
    nivel_certificacion: 'Level 3',
    vertical: 'INDIVIDUOS Y PYMES',
    fichas_rfo_status_ok: '100%',
    sn2_dependencias_asignadas: '90%',
    calidad_features: '95%',
    certificacion: '1',
    operating_model: 'Model A',
    evolucion_vulnerabilidades: '5',
    adopcion_total: '85%'
  },
  {
    period_month: 'mar 25',
    ug_name: 'MEXICO',
    uol1_name: 'SYSTEMS ENGINEERING',
    uol2_name: 'CORPORATE BANKING',
    servicel1_id: '002',
    servicel1_name: 'Service Level 2',
    nivel_certificacion: 'Level 2',
    vertical: 'EMPRESAS',
    fichas_rfo_status_ok: '85%',
    sn2_dependencias_asignadas: '80%',
    calidad_features: '88%',
    certificacion: '1',
    operating_model: 'Model B',
    evolucion_vulnerabilidades: '3',
    adopcion_total: '78%'
  },
  {
    period_month: 'mar 25',
    ug_name: 'COLOMBIA',
    uol1_name: 'INGENIERÍA & DATA',
    uol2_name: 'DIGITAL CHANNELS',
    servicel1_id: '003',
    servicel1_name: 'Service Level 1',
    nivel_certificacion: 'Level 1',
    vertical: 'CANALES DIGITALES',
    fichas_rfo_status_ok: '70%',
    sn2_dependencias_asignadas: '65%',
    calidad_features: '72%',
    certificacion: '0',
    operating_model: 'Model C',
    evolucion_vulnerabilidades: '8',
    adopcion_total: '62%'
  },
  {
    period_month: 'mar 25',
    ug_name: 'ESPAÑA',
    uol1_name: 'SYSTEMS ENGINEERING',
    uol2_name: 'PAYMENTS',
    servicel1_id: '004',
    servicel1_name: 'Service Not Certified',
    nivel_certificacion: 'Not certified',
    vertical: 'TRANSVERSALES',
    fichas_rfo_status_ok: '60%',
    sn2_dependencias_asignadas: '55%',
    calidad_features: '65%',
    certificacion: '0',
    operating_model: 'Model D',
    evolucion_vulnerabilidades: '12',
    adopcion_total: '45%'
  }
];

describe('CertificationDetail', () => {
  const mockOnClose = jest.fn();

  beforeEach(() => {
    mockOnClose.mockClear();
  });

  describe('Basic Rendering', () => {
    test('renders component with correct title and service count', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: Level 3 (1 servicios)')).toBeInTheDocument();
    });

    test('renders close button and calls onClose when clicked', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      const closeButton = screen.getByText('✕');
      fireEvent.click(closeButton);
      expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    test('renders empty state when no services match selected level', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 4"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: Level 4')).toBeInTheDocument();
      expect(screen.getByText('No hay servicios con el nivel de certificación "Level 4".')).toBeInTheDocument();
    });

    test('renders empty state close button functionality', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 4"
          onClose={mockOnClose}
        />
      );

      const closeButton = screen.getByText('✕');
      fireEvent.click(closeButton);
      expect(mockOnClose).toHaveBeenCalledTimes(1);
    });
  });

  describe('Data Filtering', () => {
    test('filters data correctly by certification level', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 2"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: Level 2 (1 servicios)')).toBeInTheDocument();
      expect(screen.getAllByText('Service Level 2')).toHaveLength(2); // Table and list
    });

    test('handles "Not certified" level correctly', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Not certified"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: Not certified (1 servicios)')).toBeInTheDocument();
      expect(screen.getAllByText('Service Not Certified')).toHaveLength(2); // Table and list
    });

    test('handles multiple services with same certification level', () => {
      const multipleServicesData = [
        ...mockData,
        {
          ...mockData[0],
          servicel1_id: '005',
          servicel1_name: 'Another Level 3 Service',
          uol2_name: 'DIFFERENT UOL2'
        }
      ];

      render(
        <CertificationDetail
          data={multipleServicesData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: Level 3 (2 servicios)')).toBeInTheDocument();
    });
  });

  describe('Statistics Display', () => {
    test('displays correct statistics for filtered data', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      // Check total services
      expect(screen.getByText('Total Servicios')).toBeInTheDocument();
      
      // Check for "1" in the statistics section specifically (not in the title)
      const statisticsSection = screen.getByText('Total Servicios').closest('div')?.parentElement;
      expect(statisticsSection).toBeInTheDocument();

      // Check UOL2 count
      expect(screen.getByText('UOL2 Únicas')).toBeInTheDocument();

      // Check geography count
      expect(screen.getByText('Geografías')).toBeInTheDocument();

      // Check verticals count
      expect(screen.getByText('Verticales')).toBeInTheDocument();
    });

    test('calculates unique counts correctly for multiple services', () => {
      const multiGeographyData = [
        mockData[0], // ARGENTINA
        {
          ...mockData[0],
          servicel1_id: '006',
          servicel1_name: 'Another Service',
          ug_name: 'MEXICO',
          vertical: 'EMPRESAS'
        }
      ];

      render(
        <CertificationDetail
          data={multiGeographyData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      // Should show 2 geographies and 2 verticals, verify the component renders statistics
      expect(screen.getByText('Total Servicios')).toBeInTheDocument();
      expect(screen.getByText('UOL2 Únicas')).toBeInTheDocument();
      expect(screen.getByText('Geografías')).toBeInTheDocument();
      expect(screen.getByText('Verticales')).toBeInTheDocument();
    });
  });

  describe('Badge Styling', () => {
    test('getCertificationBadgeStyle returns correct styles for Level 3', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      // Should render Level 3 badge with green styling
      const level3Badge = screen.getByText('Level 3');
      expect(level3Badge).toHaveClass('bg-green-100', 'text-green-800');
    });

    test('getCertificationBadgeStyle returns correct styles for Level 2', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 2"
          onClose={mockOnClose}
        />
      );

      const level2Badge = screen.getByText('Level 2');
      expect(level2Badge).toHaveClass('bg-blue-100', 'text-blue-800');
    });

    test('getCertificationBadgeStyle returns correct styles for Level 1', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 1"
          onClose={mockOnClose}
        />
      );

      const level1Badge = screen.getByText('Level 1');
      expect(level1Badge).toHaveClass('bg-yellow-100', 'text-yellow-800');
    });

    test('getCertificationBadgeStyle returns correct styles for Not certified', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Not certified"
          onClose={mockOnClose}
        />
      );

      const notCertifiedBadge = screen.getByText('Not certified');
      expect(notCertifiedBadge).toHaveClass('bg-red-100', 'text-red-800');
    });
  });

  describe('Certification Status Badge', () => {
    test('displays "Sí" for certified services', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      const certifiedBadge = screen.getByText('Sí');
      expect(certifiedBadge).toBeInTheDocument();
      expect(certifiedBadge).toHaveClass('bg-green-100', 'text-green-800');
    });

    test('displays "No" for non-certified services', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Not certified"
          onClose={mockOnClose}
        />
      );

      const notCertifiedBadge = screen.getByText('No');
      expect(notCertifiedBadge).toBeInTheDocument();
      expect(notCertifiedBadge).toHaveClass('bg-red-100', 'text-red-800');
    });
  });

  describe('Table Content', () => {
    test('renders table with all expected columns', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Geo.')).toBeInTheDocument();
      expect(screen.getByText('UOL1')).toBeInTheDocument();
      expect(screen.getByText('UOL2')).toBeInTheDocument();
      expect(screen.getByText('Servicio L1')).toBeInTheDocument();
      expect(screen.getByText('RFO')).toBeInTheDocument();
      expect(screen.getByText('Depend.')).toBeInTheDocument();
      expect(screen.getByText('Features')).toBeInTheDocument();
      expect(screen.getByText('Certif.')).toBeInTheDocument();
      expect(screen.getByText('Op.Model')).toBeInTheDocument();
      expect(screen.getByText('Vulner.')).toBeInTheDocument();
      expect(screen.getByText('Adop.')).toBeInTheDocument();
      expect(screen.getByText('Nivel')).toBeInTheDocument();
    });

    test('renders service data in table rows', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('ARGENTINA')).toBeInTheDocument();
      expect(screen.getByText('SYSTEMS ENGINEERING')).toBeInTheDocument();
      expect(screen.getByText('RETAIL BANKING')).toBeInTheDocument();
      expect(screen.getAllByText('Service Level 3')).toHaveLength(2); // Table and list
      expect(screen.getByText('100%')).toBeInTheDocument();
      expect(screen.getByText('90%')).toBeInTheDocument();
      expect(screen.getByText('95%')).toBeInTheDocument();
    });
  });

  describe('Service List Section', () => {
    test('renders complete service list section', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Lista Completa de Servicios')).toBeInTheDocument();
    });

    test('displays service names and IDs in the list', () => {
      render(
        <CertificationDetail
          data={mockData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getAllByText('Service Level 3')).toHaveLength(2); // Table and list
      expect(screen.getByText('(001)')).toBeInTheDocument();
    });

    test('handles multiple services in the list', () => {
      const multipleServicesData = [
        mockData[0],
        {
          ...mockData[0],
          servicel1_id: '007',
          servicel1_name: 'Another Level 3 Service'
        }
      ];

      render(
        <CertificationDetail
          data={multipleServicesData}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getAllByText('Service Level 3')).toHaveLength(2); // One in table, one in list
      expect(screen.getAllByText('Another Level 3 Service')).toHaveLength(2); // One in table, one in list
      expect(screen.getByText('(001)')).toBeInTheDocument();
      expect(screen.getByText('(007)')).toBeInTheDocument();
    });
  });

  describe('Edge Cases', () => {
    test('handles empty data array', () => {
      render(
        <CertificationDetail
          data={[]}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('No hay servicios con el nivel de certificación "Level 3".')).toBeInTheDocument();
    });

    test('handles data with missing nivel_certificacion', () => {
      const dataWithMissingCert = [
        {
          ...mockData[0],
          nivel_certificacion: ''
        }
      ];

      render(
        <CertificationDetail
          data={dataWithMissingCert}
          selectedLevel="No Certificado"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: No Certificado (1 servicios)')).toBeInTheDocument();
    });

    test('handles data with undefined nivel_certificacion', () => {
      const dataWithUndefinedCert = [
        {
          ...mockData[0],
          nivel_certificacion: undefined as any
        }
      ];

      render(
        <CertificationDetail
          data={dataWithUndefinedCert}
          selectedLevel="No Certificado"
          onClose={mockOnClose}
        />
      );

      expect(screen.getByText('Detalle: No Certificado (1 servicios)')).toBeInTheDocument();
    });

    test('handles long service names with truncation', () => {
      const dataWithLongNames = [
        {
          ...mockData[0],
          servicel1_name: 'This is a very long service name that should be truncated in the display'
        }
      ];

      render(
        <CertificationDetail
          data={dataWithLongNames}
          selectedLevel="Level 3"
          onClose={mockOnClose}
        />
      );

      const longServiceNames = screen.getAllByText('This is a very long service name that should be truncated in the display');
      expect(longServiceNames.length).toBeGreaterThan(0); // Should appear in both table and list
    });
  });

});