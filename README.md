# TP4 : Système de Gestion de Conférences
## Architecture Microservices avec Spring Cloud et Angular

---

- **Réalisé par :** Salah-Eddine JAOUAD  
- **Encadré par :** Mohamed YOUSSFI   
- **Module :** Systèmes Distribués & Parallèles et Sécurité

---

## 📑 Table des Matières

1. [Introduction](#introduction)
2. [Architecture du Projet](#architecture-du-projet)
3. [Technologies Utilisées](#technologies-utilisées)
4. [Structure du Projet](#structure-du-projet)
5. [Installation et Configuration](#installation-et-configuration)
6. [Démarrage de l'Application](#démarrage-de-lapplication)
7. [Configuration Keycloak](#configuration-keycloak)
8. [Tests et Démonstration](#tests-et-démonstration)
9. [Conclusion](#conclusion)

---

## 1. Introduction

### 1.1 Contexte du Projet

Ce projet consiste à développer une application complète de gestion de conférences basée sur une **architecture microservices**. L'application permet de gérer des keynotes (intervenants) et des conférences avec leurs reviews (évaluations).

### 1.2 Objectifs

- Créer une architecture microservices avec Spring Cloud
- Implémenter des services de découverte et de configuration
- Développer une API Gateway pour le routage
- Sécuriser l'application avec OAuth2/OIDC (Keycloak)
- Intégrer des mécanismes de résilience (Circuit Breakers)
- Développer un frontend moderne avec Angular
- Documenter les APIs avec OpenAPI/Swagger
- Déployer avec Docker et Docker Compose

### 1.3 Fonctionnalités Principales

- **Gestion des Keynotes** : CRUD complet (Créer, Lire, Modifier, Supprimer)  
- **Gestion des Conférences** : CRUD avec types (Académique/Commerciale)  
- **Gestion des Reviews** : Évaluations avec notation (1-5 étoiles)  
- **Sécurité** : Authentification et autorisation OAuth2  
- **Résilience** : Circuit breakers pour la tolérance aux pannes  
- **Documentation** : Swagger UI pour tester les APIs  

---

## 2. Architecture du Projet

### 2.1 Schéma d'Architecture Globale

![Architecture Globale](assets/architechture.png)

### 2.2 Description des Services

| Service | Port | Rôle | Technologie |
|---------|------|------|-------------|
| **Discovery Service** | 8761 | Enregistrement et découverte des services | Eureka Server |
| **Config Service** | 8888 | Configuration centralisée | Spring Cloud Config |
| **Gateway Service** | 8080 | Point d'entrée unique, routage, sécurité | Spring Cloud Gateway |
| **Keynote Service** | 8081 | Gestion des intervenants | Spring Boot + JPA |
| **Conference Service** | 8082 | Gestion des conférences et reviews | Spring Boot + JPA + OpenFeign |
| **Angular Frontend** | 4200 | Interface utilisateur | Angular 17 |
| **Keycloak** | 8090 | Serveur d'authentification | Keycloak 23 |

---

## 3. Technologies Utilisées

### 3.1 Backend

| Technologie | Version | Usage |
|------------|---------|-------|
| **Java** | 21 | Langage de programmation |
| **Spring Boot** | 3.5.7 | Framework application |
| **Spring Cloud** | 2025.0.0 | Microservices |
| **Eureka** | 2025.0.0 | Service Discovery |
| **Spring Cloud Gateway** | 2025.0.0 | API Gateway |
| **Spring Cloud Config** | 2025.0.0 | Configuration centralisée |
| **OpenFeign** | 2025.0.0 | Client REST déclaratif |
| **Resilience4J** | Latest | Circuit Breakers |
| **Spring Security OAuth2** | 3.5.7 | Sécurité |
| **H2 Database** | Runtime | Base de données en mémoire |
| **Lombok** | Latest | Réduction du code boilerplate |
| **SpringDoc OpenAPI** | 2.3.0 | Documentation API (Swagger) |
| **Maven** | 3.8+ | Gestionnaire de dépendances |

### 3.2 Frontend

| Technologie | Version | Usage |
|------------|---------|-------|
| **Angular** | 17 | Framework frontend |
| **TypeScript** | 5.2 | Langage de programmation |
| **RxJS** | 7.8 | Programmation réactive |
| **HTML/CSS** | - | Interface utilisateur |

### 3.3 Infrastructure

| Technologie | Version | Usage |
|------------|---------|-------|
| **Docker** | Latest | Conteneurisation |
| **Docker Compose** | Latest | Orchestration des conteneurs |
| **Keycloak** | 23.0 | Serveur d'authentification |
| **Nginx** | Alpine | Serveur web (pour Angular) |

---

## 4. Structure du Projet

### 4.1 Arborescence Générale

```
conference-management/
├── discovery-service/          # Eureka Server
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── config-service/             # Spring Cloud Config
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── gateway-service/            # API Gateway
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── keynote-service/            # Service Keynote
│   ├── src/
│   │   └── main/
│   │       ├── java/ma/jaouad/keynoteservice/
│   │       │   ├── entity/         # Keynote.java
│   │       │   ├── repository/     # KeynoteRepository.java
│   │       │   ├── service/        # KeynoteService.java
│   │       │   ├── dto/           # KeynoteDTO.java
│   │       │   ├── mapper/        # KeynoteMapper.java
│   │       │   ├── controller/    # KeynoteRestController.java
│   │       │   └── config/        # SecurityConfig, OpenAPIConfig
│   │       └── resources/
│   │           └── application.properties
│   ├── pom.xml
│   └── Dockerfile
│
├── conference-service/         # Service Conférence
│   ├── src/
│   │   └── main/
│   │       ├── java/ma/jaouad/conferenceservice/
│   │       │   ├── entity/         # Conference, Review, TypeConference
│   │       │   ├── repository/     # ConferenceRepository, ReviewRepository
│   │       │   ├── service/        # ConferenceService, ReviewService
│   │       │   ├── dto/           # DTOs
│   │       │   ├── mapper/        # Mappers
│   │       │   ├── controller/    # Controllers
│   │       │   ├── client/        # KeynoteRestClient (OpenFeign)
│   │       │   └── config/        # Config
│   │       └── resources/
│   │           └── application.properties
│   ├── pom.xml
│   └── Dockerfile
│
├── angular-front-app/          # Frontend Angular
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/    # Home, KeynoteList, ConferenceList
│   │   │   ├── services/      # KeynoteService, ConferenceService
│   │   │   ├── models/        # Models TypeScript
│   │   │   ├── app.component.ts
│   │   │   └── app.routes.ts
│   │   ├── index.html
│   │   ├── main.ts
│   │   └── styles.css
│   ├── package.json
│   ├── angular.json
│   ├── Dockerfile
│   └── nginx.conf
│
├── docker-compose.yml          # Orchestration Docker
├── pom.xml                     # Parent POM
├── README.md                   # Ce fichier
├── KEYCLOAK-SETUP.md          # Guide Keycloak
├── DEPLOYMENT.md              # Guide déploiement
├── TESTING-GUIDE.md           # Guide de tests
├── ARCHITECTURE.md            # Architecture détaillée
└── SYNTHESE.md                # Synthèse du projet
```

---

## 5. Installation et Configuration

### 5.1 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- ✅ **IntelliJ IDEA** : [Télécharger IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community ou Ultimate)
- ✅ **Java 21** : [Télécharger Java](https://www.oracle.com/java/technologies/downloads/)
- ✅ **Docker Desktop** : [Télécharger Docker](https://www.docker.com/products/docker-desktop/)

### 5.2 Vérification des Installations

Ouvrir un terminal et vérifier les versions :

```powershell
# Vérifier Java
java -version
# Doit afficher : java version "21.x.x"

# Vérifier Docker
docker --version
docker-compose --version
```

### 5.3 Ouverture du Projet dans IntelliJ IDEA

1. Lancer **IntelliJ IDEA**
2. Cliquer sur **Open**
3. Naviguer vers le dossier `conference-management`
4. Sélectionner le fichier `pom.xml` à la racine
5. Cliquer sur **Open as Project**
6. IntelliJ détectera automatiquement qu'il s'agit d'un projet Maven multi-modules

#### Configuration Initiale dans IntelliJ

1. **Configurer le JDK** :
   - Aller dans **File** → **Project Structure** (Ctrl+Alt+Shift+S)
   - Dans **Project Settings** → **Project**
   - Sélectionner **SDK** : Java 21
   - Cliquer sur **Apply**



2. **Maven Auto-Import** :
   - IntelliJ détectera automatiquement le projet Maven
   - Attendre que l'indexation et le téléchargement des dépendances se terminent
   - Vérifier l'onglet **Maven** sur le côté droit

---

## 6. Démarrage de l'Application

### 6.1 Compilation du Projet avec IntelliJ IDEA

1. Ouvrir l'onglet **Maven** sur le côté droit d'IntelliJ
2. Dérouler le projet **conference-management**
3. Dérouler **Lifecycle**
4. Double-cliquer sur **clean**
5. Double-cliquer sur **install** (cela compile tous les services et crée les JARs)

![0](assets/screenshots/0.png)
![1](assets/screenshots/1.png)
![2](assets/screenshots/2.png)

**Remarque** : L'option `-DskipTests` est déjà configurée si nécessaire, mais avec Spring Boot 3.5.7 et Spring Cloud 2025.0.0, les tests passent correctement.

### 6.2 Démarrage avec Docker Compose

C'est la méthode recommandée qui démarre tous les services en une seule commande.

#### Étape 1 : Vérifier que les JARs sont compilés

Les JARs doivent être présents dans les dossiers `target/` de chaque service après la compilation Maven.

#### Étape 2 : Démarrer tous les services avec Docker Compose

Ouvrir le terminal intégré dans IntelliJ (Alt+F12) et exécuter :

```powershell
docker-compose up --build
```

![3](assets/screenshots/3.png)
![4](assets/screenshots/4.png)
![5](assets/screenshots/5.png)

#### Étape 3 : Vérifier que tous les conteneurs sont démarrés

Dans le terminal IntelliJ :

```powershell
docker ps
```

Vous devriez voir 7 conteneurs en cours d'exécution :
- keycloak
- discovery-service
- config-service
- keynote-service
- conference-service
- gateway-service
- angular-frontend

![7](assets/screenshots/7.png)
![8](assets/screenshots/8.png)

### 6.3 Vérification du Démarrage

#### Vérifier Eureka Dashboard

Ouvrir le navigateur et accéder à : **http://localhost:8761**

Vous devriez voir tous les services enregistrés :
- KEYNOTE-SERVICE
- CONFERENCE-SERVICE
- GATEWAY-SERVICE
- CONFIG-SERVICE

![9](assets/screenshots/9.png)

#### Vérifier l'Application Frontend

Ouvrir : **http://localhost:4200**

![10](assets/screenshots/10.png)

### 6.4 Commandes Docker Utiles

```powershell
# Arrêter tous les services
docker-compose down

# Voir les logs d'un service spécifique
docker-compose logs -f keynote-service

# Redémarrer un service spécifique
docker-compose restart keynote-service

# Reconstruire et redémarrer un service
docker-compose up --build keynote-service
```

---

## 7. Configuration Keycloak

### 7.1 Accès à la Console Keycloak

1. Ouvrir : **http://localhost:8090**
2. Cliquer sur **"Administration Console"**
3. Se connecter :
   - **Username** : `admin`
   - **Password** : `admin`

![11](assets/screenshots/12.png)
![12](assets/screenshots/13.png)

### 7.2 Création du Realm

1. Cliquer sur le menu déroulant **"Master"** en haut à gauche
2. Cliquer sur **"Create Realm"**
3. Remplir :
   - **Realm name** : `conference-realm`
   - **Enabled** : `ON`
4. Cliquer sur **"Create"**

![13](assets/screenshots/14.png)

### 7.3 Création du Client Backend

1. Dans le realm `conference-realm`, Aller dans **Clients** → **Create client**
2. Configuration initiale :
   - **Client ID** : `conference-services`
   - **Client Protocol** : `openid-connect`
   - Cliquer sur **"Next"**

3. Capability config :
   - **Client authentication** : `ON`
   - **Authorization** : `OFF`
   - **Authentication flow** : Cocher **"Service accounts roles"**
   - Cliquer sur **"Next"**

4. Login settings :
   - **Valid redirect URIs** :
     ```
     http://localhost:8080/*
     http://localhost:8081/*
     http://localhost:8082/*
     ```
   - **Web origins** : `*`
   - Cliquer sur **"Save"**

![14](assets/screenshots/15.png)
![15](assets/screenshots/16.png)
![16](assets/screenshots/17.png)
![17](assets/screenshots/18.png)
![18](assets/screenshots/19.png)

### 7.4 Création du Client Frontend

1. **Clients** → **Create client**
2. Configuration initiale :
   - **Client ID** : `angular-frontend`
   - Cliquer sur **"Next"**

3. Capability config :
   - **Client authentication** : `OFF`
   - **Authorization** : `OFF`
   - **Authentication flow** :
     - ✅ Standard flow
     - ✅ Direct access grants
   - Cliquer sur **"Next"**

4. Login settings :
   - **Root URL** : `http://localhost:4200`
   - **Valid redirect URIs** : `http://localhost:4200/*`
   - **Valid post logout redirect URIs** : `http://localhost:4200/*`
   - **Web origins** : `*`
   - Cliquer sur **"Save"**

![19](assets/screenshots/20.png)
![20](assets/screenshots/21.png)
![21](assets/screenshots/22.png)

### 7.5 Création des Rôles

1. Aller dans **Realm roles** → **Create role**
2. Créez les rôles suivants un par un :
   - **Role name** : `admin` → Create
   - **Role name** : `user` → Create
   - **Role name** : `keynote-manager` → Create
   - **Role name** : `conference-manager` → Create

![22](assets/screenshots/23.png)
![23](assets/screenshots/24.png)
![24](assets/screenshots/25.png)
![25](assets/screenshots/26.png)
![26](assets/screenshots/27.png)
![27](assets/screenshots/28.png)

### 7.6 Création des Utilisateurs

#### Utilisateur Admin

1. Aller dans **Users** → **Add user**
2. Remplir :
   - **Username** : `admin`
   - **Email** : `admin@jaouad.ma`
   - **First name** : `Admin`
   - **Last name** : `User`
   - **Email verified** : `ON`
3. Cliquer sur **"Create"**

4. Onglet **Credentials** :
   - Cliquer sur **"Set password"**
   - **Password** : `admin`
   - **Password confirmation** : `admin`
   - **Temporary** : `OFF`
   - Cliquer sur **"Save"**

5. Onglet **Role mapping** :
   - Cliquer sur **"Assign role"**
   - Cocher : `admin`, `user`, `keynote-manager`, `conference-manager`
   - Cliquer sur **"Assign"**

![28](assets/screenshots/29.png)
![29](assets/screenshots/30.png)
![30](assets/screenshots/31.png)
![31](assets/screenshots/32.png)
![32](assets/screenshots/33.png)
![33](assets/screenshots/34.png)

#### Utilisateur Standard

1. **Users** → **Add user**
2. Remplir :
   - **Username** : `user`
   - **Email** : `user@jaouad.ma`
   - **First name** : `Test`
   - **Last name** : `User`
   - **Email verified** : `ON`
3. Cliquer sur **"Create"**

4. Définir le mot de passe :
   - **Password** : `user`
   - **Temporary** : `OFF`

5. Assigner le rôle :
   - Rôle : `user`

![34](assets/screenshots/35.png)
![35](assets/screenshots/36.png)
![36](assets/screenshots/37.png)

### 7.7 Test de Connexion Keycloak

Tester l'obtention d'un token avec curl ou PowerShell :

```powershell
curl -X POST "http://localhost:8090/realms/conference-realm/protocol/openid-connect/token" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "grant_type=password" `
  -d "client_id=angular-frontend" `
  -d "username=admin" `
  -d "password=admin"
```

Vous devriez recevoir un JSON avec un `access_token`.

![38](assets/screenshots/39.png)

---

## 8. Tests et Démonstration

### 8.1 Test avec Swagger UI

#### Test Keynote Service

1. Ouvrir : **http://localhost:8081/swagger-ui.html**

![39](assets/screenshots/38.png)

2. Tester **POST /api/keynotes** :
   - Cliquer sur "Try it out"
   - Body :
   ```json
   {
    "nom": "JAOUAD",
    "prenom": "Salah-Eddine",
    "email": "salah@jaouad.ma",
    "fonction": "Etudiant en Cybersecurite"
    }
   ```
   - Execute

![40](assets/screenshots/40.png)
![41](assets/screenshots/41.png)

3. Tester **GET /api/keynotes** :
   - Execute
   - Vous devriez voir le keynote créé

![42](assets/screenshots/42.png)
![43](assets/screenshots/43.png)

#### Test Conference Service

1. Ouvrir : **http://localhost:8082/swagger-ui.html**

**[📸 Screenshot 24 : Swagger UI Conference Service]**

2. Créez une conférence avec **POST /api/conferences** :
   ```json
   {
     "titre": "Introduction à Spring Cloud",
     "type": "ACADEMIQUE",
     "date": "2025-10-20",
     "duree": 3,
     "nombreInscrits": 50,
     "score": 4.5,
     "keynoteId": 1
   }
   ```

![44](assets/screenshots/44.png)
![45](assets/screenshots/45.png)

3. Ajoutez une review avec **POST /api/conferences/1/reviews** :
   ```json
   {
     "date": "2025-10-20",
     "texte": "Excellente formation, très instructive!",
     "stars": 5
   }
   ```

![46](assets/screenshots/46.png)
![47](assets/screenshots/47.png)

### 8.2 Test avec l'Interface Angular

#### Page d'Accueil

1. Ouvrir : **http://localhost:4200**

![48](assets/screenshots/48.png)

#### Gestion des Keynotes

1. Cliquer sur **"Keynotes"** dans le menu
2. Créez un nouveau keynote :
   - Remplir le formulaire
   - Cliquer sur "Créer"

![49](assets/screenshots/49.png)
![50](assets/screenshots/50.png)

3. Tester la modification :
   - Cliquer sur "✏️ Modifier"
   - Changez un champ
   - Cliquer sur "Mettre à jour"

![51](assets/screenshots/51.png)
![52](assets/screenshots/52.png)
![53](assets/screenshots/53.png)

#### Gestion des Conférences

1. Cliquer sur **"Conférences"** dans le menu
2. Créez une conférence :
   - Remplir tous les champs
   - Sélectionnez un keynote
   - Cliquer sur "Créer"

![54](assets/screenshots/54.png)
![55](assets/screenshots/55.png)
![56](assets/screenshots/56.png)


#### Gestion des Reviews

1. Sur une conférence, Cliquer sur **"📝 Voir Reviews"**
2. La section reviews s'affiche
3. Ajoutez une review :
   - Texte : "Très bonne présentation"
   - Note : 5
   - Cliquer sur "Ajouter Review"

![57](assets/screenshots/57.png)
![58](assets/screenshots/58.png)
![59](assets/screenshots/59.png)

### 8.3 Console H2

#### Keynote Service Database

1. Ouvrir : **http://localhost:8081/h2-console**
2. Configurer :
   - **JDBC URL** : `jdbc:h2:mem:keynote-db`
   - **Username** : `sa`
   - **Password** : (laisser vide)
3. Cliquer sur "Connect"

![61](assets/screenshots/61.png)
![62](assets/screenshots/62.png)

4. Exécutez la requête :
   ```sql
   SELECT * FROM keynotes;
   ```

![63](assets/screenshots/63.png)

#### Conference Service Database

1. Ouvrir : **http://localhost:8082/h2-console**
   2. **JDBC URL** : `jdbc:h2:mem:conference-db`
3. Requêtes :
   ```sql
   SELECT * FROM conferences;
   SELECT * FROM reviews;
   ```

![64](assets/screenshots/64.png)
![65](assets/screenshots/65.png)

### 8.4 Test avec API via Gateway

#### Obtenir un Token

```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8090/realms/conference-realm/protocol/openid-connect/token" `
  -Method POST `
  -ContentType "application/x-www-form-urlencoded" `
  -Body @{
    grant_type = "password"
    client_id = "angular-frontend"
    username = "admin"
    password = "admin"
  }

$token = $response.access_token
```

#### Appeler les APIs

```powershell
# Lister les keynotes via Gateway
Invoke-RestMethod -Uri "http://localhost:8080/api/keynotes" `
  -Headers @{ Authorization = "Bearer $token" }

# Créer une conférence via Gateway
Invoke-RestMethod -Uri "http://localhost:8080/api/conferences" `
  -Method POST `
  -Headers @{ 
    Authorization = "Bearer $token"
    "Content-Type" = "application/json"
  } `
  -Body (@{
    titre = "Test via Gateway"
    type = "COMMERCIALE"
    date = "2024-12-01"
    duree = 2
    nombreInscrits = 30
    score = 4.0
    keynoteId = 1
  } | ConvertTo-Json)
```

![66](assets/screenshots/66.png)
![67](assets/screenshots/67.png)
![68](assets/screenshots/68.png)
![69](assets/screenshots/69.png)

---

## 9. Conclusion

### 9.1 Résultats Obtenus

Ce projet a permis de mettre en œuvre avec succès une architecture microservices complète comprenant :

- **7 services** fonctionnant en harmonie  
- **Découverte de services** automatique avec Eureka  
- **Gateway unique** pour l'accès aux APIs  
- **Sécurité robuste** avec OAuth2 et Keycloak  
- **Résilience** grâce aux circuit breakers  
- **Documentation automatique** avec Swagger  
- **Frontend moderne** avec Angular  
- **Déploiement containerisé** avec Docker  

### 10.2 Compétences Acquises

- Architecture microservices avec Spring Cloud
- Service Discovery et Load Balancing
- API Gateway et routage
- Sécurité OAuth2/OIDC
- Communication inter-services (OpenFeign)
- Fault tolerance (Resilience4J)
- Développement frontend moderne avec Angular
- Containerisation et orchestration avec Docker
- Documentation d'APIs avec OpenAPI/Swagger
- Maîtrise d'IntelliJ IDEA pour le développement Java/Spring
- Configuration et gestion de projets Maven multi-modules

### 10.3 Améliorations Futures

Pour aller plus loin, ce projet pourrait inclure :

- Tests unitaires et d'intégration (JUnit, Mockito)
- Monitoring avec Prometheus et Grafana
- Logs centralisés avec ELK Stack
- Base de données persistante (PostgreSQL)
- Messaging asynchrone (Kafka, RabbitMQ)
- Déploiement Kubernetes
- CI/CD avec GitHub Actions
- Cache distribué (Redis)

### 10.4 Difficultés Rencontrées et Solutions

| Difficulté | Solution |
|-----------|----------|
| Configuration Keycloak complexe | Documentation détaillée étape par étape |
| Circuit breaker non testé | Arrêt volontaire d'un service pour test |
| CORS errors | Configuration CORS dans tous les services |
| Temps de démarrage long | Docker Compose pour orchestration |

---

## 📚 Références et Documentation

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Angular Documentation](https://angular.io/docs)
- [Docker Documentation](https://docs.docker.com/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Resilience4J Documentation](https://resilience4j.readme.io/)

---

## 📞 Contact

- **Salah-Eddine JAOUAD**  
- **Email** : salahjd16@gmail.com    
