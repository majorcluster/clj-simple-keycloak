# clj-simple-keycloak

![Clojars Project](https://img.shields.io/clojars/v/org.clojars.majorcluster/clj-simple-keycloak.svg) 

A Clojure library designed to authenticate 
and authorize basic permissions at an external keycloak server
Administration is so far not meant to be done here

## Usage

* Start keycloak:  
At `/docker-bakery-keycloak`
```shell
$ docker-compose up --force-recreate
```
* Add the dependency:
```clojure
[org.clojars.majorcluster/clj-simple-keycloak "LAST RELEASE NUMBER"]
```

* Configure your users, realms, clients and permissions at your `http://localhost:8090`:
  * [Keycloak docs](https://www.keycloak.org/docs/latest/authorization_services/index.html) 

* basic usage:
  * new-client-config [host realm client-id client-secret] => Builds a client map to be used on auth* methods 
  * authn!!  [kc-client-config username password] => Authenticates with username and password grant type, returning the tokens provided by keycloak. If it fails, it throws an ex-info
  * authz!!  [kc-client-config headers permission] => Authorizes user with a Authorization Bearer header (taken from `authn!!`), returning the tokens provided by keycloak. If it fails, it throws an ex-info

## Publish
### Requirements
* Leiningen (of course 😄)
* GPG (mac => brew install gpg)
* Clojars account
* Enter clojars/tokens page in your account -> generate one and use for password
```shell
export GPG_TTY=$(tty) && lein deploy clojars
```

## License
Copyright (c) 2012-2022 Mateus Carvalho

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
