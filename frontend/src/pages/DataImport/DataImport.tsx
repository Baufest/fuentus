import React, { useState } from 'react';
import Breadcrumb from '../../components/Breadcrumbs/Breadcrumb';
import PageTitle from '../../components/PageTitle';
import BackToHome from '../../components/BackToHome';

interface ImportResult {
  totalProcessed: number;
  successfulImports: number;
  failedImports: number;
  successRate: number;
  errorMessage?: string;
}

const DataImport: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [selectedEntity, setSelectedEntity] = useState<string>('nucleus-services');
  const [selectedFormat, setSelectedFormat] = useState<string>('csv');
  const [isUploading, setIsUploading] = useState<boolean>(false);
  const [importResult, setImportResult] = useState<ImportResult | null>(null);
  const [dragActive, setDragActive] = useState<boolean>(false);
  const [selectedDate, setSelectedDate] = useState<string>(''); // Para Productividad

  const handleFileSelect = (file: File) => {
    setSelectedFile(file);
    setImportResult(null);
    
    // Auto-detectar formato por extensión
    const extension = file.name.split('.').pop()?.toLowerCase();
    if (extension === 'csv' || extension === 'json') {
      setSelectedFormat(extension);
    }
  };

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === "dragenter" || e.type === "dragover") {
      setDragActive(true);
    } else if (e.type === "dragleave") {
      setDragActive(false);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFileSelect(e.dataTransfer.files[0]);
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      handleFileSelect(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) return;

    // Validación para Productividad
    if (selectedEntity === 'productividad' && !selectedDate) {
      setImportResult({
        totalProcessed: 0,
        successfulImports: 0,
        failedImports: 0,
        successRate: 0,
        errorMessage: 'Debes seleccionar una fecha para importar datos de productividad'
      });
      return;
    }

    setIsUploading(true);
    setImportResult(null);

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      let url: string;
      
      // URL específica para Productividad
      if (selectedEntity === 'productividad') {
        formData.append('fecha', selectedDate);
        url = `http://localhost:8080/api/fuentus/productividad/import`;
      } else if (selectedEntity === 'velocidad') {
        // Velocidad no requiere fecha, la extrae del CSV
        url = `http://localhost:8080/api/fuentus/velocidad/import`;
      } else {
        url = `http://localhost:8080/api/fuentus/data-import/${selectedEntity}/${selectedFormat}`;
      }

      const response = await fetch(url, {
        method: 'POST',
        body: formData,
      });

      // Verificar si la respuesta es exitosa
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error del servidor (${response.status}): ${errorText}`);
      }

      const result = await response.json();
      
      // Adaptar respuesta de Productividad y Velocidad al formato esperado
      if (selectedEntity === 'productividad' || selectedEntity === 'velocidad') {
        const messages = result.messages || [];
        const firstMessage = messages[0] || '';
        
        // Parsear el mensaje "Importación exitosa: X registros procesados"
        const match = firstMessage.match(/(\d+)\s+exitosos.*?(\d+)\s+errores|(\d+)\s+registros\s+procesados/);
        let successful = 0;
        let failed = 0;
        
        if (match) {
          if (match[1] && match[2]) {
            // "X exitosos, Y errores"
            successful = parseInt(match[1]);
            failed = parseInt(match[2]);
          } else if (match[3]) {
            // "X registros procesados"
            successful = parseInt(match[3]);
            failed = 0;
          }
        }
        
        const total = successful + failed;
        setImportResult({
          totalProcessed: total,
          successfulImports: successful,
          failedImports: failed,
          successRate: total > 0 ? (successful / total) * 100 : 0,
          errorMessage: result.success ? undefined : messages.join('\n')
        });
      } else {
        setImportResult(result);
      }
    } catch (error) {
      console.error('Error uploading file:', error);
      setImportResult({
        totalProcessed: 0,
        successfulImports: 0,
        failedImports: 0,
        successRate: 0,
        errorMessage: error instanceof Error ? error.message : 'Error de conexión al servidor'
      });
    } finally {
      setIsUploading(false);
    }
  };

  const resetForm = () => {
    setSelectedFile(null);
    setImportResult(null);
    setSelectedEntity('nucleus-services');
    setSelectedFormat('csv');
    setSelectedDate('');
  };

  const getFileIcon = () => {
    if (!selectedFile) return '📄';
    const extension = selectedFile.name.split('.').pop()?.toLowerCase();
    if (extension === 'csv') return '📊';
    if (extension === 'json') return '📋';
    return '📄';
  };

  return (
    <>
      <PageTitle title="Importación de Datos | Fuentus" />
      
      {/* Back to Home Button */}
      <div className="mb-4">
        <BackToHome />
      </div>
      
      <Breadcrumb pageName="Importación de Datos" />

      <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
        <div className="max-w-4xl mx-auto">
          
          {/* Header */}
          <div className="mb-8">
            <h3 className="text-xl font-semibold text-black dark:text-white mb-2">
              📁 Cargar Archivo de Datos
            </h3>
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Selecciona un archivo CSV o JSON para importar datos a la base de datos.
            </p>
          </div>

          {/* Configuración */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            
            {/* Selección de Tabla */}
            <div>
              <label className="mb-3 block text-sm font-medium text-black dark:text-white">
                Tabla de Destino
              </label>
              <select
                value={selectedEntity}
                onChange={(e) => setSelectedEntity(e.target.value)}
                className="w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
              >
                <option value="nucleus-services">📊 Nucleus Services</option>
                <option value="chimera_sca">🔍 Chimera SCA</option>
                <option value="chimera_sast">🛡️ Chimera SAST</option>
                <option value="apps">📱 Apps</option>
                <option value="rfo">📄 RFO</option>
                <option value="productividad">📈 Productividad</option>
                <option value="velocidad">⚡ Velocidad</option>
              </select>
            </div>

            {/* Selección de Formato */}
            <div>
              <label className="mb-3 block text-sm font-medium text-black dark:text-white">
                Formato de Archivo
              </label>
              <select
                value={selectedFormat}
                onChange={(e) => setSelectedFormat(e.target.value)}
                className="w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
              >
                <option value="csv">📊 CSV (Comma-Separated Values)</option>
                <option value="json">📋 JSON (JavaScript Object Notation)</option>
              </select>
            </div>

          </div>

          {/* Selector de Fecha para Productividad */}
          {selectedEntity === 'productividad' && (
            <div className="mb-8">
              <label htmlFor="fecha-productividad" className="mb-3 block text-sm font-medium text-black dark:text-white">
                Fecha de los Datos *
              </label>
              <input
                id="fecha-productividad"
                type="date"
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                className="w-full rounded-lg border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                required
              />
              <p className="mt-2 text-sm text-gray-600 dark:text-gray-400">
                Esta fecha se asignará a todos los registros del archivo CSV.
              </p>
            </div>
          )}

          {/* Zona de Carga de Archivo */}
          <div className="mb-8">
            <label className="mb-3 block text-sm font-medium text-black dark:text-white">
              Seleccionar Archivo
            </label>
            
            <div
              className={`relative border-2 border-dashed rounded-lg p-8 text-center transition-colors ${
                dragActive
                  ? 'border-primary bg-primary/5'
                  : selectedFile
                  ? 'border-success bg-success/5'
                  : 'border-stroke dark:border-strokedark hover:border-primary/50'
              }`}
              onDragEnter={handleDrag}
              onDragLeave={handleDrag}
              onDragOver={handleDrag}
              onDrop={handleDrop}
            >
              
              {/* Icono y texto */}
              <div className="mb-4">
                <div className="text-4xl mb-2">{getFileIcon()}</div>
                {selectedFile ? (
                  <div>
                    <p className="text-lg font-medium text-success dark:text-success">
                      {selectedFile.name}
                    </p>
                    <p className="text-sm text-gray-600 dark:text-gray-400">
                      Tamaño: {(selectedFile.size / 1024).toFixed(1)} KB
                    </p>
                  </div>
                ) : (
                  <div>
                    <p className="text-lg font-medium text-black dark:text-white mb-1">
                      Arrastra y suelta tu archivo aquí
                    </p>
                    <p className="text-sm text-gray-600 dark:text-gray-400">
                      o haz clic para seleccionar
                    </p>
                  </div>
                )}
              </div>

              {/* Input file oculto */}
              <input
                type="file"
                accept=".csv,.json"
                onChange={handleFileChange}
                className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
              />

              {/* Botón para cambiar archivo */}
              {selectedFile && (
                <button
                  type="button"
                  onClick={() => setSelectedFile(null)}
                  className="mt-2 text-sm text-primary hover:text-primary/80 underline"
                >
                  Cambiar archivo
                </button>
              )}
            </div>
          </div>

          {/* Información del formato */}
          {selectedFormat && (
            <div className="mb-8 p-4 bg-gray-50 dark:bg-gray-800 rounded-lg">
              <h4 className="font-medium text-black dark:text-white mb-2">
                📋 Formato requerido para {selectedFormat.toUpperCase()}:
              </h4>
              {selectedFormat === 'csv' ? (
                <div className="text-sm text-gray-600 dark:text-gray-400">
                  <p className="mb-2">Encabezados requeridos:</p>
                  {selectedEntity === 'nucleus-services' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      Id_Fullservice,serviceN1,serviceN2,ownerServiceN1,ownerServiceN2,uuaa,estado,area
                    </code>
                  )}
                  {selectedEntity === 'chimera_sca' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      application,component,version,vulnerability,severity,osvdbId,description,category,remediation
                    </code>
                  )}
                  {selectedEntity === 'chimera_sast' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      application,fileName,method,lines,severity,category,rule,message,status,priority,stockFlow,date,tool
                    </code>
                  )}
                  {selectedEntity === 'apps' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      name,projectId,vertical,folder,bitbucketUrl,uuaa,java,monolith
                    </code>
                  )}
                  {selectedEntity === 'rfo' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      RFO ID,SEVICIO N2,EMAIL,ESTADO RFO,FECHA puesta en producción
                    </code>
                  )}
                  {selectedEntity === 'productividad' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      Servicio N2,Features,FTEs Directos,FTEs Indirectos
                    </code>
                  )}
                  {selectedEntity === 'velocidad' && (
                    <code className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs">
                      service_n2,LT,CT,new_date,analyzing_date,ready_date,in_progress_date,test_date,ready_to_verify_date,to_rework_date,blocked_date,accepted_date,discarded_date,ready_to_deploy_date,deployed_date
                    </code>
                  )}
                </div>
              ) : (
                <div className="text-sm text-gray-600 dark:text-gray-400">
                  <p className="mb-2">Estructura JSON esperada:</p>
                  {selectedEntity === 'nucleus-services' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "Id_Fullservice": 99999,
  "serviceN1": "Servicio 1",
  "serviceN2": "Subservicio",
  "ownerServiceN1": "Owner 1",
  "ownerServiceN2": "Owner 2",
  "uuaa": "1001",
  "estado": "A",
  "area": "TI"
}]`}
                    </pre>
                  )}
                  {selectedEntity === 'chimera_sca' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "application": "MyApp",
  "component": "spring-core",
  "version": "5.3.9",
  "vulnerability": "CVE-2023-1234",
  "severity": "HIGH",
  "osvdbId": "12345",
  "description": "Vulnerability description",
  "category": "Security",
  "remediation": "Update to version 5.3.21"
}]`}
                    </pre>
                  )}
                  {selectedEntity === 'chimera_sast' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "application": "MyApp",
  "fileName": "UserService.java",
  "method": "validateUser",
  "lines": "45-50",
  "severity": "HIGH",
  "category": "Security",
  "rule": "SQL_INJECTION",
  "message": "Potential SQL injection vulnerability",
  "status": "OPEN",
  "priority": "CRITICAL",
  "stockFlow": "INBOUND",
  "date": "2024-01-15 10:30:00",
  "tool": "SonarQube"
}]`}
                    </pre>
                  )}
                  {selectedEntity === 'apps' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "name": "MyApp",
  "projectId": "my-project-123",
  "vertical": "Payments",
  "folder": "apps/my-app",
  "bitbucketUrl": "https://bitbucket.org/team/my-app",
  "uuaa": "1234",
  "java": true,
  "monolith": false
}]`}
                    </pre>
                  )}
                  {selectedEntity === 'rfo' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "rfoId": 7432,
  "sevicioN2": "SOPORTE TESTING - ARG",
  "email": "thomas.docampo@bbva.com",
  "estadoRfo": "EN CURSO",
  "fechaPuestaProduccion": "1 ene 2026"
}]`}
                    </pre>
                  )}
                  {selectedEntity === 'productividad' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "Servicio N2": "ARQUITECTURA ALPHA",
  "Features": 11,
  "FTEs Directos": "10,58",
  "FTEs Indirectos": "4,90"
}]`}
                    </pre>
                  )}
                  {selectedEntity === 'velocidad' && (
                    <pre className="bg-white dark:bg-gray-900 px-2 py-1 rounded text-xs overflow-x-auto">
{`[{
  "service_n2": "ETPB",
  "LT": "203",
  "CT": "173",
  "deployed_date": "15 ene 2025"
}]`}
                    </pre>
                  )}
                </div>
              )}
            </div>
          )}

          {/* Botones de Acción */}
          <div className="flex gap-4 mb-8">
            <button
              onClick={handleUpload}
              disabled={!selectedFile || isUploading}
              className={`flex items-center justify-center rounded-md px-6 py-3 text-center font-medium text-white transition ${
                !selectedFile || isUploading
                  ? 'bg-gray-400 cursor-not-allowed'
                  : 'bg-primary hover:bg-primary/90'
              }`}
            >
              {isUploading ? (
                <>
                  <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Importando...
                </>
              ) : (
                <>
                  <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                  </svg>
                  Importar Datos
                </>
              )}
            </button>

            <button
              onClick={resetForm}
              disabled={isUploading}
              className="flex items-center justify-center rounded-md border border-stroke px-6 py-3 text-center font-medium text-black transition hover:border-primary hover:bg-primary hover:text-white dark:border-strokedark dark:text-white dark:hover:border-primary dark:hover:bg-primary"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              Limpiar
            </button>
          </div>

          {/* Resultado de la Importación */}
          {importResult && (
            <div className={`p-6 rounded-lg border ${
              importResult.errorMessage 
                ? 'bg-red-50 border-red-200 dark:bg-red-900/20 dark:border-red-800'
                : importResult.successRate === 100
                ? 'bg-green-50 border-green-200 dark:bg-green-900/20 dark:border-green-800'
                : 'bg-yellow-50 border-yellow-200 dark:bg-yellow-900/20 dark:border-yellow-800'
            }`}>
              <h4 className={`font-semibold mb-3 ${
                importResult.errorMessage 
                  ? 'text-red-800 dark:text-red-200'
                  : importResult.successRate === 100
                  ? 'text-green-800 dark:text-green-200'
                  : 'text-yellow-800 dark:text-yellow-200'
              }`}>
                {importResult.errorMessage ? '❌ Error en la Importación' : 
                 importResult.successRate === 100 ? '✅ Importación Exitosa' : 
                 '⚠️ Importación Parcial'}
              </h4>
              
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-4">
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Total Procesados</p>
                  <p className="text-xl font-bold text-black dark:text-white">{importResult.totalProcessed}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Exitosos</p>
                  <p className="text-xl font-bold text-green-600">{importResult.successfulImports}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Fallidos</p>
                  <p className="text-xl font-bold text-red-600">{importResult.failedImports}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">Tasa de Éxito</p>
                  <p className="text-xl font-bold text-black dark:text-white">{importResult.successRate.toFixed(1)}%</p>
                </div>
              </div>

              {importResult.errorMessage && (
                <div className="bg-white dark:bg-gray-800 p-3 rounded border">
                  <p className="text-sm text-red-600 dark:text-red-400">
                    <strong>Error:</strong> {importResult.errorMessage}
                  </p>
                </div>
              )}
            </div>
          )}

        </div>
      </div>
    </>
  );
};

export default DataImport;