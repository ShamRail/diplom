docker run \
    --name sitedb \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=password \
    -e POSTGRES_DB=sitedb \
    -e PGDATA=/var/lib/postgresql/data \
    -p 5432:5432 \
    -v /home/rail/projects/diplom/site/volums/db:/var/lib/postgresql/data \
    postgres