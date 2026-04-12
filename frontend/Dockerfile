FROM node:22 AS setup

WORKDIR /app

COPY ./frontend ./

RUN npm install

FROM node:22 AS build

COPY --from=setup /app /app

WORKDIR /app

RUN npm ci

RUN npm run build --prod

FROM nginx:1.27.0-alpine AS final

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf
