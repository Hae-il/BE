# 실행용 경량 이미지
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# 로컬에서 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 컨테이너 실행
ENTRYPOINT ["java","-jar","app.jar"]