import { AfterViewInit, Component, inject, input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { ICliente } from '../cliente.model';
import { PlicometriaComponent } from '../../plicometria/list/plicometria.component';
import { PlicometriaService } from 'app/entities/plicometria/service/plicometria.service';
import { IPlicometria } from 'app/entities/plicometria/plicometria.model';
import { PesoClienteService } from 'app/entities/peso-cliente/service/peso-cliente.service';
import { IPesoCliente } from 'app/entities/peso-cliente/peso-cliente.model';

@Component({
  standalone: true,
  selector: 'jhi-cliente-detail',
  templateUrl: './cliente-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, PlicometriaComponent],
})
export class ClienteDetailComponent implements AfterViewInit {
  cliente = input<ICliente | null>(null);

  protected dataUtils = inject(DataUtils);

  /* "tricipite": 134.0,
        "petto": 12.0,
        "ascella": 133.0,
        "sottoscapolare": 1212.0,
        "soprailliaca": 1331.0,
        "addome": 3131.0,
        "coscia": 13.0,
        "mese": 1,*/
  plicometrieKeys = ['id', 'tricipite', 'petto', 'ascella', 'sottoscapolare', 'soprailliaca', 'addome', 'coscia', 'mese'];

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  plicometrieSrv = inject(PlicometriaService);
  plicometrie: IPlicometria[] = [];

  pesoSrv = inject(PesoClienteService);
  pesi: IPesoCliente[] = [];

  ngAfterViewInit(): void {
    const idCliente = this.cliente()!.id;
    // get all plicometrie for this user:
    this.plicometrieSrv.query({ 'clienteId.equals': idCliente }).subscribe({
      next: res => {
        this.plicometrie = res.body || [];
      },
      error: () => {
        this.plicometrie = [];
      },
    });

    this.pesoSrv.query({ 'clienteId.equals': idCliente }).subscribe({
      next: res => {
        this.pesi = res.body || [];
      },
      error: () => {
        this.pesi = [];
      },
    });
  }

  getKeys(obj: IPlicometria, key: string): string {
    // given a key, return the value of the object:

    // @ts-ignore
    return obj[key];
  }
}
