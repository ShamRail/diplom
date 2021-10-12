import {
  AfterViewInit,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {App, AppService} from "../services/AppService";
import {FunctionsUsingCSI, NgTerminal} from "ng-terminal";
import {WebsocketService} from "../services/WebsocketService";

const IGNORE_KEYS = [
  'Tab', 'Escape', 'Insert',
  'F1', 'F2', 'F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'F11', 'F12',
  'PageUp', 'PageDown', 'End', 'Home',
  'ArrowDown', 'ArrowUp'
];

const ENTER = 'Enter';
const NUMPAD_ENTER = 'NumpadEnter';
const BACKSPACE = 'Backspace';
const ARROW_LEFT = 'ArrowLeft';
const ARROW_RIGHT = 'ArrowRight';

@Component({
  selector: 'app-terminal',
  templateUrl: './terminal.component.html',
  styleUrls: ['./terminal.component.css']
})
export class TerminalComponent implements OnInit, OnDestroy, AfterViewInit {

  // @ts-ignore
  @ViewChild('terminal', { static: true }) terminal: NgTerminal;

  isActive: boolean = true;

  @Input() app: App = new App();
  @Output() onStarted: EventEmitter<App> = new EventEmitter<App>();

  typingCommand: string = '';
  lastLine = '';

  constructor(private appService: AppService,
              private websocketService: WebsocketService) { }

  ngAfterViewInit(): void {
    this.terminal.keyEventInput.subscribe(e => {

      const ev = e.domEvent;
      if (ev.altKey || ev.ctrlKey || ev.metaKey || IGNORE_KEYS.includes(ev.code)) {
        return;
      }

      let prefixedLine = false;
      if (ev.code === ENTER || ev.code === NUMPAD_ENTER) {
        if (this.typingCommand !== '') {
          this.typingCommand += '\n';
          this.websocketService.sendMessage(this.typingCommand);
          this.terminal.write('\r\n');
          this.lastLine = '';
        } else {
          this.terminal.write('\r\n$ ');
          this.lastLine = '$ ';
        }
        this.typingCommand = '';
      } else if (ev.code === BACKSPACE) {
        let cursor = this.terminal.underlying.buffer.active.cursorX;
        if (this.lastLine.startsWith('$')) {
          prefixedLine = true;
          cursor -= 2;
        }
        if (cursor > 0) {
          if (this.typingCommand.length == cursor) {
            this.typingCommand = this.typingCommand.slice(0, -1);
            this.lastLine = prefixedLine ? '$ ' + this.typingCommand : this.typingCommand;
            this.terminal.write('\b \b');
          } else {
            let lineCursor = cursor;
            let left = this.typingCommand.substring(0, lineCursor - 1);
            let right = this.typingCommand.substring(lineCursor);
            this.typingCommand = left + right;

            let buffer = '\r' + (prefixedLine ? '$ ' + this.typingCommand + ' ' : this.typingCommand + ' ');
            this.lastLine = buffer;
            buffer += FunctionsUsingCSI.cursorBackward(right.length + 1);
            this.terminal.write(buffer);
          }
        }

        // if (cursor > 2) {
        //   if (this.typingCommand.length == (cursor - 2)) {
        //     this.typingCommand = this.typingCommand.slice(0, -1);
        //     this.terminal.write('\b \b');
        //   } else {
        //     let lineCursor = cursor - 2;
        //     let left = this.typingCommand.substring(0, lineCursor - 1);
        //     let right = this.typingCommand.substring(lineCursor);
        //     this.typingCommand = left + right;
        //     let buffer = '\r$ ' + this.typingCommand + ' ';
        //     buffer += FunctionsUsingCSI.cursorBackward(right.length + 1);
        //     this.terminal.write(buffer);
        //   }
        // }
      } else {
        this.terminal.write(e.key);
        if (ev.code !== ARROW_LEFT && ev.code !== ARROW_RIGHT) {
          this.typingCommand += e.key;
          this.lastLine = prefixedLine ? '$ ' + this.typingCommand : this.typingCommand;
        }
      }
    })
    this.lastLine = '$ ';
    this.terminal.write(this.lastLine);
  }

  ngOnInit(): void {
    console.log(this.app);
    this.initSocket();
  }

  initSocket(): void {
    this.websocketService.initSocket(this.app.wsURI);
    this.websocketService.subscribeOnOpen().subscribe(() => {
      console.log('Сокет успешно подключен!');
      this.websocketService.sendMessage('echo Среда успешно запущена!\n');
    });
    this.websocketService.subscribeOnClose().subscribe(() => {
      this.app.isActive = false;
      this.killApp();
      alert('Соединение прервано. Повторите запуск приложения.');
    });
    this.websocketService.subscribeOnError().subscribe((error) => {
      console.log('Произошла ошибка');
      console.log(error);
      // @ts-ignore
      alert(`Произошла ошибка. ${error.message}`)
    });
    this.websocketService.subscribeOnMessage().subscribe((message) => {
      if (typeof message === "string") {
        this.onMessage(message);
      } else {
        console.log(`Неверный формат данных: ${message}`);
      }
    });
  }

  ngOnDestroy(): void {
    this.killApp();
  }

  @HostListener('window:beforeunload', ['$event']) onClose(event: any) {
    this.killApp();
  }

  onMessage(text: string) {
    const data = text.split('\n').join('\r\n');
    this.lastLine = data[data.length - 1];
    this.terminal.write(data);
  }

  sendMessage(msg: string, isCommand?: boolean) {
    if (!this.app.isActive) {
      alert('Отправка сообщения невозможна. Приложение не запущено.');
      return;
    }
    this.terminal.write('\r\n');
    this.websocketService.sendMessage(msg + "\n");
  }

  killApp() {
    console.log('Убиваю контейнер');
    this.appService.kill(this.app.id).subscribe(
      () => console.log('Контейнер убит'),
      error => console.log('Произошла ошибка', error)
    )
  }

  disableTerminal() {
    this.isActive = false;
  }

  onStart() {
    this.appService.run(this.app.project.id)
      .subscribe(
        (next) => {
           this.app = next;
           this.app.isActive = true;
           this.initSocket();
           this.onStarted.emit(this.app);
          },
        error => {
          console.log(error);
        }
      )
  }
}
