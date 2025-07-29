package ch.no1hardy.service.config;

import ch.no1hardy.service.front.GlobalData;
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
import java.util.UUID;

@Component
@AllArgsConstructor
public class GlobalDataFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        GlobalData globalData = createGlobalData();

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.setAttribute(GlobalKey.GLOBAL_DATA.getKey(), globalData, RequestAttributes.SCOPE_REQUEST);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (attributes != null) {
                attributes.removeAttribute(GlobalKey.GLOBAL_DATA.getKey(), RequestAttributes.SCOPE_REQUEST);
            }
        }
    }

    private GlobalData createGlobalData() {
        return GlobalData.builder()
                .id(UUID.randomUUID().toString())
                .build();
    }
}
