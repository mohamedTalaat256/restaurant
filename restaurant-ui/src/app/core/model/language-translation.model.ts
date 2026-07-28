import { Language } from './language.model';

export interface LanguageTranslation {
  id: number;
  language: Language;
  languageCode: number;
  key: string;
  value: string;
}
