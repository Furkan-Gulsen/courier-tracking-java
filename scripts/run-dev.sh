#!/bin/bash

echo "=========================================="
echo "🚀 Starting Courier Tracking (Development)"
echo "=========================================="

export SPRING_PROFILES_ACTIVE=dev
export COURIER_SERVICE_PORT=8080
export TRACKING_SERVICE_PORT=8081
export MONGO_ROOT_USER=admin
export MONGO_ROOT_PASSWORD=password

echo "📋 Development Configuration:"
echo "   • Profile: $SPRING_PROFILES_ACTIVE"
echo "   • Services will be accessible on localhost"
echo ""

echo "🧹 Cleaning previous builds..."
docker-compose down --remove-orphans --volumes

echo "🏗️  Building services with no cache..."
docker-compose build --no-cache

echo "🚀 Starting all services..."
docker-compose up

echo "✅ Development environment ready!" 