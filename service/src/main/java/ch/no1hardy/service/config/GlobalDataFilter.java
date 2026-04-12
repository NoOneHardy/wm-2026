package ch.no1hardy.service.config;

import ch.no1hardy.service.service.GlobalDataService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class GlobalDataFilter extends OncePerRequestFilter {
    private GlobalDataService globalDataService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        globalDataService.updateGlobalData();

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (attributes != null) {
                attributes.removeAttribute(GlobalKey.GLOBAL_DATA.getKey(), RequestAttributes.SCOPE_REQUEST);
            }
        }
    }
}
