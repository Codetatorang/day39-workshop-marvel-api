import { Component, OnDestroy, OnInit } from '@angular/core';
import { Character } from '../model/Characters';
import { Subscription, async } from 'rxjs';
import { MarvelCharService } from '../service/marvel-char.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Comment } from '../model/Comment';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit, OnDestroy {
  charId = ""
  character!: Character
  comments!: Comment[]
  param$!: Subscription
  constructor(private marvelApiSvc: MarvelCharService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }

  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        this.charId = params['charId']
        const l = await this.marvelApiSvc.getCharacterDetails(this.charId)
        this.character = l.details
        const ll = await this.marvelApiSvc.getCharComments(this.charId)
        this.comments = ll
      }
    )
  }

  addComment() {
    const queryParams: Params =
      { charParam: this.character['name'] + '|' + this.character.id }
    this.router.navigate(['/comment'], { queryParams: queryParams })
  }

}
