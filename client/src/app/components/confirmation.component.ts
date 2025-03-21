import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css',
})
export class ConfirmationComponent {

  order_id!: String;
  payment_id!: String;
  total!: number;
  value! : string;
  date!: String;
  // TODO: Task 5
  ngOnInit() {
    this.order_id = localStorage.getItem('order_id') ?? '';
    this.payment_id = localStorage.getItem('payment_id') ?? '';
    this.date = localStorage.getItem('date') ?? '';
    this.total = parseFloat(localStorage.getItem('total') ?? '');

  }
}
