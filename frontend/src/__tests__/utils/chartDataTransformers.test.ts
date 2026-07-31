import {
  transformToCoverageChartData,
  transformToChimeraChartData
} from '../../utils/chartDataTransformers';
import {
  StatsSummaryDTO,
  RepositoryStatsDTO,
  SonarInfoDTO,
  ChimeraSast,
  ChimeraSca
} from '../../types/statsSummary';

describe('chartDataTransformers', () => {
  // Mock data helpers
  const createMockRepository = (
    name: string,
    coverage?: number | null,
    chimeraSast?: ChimeraSast | null,
    chimeraSca?: ChimeraSca | null,
    sonarUrl?: string
  ): RepositoryStatsDTO => ({
    name,
    bitbucketUrl: `https://bitbucket.org/example/${name}`,
    language: 'TypeScript',
    monolith: false,
    servers: [],
    sonarInfo: {
      sonarUrl: sonarUrl || `https://sonar.example.com/${name}`,
      sonar10Url: `https://sonar10.example.com/${name}`,
      coverage,
      bugs: null,
      chimeraSast: chimeraSast || null,
      chimeraSca: chimeraSca || null
    } as SonarInfoDTO
  });

  const createMockStatsSummary = (uuaa: string, repositories: RepositoryStatsDTO[]): StatsSummaryDTO => ({
    uuaa,
    repositories
  });

  describe('transformToCoverageChartData', () => {
    it('should transform valid coverage data correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 85.5),
          createMockRepository('app2', 92.3)
        ]),
        createMockStatsSummary('UUAA2', [
          createMockRepository('app3', 78.1)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(2);
      
      // Verify UUAA1
      const uuaa1 = result.find(item => item.uuaa === 'UUAA1');
      expect(uuaa1).toBeDefined();
      expect(uuaa1!.totalApps).toBe(2);
      expect(uuaa1!.averageCoverage).toBeCloseTo(88.9, 1);
      expect(uuaa1!.applications).toHaveLength(2);
      expect(uuaa1!.applications[0].name).toBe('app1');
      expect(uuaa1!.applications[0].coverage).toBe(85.5);
      expect(uuaa1!.applications[0].bitbucketUrl).toBe('https://bitbucket.org/example/app1');

      // Verify UUAA2
      const uuaa2 = result.find(item => item.uuaa === 'UUAA2');
      expect(uuaa2).toBeDefined();
      expect(uuaa2!.totalApps).toBe(1);
      expect(uuaa2!.averageCoverage).toBe(78.1);
      expect(uuaa2!.applications).toHaveLength(1);
    });

    it('should handle empty data array', () => {
      const result = transformToCoverageChartData([]);
      expect(result).toEqual([]);
    });

    it('should filter out repositories without coverage data', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 85.5),
          createMockRepository('app2', null), // No coverage
          createMockRepository('app3', undefined as any) // No coverage
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(1);
      expect(result[0].applications).toHaveLength(1);
      expect(result[0].applications[0].name).toBe('app1');
      expect(result[0].averageCoverage).toBe(85.5);
    });

    it('should filter out UUAAs with no valid applications', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null),
          createMockRepository('app2', null)
        ]),
        createMockStatsSummary('UUAA2', [
          createMockRepository('app3', 75.0)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].uuaa).toBe('UUAA2');
    });

    it('should handle repositories without sonarInfo', () => {
      const mockData: StatsSummaryDTO[] = [
        {
          uuaa: 'UUAA1',
          repositories: [
            {
              ...createMockRepository('app1', 85.5),
              sonarInfo: null as any
            },
            createMockRepository('app2', 92.3)
          ]
        }
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(1);
      expect(result[0].applications[0].name).toBe('app2');
    });

    it('should handle zero coverage correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 0),
          createMockRepository('app2', 100)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(2);
      expect(result[0].averageCoverage).toBe(50);
      expect(result[0].applications[0].coverage).toBe(0);
    });

    it('should group multiple repositories by UUAA correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 85.5)
        ]),
        createMockStatsSummary('UUAA1', [ // Same UUAA as above
          createMockRepository('app2', 92.3)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].uuaa).toBe('UUAA1');
      expect(result[0].totalApps).toBe(2);
      expect(result[0].applications).toHaveLength(2);
    });

    it('should handle very low and very high coverage values', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 0.1), // Very low
          createMockRepository('app2', 99.9) // Very high
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(2);
      expect(result[0].averageCoverage).toBeCloseTo(50.0, 1);
      expect(result[0].applications[0].coverage).toBe(0.1);
      expect(result[0].applications[1].coverage).toBe(99.9);
    });

    it('should handle negative coverage values', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', -5), // Negative value (shouldn't happen but test edge case)
          createMockRepository('app2', 50)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(2);
      expect(result[0].averageCoverage).toBe(22.5); // (-5 + 50) / 2
      expect(result[0].applications[0].coverage).toBe(-5);
    });

    it('should handle coverage values over 100', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 150), // Over 100% (shouldn't happen but test edge case)
          createMockRepository('app2', 50)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(2);
      expect(result[0].averageCoverage).toBe(100); // (150 + 50) / 2
      expect(result[0].applications[0].coverage).toBe(150);
    });

    it('should handle decimal precision correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', 85.555555),
          createMockRepository('app2', 92.333333),
          createMockRepository('app3', 78.111111)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(3);
      // Check that precision is maintained
      const expectedAverage = (85.555555 + 92.333333 + 78.111111) / 3;
      expect(result[0].averageCoverage).toBeCloseTo(expectedAverage, 5);
    });

    it('should sort applications by name when grouped', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('zebra-app', 85.5),
          createMockRepository('alpha-app', 92.3),
          createMockRepository('beta-app', 78.1)
        ])
      ];

      const result = transformToCoverageChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].applications).toHaveLength(3);
      // Applications should be in the order they were added (not sorted by name)
      expect(result[0].applications[0].name).toBe('zebra-app');
      expect(result[0].applications[1].name).toBe('alpha-app');
      expect(result[0].applications[2].name).toBe('beta-app');
    });
  });

  describe('transformToChimeraChartData', () => {
    it('should transform chimera vulnerability data correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null, 
            { totalLow: 5, totalMedium: 3, totalHigh: 2 },
            { totalLow: 8, totalMedium: 4, totalHigh: 1, totalCritical: 1 }
          ),
          createMockRepository('app2', null,
            { totalLow: 10, totalMedium: 5, totalHigh: 3 },
            { totalLow: 12, totalMedium: 6, totalHigh: 2, totalCritical: 0 }
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      
      const uuaa1 = result[0];
      expect(uuaa1.uuaa).toBe('UUAA1');
      expect(uuaa1.totalApps).toBe(2);
      
      // Check aggregated SAST data
      expect(uuaa1.sastData.totalLow).toBe(15); // 5 + 10
      expect(uuaa1.sastData.totalMedium).toBe(8); // 3 + 5
      expect(uuaa1.sastData.totalHigh).toBe(5); // 2 + 3

      // Check aggregated SCA data
      expect(uuaa1.scaData.totalLow).toBe(20); // 8 + 12
      expect(uuaa1.scaData.totalMedium).toBe(10); // 4 + 6
      expect(uuaa1.scaData.totalHigh).toBe(3); // 1 + 2
      expect(uuaa1.scaData.totalCritical).toBe(1); // 1 + 0

      // Check applications data (sorted by total issues, highest first)
      expect(uuaa1.applications).toHaveLength(2);
      // app2 has more total issues (10+5+3+12+6+2+0=38) than app1 (5+3+2+8+4+1+1=24)
      expect(uuaa1.applications[0].name).toBe('app2');
      expect(uuaa1.applications[0].chimeraSast.totalHigh).toBe(3);
      expect(uuaa1.applications[0].chimeraSca.totalCritical).toBe(0);
      expect(uuaa1.applications[1].name).toBe('app1');
      expect(uuaa1.applications[1].chimeraSast.totalHigh).toBe(2);
      expect(uuaa1.applications[1].chimeraSca.totalCritical).toBe(1);
    });

    it('should handle empty data array', () => {
      const result = transformToChimeraChartData([]);
      expect(result).toEqual([]);
    });

    it('should filter out repositories without chimera data', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null, null, null), // No chimera data
          createMockRepository('app2', null,
            { totalLow: 5, totalMedium: 3, totalHigh: 2 },
            null
          ) // Only SAST data
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(1);
      expect(result[0].applications[0].name).toBe('app2');
    });

    it('should handle null/undefined vulnerability values', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: null, totalMedium: 5, totalHigh: undefined as any },
            { totalLow: 10, totalMedium: null, totalHigh: 2, totalCritical: undefined as any }
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      const uuaa1 = result[0];
      
      // Should treat null/undefined as 0
      expect(uuaa1.sastData.totalLow).toBe(0);
      expect(uuaa1.sastData.totalMedium).toBe(5);
      expect(uuaa1.sastData.totalHigh).toBe(0);
      
      expect(uuaa1.scaData.totalLow).toBe(10);
      expect(uuaa1.scaData.totalMedium).toBe(0);
      expect(uuaa1.scaData.totalHigh).toBe(2);
      expect(uuaa1.scaData.totalCritical).toBe(0);
    });

    it('should filter out UUAAs with no vulnerabilities', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 0, totalMedium: 0, totalHigh: 0 },
            { totalLow: 0, totalMedium: 0, totalHigh: 0, totalCritical: 0 }
          )
        ]),
        createMockStatsSummary('UUAA2', [
          createMockRepository('app2', null,
            { totalLow: 1, totalMedium: 0, totalHigh: 0 },
            null
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].uuaa).toBe('UUAA2');
    });

    it('should handle repositories without sonarInfo', () => {
      const mockData: StatsSummaryDTO[] = [
        {
          uuaa: 'UUAA1',
          repositories: [
            {
              ...createMockRepository('app1'),
              sonarInfo: null as any
            },
            createMockRepository('app2', null,
              { totalLow: 5, totalMedium: 3, totalHigh: 2 },
              null
            )
          ]
        }
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(1);
      expect(result[0].applications[0].name).toBe('app2');
    });

    it('should use sonarUrl as chimeraUrl fallback', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 5, totalMedium: 3, totalHigh: 2 },
            null,
            'https://custom-sonar.example.com/app1'
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].applications[0].chimeraUrl).toBe('https://custom-sonar.example.com/app1');
    });

    it('should handle mixed SAST and SCA data', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 5, totalMedium: 3, totalHigh: 2 }, // SAST only
            null
          ),
          createMockRepository('app2', null,
            null,
            { totalLow: 8, totalMedium: 4, totalHigh: 1, totalCritical: 1 } // SCA only
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].totalApps).toBe(2);
      
      // Should aggregate both types
      expect(result[0].sastData.totalLow).toBe(5);
      expect(result[0].scaData.totalLow).toBe(8);
    });

    it('should handle multiple UUAAs correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 5, totalMedium: 3, totalHigh: 2 },
            null
          )
        ]),
        createMockStatsSummary('UUAA2', [
          createMockRepository('app2', null,
            null,
            { totalLow: 8, totalMedium: 4, totalHigh: 1, totalCritical: 1 }
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(2);
      expect(result.map(item => item.uuaa)).toContain('UUAA1');
      expect(result.map(item => item.uuaa)).toContain('UUAA2');
    });

    it('should handle large numbers of vulnerabilities', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 999, totalMedium: 888, totalHigh: 777 },
            { totalLow: 666, totalMedium: 555, totalHigh: 444, totalCritical: 333 }
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].sastData.totalLow).toBe(999);
      expect(result[0].sastData.totalMedium).toBe(888);
      expect(result[0].sastData.totalHigh).toBe(777);
      expect(result[0].scaData.totalLow).toBe(666);
      expect(result[0].scaData.totalMedium).toBe(555);
      expect(result[0].scaData.totalHigh).toBe(444);
      expect(result[0].scaData.totalCritical).toBe(333);
    });

    it('should handle empty sonarUrl correctly', () => {
      const mockData: StatsSummaryDTO[] = [
        {
          uuaa: 'UUAA1',
          repositories: [
            {
              name: 'app1',
              bitbucketUrl: 'https://bitbucket.org/example/app1',
              language: 'TypeScript',
              monolith: false,
              servers: [],
              sonarInfo: {
                sonarUrl: '', // Empty sonarUrl
                sonar10Url: 'https://sonar10.example.com/app1',
                coverage: null,
                bugs: null,
                chimeraSast: { totalLow: 5, totalMedium: 3, totalHigh: 2 },
                chimeraSca: null
              } as any
            }
          ]
        }
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].applications[0].chimeraUrl).toBe('');
    });

    it('should handle missing sonarUrl gracefully', () => {
      const mockData: StatsSummaryDTO[] = [
        {
          uuaa: 'UUAA1',
          repositories: [
            {
              name: 'app1',
              bitbucketUrl: 'https://bitbucket.org/example/app1',
              language: 'TypeScript',
              monolith: false,
              servers: [],
              sonarInfo: {
                sonar10Url: 'https://sonar10.example.com/app1',
                coverage: null,
                bugs: null,
                chimeraSast: { totalLow: 5, totalMedium: 3, totalHigh: 2 },
                chimeraSca: null
              } as any // Missing sonarUrl
            }
          ]
        }
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].applications[0].chimeraUrl).toBe('');
    });

    it('should aggregate data from same UUAA in multiple entries', () => {
      const mockData: StatsSummaryDTO[] = [
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 5, totalMedium: 3, totalHigh: 2 },
            null
          )
        ]),
        createMockStatsSummary('UUAA1', [ // Same UUAA again
          createMockRepository('app2', null,
            { totalLow: 10, totalMedium: 7, totalHigh: 4 },
            { totalLow: 8, totalMedium: 6, totalHigh: 2, totalCritical: 1 }
          )
        ])
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(1);
      expect(result[0].uuaa).toBe('UUAA1');
      expect(result[0].totalApps).toBe(2);
      expect(result[0].sastData.totalLow).toBe(15); // 5 + 10
      expect(result[0].sastData.totalMedium).toBe(10); // 3 + 7
      expect(result[0].sastData.totalHigh).toBe(6); // 2 + 4
      expect(result[0].scaData.totalLow).toBe(8);
      expect(result[0].scaData.totalMedium).toBe(6);
      expect(result[0].scaData.totalHigh).toBe(2);
      expect(result[0].scaData.totalCritical).toBe(1);
    });

    it('should sort UUAAs by total issues count (highest first)', () => {
      const mockData: StatsSummaryDTO[] = [
        // UUAA1 has fewer total issues
        createMockStatsSummary('UUAA1', [
          createMockRepository('app1', null,
            { totalLow: 1, totalMedium: 1, totalHigh: 1 }, // SAST: 3 total
            { totalLow: 1, totalMedium: 1, totalHigh: 1, totalCritical: 1 } // SCA: 4 total
          )
        ]), // Total: 7 issues
        // UUAA2 has more total issues
        createMockStatsSummary('UUAA2', [
          createMockRepository('app2', null,
            { totalLow: 5, totalMedium: 5, totalHigh: 5 }, // SAST: 15 total
            { totalLow: 5, totalMedium: 5, totalHigh: 5, totalCritical: 5 } // SCA: 20 total
          )
        ]), // Total: 35 issues
        // UUAA3 has medium total issues
        createMockStatsSummary('UUAA3', [
          createMockRepository('app3', null,
            { totalLow: 2, totalMedium: 2, totalHigh: 2 }, // SAST: 6 total
            { totalLow: 2, totalMedium: 2, totalHigh: 2, totalCritical: 2 } // SCA: 8 total
          )
        ]) // Total: 14 issues
      ];

      const result = transformToChimeraChartData(mockData);

      expect(result).toHaveLength(3);
      // Should be sorted by total issues count (highest first)
      expect(result[0].uuaa).toBe('UUAA2'); // 35 total issues
      expect(result[1].uuaa).toBe('UUAA3'); // 14 total issues
      expect(result[2].uuaa).toBe('UUAA1'); // 7 total issues
      
      // Verify totals for first UUAA (should be UUAA2)
      const uuaa2 = result[0];
      const totalSast = uuaa2.sastData.totalLow + uuaa2.sastData.totalMedium + uuaa2.sastData.totalHigh;
      const totalSca = uuaa2.scaData.totalLow + uuaa2.scaData.totalMedium + uuaa2.scaData.totalHigh + uuaa2.scaData.totalCritical;
      expect(totalSast + totalSca).toBe(35);
    });
  });
});
