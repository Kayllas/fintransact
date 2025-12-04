import { Component, OnInit } from '@angular/core';
import { PixService } from '../../../services/pix.service';

@Component({
    selector: 'app-pix-keys',
    templateUrl: './pix-keys.component.html',
    styleUrls: ['./pix-keys.component.css']
})
export class PixKeysComponent implements OnInit {
    keys: any[] = [];
    newKey: string = '';
    newKeyType: string = 'EMAIL'; // Default
    message: string = '';
    isError: boolean = false;

    keyTypes = [
        { label: 'Email', value: 'EMAIL' },
        { label: 'CPF', value: 'CPF' },
        { label: 'Random', value: 'RANDOM' }
    ];

    constructor(private pixService: PixService) { }

    ngOnInit(): void {
        this.loadKeys();
    }

    loadKeys(): void {
        this.pixService.getMyKeys().subscribe(
            data => {
                this.keys = data;
            },
            err => {
                console.error(err);
            }
        );
    }

    registerKey(): void {
        this.pixService.createKey(this.newKey, this.newKeyType).subscribe(
            data => {
                this.message = 'Key registered successfully!';
                this.isError = false;
                this.newKey = '';
                this.loadKeys();
            },
            err => {
                this.message = err.error.message;
                this.isError = true;
            }
        );
    }
}
