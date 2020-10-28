# WebServer Guide

## Owner

|기능|URL|Method|
|------|---|---|
|로그인|/owner/signin|POST|
|회원가입|/owner/signup|POST|
|개인정보수정|/owner/info|POST|
|사장-직원 모드 전환|/owner/info/switch|POST|
|-----------------------------|---------------------------|----------|
|업체정보 열람|/owner/restaurant/:id|GET|
|업체 등록/수정/삭제|/owner/restaurant/|POST|
|업체 영업 상태 전환|/owner/restaurant/switch|POST|
|-----------------------------|---------------------------|----------|
|메뉴정보 열람|/owner/menu/:id|GET|
|메뉴 등록/수정/삭제|/owner/menu/|POST|
|-----------------------------|---------------------------|----------|
|테이블정보 열람|/owner/table/:id|GET|
|테이블 등록/수정/삭제|/owner/table/|POST|
|-----------------------------|---------------------------|----------|
