import { Directive, ElementRef, Renderer2, OnInit, OnDestroy, inject } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoaderService } from '../service/loader.service';

@Directive({
  selector: '[disableOnLoading]',
  standalone: false
})
export class DisableOnLoadingDirective implements OnInit, OnDestroy {

  private el = inject(ElementRef);
  private renderer = inject(Renderer2);
  private loadingService = inject(LoaderService);

  private subscription!: Subscription;

  ngOnInit(): void {
    this.subscription = this.loadingService.isLoading.subscribe(isLoading => {
      this.renderer.setProperty(this.el.nativeElement, 'disabled', isLoading);
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
