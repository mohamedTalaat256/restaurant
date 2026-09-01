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


export const DEFAULT_APPLICATION_SETTING: ApplicationSetting = {
  id: 0,
  applicationTitle: '',
  storeName: '',
  address: '',
  phone: '',
  icon: '',
  logo: '',
  openingTime: '',
  closingTime: '',
  discountType: 'PERCENTAGE',
  discountPercentage: 0,
  serviceChargeType: 'PERCENTAGE',
  taxPercentage: 0,
  taxNumber: '',
  currency: { id: 0, code: '', symbol: '', name: '', exchangeRateToUSD: 1 },
  currencyId: 0,
  currencySymbol: '',
  language: { languageCode: '', name: '' },
  languageCode: '',
  dateFormat: '',
  timezone: '',
  applicationDirection: 'LTR',
  poweredByText: '',
  footerText: ''
};
