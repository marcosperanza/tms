# Tasks Management System (TMS)

Description
---

TMS is a simple REST service project that serves APIs for managing your own activity tasks.

Please find the official [website](https://marcosperanza.github.io/tms/) for detailed information. 

About implementation
---

TMS uses [Dropwizard](https://www.dropwizard.io/en/latest/index.html) framework to serve the APIs.
The default database implementation is [H2](https://h2database.com/html/main.html) and the database is located into `target/db`.

To see an example of usages for MySql please point to the test cases. 

Testing
---

The code base contains a set of unit and integration tests that cover at least 85% of the code. During the build the code coverage is checked 
by [JaCoCo](https://www.jacoco.org/jacoco/). the minimum allowed coverage is 85%.  
The report is published into `target/site`


Open API
---

TMS exposes a OpenAPI `http://localhost:8080/api/swagger`. the page describes the REST APIs. To get a JSON representation use `http://localhost:8080/api/swagger.json`

How to start the TMS application
---

1. Run `mvn clean install` to build your application
2. Init the database `java -jar target/task-management-system-1.0-SNAPSHOT.jar db migrate config.yml`
3. Start application with `java -jar target/task-management-system-1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080/api/`

Dockerize your application
---

After you have built the application run:

```
docker build -t marcosperanza79/tms:latest -f target/docker/Dockerfile .
```

### Run production with docker/swarm

```
docker-compose -f src/main/docker/conf/docker-stack.yml up
```

or

```
docker stack deploy -c src/main/docker/conf/docker-stack.yml tms
```

### Deploy on Oracle Cloud OKE (k8b) 

In the production environment create a OKE cluster  

```
kubectl create -f src/main/k8b/k8b-tms-prod.deployment.yml
kubectl expose deployment tms --port=80 --type=LoadBalancer
```

inspect the loadbalancer 

```
kubectl get service tms
```


