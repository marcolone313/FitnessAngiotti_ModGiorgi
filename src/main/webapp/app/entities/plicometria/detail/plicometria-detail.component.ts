import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPlicometria } from '../plicometria.model';

@Component({
  standalone: true,
  selector: 'jhi-plicometria-detail',
  templateUrl: './plicometria-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlicometriaDetailComponent {
  plicometria = input<IPlicometria | null>(null);

  previousState(): void {
    window.history.back();
  }
}
