# TMS-UI (Task Management System)

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app), [Redux Thunk](https://redux.js.org/)
and [Typescript](https://www.typescriptlang.org/).

It manages a simple task manager based on [TMS](https://github.com/marcosperanza/tms) server.

## Code contribution and design

The project is developed based on the [redux thunk](https://redux.js.org/usage/writing-logic-thunks) framework.
It uses middleware dispatchers for creating side-effects to the actions and retrieve data from the APIs.

## Folder structure

- **\_\_test\_\_**: contains all unit tests
- **components**: contains the ui component
- **containers**: contains the connection function between redux and simple component.
- **open-api**: the json open api needed for generating the service code
- **store**: redux reducers and action creators

## UI library

A simple UI library has been used for widgets rendering: [Primereact](https://www.primefaces.org/primereact).
The main usage is the [PrimeFlex](https://www.primefaces.org/primeflex/) library that gives some simple css classes for
using the _flexbox_, _borders_ and _spicing_


## Open API

TMS server exposes a JSON representative swagger structure that is used for creating services based on `axios`
Follow https://github.com/marcosperanza/tms#open-api for extracting the updated APIs.

This project reads the open APIs json file directly on this folder: `src/open-api/tms-api-v1.json`. In order to generate the services run this

```
npm run generate
```


## Docker

The docker image is based on _nginx:1.19.10_ image. The main idea is to use nginx to publish the webapp and redirect the requests to the backand.
Into the project folder `nginx` there is a default template for configuring nginx.
It uses an envinonment variable `TMS_SERVICES` that must be used for redirect the request to the right backend server.

[See an example here](https://github.com/marcosperanza/tms/blob/6b8f62a0768ece2f83fe1bc9eacc6bd74644809f/src/main/docker/docker-compose.yml#L13)


```
npm install
npm run build
docker build -t marcosperanza79/tms-ui:latest -f Dockerfile .
```



