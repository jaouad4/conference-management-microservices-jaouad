import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conference, Review } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ConferenceService {
  private apiUrl = '/api/conferences';

  constructor(private http: HttpClient) {}

  getAllConferences(): Observable<Conference[]> {
    return this.http.get<Conference[]>(this.apiUrl);
  }

  getConferenceById(id: number): Observable<Conference> {
    return this.http.get<Conference>(`${this.apiUrl}/${id}`);
  }

  createConference(conference: Conference): Observable<Conference> {
    return this.http.post<Conference>(this.apiUrl, conference);
  }

  updateConference(id: number, conference: Conference): Observable<Conference> {
    return this.http.put<Conference>(`${this.apiUrl}/${id}`, conference);
  }

  deleteConference(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getConferencesByKeynote(keynoteId: number): Observable<Conference[]> {
    return this.http.get<Conference[]>(`${this.apiUrl}/keynote/${keynoteId}`);
  }

  // Reviews
  getReviews(conferenceId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/${conferenceId}/reviews`);
  }

  createReview(conferenceId: number, review: Review): Observable<Review> {
    return this.http.post<Review>(`${this.apiUrl}/${conferenceId}/reviews`, review);
  }

  deleteReview(conferenceId: number, reviewId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${conferenceId}/reviews/${reviewId}`);
  }
}
