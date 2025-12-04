import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
    qrCodeUrl: string = '';
    secret: string = '';
    code: string = '';
    is2faEnabled: boolean = false;
    message: string = '';
    isError: boolean = false;

    constructor(private authService: AuthService, private tokenStorage: TokenStorageService) { }

    ngOnInit(): void {
        const user = this.tokenStorage.getUser();
        // Ideally we should fetch the user status from backend to know if 2FA is already enabled
        // For now, we assume false or rely on user action
    }

    setup2fa(): void {
        this.authService.setup2fa().subscribe(
            data => {
                this.qrCodeUrl = data.qrCodeUrl;
                this.secret = data.secret;
                this.message = '';
            },
            err => {
                this.message = err.error.message;
                this.isError = true;
            }
        );
    }

    verify2fa(): void {
        this.authService.verify2fa(this.code).subscribe(
            data => {
                this.is2faEnabled = true;
                this.message = data.message;
                this.isError = false;
                this.qrCodeUrl = ''; // Hide QR code after success
            },
            err => {
                this.message = err.error.message;
                this.isError = true;
            }
        );
    }
}
