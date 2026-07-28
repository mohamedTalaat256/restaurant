export interface CashRegister {
  id: number;
  userId: number;
  userName?: string;
  cashCounterId: number;
  cashCounterNumber?: number;
  openingBalance: number;
  closingBalance?: number;
  openingTime: string;
  closingTime?: string;
  openingNote?: string;
  closingNote?: string;
  status: boolean;
}

export interface OpenCashRegisterDto {
  cashCounterId: number;
  openingBalance: number;
  openingNote?: string;
}

export interface CloseCashRegisterDto {
  closingBalance: number;
  closingNote?: string;
}
