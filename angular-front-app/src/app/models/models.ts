export interface Keynote {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  fonction: string;
}

export interface Conference {
  id?: number;
  titre: string;
  type: 'ACADEMIQUE' | 'COMMERCIALE';
  date: string;
  duree: number;
  nombreInscrits: number;
  score: number;
  keynoteId: number;
  keynote?: Keynote;
  reviews?: Review[];
}

export interface Review {
  id?: number;
  date: string;
  texte: string;
  stars: number;
  conferenceId?: number;
}
