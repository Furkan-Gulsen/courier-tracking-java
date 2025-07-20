#!/bin/bash


echo "=========================================="
echo "🧪 Running Courier Tracking Tests"
echo "=========================================="

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven and try again."
    exit 1
fi

run_service_tests() {
    local service_name=$1
    local service_dir=$2
    
    echo ""
    echo "📋 Running tests for $service_name..."
    echo "----------------------------------------"
    
    cd $service_dir
    
    mvn clean test
    
    if [ $? -eq 0 ]; then
        echo "✅ $service_name tests passed!"
    else
        echo "❌ $service_name tests failed!"
        exit_code=1
    fi
    
    cd ..
}

exit_code=0

run_service_tests "Courier Service" "courier-service"
run_service_tests "Tracking Service" "tracking-service"

echo ""
echo "=========================================="
if [ $exit_code -eq 0 ]; then
    echo "✅ All tests passed successfully!"
else
    echo "❌ Some tests failed. Please check the logs above."
fi
echo "=========================================="

exit $exit_code 