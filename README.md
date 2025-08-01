## 🔐 WH_MALL

 “WH MALL”은 가상의 전자상거래 플랫폼을 차용하여 설계되었다. WH MALL은 외부 고객 대상 웹 사이트, 중간망(API 서버), 내부망(DB Server)으로 나뉘며, 특히 외부망의 회원가입 및 게시글 작성 기능을 중심으로 공격 환경이 구축되어 있습니다.
 이러한 과정을 통해 실습자는 단순 취약점 악용을 넘어서, 클라이언트 검증 우회, 토큰 검증 로직 공격, 템플릿 RCE, 터널링 기반 내부망 침투, DB 조작, 비즈니스 로직 변조 등 종합적인 침투 시나리오 전 과정을 경험하게 됩니다. 
인증 로직 검증 우회, 키 관리 미흡, 서버 템플릿 보안 강화, 네트워크 탐지 및 로그 모니터링 등 다양한 보안 취약점과 대응 전략을 한꺼번에 실습할 수 있는 실전형 교육 도구로 활용할 수 있습니다.


---

## 🛠️ Technology Stack
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=flat-square&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?style=flat-square&logo=docker&logoColor=white)
![NGINX](https://img.shields.io/badge/NGINX-009639?style=flat-square&logo=nginx&logoColor=white)

---

## 📑 목차
1. 주요 명령어
2. 기여자
3. 협업 방식
4. 개발 기간
5. 공격 흐름도
6. ERD
---

## 주요 명령어
```
# 전체 서비스 시작
docker-compose up -d --build

# 전체 서비스 중지
docker-compose down

# 로그 확인
docker-compose logs -f [service-name]

# 특정 모듈만 빌드
docker-compose build [service-name]
```

---


## 👏 기여자 표

<h3>Project Team</h3>

<table>
  <thead>
    <tr>
      <th>Profile</th>
      <th>Role</th>
      <th>Expertise</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">
        <a href="https://github.com/MEspeaker">
          <img src="https://github.com/MEspeaker.png" width="60"/><br/>
          MEspeaker
        </a>
      </td>
      <td align="center">Project Member</td>
      <td align="center">SSTI Vulnerability, Chisel Reverse Tunneling</td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/Ranunculus2165">
          <img src="https://github.com/Ranunculus2165.png" width="60"/><br/>
          woo.__.bee
        </a>
      </td>
      <td align="center">Project Member</td>
      <td align="center">Auth Bypass, JWK Injection</td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/kkaturi14">
          <img src="https://github.com/kkaturi14.png" width="60"/><br/>
          minkyungkwak
        </a>
      </td>
      <td align="center">Project Member</td>
      <td align="center">Database, MyPage</td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://github.com/Hwanghangwoo">
          <img src="https://github.com/Hwanghangwoo.png" width="60"/><br/>
          HwangChangwoo
        </a>
      </td>
      <td align="center">Project Member</td>
      <td align="center">Coupon System, Product Page</td>
    </tr>
  </tbody>
</table>

---

## 🔥 협업 방식

| 🖥️ 플랫폼 | 🛠️ 사용 방식 |
|-----------|--------------|
| ![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white) | 매주 목요일,토요일 2시 회의 |
| ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) | PR을 통해 변경사항 및 테스트 과정 확인 |
| ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white) | 시나리오 구성, API, 회의 기록 문서화 |

---

## 📆 개발 기간

- 2025.06.29 ~ 2025.07.03 : 시나리오 컨셉 및 구현할 취약점 회의
- 2025.07.04 ~ 2025.07.07 : 각 취약점 구현 및 페이지 제작
- 2025.07.08 ~ 2025.07.13 : 버그 수정 및 에러페이지 구현
- 2025.07.13 ~ 2025.07.19 : 시나리오 보고서 작성 및 시나리오 통합 테스트
- 2025.07.29 ~ 2025.08.02 : 플랫폼 오픈

---
## 공격흐름도
<img width="1022" height="539" alt="image" src="https://github.com/user-attachments/assets/3e8b3cb6-9a32-4ad9-a980-14c812c58d4d" />

---
## 📝 ERD
<img width="1507" height="819" alt="image" src="https://github.com/user-attachments/assets/c7a88282-a923-433d-8490-69ad7aaf6c87" />

---

## Write-up
<img width="1700" height="2200" alt="WH MALL Write-up -02" src="https://github.com/user-attachments/assets/cf5c715c-f922-49f4-8da1-2893d98b2a02" />
<img width="1700" height="2200" alt="WH MALL Write-up -03" src="https://github.com/user-attachments/assets/2ab0623f-c71a-4453-8cf0-eb15bf6d0195" />
<img width="1700" height="2200" alt="WH MALL Write-up -04" src="https://github.com/user-attachments/assets/d91afeba-114e-436d-ad87-8d1b3f42ee25" />
<img width="1700" height="2200" alt="WH MALL Write-up -05" src="https://github.com/user-attachments/assets/0c5fc295-cc86-466c-a900-f88a1fe662c4" />
<img width="1700" height="2200" alt="WH MALL Write-up -06" src="https://github.com/user-attachments/assets/e9151116-37e5-4f5f-b44f-8cfffbfcdbaa" />
<img width="1700" height="2200" alt="WH MALL Write-up -07" src="https://github.com/user-attachments/assets/94acb076-fc0e-4d86-ae8a-5729c3db8417" />
<img width="1700" height="2200" alt="WH MALL Write-up -08" src="https://github.com/user-attachments/assets/03122969-c9d6-4ec7-bacd-12871ccb2018" />
<img width="1700" height="2200" alt="WH MALL Write-up -09" src="https://github.com/user-attachments/assets/8f3d48a0-2e89-4cc3-8120-c53506dd15aa" />
<img width="1700" height="2200" alt="WH MALL Write-up -10" src="https://github.com/user-attachments/assets/878c8c05-95cf-49d9-bbce-1e8d34ec4a0c" />
<img width="1700" height="2200" alt="WH MALL Write-up -11" src="https://github.com/user-attachments/assets/4acc38f4-f84b-421a-9148-417f88b05f35" />
<img width="1700" height="2200" alt="WH MALL Write-up -12" src="https://github.com/user-attachments/assets/32cb25b7-c5a2-4e14-a856-ba816e730128" />


---
