import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISchedaSettimanale } from '../scheda-settimanale.model';
import { SchedaSettimanaleService } from '../service/scheda-settimanale.service';

@Component({
  standalone: true,
  templateUrl: './scheda-settimanale-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SchedaSettimanaleDeleteDialogComponent {
  schedaSettimanale?: ISchedaSettimanale;

  protected schedaSettimanaleService = inject(SchedaSettimanaleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.schedaSettimanaleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
