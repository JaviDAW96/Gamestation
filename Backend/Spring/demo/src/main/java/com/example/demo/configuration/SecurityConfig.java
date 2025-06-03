package com.example.demo.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.utils.JwtFilter;

/**
 * Esta clase configura la seguridad de la aplicación.
 * Aquí le decimos a Spring cómo queremos proteger nuestras rutas y cómo debe
 * autenticar a los usuarios.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Servicio que carga los datos de los usuarios desde la base de datos
    @Autowired
    private CustomUserDetailsService uds;
    // Filtro que comprueba el token JWT en cada petición
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Configura el AuthenticationManager, que es el encargado de autenticar
     * usuarios.
     * Le decimos que use nuestro servicio de usuarios y el encoder de contraseñas.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(uds) // Usa nuestro servicio para buscar usuarios
                .passwordEncoder(passwordEncoder()); // Usa BCrypt para las contraseñas
        return authBuilder.build();
    }

    /**
     * Este bean se encarga de encriptar las contraseñas usando BCrypt.
     * Así las contraseñas no se guardan en texto plano.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Aquí configuramos las reglas de seguridad:
     * - Desactivamos CSRF porque usamos JWT (no sesiones).
     * - Permitimos el acceso libre a /api/auth/**, /index y /error.
     * - Permitimos el acceso libre a las peticiones OPTIONS a /api/**.
     * - El resto de rutas requieren autenticación (token válido).
     * - No se guarda sesión en el servidor (stateless).
     * - Añadimos nuestro filtro JWT antes del filtro de usuario/contraseña.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**", "/index", "/error").permitAll()
                        .anyRequest().authenticated()
)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(
    "http://localhost:4200",
    "https://localhost:4200",
    "https://gamestation-frontend-58rf.onrender.com"
));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
