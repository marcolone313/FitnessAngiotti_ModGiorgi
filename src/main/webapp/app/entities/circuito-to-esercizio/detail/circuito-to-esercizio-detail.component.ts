import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICircuitoToEsercizio } from '../circuito-to-esercizio.model';

@Component({
  standalone: true,
  selector: 'jhi-circuito-to-esercizio-detail',
  templateUrl: './circuito-to-esercizio-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CircuitoToEsercizioDetailComponent {
  circuitoToEsercizio = input<ICircuitoToEsercizio | null>(null);

  previousState(): void {
    window.history.back();
  }
}
