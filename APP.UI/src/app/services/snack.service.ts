import { EventEmitter, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { WarningSnackComponent } from '../wind/warning-snack/warning-snack.component';
import { ClockService } from './clock.service';

@Injectable({
  providedIn: 'root'
})
export class SnackService {
  durationInSeconds: 5;

  constructor(private _snackBar: MatSnackBar) { }

  open(msg: String) {
    this._snackBar.openFromComponent(WarningSnackComponent, {
      duration: 3000,
      data: msg
    });
  }

  openForAction(msg: String, eventToBecalled?: ClockService) {
    const snack = this._snackBar.openFromComponent(WarningSnackComponent, {
      data: msg
    });
    snack
      .onAction()
      .subscribe(() => {
        if (eventToBecalled)
          eventToBecalled.connectDevice();
        else
          window.location.reload();
      });

    setTimeout(() => {
      snack.dismiss();
    }, 15000);
  }
}
