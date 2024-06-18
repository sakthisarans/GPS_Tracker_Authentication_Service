FROM ubuntu:latest
LABEL authors="sakth"

ENTRYPOINT ["top", "-b"]