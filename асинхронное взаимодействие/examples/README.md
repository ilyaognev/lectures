# Kafka

### Сборка

```shell
$ ./gradlew clean build
```

### Запуск

Запускаем с профилем do, kafka развернута в кластере на DigitalOcean. Для локального тестирования можно развернуть в
VirtualBox с помощью Vagrant.

Скрипты развертывания VM и установки Kafka находятся в
репозитории [ansible-kafka](https://github.com/Romanow/ansible-kafka).

Запускаем producer:

```shell
$ ./gradlew kafka-producer:bootRun

r.r.k.producer.KafkaProducerApplication  : Starting KafkaProducerApplication using Java 11.0.2 on aromanov.local with PID 74526 (/Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-producer/build/classes/java/main started by aromanov in /Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-producer)
r.r.k.producer.KafkaProducerApplication  : The following profiles are active: do

   ....

r.r.k.producer.KafkaProducerApplication  : Started KafkaProducerApplication in 1.247 seconds (JVM running for 1.49)
   
   ....
   
o.a.kafka.common.utils.AppInfoParser     : Kafka version: 3.0.0
o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 8cb0a5e9d3441962
o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1639650985032
org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: 3HSoxaVGTaOit7TpBQgV-A
r.r.k.producer.KafkaProducerApplication  : Send data to topic my-topic: 'Hello from producer: 'YJjPdtbqwY''
r.r.k.producer.KafkaProducerApplication  : Send data to topic my-topic: 'Hello from producer: 'KGFjJyuypX''
```

Запускаем два consumer (в разных терминалах), оба подключаются к топику `my-topic` и работают в единой Consumer
Group `kafka-consumer`.

Первый терминал:

```shell
$ ./gradlew kafka-consumer:bootRun

r.r.k.consumer.KafkaConsumerApplication  : Starting KafkaConsumerApplication using Java 11.0.2 on aromanov.local with PID 74573 (/Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-consumer/build/classes/java/main started by aromanov in /Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-consumer)
r.r.k.consumer.KafkaConsumerApplication  : The following profiles are active: do

   ....

o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Subscribed to topic(s): my-topic
r.r.k.consumer.KafkaConsumerApplication  : Started KafkaConsumerApplication in 1.004 seconds (JVM running for 1.29)
org.apache.kafka.clients.Metadata        : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Cluster ID: 3HSoxaVGTaOit7TpBQgV-A
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Discovered group coordinator 188.166.84.184:9092 (id: 2147483645 rack: null)
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Request joining group due to: need to re-join with the given member-id
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Successfully joined group with generation Generation{generationId=5, memberId='consumer-kafka-consumer-1-3770fe74-3d9b-48bd-8d0f-64c4ba413472', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Finished assignment for group at generation 5: {consumer-kafka-consumer-1-3770fe74-3d9b-48bd-8d0f-64c4ba413472=Assignment(partitions=[my-topic-0, my-topic-1])}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Successfully synced group in generation Generation{generationId=5, memberId='consumer-kafka-consumer-1-3770fe74-3d9b-48bd-8d0f-64c4ba413472', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Notifying assignor about the new Assignment(partitions=[my-topic-0, my-topic-1])
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Adding newly assigned partitions: my-topic-0, my-topic-1
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Setting offset for partition my-topic-0 to the committed offset FetchPosition{offset=0, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[188.166.32.237:9092 (id: 1 rack: null)], epoch=0}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Setting offset for partition my-topic-1 to the committed offset FetchPosition{offset=0, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[188.166.84.184:9092 (id: 2 rack: null)], epoch=0}}
o.s.k.l.KafkaMessageListenerContainer    : kafka-consumer: partitions assigned: [my-topic-0, my-topic-1]
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '9fbcf9f1-0533-4646-8af3-ae7ea164b967', partition 1): 'Hello from producer: 'YJjPdtbqwY''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '472ab571-4ed9-4ad7-a535-b61f2e355b3e', partition 0): 'Hello from producer: 'KGFjJyuypX''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '7f1ca9b6-5634-4c91-91f1-eb01c339773a', partition 1): 'Hello from producer: 'riqvLwgrHH''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: 'b3989b37-6e1d-4d0d-8999-c340b0d22545', partition 0): 'Hello from producer: 'XCpugxCHpL''

```

После запуска второго consumer, ему будет выделена Partition 0, а на первом произойдет перебалансировка и ему останется
только Partition 1.

```shell
$ ./gradlew kafka-consumer:bootRun

r.r.k.consumer.KafkaConsumerApplication  : Starting KafkaConsumerApplication using Java 11.0.2 on aromanov.local with PID 74573 (/Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-consumer/build/classes/java/main started by aromanov in /Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-consumer)
r.r.k.consumer.KafkaConsumerApplication  : The following profiles are active: do

   ....


o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Subscribed to topic(s): my-topic
r.r.k.consumer.KafkaConsumerApplication  : Started KafkaConsumerApplication in 0.851 seconds (JVM running for 1.131)
org.apache.kafka.clients.Metadata        : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Cluster ID: 3HSoxaVGTaOit7TpBQgV-A
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Discovered group coordinator 188.166.84.184:9092 (id: 2147483645 rack: null)
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Request joining group due to: need to re-join with the given member-id
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Successfully joined group with generation Generation{generationId=9, memberId='consumer-kafka-consumer-1-b826eddb-8a84-4540-9cd6-24eaa80f8890', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Finished assignment for group at generation 9: {consumer-kafka-consumer-1-b826eddb-8a84-4540-9cd6-24eaa80f8890=Assignment(partitions=[my-topic-0]), consumer-kafka-consumer-1-d4adb74d-4e4a-40c3-ad5c-26a0fe03701b=Assignment(partitions=[my-topic-1])}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Successfully synced group in generation Generation{generationId=9, memberId='consumer-kafka-consumer-1-b826eddb-8a84-4540-9cd6-24eaa80f8890', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Notifying assignor about the new Assignment(partitions=[my-topic-0])
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Adding newly assigned partitions: my-topic-0
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Setting offset for partition my-topic-0 to the committed offset FetchPosition{offset=0, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[188.166.32.237:9092 (id: 1 rack: null)], epoch=0}
o.s.k.l.KafkaMessageListenerContainer    : kafka-consumer: partitions assigned: [my-topic-0]
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: 'cccca17e-3b0c-4566-9802-67216f7e45e2', partition 0): 'Hello from producer: 'jpYSFvKqUm''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: 'c143c94e-2dd3-47fe-b346-6b7be3bc49ad', partition 0): 'Hello from producer: 'LpHrsVTIwD''
```

На первом consumer:

```shell
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Request joining group due to: group is already rebalancing
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Revoke previously assigned partitions my-topic-0, my-topic-1
o.s.k.l.KafkaMessageListenerContainer    : kafka-consumer: partitions revoked: [my-topic-0, my-topic-1]
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Successfully joined group with generation Generation{generationId=11, memberId='consumer-kafka-consumer-1-d4adb74d-4e4a-40c3-ad5c-26a0fe03701b', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Finished assignment for group at generation 11: {consumer-kafka-consumer-1-091a75a7-03d4-43f4-bab7-106f1ad85c20=Assignment(partitions=[my-topic-0]), consumer-kafka-consumer-1-d4adb74d-4e4a-40c3-ad5c-26a0fe03701b=Assignment(partitions=[my-topic-1])}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Successfully synced group in generation Generation{generationId=11, memberId='consumer-kafka-consumer-1-d4adb74d-4e4a-40c3-ad5c-26a0fe03701b', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Notifying assignor about the new Assignment(partitions=[my-topic-1])
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Adding newly assigned partitions: my-topic-1
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-1, groupId=kafka-consumer] Setting offset for partition my-topic-1 to the committed offset FetchPosition{offset=92, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[188.166.84.184:9092 (id: 2 rack: null)], epoch=0}}
o.s.k.l.KafkaMessageListenerContainer    : kafka-consumer: partitions assigned: [my-topic-1]
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '3dd1b0c7-a434-49a7-a928-f695baee14aa', partition 1): 'Hello from producer: 'IUPtYXBvFq''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '4b1f5408-5275-4e38-8db8-8d92eb8da72f', partition 1): 'Hello from producer: 'ZsfaEKgJVS''
```

Если мы запускаем consumer в разных Consumer Groups, то кажый consumer будет вычитывать сообщения из всех партиций:

```shell
./gradlew kafka-consumer:bootRun --args='--spring.kafka.consumer.group-id=kafka-consumer-new'

r.r.k.consumer.KafkaConsumerApplication  : Starting KafkaConsumerApplication using Java 11.0.2 on aromanov.local with PID 74807 (/Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-consumer/build/classes/java/main started by aromanov in /Users/aromanov/Develop/inst/lectures/асинхронное взаимодействие/examples/kafka-consumer)
r.r.k.consumer.KafkaConsumerApplication  : The following profiles are active: do

   ....
   
o.a.k.clients.consumer.KafkaConsumer     : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Subscribed to topic(s): my-topic
r.r.k.consumer.KafkaConsumerApplication  : Started KafkaConsumerApplication in 0.919 seconds (JVM running for 1.194)
org.apache.kafka.clients.Metadata        : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Cluster ID: 3HSoxaVGTaOit7TpBQgV-A
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Discovered group coordinator 188.166.32.237:9092 (id: 2147483646 rack: null)
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Request joining group due to: need to re-join with the given member-id
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] (Re-)joining group
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Successfully joined group with generation Generation{generationId=1, memberId='consumer-kafka-consumer-new-1-75fc3566-aecb-4ffa-b379-2c49a9bf45bc', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Finished assignment for group at generation 1: {consumer-kafka-consumer-new-1-75fc3566-aecb-4ffa-b379-2c49a9bf45bc=Assignment(partitions=[my-topic-0, my-topic-1])}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Successfully synced group in generation Generation{generationId=1, memberId='consumer-kafka-consumer-new-1-75fc3566-aecb-4ffa-b379-2c49a9bf45bc', protocol='range'}
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Notifying assignor about the new Assignment(partitions=[my-topic-0, my-topic-1])
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Adding newly assigned partitions: my-topic-0, my-topic-1
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Found no committed offset for partition my-topic-0
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Found no committed offset for partition my-topic-1
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Found no committed offset for partition my-topic-0
o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Found no committed offset for partition my-topic-1
o.a.k.c.c.internals.SubscriptionState    : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Resetting offset for partition my-topic-0 to position FetchPosition{offset=34, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[188.166.32.237:9092 (id: 1 rack: null)], epoch=0}}
o.a.k.c.c.internals.SubscriptionState    : [Consumer clientId=consumer-kafka-consumer-new-1, groupId=kafka-consumer-new] Resetting offset for partition my-topic-1 to position FetchPosition{offset=107, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[188.166.84.184:9092 (id: 2 rack: null)], epoch=0}}.
o.s.k.l.KafkaMessageListenerContainer    : kafka-consumer-new: partitions assigned: [my-topic-0, my-topic-1]
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: 'd1e721b9-cca5-4281-8650-f2e8340e6bd0', partition 0): 'Hello from producer: 'WxABvspqnI''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '0d8528db-c908-4604-bb40-dbac37dc4a33', partition 0): 'Hello from producer: 'AOjaCjJtXT''
r.r.k.consumer.KafkaConsumerApplication  : Received message (key: '2a59c153-8125-4e4d-81d3-264a94e898a7', partition 1): 'Hello from producer: 'MUDDjrsODJ''
```