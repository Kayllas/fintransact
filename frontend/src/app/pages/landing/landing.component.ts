import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
    selector: 'app-landing',
    templateUrl: './landing.component.html',
    styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
    isLoggedIn = false;

    constructor(private tokenStorage: TokenStorageService, private router: Router) { }

    ngOnInit(): void {
        if (this.tokenStorage.getToken()) {
            this.isLoggedIn = true;
        }
    }

    navigateTo(path: string): void {
        this.router.navigate([path]);
    }
}
