import { MatIconModule } from "@angular/material/icon";
import { FormInput } from "./form-input/form-input";
import { MatButtonModule } from "@angular/material/button";
import { MatInputModule } from "@angular/material/input";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatCardModule } from "@angular/material/card";
import { ReactiveFormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";

export const SHARED_MODULES = [
   CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    FormInput  ];
