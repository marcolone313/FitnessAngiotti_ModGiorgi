import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGym } from '../gym.model';
import { GymService } from '../service/gym.service';

@Component({
  standalone: true,
  templateUrl: './gym-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GymDeleteDialogComponent {
  gym?: IGym;

  protected gymService = inject(GymService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gymService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
