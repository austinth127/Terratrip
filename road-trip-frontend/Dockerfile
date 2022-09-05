FROM node:16 AS build
WORKDIR /build
COPY . .

RUN npm install
RUN npm run build

FROM node:16
WORKDIR /app
COPY --from=build /build .

RUN chmod +x ./frontend-entrypoint

# Running the app
ENTRYPOINT ./frontend-entrypoint
