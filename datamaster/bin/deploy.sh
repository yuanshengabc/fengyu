cd /opt/datamaster/
# $1: env  $2: branch
git pull
if [ -n "$2" ]; then
    git checkout $2
fi
git pull
./gradlew clean build -Denv=$1
./bin/datamaster stop
sleep 1s
./bin/datamaster start $1

cd /opt/datamaster-web
git pull
npm run build
