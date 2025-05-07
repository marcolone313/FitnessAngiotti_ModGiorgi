import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IDieta } from '../dieta.model';

@Component({
  standalone: true,
  selector: 'jhi-dieta-detail',
  templateUrl: './dieta-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DietaDetailComponent {
  dieta = input<IDieta | null>(null);

  previousState(): void {
    window.history.back();
  }
}
