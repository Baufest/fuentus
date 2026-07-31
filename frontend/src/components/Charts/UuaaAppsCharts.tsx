import React from 'react';
import ReactApexChart from 'react-apexcharts';
import { ApexOptions } from 'apexcharts';
import { Apps } from '../../types/app';

interface UuaaAppsChartsProps {
  apps: Apps[];
  loading: boolean;
}

const UuaaAppsCharts: React.FC<UuaaAppsChartsProps> = ({ apps, loading }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-6">
        <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark animate-pulse h-64" />
        <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark animate-pulse h-64" />
      </div>
    );
  }

  if (apps.length === 0) {
    return null;
  }

  // Filter apps with coverage data and sort by coverage
  const appsWithCoverage = apps
    .filter(app => app.coverage !== null && app.coverage !== undefined)
    .sort((a, b) => (b.coverage || 0) - (a.coverage || 0))
    .slice(0, 10); // Top 10

  // Prepare coverage chart data
  const coverageCategories = appsWithCoverage.map(app => 
    app.name.length > 15 ? `${app.name.substring(0, 15)}...` : app.name
  );
  const coverageValues = appsWithCoverage.map(app => app.coverage || 0);

  const coverageOptions: ApexOptions = {
    chart: {
      type: 'bar',
      fontFamily: 'Satoshi, sans-serif',
      toolbar: { show: false },
      height: 250,
    },
    colors: ['#0f766e'],
    plotOptions: {
      bar: {
        horizontal: true,
        borderRadius: 4,
        barHeight: '70%',
      },
    },
    dataLabels: {
      enabled: true,
      formatter: (val: number) => `${val}%`,
      style: { fontSize: '11px' },
    },
    xaxis: {
      categories: coverageCategories,
      max: 100,
      labels: {
        formatter: (val: string) => `${val}%`,
      },
    },
    yaxis: {
      labels: {
        style: { fontSize: '11px' },
      },
    },
    title: {
      text: 'Coverage por Aplicación (Top 10)',
      align: 'left',
      style: {
        fontSize: '14px',
        fontWeight: 'bold',
      },
    },
    tooltip: {
      custom: ({ dataPointIndex }) => {
        const app = appsWithCoverage[dataPointIndex];
        return `
          <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg">
            <div class="font-semibold text-gray-900">${app.name}</div>
            <div class="text-sm text-gray-600">Coverage: ${app.coverage}%</div>
          </div>
        `;
      },
    },
  };

  const coverageSeries = [{ name: 'Coverage', data: coverageValues }];

  // Prepare vulnerability chart data
  const vulnerabilityData = apps.map(app => ({
    name: app.name.length > 12 ? `${app.name.substring(0, 12)}...` : app.name,
    fullName: app.name,
    sastHigh: app.chimeraSast?.totalHigh || 0,
    sastMedium: app.chimeraSast?.totalMedium || 0,
    sastLow: app.chimeraSast?.totalLow || 0,
    scaCritical: app.chimeraSca?.totalCritical || 0,
    scaHigh: app.chimeraSca?.totalHigh || 0,
    scaMedium: app.chimeraSca?.totalMedium || 0,
    scaLow: app.chimeraSca?.totalLow || 0,
    total: 
      (app.chimeraSast?.totalHigh || 0) + 
      (app.chimeraSast?.totalMedium || 0) + 
      (app.chimeraSast?.totalLow || 0) +
      (app.chimeraSca?.totalCritical || 0) +
      (app.chimeraSca?.totalHigh || 0) +
      (app.chimeraSca?.totalMedium || 0) +
      (app.chimeraSca?.totalLow || 0),
  }))
  .filter(app => app.total > 0)
  .sort((a, b) => b.total - a.total)
  .slice(0, 10);

  const vulnCategories = vulnerabilityData.map(d => d.name);

  const vulnOptions: ApexOptions = {
    chart: {
      type: 'bar',
      fontFamily: 'Satoshi, sans-serif',
      stacked: true,
      toolbar: { show: false },
      height: 250,
    },
    plotOptions: {
      bar: {
        horizontal: true,
        borderRadius: 2,
        barHeight: '70%',
      },
    },
    dataLabels: { enabled: false },
    xaxis: { categories: vulnCategories },
    yaxis: {
      labels: {
        style: { fontSize: '11px' },
      },
    },
    title: {
      text: 'Vulnerabilidades por Aplicación (Top 10)',
      align: 'left',
      style: {
        fontSize: '14px',
        fontWeight: 'bold',
      },
    },
    legend: {
      position: 'top',
      horizontalAlign: 'left',
      fontSize: '11px',
    },
    colors: ['#dc2626', '#f87171', '#f59e0b', '#10b981'],
    tooltip: {
      custom: ({ dataPointIndex }) => {
        const d = vulnerabilityData[dataPointIndex];
        return `
          <div class="px-3 py-2 bg-white border border-gray-200 rounded shadow-lg">
            <div class="font-semibold text-gray-900 mb-2">${d.fullName}</div>
            <div class="text-xs space-y-1">
              <div class="font-medium">SAST:</div>
              <div class="flex gap-2 ml-2">
                <span class="text-red-500">${d.sastHigh}H</span>
                <span class="text-yellow-500">${d.sastMedium}M</span>
                <span class="text-green-500">${d.sastLow}L</span>
              </div>
              <div class="font-medium">SCA:</div>
              <div class="flex gap-2 ml-2">
                <span class="text-red-700">${d.scaCritical}C</span>
                <span class="text-red-500">${d.scaHigh}H</span>
                <span class="text-yellow-500">${d.scaMedium}M</span>
                <span class="text-green-500">${d.scaLow}L</span>
              </div>
            </div>
          </div>
        `;
      },
    },
  };

  const vulnSeries = [
    { name: 'Critical', data: vulnerabilityData.map(d => d.scaCritical) },
    { name: 'High', data: vulnerabilityData.map(d => d.sastHigh + d.scaHigh) },
    { name: 'Medium', data: vulnerabilityData.map(d => d.sastMedium + d.scaMedium) },
    { name: 'Low', data: vulnerabilityData.map(d => d.sastLow + d.scaLow) },
  ];

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-6">
      {/* Coverage Chart */}
      {appsWithCoverage.length > 0 && (
        <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
          <ReactApexChart
            options={coverageOptions}
            series={coverageSeries}
            type="bar"
            height={250}
          />
        </div>
      )}

      {/* Vulnerabilities Chart */}
      {vulnerabilityData.length > 0 && (
        <div className="rounded-lg border border-stroke bg-white p-5 shadow-default dark:border-strokedark dark:bg-boxdark">
          <ReactApexChart
            options={vulnOptions}
            series={vulnSeries}
            type="bar"
            height={250}
          />
        </div>
      )}
    </div>
  );
};

export default UuaaAppsCharts;
