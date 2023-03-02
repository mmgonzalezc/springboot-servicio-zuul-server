package com.formacionbdi.springboot.app.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
/***
 * RefreshScope Anotacion que nos permite actualizar en tiempo real mediante una url de spring actuator los
 * componentes, controlodores, clases anotados con component, service , controllers
 * que le estemos enyectando con @Value.
 */
@RefreshScope
@Configuration
/**
 * @EnableResorceServer -> configuracion del servidor del recurso
 */
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;

    /***
     * Metodo de configuracion para el token
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    // Configuramos Token storage
        resources.tokenStore(this.tokenStore());

    }

    /***
     *
     * Metodo de configuracion para ruta
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        /**
         * Protegemos nuestros endpoints
         * patron ** -> Significa que se aplica a cualquier ruta que venga despues de oauth
         * hasAnyRole -> Roles
         * has -> Rol
         * anyRequest -> Cualquier ruta que no halla sigo configurada
         * authenticated -> Requiere autentificacion
         */

        http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll() // CUALQUIER USUARIO SE PUEDE AUTENTICAR
                .antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios").permitAll()
                .antMatchers(HttpMethod.GET, "/api/productos/ver/{id}",
                        "/api/items/ver/{id}/cantidad/{cantidad}",
                        "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/productos/**", "/api/items/**", "/api/usuarios/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().cors().configurationSource(corsConfigurationSource());
    }

    /***
     * Configuracion de nuestras aplicaciones clientes
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("*")); // patron * permite agregar de manera automatica cualquier origen
        corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS")); // Permiter todos los metodos
        corsConfig.setAllowCredentials(true); //
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Permitir cabezeras
        // Pasamos la configuracion a nuestras rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // registramos la configuracion para que se aplique a todas las rutas
        return source;
    }

    /***
     * Configuracion de filtro core, para que tambien quede configurado a nivel global en un filtro de spring a toda nuestra aplicacion en general
     * @return
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Prioridad alta
        return bean;
    }

    /**
     * Se generara como componente de spring pra la configuracion
     * @return
     */
    @Bean
    /***
     * Componente que se encarga de guardar el token con los datos del accessTokenConverter
     */
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Se generara como componente de spring pra la configuracion
     * @return
     */
    @Bean
    /***
     * Se encarga de guardar los datos del usuario en el token codificados en base 64
     */
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(jwtKey);// Asigna llave secreta
        return tokenConverter;
    }
}
