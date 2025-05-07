import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IReportSettimanale } from '../report-settimanale.model';
import { ReportSettimanaleService } from '../service/report-settimanale.service';

@Component({
  standalone: true,
  templateUrl: './report-settimanale-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ReportSettimanaleDeleteDialogComponent {
  reportSettimanale?: IReportSettimanale;

  protected reportSettimanaleService = inject(ReportSettimanaleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reportSettimanaleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
