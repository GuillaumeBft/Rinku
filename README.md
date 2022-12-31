# Rinku

## Description
Ce projet a été réalisé dans le contexte du module d'Algorithme de la mobilité du master 2 Informatique de Bordeaux. Sa réalisation a été faite en suivant les instructions du sujet suivant :

Entités mobiles pour rétablir la connexité
Domaine d'application : déploiement réseau dans des zones sans infrastructure

Entrée : Nous avons des noeuds isolés dans un réseau (trop éloignés pour communiquer). On souhaite déployer des robots mobiles qui vont se déplacer pour rétablir une forme de connexité entre ces noeuds.

Objectifs :

Permettre à l'utilisateur de placer les villages
Faire une version simple avec un seul robot (tester au moins deux algos ?)
Afficher le temps max de communication entre villages
Faire une version multi-robots (au moins 2 algos différents)
Observer comment l'augmentation du nombre de robots améliore le temps.
Ajouter des composants graphiques pour choisir les différents paramètres
On peut supposer que les robots sont initialement placés là où l'algo le souhaite.

Adaptations possibles en fonction des souhaits du binôme (à discuter avec l'enseignant). Une amélioration utile serait de pouvoir connaître la valeur optimale (qui à utiliser un algo de brute force) pour comparer les solutions de vos algorithmes.

## Utilisation
En lançant le projet, une interface apparaît avec 5 villages prédéfinis, vous pouvez démarrer l'exécution en ouvrant le menu contextuel avec un clic droit sur un espace vide puis en sélectionnant **Start execution**.

### Options
Avant de démarrer l'exécution, vous avez plusieurs options pour modifier celle-ci :

- Changer la configuration du réseau de villages.
  - Ajouter/Retirer des villages avec respectivement clic droit/clic gauche
  - Déplacer des villages avec le glisser-déposer

- Modifier le nombre de robots ou l'algorithme qui va être utilisé via le menu contextuel.

Pendant l'exécution, vous pouvez afficher les temps de communication maximums entre tous les villages via **Display max comm. times** . Vous pouvez aussi changer la vitesse du système avec **Set system speed** (cela ne changera pas les valeurs affichées par **Display max comm. times**).
