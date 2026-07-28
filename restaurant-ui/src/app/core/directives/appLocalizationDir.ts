import { Directive, ElementRef, Input, Renderer2, OnInit } from '@angular/core';

@Directive({
  selector: '[appLocalizationDir]',
})
export class LocalizationDirDirective implements OnInit {
  @Input() language: string=null!;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit() {
    if (this.language === 'ar') {
     // this.renderer.setAttribute(this.el.nativeElement, 'dir', 'rtl');

      document.body.dir = 'rtl';
    } else {
     // this.renderer.setAttribute(this.el.nativeElement, 'dir', 'ltr');
      document.body.dir = 'ltr';
    }
  }
}
