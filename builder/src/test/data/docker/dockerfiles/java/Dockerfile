FROM maven
WORKDIR /app
COPY . .
ARG BUILD_COMMAND
RUN echo $BUILD_COMMAND > echo.txt
RUN $BUILD_COMMAND > build_log.txt
ENTRYPOINT ["bash"]
