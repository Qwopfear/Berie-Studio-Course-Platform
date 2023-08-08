package berie.studio.beriestudiocourseplatform.service.auth;

import berie.studio.beriestudiocourseplatform.dto.AuthRequest;
import berie.studio.beriestudiocourseplatform.dto.AuthResponse;
import berie.studio.beriestudiocourseplatform.dto.RegistrationRequest;
import berie.studio.beriestudiocourseplatform.dto.RegistrationResponse;
import berie.studio.beriestudiocourseplatform.entity.user.User;
import berie.studio.beriestudiocourseplatform.jpa.UserRepository;
import berie.studio.beriestudiocourseplatform.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public RegistrationResponse register(RegistrationRequest request) {
        User user = userService.create(request);
        return new RegistrationResponse(
                        user.getId(),
                        jwtService.generateToken(user)
                );
    }

    @Override
    public AuthResponse authorization(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Bad credentials");
        }

        var jwtToken = jwtService.generateToken(
                userService.findByEmail(request.username()));
        return new AuthResponse(jwtToken);
    }



}
