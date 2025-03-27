package it.epicode.capstone.authentication;

import it.epicode.capstone.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;
    private final AppUserRepository AppUserRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) throws MessagingException {
        AppUser newUser = appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_USER),
                registerRequest.getEmail()// Assegna il ruolo di default
        );
        emailService.sendEmail(
                newUser.getEmail(),
                "You're in!",
                "Hi, " + newUser.getUsername() + "! " + "That's it, you're in. Now, try to login and start creating " +
                        "playlists with your favorite songs!");


        return ResponseEntity.ok("Signup successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        AppUser appUser = appUserService.loadUserByUsername(loginRequest.getUsername());
        Long userId = appUser.getId();


        return ResponseEntity.ok(new AuthResponse(token, userId));
    }

    @GetMapping("/members")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getMembers() {
        try {
            List<UserDto> members = AppUserRepository.findAll().stream()
                    .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(members);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Access Denied, you don't have the necessary authority to see this " +
                            "content."));
        }
    }}


