import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Keynote } from '../../models/models';
import { KeynoteService } from '../../services/keynote.service';

@Component({
  selector: 'app-keynote-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card" style="margin-top: 20px;">
      <h2>Gestion des Keynotes</h2>
      
      <div class="error" *ngIf="errorMessage">
        {{ errorMessage }}
      </div>

      <div class="success" *ngIf="successMessage">
        {{ successMessage }}
      </div>

      <!-- Form -->
      <form (ngSubmit)="saveKeynote()" style="margin-bottom: 30px;">
        <div class="form-group">
          <label>Nom</label>
          <input type="text" [(ngModel)]="currentKeynote.nom" name="nom" required>
        </div>
        
        <div class="form-group">
          <label>Prénom</label>
          <input type="text" [(ngModel)]="currentKeynote.prenom" name="prenom" required>
        </div>
        
        <div class="form-group">
          <label>Email</label>
          <input type="email" [(ngModel)]="currentKeynote.email" name="email" required>
        </div>
        
        <div class="form-group">
          <label>Fonction</label>
          <input type="text" [(ngModel)]="currentKeynote.fonction" name="fonction" required>
        </div>
        
        <button type="submit" class="btn-primary">
          {{ isEditing ? 'Mettre à jour' : 'Créer' }}
        </button>
        <button type="button" class="btn-secondary" (click)="resetForm()" style="margin-left: 10px;">
          Annuler
        </button>
      </form>

      <!-- List -->
      <div *ngIf="loading" class="loading">Chargement...</div>

      <table *ngIf="!loading && keynotes.length > 0">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Email</th>
            <th>Fonction</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let keynote of keynotes">
            <td>{{ keynote.id }}</td>
            <td>{{ keynote.nom }}</td>
            <td>{{ keynote.prenom }}</td>
            <td>{{ keynote.email }}</td>
            <td>{{ keynote.fonction }}</td>
            <td>
              <button class="btn-primary" (click)="editKeynote(keynote)" style="margin-right: 5px;">
                ✏️ Modifier
              </button>
              <button class="btn-danger" (click)="deleteKeynote(keynote.id!)">
                🗑️ Supprimer
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <p *ngIf="!loading && keynotes.length === 0" style="text-align: center; padding: 20px; color: #999;">
        Aucun keynote trouvé
      </p>
    </div>
  `,
  styles: []
})
export class KeynoteListComponent implements OnInit {
  keynotes: Keynote[] = [];
  currentKeynote: Keynote = this.getEmptyKeynote();
  loading = false;
  isEditing = false;
  errorMessage = '';
  successMessage = '';

  constructor(private keynoteService: KeynoteService) {}

  ngOnInit() {
    this.loadKeynotes();
  }

  loadKeynotes() {
    this.loading = true;
    this.keynoteService.getAllKeynotes().subscribe({
      next: (data) => {
        this.keynotes = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des keynotes';
        this.loading = false;
        console.error(error);
      }
    });
  }

  saveKeynote() {
    this.clearMessages();
    
    if (this.isEditing && this.currentKeynote.id) {
      this.keynoteService.updateKeynote(this.currentKeynote.id, this.currentKeynote).subscribe({
        next: () => {
          this.successMessage = 'Keynote modifié avec succès';
          this.loadKeynotes();
          this.resetForm();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la modification';
          console.error(error);
        }
      });
    } else {
      this.keynoteService.createKeynote(this.currentKeynote).subscribe({
        next: () => {
          this.successMessage = 'Keynote créé avec succès';
          this.loadKeynotes();
          this.resetForm();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la création';
          console.error(error);
        }
      });
    }
  }

  editKeynote(keynote: Keynote) {
    this.currentKeynote = { ...keynote };
    this.isEditing = true;
    this.clearMessages();
  }

  deleteKeynote(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce keynote ?')) {
      this.keynoteService.deleteKeynote(id).subscribe({
        next: () => {
          this.successMessage = 'Keynote supprimé avec succès';
          this.loadKeynotes();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la suppression';
          console.error(error);
        }
      });
    }
  }

  resetForm() {
    this.currentKeynote = this.getEmptyKeynote();
    this.isEditing = false;
    this.clearMessages();
  }

  clearMessages() {
    this.errorMessage = '';
    this.successMessage = '';
  }

  getEmptyKeynote(): Keynote {
    return {
      nom: '',
      prenom: '',
      email: '',
      fonction: ''
    };
  }
}
