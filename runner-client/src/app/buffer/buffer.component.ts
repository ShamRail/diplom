import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BufferDTO} from "../panel/panel.component";
import {DomSanitizer} from "@angular/platform-browser";
import {SafeUrl} from "@angular/platform-browser";

const imageFormats = [
  'svg', 'png', 'img', 'jpeg', "jpg"
];

@Component({
  selector: 'app-buffer',
  templateUrl: './buffer.component.html',
  styleUrls: ['./buffer.component.css']
})
export class BufferComponent implements OnInit {

  hasData: boolean = false;
  hasImageData: boolean = false;
  textData: any = '';
  imgURLString: SafeUrl = '#';

  @ViewChild('imgBuffer', {static: false}) imgBuffer: ElementRef | null = null;
  @ViewChild('buffer', {static: false}) buffer: ElementRef | null = null;

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
  }

  updateBuffer(input: BufferDTO) {
    this.hasData = true;
    if (this.isImage(input.file)) {
      this.hasImageData = true;
      this.setImage(input.blob);
    } else {
      this.hasImageData = false;
      this.setText(input.blob);
    }
  }

  private isImage(fileName: string): boolean {
    return imageFormats.filter(s => fileName.endsWith(s)).length != 0;
  }

  private setImage(data: Blob) {
    let urlString = URL.createObjectURL(data);
    this.imgURLString = this.sanitizer.bypassSecurityTrustUrl(urlString);
  }

  private setText(data: Blob) {
    if (this.imgURLString != '#') {
      URL.revokeObjectURL(this.imgURLString.toString());
    }
    let reader = new FileReader();
    reader.readAsText(data);
    reader.onload = () => {
      this.textData = reader.result;
    }
  }

}
