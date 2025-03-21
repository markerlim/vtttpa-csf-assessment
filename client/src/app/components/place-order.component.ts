import { Component, inject, OnInit } from '@angular/core';
import { Menu } from '../models';
import { RestaurantService } from '../restaurant.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css',
})
export class PlaceOrderComponent implements OnInit {
  // TODO: Task 3
  listoforders: Menu[] = [];
  items: { id: string; price: number; quantity: number }[] = [];
  total: number = 0;

  form!: FormGroup;

  private restaurantService = inject(RestaurantService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  ngOnInit() {
    this.restaurantService.menu$.subscribe({
      next: (response) => {
        response.forEach((entry) => {
          if (entry.quantity > 0) {
            this.listoforders.push(entry);
          }
          this.total += entry.quantity * entry.price;
        });
      },
    });
    this.form = this.createForm();
  }

  onSubmit() {
    this.listoforders.map((entry) => {
      const item = {
        id: entry.id,
        price: entry.price,
        quantity: entry.quantity,
      };
      this.items.push(item);
    });

    this.restaurantService
      .sendOrder(
        this.form.controls['username'].value,
        this.form.controls['password'].value,
        this.items
      )
      .subscribe({
        next: (response) => {
          const object = JSON.parse(JSON.stringify(response));
          localStorage.setItem('order_id',object.order_id);
          localStorage.setItem('payment_id',object.payment_id);
          localStorage.setItem('total', object.total);
          localStorage.setItem('date', object.timestamp);
          this.router.navigateByUrl(`/confirmation`);
        },
        error: (error) => {
          const object = JSON.parse(JSON.stringify(error));
          console.log(object);
          alert(object.error);
        },
      });
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<string>(''),
      password: this.fb.control<string>(''),
    });
  }
}
