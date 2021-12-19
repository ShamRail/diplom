# 1. Pull need images

docker-compose pull

# 2. Create environment vars

export DB_USER=  
export DB_PASSWORD= 
export ADMIN_LOGIN=
export ADMIN_PASSWORD=
export DOCKER_USER=
export DOCKER_PASSWORD=
export HOST=

# 3. Run docker-compose

docker-compose --compatibility up -d
