import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {FunctionsUsingCSI} from "ng-terminal";
import {FunctionExpr} from "@angular/compiler";

@Injectable({
  providedIn: "root"
})
export class WebsocketService {

  // @ts-ignore
  socket: WebSocket;

  initSocket(url: string) {
    this.socket = new WebSocket(url);
  }

  subscribeOnOpen(): Observable<void> {
    return new Observable<any>(next => {
      this.socket.onopen = () => {
        next.next();
      }
    })
  }

  subscribeOnClose(): Observable<void> {
    return new Observable<void>(next => {
      this.socket.onclose = () => {
        next.next();
      }
    })
  }

  subscribeOnMessage(): Observable<string | ArrayBuffer | null> {
    return new Observable<string | ArrayBuffer | null>(next => {
      this.socket.onmessage = (event) => {
        let reader = new FileReader();
        reader.onload = function () {
          next.next(reader.result);
        }
        reader.readAsText(event.data);
      }
    })
  }

  subscribeOnError(): Observable<object> {
    return new Observable<object>(next => {
      this.socket.onerror = (error) => {
        next.next(error);
      }
    });
  }

  sendMessage(message: string) {
    this.socket.send(new Blob([message], {type: 'text/plain'}));
  }

}
