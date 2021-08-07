FROM ubuntu

WORKDIR /cpp
COPY . .
RUN apt-get update
RUN echo "y" | apt-get install g++

ARG BUILD_COMMAND
RUN echo $BUILD_COMMAND > echo.txt
RUN $BUILD_COMMAND > build_log.txt
