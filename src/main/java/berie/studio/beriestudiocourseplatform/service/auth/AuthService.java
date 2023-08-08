package berie.studio.beriestudiocourseplatform.service.auth;

import berie.studio.beriestudiocourseplatform.dto.AuthRequest;
import berie.studio.beriestudiocourseplatform.dto.AuthResponse;
import berie.studio.beriestudiocourseplatform.dto.RegistrationRequest;
import berie.studio.beriestudiocourseplatform.dto.RegistrationResponse;
import berie.studio.beriestudiocourseplatform.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {


    RegistrationResponse register(RegistrationRequest request);

    AuthResponse authorization(AuthRequest request);
}
