#!/bin/bash

# === 설정 부분 ===
DOCKER_HUB_ID="dnglzl0823"         # Docker Hub ID
IMAGE_NAME="haeil-app"             # 이미지 이름
IMAGE_TAG="latest"                 # 이미지 태그
CONTAINER_NAME="haeil-container"   # 컨테이너 이름
PORT=8080                          # 서버 포트
SPRING_PROFILE="prod"              # active profile

# === 스크립트 위치 기준 경로 (스크립트가 어디서 실행되든 BE 폴더를 찾도록) ===
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ENV_FILE="$SCRIPT_DIR/.env"

# === .env 파일 확인 및 로드(또는 에러) ===
if [ -f "$ENV_FILE" ]; then
  echo "Using env file: $ENV_FILE"
else
  echo "ERROR: env file not found: $ENV_FILE"
  echo "Put your RDS / SERVER env variables into $ENV_FILE and try again."
  exit 1
fi

# === 기존 컨테이너 종료 및 삭제 (있으면) ===
EXISTING=$(docker ps -a -q -f name="^/${CONTAINER_NAME}$")
if [ -n "$EXISTING" ]; then
  echo "Stopping and removing existing container: $CONTAINER_NAME"
  docker stop "$CONTAINER_NAME" || true
  docker rm "$CONTAINER_NAME" || true
fi

# === Docker Hub에서 최신 이미지 pull ===
echo "Pulling image: $DOCKER_HUB_ID/$IMAGE_NAME:$IMAGE_TAG"
docker pull "$DOCKER_HUB_ID/$IMAGE_NAME:$IMAGE_TAG"

# === 새 컨테이너 실행 ===
echo "Running container: $CONTAINER_NAME"
docker run -d \
  --name "$CONTAINER_NAME" \
  -p "$PORT:$PORT" \
  --env-file "$ENV_FILE" \
  -e SPRING_PROFILES_ACTIVE="$SPRING_PROFILE" \
  --restart unless-stopped \
  "$DOCKER_HUB_ID/$IMAGE_NAME:$IMAGE_TAG"

# === 완료 메시지 ===
echo "Deployment finished."
echo "To follow logs: docker logs -f $CONTAINER_NAME"
echo "To check running containers: docker ps"
