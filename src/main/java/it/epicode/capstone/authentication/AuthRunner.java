package it.epicode.capstone.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        Optional<AppUser> adminUser = appUserService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN),"email");
        }

        // Creazione dell'utente user se non esiste
        Optional<AppUser> normalUser = appUserService.findByUsername("user");
        if (normalUser.isEmpty()) {
            appUserService.registerUser("user", "userpwd", Set.of(Role.ROLE_USER),"email");
        }

        /* Creazione dell'utente seller se non esiste
        Optional<AppUser> normalSeller = appUserService.findByUsername("seller");
        if (normalUser.isEmpty()) {
            appUserService.registerUser("seller", "sellerpwd", Set.of(Role.ROLE_SELLER));
        }

        //creo utente organizzatore
        Optional<AppUser> organizzatoreEventi = appUserService.findByUsername("organizzatore");
        if (organizzatoreEventi.isEmpty()) {
            appUserService.registerUser("organizzatore", "organizzatorepwd", Set.of(Role.ROLE_ORGANIZZATORE));

        }*/
    }}
