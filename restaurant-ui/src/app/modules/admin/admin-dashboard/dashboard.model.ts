import { ItemFood } from "../../../core/model/item-food.model";

export interface DashboardData {
  totalOrders: number;
  totalOrdersLast24Hours: number;
  totalCustomers: number;
  totalProducts: number;
  totalRevenue: number;
  topSellingItems: ItemFood[];
}

export const DEFAULT_DASHBOARD_DATA: DashboardData = {
  totalOrders: 0,
  totalOrdersLast24Hours: 0,
  totalCustomers: 0,
  totalProducts: 0,
  totalRevenue: 0,
  topSellingItems: [],
};
