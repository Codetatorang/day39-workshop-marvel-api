import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MarvelCharService } from '../service/marvel-char.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Comment } from '../model/Comment';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit, OnDestroy{

  form!:FormGroup
  queryParams$!:Subscription
  charParam!: any
  charName!: string
  charId!:string

  constructor(private formbld:FormBuilder, private activatedRoute:ActivatedRoute, private marvelApiSvc:MarvelCharService, private router:Router){}

  ngOnInit(): void {
      this.form = this.createForm()
      this.queryParams$ = this.activatedRoute.queryParams.subscribe(
        (queryParams)=>{
          this.charParam = queryParams['charParam'].split('|')
          this.charName = this.charParam[0]
          this.charId = this.charParam[1]
        }
      )
  }

  ngOnDestroy(): void {
      this.queryParams$.unsubscribe()
  }

  private createForm(): FormGroup {
    return this.formbld.group({
      comment:this.formbld.control(''),
      })
  }

  backToCharDetails(){
    this.router.navigate(['/details', this.charId])
  }
  
  saveComment(){
    const commentVal = this.form?.value['comment']
    const c = {} as Comment
    c.comment = commentVal
    c.charId = this.charId
    this.marvelApiSvc.saveComment(c)

    //revert back to use obserable due to some bugs on the lasvaluefrom when dealing with posting
    this.marvelApiSvc.saveComment(c).subscribe({
      next: (results)=>{
        console.log('save c', results)
      },

      error:(err: any)=>{
        console.log('error save c', err.status)
      },
      complete:()=>{}
    })
    this.backToCharDetails()
  }
}
