#
# Copyright © 2016-2022 The Thingsboard Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM thingsboard/openjdk11

RUN apt-get update
RUN apt-get install -y curl gnupg2 wget
RUN echo 'deb http://apt.postgresql.org/pub/repos/apt/ buster-pgdg main' | tee --append /etc/apt/sources.list.d/pgdg.list > /dev/null
RUN curl -L https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -
ENV PG_MAJOR 12
RUN apt-get update
RUN apt-get install -y postgresql-12
RUN update-rc.d postgresql disable
RUN wget https://github.com/thingsboard/thingsboard/releases/download/v3.3.3/thingsboard-3.3.3.deb
RUN mv thingsboard-3.3.3.deb /tmp/

COPY logback.xml thingsboard.conf install-tb.sh start-tb.sh upgrade-tb.sh start-db.sh stop-db.sh /tmp/

RUN chmod a+x /tmp/*.sh \
    && mv /tmp/start-tb.sh /usr/bin \
    && mv /tmp/upgrade-tb.sh /usr/bin \
    && mv /tmp/install-tb.sh /usr/bin \
    && mv /tmp/start-db.sh /usr/bin \
    && mv /tmp/stop-db.sh /usr/bin

RUN dpkg -i /tmp/thingsboard-3.3.3.deb
RUN rm /tmp/thingsboard-3.3.3.deb

RUN systemctl --no-reload disable --now thingsboard.service > /dev/null 2>&1 || :

ENV DATA_FOLDER=/data

ENV HTTP_BIND_PORT=9090
ENV DATABASE_TS_TYPE=sql

ENV PGDATA=/data/db
ENV PATH=$PATH:/usr/lib/postgresql/12/bin

ENV SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
ENV SPRING_DRIVER_CLASS_NAME=org.postgresql.Driver
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/thingsboard
ENV SPRING_DATASOURCE_USERNAME=thingsboard
ENV SPRING_DATASOURCE_PASSWORD=postgres


ENV PGLOG=/var/log/postgres
RUN mkdir -p $PGLOG
RUN chown -R thingsboard:thingsboard $PGLOG

RUN chown -R thingsboard:thingsboard /var/run/postgresql

RUN mkdir -p /data
RUN chown -R thingsboard:thingsboard /data

RUN chown -R thingsboard:thingsboard /var/log/thingsboard

USER thingsboard

EXPOSE 9090
EXPOSE 1883
EXPOSE 5432
EXPOSE 5683/udp
EXPOSE 5685/udp

VOLUME ["/data"]

CMD ["start-tb.sh"]
