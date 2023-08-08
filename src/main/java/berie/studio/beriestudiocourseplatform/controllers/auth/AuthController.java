package berie.studio.beriestudiocourseplatform.controllers.auth;

import berie.studio.beriestudiocourseplatform.dto.AuthRequest;
import berie.studio.beriestudiocourseplatform.dto.AuthResponse;
import berie.studio.beriestudiocourseplatform.dto.RegistrationRequest;
import berie.studio.beriestudiocourseplatform.dto.RegistrationResponse;
import berie.studio.beriestudiocourseplatform.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegistrationResponse> register (
            @RequestBody RegistrationRequest request,
            UriComponentsBuilder ucb
    ) {
        RegistrationResponse registrationResponse = authService.register(request);
        URI locationOfNewUser = ucb
                .path("/api/v1/users/{id}")
                .buildAndExpand(registrationResponse.id())
                .toUri();

        return ResponseEntity.created(locationOfNewUser).body(registrationResponse);
    }


    @PostMapping("/authorize")
    public ResponseEntity<AuthResponse> authorization (
            @RequestBody AuthRequest request
    ) {
        return  ResponseEntity.ok(authService.authorization(request));
    }


}
