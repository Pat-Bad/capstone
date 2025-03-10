package it.epicode.capstone.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.capstone.playlists.Playlist;
import it.epicode.capstone.vocalMemo.VocalMemo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private  boolean accountNonExpired=true;
    private  boolean accountNonLocked=true;
    private  boolean credentialsNonExpired=true;
    private  boolean enabled=true;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<VocalMemo> vocalMemos;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Playlist> playlists;


    private String email;


    @Override
    public Collection<GrantedAuthority> getAuthorities() {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    public AppUser(String username, String password, String email,Collection<? extends GrantedAuthority> authorities) {
        this(username, password, email, true, true, true, true, authorities);
    }

    public AppUser(String username, String password, String email, boolean enabled, boolean accountNonExpired,
                   boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
   }
}
