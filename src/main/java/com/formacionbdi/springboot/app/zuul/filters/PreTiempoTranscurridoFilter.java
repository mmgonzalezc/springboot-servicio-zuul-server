package com.formacionbdi.springboot.app.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/***
 * Se ejecuta antes de que el request sea enrutado
 * Se usa para pasar datos al request
 */
@Component
public class PreTiempoTranscurridoFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreTiempoTranscurridoFilter.class);

    /***
     * Definimos el tipo de filtro
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
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
        log.info(String.format("%s request enrutado a %s", request.getMethod(), request.getRequestURL().toString()));

        Long tiempoInicio = System.currentTimeMillis();
        request.setAttribute("tiempoInicio",tiempoInicio);


        return null;
    }
}
