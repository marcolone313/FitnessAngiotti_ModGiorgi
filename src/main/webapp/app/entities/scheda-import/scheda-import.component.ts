import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { SchedaSettimanaleService } from 'app/entities/scheda-settimanale/service/scheda-settimanale.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ISchedaSettimanale } from 'app/entities/scheda-settimanale/scheda-settimanale.model';

@Component({
  selector: 'app-scheda-import',
  templateUrl: './scheda-import.component.html',
})
export class SchedaImportComponent implements OnInit {
  form!: FormGroup;
  clienti: ICliente[] = [];
  schedaJson: ISchedaSettimanale | null = null;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private schedaService: SchedaSettimanaleService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      cliente: [null, Validators.required],
    });

    this.clienteService.query().subscribe(response => {
      this.clienti = response.body ?? [];
    });
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      try {
        this.schedaJson = JSON.parse(reader.result as string);
      } catch (e) {
        alert('File JSON non valido');
        this.schedaJson = null;
      }
    };
    reader.readAsText(file);
  }

  onSubmit(): void {
    if (!this.schedaJson || !this.form.valid) return;

    const scheda: ISchedaSettimanale = {
      ...this.schedaJson,
      cliente: this.form.value.cliente,
    };

    this.schedaService.importScheda(scheda).subscribe({
      next: () => alert('Scheda importata con successo'),
      error: () => alert("Errore durante l'importazione"),
    });
  }
}
