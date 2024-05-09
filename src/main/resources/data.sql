INSERT INTO user_role (
    role_code,
    role_name
)
SELECT 'ROLE_USER', '사용자'
from dual
WHERE NOT EXISTS ( SELECT * FROM user_role WHERE role_code ='ROLE_USER');

INSERT INTO user_role (
    role_code,
    role_name
)
SELECT 'ROLE_ADMIN', '관리자'
from dual
WHERE NOT EXISTS ( SELECT * FROM user_role WHERE role_code ='ROLE_ADMIN');

INSERT INTO user_role (
    role_code,
    role_name
)
SELECT 'ROLE_WORKER', '작업자'
from dual
WHERE NOT EXISTS ( SELECT * FROM user_role WHERE role_code ='ROLE_WORKER');
