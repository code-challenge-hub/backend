export $(cat /home/ec2-user/app/.env  | xargs -d '\n')
nohup java -jar /home/ec2-user/app/code-challenge-hub.jar > server.log 2>&1 &