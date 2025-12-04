import { Component, OnInit } from '@angular/core';
import { PixService } from '../../../services/pix.service';

@Component({
    selector: 'app-pix-transfer',
    templateUrl: './pix-transfer.component.html',
    styleUrls: ['./pix-transfer.component.css']
})
export class PixTransferComponent implements OnInit {
    step: number = 1;
    targetKey: string = '';
    amount: number = 0;
    targetName: string = '';
    message: string = '';
    isError: boolean = false;

    constructor(private pixService: PixService) { }

    ngOnInit(): void {
    }

    lookupKey(): void {
        this.pixService.getKeyOwner(this.targetKey).subscribe(
            data => {
                this.targetName = data.message.replace('Key belongs to: ', '');
                this.step = 2;
                this.message = '';
                this.isError = false;
            },
            err => {
                this.message = err.error.message;
                this.isError = true;
            }
        );
    }

    confirmTransfer(): void {
        this.pixService.transfer(this.targetKey, this.amount).subscribe(
            data => {
                this.message = data.message;
                this.isError = false;
                this.step = 3;
            },
            err => {
                this.message = err.error.message;
                this.isError = true;
            }
        );
    }

    reset(): void {
        this.step = 1;
        this.targetKey = '';
        this.amount = 0;
        this.targetName = '';
        this.message = '';
        this.isError = false;
    }
}
