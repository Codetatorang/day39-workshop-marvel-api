import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription, async } from 'rxjs';
import { Character } from '../model/Characters';
import { MarvelCharService } from '../service/marvel-char.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  charName = ""
  params$!: Subscription
  characters!: Character[]
  currentIndex!: number
  pageNumber!: number
  disablePrevious: boolean = false


  constructor(private activatedRoute: ActivatedRoute, private router: Router, private marvelApiSvc: MarvelCharService) { }


  ngOnInit(): void {
    this.params$ = this.activatedRoute.params.subscribe(
      async (params) => {
        this.charName = params['charName']
        this.pageNumber = 1
        this.disablePrevious = true
        const l = await this.marvelApiSvc
          .getCharacters(this.charName, 0, 5)
        this.currentIndex = 1
        if (l === undefined || l.length == 0) {
          this.router.navigate(['/'])
        }else
          this.characters = l
      }
    )
  }

  ngOnDestroy(): void {

  }

  async previous() {
    if (this.currentIndex > 0) {
      this.currentIndex = this.currentIndex - 5
      this.pageNumber--
      const l = await this.marvelApiSvc
        .getCharacters(this.charName, this.currentIndex, 5)
      this.characters = l
      if(this.pageNumber ==1)
        this.disablePrevious = true
    }
  }

  async next(){
    this.currentIndex = this.currentIndex + 5
    const l = await this.marvelApiSvc
        .getCharacters(this.charName, this.currentIndex, 5)
    this.characters = l
    this.pageNumber++
    this.disablePrevious = false
  }

}
