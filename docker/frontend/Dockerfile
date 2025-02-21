# stage 1
FROM node:18 as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install --save --legacy-peer-deps
COPY . .
RUN npm run build -omit=dev

#stage 2

FROM nginx:alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage  /app/dist /usr/share/nginx/html
EXPOSE 81
# ENTRYPOINT ["nginx","-g","daemon off;"]

# FROM node:10 AS ui-build
# WORKDIR /usr/src/app
# COPY my-app/ ./my-app/
# RUN cd my-app && npm install @angular/cli && npm install && npm run build

# FROM node:10 AS server-build
# WORKDIR /root/
# COPY --from=ui-build /usr/src/app/my-app/dist ./my-app/dist
# COPY package*.json ./
# RUN npm install
# COPY server.js .

# EXPOSE 3080

# CMD ["node", "server.js"]
# # FROM nginx:alpine
# # COPY ./nginx.conf /etc/nginx/conf.d/default.conf
# # COPY dist/ /usr/share/nginx/html
# # EXPOSE 80
