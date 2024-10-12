
if type -p java; then
  java_version=$(java -version 2>&1 | head -n 1)

  if [[ $java_version == *"17."* ]]; then
    exit 0
  else
    echo "Another version of java is installed : $java_version"
  fi
else
  echo "Java is not installed"
fi

sudo yum update -y
sudo yum install -y java-17-openjdk

if type -p java; then
  echo "Java install successfully"
else
  echo "Java install failed"
  exit 1
fi
