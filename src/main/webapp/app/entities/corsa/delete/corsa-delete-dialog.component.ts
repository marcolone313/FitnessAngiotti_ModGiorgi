import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICorsa } from '../corsa.model';
import { CorsaService } from '../service/corsa.service';

@Component({
  standalone: true,
  templateUrl: './corsa-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CorsaDeleteDialogComponent {
  corsa?: ICorsa;

  protected corsaService = inject(CorsaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.corsaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
