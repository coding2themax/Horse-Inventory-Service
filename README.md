# Horse-Inventory-Service

Keeps horse goods and feed stock.

## Feed purchase saga

This service is the inventory step of the feed purchase saga run by
horse-store-order-service. It reads commands from the
`feed-purchase.inventory.commands` RabbitMQ queue and replies on the
`feed.purchase.saga` exchange:

| Command | What happens | Reply |
| --- | --- | --- |
| `inventory.reserve` | Holds the stock and adds the price | `saga.inventory.reserved`, or `saga.inventory.rejected` with a reason |
| `inventory.commit` | Makes the hold final after payment | `saga.inventory.committed` |
| `inventory.release` | Puts the stock back after a failed payment | `saga.inventory.released` |

Each command is keyed by saga id and safe to receive twice. Messages that keep
failing go to the `feed-purchase.dead-letter` queue.

Stock is kept in memory (`service/FeedStockService`) and resets when the service
restarts. Current stock: `GET /feed-stock`.

## Running

Needs RabbitMQ. Set `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME` and
`RABBITMQ_PASSWORD` if it is not on `localhost:5672` with `guest`/`guest`.

```bash
./mvnw spring-boot:run
```
