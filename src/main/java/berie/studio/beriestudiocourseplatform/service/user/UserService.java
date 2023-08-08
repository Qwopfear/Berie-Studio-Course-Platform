package berie.studio.beriestudiocourseplatform.service.user;

import berie.studio.beriestudiocourseplatform.dto.RegistrationRequest;
import berie.studio.beriestudiocourseplatform.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User create(RegistrationRequest request);

    User findByEmail(String email);
    User findById(String id);

}
