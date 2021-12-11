### Deployment

For deploiyng in production or stage you can choose multiple ways: Docker/swarm or k8b. Some scripts have been created to
deploy a default configuration.

All project docker images are published into [Docker Hub](https://hub.docker.com/u/marcosperanza79)

- [TMS](https://hub.docker.com/u/marcosperanza79/tms)
- [TMS-UI](https://hub.docker.com/u/marcosperanza79/tms-ui)

_The docker images will be published by github workflows_

#### Docker/SWARM services

- **Compose**

Deploy on the same node Server and UI

```
docker-compose -f src/main/docker/conf/docker-stack.yml up
```

- **SWARM**

Deploy on docker swarm Server and UI

```
docker stack deploy -c src/main/docker/conf/docker-stack.yml tms
```

#### Kubernates

You need a production k8b cluster. i.e. Oracle Kubernates Cluster Service

```
kubectl apply -f https://raw.githubusercontent.com/marcosperanza/tms/master/src/main/k8b/k8b-tms-prod.deployment.yml
kubectl expose deployment tms --port=80 --type=LoadBalancer
```

inspect the loadbalancer for discovering the public IP. The UI will be exposed on the `http://<public_ip>`

```
kubectl get service tms
```


### Develop

To run the server and ui:

`docker-compose -f src/main/docker/conf/docker-stack.yml up tms tms-ui`

There is also a performance test plan that can be started easily.

The file uses `${SRC}` environment variable for mounting a volume that point to the project codebase needed for:
- wait-for-it.sh: a script that checks the real presence of the service in the swarm
- test-plan.jmx: the test plan, present in the project codebase folder, `src/test/jmeter`
- all output reports and logs

`rm -fr target/jmeter && docker-compose -f src/main/docker/conf/docker-stack-dev.yml up tms rest-perf`

the results and reports will be placed into `target/jmeter`

here is an example of jmeter report

![img.png](docs/jmeter.png)
