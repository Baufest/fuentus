import {
  createCustomTooltip,
  addTooltipEventListeners,
  removeTooltipEventListeners,
  formatCoverageTooltip,
  formatVulnerabilityTooltip,
  addTooltipStyles,
  createCoverageTooltipContent,
  createChimeraTooltipContent,
  formatChimeraTooltipContent,
  scrollToElement,
  TooltipData
} from '../../utils/chartUtils';

// Mock DOM methods
Object.defineProperty(document, 'getElementById', {
  value: jest.fn(),
  writable: true
});

// Mock element with scrollIntoView
const mockElement = {
  scrollIntoView: jest.fn()
};

describe('chartUtils', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    jest.clearAllTimers();
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  describe('createCustomTooltip', () => {
    it('creates basic tooltip without extra content', () => {
      const data: TooltipData = {
        uuaa: 'TEST-APP',
        totalApps: 5
      };

      const result = createCustomTooltip(data);

      expect(result).toContain('TEST-APP');
      expect(result).toContain('Total Apps: 5');
      expect(result).toContain('font-semibold text-gray-900');
      expect(result).toContain('text-sm text-gray-600');
    });

    it('creates tooltip with extra content', () => {
      const data: TooltipData = {
        uuaa: 'ANOTHER-APP',
        totalApps: 3
      };
      const extraContent = 'Coverage: 85%';

      const result = createCustomTooltip(data, extraContent);

      expect(result).toContain('ANOTHER-APP');
      expect(result).toContain('Total Apps: 3');
      expect(result).toContain('Coverage: 85%');
    });

    it('handles data with additional properties', () => {
      const data: TooltipData = {
        uuaa: 'COMPLEX-APP',
        totalApps: 10,
        environment: 'production',
        version: '1.2.3'
      };

      const result = createCustomTooltip(data);

      expect(result).toContain('COMPLEX-APP');
      expect(result).toContain('Total Apps: 10');
      // Additional properties should not break the function
    });

    it('handles empty extra content', () => {
      const data: TooltipData = {
        uuaa: 'TEST-APP',
        totalApps: 1
      };

      const result = createCustomTooltip(data, '');

      expect(result).toContain('TEST-APP');
      expect(result).toContain('Total Apps: 1');
      expect(result).not.toContain('<div class="text-sm text-gray-600"></div>');
    });
  });

  describe('addTooltipEventListeners', () => {
    it('adds event listener when chart ref and chart exist', () => {
      const mockChart = {
        addEventListener: jest.fn()
      };
      const mockChartRef = {
        current: {
          chart: mockChart
        }
      };
      const mockCallback = jest.fn();

      addTooltipEventListeners(mockChartRef, mockCallback);

      expect(mockChart.addEventListener).toHaveBeenCalledWith(
        'dataPointSelection',
        expect.any(Function)
      );
    });

    it('does nothing when chart ref is null', () => {
      const mockCallback = jest.fn();

      addTooltipEventListeners(null, mockCallback);

      // Should not throw error
      expect(mockCallback).not.toHaveBeenCalled();
    });

    it('does nothing when chart ref current is null', () => {
      const mockChartRef = {
        current: null
      };
      const mockCallback = jest.fn();

      addTooltipEventListeners(mockChartRef, mockCallback);

      // Should not throw error
      expect(mockCallback).not.toHaveBeenCalled();
    });

    it('does nothing when chart is null', () => {
      const mockChartRef = {
        current: {
          chart: null
        }
      };
      const mockCallback = jest.fn();

      addTooltipEventListeners(mockChartRef, mockCallback);

      // Should not throw error
      expect(mockCallback).not.toHaveBeenCalled();
    });

    it('calls callback when event is triggered', () => {
      const mockChart = {
        addEventListener: jest.fn()
      };
      const mockChartRef = {
        current: {
          chart: mockChart
        }
      };
      const mockCallback = jest.fn();

      addTooltipEventListeners(mockChartRef, mockCallback);

      // Simulate the event listener being called
      const eventListener = mockChart.addEventListener.mock.calls[0][1];
      const mockConfig = {
        dataPointIndex: 1,
        seriesIndex: 0,
        w: { globals: { labels: ['A', 'B', 'C'] } }
      };

      eventListener(null, null, mockConfig);

      expect(mockCallback).toHaveBeenCalledWith({
        dataPointIndex: 1,
        seriesIndex: 0,
        w: { globals: { labels: ['A', 'B', 'C'] } }
      });
    });
  });

  describe('removeTooltipEventListeners', () => {
    it('handles null chart ref gracefully', () => {
      // Should not throw error
      removeTooltipEventListeners(null);
    });

    it('handles null current gracefully', () => {
      const mockChartRef = {
        current: null
      };

      // Should not throw error
      removeTooltipEventListeners(mockChartRef);
    });

    it('handles null chart gracefully', () => {
      const mockChartRef = {
        current: {
          chart: null
        }
      };

      // Should not throw error
      removeTooltipEventListeners(mockChartRef);
    });

    it('handles valid chart ref without errors', () => {
      const mockChart = {};
      const mockChartRef = {
        current: {
          chart: mockChart
        }
      };

      // Should not throw error
      removeTooltipEventListeners(mockChartRef);
    });
  });

  describe('formatCoverageTooltip', () => {
    it('formats high coverage (>=80%) with green color', () => {
      const result = formatCoverageTooltip(85, 'High Coverage App');

      expect(result).toContain('High Coverage App');
      expect(result).toContain('85%');
      expect(result).toContain('text-green-600');
      expect(result).toContain('bg-white border border-gray-200 rounded shadow-lg');
    });

    it('formats medium coverage (60-79%) with yellow color', () => {
      const result = formatCoverageTooltip(65, 'Medium Coverage App');

      expect(result).toContain('Medium Coverage App');
      expect(result).toContain('65%');
      expect(result).toContain('text-yellow-600');
    });

    it('formats low coverage (<60%) with red color', () => {
      const result = formatCoverageTooltip(45, 'Low Coverage App');

      expect(result).toContain('Low Coverage App');
      expect(result).toContain('45%');
      expect(result).toContain('text-red-600');
    });

    it('formats without app name when not provided', () => {
      const result = formatCoverageTooltip(75);

      expect(result).toContain('75%');
      expect(result).toContain('text-yellow-600');
      expect(result).not.toContain('font-medium text-gray-900 mb-1');
    });

    it('handles edge cases for coverage values', () => {
      expect(formatCoverageTooltip(80)).toContain('text-green-600'); // Exactly 80
      expect(formatCoverageTooltip(60)).toContain('text-yellow-600'); // Exactly 60
      expect(formatCoverageTooltip(0)).toContain('text-red-600'); // Zero coverage
      expect(formatCoverageTooltip(100)).toContain('text-green-600'); // Perfect coverage
    });
  });

  describe('formatVulnerabilityTooltip', () => {
    it('formats vulnerability tooltip with all severity levels', () => {
      const sast = { totalHigh: 5, totalMedium: 10, totalLow: 15 };
      const sca = { totalCritical: 2, totalHigh: 3, totalMedium: 8, totalLow: 12 };

      const result = formatVulnerabilityTooltip(sast, sca, 'Vulnerable App');

      expect(result).toContain('Vulnerable App');
      expect(result).toContain('SAST:');
      expect(result).toContain('5H');
      expect(result).toContain('10M');
      expect(result).toContain('15L');
      expect(result).toContain('SCA:');
      expect(result).toContain('2C');
      expect(result).toContain('3H');
      expect(result).toContain('8M');
      expect(result).toContain('12L');
    });

    it('formats without app name when not provided', () => {
      const sast = { totalHigh: 1, totalMedium: 2, totalLow: 3 };
      const sca = { totalCritical: 0, totalHigh: 1, totalMedium: 2, totalLow: 3 };

      const result = formatVulnerabilityTooltip(sast, sca);

      expect(result).toContain('SAST:');
      expect(result).toContain('SCA:');
      expect(result).not.toContain('font-medium text-gray-900 mb-2');
    });

    it('handles zero vulnerabilities', () => {
      const sast = { totalHigh: 0, totalMedium: 0, totalLow: 0 };
      const sca = { totalCritical: 0, totalHigh: 0, totalMedium: 0, totalLow: 0 };

      const result = formatVulnerabilityTooltip(sast, sca, 'Clean App');

      expect(result).toContain('Clean App');
      expect(result).toContain('0H');
      expect(result).toContain('0M');
      expect(result).toContain('0L');
      expect(result).toContain('0C');
    });
  });

  describe('addTooltipStyles', () => {
    it('returns CSS styles for custom tooltips', () => {
      const styles = addTooltipStyles();

      expect(styles).toContain('.custom-tooltip');
      expect(styles).toContain('background: white');
      expect(styles).toContain('border: 1px solid #e5e7eb');
      expect(styles).toContain('border-radius: 6px');
      expect(styles).toContain('z-index: 1000');
      expect(styles).toContain('.tooltip-title');
      expect(styles).toContain('.tooltip-content');
    });

    it('includes all necessary CSS properties', () => {
      const styles = addTooltipStyles();

      expect(styles).toContain('box-shadow');
      expect(styles).toContain('font-family');
      expect(styles).toContain('font-size');
      expect(styles).toContain('max-width');
      expect(styles).toContain('padding');
    });
  });

  describe('createCoverageTooltipContent', () => {
    it('creates coverage tooltip content from chart params', () => {
      const params = {
        series: [[75, 85, 65]],
        dataPointIndex: 1,
        w: {
          globals: {
            labels: ['App A', 'App B', 'App C']
          }
        }
      };

      const result = createCoverageTooltipContent(params);

      expect(result).toContain('App B');
      expect(result).toContain('85%');
      expect(result).toContain('text-green-600'); // 85% should be green
    });

    it('handles edge case with missing series data', () => {
      const params = {
        series: [[]],
        dataPointIndex: 0,
        w: {
          globals: {
            labels: ['App A']
          }
        }
      };

      const result = createCoverageTooltipContent(params);
      
      // Should not crash and should contain the app name
      expect(result).toContain('App A');
    });
  });

  describe('createChimeraTooltipContent', () => {
    it('creates chimera tooltip content from chart params', () => {
      const params = {
        series: [[20, 30, 15], [10, 25, 8]],
        dataPointIndex: 1,
        w: {
          globals: {
            labels: ['App A', 'App B', 'App C']
          }
        }
      };

      const result = createChimeraTooltipContent(params);

      expect(result).toContain('App B');
      expect(result).toContain('SAST:');
      expect(result).toContain('SCA:');
    });

    it('handles missing series data gracefully', () => {
      const params = {
        series: [],
        dataPointIndex: 0,
        w: {
          globals: {
            labels: ['App A']
          }
        }
      };

      const result = createChimeraTooltipContent(params);

      expect(result).toContain('App A');
      expect(result).toContain('SAST:');
      expect(result).toContain('SCA:');
    });
  });

  describe('formatChimeraTooltipContent', () => {
    it('formats SAST vulnerability data', () => {
      const sastData = { totalHigh: 3, totalMedium: 7, totalLow: 12 };
      
      const result = formatChimeraTooltipContent('SAST', sastData);

      expect(result).toContain('SAST Vulnerabilities');
      expect(result).toContain('3H');
      expect(result).toContain('7M');
      expect(result).toContain('12L');
      expect(result).toContain('text-red-600');
      expect(result).toContain('text-orange-600');
      expect(result).toContain('text-yellow-600');
    });

    it('formats SCA vulnerability data', () => {
      const scaData = { totalCritical: 1, totalHigh: 2, totalMedium: 5, totalLow: 8 };
      
      const result = formatChimeraTooltipContent('SCA', scaData);

      expect(result).toContain('SCA Vulnerabilities');
      expect(result).toContain('1C');
      expect(result).toContain('2H');
      expect(result).toContain('5M');
      expect(result).toContain('8L');
      expect(result).toContain('text-purple-600');
      expect(result).toContain('text-red-600');
      expect(result).toContain('text-orange-600');
      expect(result).toContain('text-yellow-600');
    });

    it('returns empty string for unknown type', () => {
      const data = { totalHigh: 1, totalMedium: 2, totalLow: 3 };
      
      const result = formatChimeraTooltipContent('UNKNOWN', data);

      expect(result).toBe('');
    });
  });

  describe('scrollToElement', () => {
    it('scrolls to element when found', () => {
      (document.getElementById as jest.Mock).mockReturnValue(mockElement);

      scrollToElement('test-element');

      jest.advanceTimersByTime(100);

      expect(document.getElementById).toHaveBeenCalledWith('test-element');
      expect(mockElement.scrollIntoView).toHaveBeenCalledWith({
        behavior: 'smooth',
        block: 'start'
      });
    });

    it('does nothing when element not found', () => {
      (document.getElementById as jest.Mock).mockReturnValue(null);

      scrollToElement('non-existent-element');

      jest.advanceTimersByTime(100);

      expect(document.getElementById).toHaveBeenCalledWith('non-existent-element');
      expect(mockElement.scrollIntoView).not.toHaveBeenCalled();
    });

    it('uses custom delay', () => {
      (document.getElementById as jest.Mock).mockReturnValue(mockElement);

      scrollToElement('test-element', 500);

      jest.advanceTimersByTime(499);
      expect(mockElement.scrollIntoView).not.toHaveBeenCalled();

      jest.advanceTimersByTime(1);
      expect(mockElement.scrollIntoView).toHaveBeenCalled();
    });

    it('handles multiple calls correctly', () => {
      (document.getElementById as jest.Mock).mockReturnValue(mockElement);

      scrollToElement('element1', 200);
      scrollToElement('element2', 300);

      jest.advanceTimersByTime(200);
      expect(document.getElementById).toHaveBeenCalledWith('element1');

      jest.advanceTimersByTime(100);
      expect(document.getElementById).toHaveBeenCalledWith('element2');

      expect(mockElement.scrollIntoView).toHaveBeenCalledTimes(2);
    });
  });
});
