# Axon
Learning Axon Framework 

## Install Axon Server 
  1. https://download.axoniq.io/axonserver/AxonServer.zip
    - 위 주소에서 다운로드 받은 후 `java -jar axonserver.jar` 
  2. axonserver.properties 파일에 `axoniq.axonserver.devmode.enabled=true` 옵션을 사용하면 개발모드를 활성화 할 수 있다.
    - 개발 모드에서는 EventStore 의 초기화를 할 수 있다. (발행된 Event)
    
## Database
  1. mysql 설치
  2. 각 모듈별 application.yml 내용에 있는 계정 및 데이터베이스 생성
  
## Dashboard 접근
  1. Axon Server 는 application 간의 command, query의 내용을 확인 할 수 있는 대시보드 페이지를 제공한다.
  2. Axon Server 의 기본 대시보드 포트는 8024 이며 각 app들 과의 통신은 gRPC(8124) 프로토콜을 사용한다.
  3. axonserverhost:8024 주소로 접근하면 대시보드 페이지를 확인할 수 있다.
