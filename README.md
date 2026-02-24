# MediLabo - Détection du risque de diabète

Ce projet a été réalisé dans le cadre de ma formation de Concepteur Développeur d'Applications. 
MediLabo est une application basée sur une architecture microservices permettant d'évaluer le risque de diabète chez les patients, en se basant sur leurs données administratives et l'analyse de mots-clés dans leurs notes médicales.

## Architecture du Projet

L'application est découpée en 5 microservices distincts pour garantir la séparation des responsabilités et la résilience :

* **client-ui** : Interface utilisateur (Front-end) permettant aux médecins de consulter et gérer les dossiers.
* **gateway-service** : Point d'entrée unique (API Gateway) qui sécurise et route les requêtes vers les bons services back-end.
* **patient-service** : Gère les données administratives des patients (Base de données SQL).
* **note-service** : Gère l'historique des notes et observations médicales (Base de données NoSQL - MongoDB).
* **assessment-service** : Moteur de règles métier qui croise les données (Patients + Notes via OpenFeign) pour calculer le niveau de risque de diabète.

## Technologies Utilisées

* **Langage & Framework :** Java 17, Spring Boot 3, Spring Cloud (Gateway, OpenFeign)
* **Bases de données :** MySQL/H2 (Relationnel) et MongoDB (Documentaire)
* **Déploiement :** Docker, Docker Compose
* **Qualité & CI/CD :** GitHub Actions (Tests automatisés à chaque push), JaCoCo, Javadoc

## Prérequis

Pour lancer ce projet sur votre machine locale, vous devez avoir installé :
1. [Docker Desktop](https://www.docker.com/products/docker-desktop/) (doit être en cours d'exécution).
2. Java 17 (pour la compilation via le wrapper Maven).
3. Un terminal PowerShell (pour les utilisateurs Windows).

## Installation et Lancement Rapide

Pour faciliter l'évaluation, un script d'automatisation a été créé à la racine du projet. Il se charge de compiler tous les microservices et de monter l'infrastructure Docker.

1. Clonez ce dépôt sur votre machine locale.
2. Ouvrez Docker Desktop et attendez que le moteur soit démarré.
3. Ouvrez un terminal PowerShell à la racine du projet (`/P9`).
4. Exécutez le script de lancement :

   ```powershell
   .\LanceTout.ps1
Note : Le script va exécuter Maven (clean package) pour chaque service, puis lancer docker-compose up --build. Le premier lancement peut prendre quelques minutes le temps de télécharger les images Docker.

## Accès à l'application

Une fois que tous les conteneurs sont démarrés dans Docker, l'application est accessible via votre navigateur :

URL : http://localhost:8080
Identifiant : user
Mot de passe : password

(L'authentification Basic Auth est configurée pour le Client UI dans le cadre de ce MVP).

## Arrêter l'application

Pour arrêter proprement l'application et détruire les conteneurs, utilisez la commande suivante à la racine du projet :

PowerShell
docker-compose down