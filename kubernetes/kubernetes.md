# Kubernetes

Kubernetes — это портативная расширяемая платформа с открытым исходным кодом для управления контейнеризованными рабочими
нагрузками и сервисами, которая облегчает как декларативную настройку, так и автоматизацию.

С помощью Kubernetes API задается желаемое состояние кластера:

* какие приложения или другие рабочие нагрузки нужно запустить;
* какие образы контейнеров используются;
* количество реплик;
* какие сетевые и дисковые ресурсы.

Обычно это делается через интерфейс командной строки kubectl.

После того, как желаемое состояние описано, kubernetes пытается привести текущее состояние кластера к желаемому
состоянию с помощью генератора событий жизненного цикла подов (Pod Lifecycle Event Generator). Для этого Kubernetes
автоматически выполняет множество задач, таких как запуск или перезапуск контейнеров, масштабирование количества реплик
данного приложения и многое другое. Kubernetes состоит из набора процессов, запущенных в вашем кластере.

Master node в Kubernetes — это коллекция из трех процессов, которые выполняются на одном узле в вашем кластере, который
обозначен как master: `kube-apiserver` (API-сервер), `kube-controller-manager` (менеджер контроллеров),
`kube-scheduler` (планировщик). Так же там запущено хранилище etcd. Master координирует все процессы в кластере, такие
как планирование выполнения приложений, сохранение требуемого состояния приложений, а также их масштабирование и
обновление.

Каждый отдельный неосновной узел в кластере выполняет два процесса:

* `kubelet` – получение инструкций от управляющего узла и приведение подов на данном рабочем узле в желаемое состояние;
* `kube-proxy` – сетевой прокси, который обрабатывает сетевые сервисы Kubernetes на каждом узле.

Kubernetes содержит ряд абстракций, которые представляют состояние системы: развернутые контейнеризованные приложения и
рабочие нагрузки, связанные с ними сетевые и дисковые ресурсы и другую информацию, что делает кластер.

Кластер Kubernetes состоит из набора машин, называемых нода (Node), которые запускают контейнеризированные приложения.
Каждая нода содержит поды (Pod) – минимальная сущность для развертывания в кластере. K8S управляет подами, а не
контейнерами напрямую.

Основные объекты Kubernetes включают в себя:

* `Pod` – минимальная сущность для развертывания в кластере. Каждый `Pod` предназначен для запуска одного (обычно)
  экземпляра конкретного приложения. Если есть необходимость горизонтального масштабирования, то можно запустить
  несколько экземпляров `Pod` - в терминологии Kubernetes это называется репликацией.
* `Service` – абстракция, которая определяет логический набор подов и политику доступа к ним, как сетевой сервис. `Pod`
  создаются и удаляются, чтобы поддерживать описанное состояние кластера. Каждый pod имеет свой ip-адрес, но эти адреса
  не постоянны и могут меняться со временем
  (при переезде между нодами, например).
* `Volumes` – персистентное хранилище данных внутри кластера. По-умолчанию используется `emptyDir` – volume создается на
  диске и существует до тех пор, пока `Pod` работает на этой ноде. `ConfigMaps` так же могут использоваться как volume
  для конфигурирования приложения.
* `Namespace` – виртуальные кластеры размещенные поверх физического.
* `Secrets` – используются для хранения конфиденциальной информации.

Kubernetes также содержит абстракции более высокого уровня, которые опираются на Контроллеры (`Controller`) для создания
базовых объектов и предоставляют дополнительные функциональные и удобные функции. Они включают:

* `Deployment` – обеспечивает декларативные обновления для `Pods` и `ReplicaSets`. Наиболее распространенный тип
  описания ресурсов, состоит из секции описания `Pod` (`.spec.template`), `Labels` (`.spec.template.metadata.labels`),
  информация о репликации (`.spec.replicas`).
* `DaemonSet` – гарантирует, что определенный Pod будет запущен на всех нодах.
* `StatefulSet` – используется для управления `приложениями` с сохранением состояния.
* `ReplicaSet` – гарантирует, что определенное количество экземпляров подов (Pods) будет запущено в кластере в любой
  момент времени.

Есть еще несколько служебных сущностей, которые упрощают работу с k8s:

* `Labels` – используются для маркирования объектов кластера, а так же для выбора этих
  объектов `kubectl get pods -l app=simple-backend`.
* `ConfigMaps` – абстракция над файлами конфигурации, позволяет разделять настройки приложения и сами контейнеры,
  избавляя от необходимости упаковывать конфиги в docker-образ.
