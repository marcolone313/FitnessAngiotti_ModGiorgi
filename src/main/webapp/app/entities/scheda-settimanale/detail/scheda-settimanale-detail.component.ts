import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISchedaSettimanale } from '../scheda-settimanale.model';

@Component({
  standalone: true,
  selector: 'jhi-scheda-settimanale-detail',
  templateUrl: './scheda-settimanale-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SchedaSettimanaleDetailComponent {
  schedaSettimanale = input<ISchedaSettimanale | null>(null);

  previousState(): void {
    window.history.back();
  }
}
