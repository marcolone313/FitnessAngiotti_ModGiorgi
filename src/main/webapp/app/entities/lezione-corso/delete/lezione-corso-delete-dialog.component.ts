import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILezioneCorso } from '../lezione-corso.model';
import { LezioneCorsoService } from '../service/lezione-corso.service';

@Component({
  standalone: true,
  templateUrl: './lezione-corso-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LezioneCorsoDeleteDialogComponent {
  lezioneCorso?: ILezioneCorso;

  protected lezioneCorsoService = inject(LezioneCorsoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lezioneCorsoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