* `Annotations` – используются для добавления собственных метаданных к объектам. Такие клиенты, как инструменты и
  библиотеки, могут получить эти метаданные. Эту информацию можно хранить в БД или файлах, но это усложняет процесс
  создания общих клиентских библиотек. Некоторые примеры информации, которая может быть в аннотациях:
    * Поля, управляемые декларативным уровнем конфигурации. Добавление этих полей в виде аннотаций позволяет отличать их
      от значений по умолчанию, установленных клиентами или серверами, а также от автоматически сгенерированных полей и
      полей, заданных системами автоматического масштабирования.
    * Информация репозитории, сборке, выпуске или образе.
    * Информация об источнике пользователя или инструмента/системы, например, URL-адреса связанных объектов из других
      компонентов экосистемы.

## Пример

Есть возможность конвертировать существующие Docker Compose файлы в манифесты k8s.

```shell
$ kompose convert --controller deployment --out k8s/ --with-kompose-annotation=false
WARN Service "simple-backend" won't be created because 'ports' is not specified 
INFO Kubernetes file "k8s/simple-frontend-service.yaml" created 
INFO Kubernetes file "k8s/simple-backend-deployment.yaml" created 
INFO Kubernetes file "k8s/simple-frontend-deployment.yaml" created 
```

#### Развертывание Managed k8s cluster на DigitalOcean и запуск приложений в кластере.

