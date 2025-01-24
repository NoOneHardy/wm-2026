FROM node:18.20-bookworm AS build
LABEL authors="no1hardy"

COPY ./src /app/src
COPY ./package.json /app/package.json
COPY ./package-lock.json /app/package-lock.json
COPY ./tsconfig.json /app/tsconfig.json

WORKDIR /app

RUN npm install
RUN npx tsc

FROM node:20.16-bookworm AS final
LABEL authors="no1hardy"

COPY --from=build /app/package.json /app/package.json
COPY --from=build /app/package-lock.json /app/package-lock.json
COPY --from=build /app/dist /app
COPY ./.env /app/.env

WORKDIR /app

RUN npm ci

CMD node --env-file=./.env ./index.js