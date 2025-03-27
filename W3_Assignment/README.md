# 주문 관리 시스템

## 기술 스택
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database
- Lombok

## 주요 기능

### User (유저)
- 등록: 새 유저를 등록할 수 있습니다.
  - 필수 정보: `name`, `email`
- 조회
  - 단건 조회: 특정 유저를 ID로 조회
  - 목록 조회: 전체 유저 목록 조회

### Product (상품)
- 등록: 새 상품을 등록할 수 있습니다.
  - 필수 정보: `name`, `price`, `stockQuantity` (재고)
- 조회
  - 단건 조회: 상품 ID로 조회
  - 목록 조회: 전체 상품 목록 조회
- 재고 관리
  - 주문 시 재고가 감소되며, 0 미만으로 내려가면 예외가 발생합니다.

### Order (주문)
- 생성
  - 하나의 유저가 여러 상품을 한 번에 주문할 수 있습니다.
  - 유저 존재 여부와 각 상품의 재고를 확인하고, 주문 정보를 저장합니다.
- 조회
  - 단건 조회: 주문 ID로 조회 (주문에 포함된 상품 목록, 총금액 등 확인)
  - 유저별 주문 목록: 특정 유저가 가진 모든 주문 조회
- 취소
  - 주문을 취소하면 재고가 복원되고 주문 상태가 변경됩니다.

## API 엔드포인트

### User API
- `POST /api/users`: 새 유저 등록
- `GET /api/users/{id}`: 특정 유저 조회
- `GET /api/users`: 전체 유저 목록 조회

### Product API
- `POST /api/products`: 새 상품 등록
- `GET /api/products/{id}`: 특정 상품 조회
- `GET /api/products`: 전체 상품 목록 조회

### Order API
- `POST /api/orders`: 새 주문 생성
- `GET /api/orders/{id}`: 특정 주문 조회
- `GET /api/orders/user/{userId}`: 특정 유저의 주문 목록 조회
- `PATCH /api/orders/{id}/cancel`: 주문 취소

## 데이터베이스 구조
```
 ┌───────────────┐              ┌──────────────────┐
 │     USER      │              │      ORDER       │
 │---------------│              │------------------│
 │ PK: id        │    1    N    │ PK: id           │
 │ name          │ <----------> │ order_date       │
 │ email         │              │ status           │ <== "ORDERED","CANCELED"
 └───────────────┘              │ FK: user_id      │
                                └──────────────────┘
                                         │
                                         │ 1  :  N
                                         ▼
                             ┌──────────────────────┐
                             │      ORDER_ITEM      │
                             │----------------------│
                             │ PK: id               │
                             │ quantity             │
                             │ order_price          │
                             │ FK: order_id         │
                             │ FK: product_id       │
                             └──────────────────────┘
                                         │
                                         │ N : 1
                                         ▼
                                 ┌──────────────────┐
                                 │     PRODUCT      │
                                 │------------------│
                                 │ PK: id           │
                                 │ name             │
                                 │ price            │
                                 │ stockQuantity    │
                                 └──────────────────┘
```