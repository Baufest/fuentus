export interface ComboValuesDTO {
  verticals: string[];
  uol2Values: string[];
  sn1Values: string[];
  sn2Values: string[];
}

export interface GoalDTO {
  id: number;
  category: string;
  name: string;
  description?: string;
  numericValue?: number;
  categoricalValue?: string;
  unit?: string;
  displayOrder?: number;
}

export interface GoalCategoryDTO {
  category: string;
  goals: GoalDTO[];
}