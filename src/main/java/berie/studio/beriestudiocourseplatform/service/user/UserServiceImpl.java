package berie.studio.beriestudiocourseplatform.service.user;

import berie.studio.beriestudiocourseplatform.dto.RegistrationRequest;
import berie.studio.beriestudiocourseplatform.entity.user.Role;
import berie.studio.beriestudiocourseplatform.entity.user.User;
import berie.studio.beriestudiocourseplatform.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User create(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Already exist");
        }
        return userRepository.save(mapRequestToUser(request));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public User findById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent())
            return userOptional.get();
        throw new RuntimeException(
                String.format("User with id %s is no found",id));

    }

    private User mapRequestToUser(RegistrationRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(Role.USER)
                .build();
    }
}
