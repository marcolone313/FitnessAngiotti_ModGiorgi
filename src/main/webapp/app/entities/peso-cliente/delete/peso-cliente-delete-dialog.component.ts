import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPesoCliente } from '../peso-cliente.model';
import { PesoClienteService } from '../service/peso-cliente.service';

@Component({
  standalone: true,
  templateUrl: './peso-cliente-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PesoClienteDeleteDialogComponent {
  pesoCliente?: IPesoCliente;

  protected pesoClienteService = inject(PesoClienteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pesoClienteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
