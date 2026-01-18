# TODO List - Projet BackPack Hero (L3)

## 🏁 Phase 0 : Préparation & Architecture

- [x] **Administration**
  - [x] S'inscrire sur e-learning avant la deadline (13 novembre 23:59).
- [x] **Mise en place de l'environnement**
  - [x] Créer le dossier `lib`.
  - [x] Télécharger `zen-6.0.jar`.
  - [x] Ajouter `zen-6.0.jar` au Build Path.
- [x] **Architecture MVC**
  - [x] **Modèle :** Classes de données et logique (`Hero`, `Backpack`, `Item`, `Enemy`, `Dungeon`, `CombatEngine`).
  - [x] **Vue :** Classe `View` qui utilise Zen pour dessiner.
  - [x] **Contrôleur :** Classe `Controller` qui gère la boucle de jeu et les événements.

---

## 1️⃣ Phase 1 : La Base (Soutenance β)

### Héros

- [x] Classe `Hero` avec 40 HP de départ
- [x] Gestion de l'énergie (3 par tour)
- [x] Gestion du block (protection)

### Sac à dos (Phase 1 = taille fixe)

- [x] Classe `Backpack` avec grille **3 lignes × 5 colonnes = 15 cases** (selon le sujet)
- [x] Placement d'items avec leur forme
- [x] Vérification des collisions
- [x] Rotation des items (90°)
- [x] Translation des items

### Équipements (SANS interactions pour Phase 1)

- [x] Classe abstraite `Item` avec forme (`Shape`)
- [x] **Arme** : `Weapon` (épée en bois : 1 énergie, 6 dégâts)
- [x] **Armure** : `Armor` (protection)
- [x] **Objet magique** : nécessite de la mana
- [x] **Pierres de mana** : `ManaStone`
- [x] **Or** : `Gold`

### Ennemis (Phase 1 = simples)

- [x] Classe `Enemy`
- [x] `RatLoup` (20 HP, attaque ou défense aléatoire)
- [x] `PetitRatLoup` (moins de HP)
- [x] IA simple : choix aléatoire entre attaque et défense

### Combat (CombatEngine)

- [x] Structure de base du `CombatEngine`
- [x] Initialisation du tour héros (3 énergie, block à 0)
- [x] L'ennemi annonce son action AVANT son tour (visible par le joueur)
- [x] Héros attaque un ennemi avec une arme
- [x] Héros se défend avec une armure
- [x] Ennemi attaque le héros (dégâts - block)
- [x] Ennemi se défend (augmente son block)
- [x] Vérification fin de combat
- [x] Calcul de l'or gagné
- [x] **Intégration avec l'interface graphique** (le joueur clique sur ses items)
- [x] Afficher l'action annoncée par l'ennemi
- [x] Gestion du loot après victoire

### Donjon (3 étages, codé en dur pour Phase 1)

- [x] Classe `Dungeon` (contient 3 `Floor`)
- [x] Classe `Floor` (grille 5×11 selon le sujet)
- [x] Classe `Room` avec `RoomType`
- [x] Types de salles :
  - [x] Couloir (autant que souhaité)
  - [x] Salle d'ennemis (×3 par étage)
  - [x] Marchand (×1 par étage)
  - [x] Guérisseur (×1 par étage)
  - [x] Trésor (×2 par étage)
  - [x] Porte de sortie (×1 par étage)
- [x] Contenu des salles (ennemis, loot) codé en dur
- [w] Logique du marchand (acheter/vendre)
- [x] Logique du guérisseur (soigner contre or)
- [x] Logique du trésor (ramasser le loot)

### Interface graphique (Vue & Contrôleur)

- [x] Fenêtre Zen créée
- [x] Affichage de la carte du donjon
- [x] Clic pour se déplacer (règle : passer uniquement par les couloirs)
- [x] Affichage du sac à dos
- [x] Clic pour utiliser un item
- [x] **Écran de combat**
  - [x] Afficher le héros (HP, énergie, block)
  - [x] Afficher les ennemis (HP, block, action annoncée)
  - [x] Cliquer sur une arme → attaquer un ennemi
  - [x] Cliquer sur une armure → se défendre
  - [x] Bouton "Fin de tour"
