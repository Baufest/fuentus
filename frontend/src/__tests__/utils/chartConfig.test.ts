import {
  getBaseChartOptions,
  getBarChartOptions,
  getStackedBarChartOptions,
  getDonutChartOptions,
  SEVERITY_COLORS
} from '../../utils/chartConfig';

describe('chartConfig', () => {
  describe('getBaseChartOptions', () => {
    it('returns base chart configuration', () => {
      const options = getBaseChartOptions();

      expect(options.chart?.fontFamily).toBe('Satoshi, sans-serif');
      expect(options.chart?.toolbar?.show).toBe(false);
      expect(options.dataLabels?.enabled).toBe(false);
    });

    it('includes proper axis label styling', () => {
      const options = getBaseChartOptions();

      expect(options.xaxis?.labels?.style?.colors).toBe('#6b7280');
      expect(options.xaxis?.labels?.style?.fontSize).toBe('12px');
      expect((options.yaxis as any)?.labels?.style?.colors).toBe('#6b7280');
    });

    it('configures legend correctly', () => {
      const options = getBaseChartOptions();

      expect(options.legend?.position).toBe('top');
      expect(options.legend?.horizontalAlign).toBe('left');
      expect(options.legend?.labels?.colors).toBe('#6b7280');
    });

    it('includes grid configuration', () => {
      const options = getBaseChartOptions();

      expect(options.grid?.borderColor).toBe('#e5e7eb');
      expect(options.grid?.strokeDashArray).toBe(0);
    });

    it('configures tooltip settings', () => {
      const options = getBaseChartOptions();

      expect(options.tooltip?.shared).toBe(true);
      expect(options.tooltip?.intersect).toBe(false);
    });

    it('includes responsive configuration', () => {
      const options = getBaseChartOptions();

      expect(options.responsive).toHaveLength(1);
      expect(options.responsive?.[0].breakpoint).toBe(480);
      expect(options.responsive?.[0].options?.legend?.position).toBe('bottom');
    });

    it('sets light theme mode', () => {
      const options = getBaseChartOptions();

      expect(options.theme?.mode).toBe('light');
    });
  });

  describe('getBarChartOptions', () => {
    const mockDataPointSelection = jest.fn();
    const mockXAxisLabelClick = jest.fn();

    beforeEach(() => {
      jest.clearAllMocks();
    });

    it('creates bar chart options with required parameters', () => {
      const options = getBarChartOptions(
        'Test Bar Chart',
        'test-chart-id',
        'Y Axis Title'
      );

      expect(options.chart?.type).toBe('bar');
      expect(options.chart?.id).toBe('test-chart-id');
      expect(options.title?.text).toBe('Test Bar Chart');
      expect((options.yaxis as any)?.title?.text).toBe('Y Axis Title');
    });

    it('includes base chart options', () => {
      const options = getBarChartOptions(
        'Test Chart',
        'test-id',
        'Y Title'
      );

      expect(options.chart?.fontFamily).toBe('Satoshi, sans-serif');
      expect(options.chart?.toolbar?.show).toBe(false);
      expect(options.dataLabels?.enabled).toBe(false);
    });

    it('configures plot options for bars', () => {
      const options = getBarChartOptions(
        'Test Chart',
        'test-id',
        'Y Title'
      );

      expect(options.plotOptions?.bar?.horizontal).toBe(false);
      expect(options.plotOptions?.bar?.columnWidth).toBe('50%');
      expect(options.plotOptions?.bar?.borderRadius).toBe(4);
    });

    it('includes title styling', () => {
      const options = getBarChartOptions(
        'Styled Title',
        'test-id',
        'Y Title'
      );

      expect(options.title?.align).toBe('left');
      expect(options.title?.style?.fontSize).toBe('16px');
      expect(options.title?.style?.fontWeight).toBe('bold');
      expect(options.title?.style?.color).toBe('#374151');
    });

    it('configures y-axis styling', () => {
      const options = getBarChartOptions(
        'Test Chart',
        'test-id',
        'Custom Y Title'
      );

      expect((options.yaxis as any)?.title?.style?.color).toBe('#6b7280');
    });

    it('includes event handlers when provided', () => {
      const options = getBarChartOptions(
        'Test Chart',
        'test-id',
        'Y Title',
        mockDataPointSelection,
        mockXAxisLabelClick
      );

      expect(options.chart?.events?.dataPointSelection).toBe(mockDataPointSelection);
      expect(options.chart?.events?.xAxisLabelClick).toBe(mockXAxisLabelClick);
    });

    it('works without event handlers', () => {
      const options = getBarChartOptions(
        'Test Chart',
        'test-id',
        'Y Title'
      );

      expect(options.chart?.events?.dataPointSelection).toBeUndefined();
      expect(options.chart?.events?.xAxisLabelClick).toBeUndefined();
    });
  });

  describe('getStackedBarChartOptions', () => {
    it('creates stacked bar chart options', () => {
      const options = getStackedBarChartOptions(
        'Stacked Chart',
        'stacked-id',
        'Stacked Y Title'
      );

      expect(options.chart?.type).toBe('bar');
      expect(options.chart?.stacked).toBe(true);
      expect(options.title?.text).toBe('Stacked Chart');
      expect((options.yaxis as any)?.title?.text).toBe('Stacked Y Title');
    });

    it('inherits from bar chart options', () => {
      const options = getStackedBarChartOptions(
        'Stacked Chart',
        'stacked-id',
        'Y Title'
      );

      expect(options.plotOptions?.bar?.horizontal).toBe(false);
      expect(options.plotOptions?.bar?.columnWidth).toBe('50%');
      expect(options.chart?.fontFamily).toBe('Satoshi, sans-serif');
    });

    it('includes event handlers when provided', () => {
      const mockDataPointSelection = jest.fn();
      const mockXAxisLabelClick = jest.fn();

      const options = getStackedBarChartOptions(
        'Stacked Chart',
        'stacked-id',
        'Y Title',
        mockDataPointSelection,
        mockXAxisLabelClick
      );

      expect(options.chart?.events?.dataPointSelection).toBe(mockDataPointSelection);
      expect(options.chart?.events?.xAxisLabelClick).toBe(mockXAxisLabelClick);
    });
  });

  describe('getDonutChartOptions', () => {
    const testColors = ['#ff0000', '#00ff00', '#0000ff'];
    const testLabels = ['Red', 'Green', 'Blue'];

    it('creates donut chart options with colors and labels', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.chart?.type).toBe('donut');
      expect(options.colors).toEqual(testColors);
      expect(options.labels).toEqual(testLabels);
    });

    it('configures chart family font', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.chart?.fontFamily).toBe('Satoshi, sans-serif');
    });

    it('hides legend by default', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.legend?.show).toBe(false);
      expect(options.legend?.position).toBe('bottom');
    });

    it('configures donut plot options', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.plotOptions?.pie?.donut?.size).toBe('65%');
      expect(options.plotOptions?.pie?.donut?.background).toBe('transparent');
    });

    it('disables data labels', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.dataLabels?.enabled).toBe(false);
    });

    it('includes responsive breakpoints', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.responsive).toHaveLength(2);
      
      const largeBreakpoint = options.responsive?.find(r => r.breakpoint === 2600);
      const smallBreakpoint = options.responsive?.find(r => r.breakpoint === 640);
      
      expect(largeBreakpoint?.options?.chart?.width).toBe(380);
      expect(smallBreakpoint?.options?.chart?.width).toBe(200);
    });

    it('includes event handler when provided', () => {
      const mockDataPointSelection = jest.fn();
      
      const options = getDonutChartOptions(
        testColors, 
        testLabels, 
        mockDataPointSelection
      );

      expect(options.chart?.events?.dataPointSelection).toBe(mockDataPointSelection);
    });

    it('works without event handler', () => {
      const options = getDonutChartOptions(testColors, testLabels);

      expect(options.chart?.events?.dataPointSelection).toBeUndefined();
    });

    it('handles empty colors and labels', () => {
      const options = getDonutChartOptions([], []);

      expect(options.colors).toEqual([]);
      expect(options.labels).toEqual([]);
      expect(options.chart?.type).toBe('donut');
    });
  });

  describe('SEVERITY_COLORS', () => {
    it('contains all expected severity levels', () => {
      expect(SEVERITY_COLORS).toHaveProperty('success');
      expect(SEVERITY_COLORS).toHaveProperty('warning');
      expect(SEVERITY_COLORS).toHaveProperty('error');
      expect(SEVERITY_COLORS).toHaveProperty('critical');
      expect(SEVERITY_COLORS).toHaveProperty('primary');
    });

    it('has correct color values', () => {
      expect(SEVERITY_COLORS.success).toBe('#10b981');    // green-500
      expect(SEVERITY_COLORS.warning).toBe('#f59e0b');    // amber-500
      expect(SEVERITY_COLORS.error).toBe('#f87171');      // red-400
      expect(SEVERITY_COLORS.critical).toBe('#dc2626');   // red-600
      expect(SEVERITY_COLORS.primary).toBe('#1973b8');    // blue-600
    });

    it('colors are valid hex format', () => {
      Object.values(SEVERITY_COLORS).forEach(color => {
        expect(color).toMatch(/^#[0-9a-f]{6}$/i);
      });
    });

    it('contains all expected color properties', () => {
      // Test that the expected properties exist based on the actual object
      expect(SEVERITY_COLORS).toHaveProperty('primary');
      expect(SEVERITY_COLORS).toHaveProperty('success');
      expect(SEVERITY_COLORS).toHaveProperty('warning');
      expect(SEVERITY_COLORS).toHaveProperty('error');
      expect(SEVERITY_COLORS).toHaveProperty('critical');
      
      // Verify the object structure matches our expectations
      expect(Object.keys(SEVERITY_COLORS)).toHaveLength(5);
      expect(Object.keys(SEVERITY_COLORS).sort()).toEqual(['critical', 'error', 'primary', 'success', 'warning']);
    });
  });

  describe('Integration tests', () => {
    it('bar chart options can be used to create stacked chart', () => {
      const barOptions = getBarChartOptions('Bar', 'bar-id', 'Y');
      const stackedOptions = getStackedBarChartOptions('Stacked', 'stacked-id', 'Y');

      // Should share most properties but differ in stacked
      expect(barOptions.chart?.type).toBe(stackedOptions.chart?.type);
      expect(barOptions.chart?.fontFamily).toBe(stackedOptions.chart?.fontFamily);
      expect(barOptions.chart?.stacked).toBeUndefined();
      expect(stackedOptions.chart?.stacked).toBe(true);
    });

    it('all chart types use consistent font family', () => {
      const baseOptions = getBaseChartOptions();
      const barOptions = getBarChartOptions('Bar', 'id', 'Y');
      const donutOptions = getDonutChartOptions(['#ff0000'], ['Red']);

      expect(baseOptions.chart?.fontFamily).toBe('Satoshi, sans-serif');
      expect(barOptions.chart?.fontFamily).toBe('Satoshi, sans-serif');
      expect(donutOptions.chart?.fontFamily).toBe('Satoshi, sans-serif');
    });

    it('responsive configurations work across chart types', () => {
      const baseOptions = getBaseChartOptions();
      const donutOptions = getDonutChartOptions(['#ff0000'], ['Red']);

      expect(baseOptions.responsive).toBeDefined();
      expect(donutOptions.responsive).toBeDefined();
      
      // Both should have breakpoint configurations
      expect(baseOptions.responsive?.length).toBeGreaterThan(0);
      expect(donutOptions.responsive?.length).toBeGreaterThan(0);
    });
  });
});
