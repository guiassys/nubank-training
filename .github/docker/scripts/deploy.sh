#!/bin/bash

set -euo pipefail

ENV="${1:-}"

ENV_DIR=".github/docker/env"
COMPOSE_FILE=".github/docker/compose/docker-compose.yml"
TARGET_ENV_FILE="$ENV_DIR/.env.$ENV"
STACK_NAME="nubank-training-$ENV"

if [[ -z "$ENV" ]]; then
    echo "❌ Environment not specified."
    echo "Usage: ./.github/docker/scripts/deploy.sh [dev|test]"
    exit 1
fi

if [[ "$ENV" != "dev" && "$ENV" != "test" ]]; then
    echo "❌ Invalid environment '$ENV'."
    echo "Usage: ./.github/docker/scripts/deploy.sh [dev|test]"
    exit 1
fi

if [[ ! -f "$TARGET_ENV_FILE" ]]; then
    echo "❌ Environment file not found: $TARGET_ENV_FILE"
    exit 1
fi

echo "📥 Loading environment file: $TARGET_ENV_FILE"

CLEAN_ENV_FILE=$(mktemp)
trap 'rm -f "$CLEAN_ENV_FILE"' EXIT

sed 's/\r//' "$TARGET_ENV_FILE" > "$CLEAN_ENV_FILE"

set -a
source "$CLEAN_ENV_FILE"
set +a

echo "--------------------------------------------------"
echo "⚙️ Deployment Information"
echo "--------------------------------------------------"
echo "Environment      : $ENV"
echo "Stack Name       : $STACK_NAME"
echo "Compose File     : $COMPOSE_FILE"
echo "Env File         : $TARGET_ENV_FILE"
echo "Runner User      : $(whoami)"
echo "--------------------------------------------------"

echo "🛑 Stopping previous container..."

docker compose \
  -p "$STACK_NAME" \
  -f "$COMPOSE_FILE" \
  --env-file "$CLEAN_ENV_FILE" \
  down \
  --remove-orphans

echo "🚀 Building and starting container..."

docker compose \
  -p "$STACK_NAME" \
  -f "$COMPOSE_FILE" \
  --env-file "$CLEAN_ENV_FILE" \
  up -d --build \
  --remove-orphans

echo "🧹 Cleaning dangling Docker images..."
docker image prune -f

echo "✅ Deployment completed successfully."