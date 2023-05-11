import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{
  form!: FormGroup
  charName?: string
  
  constructor(private formBld: FormBuilder, private router:Router){}

  ngOnInit(): void {
      this.form = this.createForm()
  }

  search(){
    const charName = this.form?.value['charName']
  }
  createForm():FormGroup{
    return this.formBld.group({
      charName: this.formBld.control(''),
    })
  }
}
