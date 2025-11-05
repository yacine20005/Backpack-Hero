# TODO List - Projet BackPack Hero (L3)

## 🏁 Phase 0 : Préparation & Architecture (L'étape la plus critique)

- [ ] **Administration (Priorité Zéro)**
  - [ ] S'inscrire sur e-learning avant la deadline (13 novembre 23:59). (Ne pas faire ça = défaillant).
- [ ] **Mise en place de l'environnement**
  - [ ] Créer le dossier `lib`.
  - [ ] Télécharger `zen-6.0.jar`.
  - [ ] Ajouter `zen-6.0.jar` au Build Path.
- [ ] **Réflexion & Conception (Le VRAI travail)**
  - [ ] **Choisir le pattern de conception.** Le sujet vous pousse vers **MVC (Modèle-Vue-Contrôleur)**.
    - [ ] **Modèle :** `Hero`, `Backpack`, `Item`, `Enemy`, `GameMap`, `CombatEngine`. (Les données et la logique pure).
    - [ ] **Vue :** La classe qui utilise `Zen` pour dessiner. Elle ne fait *que* dessiner ce que le Modèle lui dit.
    - [ ] **Contrôleur :** La boucle de jeu, gère les clics, le clavier. Il dit au Modèle "le joueur a cliqué ici" et à la Vue "rafraîchis".
  - [ ] **Définir les interfaces.** C'est ça, la POO.
    - [ ] `interface Item { ... }`
    - [ ] `interface EnemyAction { ... }`
    - [ ] `interface RoomContent { ... }`

---

## 1️⃣ Phase 1 : La Base (Le "Proof of Concept")

- [ ] **Modèle : Héros & Sac**
  - [ ] Classe `Hero` (avec 40 HP).
  - [ ] Classe `Backpack` (grille fixe 3x5) (Un `Item[3][5]` ? Une `Map<Position, Item>` ?).
- [ ] **Modèle : Équipements (Simples)**
  - [ ] Classe abstraite `Equipment` (ou interface `Item`).
  - [ ] Classe `WeaponMelee`.
  - [ ] Classe `WeaponRanged`.
  - [ ] Classe `Armor`.
  - [ ] Classe `Shield`.
  - [ ] Classe `MagicItem`.
  - [ ] Classe `ManaStone`.
  - [ ] Classe `Gold`.
  - [ ] **Règle P1 :** PAS d'interactions. Un objet = un effet simple.
- [ ] **Modèle : Ennemis (Simples)**
  - [ ] Classe `Enemy` (abstraite).
  - [ ] Classe `RatLoup` et `PetitRatLoup`.
  - [ ] IA simple : `chooseAction()` (random attaque ou défense).
- [ ] **Modèle : Combat**
  - [ ] Classe `CombatEngine`.
  - [ ] Logique de tour par tour (Héros, puis Ennemis).
  - [ ] Gestion énergie (3 par tour).
  - [ ] Gestion HP et Protection.
- [ ] **Modèle : Donjon (Codé en dur)**
  - [ ] Classe `Dungeon` (contient 3 `Floor`).
  - [ ] Classe `Floor` (contient une grille de `Room`).
  - [ ] Coder en dur la carte (3 étages).
  - [ ] Classes pour les salles : `Corridor`, `EnemyRoom`, `MerchantRoom`, `HealerRoom`, `TreasureRoom`, `ExitDoor`.
  - [ ] Coder en dur le loot des trésors et combats.
- [ ] **Vue & Contrôleur (GUI)**
  - [ ] Créer une fenêtre `Zen`.
  - [ ] Afficher la carte du donjon.
  - [ ] Gérer le clic pour se déplacer (règle des couloirs).
  - [ ] Afficher l'écran de combat.
  - [ ] Afficher le sac à dos.
  - [ ] Gérer les clics pour utiliser un objet (épée, bouclier).

---

## 2️⃣ Phase 2 : Le Vrai Jeu (Le "Cœur" du projet)

- [ ] **Refactor : Évolution du Héros**
  - [ ] Ajouter XP au `Hero`.
  - [ ] Ajouter gain d'XP à la mort des ennemis.
  - [ ] Implémenter `levelUp()`.
- [ ] **Refactor : Sac à dos Dynamique**
  - [ ] Modifier la classe `Backpack` pour gérer l'ajout de cases (3-4 par niveau).
  - [ ] Le sac est contenu dans 5x7 max.
  - [ ] Gérer la rotation (90°) et la translation des objets.
