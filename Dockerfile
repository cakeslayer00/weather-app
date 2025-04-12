FROM ubuntu:latest
LABEL authors="vladsv"

ENTRYPOINT ["top", "-b"]