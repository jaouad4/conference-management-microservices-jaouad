import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // For now, we'll get a token and hardcode it
  // In production, this would come from a proper auth service
  
  // Get token from localStorage (we'll set it manually for testing)
  const token = localStorage.getItem('access_token');
  
  if (token) {
    // Clone the request and add the Authorization header
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }
  
  return next(req);
};
