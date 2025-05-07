import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPesoCliente } from '../peso-cliente.model';

@Component({
  standalone: true,
  selector: 'jhi-peso-cliente-detail',
  templateUrl: './peso-cliente-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PesoClienteDetailComponent {
  pesoCliente = input<IPesoCliente | null>(null);

  previousState(): void {
    window.history.back();
  }
}
