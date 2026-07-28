export interface FiscalPeriod {
  id: number;
  year: number;
  month: number;
  startDate: string;
  endDate: string;
  locked: boolean;
  lockedAt: string;
  lockedByUsername: string;
}
