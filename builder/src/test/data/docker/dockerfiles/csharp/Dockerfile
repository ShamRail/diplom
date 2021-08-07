FROM mcr.microsoft.com/dotnet/sdk:3.1-focal AS build

WORKDIR /app
COPY . .
ARG BUILD_COMMAND
RUN echo $BUILD_COMMAND > echo.txt
RUN $BUILD_COMMAND > build_log.txt

ENTRYPOINT [ "bash" ]