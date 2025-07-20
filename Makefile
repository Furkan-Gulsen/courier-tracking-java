# Courier Tracking System - Makefile
# Simple commands to manage all operations

.PHONY: help build test docker-up docker-down clean setup api-test run-local run-dev package status logs

.DEFAULT_GOAL := help

GREEN := \033[0;32m
YELLOW := \033[1;33m
BLUE := \033[0;34m
NC := \033[0m

help: 
	@echo ""
	@echo "$(BLUE)Courier Tracking System - Available Commands$(NC)"
	@echo "=============================================="
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-15s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)Examples:$(NC)"
	@echo "  make setup     # First time setup"
	@echo "  make up        # Start with Docker"
	@echo "  make test      # Run all tests"
	@echo "  make clean     # Clean everything"
	@echo ""

build: ## Build both services
	@echo "$(YELLOW)Building services...$(NC)"
	@cd courier-service && mvn clean compile
	@cd tracking-service && mvn clean compile
	@echo "$(GREEN)✅ Build completed!$(NC)"

package: ## Package services as JARs
	@echo "$(YELLOW)Packaging services...$(NC)"
	@cd courier-service && mvn clean package -DskipTests
	@cd tracking-service && mvn clean package -DskipTests
	@echo "$(GREEN)✅ Packaging completed!$(NC)"

test: ## Run all tests
	@echo "$(YELLOW)Running all tests...$(NC)"
	@chmod +x ./scripts/run-tests.sh && ./scripts/run-tests.sh

up: docker-up ## Start services with Docker

docker-up: ## Start all services with Docker Compose
	@echo "$(YELLOW)Starting services...$(NC)"
	@chmod +x ./scripts/run-local.sh && ./scripts/run-local.sh

docker-down: ## Stop Docker Compose services
	@echo "$(YELLOW)Stopping services...$(NC)"
	@docker-compose down --remove-orphans

run-local: ## Run in local development mode
	@chmod +x ./scripts/run-local.sh && ./scripts/run-local.sh

run-dev: ## Run in development environment
	@chmod +x ./scripts/run-dev.sh && ./scripts/run-dev.sh

api-test: ## Test the APIs
	@chmod +x ./scripts/test-api.sh && ./scripts/test-api.sh

setup: ## First time project setup
	@echo "$(YELLOW)Setting up project...$(NC)"
	@command -v java >/dev/null 2>&1 || { echo "❌ Java 17+ required"; exit 1; }
	@command -v mvn >/dev/null 2>&1 || { echo "❌ Maven required"; exit 1; }
	@command -v docker >/dev/null 2>&1 || { echo "❌ Docker required"; exit 1; }
	@echo "$(GREEN)✅ Prerequisites OK$(NC)"
	@$(MAKE) build
	@echo "$(GREEN)✅ Setup completed!$(NC)"

clean: ## Clean all build artifacts
	@echo "$(YELLOW)Cleaning up...$(NC)"
	@cd courier-service && mvn clean
	@cd tracking-service && mvn clean
	@docker-compose down --volumes --remove-orphans 2>/dev/null || true
	@echo "$(GREEN)✅ Cleanup completed!$(NC)"

status: ## Show system status
	@echo "$(YELLOW)System Status$(NC)"
	@echo "=============="
	@curl -s http://localhost:8080/actuator/health >/dev/null && echo "✅ Courier Service" || echo "❌ Courier Service"
	@curl -s http://localhost:8081/actuator/health >/dev/null && echo "✅ Tracking Service" || echo "❌ Tracking Service"

logs: ## Show service logs
	@docker-compose logs --tail=30 