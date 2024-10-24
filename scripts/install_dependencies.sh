
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

sudo dnf update -y
sudo dnf install -y https://corretto.aws/downloads/latest/amazon-corretto-17-x64-linux-jdk.rpm

if type -p java; then
  echo "Java install successfully"
else
  echo "Java install failed"
  exit 1
fi
