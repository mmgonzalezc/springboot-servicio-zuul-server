package com.formacionbdi.springboot.app.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/***
 * Se ejecuta despues de que el request haya sido enrutado
 * se usa para manipular la respuesta
 */
@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);

    /***
     * Definimos el tipo de filtro
     * @return
     */
    @Override
    public String filterType() {
        return "post";
    }

    /***
     * Definimos el orden
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /***
     * Para ejecutar o no el filtro
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /***
     * Resuelve la logica del filtro
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        /**
         * Necesitamos pasar datos al request
         */
        RequestContext ctx = RequestContext.getCurrentContext();
        // Atra vez del metodo contexto obtemos el request
        HttpServletRequest request = ctx.getRequest();
        log.info("Entrando a post filter");

        Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
        Long tiempoFinal = System.currentTimeMillis();
        Long tiempoTrascurrido = tiempoFinal - tiempoInicio;
        log.info(String.format("Tiempo trascurrido en segundos %s seg.", tiempoTrascurrido.doubleValue()/1000.00));
        log.info(String.format("Tiempo trascurrido en miliseg %s ms.", tiempoTrascurrido));


        return null;
    }
}
