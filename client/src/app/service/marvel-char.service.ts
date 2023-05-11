import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, lastValueFrom, of, tap } from 'rxjs';
import { Character } from '../model/Characters';
import { Comment } from '../model/Comment';

@Injectable({
  providedIn: 'root'
})
export class MarvelCharService {

  private MARVEL_API_URL: string = "/api/characters"
  private headers = new HttpHeaders()
    .set
    ('Content-Type', 'application/json; charset=utf-8')

  constructor(private httpClient: HttpClient) { }

  getCharacters(charName: string, offset: number, limit: number): Promise<any> {
    const params = new HttpParams()
      .set('charName', charName)
      .set('limit', limit)
      .set('offset', offset)

    return lastValueFrom(this.httpClient.get<Character[]>(this.MARVEL_API_URL, { params: params, headers: this.headers }))
  }

  getCharacterDetails(charId: string): Promise<any> {
    return lastValueFrom(this.httpClient.get<Character>(this.MARVEL_API_URL + '/' + charId, { headers: this.headers }))
  }

  saveComment(c: Comment): Observable<Comment> {
    const body = JSON.stringify(c)
    return this.httpClient
      .post<Comment>(this.MARVEL_API_URL + '/' + c.charId, body, { headers: this.headers }).pipe(tap(_ => console.log('add comment')),
        catchError(this.handleError<Comment>('saveComment')))
  }

  /*
  Handle Http operateion that failed.
  Let the app continue.

  @param operation - name of the operation that failed
  @param result - optional value to return as obserable result
  */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error:any):Observable<T>=>{
      return of(result as T)
    }
  }

  getCharComments(charId:string):Promise<Comment[]> {
    return lastValueFrom(this.httpClient
          .get<Comment[]>(this.MARVEL_API_URL + "/comments/" + charId, {headers:this.headers}))
  }
}
