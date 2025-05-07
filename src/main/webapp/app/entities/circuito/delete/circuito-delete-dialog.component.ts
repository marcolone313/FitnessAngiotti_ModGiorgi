import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICircuito } from '../circuito.model';
import { CircuitoService } from '../service/circuito.service';

@Component({
  standalone: true,
  templateUrl: './circuito-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CircuitoDeleteDialogComponent {
  circuito?: ICircuito;

  protected circuitoService = inject(CircuitoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.circuitoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
