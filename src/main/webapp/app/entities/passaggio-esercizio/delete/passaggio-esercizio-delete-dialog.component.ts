import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPassaggioEsercizio } from '../passaggio-esercizio.model';
import { PassaggioEsercizioService } from '../service/passaggio-esercizio.service';

@Component({
  standalone: true,
  templateUrl: './passaggio-esercizio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PassaggioEsercizioDeleteDialogComponent {
  passaggioEsercizio?: IPassaggioEsercizio;

  protected passaggioEsercizioService = inject(PassaggioEsercizioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.passaggioEsercizioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
