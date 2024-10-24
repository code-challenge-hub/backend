# PID 찾기
PID=$(pgrep -f 'java -jar /home/ec2-user/app/code-challenge-hub.jar')

if [ -n "$PID" ]; then
  echo "Stop Code-challenge-hub Application jar $PID"
  kill -15 $PID
  wait $PID
  echo "Code-challenge-hub Application jar Stopped"
else
  echo "Code-challenge-hub Application jar not running"
fi