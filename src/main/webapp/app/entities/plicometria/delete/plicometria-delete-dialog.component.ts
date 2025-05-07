import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlicometria } from '../plicometria.model';
import { PlicometriaService } from '../service/plicometria.service';

@Component({
  standalone: true,
  templateUrl: './plicometria-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlicometriaDeleteDialogComponent {
  plicometria?: IPlicometria;

  protected plicometriaService = inject(PlicometriaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plicometriaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
