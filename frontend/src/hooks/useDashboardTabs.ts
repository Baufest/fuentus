import { useState } from 'react';
import { getStatsSummaryByFilters, getCoverageAverageByLevel } from '../api/statsSummaryApi';
import { StatsSummaryDTO, NucleusCoverageStatsSummary } from '../types/statsSummary';

export type TabType = 'certificacion' | 'coverage' | 'chimera' | 'nucleus-coverage' | 'sistematica' | 'deuda-tecnica';

export const useDashboardTabs = () => {
  const [activeTab, setActiveTab] = useState<TabType>('certificacion');
  const [coverageData, setCoverageData] = useState<StatsSummaryDTO[]>([]);
  const [coverageLoading, setCoverageLoading] = useState(false);
  const [chimeraData, setChimeraData] = useState<StatsSummaryDTO[]>([]);
  const [chimeraLoading, setChimeraLoading] = useState(false);
  const [nucleusCoverageData, setNucleusCoverageData] = useState<NucleusCoverageStatsSummary[]>([]);
  const [nucleusCoverageLoading, setNucleusCoverageLoading] = useState(false);
  const [selectedVertical, setSelectedVertical] = useState<string | undefined>();
  const [selectedUol2, setSelectedUol2] = useState<string | undefined>();
  const [selectedSn1, setSelectedSn1] = useState<string | undefined>();
  const [selectedSn2, setSelectedSn2] = useState<string | undefined>();
  
  // Estados para drill-down de Deuda Técnica
  const [deudaTecnicaVertical, setDeudaTecnicaVertical] = useState<string | undefined>();
  const [deudaTecnicaFabrica, setDeudaTecnicaFabrica] = useState<string | undefined>();
  const [deudaTecnicaSn1, setDeudaTecnicaSn1] = useState<string | undefined>();

  const loadCoverageData = async (selectedUOL2: string[]) => {
    if (selectedUOL2.length === 0) return;

    try {
      setCoverageLoading(true);
      const response = await getStatsSummaryByFilters(
        undefined, // area
        undefined, // orgN1
        selectedUOL2  // orgN2fabrica
      );
      setCoverageData(response || []);
    } catch (error) {
      console.error('Error loading coverage data:', error);
      setCoverageData([]);
    } finally {
      setCoverageLoading(false);
    }
  };

  const loadChimeraData = async (selectedUOL2: string[]) => {
    if (selectedUOL2.length === 0) return;

    try {
      setChimeraLoading(true);
      const response = await getStatsSummaryByFilters(
        undefined, // area
        undefined, // orgN1
        selectedUOL2  // orgN2fabrica
      );
      setChimeraData(response || []);
    } catch (error) {
      console.error('Error loading chimera data:', error);
      setChimeraData([]);
    } finally {
      setChimeraLoading(false);
    }
  };

  const loadNucleusCoverageData = async (vertical?: string, uol2?: string, sn1?: string, sn2?: string, uuaa?: string) => {
    try {
      setNucleusCoverageLoading(true);
      const response = await getCoverageAverageByLevel(vertical, uol2, sn1, sn2, uuaa);
      console.log('Nucleus coverage data response:', response);
      setNucleusCoverageData(response || []);
    } catch (error) {
      console.error('Error loading nucleus coverage data:', error);
      setNucleusCoverageData([]);
    } finally {
      setNucleusCoverageLoading(false);
    }
  };

  const handleNucleusCoverageItemClick = (item: NucleusCoverageStatsSummary) => {
    if (item.nucleusLevel === 'VERTICAL') {
      setSelectedVertical(item.label);
      setSelectedUol2(undefined);
      setSelectedSn1(undefined);
      setSelectedSn2(undefined);
      loadNucleusCoverageData(item.label, undefined, undefined, undefined, undefined);
    } else if (item.nucleusLevel === 'UOL2') {
      setSelectedUol2(item.label);
      setSelectedSn1(undefined);
      setSelectedSn2(undefined);
      loadNucleusCoverageData(selectedVertical, item.label, undefined, undefined, undefined);
    } else if (item.nucleusLevel === 'SN1') {
      setSelectedSn1(item.label);
      setSelectedSn2(undefined);
      loadNucleusCoverageData(selectedVertical, selectedUol2, item.label, undefined, undefined);
    } else if (item.nucleusLevel === 'SN2') {
      setSelectedSn2(item.label);
      loadNucleusCoverageData(selectedVertical, selectedUol2, selectedSn1, item.label, undefined);
    }
  };

  const handleNucleusCoverageBackClick = () => {
    if (selectedSn2) {
      // Si estamos en SN2, volver a SN1
      setSelectedSn2(undefined);
      loadNucleusCoverageData(selectedVertical, selectedUol2, selectedSn1, undefined, undefined);
    } else if (selectedSn1) {
      // Si estamos en SN2, volver a SN1
      setSelectedSn1(undefined);
      loadNucleusCoverageData(selectedVertical, selectedUol2, undefined, undefined, undefined);
    } else if (selectedUol2) {
      // Si estamos en SN1, volver a UOL2
      setSelectedUol2(undefined);
      loadNucleusCoverageData(selectedVertical, undefined, undefined, undefined, undefined);
    } else if (selectedVertical) {
      // Si estamos en UOL2, volver a VERTICAL
      setSelectedVertical(undefined);
      loadNucleusCoverageData(undefined, undefined, undefined, undefined, undefined);
    }
  };

  const resetNucleusCoverageData = () => {
    setSelectedVertical(undefined);
    setSelectedUol2(undefined);
    setSelectedSn1(undefined);
    setSelectedSn2(undefined);
    setNucleusCoverageData([]);
  };

  // Handlers para drill-down de Deuda Técnica
  const handleDeudaTecnicaDrillDown = (label: string, level: string) => {
    if (level === 'VERTICAL') {
      setDeudaTecnicaVertical(label);
      setDeudaTecnicaFabrica(undefined);
      setDeudaTecnicaSn1(undefined);
    } else if (level === 'FABRICA') {
      setDeudaTecnicaFabrica(label);
      setDeudaTecnicaSn1(undefined);
    } else if (level === 'SN1') {
      setDeudaTecnicaSn1(label);
    }
  };

  const handleDeudaTecnicaBackClick = () => {
    if (deudaTecnicaSn1) {
      setDeudaTecnicaSn1(undefined);
    } else if (deudaTecnicaFabrica) {
      setDeudaTecnicaFabrica(undefined);
    } else if (deudaTecnicaVertical) {
      setDeudaTecnicaVertical(undefined);
    }
  };

  const resetDeudaTecnicaData = () => {
    setDeudaTecnicaVertical(undefined);
    setDeudaTecnicaFabrica(undefined);
    setDeudaTecnicaSn1(undefined);
  };

  return {
    activeTab,
    setActiveTab,
    coverageData,
    coverageLoading,
    chimeraData,
    chimeraLoading,
    nucleusCoverageData,
    nucleusCoverageLoading,
    selectedVertical,
    selectedUol2,
    selectedSn1,
    selectedSn2,
    loadCoverageData,
    loadChimeraData,
    loadNucleusCoverageData,
    handleNucleusCoverageItemClick,
    handleNucleusCoverageBackClick,
    resetNucleusCoverageData,
    // Deuda Técnica
    deudaTecnicaVertical,
    deudaTecnicaFabrica,
    deudaTecnicaSn1,
    handleDeudaTecnicaDrillDown,
    handleDeudaTecnicaBackClick,
    resetDeudaTecnicaData
  };
};
