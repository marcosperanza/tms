### Deployment

For deploiyng in production or stage you can choose multiple ways: Docker/swarm or k8s. Some scripts have been created to
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

You need a production k8s cluster. i.e. Oracle Kubernates Cluster Service

```
kubectl apply -f https://raw.githubusercontent.com/marcosperanza/tms/master/src/main/k8s/k8s-tms-prod.deployment.yml
kubectl expose deployment tms --port=80 --type=LoadBalancer
```

inspect the loadbalancer for discovering the public IP. The UI will be exposed on the `http://<public_ip>`

```
kubectl get service tms
```

