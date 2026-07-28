import { Component, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'toggle-button',
  template: `
  <input
      type="checkbox"
      [id]="id"
      [checked]="checked"
      (change)="onChange($event)"
    >

    <label
      class="toggle-button-switch"
      [for]="id">
    </label>

    <div class="toggle-button-text">
      <div class="toggle-button-text-on">ON</div>
      <div class="toggle-button-text-off">OFF</div>
    </div>

  `,
  styles: [`
    :host {
      display: block;
      position: relative;
      width: 50px;
      height: 25px;
    }

    input[type="checkbox"] {
      display: none;
    }

    /* 토글 버튼 내부의 원. 감춘 체크박스와 연동한다. */
    .toggle-button-switch {
      position: absolute;
      top: 2px;
      left: 2px;
      width: 23px;
      height: 23px;
      background-color: #fff;
      border-radius: 100%;
      cursor: pointer;
      z-index: 100;
      transition: left 0.3s;
    }

    /* 토글 버튼의 바탕 */
    .toggle-button-text {
      overflow: hidden;
      background-color: #fc3164;
      border-radius: 25px;
      box-shadow: 2px 2px 5px 0 rgba(50, 50, 50, 0.75);
      transition: background-color 0.3s;
    }

    /* 토글 버튼의 텍스트 */
    .toggle-button-text-on,
    .toggle-button-text-off {
      float: left;
      width: 50%;
      height: 100%;
      line-height: 25px;
      font-family: Lato, sans-serif;
      font-weight: bold;
      color: #fff;
      text-align: center;
      font-size: 9px;
    }

    /* 체크박스가 체크 상태이면 토글 버튼 내부의 원을 오른쪽으로 52px 이동 */
    input[type="checkbox"]:checked ~ .toggle-button-switch {
      left: 26px;
    }

    /* 체크박스가 체크 상태이면 토글 버튼의 배경색 변경 */
    input[type="checkbox"]:checked ~ .toggle-button-text {
      background-color: #3dbf87;
    }
  `]
})
export class ToggleButtonComponent {
   private static nextId = 0;

  id = `toggle-button-${ToggleButtonComponent.nextId++}`;
  @Output() changed = new EventEmitter<boolean>();
  @Input() checked = false;


  onChange(event: any) {
    const checked = (event.target as HTMLInputElement).checked;
    this.changed.emit(checked);
  }
}
