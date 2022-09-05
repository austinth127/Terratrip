# Building the Next.js app
FROM node:16 AS build
WORKDIR /build
COPY . .

RUN npm install
RUN npm run build

# Configuring the Next.js app runtime
FROM node:16
WORKDIR /app
COPY --from=build /build .

ENV NODE_ENV production
ENV PORT 3000

EXPOSE 3000

# Running the app
CMD [ "npm", "start" ]
