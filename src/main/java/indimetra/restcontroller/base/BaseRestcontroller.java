package indimetra.restcontroller.base;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import indimetra.utils.ApiResponse;

public abstract class BaseRestcontroller {
    protected String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "Anónimo";
    }

    protected boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    protected <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>(data, message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(201).body(new ApiResponse<>(data, message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> failure(String message) {
        return ResponseEntity.badRequest().body(new ApiResponse<>(message));
    }
}
