import { Currency } from './currency.model';
import { Language } from './language.model';

export interface ApplicationSetting {
  id: number;
  applicationTitle: string;
  storeName: string;
  address: string;
  phone: string;
  icon: string;
  logo: string;
  openingTime: string;
  closingTime: string;
  discountType: string;
  discountPercentage: number;
  serviceChargeType: string;
  taxPercentage: number;
  taxNumber: string;
  currency: Currency;
  currencyId: number;
  currencySymbol?: string;
  language: Language;
  languageCode: string;
  dateFormat: string;
  timezone: string;
  applicationDirection: string;
  poweredByText: string;
  footerText: string;
}
