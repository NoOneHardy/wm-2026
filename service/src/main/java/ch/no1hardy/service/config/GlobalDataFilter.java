package ch.no1hardy.service.config;

import ch.no1hardy.service.front.GlobalData;
import ch.no1hardy.service.mapper.NotificationMapperImpl;
import ch.no1hardy.service.model.notification.Notification;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.service.UserService;
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
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class GlobalDataFilter extends OncePerRequestFilter {
    private UserService userService;
    private NotificationMapperImpl notificationMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
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
        User user = userService.getLoggedInUser();
        List<Notification> notifications;
        if (user != null) {
            notifications = userService.getNotifications(user);
        } else {
            notifications = List.of();
        }

        return GlobalData.builder()
                .id(UUID.randomUUID().toString())
                .notifications(notificationMapper.toDto(notifications))
                .build();
    }
}
