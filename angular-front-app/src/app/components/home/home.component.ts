import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="card" style="margin-top: 40px;">
      <h2>Bienvenue dans le Système de Gestion de Conférences</h2>
      <p>Ce système permet de gérer les keynotes et les conférences avec leurs reviews.</p>
      
      <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 30px;">
        <div class="card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
          <h3>📝 Keynotes</h3>
          <p>Gérez les intervenants de vos conférences</p>
          <button class="btn-secondary" routerLink="/keynotes">Voir les keynotes</button>
        </div>
        
        <div class="card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white;">
          <h3>🎯 Conférences</h3>
          <p>Organisez et suivez vos événements</p>
          <button class="btn-secondary" routerLink="/conferences">Voir les conférences</button>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class HomeComponent {}
