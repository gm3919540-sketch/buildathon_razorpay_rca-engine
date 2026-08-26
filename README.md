# RCA Engine Backend

AI Powered Root Cause Analysis Platform Backend

RCA Engine is an AI-powered backend system that automatically detects production incidents, analyzes failures, generates Root Cause Analysis (RCA), allows human verification, and stores verified knowledge for future incident resolution.

The platform combines event-driven architecture, Large Language Models, vector search, and human feedback to create an intelligent incident management workflow.

---

# Features

## Incident Detection

The system consumes application logs through Kafka and automatically detects production failures.

Supported workflow:
```text
Application Logs

        |
        |

      Kafka

        |
        |

Incident Detection

        |
        |

Incident Creation
```

---

## Automated RCA Generation

After detecting an incident, the system generates an AI-based Root Cause Analysis using Google Gemini.

Generated RCA contains:

- Root Cause
- Evidence
- Recommended Actions
- Confidence Score

Flow:
```
Incident
|
|
Historical Knowledge Search
|
|
Gemini AI Analysis
|
|
Generated RCA
```



---

## Human Verification Workflow

AI generated RCA is verified by engineers before becoming part of the knowledge base.

Workflow:

```
AI Generated RCA
|
|
Human Review
|
|

Verified RCA

```

Stored information:

- Actual Root Cause
- Actual Resolution
- Review Status

---

## Knowledge Base with Vector Search

Verified incidents are converted into embeddings and stored in PGVector.

Future incidents can search similar historical failures.

Architecture:

```
Historical Incident

    |
    |

Embedding Generation

    |
    |

PGVector Database

    |
    |

Similarity Search

    |
    |

RCA Generation Context

```
---

# Architecture
```
            Application Logs

                   |
                   |

                Kafka

                   |
                   |

          Log Event Consumer

                   |
                   |

          Incident Processing

                   |
                   |

             PostgreSQL

                   |
                   |

            RCA Generation

                   |
                   |

            Google Gemini

                   |
                   |

          Human Verification

                   |
                   |

             PGVector Store
```

---

# Tech Stack

## Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate

## Database

- PostgreSQL
- PGVector

## Messaging

- Apache Kafka

## AI

- Spring AI
- Google Gemini
- Gemini Embeddings

## Infrastructure

````
- Docker
- Docker Compose

---

# Project Structure
```declarative

src/main/java/com/rcaengine

├── controller
│
├── service
│
├── repository
│
├── entity
│
├── dto
│
├── config
│
└── exception
```
---

# Database Design

## Service

Stores application/service information.


Service

id
name
description
environment
createdAt


---

## Incident

Stores production failures.


Incident

id
title
description
severity
status
service
startedAt
resolvedAt
createdAt


Incident lifecycle:


OPEN
|
|
RESOLVED


---

## LogEvent

Stores application logs.


LogEvent

id
fingerprint
timestamp
level
message
exceptionType
stackTrace
traceId
environment
incident


---

## Generated RCA

Stores AI and human verified analysis.


GeneratedRCA

id
rootCause
evidence
recommendedActions
confidence
reviewed
actualRootCause
actualResolution
generatedAt


---

# Kafka Pipeline

Kafka is used for asynchronous log processing.

Flow:

```declarative
Application

|
|

Kafka Topic

|
|

LogEventConsumer

|
|

LogEventService

|
|

Incident Creation
```


---

# RCA Generation Pipeline

Step-by-step:

## 1. Incident Created

An error log creates a new incident.

---

## 2. Historical Search

The system searches previous similar failures using vector similarity.

---

## 3. Gemini Analysis

Gemini receives:

- Current incident details
- Historical evidence

and generates RCA.

---

## 4. RCA Storage

Generated RCA is stored in PostgreSQL.

---

## 5. Human Review

Engineer verifies:

- Actual Root Cause
- Actual Resolution

---

## 6. Indexing

Verified RCA is stored in PGVector.

---

# API Endpoints

## RCA APIs

### Generate RCA

GET /api/rca/incidents/{incidentId}


Returns AI generated RCA report.


---

### Review RCA


PUT /api/rca/incidents/{incidentId}/review


Updates:

- Actual Root Cause
- Actual Resolution
- Review status


---

### Index RCA


POST /api/rca/incidents/{incidentId}/index


Stores verified RCA into vector database.

---

## Knowledge APIs

### Add Historical Incident


POST /api/knowledge/incidents


Adds historical incident knowledge.


---

### Search Knowledge


GET /api/knowledge/search?query=


Returns similar incidents from vector database.

---

# Configuration

Application configuration:


server.port=8080

Database:
PostgreSQL

AI:
Google Gemini

Vector Store:
PGVector


---

# Environment Variables

Required:


GOOGLE_GENAI_API_KEY


---

# Running Locally

## Requirements

Install:

- Java 21
- Maven
- Docker
- PostgreSQL
- Kafka


---

## Start Infrastructure


docker compose up


Starts:

- PostgreSQL
- Kafka
- PGVector


---

## Run Application


mvn spring-boot:run


Backend runs on:


http://localhost:8080


---

# AI Workflow Example


Error Log

↓

Kafka Event

↓

Incident Created

↓

Historical Incident Search

↓

Gemini RCA Generation

↓

Human Verification

↓

Knowledge Indexing

↓

Future RCA Improvement


---

# Future Improvements

- Multi-tenant organization support
- Advanced role based access control
- Kubernetes deployment
- Monitoring with Prometheus and Grafana
- Automated incident remediation
- Slack/Jira integrations

---

# Built With

Spring Boot + Spring AI + Kafka + PostgreSQL + PGVector + Google Gemini

Built for Razorpay Buildathon.



