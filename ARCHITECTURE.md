# 🏗️ Architecture Détaillée du Système

## Vue d'Ensemble de l'Architecture

![1](assets/architechture/1.png)

---

## Flux de Requêtes

### Scénario 1: Lister les Keynotes

![2](assets/architechture/2.png)

### Scénario 2: Créer une Conférence avec Enrichissement Keynote

![3](assets/architechture/3.png)

### Scénario 3: Circuit Breaker en Action

![4](assets/architechture/4.png)

---

## Architecture de Sécurité

![5](assets/architechture/5.png)

---

## Architecture des Données

### Keynote Service - keynote-db

```sql
TABLE: keynotes
┌────────────┬──────────┬──────────┬───────────────────────┬─────────────┐
│ id (PK)    │ nom      │ prenom   │ email (UNIQUE)        │ fonction    │
├────────────┼──────────┼──────────┼───────────────────────┼─────────────┤
│ BIGINT     │ VARCHAR  │ VARCHAR  │ VARCHAR               │ VARCHAR     │
│ AUTO_INC   │ NOT NULL │ NOT NULL │ NOT NULL              │ NOT NULL    │
└────────────┴──────────┴──────────┴───────────────────────┴─────────────┘
```

### Conference Service - conference-db

![6](assets/architechture/6.png)

---

## Pattern de Communication

### Synchrone (REST + OpenFeign)

![7](assets/architechture/7.png)

---

## Déploiement Docker

![8](assets/architechture/8.png)

---

## Résilience et Fault Tolerance

### Niveaux de Résilience

![9](assets/architechture/9.png)

---

## Monitoring et Observabilité

### Endpoints Disponibles

```
Service          Actuator                    Swagger                    H2 Console
─────────────────────────────────────────────────────────────────────────────────
Discovery        :8761/actuator              N/A                        N/A
Config           :8888/actuator              N/A                        N/A
Gateway          :8080/actuator              N/A                        N/A
Keynote          :8081/actuator              :8081/swagger-ui.html      :8081/h2-console
Conference       :8082/actuator              :8082/swagger-ui.html      :8082/h2-console
```

### Métriques Importantes

```
GET /actuator/health        → État du service
GET /actuator/info          → Informations du service
GET /actuator/metrics       → Métriques détaillées
GET /actuator/env           → Variables d'environnement
```

---

Ce document fournit une vue complète de l'architecture du système. Pour plus de détails techniques, consultez les autres documents:
- README.md
