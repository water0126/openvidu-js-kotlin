# openvidu-js-kotlin

openvidu, kurento 를 활용한 영상 채팅 rest 서버 입니다.
kotlin 으로 구현 되어 있으며, clinet 는 react 로 구현예정입니다.

기본 기능 구현 후, 영상 면접 솔루션 구현 예정입니다.

db 는  mysql 로 application.yaml 의 datasource 를 수정 하면됩니다.
replication 연동까지 정의 되어 있으므로, 기본 template 변경 후, 원하는 서비스로 개발 가능합니다.

## 실행 방법
a. docker 를 통해 openvidu, kms 실행

~~~
docker run -p 4443:4443 --rm -e openvidu.secret=MY_SECRET openvidu/openvidu-server-kms:2.6.0
~~~

b. project gradle build / server start

~~~
./gradlew  build
cd build/libs/
java -jar demo-0.0.1-SNAPSHOT.jar
~~~

c. reactjs server start
 - 추후 repository 생성 예정입니다.
