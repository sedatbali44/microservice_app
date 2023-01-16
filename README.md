

 create / update / delete operation on a "Person" and "Customer" entity will result in the creation of a CloudEvent 1.0 compliant message in Kafka. There are 3 topics that will 

* personevents-created
* personevents-changed
* personevents-deleted



Subscribe to Contact Updates from Contact Service

For now the service only stores a snapshot of the referenced contact persons. This means the local database is out of sync when changes are made to contacts post order creation / manipulation. To ensure eventual consistency, you should now subscribe to the respective person events (topics personevents-created, personevents-changed, personevents-deleted) and ensure the concerned orders reflect the updates. This behavior 






