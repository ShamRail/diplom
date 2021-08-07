import {Component, EventEmitter, HostListener, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {App, AppService} from "../services/AppService";

const BUFFER_LENGTH: number = 63;

@Component({
  selector: 'app-terminal',
  templateUrl: './terminal.component.html',
  styleUrls: ['./terminal.component.css']
})
export class TerminalComponent implements OnInit, OnDestroy {

  lines: string[][] = [];
  linesCount: number = 35;
  activeLine: number = 0;

  isActive: boolean = true;
  socket: WebSocket | null = null;

  @Input() app: App = new App();
  @Output() onStarted: EventEmitter<App> = new EventEmitter<App>();

  textBuffer: string[] = [];
  lastCommand: string = '';

  constructor(private appService: AppService) {
    this.initTerminal();
  }

  ngOnInit(): void {
    this.initSocket();
  }

  initTerminal() {
    this.isActive = true;
    this.lines = [];
    this.linesCount = 35;
    for (let n = 1; n <= this.linesCount; n++) {
      this.lines.push([]);
    }
  }

  initSocket(): void {
    this.socket = new WebSocket(
      `ws://localhost:2375/containers/${this.app.containerID.substring(0, 12)}/attach/ws?stdout=true&stderr=true&stream=true&logs=true`
    );
    this.socket.onopen = (ev) => {
      console.log('Сокет успешно подключен!');
      this.socket?.send('\n');
    }
    this.socket.onclose = (ev) => {
      this.app.isActive = false;
      this.killApp();
      alert('Соединение прервано. Повторите запуск приложения.');
    }
    this.socket.onerror = (ev) => {
      console.log('Произошла ошибка');
      console.log(ev);
    }
    this.socket.onmessage = async (msg) => {
      let text: string = await msg.data.text();
      this.onMessage(text);
    }
  }

  ngOnDestroy(): void {
    this.killApp();
  }

  @HostListener('document:keydown', ['$event']) onKeydownHandler(event: KeyboardEvent) {
    if (!this.app.isActive || !this.isActive) {
      return;
    }
    if (event.key.length == 1 || event.key == 'Enter' || event.key == 'Backspace') {
      if (event.key == 'Enter') {
        this.sendMessage(this.textBuffer.join(''));
        this.activeLine++;
        return;
      }
      if (event.key == 'Backspace') {
        if (this.textBuffer.length > 0) {
          this.lines[this.activeLine].pop();
          this.textBuffer.pop();
        }
        return;
      }
      if (this.textBuffer.length < BUFFER_LENGTH) {
        this.lines[this.activeLine].push(event.key);
        this.textBuffer.push(event.key);
      }
    }
  }

  @HostListener('window:beforeunload', ['$event']) onClose(event: any) {
    this.killApp();
  }

  onMessage(text: string) {
    console.log("Получено сообщение", text.split(''));
    let lines: string[] = text.split("\n");
    for (let i = 0; i < lines.length - 1; i++) {
      let trimmedOutput = text.trim();
      if (this.lastCommand.trim() != trimmedOutput) {
        if (trimmedOutput.length <= BUFFER_LENGTH) {
          this.expandTerminal();
          this.lines[this.activeLine++].push(lines[i]);
          continue;
        }
        this.divideLine(trimmedOutput);
      }
      this.expandTerminal();
    }
    if (lines[lines.length - 1].length <= BUFFER_LENGTH) {
      this.lines[this.activeLine].push(lines[lines.length - 1]);
    } else {
      this.divideLine(lines[lines.length - 1]);
    }
  }

  divideLine(trimmedOutput: string) {
    let hasPart: boolean = true;
    let startIndex = 0;
    let endIndex = BUFFER_LENGTH + 1;
    do {
      this.expandTerminal();
      let subString = trimmedOutput.substring(startIndex, endIndex);
      this.lines[this.activeLine++].push(subString);
      startIndex += (BUFFER_LENGTH + 1);
      endIndex = Math.min(endIndex + BUFFER_LENGTH, trimmedOutput.length);
      hasPart = startIndex < trimmedOutput.length;
    } while (hasPart);
  }

  expandTerminal() {
    if (this.activeLine == this.lines.length) {
      this.linesCount += 100;
      for (let i = 0; i < 100; i++) {
        this.lines.push([]);
      }
    }
  }

  sendMessage(msg: string, isCommand?: boolean) {
    if (!this.app.isActive) {
      alert('Отправка сообщения невозможна. Приложение не запущено.');
      return;
    }
    if (msg.toLowerCase() == 'exit') {
      msg = 'echo exit';
    }
    if (isCommand) {
      this.lines[this.activeLine++].push(msg);
    }
    this.socket?.send(msg + "\n");
    this.lastCommand = msg;
    this.textBuffer = [];
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

  activeTerminal() {
    this.isActive = true;
  }

  onStart() {
    this.socket?.close();
    this.appService.run(this.app.project.id)
      .subscribe(
        (next) => {
           this.app = next;
           this.app.isActive = true;
           this.initTerminal();
           this.initSocket();
           this.onStarted.emit(this.app);
          },
        error => {
          console.log(error);
        }
      )
  }
}
