import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { InputTextModule } from 'primeng/inputtext';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService } from 'primeng/api';
import { Toast } from 'primeng/toast';
import { DriverService } from '../driver.service';
import { Driver, DriverStatus, DRIVER_STATUS_SEVERITY } from '../../../../core/model/driver.model';
import { TranslateService } from '../../../../core/service/translate.service';

@Component({
  selector: 'app-drivers',
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    ToolbarModule,
    InputTextModule,
    TagModule,
    InputIconModule,
    IconFieldModule,
    SelectModule,
    FormsModule,
    TooltipModule,
    Toast,
  ],
  templateUrl: './drivers.html',
  styleUrls: ['./drivers.scss'],
  providers: [MessageService],
})
export class Drivers implements OnInit {
  @ViewChild('dt') dt!: Table;

  readonly driverService = inject(DriverService);
  readonly translate = inject(TranslateService);
  private router = inject(Router);

  readonly statusOptions: { label: string; value: DriverStatus }[] = [
    { label: 'ONLINE', value: 'ONLINE' },
    { label: 'OFFLINE', value: 'OFFLINE' },
    { label: 'BUSY', value: 'BUSY' },
    { label: 'BREAK', value: 'BREAK' },
  ];

  ngOnInit() {
    this.driverService.loadDrivers();
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }

  getStatusSeverity(status: DriverStatus) {
    return DRIVER_STATUS_SEVERITY[status] ?? 'secondary';
  }

  onChangeStatus(driver: Driver, newStatus: DriverStatus) {
    this.driverService.changeStatus(driver.id, { status: newStatus });
  }

  goToDetail(driver: Driver) {
    this.router.navigate(['/admin/drivers', driver.id]);
  }
}
