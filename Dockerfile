# 1단계: 빌드 (Gradle을 이용해 jar 파일 생성)
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# 소스 코드 전체 복사
COPY . .

# 윈도우(CRLF)와 리눅스(LF) 줄바꿈 차이로 인한 오류 방지 및 권한 부여
RUN chmod +x gradlew

# 테스트를 제외하고 빌드 진행
RUN ./gradlew clean bootJar --no-daemon

# 2단계: 실행 (가볍고 안전한 JRE 환경에서 실행)
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 단계에서 생성된 jar 파일을 복사해 옴
COPY --from=builder /app/build/libs/*.jar app.jar

# Render에서 지정하는 포트(기본 10000) 노출
EXPOSE 10000

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]