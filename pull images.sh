# 1. Pull need images

docker pull node
docker pull maven
docker pull openjdk
docker pull nginx
docker pull alpine/socat

# 2. Run docker-compose

docker-compose --compatibility up -d
