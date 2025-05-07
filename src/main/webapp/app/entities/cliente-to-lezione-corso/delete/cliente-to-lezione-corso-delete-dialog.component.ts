import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IClienteToLezioneCorso } from '../cliente-to-lezione-corso.model';
import { ClienteToLezioneCorsoService } from '../service/cliente-to-lezione-corso.service';

@Component({
  standalone: true,
  templateUrl: './cliente-to-lezione-corso-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClienteToLezioneCorsoDeleteDialogComponent {
  clienteToLezioneCorso?: IClienteToLezioneCorso;

  protected clienteToLezioneCorsoService = inject(ClienteToLezioneCorsoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clienteToLezioneCorsoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
