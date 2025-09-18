FROM node:20 AS build

COPY frontend/src /app/src
COPY frontend/package.json /app/package.json
COPY frontend/package-lock.json /app/package-lock.json
COPY frontend/angular.json /app/angular.json
COPY frontend/tsconfig.json /app/tsconfig.json
COPY frontend/tsconfig.app.json /app/tsconfig.app.json

WORKDIR /app

RUN npm ci

RUN npm run build --prod

FROM nginx:1.27.0-alpine AS final

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf
