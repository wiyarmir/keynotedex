[![CircleCI Badge](https://circleci.com/gh/wiyarmir/keynotedex.svg?style=shield&circle-token=72ac43ae5f62b6afd03c960360f46d573d852f0b)](https://circleci.com/gh/wiyarmir/keynotedex)

# Keynotedex

Multiplatform Kotlin implementation of a service to save, track and share conference submissions.

| Platform | Status |
|----------|--------|
| API      |   ✅   |
| Backend  |   ✅   |
| Web      |   ✅   |
| Android  |   👷   |
| iOS      |   👷   |

## Media

| Title | Venue | Slides | Video |
| --- | --- | --- | --- |
| Kotlin Beyond Android | 2018 Milan Community Kotlin Conf | https://speakerdeck.com/wiyarmir/kotlin-beyond-android | https://vimeo.com/280696828 | 
| All-Stacks Kotlin | 2019 Kotliners Budapest | https://speakerdeck.com/wiyarmir/all-stacks-kotlin | https://www.youtube.com/watch?v=31aX4sONZTs |

## Building the project

There is a Gradle task that will produce a JAR ready to go.

```bash
$ ./gradlew stage
```

The output is in `backend/build/libs/backend-release.jar`.

## Running the project

The following environment variables are recognised:

| Name | Value |
|------|-------|
| PORT | Port where the server will run and listen to incoming connections |
| COOKIE_KEY | Key used to encrypt the cookies set |
| ENVIRONMENT | Can be either `production` or `development`. If absent, will assume `development`. |

## Development

You can run the backend development server executing:

```bash
$ ./gradlew backend:run
```

This will start serving the app in port 9090 by default. 

If you want to run the frontend development server as well, you can execute:

```bash
$ ./gradlew web:run
```

This will start the webpack development server in port 8080, and proxy all calls to files it doesn't know to port 9090.

**Warning**: The webpack development server will keep running until you execute `./gradlew web:stop`.

### Hot reloading

In backend, Ktor supports hot reloading, but since the task serving the app is kept alive, you need to execute in a separate console:

```bash
$ ./gradlew backend:classes -t
```

This will recompilate classes on file changes, and Ktor will detect it and reload them on the next request it serves.

For the frontend, it's enough to execute the original run task with `-t` flag.

```bash
$ ./gradlew web:run -t
```

## Deployment

There is a `Procfile` that will JustWork™️ on Heroku or Herokuish environments (think [Dokku](https://github.com/dokku/dokku)).

## Contributing

If you would like to contribute code to this repository you can do so through GitHub by creating a new branch in the repository and sending a pull request or opening an issue. Please, remember that there are some requirements you have to pass before accepting your contribution:

* Write clean code and test it.
* The code written will have to match the product owner requirements.
* Follow the repository code style.
* Write good commit messages.
* Do not send pull requests without checking if the project build is OK in the CI environment.
* Review if your changes affects the repository documentation and update it.
* Describe the PR content and don't hesitate to add comments to explain us why you've added or changed something.

## License

    Copyright 2018 Keynotedex

    Licensed under the Apache License, Version 2.0 (the "License"); you may 
    not use this file except in compliance with the License. You may obtain a 
    copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
    License for the specific language governing permissions and limitations 
    under the License.