- [x] Affichage du loot après combat
- [x] Écran de victoire/défaite

---

## 2️⃣ Phase 2 : Le jeu en version β

### Expérience et niveaux

- [x] Ajouter XP au `Hero`
- [x] Chaque ennemi donne de l'XP à sa mort
- [x] Monter de niveau = +3 ou 4 cases dans le sac
- [x] Le joueur choisit où placer les nouvelles cases
- [x] Sac à dos max : 5×7 cases

### Interactions d'équipements

- [ ] Un item "sait" quels items sont adjacents (via le `Backpack`)
- [ ] Gemme de cœur : +1 HP quand arme adjacente utilisée
- [ ] Hachette : 4 dégâts, mais 1 dégât si le héros porte une armure
- [ ] Autres synergies...

### Effets de combat

- [ ] Système de `StatusEffect` (positif/négatif)
- [ ] Poison, Haste, Weakness, etc.
- [ ] Appliquer les effets à chaque tour

### Malédictions

- [ ] Classe `Curse` (hérite de `Item`)
- [ ] Choix : prendre k dégâts OU insérer la malédiction (k = nombre de refus)
- [ ] Pas de rotation des malédictions
- [ ] Jeter les objets en dessous de la malédiction
- [ ] Se débarrasser d'une malédiction = pénalité jusqu'à fin du prochain combat

### Loot aléatoire

- [x] Tables de loot basées sur la rareté
- [x] Remplacer le loot codé en dur par du loot aléatoire

---

## 3️⃣ Phase 3 : Le jeu complet

### Score & Hall of Fame (PRIORITÉ 1)

- [ ] Formule de score (HP max + somme prix équipements)
- [ ] Hall of Fame avec les 3 meilleures parties
- [ ] Sauvegarder/charger avec `java.nio` (PAS `java.io.File` !)

### Nouveaux ennemis

- [x] Sorcier-grenouille (Frog Wizard)
- [x] Ombre vivante (Living Shadow)
- [x] Reine des abeilles (Bee Queen)

### Génération aléatoire de carte

- [x] Algorithme de génération (la carte doit être connexe)
- [ ] Grilles et clés

---

## 📦 Livrables

### Rendu intermédiaire (soutenance β)

- [ ] Archive `.zip` nommée `Nom1_Nom2_BackpackHero.zip`
- [ ] Répertoire `src` avec les sources
- [ ] Répertoire `docs` avec :
  - [ ] `user.pdf` : manuel utilisateur (lisible par "Bosphore, 11 ans")
  - [ ] `dev.pdf` : architecture du projet
  - [ ] `doc/` : Javadoc en anglais
- [ ] Répertoire `lib` avec `zen-6.0.jar`
- [ ] `BackpackHero.jar` exécutable (`java -jar BackpackHero.jar`)

### Rendu final

- [ ] Tout ce qui précède, PLUS :
- [ ] Répertoire `classes` (vide dans l'archive)
- [ ] `build.xml` avec targets : `compile`, `jar` (défaut), `javadoc`, `clean`
- [ ] `dev.pdf` inclut les améliorations depuis la soutenance β

---

## ✅ Qualité du code (Points importants)

- [ ] Pas de méthodes > 20 lignes
- [ ] Pas de duplication de code
- [ ] Tous les champs sont `private`
- [ ] Pas de variables globales
- [ ] Méthodes publiques : vérifier les arguments (`Objects.requireNonNull`)
- [ ] Javadoc en anglais
- [ ] PAS de `java.io.File` (utiliser `java.nio`)
- [ ] PAS de code copié du net

---

## 💀 Règles Mort Subite

- [ ] Participer aux DEUX soutenances (β et finale)
- [ ] Le code DOIT compiler
- [ ] Archive `.zip` avec le bon nom et la bonne structure
- [ ] Pas de librairies externes non autorisées
- [ ] Pas de plagiat