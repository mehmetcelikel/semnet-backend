#!/bin/bash

function install {
  echo "installing $1 dependencies"
  cd $1 && mvn clean install -DskipTests && cd ..
}

install semnet.commons && install semnet.services &&  
cd semnet.api && mvn clean install -DskipTests -Djava.security.egd=file:/dev/./urandom
