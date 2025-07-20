#!/bin/bash

COURIER_SERVICE_URL="http://localhost:8080"
TRACKING_SERVICE_URL="http://localhost:8081"

echo "=========================================="
echo "🧪 Testing Courier Tracking APIs"
echo "=========================================="

echo "⏳ Waiting for services to be ready..."
sleep 10

check_health() {
    local service_name=$1
    local url=$2
    
    echo "🏥 Checking $service_name health..."
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url/actuator/health")
    
    if [ "$response" -eq 200 ]; then
        echo "✅ $service_name is healthy"
        return 0
    else
        echo "❌ $service_name is not responding (HTTP: $response)"
        return 1
    fi
}

check_health "Courier Service" $COURIER_SERVICE_URL
check_health "Tracking Service" $TRACKING_SERVICE_URL

echo ""
echo "🏪 Creating stores..."

echo "1. Creating Ataşehir Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/stores" \
  -H "Content-Type: application/json" \
  -d '{"name": "Ataşehir Store", "lat": 40.9923307, "lng": 29.1244229}'

sleep 1

echo "2. Creating Novada Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/stores" \
  -H "Content-Type: application/json" \
  -d '{"name": "Novada Store", "lat": 40.986106, "lng": 29.1161293}'

sleep 1

echo "3. Creating Beylikdüzü 5M Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/stores" \
  -H "Content-Type: application/json" \
  -d '{"name": "Beylikdüzü 5M Store", "lat": 41.0066851, "lng": 28.6552262}'

sleep 1

echo "4. Creating Ortaköy Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/stores" \
  -H "Content-Type: application/json" \
  -d '{"name": "Ortaköy Store", "lat": 41.055783, "lng": 29.0210292}'

sleep 1

echo "5. Creating Caddebostan Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/stores" \
  -H "Content-Type: application/json" \
  -d '{"name": "Caddebostan Store", "lat": 40.9632463, "lng": 29.0630908}'

sleep 2

echo -e "\n✅ All stores created!"

echo ""
echo "🚚 Creating test courier..."
COURIER_RESPONSE=$(curl -s -X POST "$COURIER_SERVICE_URL/api/v1/couriers" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Courier", "phoneNumber": "5551234567"}')

echo "$COURIER_RESPONSE"

COURIER_ID=$(echo "$COURIER_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)

if [ -z "$COURIER_ID" ]; then
    echo "❌ Failed to extract courier ID. Using default ID."
    echo "Creating courier with explicit ID for testing..."
    curl -s -X POST "$COURIER_SERVICE_URL/api/v1/couriers" \
      -H "Content-Type: application/json" \
      -d '{"name": "Test Courier", "phoneNumber": "5551234567"}'
    
    COURIER_ID="test-courier-123"
else
    echo "✅ Created courier with ID: $COURIER_ID"
fi

sleep 2

echo ""
echo "📍 Testing location reporting..."

echo "1. Reporting location near Ataşehir Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/couriers/$COURIER_ID/location" \
  -H "Content-Type: application/json" \
  -d '{"lat": 40.9923307, "lng": 29.1244229}'

sleep 2

echo -e "\n2. Reporting location moving away..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/couriers/$COURIER_ID/location" \
  -H "Content-Type: application/json" \
  -d '{"lat": 40.9925000, "lng": 29.1250000}'

sleep 2

echo -e "\n3. Reporting location near Novada Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/couriers/$COURIER_ID/location" \
  -H "Content-Type: application/json" \
  -d '{"lat": 40.986106, "lng": 29.1161293}'

sleep 2

echo -e "\n4. Reporting location near Caddebostan Store..."
curl -X POST "$COURIER_SERVICE_URL/api/v1/couriers/$COURIER_ID/location" \
  -H "Content-Type: application/json" \
  -d '{"lat": 40.9632463, "lng": 29.0630908}'

sleep 3

echo -e "\n5. Querying total travel distance..."
response=$(curl -s "$TRACKING_SERVICE_URL/api/v1/couriers/$COURIER_ID/total-travel-distance")
echo "📊 Total travel distance for $COURIER_ID: $response meters"

echo -e "\n6. Querying store entries..."
echo "📋 Store entries for $COURIER_ID:"
store_entries=$(curl -s "$TRACKING_SERVICE_URL/api/v1/couriers/$COURIER_ID/store-entries")

if [[ $store_entries == *"error"* ]]; then
    echo "❌ No store entries found or error occurred:"
    echo "$store_entries" | json_pp
else
    echo "$store_entries" | json_pp
fi

echo ""
echo "✅ API testing completed!"
echo ""
echo "🌐 Open these URLs to explore the APIs:"
echo "   • Courier Service API Docs: $COURIER_SERVICE_URL/swagger-ui/index.html"
echo "   • Tracking Service API Docs: $TRACKING_SERVICE_URL/swagger-ui/index.html" 