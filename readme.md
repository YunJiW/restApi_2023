## RestApi를 만드는 방법에 대해서 공부해보자.

- RestApi 주소 체계를 사용하는 이유
  - 보여지는 부분은 react 같은 게 담당하기 때문에 
  - Http 요청을 통해서 통신을 하여 CRUD 기능을 완료
  -  ** API의 구조를 명확하게 정의하고 관리하기 위해서 사용된다.

- 예시
  - 게시물 목록 : GET http://localhost:8080/articles

  - 게시물 등록 : POST http://localhost:8080/articles

  - 게시물 단건 조회 : GET http://localhost:8080/articles/1

  - 게시물 수정 : PATCH http://localhost:8080/articles/1

  - 게시물 삭제 : DELETE http://localhost:8080/articles/1

  - 회원 목록 : GET http://localhost:8080/members

  - 회원 로그인 : POST http://localhost:8080/member/login

  - 현재 로그인한 회원의 정보 : GET http://localhost:8080/member/me

-  추가로 PostMan 사용법도 익혀보자