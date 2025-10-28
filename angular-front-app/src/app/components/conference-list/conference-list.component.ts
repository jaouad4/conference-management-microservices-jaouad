import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Conference, Keynote, Review } from '../../models/models';
import { ConferenceService } from '../../services/conference.service';
import { KeynoteService } from '../../services/keynote.service';

@Component({
  selector: 'app-conference-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card" style="margin-top: 20px;">
      <h2>Gestion des Conférences</h2>
      
      <div class="error" *ngIf="errorMessage">{{ errorMessage }}</div>
      <div class="success" *ngIf="successMessage">{{ successMessage }}</div>

      <!-- Form -->
      <form (ngSubmit)="saveConference()" style="margin-bottom: 30px;">
        <div class="form-group">
          <label>Titre</label>
          <input type="text" [(ngModel)]="currentConference.titre" name="titre" required>
        </div>
        
        <div class="form-group">
          <label>Type</label>
          <select [(ngModel)]="currentConference.type" name="type" required>
            <option value="ACADEMIQUE">Académique</option>
            <option value="COMMERCIALE">Commerciale</option>
          </select>
        </div>
        
        <div class="form-group">
          <label>Date</label>
          <input type="date" [(ngModel)]="currentConference.date" name="date" required>
        </div>
        
        <div class="form-group">
          <label>Durée (heures)</label>
          <input type="number" [(ngModel)]="currentConference.duree" name="duree" required>
        </div>
        
        <div class="form-group">
          <label>Nombre d'inscrits</label>
          <input type="number" [(ngModel)]="currentConference.nombreInscrits" name="nombreInscrits" required>
        </div>
        
        <div class="form-group">
          <label>Score</label>
          <input type="number" step="0.1" [(ngModel)]="currentConference.score" name="score" required>
        </div>
        
        <div class="form-group">
          <label>Keynote</label>
          <select [(ngModel)]="currentConference.keynoteId" name="keynoteId" required>
            <option [value]="null">Sélectionner un keynote</option>
            <option *ngFor="let k of keynotes" [value]="k.id">
              {{ k.prenom }} {{ k.nom }} ({{ k.fonction }})
            </option>
          </select>
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

      <div *ngIf="!loading && conferences.length > 0">
        <div class="card" *ngFor="let conf of conferences" style="margin-bottom: 20px;">
          <h3>{{ conf.titre }}</h3>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin: 15px 0;">
            <div>
              <strong>Type:</strong> 
              <span [class]="'badge badge-' + conf.type.toLowerCase()">
                {{ conf.type }}
              </span>
            </div>
            <div><strong>Date:</strong> {{ conf.date }}</div>
            <div><strong>Durée:</strong> {{ conf.duree }}h</div>
            <div><strong>Inscrits:</strong> {{ conf.nombreInscrits }}</div>
            <div><strong>Score:</strong> {{ conf.score }}</div>
            <div>
              <strong>Keynote:</strong> 
              {{ conf.keynote ? conf.keynote.prenom + ' ' + conf.keynote.nom : 'N/A' }}
            </div>
          </div>
          
          <div style="margin-top: 15px;">
            <button class="btn-primary" (click)="editConference(conf)" style="margin-right: 5px;">
              ✏️ Modifier
            </button>
            <button class="btn-danger" (click)="deleteConference(conf.id!)" style="margin-right: 5px;">
              🗑️ Supprimer
            </button>
            <button class="btn-success" (click)="toggleReviews(conf.id!)">
              {{ showReviews[conf.id!] ? '📖 Masquer' : '📝 Voir' }} Reviews
            </button>
          </div>

          <!-- Reviews Section -->
          <div *ngIf="showReviews[conf.id!]" style="margin-top: 20px; padding-top: 20px; border-top: 2px solid #eee;">
            <h4>Reviews</h4>
            
            <form (ngSubmit)="addReview(conf.id!)" style="margin-bottom: 20px;">
              <div class="form-group">
                <label>Texte</label>
                <textarea [(ngModel)]="newReview.texte" name="reviewTexte" required></textarea>
              </div>
              <div class="form-group">
                <label>Note (1-5)</label>
                <input type="number" min="1" max="5" [(ngModel)]="newReview.stars" name="reviewStars" required>
              </div>
              <button type="submit" class="btn-success">Ajouter Review</button>
            </form>

            <div *ngIf="conf.reviews && conf.reviews.length > 0">
              <div *ngFor="let review of conf.reviews" class="card" style="margin-bottom: 10px;">
                <div><strong>Date:</strong> {{ review.date }}</div>
                <div><strong>Note:</strong> <span class="stars">{{ getStars(review.stars) }}</span></div>
                <p style="margin-top: 10px;">{{ review.texte }}</p>
              </div>
            </div>
            <p *ngIf="!conf.reviews || conf.reviews.length === 0" style="color: #999;">
              Aucune review pour cette conférence
            </p>
          </div>
        </div>
      </div>

      <p *ngIf="!loading && conferences.length === 0" style="text-align: center; padding: 20px; color: #999;">
        Aucune conférence trouvée
      </p>
    </div>
  `,
  styles: []
})
export class ConferenceListComponent implements OnInit {
  conferences: Conference[] = [];
  keynotes: Keynote[] = [];
  currentConference: Conference = this.getEmptyConference();
  newReview: Review = this.getEmptyReview();
  loading = false;
  isEditing = false;
  errorMessage = '';
  successMessage = '';
  showReviews: { [key: number]: boolean } = {};

  constructor(
    private conferenceService: ConferenceService,
    private keynoteService: KeynoteService
  ) {}

  ngOnInit() {
    this.loadKeynotes();
    this.loadConferences();
  }

  loadKeynotes() {
    this.keynoteService.getAllKeynotes().subscribe({
      next: (data) => this.keynotes = data,
      error: (error) => console.error(error)
    });
  }

  loadConferences() {
    this.loading = true;
    this.conferenceService.getAllConferences().subscribe({
      next: (data) => {
        this.conferences = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des conférences';
        this.loading = false;
        console.error(error);
      }
    });
  }

  saveConference() {
    this.clearMessages();
    
    if (this.isEditing && this.currentConference.id) {
      this.conferenceService.updateConference(this.currentConference.id, this.currentConference).subscribe({
        next: () => {
          this.successMessage = 'Conférence modifiée avec succès';
          this.loadConferences();
          this.resetForm();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la modification';
          console.error(error);
        }
      });
    } else {
      this.conferenceService.createConference(this.currentConference).subscribe({
        next: () => {
          this.successMessage = 'Conférence créée avec succès';
          this.loadConferences();
          this.resetForm();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la création';
          console.error(error);
        }
      });
    }
  }

  editConference(conference: Conference) {
    this.currentConference = { ...conference };
    this.isEditing = true;
    this.clearMessages();
    window.scrollTo(0, 0);
  }

  deleteConference(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette conférence ?')) {
      this.conferenceService.deleteConference(id).subscribe({
        next: () => {
          this.successMessage = 'Conférence supprimée avec succès';
          this.loadConferences();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la suppression';
          console.error(error);
        }
      });
    }
  }

  toggleReviews(conferenceId: number) {
    this.showReviews[conferenceId] = !this.showReviews[conferenceId];
    
    if (this.showReviews[conferenceId]) {
      this.conferenceService.getReviews(conferenceId).subscribe({
        next: (reviews) => {
          const conf = this.conferences.find(c => c.id === conferenceId);
          if (conf) {
            conf.reviews = reviews;
          }
        },
        error: (error) => console.error(error)
      });
    }
  }

  addReview(conferenceId: number) {
    this.newReview.date = new Date().toISOString().split('T')[0];
    
    this.conferenceService.createReview(conferenceId, this.newReview).subscribe({
      next: () => {
        this.successMessage = 'Review ajoutée avec succès';
        this.newReview = this.getEmptyReview();
        this.toggleReviews(conferenceId);
        setTimeout(() => this.toggleReviews(conferenceId), 100);
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de l\'ajout de la review';
        console.error(error);
      }
    });
  }

  getStars(count: number): string {
    return '⭐'.repeat(count);
  }

  resetForm() {
    this.currentConference = this.getEmptyConference();
    this.isEditing = false;
    this.clearMessages();
  }

  clearMessages() {
    this.errorMessage = '';
    this.successMessage = '';
  }

  getEmptyConference(): Conference {
    return {
      titre: '',
      type: 'ACADEMIQUE',
      date: '',
      duree: 0,
      nombreInscrits: 0,
      score: 0,
      keynoteId: 0
    };
  }

  getEmptyReview(): Review {
    return {
      date: '',
      texte: '',
      stars: 5
    };
  }
}
