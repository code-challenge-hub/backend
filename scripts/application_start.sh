export $(cat /home/ec2-user/app/.env  | xargs -d '\n')
nohup java -jar /home/ec2-user/app/code-challenge-hub.jar > /home/ec2-user/app/server.log 2>&1 &