import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DashboardData, DEFAULT_DASHBOARD_DATA } from './dashboard.model';
import { env } from '../../../../environment/env';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${env.apiUrl}/dashboard`;

  dashboardData = signal<DashboardData>(DEFAULT_DASHBOARD_DATA);
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(private http: HttpClient) {}

  loadDashboardData() {
    this.loading.set(true);
    this.error.set(null);

    this.http.get<any>(`${this.apiUrl}/data`).subscribe({
      next: (response) => {
        if (response.status) {
          console.log('Dashboard data loaded successfully:', response.data);
          this.dashboardData.set(response.data);
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.message || 'Failed to load dashboard data');
        this.loading.set(false);
      }
    });
  }
}
