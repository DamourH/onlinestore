import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductOrder } from '../product-order.model';

@Component({
  selector: 'jhi-product-order-detail',
  templateUrl: './product-order-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ProductOrderDetailComponent {
  productOrder = input<IProductOrder | null>(null);

  previousState(): void {
    window.history.back();
  }
}
