![](https://circleci.com/gh/wiyarmir/keynotedex.svg?style=shield&circle-token=72ac43ae5f62b6afd03c960360f46d573d852f0b)

# Keynotedex

Pure Kotlin implementation of a platform to save, track and share conference submissions.

Deployment
----------

There is a `Procfile` that will JustWork(TM) on Heroku or Herokuish environments (think Dokku).

The following environment variables are used:

| Name | Value |
|------|-------|
| PORT | Port where the server will run and listen to incoming connections |
| COOKIE_KEY | Key used to encrypt the cookies set |
| ENVIRONMENT | Can be either `production` or `development`. If absent, will assume `development`. |
