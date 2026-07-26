FROM nginx:1.29.5-alpine-slim AS final

COPY proxy/nginx.conf /etc/nginx/nginx.conf
COPY proxy/default.conf /etc/nginx/templates/default.conf.template

EXPOSE 26000