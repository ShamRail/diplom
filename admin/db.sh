docker run --rm \
    --name admindb \
    -e POSTGRES_USER=runner \
    -e POSTGRES_PASSWORD=runner \
    -e POSTGRES_DB=admindb \
    -e PGDATA=/var/lib/postgresql/data \
    -p 5432:5432 \
    -v /home/rail/projects/diplom/admin/volumes/db:/var/lib/postgresql/data \
    postgres
