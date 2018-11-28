# openvidu-js-kotlin

openvidu, kurento 를 활용한 영상 채팅 rest 서버 입니다.
kotlin 으로 구현 되어 있으며, clinet 는 react 로 구현예정입니다.

기본 기능 구현 후, 영상 면접 솔루션 구현 예정입니다.



### 실행 방법(with docker)

A.  openvidu (with kurento server) start
 ```
 docker run -p 4443:4443 --rm -e openvidu.secret=MY_SECRET openvidu/openvidu-server-kms:2.6.0
 ex) docker run -p 4443:4443 --rm -e openvidu.secret=HAHAH -e openvidu.publicurl=https://192.168.10.149:4443/ openvidu/openvidu-server-kms:2.6.0
 ```

B. redis start
 ```
 docker run -p 6379:6379 redis -d redis
 ```
  
C. 서버 실행.
  - spring boot 로 실행 하세요.^^
  - rest controller 를 통해서 api 가 생성 됩니다.
  
  
  
  
  