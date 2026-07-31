#!/bin/bash

# Script para optimizar tests en Jenkins CI
echo "🚀 Iniciando tests optimizados para CI..."

# Configurar variables de entorno para CI
export CI=true
export NODE_ENV=test

# Limpiar cache si existe
if [ -d ".jest-cache" ]; then
    echo "🧹 Limpiando cache de Jest..."
    rm -rf .jest-cache
fi

# Configurar Node para mejor performance
export NODE_OPTIONS="--max_old_space_size=4096"

# Ejecutar tests con configuración optimizada
echo "🧪 Ejecutando tests con configuración CI..."
npm run test:ci

echo "✅ Tests completados!"