Создание кластера k8s в DigitalOcean с помощью [terraform](https://www.terraform.io/intro/index.html) и
[terragrunt](https://terragrunt.gruntwork.io/.

```shell
$ git clone https://github.com/Romanow/ansible-kubernetes.git

$ cd ansible-kubernetes/terraform/terragrunt

# используем terraform (tfenv) и terragrunt (tgenv)
$ tgenv install
Terragrunt v0.35.10 is already installed

$ tfenv install 1.0.11
Terraform v1.0.11 is already installed

# получаем DigitalOcean Token (https://docs.digitalocean.com/reference/api/create-personal-access-token/)
# создается k8s кластер из 3х нод 2CPU, 2Gb памяти в региона AMS, настраивается ingress (создается LoadBalancer)
# и создаются DNS записи в домене romanow-alex.ru
$ terragrunt apply
 
var.do_token
  Enter a value: <token>


Terraform used the selected providers to generate the following execution
plan. Resource actions are indicated with the following symbols:
  + create
 <= read (data resources)

Terraform will perform the following actions:

  # digitalocean_kubernetes_cluster.cluster will be created
  + resource "digitalocean_kubernetes_cluster" "cluster" {
      + cluster_subnet = (known after apply)
      + created_at     = (known after apply)
      + endpoint       = (known after apply)
      + id             = (known after apply)
      + ipv4_address   = (known after apply)
      + kube_config    = (sensitive value)
      + name           = "terragrunt-cluster"
      + region         = "ams3"
      + service_subnet = (known after apply)
      + status         = (known after apply)
      + surge_upgrade  = true
      + updated_at     = (known after apply)
      + version        = "1.20.11-do.0"
      + vpc_uuid       = (known after apply)

      + node_pool {
          + actual_node_count = (known after apply)
          + auto_scale        = false
          + id                = (known after apply)
          + name              = "worker-pool"
          + node_count        = 3
          + nodes             = (known after apply)
          + size              = "s-2vcpu-4gb"
          + tags              = [
              + "k8s-cluster",
            ]
        }
    }

  # module.nginx-ingress[0].data.kubernetes_service.nginx-ingress will be read during apply
  # (config refers to values not yet known)
 <= data "kubernetes_service" "nginx-ingress"  {
      + id     = (known after apply)
      + spec   = (known after apply)
      + status = (known after apply)

      + metadata {
          + generation       = (known after apply)
          + name             = "nginx-stable-nginx-ingress"
          + namespace        = "nginx-ingress"
          + resource_version = (known after apply)
          + uid              = (known after apply)
        }
    }

  # module.nginx-ingress[0].helm_release.ingress will be created
  + resource "helm_release" "ingress" {
      + atomic                     = false
      + chart                      = "nginx-ingress"
      + cleanup_on_fail            = false
      + create_namespace           = true
      + id                         = (known after apply)
      + manifest                   = (known after apply)
      + metadata                   = (known after apply)
      + name                       = "nginx-stable"
      + namespace                  = "nginx-ingress"
      + repository                 = "https://helm.nginx.com/stable"

      + set {
          + name  = "controller.service.annotations.service\\.beta\\.kubernetes\\.io/do-loadbalancer-certificate-id"
          + value = "d67eda2a-c1d2-4c7e-9381-48ee6be9454a"
        }
      + set {
          + name  = "controller.service.annotations.service\\.beta\\.kubernetes\\.io/do-loadbalancer-name"
          + value = "loadbalancer"
        }
      + set {
          + name  = "controller.service.httpsPort.targetPort"
          + value = "80"
        }
    }

Plan: 10 to add, 0 to change, 0 to destroy.

Do you want to perform these actions?
  Terraform will perform the actions described above.
  Only 'yes' will be accepted to approve.
Enter a value: yes 

$ doctl kubernetes cluster kubeconfig save terragrunt-cluster
Notice: Adding cluster credentials to kubeconfig file found in "/Users/aromanov/.kube/config"
Notice: Setting current-context to do-ams3-terragrunt-cluster
```

Деплой приложения в кластер.

```shell
$ kubectl apply -f postgres
service/postgres created
configmap/postgres-config created
deployment.apps/postgres created

$ kubectl apply -f simple-backend.yml 
service/simple-frontend created
deployment.apps/simple-backend created

$ kubectl apply -f simple-frontend.yml 
service/simple-frontend configured
deployment.apps/simple-frontend created

$ kubectl get pods
NAME                               READY   STATUS    RESTARTS   AGE
postgres-54df449488-wj7g2          1/1     Running   0          3m55s
simple-backend-7f4cb85ff9-c69sq    1/1     Running   0          3m28s
simple-frontend-5db47b7c64-znq5s   1/1     Running   0          96s

$ kubectl get svc -n nginx-ingress
NAME                         TYPE           CLUSTER-IP     EXTERNAL-IP     PORT(S)                      AGE
nginx-stable-nginx-ingress   LoadBalancer   10.245.98.52   167.99.16.213   80:31075/TCP,443:32052/TCP   15m

$ kubectl get ingress
NAME              CLASS    HOSTS                             ADDRESS         PORTS   AGE
ingress-service   <none>   simple-frontend.romanow-alex.ru   167.99.16.213   80      2m27s

# для использования type: LoadBalancer нужно выключить ingress
$ helm uninstall nginx-stable -n nginx-ingress
release "nginx-stable" uninstalled

$ kubectl apply -f loadbalancer.yml 
service/simple-frontend configured

# для simple-frontend будет создан новый физический LoadBalancer, поэтому в DNS нужно будет поменять ip адрес
$ kubectl get services 
NAME              TYPE           CLUSTER-IP       EXTERNAL-IP                       PORT(S)         AGE
kubernetes        ClusterIP      10.245.0.1       <none>                            443/TCP         27m
postgres          ClusterIP      10.245.172.128   <none>                            5432/TCP        12m
simple-backend    ClusterIP      10.245.241.29    <none>                            8080/TCP        9m12s
simple-frontend   LoadBalancer   10.245.138.82    simple-frontend.romanow-alex.ru   443:31849/TCP   12m
```

Открыть в браузере `https://simple-frontend.romanow-alex.ru`

### Установка с помощью Helm

```shell
$ cd examples/helm

# устанавливаем postgres
$ helm install postgres postgres-chart/
NAME: postgres
LAST DEPLOYED: Wed Dec 15 12:33:33 2021
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None

# обновление postgres
$ helm upgrade \
  --set image.version=14 \
  --description 'Increment version' \
  postgres postgres-chart/
  
Release "postgres" has been upgraded. Happy Helming!
NAME: postgres
LAST DEPLOYED: Wed Dec 15 12:57:32 2021
NAMESPACE: default
STATUS: deployed
REVISION: 2
TEST SUITE: None

# получение истории изменений postgres
$ helm history postgres
REVISION	UPDATED                 	STATUS  	CHART         	APP VERSION	DESCRIPTION     
1       	Wed Dec 15 12:33:33 2021	superseded	postgres-1.0.0	           	Install complete
2       	Wed Dec 15 12:57:32 2021	deployed  	postgres-1.0.0	           	Increment version

# тестовый запуск services (frontend + backend), без применения изменений на кластере
$ helm install services services-chart/ --debug --dry-run

# вывод манифестов для отладки шаблонов
$ helm template services services-chart/ --debug

# деплоим frontend и backend в кластер
$ helm install services services-chart/
NAME: services
LAST DEPLOYED: Wed Dec 15 13:00:01 2021
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None

# удаление services
$ helm uninstall services
release "services" uninstalled
```

## Литература

2. [Helm](https://helm.sh/docs/)
1. [Kompose User Guide](https://kompose.io/user-guide/)
