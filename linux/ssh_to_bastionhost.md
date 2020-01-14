## 터미널에서 Bastionhost 를 통하여 ssh 접속 하는 방법

https://docs.bitnami.com/aws-templates/infrastructure/lamp-production-ready/get-started/connect-ssh/

```
chmod 600 KEYFILE 
ssh -i KEYFILE ec2-user@BASTION_IP # 접속 확인 <br>
ssh-add KEYFILE<br>
ssh -A ec2-user@BASTION_IP 
ssh-add -L  # 저장 확인
ssh username@PRIVATE_IP