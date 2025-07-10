import { Component, NgZone, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Observable, Observer, share} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Component({
    selector: 'app-root',
    imports: [],
    templateUrl: './app.component.html'
})
export class AppComponent {
  private readonly zone = inject(NgZone);
  private readonly httpClient = inject(HttpClient);


  private eventSource: EventSource | undefined;
  private readonly sseApi = 'http://localhost:8080';

  constructor() {
    this.subscribe('progress', 'client1').subscribe((data) => {
      console.log('Data received: ', data);
    });
  }

  private getEventSource(clientId: string, eventType: string): EventSource {
    if (!this.eventSource) {
      this.eventSource = new EventSource(`${this.sseApi}/register/${eventType}/${clientId}`);
    }

    return this.eventSource;
  }

  public subscribe(eventType: string, clientId: string): Observable<any> {
    const eventSource = this.getEventSource(clientId, eventType);

    eventSource.onmessage = function(event) {
      console.log('Message from server ', event.data);
      // Update progress bar based on event data
    };

    eventSource.onerror = function(err) {
      console.error('EventSource failed:', err);
    };

    return new Observable((observer: Observer<any>) => {
      const eventListener = (event: any) => {
        this.zone.run(() => observer.next(JSON.parse(event.data)));
      }

      eventSource.addEventListener(eventType, eventListener);
      return () => {
        eventSource.removeEventListener(eventType, eventListener);
      }
    })
      .pipe(
        share()
      );
  }

  public unsubscribe(eventType: string, clientId: string): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
    }

    this.httpClient.get(`${this.sseApi}/unregister/${clientId}`).subscribe();
  }
}
