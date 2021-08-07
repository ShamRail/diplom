import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

export class Project {

  id: number = 0;
  name: string = '';
  runCommand: string = '';
  inFiles: string = '';
  outFiles: string = '';
  configurationId: number = 0;


}

export class App {

  id: number = 0;
  startAt: number[] = [];
  endAt: string = '';
  containerID: string = '';
  wsURI: string = '';
  appStatus: string = '';
  message: string = '';
  project: Project = new Project();
  isActive: boolean = true;

}

export class CommandInfo {
  commandStatus: string = '';
  message: string = '';
}

@Injectable({
  providedIn: "root"
})
export class AppService {

  constructor(private httpClient: HttpClient) { }

  run(id: number): Observable<App> {
    return this.httpClient.post<App>(
      `${environment.api}/run`, {}, {
        params: new HttpParams().append('projectID', id)
      }
    );
  }

  uploadFile(idApp: number, data: FormData): Observable<CommandInfo> {
    return this.httpClient.post<CommandInfo>(`${environment.api}/uploadFile`, data, {
      params: new HttpParams().append('appID', idApp)
    });
  }

  downloadFile(appID: number, filePath: string): Observable<any> {
    //return `${environment.api}/downloadFile?appID=${appID}&filePath=${filePath}`;
    return this.httpClient.get(
      `${environment.api}/downloadFile?appID=${appID}&filePath=${filePath}`,
      { responseType: 'blob'}
    );
  }

  kill(id: number): Observable<any> {
    return this.httpClient.get<any>(`${environment.api}/kill`, {
      params: new HttpParams().append('id', id)
    })
  }

}
