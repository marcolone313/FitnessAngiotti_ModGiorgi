import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICirconferenza } from '../circonferenza.model';
import { CirconferenzaService } from '../service/circonferenza.service';

@Component({
  standalone: true,
  templateUrl: './circonferenza-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CirconferenzaDeleteDialogComponent {
  circonferenza?: ICirconferenza;

  protected circonferenzaService = inject(CirconferenzaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.circonferenzaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
