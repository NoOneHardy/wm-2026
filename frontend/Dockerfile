FROM node:20 AS build

COPY ./src /app/src
COPY ./package.json /app/package.json
COPY ./package-lock.json /app/package-lock.json
COPY ./angular.json /app/angular.json
COPY ./tsconfig.json /app/tsconfig.json
COPY ./tsconfig.app.json /app/tsconfig.app.json

WORKDIR /app

RUN npm ci

RUN npm run build --prod

FROM nginx:1.27.0-alpine AS final

COPY --from=build /src/dist/frontend/browser /usr/local/apache2/htdocs
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
