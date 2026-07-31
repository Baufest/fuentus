export interface Productividad {
  id: number;
  nucleusId: number | null;
  servicioN2: string | null;
  features: number;
  ftesDirectos: number;
  ftesIndirectos: number;
  fecha: string; // formato ISO: "yyyy-MM-dd"
}

export interface ProductividadImportDTO {
  "Servicio N2": string;
  Features: number;
  "FTEs Directos": string;
  "FTEs Indirectos": string;
}
