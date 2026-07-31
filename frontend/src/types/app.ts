export interface Apps {
    id:           number;
    name:         string;
    uuaa:         string;
    bitbucketUrl: null | string;
    sonarUrl:     null | string;
    sonar10Url:   string;
    chimeraUrl:   string;
    samuelUrl:    string;
    monolith:     boolean;
    coverage:     number | null;
    bugs:         number | null;
    language:     Language | null;
    chimeraSast:  ChimeraS;
    chimeraSca:   ChimeraS;
}

export interface ChimeraS {
    totalLow:       number | null;
    totalMedium:    number | null;
    totalHigh:      number | null;
    totalCritical?: number | null;
}

export enum Language {
    Empty = "*",
    Javascript = "javascript",
    Vue = "vue",
}
