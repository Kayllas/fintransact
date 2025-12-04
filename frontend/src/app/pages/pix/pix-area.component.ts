import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-pix-area',
    templateUrl: './pix-area.component.html',
    styleUrls: ['./pix-area.component.css']
})
export class PixAreaComponent implements OnInit {
    activeTab: string = 'transfer'; // 'transfer' or 'keys'

    constructor() { }

    ngOnInit(): void {
    }
}
