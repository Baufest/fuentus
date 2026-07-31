import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Badge from '../../components/Badges/Badge';
import { Tooltip } from '../../components/Helpers/Tooltip';
import { FaJava } from 'react-icons/fa';
import { TbShieldCheck, TbPackage } from "react-icons/tb";
import Button from '../../components/Buttons/Button';
import { Apps, Language } from '../../types/app';
import fuentusapi from '../../api/fuentusapi';
import Loader from '../../common/Loader';

type Server = {
  serverName: string;
  serverUrl?: string;
}

type AppSummaryDTO = Apps & { servers?: Server[] };

const AppDetail = () => {
  const { appId } = useParams<{ appId: string }>();
  const [data, setData] = useState<AppSummaryDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAppDetails = async () => {
      if (!appId) {
        setError('No se proporcionó ID de aplicación');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const response = await fuentusapi.get<AppSummaryDTO>(`apps/details/${appId}`);
        setData(response.data);
        setError(null);
      } catch (err) {
        console.error('Error fetching app details:', err);
        setError('Error al cargar los detalles de la aplicación');
        setData(null);
      } finally {
        setLoading(false);
      }
    };

    fetchAppDetails();
  }, [appId]);

  if (loading) {
    return <Loader size="lg" color="primary" />;
  }

  const renderMessage = (message: string) => (
    <div className="p-8 bg-white dark:bg-boxdark rounded-2xl shadow-md max-w-7xl mx-auto">
      <p className="text-red-500">{message}</p>
      <Button 
        variant='outline' 
        size='sm'
        className="!px-4 !py-2 mt-4"
        onClick={() => window.history.back()}
      >
        Volver
      </Button>
    </div>
  );

  if (error) {
    return renderMessage(error);
  }

  if (!data) {
    return renderMessage('No se encontraron datos para la aplicación solicitada');
  }

  function getCoverage(coverage: number) {
    if (coverage >= 80) {
      return 'success';
    } else if (coverage >= 50) {
      return 'warning';
    } else {
      return 'error';
    }
  }

  function getBugs(bugs: number) {
    if (bugs <= 0) {
      return 'success';
    } else if (bugs < 10) {
      return 'warning';
    } else {
      return 'error';
    }
  }

  return (
    <div className="p-8 bg-white dark:bg-boxdark rounded-2xl shadow-md max-w-7xl mx-auto">
      <h2 className="text-gray-400 dark:text-gray-300 font-semibold text-xl mb-8">FUENTUS</h2>

      <div className="grid grid-cols-2 gap-10">
        {/* Atributos Standard */}
        <div>
          <h3 className="font-bold text-gray-800 dark:text-white text-2xl mb-6">Atributos Standard</h3>
          <ul className="space-y-3 text-lg text-gray-700 dark:text-gray-300">
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">UUAA:</strong> {data?.uuaa}</li>
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">Aplicación:</strong> {data?.name}</li>
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">Tipo:</strong> {data?.language !== Language.Javascript ? 'Backend' : 'Frontend'} <span className="inline-block ml-2"><img src="https://img.icons8.com/color/24/000000/source-code.png" alt="front" className="inline" /></span></li>
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">Tech:</strong> {data?.language} <span className="inline-block ml-2">{data?.language !== Language.Javascript ? <FaJava className='size-6' /> : <img src="https://img.icons8.com/color/24/000000/javascript.png" alt="js" className="inline" />}</span></li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">Repositorio:</strong>
              <a href={data?.bitbucketUrl ?? undefined} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                Bitbucket
              </a>
              <span className="inline-block bg-blue-500 text-white text-xs px-2 py-0.5 rounded">3 feat</span>
              <span className="inline-block bg-green-300 text-xs px-2 py-0.5 rounded dark:bg-green-600">1 RC</span>
            </li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">Pipeline:</strong>
              <a href={"https://jenkins.arg.igrupobbva/"} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                Jenkins
              </a>
              <span className="inline-block bg-red-300 dark:bg-error-500 text-xs px-2 py-0.5 rounded">failed</span>
            </li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">An. Estático:</strong>
              <a href={data?.sonar10Url} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                SonarQube
              </a>
              <Tooltip text="% de cobertura de código">
                <Badge size="sm" color={getCoverage(data?.coverage || 0)} >{data?.coverage || 0}%</Badge>
              </Tooltip>
            </li>
          </ul>
        </div>

        {/* More Standard Attributes */}
        <div>
          <ul className="space-y-3 text-lg text-gray-700 dark:text-gray-300 mt-12">
            {/* Chimera Security Analysis - Agrupado */}
            <li className="space-y-2 text-lg">
              <div className="font-semibold text-gray-800 dark:text-white mb-2">Análisis de Seguridad - Chimera</div>
              <div className="ml-4 space-y-2">
                <div className="flex items-center">
                  <div className="flex items-center gap-2 min-w-[140px]">
                    <TbShieldCheck className='size-5 text-primary dark:text-white'/>
                    <a href={data?.chimeraUrl ?? "#"} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                      Chimera SAST
                    </a>
                  </div>
                  <div className="flex gap-1">
                    <Tooltip text="Total de vulnerabilidades Critical (SAST)" className='invisible'>
                      <Badge size="sm" color={"error"} variant="solid" >
                        <span className="w-12 text-center inline-block">0C</span>
                      </Badge>
                    </Tooltip>
                    <Tooltip text="Total de vulnerabilidades High (SAST)">
                      <Badge size="sm" color={"error"} >
                        <span className="w-12 text-center inline-block">{data?.chimeraSast?.totalHigh || 0}H</span>
                      </Badge>
                    </Tooltip>
                    <Tooltip text="Total de vulnerabilidades Medium (SAST)">
                      <Badge size="sm" color={"warning"} >
                        <span className="w-12 text-center inline-block">{data?.chimeraSast?.totalMedium || 0}M</span>
                      </Badge>
                    </Tooltip>
                    <Tooltip text="Total de vulnerabilidades Low (SAST)">
                      <Badge size="sm" color={"success"} >
                        <span className="w-12 text-center inline-block">{data?.chimeraSast?.totalLow || 0}L</span>
                      </Badge>
                    </Tooltip>
                  </div>
                </div>
                <div className="flex items-center">
                  <div className="flex items-center gap-2 min-w-[140px]">
                    <TbPackage className='size-5 text-primary dark:text-white'/>
                    <a href={data?.chimeraUrl?.replace('sast','sca') ?? "#"} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                      Chimera SCA
                    </a>
                  </div>
                  <div className="flex gap-1">
                    <Tooltip text="Total de vulnerabilidades Critical (SCA)">
                      <Badge size="sm" color={"error"} variant="solid" >
                        <span className="w-12 text-center inline-block">{data?.chimeraSca?.totalCritical || 0}C</span>
                      </Badge>
                    </Tooltip>
                    <Tooltip text="Total de vulnerabilidades High (SCA)">
                      <Badge size="sm" color={"error"} >
                        <span className="w-12 text-center inline-block">{data?.chimeraSca?.totalHigh || 0}H</span>
                      </Badge>
                    </Tooltip>
                    <Tooltip text="Total de vulnerabilidades Medium (SCA)">
                      <Badge size="sm" color={"warning"} >
                        <span className="w-12 text-center inline-block">{data?.chimeraSca?.totalMedium || 0}M</span>
                      </Badge>
                    </Tooltip>
                    <Tooltip text="Total de vulnerabilidades Low (SCA)">
                      <Badge size="sm" color={"success"} >
                        <span className="w-12 text-center inline-block">{data?.chimeraSca?.totalLow || 0}L</span>
                      </Badge>
                    </Tooltip>
                  </div>
                </div>
              </div>
            </li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">Entornos Previos:</strong>
              <a href="https://atenea.aapc.com/job/tallerpf" target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                Atenea
              </a>
              <span className="inline-block bg-black text-white text-xs px-2 py-0.5 rounded">-</span>
            </li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">Evolución:</strong>
              <a href="https://jira.aapc.com/browse/TALLERPF" target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                Jira
              </a>
              <span className="inline-block bg-blue-400 text-white text-xs px-2 py-0.5 rounded">2 feat</span>
            </li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">Operación:</strong>
              <a href="https://helix.aapc.com/job/tallerpf" target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                Helix
              </a>
              <Tooltip text="Número de bugs reportados">
                <Badge size="sm" color={getBugs(data?.bugs || 0)} >{data?.bugs || 0} bug{(data?.bugs || 0) !== 1 ? 's' : ''}</Badge>
              </Tooltip>
            </li>
            <li className="flex items-center gap-2 text-lg">
              <strong className="font-semibold text-gray-800 dark:text-white">Enforcement:</strong>
              <a href={data?.samuelUrl ?? "https://samuel.globaldevtools.bbva.com"} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                Samuel
              </a>
            </li>
          </ul>
        </div>
      </div>

      {/* Atributos Custom y Servidores */}
      <div className="mt-12 grid grid-cols-2 gap-10">
        {/* Atributos Custom */}
        <div>
          <h3 className="font-bold text-gray-800 dark:text-white text-2xl mb-6">Atributos Custom</h3>
          <ul className="space-y-3 text-lg text-gray-700 dark:text-gray-300">
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">🔧 Endpoint expuesto:</strong> tallerPF.bbva.com.ar</li>
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">🔒 Certificado:</strong> sign.tallerPF.bbva.com.ar</li>
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">⚠️ Criticidad:</strong> 5</li>
            <li className="text-lg"><strong className="text-gray-800 dark:text-white">🗑️ Obsoleto:</strong> FALSE</li>
          </ul>
        </div>

        {/* Servidores */}
        <div>
          <h3 className="font-bold text-gray-800 dark:text-white text-2xl mb-6">Servidores</h3>
          {data?.servers && data.servers.length > 0 ? (
            <ul className="space-y-3 text-lg text-gray-700 dark:text-gray-300">
              {data.servers.map((server: Server, index: number) => (
                <li key={index} className="flex items-center gap-2 text-lg">
                  <strong className="font-semibold text-gray-800 dark:text-white">🖥️ Servidor {index + 1}:</strong>
                  <a href={server.serverUrl || `https://${server.serverName}`} target="_blank" rel="noopener noreferrer" className="text-blue-600 dark:text-blue-400 underline hover:text-blue-800 dark:hover:text-blue-300 font-medium">
                    {server.serverName}
                  </a>
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-lg text-gray-500 dark:text-gray-400">No hay servidores configurados</p>
          )}
        </div>
      </div>

      {/* Back Button */}
      <div className="mt-6 text-right">
        <Button 
          variant='outline' 
          size='sm'
          className="!px-4 !py-2"
          onClick={() => window.history.back()}
        >
          Volver
        </Button>
      </div>
    </div>
  );
}


export default AppDetail;
