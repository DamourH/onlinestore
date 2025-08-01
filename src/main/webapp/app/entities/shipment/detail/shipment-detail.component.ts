import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IShipment } from '../shipment.model';

@Component({
  selector: 'jhi-shipment-detail',
  templateUrl: './shipment-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ShipmentDetailComponent {
  shipment = input<IShipment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
