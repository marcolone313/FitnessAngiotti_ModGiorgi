import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEsercizio } from '../esercizio.model';
import { EsercizioService } from '../service/esercizio.service';

@Component({
  standalone: true,
  templateUrl: './esercizio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EsercizioDeleteDialogComponent {
  esercizio?: IEsercizio;

  protected esercizioService = inject(EsercizioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.esercizioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
