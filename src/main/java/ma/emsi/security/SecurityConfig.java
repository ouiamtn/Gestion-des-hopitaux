package ma.emsi.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
// pour dire que c'est une classe de configuration
//chaque classe qui utilise l'annotation config va etre instancier du 1er lieu
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{


    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    //configure=>la methode configure avec AuthenticationManagerBuilder va servir pour preciser quel strategie
    //que vous vouler utiliser pourque spring security va chercher l'utilisateur
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder= PasswordEncoder(); //permet de creer un objet BCryptPasswordEncoder
        /*String encodedPWD = PasswordEncoder().encode("1234");
        System.out.println(encodedPWD);

        auth.inMemoryAuthentication().withUser("user1").password(encodedPWD).roles("USER") // spring va chercher parmis ces users
                .and().
                withUser("user2").password(PasswordEncoder().encode("1111")).roles("USER")
                .and().
                withUser("admin").password(PasswordEncoder().encode("1234")).roles("USER", "ADMIN");*/
        /*auth.jdbcAuthentication()
                .dataSource(dataSource)
                //  pour spring security : principal =>username et credentials=> password
                .usersByUsernameQuery("select username as principal, password as credentials, active from users where username =?")
                //spring security va charger les users et compare le mdp entré avec le mdp de l'user
                // s'il est bon => il va creer la session
                // authoritiesByUsernameQuery=>pour charger les roles
                .authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
                .rolePrefix("ROLE_") // ajoute un prefixe
                .passwordEncoder(passwordEncoder);*/
// quand l'utilisateur va entrer son username et mdp spring sec va faire appel a l'obj userDetailsService qui va faire appel a la method loadUserByUername
        auth.userDetailsService(userDetailsService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // pour specifier les droits d'access
        // http.formLogin(); //  demander a spring security => utliser un formulaire d'authentification
        http.formLogin().loginPage("/login");
        // pour utliser votre formulaire on ajout:
        //http.formLogin().loginPage("/login");
        //qlq soit les ressources utilisé dans l'app necessite une auth
        //http.authorizeRequests().antMatchers("/delete/**", "/edit/**", "/save/**", "/formpatients/**").hasRole("admin");
        // ^ tt necessite un role admin
        //http.authorizeRequests().antMatchers("/index/**").hasRole("user");
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/user/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers("/resources/**" ,"/webjars/**", "/login").permitAll();
        //autoriser les ressources static
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");


    }
    @Bean // au demarrage creer moi un pwdencoder
    //il va utiliser bcrypt
    PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
