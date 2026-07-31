export interface Velocidad {
  id: number;
  nucleusId: number | null;
  servicioN2: string | null;
  lt: number;
  ct: number;
  date: string; // formato ISO: "yyyy-MM-dd"
}

export interface VelocidadImportDTO {
  service_n2: string;
  LT: string;
  CT: string;
  new_date?: string;
  analyzing_date?: string;
  ready_date?: string;
  in_progress_date?: string;
  test_date?: string;
  ready_to_verify_date?: string;
  to_rework_date?: string;
  blocked_date?: string;
  accepted_date?: string;
  discarded_date?: string;
  ready_to_deploy_date?: string;
  deployed_date?: string;
}
