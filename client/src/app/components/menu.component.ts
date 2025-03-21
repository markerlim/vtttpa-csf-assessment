import { AfterViewChecked, AfterViewInit, Component, DoCheck, inject, OnInit, SimpleChanges } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Menu } from '../models';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
})
export class MenuComponent implements OnInit, DoCheck{
  // TODO: Task 2
  menuItems!: Menu[];

  total: number = 0;

  quantity: number = 0;

  private restaurantService = inject(RestaurantService);

  ngOnInit() {
    this.restaurantService.getMenuItems();
    this.restaurantService.menu$.subscribe({
      next: (response) => {
        this.menuItems = response;
      },
    });
  }

  ngDoCheck(){
    this.restaurantService.menu$.subscribe({
      next: (response) => {
        this.quantity = 0;
        this.total = 0;
        response.forEach(entry=>{
          if(entry.quantity > 0){
          this.quantity += entry.quantity
          this.total += entry.price * entry.quantity
          }
        })
      },
    });
  }

  addOrder(index: number) {
    this.restaurantService.addItem(index);
  }

  removeOrder(index: number) {
    this.restaurantService.removeItem(index);
  }
}
