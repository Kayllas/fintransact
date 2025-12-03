import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../services/transaction.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  currentUser: any;
  account: any = {};
  transactions: any[] = [];
  displayTransferDialog: boolean = false;
  transferForm: any = {};

  constructor(
    private token: TokenStorageService,
    private transactionService: TransactionService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.loadAccountData();
    this.loadHistory();
  }

  loadAccountData(): void {
    this.transactionService.getBalance().subscribe(
      data => {
        this.account = data;
      },
      err => {
        console.error(err);
      }
    );
  }

  loadHistory(): void {
    this.transactionService.getHistory().subscribe(
      data => {
        this.transactions = data;
      },
      err => {
        console.error(err);
      }
    );
  }

  showTransferDialog(): void {
    this.displayTransferDialog = true;
  }

  onTransfer(): void {
    this.transactionService.transfer(this.transferForm.targetAccountNumber, this.transferForm.amount).subscribe(
      data => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Transfer successful' });
        this.displayTransferDialog = false;
        this.loadAccountData();
        this.loadHistory();
        this.transferForm = {};
      },
      err => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error.message || 'Transfer failed' });
      }
    );
  }

  logout(): void {
    this.token.signOut();
    window.location.reload();
  }
}
