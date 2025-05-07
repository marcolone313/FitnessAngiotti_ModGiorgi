import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICirconferenza } from '../circonferenza.model';

@Component({
  standalone: true,
  selector: 'jhi-circonferenza-detail',
  templateUrl: './circonferenza-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CirconferenzaDetailComponent {
  circonferenza = input<ICirconferenza | null>(null);

  previousState(): void {
    window.history.back();
  }
}
