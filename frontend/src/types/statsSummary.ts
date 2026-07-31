export interface StatsSummaryDTO {
  uuaa: string;
  repositories: RepositoryStatsDTO[];
}

export interface RepositoryStatsDTO {
  name: string;
  bitbucketUrl: string;
  language: string;
  monolith: boolean;
  servers: ServerInfoDTO[];
  sonarInfo: SonarInfoDTO;
}

export interface ServerInfoDTO {
  // Agregar propiedades según la estructura real
  [key: string]: any;
}

export interface SonarInfoDTO {
  sonarUrl: string;
  sonar10Url: string;
  coverage: number | null;
  bugs: number | null;
  chimeraSast: ChimeraSast;
  chimeraSca: ChimeraSca;
}

export interface ChimeraSast {
  totalLow: number | null;
  totalMedium: number | null;
  totalHigh: number | null;
}

export interface ChimeraSca {
  totalLow: number | null;
  totalMedium: number | null;
  totalHigh: number | null;
  totalCritical: number | null;
}

export interface CoverageChartData {
  uuaa: string;
  averageCoverage: number;
  totalApps: number;
  applications: ApplicationCoverageData[];
}

export interface ApplicationCoverageData {
  name: string;
  coverage: number;
  bitbucketUrl: string;
}

export interface ChimeraChartData {
  uuaa: string;
  sastData: ChimeraSastData;
  scaData: ChimeraScaData;
  totalApps: number;
  applications: ApplicationChimeraData[];
}

export interface ChimeraSastData {
  totalLow: number;
  totalMedium: number;
  totalHigh: number;
}

export interface ChimeraScaData {
  totalLow: number;
  totalMedium: number;
  totalHigh: number;
  totalCritical: number;
}

export interface ApplicationChimeraData {
  name: string;
  bitbucketUrl: string;
  chimeraUrl: string;
  chimeraSast: ChimeraSast;
  chimeraSca: ChimeraSca;
}

export interface CountSummaryDTO {
  totalVerticales: number;
  totalFabricas: number; // orgN2fabrica
  totalSn1: number; // serviceN1
  totalSn2: number; // serviceN2
  totalUuaas: number;
}

export interface NucleusCoverageStatsSummary {
  label: string;
  coveragePercentage: number;
  nucleusLevel: 'VERTICAL' | 'FACTORY' | 'UOL2' | 'SN1' | 'SN2' | 'UUAA' | 'APP';
}

export interface NucleusCoverageChartData {
  items: NucleusCoverageStatsSummary[];
  currentLevel: 'VERTICAL' | 'UOL2' | 'SN1' | 'SN2' | 'UUAA' | 'APP';
  selectedVertical?: string;
  selectedUol2?: string;
  selectedSn1?: string;
  selectedSn2?: string;
  selectedUuaa?: string;
}
export interface UuaaSummaryDTO {
  uuaa: string;
  totalApps: number;
  averageCoverage: number;
  totalBugs: number;
  
  // Chimera SAST totals
  totalSastLow: number;
  totalSastMedium: number;
  totalSastHigh: number;
  
  // Chimera SCA totals
  totalScaLow: number;
  totalScaMedium: number;
  totalScaHigh: number;
  totalScaCritical: number;
  
  // RFO Information
  rfoId: number | null;
  rfoEstado: string | null;
  
  // Computed totals (from backend)
  totalSastVulnerabilities: number;
  totalScaVulnerabilities: number;
  totalVulnerabilities: number;
}

export interface ServiceSummaryDTO {
  serviceId: number;
  serviceN1: string;
  serviceN2: string;
  ownerServiceN1: string;
  uuaas: string[];
  totalApps: number;
  averageCoverage: number;
  totalBugs: number;
  
  // Chimera SAST totals
  totalSastLow: number;
  totalSastMedium: number;
  totalSastHigh: number;
  
  // Chimera SCA totals
  totalScaLow: number;
  totalScaMedium: number;
  totalScaHigh: number;
  totalScaCritical: number;
  
  // RFO Information
  rfoId: number | null;
  rfoEstado: string | null;
  
  // Computed totals
  totalSastVulnerabilities: number;
  totalScaVulnerabilities: number;
  totalVulnerabilities: number;
}