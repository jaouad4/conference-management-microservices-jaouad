import { Routes } from '@angular/router';
import { KeynoteListComponent } from './components/keynote-list/keynote-list.component';
import { ConferenceListComponent } from './components/conference-list/conference-list.component';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'keynotes', component: KeynoteListComponent },
  { path: 'conferences', component: ConferenceListComponent },
  { path: '**', redirectTo: '' }
];
