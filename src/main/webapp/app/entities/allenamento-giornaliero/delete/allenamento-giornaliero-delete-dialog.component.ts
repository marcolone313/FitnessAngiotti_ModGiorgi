import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAllenamentoGiornaliero } from '../allenamento-giornaliero.model';
import { AllenamentoGiornalieroService } from '../service/allenamento-giornaliero.service';

@Component({
  standalone: true,
  templateUrl: './allenamento-giornaliero-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AllenamentoGiornalieroDeleteDialogComponent {
  allenamentoGiornaliero?: IAllenamentoGiornaliero;

  protected allenamentoGiornalieroService = inject(AllenamentoGiornalieroService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.allenamentoGiornalieroService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
