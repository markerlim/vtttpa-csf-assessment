import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Menu } from './models';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private menuSubject = new BehaviorSubject<Menu[]>([]);
  menu$ = this.menuSubject.asObservable();
  private http = inject(HttpClient);

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() {
    this.http.get<Menu[]>('/api/menu').subscribe({
      next: (response) => {
        response.map((entry) => {
          entry.quantity = 0;
        });
        this.menuSubject.next(response);
      },
    });
  }

  addItem(index: number) {
    const currentOrder = this.menuSubject.value;
    currentOrder[index].quantity++;
    console.log(currentOrder);
  }

  removeItem(index: number) {
    const currentOrder = this.menuSubject.value;
    if (currentOrder[index].quantity) {
      currentOrder[index].quantity--;
    } else if (currentOrder[index].quantity == 1) {
      currentOrder[index].quantity = 0;
    }
    console.log(currentOrder);
  }

  // TODO: Task 3.2
  sendOrder(
    username: string,
    password: string,
    items: { id: string; price: number; quantity: number }[]
  ) {
    return this.http.post('/api/food_order',{
      username: username,
      password: password,
      items: items
    })
  }
}
