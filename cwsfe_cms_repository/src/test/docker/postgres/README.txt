0. Optional: remove existing tag
docker rm cwsfe_cms_dao_tests;

0. Optional: enter folder with Dockerfile
cd 9.6

1. Build postgres image in docker
docker build -t cwsfe_cms_dao_tests .

2. Run image with published port 55432
docker run -p 127.0.0.1:55432:5432 cwsfe_cms_dao_tests

3. Connect over JDBC
jdbc:postgresql://localhost:55432/postgres
with login "postgres" and password "postgres"
