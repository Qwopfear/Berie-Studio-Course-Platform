package berie.studio.beriestudiocourseplatform.controllers.user;

import berie.studio.beriestudiocourseplatform.entity.user.User;
import berie.studio.beriestudiocourseplatform.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById (
            @PathVariable String id
    ) {
       return ResponseEntity.ok(userService.findById(id));
    }

}
