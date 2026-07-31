export interface VerticalResponseDto {
  id: number;
  name: string;
  orgN2fabrica: OrgN2FabricaDto[] | null;
}

export interface VerticalRequestDto {
  name: string;
  refVertical?: string;
  orgN2fabricaList: string[];
}

export interface VerticalUpdateDto {
  name: string;
  refVertical?: string;
  orgN2fabricaList: string[];
}

export interface OrgN2FabricaDto {
  name: string;
  ownerFactory: string;
}

export interface OrgN2FabricaResponseDto {
  id: number;
  orgN2: string;
  verticales: VerticalSimpleDto[];
}

export interface VerticalSimpleDto {
  id: number;
  name: string;
}
