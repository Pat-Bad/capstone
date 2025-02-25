package it.epicode.capstone.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        // Ottieni l'eccezione dalla richiesta
        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");

        // Se l'eccezione esiste, lanciarla, altrimenti restituisci un errore generico
        if (exception != null) {
            throw new RuntimeException("Un errore si è verificato", exception);
        } else {
            // Se l'eccezione è null, restituisci un messaggio di errore generico
            return ResponseEntity.status(500).body("Errore interno del server");
        }
    }
}
