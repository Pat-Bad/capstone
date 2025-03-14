package it.epicode.capstone.authentication;

import it.epicode.capstone.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) throws MessagingException {
       AppUser newUser= appUserService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                Set.of(Role.ROLE_USER),
                registerRequest.getEmail()// Assegna il ruolo di default
        );
        emailService.sendEmail(
                newUser.getEmail(),
        "Registrazione effettuata!",
       "Ciao, " + newUser.getUsername() +"! " + "La registrazione si è conclusa, ora puoi effettuare il login." );


        return ResponseEntity.ok("Registrazione avvenuta con successo");
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
}
