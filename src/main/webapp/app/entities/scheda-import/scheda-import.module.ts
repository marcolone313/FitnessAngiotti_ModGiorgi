import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { SchedaImportComponent } from './scheda-import.component';

const routes: Routes = [
  {
    path: '',
    component: SchedaImportComponent,
  },
];

@NgModule({
  declarations: [SchedaImportComponent],
  imports: [CommonModule, ReactiveFormsModule, RouterModule.forChild(routes)],
  exports: [SchedaImportComponent],
})
export class SchedaImportModule {}
