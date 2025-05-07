import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';

@Component({
  standalone: true,
  selector: 'jhi-cliente-to-lezione-corso-detail',
  templateUrl: './cliente-to-lezione-corso-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ClienteToLezioneCorsoDetailComponent {
  clienteToLezioneCorso = input<IClienteToLezioneCorso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
