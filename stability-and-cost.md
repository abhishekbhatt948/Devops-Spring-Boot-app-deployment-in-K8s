Stability & Cost Considerations md file : -

Note: "written document made by kept in mind that its PUC project test env. for production ready we will implement more preciously, awaireness, cost optimization should be between mid to high (balanced) not low. and more"  

This document outlines the design decisions taken to ensure application stability, operational reliability, and cost awareness for the Spring Boot + MySQL + Kafka deployment on Kubernetes.

The focus is on practical trade-offs suitable for a Dev / QA environment, aligned with real-world DevOps practices.

1. Application Stability Across Environments
Stability is achieved by separating application concerns from infrastructure concerns.

Key measures:

Same Docker image is used across environments (Dev / QA).

Environment-specific behavior is controlled using ConfigMaps and Secrets. (In main you might see secret just for show case not in real time.)

No hardcoded environment values inside the application.

Kubernetes Namespaces are used for isolation.

This approach ensures predictable behavior and avoids configuration drift between environments.

2. Health Checks and Failure Handling

Kubernetes health probes are configured carefully to avoid false restarts during startup.

Implemented probes:

Startup Probe
Allows sufficient time for Spring Boot, database connections, and Kafka initialization.

Readiness Probe
Determines when the application is ready to receive traffic.

Liveness Probe
Ensures the container is restarted only when the application becomes unhealthy.

Dedicated actuator endpoints are used for probes to avoid dependency-related delays.

This prevents unnecessary restarts and improves runtime stability.

3. Kafka Stability (KRaft Mode)

Kafka is deployed in KRaft mode, removing the dependency on Zookeeper.

Stability benefits:

Fewer moving parts

Simpler operational model

Reduced resource usage

Faster startup and recovery

For this assignment:

Single-node Kafka is used (suitable for Dev / QA).

Topics are created automatically or via admin tooling. Note: Not Externally include here only by default.

Kafka failures do not block application startup.

This setup balances simplicity with functional correctness.

4. Database Reliability

MySQL is configured using:

Kubernetes Secrets for credentials

Init scripts for schema creation

ClusterIP service for internal access

5. Rollback and Recovery Strategy

Rollback is handled at multiple levels:

Application rollback

Kubernetes supports kubectl rollout undo

Image tags allow reverting to a known working version

Configuration rollback

ConfigMaps and Secrets can be versioned and reapplied

Kafka & MySQL

Stateful data is preserved independently of application restarts

This provides a safe recovery path without complex tooling.

6. Cost Optimization Decisions

Cost considerations were taken into account throughout the design.

Key optimizations:

Resource requests and limits defined for all containers

Single-node Kafka and MySQL for non-production environments

No over-provisioning of CPU or memory

No external managed services used for Dev setup

These choices keep infrastructure lightweight while remaining realistic.

7. CI/CD Cost Awareness

CI pipeline is designed to:

Build once, deploy many

Avoid environment-specific builds

Push Docker images only when needed

Deployment is intentionally manual for local Kubernetes to avoid unnecessary CI complexity and infrastructure costs.

8. Monitoring and Observability (Current & Future)

Current:

Kubernetes probes for health

Application logs via kubectl logs

Kafka and MySQL logs accessible per pod

Future improvements (out of scope for assignment):

Centralized logging (ELK / Loki)

Metrics via Prometheus & Grafana

Kafka consumer lag monitoring

Alerting on pod restarts and resource saturation

9. Summary

The solution prioritizes:

Stability through isolation and health management

Simplicity over unnecessary complexity

Cost awareness suitable for Dev / QA workloads

Clear rollback and recovery paths

