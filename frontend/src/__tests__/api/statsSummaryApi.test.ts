import { 
  getStatsSummaryByFilters, 
  getCountSummaryByFilters, 
  getCoverageAverageByLevel 
} from '../../api/statsSummaryApi';
import fuentusapi from '../../api/fuentusapi';

// Mock del módulo fuentusapi
jest.mock('../../api/fuentusapi');
const mockedFuentusApi = fuentusapi as jest.Mocked<typeof fuentusapi>;

describe('statsSummaryApi', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const mockResponseData = [
    {
      uuaa: 'TEST001',
      repositories: [
        {
          name: 'repo1',
          bitbucketUrl: 'https://bitbucket.com/repo1',
          language: 'Java',
          monolith: false,
          servers: [],
          sonarInfo: {
            coverage: 85.5,
            sonarUrl: 'https://sonar.test.com/repo1',
            sonar10Url: 'https://sonar10.test.com/repo1',
            bugs: 5,
            chimeraSast: { totalLow: 2, totalMedium: 1, totalHigh: 0 },
            chimeraSca: { totalLow: 1, totalMedium: 0, totalHigh: 0, totalCritical: 0 }
          }
        }
      ]
    }
  ];

  describe('getStatsSummaryByFilters', () => {
    test('calls API with no parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      const result = await getStatsSummaryByFilters();

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?page=0');
      expect(result).toEqual(mockResponseData);
    });

    test('calls API with area parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters('ARGENTINA');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?area=ARGENTINA&page=0');
    });

    test('calls API with single orgN1 parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters(undefined, 'SYSTEMS ENGINEERING');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?orgN1=SYSTEMS+ENGINEERING&page=0');
    });

    test('calls API with array of orgN1 parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters(undefined, ['SYSTEMS ENGINEERING', 'BUSINESS']);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?orgN1=SYSTEMS+ENGINEERING&orgN1=BUSINESS&page=0');
    });

    test('calls API with single orgN2fabrica parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters(undefined, undefined, 'ARCHITECTURE');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?orgN2fabrica=ARCHITECTURE&page=0');
    });

    test('calls API with array of orgN2fabrica parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters(undefined, undefined, ['ARCHITECTURE', 'DEVELOPMENT']);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?orgN2fabrica=ARCHITECTURE&orgN2fabrica=DEVELOPMENT&page=0');
    });

    test('calls API with all parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters(
        'ARGENTINA', 
        ['SYSTEMS ENGINEERING'], 
        ['ARCHITECTURE', 'DEVELOPMENT'],
        1
      );

      expect(mockedFuentusApi.get).toHaveBeenCalledWith(
        'stats-summary/filters?area=ARGENTINA&orgN1=SYSTEMS+ENGINEERING&orgN2fabrica=ARCHITECTURE&orgN2fabrica=DEVELOPMENT&page=1'
      );
    });

    test('calls API with custom page parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters(undefined, undefined, undefined, 5);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?page=5');
    });

    test('handles API error', async () => {
      const error = new Error('API Error');
      mockedFuentusApi.get.mockRejectedValue(error);

      await expect(getStatsSummaryByFilters()).rejects.toThrow('API Error');
    });

    test('logs error when API fails', async () => {
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation();
      const error = new Error('Network Error');
      mockedFuentusApi.get.mockRejectedValue(error);

      try {
        await getStatsSummaryByFilters();
      } catch (e) {
        // Expected to throw
      }

      expect(consoleSpy).toHaveBeenCalledWith('Error fetching stats summary:', error);
      consoleSpy.mockRestore();
    });

    test('handles special characters in parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters('ESPAÑA & PORTUGAL', 'TEST/UNIT', 'DEV & QA');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith(
        'stats-summary/filters?area=ESPA%C3%91A+%26+PORTUGAL&orgN1=TEST%2FUNIT&orgN2fabrica=DEV+%26+QA&page=0'
      );
    });

    test('handles empty arrays', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockResponseData });

      await getStatsSummaryByFilters('ARGENTINA', [], []);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/filters?area=ARGENTINA&page=0');
    });
  });

  describe('getCountSummaryByFilters', () => {
    const mockCountSummaryData = {
      totalVerticales: 5,
      totalFabricas: 12,
      totalSn1: 25,
      totalSn2: 48,
      totalUuaas: 120
    };

    test('calls API with no parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      const result = await getCountSummaryByFilters();

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?');
      expect(result).toEqual(mockCountSummaryData);
    });

    test('calls API with vertical parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters('CONSUMER_DIGITAL');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?vertical=CONSUMER_DIGITAL');
    });

    test('calls API with single fabrica parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, 'ARCHITECTURE');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?fabrica=ARCHITECTURE');
    });

    test('calls API with array of fabrica parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, ['ARCHITECTURE', 'DEVELOPMENT']);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?fabrica=ARCHITECTURE&fabrica=DEVELOPMENT');
    });

    test('calls API with single sn1 parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, undefined, 'CORE_BANKING');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?sn1=CORE_BANKING');
    });

    test('calls API with array of sn1 parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, undefined, ['CORE_BANKING', 'DIGITAL_CHANNELS']);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?sn1=CORE_BANKING&sn1=DIGITAL_CHANNELS');
    });

    test('calls API with single sn2 parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, undefined, undefined, 'PAYMENTS');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?sn2=PAYMENTS');
    });

    test('calls API with array of sn2 parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, undefined, undefined, ['PAYMENTS', 'LOANS']);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?sn2=PAYMENTS&sn2=LOANS');
    });

    test('calls API with single uuaa parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, undefined, undefined, undefined, 'BBVA001');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?uuaa=BBVA001');
    });

    test('calls API with array of uuaa parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(undefined, undefined, undefined, undefined, ['BBVA001', 'BBVA002']);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?uuaa=BBVA001&uuaa=BBVA002');
    });

    test('calls API with all parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters(
        'CONSUMER_DIGITAL',
        ['ARCHITECTURE'],
        ['CORE_BANKING'],
        ['PAYMENTS'],
        ['BBVA001']
      );

      expect(mockedFuentusApi.get).toHaveBeenCalledWith(
        'stats-summary/count?vertical=CONSUMER_DIGITAL&fabrica=ARCHITECTURE&sn1=CORE_BANKING&sn2=PAYMENTS&uuaa=BBVA001'
      );
    });

    test('handles API error', async () => {
      const error = new Error('Count API Error');
      mockedFuentusApi.get.mockRejectedValue(error);

      await expect(getCountSummaryByFilters()).rejects.toThrow('Count API Error');
    });

    test('logs error when API fails', async () => {
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation();
      const error = new Error('Network Error');
      mockedFuentusApi.get.mockRejectedValue(error);

      try {
        await getCountSummaryByFilters();
      } catch (e) {
        // Expected to throw
      }

      expect(consoleSpy).toHaveBeenCalledWith('Error fetching count summary:', error);
      consoleSpy.mockRestore();
    });

    test('handles special characters in parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters('CONSUMER & BUSINESS', 'ARCH/DEV', 'CORE & DIGITAL');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith(
        'stats-summary/count?vertical=CONSUMER+%26+BUSINESS&fabrica=ARCH%2FDEV&sn1=CORE+%26+DIGITAL'
      );
    });

    test('handles empty arrays', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCountSummaryData });

      await getCountSummaryByFilters('CONSUMER_DIGITAL', [], []);

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/count?vertical=CONSUMER_DIGITAL');
    });
  });

  describe('getCoverageAverageByLevel', () => {
    const mockCoverageData = [
      {
        label: 'CONSUMER_DIGITAL',
        coveragePercentage: 75.5,
        nucleusLevel: 'VERTICAL' as const
      },
      {
        label: 'BUSINESS_BANKING',
        coveragePercentage: 82.3,
        nucleusLevel: 'VERTICAL' as const
      }
    ];

    test('calls API with no parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      const result = await getCoverageAverageByLevel();

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?');
      expect(result).toEqual(mockCoverageData);
    });

    test('calls API with vertical parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel('CONSUMER_DIGITAL');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?vertical=CONSUMER_DIGITAL');
    });

    test('calls API with uol2 parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel(undefined, 'DIGITAL_SERVICES');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?uol2=DIGITAL_SERVICES');
    });

    test('calls API with sn1 parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel(undefined, undefined, 'CORE_BANKING');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?sn1=CORE_BANKING');
    });

    test('calls API with sn2 parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel(undefined, undefined, undefined, 'PAYMENTS');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?sn2=PAYMENTS');
    });

    test('calls API with uuaa parameter', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel(undefined, undefined, undefined, undefined, 'BBVA001');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?uuaa=BBVA001');
    });

    test('calls API with all parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel(
        'CONSUMER_DIGITAL',
        'DIGITAL_SERVICES',
        'CORE_BANKING',
        'PAYMENTS',
        'BBVA001'
      );

      expect(mockedFuentusApi.get).toHaveBeenCalledWith(
        'stats-summary/coverage-average?vertical=CONSUMER_DIGITAL&uol2=DIGITAL_SERVICES&sn1=CORE_BANKING&sn2=PAYMENTS&uuaa=BBVA001'
      );
    });

    test('handles API error', async () => {
      const error = new Error('Coverage API Error');
      mockedFuentusApi.get.mockRejectedValue(error);

      await expect(getCoverageAverageByLevel()).rejects.toThrow('Coverage API Error');
    });

    test('logs error when API fails', async () => {
      const consoleSpy = jest.spyOn(console, 'error').mockImplementation();
      const error = new Error('Network Error');
      mockedFuentusApi.get.mockRejectedValue(error);

      try {
        await getCoverageAverageByLevel();
      } catch (e) {
        // Expected to throw
      }

      expect(consoleSpy).toHaveBeenCalledWith('Error fetching coverage average by level:', error);
      consoleSpy.mockRestore();
    });

    test('handles special characters in parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel('CONSUMER & DIGITAL', 'ARCH/DEV', 'CORE & BANKING');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith(
        'stats-summary/coverage-average?vertical=CONSUMER+%26+DIGITAL&uol2=ARCH%2FDEV&sn1=CORE+%26+BANKING'
      );
    });

    test('handles empty string parameters', async () => {
      mockedFuentusApi.get.mockResolvedValue({ data: mockCoverageData });

      await getCoverageAverageByLevel('', '', '');

      expect(mockedFuentusApi.get).toHaveBeenCalledWith('stats-summary/coverage-average?');
    });
  });
});
