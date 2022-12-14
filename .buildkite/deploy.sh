wget https://get.helm.sh/helm-v3.9.0-linux-amd64.tar.gz
tar -zxvf helm-v3.9.0-linux-amd64.tar.gz
./linux-amd64/helm version

aws eks --region ap-southeast-1 update-kubeconfig --name eks-raksit31667

./linux-amd64/helm upgrade --install example-spring-loyalty-api-service ./api-service/helm --set image.tag=${SEMANTIC_VERSION}
