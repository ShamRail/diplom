FROM golang:1.17 AS build_base

COPY go.mod go.sum /go/app/
WORKDIR /go/app/
RUN go mod download
COPY . /go/app/

RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o build/app

FROM alpine:3.9
RUN apk add ca-certificates
COPY --from=build_base /go/app/build/app /site/app
COPY resources /resources

EXPOSE 8080
CMD ["/site/app"]