import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig(({ command }) => {
  const plugins = [
    vue(),
  ]

  // If 'serve' (npm run dev), then add this plugin.
  // If 'build' (npm run build), then do not add this plugin.
  if (command === 'serve') {
    plugins.push(vueDevTools())
  }

  return {
    plugins: plugins,
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
    server: {
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
          secure: false
        }
      }
    }
  }
})
