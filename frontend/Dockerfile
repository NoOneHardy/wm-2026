FROM node:22 AS setup

RUN apt-get update
RUN apt-get install -y  \
    libappindicator1  \
    fonts-liberation \
    libasound2 \
    libatk-bridge2.0-0 \
    libatspi2.0-0 \
    libgbm1 \
    libgtk-3-0 \
    libgtk-4-1 \
    libnspr4 \
    libnss3 \
    libvulkan1 \
    libxkbcommon0 \
    xdg-utils
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt --fix-broken install -y
RUN dpkg -i google-chrome-stable_current_amd64.deb
RUN google-chrome-stable --version

WORKDIR /app

COPY ./frontend ./

RUN npm install

USER node

FROM node:22 AS build

COPY --from=setup /app /app

WORKDIR /app

RUN npm ci

RUN npm run build --prod

FROM nginx:1.27.0-alpine AS final

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf
