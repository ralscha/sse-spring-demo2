import { DecimalPipe } from '@angular/common';
import { Component, OnDestroy, signal } from '@angular/core';

interface ProgressEvent {
  status: string;
  percentage: number;
}

@Component({
  selector: 'app-root',
  imports: [DecimalPipe],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnDestroy {
  protected readonly progress = signal<ProgressEvent | undefined>(undefined);
  protected readonly message = signal('Waiting for server events...');
  protected readonly connectionState = signal('connecting');

  private readonly eventSource = new EventSource('http://localhost:8080/register/client1');

  public constructor() {
    this.eventSource.onopen = () => {
      this.connectionState.set('connected');
    };
    this.eventSource.onerror = () => {
      this.connectionState.set('reconnecting');
    };
    this.eventSource.addEventListener('progress', (event) => {
      this.progress.set(JSON.parse(event.data) as ProgressEvent);
    });
    this.eventSource.addEventListener('event', (event) => {
      this.message.set(event.data);
    });
  }

  public ngOnDestroy(): void {
    this.eventSource.close();
  }
}
