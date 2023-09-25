# debezium-mysql-to-elastic


## Kurulum

```bash
export DEBEZIUM_VERSION=latest
docker-compose up
```

## Elastic sink connector kurulum

```bash
curl -X POST http://localhost:8083/connectors -H 'Content-Type: application/json' -d \
'{
  "name": "elasticsearch-sink",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "customers",
    "key.ignore": "true",
    "schema.ignore": "true",
    "connection.url": "http://elastic:9200",
    "type.name": "_doc",
    "name": "elasticsearch-sink",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable": "false"
  }
}'
```

## Debezium mysql source connector kurulum

```bash
curl -i -X POST -H "Accept:application/json" \
    -H  "Content-Type:application/json" http://localhost:8083/connectors/ \
    -d '{
            "name": "inventory-connector",
            "config": {
                "connector.class": "io.debezium.connector.mysql.MySqlConnector",
                "tasks.max": "1",
                "topic.prefix": "dbserver1",
                "database.hostname": "mysql",
                "database.port": "3306",
                "database.user": "debezium",
                "database.password": "dbz",
                "database.server.id": "184054",
                "database.include.list": "inventory",
                "schema.history.internal.kafka.bootstrap.servers": "kafka:9092",
                "schema.history.internal.kafka.topic": "schema-changes.inventory",
                "transforms": "route",
                "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
                "transforms.route.regex": "([^.]+)\\.([^.]+)\\.([^.]+)",
                "transforms.route.replacement": "$3"
            }
        }'
```

## Test db

```bash
docker-compose exec mysql bash -c 'mysql -u $MYSQL_USER  -p$MYSQL_PASSWORD inventory -e "select * from customers"'
```

## mysql yeni kayıt ekleme

```bash
docker-compose exec mysql bash -c 'mysql -u $MYSQL_USER  -p$MYSQL_PASSWORD inventory'
```

```sql
INSERT INTO `customers` (`first_name`, `last_name`, `email`) VALUES ('Ahmet', 'Yılmaz', 'fozmen');
```

## Elasticdeki kayıtları listeleme

```bash
curl -X GET "localhost:9200/customers/_search?pretty"
```

