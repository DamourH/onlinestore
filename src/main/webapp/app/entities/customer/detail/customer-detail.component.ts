import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICustomer } from '../customer.model';

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CustomerDetailComponent {
  customer = input<ICustomer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
