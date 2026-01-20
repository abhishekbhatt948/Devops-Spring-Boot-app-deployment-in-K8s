## DevOps Engineer Assignment – Spring Boot, Kafka & MySQL on Kubernetes

# GITHUB Repository Link : "https://github.com/abhishekbhatt948/Devops-Spring-Boot-app-deployment-in-K8s.git"

# How to Use: Go To Github Repository Link -> Open Project -> copy link and Clone into your Local setup.
# Prerequivisit: Docker, Kuberntes (light or high setup). i  used k3d in my local system due to limited system .

# Deployemnt inot k8s keep Manually using cmd "Kubectl apply -f k8s/dev/*" *= k8s Manifest

# CI/CD Pipeline : Github-Action

# GIT Workflow Main and Feature/*, PR, Branch Protection Rule Apply onto main.

# Overview:-

This repository contains a sample Spring Boot application deployed on Kubernetes with MySQL as the database and Apache Kafka as the messaging system (Image used here is Native which is light version just for demo purposed.).

The objective of this assignment is to demonstrate:

Containerization

CI pipeline design

Kubernetes deployment

Kafka integration (without Zookeeper)

Stability, rollback, and cost-aware decisions

The solution is designed for Dev / QA environments, focusing on clarity and correctness rather than over-engineering.

## Architecture

The application consists of three main components deployed inside Kubernetes:

# Spring Boot Application

Exposes REST APIs

Produces messages to Kafka

Connects to MySQL using environment variables

# MySQL

Runs as a Kubernetes Deployment

Credentials managed using Kubernetes Secrets

Database initialized via init scripts

# Apache Kafka

Runs in KRaft mode (no Zookeeper)

Used only as a message broker

Single-node setup suitable for non-production use

Each component is exposed internally using ClusterIP services.

## Repository Structure
.
├── app/
│   └── Spring Boot source code and Dockerfile
├── k8s/
│   ├── dev/
│   │   ├── namespace.yml
│   │   ├── mysql/
│   │   ├── kafka/
│   │   ├── spring-boot-app/
├──------ QA/ *
├── github-action.yml
├── stability-and-cost.md
└── README.md

# Application Features

REST endpoint to publish messages to Kafka

MySQL integration using JPA

Health endpoints exposed via Spring Boot Actuator

Graceful startup and recovery behavior

Configurable entirely via environment variables

# Containerization

The application is containerized using a multi-stage Docker build:

Build stage compiles the application

Runtime stage runs only the final JAR

Java 17 is used

Docker images are tagged explicitly (e.g. spring-boot-app:v*) to avoid ambiguity during deployment.

# CI Pipeline

The CI pipeline is implemented using GitHub Actions.

Pipeline Responsibilities

Checkout source code

Build the application

Run unit tests (basic)

Build Docker image

Push image to Docker Hub

Deployment Strategy

Deployment to Kubernetes is intentionally kept manual.

# Reason:

The Kubernetes cluster used for this assignment is local (k3d).

Avoids coupling CI with local cluster credentials.

Mirrors real-world setups where CI produces artifacts and CD is environment-controlled.

Manual deployment is performed using kubectl apply.

# Kubernetes Deployment
Namespaces

Each environment is isolated using Kubernetes namespaces.

Current implementation focuses on dev.

Configuration Management

ConfigMaps for non-sensitive configuration

Secrets for database credentials

Same image used across environments

Health Checks

Startup, readiness, and liveness probes are configured.

Dedicated actuator health groups are used to avoid false restarts.

External dependencies (DB, Kafka) do not block readiness.

Kafka Setup

Kafka is deployed using the apache/kafka-native image.

Runs in KRaft mode (no Zookeeper).

Single broker + controller (same pod).

Topics are auto-created on first publish (default Kafka behavior).

Kafka is accessed internally via Kubernetes service DNS.

# MySQL Setup

MySQL runs as a Deployment with persistent initialization.

Database credentials are stored in Secrets.

Schema initialization handled via init SQL scripts.

Application uses connection pooling (HikariCP).

## How to Deploy (Dev Environment)
1. Create namespace
kubectl apply -f k8s/dev/namespace.yml

2. Deploy MySQL
kubectl apply -f k8s/dev/mysql/

3. Deploy Kafka
kubectl apply -f k8s/dev/kafka/

4. Deploy Spring Boot application
kubectl apply -f k8s/dev/spring-boot-app/

# How to Test
Port-forward application
kubectl port-forward svc/spring-boot-service 8080:80 -n dev

Health check
curl http://localhost:8080/actuator/health

Publish Kafka message
curl -X POST http://localhost:8080/kafka/publish \
  -H "Content-Type: text/plain" \
  -d "Test message"


Successful execution confirms:

Application is running

Kafka connectivity works

Database connectivity is stable

Rollback Strategy

Kubernetes supports rolling updates.

Application rollback can be done using:

kubectl rollout undo deployment spring-boot-app -n dev


#Image versioning allows quick reversion.

Configurations can be reverted independently using ConfigMaps and Secrets.

Stability & Cost Considerations

Detailed stability and cost decisions are documented separately in:

---> stability-and-cost.md


This includes:

Probe strategy

Kafka mode choice

Resource limits

Cost-aware design decisions

## Assumptions & Limitations

Designed for Dev / QA, not production scale.

Single-node Kafka and MySQL.

No automated CD due to local cluster usage.

Monitoring is basic (logs and probes).

These trade-offs are intentional and documented.

## Future Improvements

-- Helm charts for deployment

-- Horizontal Pod Autoscaling

-- Centralized logging and metrics

-- Multi-node Kafka setup

-- Automated deployment for cloud clusters

## Conclusion

This assignment demonstrates a practical DevOps workflow:

Clean containerization

Stable Kubernetes deployment

Kafka integration without Zookeeper

CI pipeline focused on artifact creation

Clear rollback and cost considerations

The solution prioritizes correctness, clarity, and maintainability over unnecessary complexity.

# ------------------------------------Abhishek Bhatt ------------------------------------------