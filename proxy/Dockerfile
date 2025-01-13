FROM node:18.20-bookworm AS build
LABEL authors="no1hardy"

COPY ./src /app/src
COPY ./package.json /app/package.json
COPY ./package-lock.json /app/package-lock.json
COPY ./tsconfig.json /app/tsconfig.json

WORKDIR /app

RUN npm install
RUN npx tsc

FROM node:18.20-bookworm AS final
LABEL authors="no1hardy"

COPY --from=build /app/dist /app

WORKDIR /app

CMD node ./index.js