import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'fitnessAngiottiApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'fitnessAngiottiApp.cliente.home.title' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'peso-cliente',
    data: { pageTitle: 'fitnessAngiottiApp.pesoCliente.home.title' },
    loadChildren: () => import('./peso-cliente/peso-cliente.routes'),
  },
  {
    path: 'plicometria',
    data: { pageTitle: 'fitnessAngiottiApp.plicometria.home.title' },
    loadChildren: () => import('./plicometria/plicometria.routes'),
  },
  {
    path: 'circonferenza',
    data: { pageTitle: 'fitnessAngiottiApp.circonferenza.home.title' },
    loadChildren: () => import('./circonferenza/circonferenza.routes'),
  },
  {
    path: 'esercizio',
    data: { pageTitle: 'fitnessAngiottiApp.esercizio.home.title' },
    loadChildren: () => import('./esercizio/esercizio.routes'),
  },
  {
    path: 'passaggio-esercizio',
    data: { pageTitle: 'fitnessAngiottiApp.passaggioEsercizio.home.title' },
    loadChildren: () => import('./passaggio-esercizio/passaggio-esercizio.routes'),
  },
  {
    path: 'scheda-settimanale',
    data: { pageTitle: 'fitnessAngiottiApp.schedaSettimanale.home.title' },
    loadChildren: () => import('./scheda-settimanale/scheda-settimanale.routes'),
  },
  {
    path: 'allenamento-giornaliero',
    data: { pageTitle: 'fitnessAngiottiApp.allenamentoGiornaliero.home.title' },
    loadChildren: () => import('./allenamento-giornaliero/allenamento-giornaliero.routes'),
  },
  {
    path: 'circuito',
    data: { pageTitle: 'fitnessAngiottiApp.circuito.home.title' },
    loadChildren: () => import('./circuito/circuito.routes'),
  },
  {
    path: 'corsa',
    data: { pageTitle: 'fitnessAngiottiApp.corsa.home.title' },
    loadChildren: () => import('./corsa/corsa.routes'),
  },
  {
    path: 'gym',
    data: { pageTitle: 'fitnessAngiottiApp.gym.home.title' },
    loadChildren: () => import('./gym/gym.routes'),
  },
  {
    path: 'dieta',
    data: { pageTitle: 'fitnessAngiottiApp.dieta.home.title' },
    loadChildren: () => import('./dieta/dieta.routes'),
  },
  {
    path: 'report-settimanale',
    data: { pageTitle: 'fitnessAngiottiApp.reportSettimanale.home.title' },
    loadChildren: () => import('./report-settimanale/report-settimanale.routes'),
  },
  {
    path: 'corso-academy',
    data: { pageTitle: 'fitnessAngiottiApp.corsoAcademy.home.title' },
    loadChildren: () => import('./corso-academy/corso-academy.routes'),
  },
  {
    path: 'lezione-corso',
    data: { pageTitle: 'fitnessAngiottiApp.lezioneCorso.home.title' },
    loadChildren: () => import('./lezione-corso/lezione-corso.routes'),
  },
  {
    path: 'cliente-to-lezione-corso',
    data: { pageTitle: 'fitnessAngiottiApp.clienteToLezioneCorso.home.title' },
    loadChildren: () => import('./cliente-to-lezione-corso/cliente-to-lezione-corso.routes'),
  },
  {
    path: 'circuito-to-esercizio',
    data: { pageTitle: 'fitnessAngiottiApp.circuitoToEsercizio.home.title' },
    loadChildren: () => import('./circuito-to-esercizio/circuito-to-esercizio.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
