1. Build postgres image in docker
docker build .

2. Optional: remove existing tag
docker rm cwsfe_cms_dao_tests

3. Tag output docker ID with name "cwsfe_cms_dao_tests" (64ec7e6ffc7f is an example value)
docker tag 64ec7e6ffc7f cwsfe_cms_dao_tests

4. Run image with published port 55432
docker run -p 127.0.0.1:55432:5432 cwsfe_cms_dao_tests

5. Connect over JDBC
jdbc:postgresql://localhost:55432/postgres
with login "postgres" and password "postgres"