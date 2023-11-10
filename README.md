# BidPanda 프로젝트
[🐼 비드판다 사이트 이동하기](https://bidpanda.app/)

<img src="./bidpanda.png" />

## 프로젝트 소개
### 서비스 개요
```
    BidPanda 서비스는 판매자와 구매자간 커뮤니케이션 리소스를 최소화 하기 위해
    경매 방식 거래 서비스를 도입하는게 어떨까?라는 아이디어에서 시작된 서비스입니다.

    사이트에 가입된 유저은 팔고자하는 물건을 지정한 기한을 정해서 경매 방식으로 판매 가능합니다.
    판매물건이 낙찰 확정되면, 판매자-낙찰자간 알림을 전송하고, 채팅을 할 수 있습니다.
```

#### 프로젝트 기간
2023.10.04 ~ 2023.11.15

### 구현 기능

####  1.회원가입, 로그인 기능

- 이메일, 아이디로 가입하는 회원가입 기능
- 카카오 회원가입 기능
- `OAuth` 방식을 적용한 로그인 기능
- `JWT`를 적용한 인증,인가 구현

#### 2.경매 물품 관련 기능

- 물품 등록 API
    - Cron-Job.org 외부 API를 이용하여 마감 스케쥴링 이벤트 등록

- 물품 정보 수정 API
    - 유찰(입찰자가 없어 경매 실패) 시,<br>경매 기간 재연장 가능

- 물품 입찰 API (입찰가 등록 API)

- 물품 검색 API

- 물품 좋아요 API

#### 3.알림 기능

- 경매 마감 30분전, 입찰을 했던 회원에게 알림 메시지 기능 

- 경매 마감시간에, 판매자와 낙찰자에게 알림창에서 알림 보내는 기능

- 회원 알림 읽음 처리 기능

#### 4.채팅 기능

- 회원 채팅방 조회
    - 조회 요청한 회원의 낙찰자, 물품 판매자 입장 2가지 경우로 나눠서 주는 API 구현

- 회원 채팅방 입장
    - 채팅방 입장에 대한 인증, 인가 적용 
    - 채팅방 입장시,최신 채팅 메시지 20개 조회 API 구현

- 회원간 채팅 기능
    - STOMP가 적용된 웹소켓 채팅 기능 구현
    - 회원 채팅 참여 유무를 서버 캐시에 기록
    - 상대 회원이 채팅에 참여하지 않은 경우, 채팅 내용 기록 구현
    - 채팅방에 없는 회원에게 메시지 알림 전송 기능 동작

#### 5. 그 외

- 회원 정보 수정 API 
 

### 아키텍쳐 구성도

<!-- 여기에 이미지 -->

<img src="" />

### ERD

<img src="" />

### 사용 기술 스택

|목록|사용 기술|
|:---|:------|
|언어,프레임워크|<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/> <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>|
|데이터베이스/ ORM| <img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white"/> <img src="https://img.shields.io/badge/hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white"/>|
|빌드/ Dependency 관리|<img src="https://img.shields.io/badge/gradle-02303A?style=flat-square&logo=gradle&logoColor=white"/>|
|배포/CICD|<img src="https://img.shields.io/badge/amazonec2-FF9900?style=flat-square&logo=amazonec2&logoColor=white"/> <img src="https://img.shields.io/badge/githubactions-2088FF?style=flat-square&logo=githubactions&logoColor=white"/>|
|형상관리|<img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white"/>|
|그 외 |![](https://img.shields.io/badge/amazons3-569A31?style=flat-square&logo=amazons3&logoColor=white) ![](https://img.shields.io/badge/googlecloud-4285F4?style=flat-square&logo=googlecloud&logoColor=white)<br>[⌚cron-job.org](https://cron-job.org/en/)|

#### 기술 사용 의사 결정 
<!-- github-action nginx docker sse redis stomp(webSocket) cron-job -->

|사용 기술|적용부분|사용 이유|
|:-------|:-------|:-------|
|Redis| 인증, 인가 | 사용 이유|
|GitHub Action| CI/CD |프로젝트 레포지토리 특정 브런치에서 배포 |
|Docker| CI/CD |형철님 찢었따...|
|Nginx| Server |무중단 배포 구현을 위해서, 리버스 프록시 적용|
|SSE| 알림 기능 | 서버 -> Client 단방향 알림을 주기 위해서|
|STOMP| 채팅 기능 | STOMP특유의 정형화된 부분 -> 채팅 메시지 전달에 좋을 것 같음, 채팅 참여자 추적에 용이|
|Cron-Job| 마감 스케쥴링 기능 | 이미 잘 만들어진 외부 API를 이용 하자|
|Google Cloud| Database | 비용 절감 효과|



### 프로젝트 팀원 소개

|이름|역할|구현 기능|Contect|
|:-----:|:-----:|:------------|:-----|
|강성원|`LEAD`<br>`BE`|경매 상품(등록, 조회<querydsl 동적쿼리 적용>, 입찰)<br>경매 종료시 판매자와 낙찰자에게 SSE 알림 발송 ( Cron-Job에서 호출시)<br>알림 목록 조회<br>이미지 관리 AWS S3( 프로필 이미지, 상품 이미지)<br>회원탈퇴(Soft Delete)<br>응답 포맷 형식,<br>CI/CD<br>Swagger적용|📧 ksw270@gmail.com<br>✨ [https://github.com/dewey-89](https://github.com/dewey-89)|
|김병관|`BE`|SSE 알림,<br>단일 알람 조회 및 읽음 처리,<br>경매종료 30분 전 알람 SSE 전송 기능, 알람 삭제,<br>찜 알람 기능,<br>상품 수정 및 삭제 기능,<br>상품 찜하기 및 취소 기능,<br>마이페이지 조회 기능(사용자 입찰 물품, 찜한 물품, 경매 등록 물품 조회),<br>CI/CD, 글로벌 예외처리 적용|📧 ktera1231@gmail.com<br>✨[https://github.com/byunggwan-Kim](https://github.com/byunggwan-Kim)
|김혜린|`BE`|회원 채팅 List 조회 기능(Querydsl 적용),<br>채팅방 생성-입장 기능 (Websocket 전환),<br>채팅 최신 메시지 조회 기능,<br>Spring Websocket + STOMP가 적용된 채팅 기능,<br>채팅 참여 멤버 서버 캐싱, 채팅 메시지 알림 이벤트 발생,채팅 메시지 저장 기능,<br>Open API를 사용한 경매 종료 스케쥴링 작업 등록 기능(경매 종료, 경매 종료 30분 전),<br>경매 종료 시, Crong-job 작업 삭제, 알림 이벤트 발생 기능|📧 rlafls9596@gmail.com<br>✨[https://github.com/OolongTea620](https://github.com/OolongTea620)
|이형철|`BE`|SSE 알림,<br>OAuth2(카카오톡 로그인),<br>User(로그인, 회원 가입, 회원 정보 수정),<br>이메일 인증 보내기,<br>Github Actions + Docker + Codedeploy , Nginx 서버관리,<br>글로벌 예외처리 적용,  Redis, 응답 포맷 형식|📧 hyeongchul13@gmail.com<br>✨[https://github.com/hyeongchul13](https://github.com/hyeongchul13)<br>


## 트러블 슈팅 🚀 


### Cron-job Open API 사용

<details>
<summary>해결 과정</summary>
<div>

#### `전재`

현재 상품 데이터베이스에는 경매 종료 시간 필드가 존재하므로,<br>경매 종료시간에 맞춰서 
판매자와 낙찰자에게 SSE 알림을 보내려는 계획이었다.<br>

어떤 시간에 경매 종료가 일어나는지 알 수 없는 상황<br>(상품 등록자는 원하는 종료 시간을 자유롭게 선택 가능)

하지만, 경매 종료 시간에 딱 맞춰서 이벤트를 발생시키는 것이 어려웠다.

#### `의견 제시`

1\. BackEnd 서버에서 EVENT 발생시키기

Schedular를 이용해 DB쿼리를 매초 날려 경매종료시간이 지났는지 확인해서 지났으면 SSE알림을 보내게 한다. 


2\.FrontEnd 페이지에서 EVENT 발생시키기


프론트 페이지에서 경매시간이 초단위로 카운팅 되면서 줄어들고 있다.남은시간이 0이 되었을 때,<br>
해당 itemId를 가지고 판매자와 낙찰자에게 종료 SSE알림을 보내는 백엔드 서버로 요청을 보내서<br>
SSE알림을 보내게 한다.


3\. 외부 Open API를 사용해 EVENT 발생시키기

#### `도입과정`

스케쥴링 프로그램 중 Apatch Airflow 사용 고민
    → 프로그램이 가볍지 않다는 피드백을 받았음🤔


Chat GTP를 이용하여 스케쥴링 작업을 할 수 있는 외부 라이브러리, API 검색<br>
-> Cron-job.org를 알게 되었음

#### `해결`
회원이 상품 등록 시, 스케쥴링 추가 이벤트 발생 시킴<br>
`WebClient`를 이용해서 Cron-job.org에 API요청으로 스케쥴 작업 추가

</div>
</details>


### Cron-Job API 요청 시 에러

<details>
<summary>해결 과정</summary>
<div>

#### `전재`

→ 상품 등록시 Cron-Job API를 이용해 경매 종료 알림 예약을 잘 보내고 있었다.
    여기에 추가로 경매종료 30분전에 해당 상품에 입찰했던 사람들에게 경매 종료가 임박했다는 알림을 
    보내주기 위해 Cron-Job API 요청을 한번더 보냈는데, 에러가 발생해서 상품등록이 안되는 문제가 
    발생했다.<br>
    429 Too Many Requests from PUT https://api.cron-job.org/jobs

    ```
    2023-11-08T17:19:26.884+09:00 ERROR 33419 --- [nio-8080-exec-6] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.web.reactive.function.client.WebClientResponseException$TooManyRequests: 429 Too Many Requests from PUT https://api.cron-job.org/jobs] with root cause
    > org.springframework.web.reactive.function.client.WebClientResponseException$TooManyRequests: 429 Too Many Requests from PUT https://api.cron-job.org/jobs
	at org.springframework.web.reactive.function.client.WebClientResponseException.create(WebClientResponseException.java:325) ~[spring-webflux-6.0.12.jar:6.0.12]
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
Error has been observed at the following site(s):
	*__checkpoint ⇢ 429 TOO_MANY_REQUESTS from PUT https://api.cron-job.org/jobs [DefaultWebClient]
    ```
#### `원인`

Cron-job API는 1초에 1건의 요청만 보낼 수 있게 되어있기 때문에 두개를 연달아 보냈을 때, 1초내에 두개의 요청이 전송되어 에러가 나는 것이었다.

#### `시도`

수정 전
```java
publisher.publishEvent(new ItemCUDEvent(item, JobEventType.create));
publisher.publishEvent(new ItemCUDEvent(item, JobEventType.remind));
```

수정 후
```java
publisher.publishEvent(new ItemCUDEvent(item, JobEventType.create));
Thread.sleep(1000);
publisher.publishEvent(new ItemCUDEvent(item, JobEventType.remind));
```

Thread.sleep(1000) 으로 1초 쉬게하고 요청을 보내지게 하였지만, 메인 스레드나 다른 스레드에 미치는 영향을 미쳐서 동시성 문제가 생기는 것 같았다.

#### `해결`

`@Async` 어노테이션 사용

- 해당 메서드는 비동기적으로 실행되어 독립적으로 실행된다.
- 따라서 다른 작업에 영향을 미치지 않으며 Thread.sleep(1000)을 호출하더라도
메인 스레드나 다른 스레드에 미치는 영향이 없다.

이렇게 다른 작업중에도 상품 등록시 Cron-Job 요청을 1초 쉬면서 두번 보낼 수 있게 되었다.

```java
@Async
public void oneSecondDelay() throws InterruptedException {
   Thread.sleep(1000);
}
```

```java
publisher.publishEvent(new ItemCUDEvent(item, JobEventType.create));
oneSecondDelay();
publisher.publishEvent(new ItemCUDEvent(item, JobEventType.remind));
```
</div>
</details>


### QueryDSL 도입 과정

<details>
<summary>해결 과정</summary>
<div>

#### `상황`

→ 상품 리스트 조회 시 여러 조건들과 정렬들을 적용하기 위해서는 현재 상황에선 여러 조건들과 여러가지 
    정렬기능들을 다 만족하기 위해선 조회 API를 여러개 만들어야 했다.
    따라서, 동적으로 쿼리를 날릴 수 있는 아이템 리스트 조회 API가 필요하였다.

- Querydsl
1. SQL 쿼리를 Java 코드로 작성하므로 복잡한 쿼리 및 동적 쿼리 작성이 용이.
2. 타입 안정성을 제공하여 컴파일 시점에 오류를 발견할 수 있다.
3. JPA나 Hibernate와 함께 사용할 수 있어 JPA의 장점과 결합하여 활용가능.
4. 복잡한 데이터베이스 작업 및 성능 최적화에 우수한 지원을 제공.

#### `해결`

querydsl 쿼리 작성.

where절이나 orderBy절에 null값이 들어가면 무시되므로 원하는 조건을 동적으로 값을 넣을 수 있었다.

따라서 동적 쿼리로 아이템들을 조회할 수 있는 API 하나를 구현해서 여러 조건들로 조회할 수 있게 되었다.

조건 : 상품 검색, 카테고리별 상품 조회, 경매 진행중/종료된 상품 조회<br>
정렬 : 가격 낮은순/높은순, 경매 종료 남은시간 짧은순/긴순, 최신순, 입찰 많은 순

</div>
</details>


### 서버에서 넘겨준 RefreshToken이 헤더에 담기지 않는 문제
<details>
<summary>해결 과정</summary>
<div>

→ localhost에서는 발급된 RefreshToken이 헤더에 잘 넘어가는 반면, 배포된 서버에서는 토큰이 헤더로 넘어가지 않았다.

#### `시도`

1. 로그인할 때 RefreshToken이 잘 생성 되는지 log를 찍어 보며 배포된 서버에서 확인을 한다.

-> Gitbash에서 확인 결과, AccessToken은 잘 생성 되는 반면 RefreshToken은 Null값으로 확인됐다.

2. Postman으로 배포된 서버의 RefreshToken을 넣어 상품을 생성해보니 500에러가 뜨며, 
log에는 AccesToken이 만료 되었다는 메세지와 Jwtexception = null 이라는 에러 문구가 떴는데
우리가 설정해둔 JwtException("Refresh Token Error") 메시지가 뜨지 않는다는 것을 확인했다.

#### `해결`

1. Nginx는 header에 언더바(_)를 기본적으로 제한하고 있었다. 
저희 프로젝트에서는 Authorization_Refresh 라는 이름으로 사용하고 있었기 때문에 **Refresh 라는 이름으로 변경**하였다.

```java
// <수정 전>
public static final String AUTHORIZATION_HEADER = "Authorization";
public static final String REFRESH_HEADER = "Authorezation_Refresh";
public static final String BEARER_PREFIX = "Bearer ";
private final long TOKEN_TIME = 60 * 60 * 1000L;
private final long RefreshTOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;
```

```java
// <수정 후>
public static final String AUTHORIZATION_HEADER = "Authorization";
public static final String REFRESH_HEADER = "Refresh";
public static final String BEARER_PREFIX = "Bearer ";
private final long TOKEN_TIME = 60 * 60 * 1000L;
private final long RefreshTOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;
```

2. 본인이 설정한 Nginx conf 파일로 가서 server block 아래에 underscores_in_headers on; 을 적용하면 언더바가 있는 header 값도 나온다.

```java
sudo vim /etc/nginx/nginx.conf
// nginx.conf 파일 경로
```

```java
underscores_in_headers on;
```

→ 최종적으로는 Nginx측 [공식문서](https://www.nginx.com/resources/wiki/start/topics/tutorials/config_pitfalls/?highlight=underscore#missing-disappearing-http-headers)에 따르면 _(underscore)를 권장하지 않아서 1번 방법을 사용하여 언더바가 없는 Refresh라는 이름으로 token을 넘겨 주었다.

</div>
</details>


### SSE 구독 시 1분이 지나면 연결이 끊기는 문제

<details>
<summary>해결 과정</summary>
<div>

→ 로컬에서 SSE 통신이 잘 작동했으나 배포한 서버에서는 1분을 넘기지 못하고 연결이 계속 끊어지는 문제가 발생했다.

#### `의견 제시`

원인을 찾아보던 중 Nginx는 기본적으로 Upstream으로 요청을 보낼때 HTTP/1.0 버전을 사용한다는 것을 확인했다.

- HTTP/1.1은 지속 연결이 기본이기 때문에 헤더를 따로 설정해줄 필요가 없지만, **Nginx에서 백엔드 WAS로 요청을 보낼 때는 HTTP/1.0을 사용하고 `Connection: close` 헤더를 사용했다.**
- SSE는 지속 연결이 되어 있어야 동작하는데 Nginx에서 지속 연결을 닫아버려 제대로 동작하지 않는다고 생각했다.
- EC2 서버의 Nginx config 파일에 아래의 코드를 추가했으나 문제점이 해결되지는 않았다.

```java
proxy_set_header Connection '';
proxy_http_version 1.1;
```

#### `해결`

무중단 배포를 위해 Nginx를 사용하는데 **Nginx의 기본 ‘proxy_read_timeout 설정값이 60초로 설정**되어 있었다.

- ‘proxy_read_timeout’ 은 프록시 응답을 기다리는 최대 시간을 설정하는 코드.
- EC2 서버의 Nginx default파일에 아래의 코드를 추가해서 해결했다.

```java
sudo vim /etc/nginx/sites-available/default
// Nginx default 파일 진입 경로

// Nginx의 sites-available 디렉토리와 그 안에 있는 default 파일은 
// Nginx 웹 서버의 가상 호스트(Virtual Host) 설정을 관리하는 데 사용되는 
// 파일과 디렉토리입니다. 이러한 설정을 사용하여 
// Nginx는 여러 웹 사이트 또는 애플리케이션을 호스팅하고 관리할 수 있습니다.
```

```java
proxy_read_timeout 86400;
// 24시간으로 변경
```
	
</div>
</details>


### 회원탈퇴가 안되는 문제

<details>
<summary>해결 과정</summary>
<div>

#### `상황`
→ 회원 탈퇴 시 엔티티 연관관계들로 인해 계정 삭제가 안되는 문제가 발생 했다.<br>
또한 우리의 경매서비스 특성 상 판매자나 입찰자가 탈퇴를 해도 낙찰이 된 상품들은 이력들이 남아 있어야 했다.<br> 
경매 상품을 등록해서 진행 중 일 때, 경매 진행 중인 상품의 최고입찰자로 선정되어 있을 때에는 회원탈퇴가 불가능 해야 했다. 

#### `해결`

1. 공통
- 탈퇴하는 회원이 등록한 상품들 중 유찰 된 상품들은 삭제.
- 입찰 내역들 삭제.

1. 일반 회원 탈퇴
- 계정을 삭제하지 않고, 다시 가입할 수 있게 메일을 null 로 변환하고, isDeleted = true로 탈퇴한 회원으로 판단.
- nickname을 탈퇴한 회원+pk(memberId) 로 변경
→ 남아 있는 경매 이력들에 영향을 미치지 않음.

1. 카카오 회원 탈퇴
- 계정을 삭제하지 않고, 같은 카카오 계정으로 가입할 수 있게 kakoId와 membername 을 null 로 변환
- 메일을 null 로, isDeleted = true로 변환하여 탈퇴한 회원으로 표기.
- nickname을 탈퇴한 회원+pk(memberId) 로 변경
→ 남아 있는 경매 이력들에 영향을 미치지 않음.

```java
public void deactiveMember() {
        if (this.kakaoId != null) {
            this.kakaoId = null;
            this.membername = "탈퇴한 회원" + this.id;
        }
        this.nickname = "탈퇴한 회원" + this.id;
        this.email = null;
        this.isDeleted = true;
    }
```
</div>
</details>

## 부록 

### BidPanda API Documentation
[**🔗 Documentation Link**]()
