# Giai đoạn cơ sở (Base): Cài đặt thư viện
FROM node:18-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm install

# Giai đoạn Phát triển (Dev): Hỗ trợ hot-reload
FROM base AS dev
COPY . .
EXPOSE 5173
# Cần --host để Vite lắng nghe kết nối từ bên ngoài container
CMD ["npm", "run", "dev", "--", "--host"]

# Giai đoạn Xây dựng (Build)
FROM base AS build
COPY . .
RUN npm run build

# Giai đoạn Chính thức (Production): Dùng Nginx
FROM nginx:stable-alpine AS final
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
