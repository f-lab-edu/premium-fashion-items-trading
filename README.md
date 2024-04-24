# premium-fashion-items-trading

## **프리미엄 패션 제품 거래 서비스**

KREAM을 모티브로 만든 프리미엄 패션 제품 거래 서비스 입니다.

## **프로젝트 목표**

- 한정판 거래 플랫폼 KREAM을 모티브로 사용자의 코어 기능인 거래 서비스와 제품을 관리하는 관리자 / 제품을 검수하는 작업자의 프로세스도 구현하는 것이 목표입니다.
- 단순한 기능 구현 뿐 아니라 분산 서버 환경에서 대용량 트래픽 처리와 데이터 일관성을 고려한 기능을 구현하는 것이 목표입니다.
- 문서화, 단위 테스트에 높은 우선순위를 두고 작업하고, CI/CD를 통한 자동화를 구현하여 쉽게 협업이 가능한 프로젝트로 만드는 것이 목표입니다.
- 최소한의 시스템 구조로 개발을 진행하고 성능 이슈가 발생하는 항목을 체크하여 확장하는 방향으로 개발을 진행하는 것이 목표입니다. 
- 객체지향 원리와 여러 이론적 토대위에서 올바른 코드를 작성하는 것이 목표입니다.

## **시스템 구성도**
![Pasted image 20240423225608](https://github.com/f-lab-edu/premium-fashion-items-trading/assets/110794550/f9209585-33df-452f-a489-7981008df482)

## **데이터베이스 ERD**
![2024-04-23 22 42 10](https://github.com/f-lab-edu/premium-fashion-items-trading/assets/110794550/c2cc0eaa-bd27-4d1f-8b55-f2a3e0b84d6c)

## **사용기술**
- Java 17
- Spring Boot 3.2.4
- JPA
- Gradle
- Junit
- MySQL
- Redis
- Firebase
- Jenkins
- Nginx
- Naver Cloud Platform
- Grafana
- Prometheus

## **API 명세서**

### **사용자 API 명세**

| No  | API Name                  | Method | EndPoint              |
| --- | ------------------------- | ------ | --------------------- |
| 1   | 로그인                       | POST   | /user/login           |
| 2   | 로그아웃                      | POST   | /user/logout          |
| 3   | 사용자 등록                    | POST   | /user                 |
| 4   | 사용자 편집                    | PATCH  | /user                 |
| 5   | 마이페이지 조회                  | GET    | /user/mypage          |
| 6   | 포인트 조회                    | GET    | /user/point           |
| 7   | 메인화면 조회                   | GET    | /main                 |
| 8   | 상품페이지 조회                  | GET    | /item/detail          |
| 9   | 상품페이지 - 체결 목록 조회 / Cursor | GET    | /trade                |
| 10  | 상품페이지 - 판매 입찰 조회 / Cursor | GET    | /trade/sell           |
| 11  | 상품페이지 - 구매 목록 조회 / Cursor | GET    | /trade/purchase       |
| 12  | 구매하기                      | POST   | /trade/purchase       |
| 13  | 판매하기                      | POST   | /trade/sell           |
| 14  | 주소록 조회                    | GET    | /address              |
| 15  | 주소록 등록                    | POST   | /address              |
| 16  | 주소록 편집                    | PATCH  | /address              |
| 17  | 포인트 충전                    | POST   | /point-process        |
| 18  | 포인트 처리 조회 / Cursor        | GET    | /point-process        |
| 19  | 마이페이지 - 구매 내역 조회 / Cursor | GET    | /trade/puchase        |
| 20  | 마이페이지 - 구매 입찰 상세          | GET    | /trade/puchase/detail |
| 21  | 마이페이지 - 판매 내역 조회          | GET    | /trade/sell           |
| 22  | 마이페이지 - 판매 입찰 상세          | GET    | /trade/sell/detail    |
| 23  | 알림 조회 / Cursor            | GET    | /notification         |

### **관리자 API 명세**

| No  | API Name         | Method | EndPoint             |
| --- | ---------------- | ------ | -------------------- |
| 1   | 카테고리 조회          | GET    | /category            |
| 2   | 카테고리 조회 / Paging | GET    | /category/paging     |
| 3   | 카테고리 등록          | POST   | /category            |
| 4   | 카테고리 편집          | PATCH  | /category            |
| 5   | 브랜드 조회           | GET    | /brand               |
| 6   | 브랜드 조회 / Paging  | GET    | /brand/paging        |
| 7   | 브랜드 등록           | POST   | /brand               |
| 8   | 브랜드 편집           | PATCH  | /brand               |
| 9   | 사이즈 조회           | GET    | /size                |
| 10  | 사이즈 조회 / Paging  | GET    | /size/paging         |
| 11  | 사이즈 등록           | POST   | /size                |
| 12  | 사이즈 편집           | PATCH  | /size                |
| 13  | 상품 조회            | GET    | /item                |
| 14  | 상품 조회 / Paging   | GET    | /item/paging         |
| 15  | 상품 등록            | POST   | /item                |
| 16  | 상품 편집            | PATCH  | /item                |
| 17  | 알림 조회 / Paging   | GET    | /notification/paging |

### **작업자 API 명세**

| No | API Name | Method | EndPoint               |
| -- | -------- | ------ | ---------------------- |
| 1  | 운송장 조회   | GET    | /item-inspect/invoice  |
| 2  | 입고 처리    | POST   | /item-inspect/receipt  |
| 3  | 검수 대상 조회 | GET    | /item-inspect/inspect  |
| 4  | 검수 처리    | POST   | /item-inspect/inspect  |
| 5  | 발송 대상 조회 | GET    | /item-inspect/shipment |
| 6  | 발송 처리    | POST   | /item-inspect/shipment |

## **서비스 UI 프로토타이핑**

### **사용자 UI**
https://ovenapp.io/view/s35YjjnYqjmjmaFUzywLWUOn9ymOIHBN/XC4TV

### **관리자 UI**
https://ovenapp.io/view/OXfxsceYw4As0fHPb1BufOC4J2F8ynTC/XRGLp