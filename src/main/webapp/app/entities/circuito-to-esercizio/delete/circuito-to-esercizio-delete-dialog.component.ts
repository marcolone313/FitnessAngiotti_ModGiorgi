import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICircuitoToEsercizio } from '../circuito-to-esercizio.model';
import { CircuitoToEsercizioService } from '../service/circuito-to-esercizio.service';

@Component({
  standalone: true,
  templateUrl: './circuito-to-esercizio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CircuitoToEsercizioDeleteDialogComponent {
  circuitoToEsercizio?: ICircuitoToEsercizio;

  protected circuitoToEsercizioService = inject(CircuitoToEsercizioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.circuitoToEsercizioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
