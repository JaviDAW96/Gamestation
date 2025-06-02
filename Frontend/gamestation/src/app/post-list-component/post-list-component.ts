import { Component, Input } from '@angular/core';
import { Post } from '../interfaces/Post';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-list',
  imports: [CommonModule],
  templateUrl: './post-list-component.html',
  styleUrl: './post-list-component.css'
})
export class PostListComponent {
@Input() items: Post[] = [];
}
