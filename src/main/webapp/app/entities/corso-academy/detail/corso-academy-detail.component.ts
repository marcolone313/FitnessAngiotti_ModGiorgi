import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICorsoAcademy } from '../corso-academy.model';

@Component({
  standalone: true,
  selector: 'jhi-corso-academy-detail',
  templateUrl: './corso-academy-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CorsoAcademyDetailComponent {
  corsoAcademy = input<ICorsoAcademy | null>(null);

  previousState(): void {
    window.history.back();
  }
}
