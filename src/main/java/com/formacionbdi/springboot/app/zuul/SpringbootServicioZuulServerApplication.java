package com.formacionbdi.springboot.app.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/***
 * Habilitar el micro servicio con EnableEurekaClient
 * es explicito ya que al tener la dependencia ya se estaria habilitando
 */
@EnableEurekaClient
/***
 *
 */
@EnableZuulProxy
@SpringBootApplication
public class SpringbootServicioZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootServicioZuulServerApplication.class, args);
    }

}
