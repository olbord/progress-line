# Start with a Node.js environment to build
FROM --node:latest AS build-stage
# Set working directory
WORKDIR /app

# Copy package listst and install dependencies
COPY package*.json ./
RUN nmp ci

# Copy source code
COPY . .

# Build project inside /app/dist
RUN npm run build

FROM nginx:alpine

# Copy from the "build-stage" into the Nginx public directory.
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Copy the Nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Tell Docker that the container will listen on port 80
EXPOSE 80

# The command to start the Nginx server when the container starts
CMD ["nginx", "-g", "daemon off;"]