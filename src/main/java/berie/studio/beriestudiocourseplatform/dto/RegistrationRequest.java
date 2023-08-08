package berie.studio.beriestudiocourseplatform.dto;


public record RegistrationRequest (
        String name,
        String email,
        String phoneNumber,
        String password
) {
}
