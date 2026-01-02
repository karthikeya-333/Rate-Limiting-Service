<h1>Rate Limiting Service</h1>

This repository contains a rate limiting service implemented using Spring Boot that controls how many requests an API can receive within a given time window. It is designed to protect backend services from excessive traffic and misuse by enforcing configurable request limits.

The service allows creating projects, assigning API keys to them, and defining rate-limit rules such as requests per minute, hour, or day. Each incoming request is checked against these rules before being allowed. If the request exceeds the defined limit, it is rejected.

PostgreSQL is used to store projects, API keys, and rate-limit configurations, while Redis is used to keep fast, temporary counters for request tracking.The service also supports idempotency, so repeated requests with the same idempotency key are not counted multiple times. 

Overall, this service acts as a central rate-limiting layer that can be plugged into other systems to keep APIs safe, stable, and predictable.
