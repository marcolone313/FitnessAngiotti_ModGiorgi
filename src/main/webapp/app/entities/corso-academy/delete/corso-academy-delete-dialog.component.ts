import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICorsoAcademy } from '../corso-academy.model';
import { CorsoAcademyService } from '../service/corso-academy.service';

@Component({
  standalone: true,
  templateUrl: './corso-academy-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CorsoAcademyDeleteDialogComponent {
  corsoAcademy?: ICorsoAcademy;

  protected corsoAcademyService = inject(CorsoAcademyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.corsoAcademyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
