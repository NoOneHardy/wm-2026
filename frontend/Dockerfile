FROM node:22 AS build

COPY /src /src/src
COPY /public /src/public
COPY ./angular.json /src/angular.json
COPY ./eslint.config.js /src/eslint.config.js
COPY ./package.json /src/package.json
COPY ./package-lock.json /src/package-lock.json
COPY ./tsconfig.json /src/tsconfig.json
COPY ./tsconfig.app.json /src/tsconfig.app.json
COPY ./tsconfig.spec.json /src/tsconfig.spec.json

WORKDIR /src

RUN npm ci

RUN npm run build --prod

FROM httpd:2-alpine AS final

COPY --from=build /src/dist/frontend/browser /usr/local/apache2/htdocs