- [ ] **POO : Interactions d'Objets**
  - [ ] Comment un objet "sait" qu'il est à côté d'un autre ? (Le `Backpack` doit le lui dire).
  - [ ] Implémenter des synergies (ex: Gemme de cœur + arme adjacente).
  - [ ] Implémenter des conditions (ex: Hachette + armure).
- [ ] **Modèle : Effets de Combat**
  - [ ] Créer un système de `StatusEffect` (positif/négatif) (ex: `Poison`, `Haste`, `Weakness`).
  - [ ] Mettre à jour `CombatEngine` pour appliquer les effets à chaque tour.
- [ ] **Modèle : Malédictions**
  - [ ] Classe `Curse` (hérite de `Item`).
  - [ ] Logique d'ajout :
    - [ ] Choix : prendre dégâts ou insérer.
    - [ ] Mécanique de refus (k dégâts au k-ième refus).
    - [ ] **Pas de rotation**.
    - [ ] Force à jeter les objets en dessous.
- [ ] **Logique : Randomisation**
  - [ ] Créer des tables de loot (basées sur la rareté).
  - [ ] Remplacer le loot codé en dur par du loot aléatoire.

---

## 3️⃣ Phase 3 : Finitions (Le "Polish")

- [ ] **Score & Persistance (Priorité 1)**
  - [ ] Définir une formule de score.
  - [ ] Créer le "Hall of Fame".
  - [ ] **IO :** Sauvegarder/charger les scores.
  - [ ] **ATTENTION :** Interdiction d'utiliser `java.io.File`. Utilisez les `Path` et `Files` de `java.nio`.
- [ ] **Contenu : Nouveaux Ennemis**
  - [ ] Implémenter le Sorcier-grenouille.
  - [ ] Implémenter l'Ombre vivante.
  - [ ] Implémenter la Reine des abeilles.
- [ ] **Logique : Génération de Carte (Le plus dur)**
  - [ ] Remplacer la carte codée en dur par un algorithme.
  - [ ] **Contrainte :** La carte doit être connexe. (Pensez "labyrinthe", DFS/BFS, ou algo de Prim/Kruskal).
  - [ ] Gérer les grilles/clés.

---

## 📦 Livrables & Qualité (Ce qui vous rapporte des points)

- [ ] **Code**
  - [ ] **Pas de duplication**. (Si vous copiez-collez, vous avez raté votre conception).
  - [ ] **Pas de méthodes > 20 lignes**. (Une méthode = une seule chose).
  - [ ] **Pas de champs non `private`**. (Encapsulation !).
  - [ ] **Pas de variables globales**.
  - [ ] **Vérification des arguments publics** (ex: `Objects.requireNonNull(arg)` en début de méthode).
- [ ] **Documentation**
  - [ ] `docs/doc/` : Javadoc **en anglais**.
  - [ ] `docs/user.pdf` : Manuel pour "Bosphore, 11 ans". (Simple, visuel).
  - [ ] `docs/dev.pdf` : Le manuel d'architecture (votre diagramme de classes, vos choix de design).
- [ ] **Packaging (Rendu Intermédiaire)**
  - [ ] Archive `.zip` (pas de .rar/.7z).
  - [ ] Nom `Nom1_Nom2_BackpackHero.zip`.
  - [ ] Structure : `src`, `docs`, `lib`, `BackpackHero.jar`.
  - [ ] Le `.jar` doit être exécutable (`java -jar ...`).
- [ ] **Packaging (Rendu Final)**
  - [ ] Idem que le rendu intermédiaire, MAIS :
  - [ ] Ajout d'un répertoire `classes` (vide).
  - [ ] Ajout d'un `build.xml` (fichier Ant).
  - [ ] `build.xml` doit avoir les cibles `compile`, `jar` (défaut), `javadoc`, `clean`.
  - [ ] `dev.pdf` doit inclure les améliorations depuis la soutenance.

---

## 💀 Règles d'Or (Mort Subite - À relire avant chaque commit)

- [ ] Je dois participer aux deux soutenances.
- [ ] Mon code doit compiler.
- [ ] Je ne dois **JAMAIS** copier-coller du code du net.
- [ ] Je ne dois **JAMAIS** utiliser `java.io.File`.
- [ ] Je dois écrire ma Javadoc en **anglais**.
- [ ] Mon archive `.zip` doit avoir le bon nom et la bonne structure.
