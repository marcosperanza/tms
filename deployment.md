### Deployment

For deploiyng in production or stage you can choose multiple ways: Docker/swarm or k8s. Some scripts have been created to
deploy a default configuration.

All project docker images are published into [Docker Hub](https://hub.docker.com/u/marcosperanza79)

- [TMS](https://hub.docker.com/u/marcosperanza79/tms)
- [TMS-UI](https://hub.docker.com/u/marcosperanza79/tms-ui)

_The docker images will be published by github workflows_

#### Docker/SWARM services

The docker compose files use an `src/main/docker/conf/env.prod` file for defining the production coordinate. 

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

#### Kubernetes

You need a production k8s cluster. i.e. Oracle Kubernetes Cluster Service

```
kubectl create secret generic database-access \
  --from-literal=username=sa \
  --from-literal=password=sa \
  --from-literal=driver=org.h2.Driver \
  --from-literal=url=jdbc:h2:./target/db

kubectl apply -f https://raw.githubusercontent.com/marcosperanza/tms/master/src/main/k8s/k8s-tms-prod.deployment.yml
kubectl expose deployment tms --port=80 --type=LoadBalancer
```

inspect the loadbalancer for discovering the public IP. The UI will be exposed on the `http://<public_ip>`

```
kubectl get service tms
```


