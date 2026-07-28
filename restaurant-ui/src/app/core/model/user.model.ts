 export interface User {
  id: number;
  firstname: string;
  lastname: string;
  about: string;
  waiterKitchenToken: string;
  email: string;
  password: string | null;
  image: string | null;
  lastLogin: string;
  lastLogout: string | null;
  ipAddress: string;
  counter: number;
  status: boolean;
  isAdmin: boolean;
  roleIds: number[];
  isMonitor: boolean;
}
