#!/bin/bash

echo "=========================================="
echo "🚀 Starting Courier Tracking Application"
echo "=========================================="

export SPRING_PROFILES_ACTIVE=dev
export COURIER_SERVICE_PORT=8080
export TRACKING_SERVICE_PORT=8081
export MONGO_ROOT_USER=admin
export MONGO_ROOT_PASSWORD=password

echo "📋 Environment Configuration:"
echo "   • Profile: $SPRING_PROFILES_ACTIVE"
echo "   • Courier Service: http://localhost:$COURIER_SERVICE_PORT"
echo "   • Tracking Service: http://localhost:$TRACKING_SERVICE_PORT"
echo ""

if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

echo "🧹 Cleaning up existing containers..."
docker-compose down --remove-orphans

echo "🏗️  Building and starting services..."
docker-compose up --build

echo "✅ Application started successfully!"
echo ""
echo "📖 API Documentation:"
echo "   • Courier Service API: http://localhost:$COURIER_SERVICE_PORT/swagger-ui/index.html"
echo "   • Tracking Service API: http://localhost:$TRACKING_SERVICE_PORT/swagger-ui/index.html"
echo ""
echo "📊 Monitoring:"
echo "   • Courier Service Health: http://localhost:$COURIER_SERVICE_PORT/actuator/health"
echo "   • Tracking Service Health: http://localhost:$TRACKING_SERVICE_PORT/actuator/health" 