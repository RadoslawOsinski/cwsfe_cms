FROM fedora
MAINTAINER Radoslaw.Osinski@cwsfe.pl

# Install ``python-software-properties``, ``software-properties-common`` and PostgreSQL 9.3
#  There are some warnings (in red) that show up during the build. You can hide
#  them by prefixing each apt-get statement with DEBIAN_FRONTEND=noninteractive
#RUN yum -y install postgresql

# Run the rest of the commands as the ``postgres`` user created by the ``postgres-9.3`` package
#USER postgres

#init postgresql
#RUN postgresql-setup initdb

#start postgresql
#RUN systemctl start postgresql

#domyslny start postgresa po boot
#RUN chkconfig postgresql on

# Create a PostgreSQL role named ``cwsfe`` with ``cwsfe`` as the password and
# then create a database `cwsfe` owned by the ``cwsfe`` role.
# Note: here we use ``&&\`` to run commands one after the other - the ``\``
#       allows the RUN command to span multiple lines.
#RUN    psql --command "CREATE USER cwsfe WITH SUPERUSER PASSWORD 'cwsfe';" &&\
#    createdb -O cwsfe cwsfe

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
#RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf

# And add ``listen_addresses`` to ``/etc/postgresql/9.3/main/postgresql.conf``
#RUN echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf

# Expose the PostgreSQL port
#EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
#VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

# Set the default command to run when starting the container
#CMD ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]