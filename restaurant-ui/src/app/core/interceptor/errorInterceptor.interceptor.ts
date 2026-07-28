import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { MessageService } from 'primeng/api';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const messageService = inject(MessageService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Best Practice: Use translation keys for ALL messages, not raw English strings
      let messageKey = 'msg_something_went_wrong';

      if (error.status === 0) {
        messageKey = 'msg_cannot_connect';
      } else if (error.status === 400) { // FIXED: Added missing 'else' here
        messageKey = 'msg_bad_request';
      } else if (error.status === 401) {
        messageKey = 'msg_unauthorized';
      } else if (error.status === 403) {
        messageKey = 'msg_forbidden';
      } else if (error.status === 404) {
        messageKey = 'msg_not_found';
      } else if (error.status === 500) {
        // If the backend returns a specific string, you might have to display it as-is,
        // otherwise default to a translation key.
        messageKey = error.error?.message || 'msg_server_error';
      } else if (error.status === 413) {
        messageKey = 'msg_content_too_large';
      } else if (error.status === 415) {
        messageKey = 'msg_unsupported_media';
      } else if (error.error?.message) {
        // Warning: Backend messages usually aren't keys in your frontend i18n JSON
        messageKey = error.error.message;
      }


      messageService.add({
        severity: 'error',
        summary: messageKeyMap['label_error'],
        detail: messageKeyMap[messageKey] || messageKey,
        life: 10000
      });


      return throwError(() => error);
    })
  );
};


export const messageKeyMap: { [key: string]: string } = {

  "label_error": "Error",
  "label_success": "Success",

  "msg_login_success": "Login successful",
  "msg_login_failed": "Login failed",
  "msg_authenticated_user_could_not_be_loaded": "Authenticated user could not be loaded",
  "msg_invalid_email_or_password": "Invalid email or password",
  "msg_unauthorized": "Unauthorized access",
  "msg_forbidden": "Forbidden access",
  "msg_not_found": "Resource not found",
  "msg_cannot_connect": "Cannot connect to server",
};
