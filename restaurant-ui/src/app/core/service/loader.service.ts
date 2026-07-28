import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({ providedIn: 'root' })
export class LoaderService {
   apiCount = 0;
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public isLoading = this.loadingSubject.asObservable();

  showLoader(): void {
    this.apiCount++;
    if (this.apiCount === 1) {
      this.loadingSubject.next(true);
    }
  }

  hideLoader(): void {
    if (this.apiCount > 0) {
      this.apiCount--;
      if (this.apiCount === 0) {
        this.loadingSubject.next(false);
      }
    }
  }
}
