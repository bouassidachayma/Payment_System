# PayInsight

### Système de gestion des paiements avec XML, XSD, XPath et services web SOAP

Projet académique de développement Java autour de XML, XSD, XPath et des services web SOAP.

---

## Table des matières

1. [Présentation du projet](#1-présentation-du-projet)
2. [Objectifs pédagogiques](#2-objectifs-pédagogiques)
3. [Technologies utilisées](#3-technologies-utilisées)
4. [Structure du projet](#4-structure-du-projet)
5. [Partie 1 — Les données (XML, XSD et XPath)](#5-partie-1--les-données-xml-xsd-et-xpath)
6. [Partie 2 — Le service web SOAP](#6-partie-2--le-service-web-soap)
7. [Exécution du projet](#7-exécution-du-projet)
8. [Limites et perspectives](#8-limites-et-perspectives)
9. [Conclusion](#9-conclusion)
10. [Avertissement](#10-avertissement)

---

## 1. Présentation du projet

**PayInsight** est une application Java qui modélise un système de paiement simplifié. Elle permet de gérer des informations relatives aux clients, commerçants, terminaux de paiement, transactions et codes d'erreur.

Le projet est construit en deux parties qui s'enchaînent :

* **Partie 1 — Les données :** modélisation en XML, définition d'un schéma XSD, validation du document en Java avec JAXP et interrogation des données avec XPath.
* **Partie 2 — Le service web :** un service SOAP développé avec JAX-WS, qui permet d'accéder à ces mêmes données à distance.

L'idée est simple : la partie 1 prépare des données fiables, la partie 2 les met à disposition de clients externes. L'objectif principal est de montrer comment plusieurs technologies peuvent fonctionner ensemble dans une même application.

**Lien entre les deux parties :** le service SOAP de la partie 2 réutilise le document XML, le schéma XSD et la méthode de validation de la partie 1. Il ne s'agit donc pas de deux applications indépendantes, mais d'un seul projet.

---

## 2. Objectifs pédagogiques

| Partie | Objectifs                                                                                                                         |
| ------ | --------------------------------------------------------------------------------------------------------------------------------- |
| 1 | Modéliser un domaine réel en XML, créer un schéma XSD, valider un document XML et interroger ses données avec XPath.              |
| 2 | Développer un service web SOAP avec JAX-WS, publier le service, consulter son WSDL et tester ses opérations avec SoapUI et JUnit. |

À travers ce projet, nous avons travaillé sur la structuration, la validation, l'exploitation et l'exposition de données à l'aide de différentes technologies Java et XML.

---

## 3. Technologies utilisées

| Technologie / Outil | Rôle                                                |
| ------------------- | --------------------------------------------------- |
| Java                | Langage de programmation                            |
| JAXP                | Validation XML/XSD et traitement XPath              |
| XML                 | Représentation des données de paiement              |
| XSD                 | Définition et validation de la structure XML        |
| XPath               | Recherche et extraction des données XML             |
| Maven               | Gestion du projet et des dépendances                |
| SOAP                | Protocole d'échange de messages XML                 |
| JAX-WS              | Développement du service web SOAP                   |
| JAXB                | Conversion entre objets Java et représentations XML |
| JUnit 4             | Tests unitaires                                     |
| SoapUI              | Test des opérations SOAP                            |
| IntelliJ IDEA       | Environnement de développement                      |
| Git et GitHub       | Gestion de versions et hébergement du projet        |

### Dépendances Maven principales

| Dépendance                   | Version | Rôle                         |
| ---------------------------- | ------- | ---------------------------- |
| JUnit                        | 4.13.2  | Tests unitaires              |
| JAX-WS Runtime (`jaxws-rt`)  | 2.3.7   | Exécution du service SOAP    |
| Jakarta XML Web Services API | 2.3.3   | API des services web         |
| Javax JWS API                | 1.1     | Annotations des services web |

La compilation est configurée avec Java 8 (`release 8`) et l'encodage UTF-8.

> **Remarque sur les packages :** bien que la dépendance soit nommée `jakarta.xml.ws-api`, la version 2.3.3 utilise encore les anciens packages `javax.*` (par exemple `javax.xml.ws.Endpoint`). Le passage aux packages `jakarta.*` a été introduit avec Jakarta EE 9 et les versions 3.x et 4.x des API. Il est donc normal que le code de ce projet utilise des imports `javax.*`.

---

## 4. Structure du projet

```text
PayInsight/
│
├── pom.xml
├── README.md
├── .gitignore
│
├── data/
│   ├── payments.xml
│   └── payments.xsd
│
├── images/
│   ├── validation-success.png
│   ├── validation-error.png
│   ├── xpath-results.png
│   ├── cycle-de-vie-ws.png
│   ├── diagramme-classes.png
│   ├── structure-wsdl.png
│   ├── message-soap.png
│   ├── sequence-gettransaction.png
│   ├── soap-get-transaction.png
│   ├── soap-approved-transactions.png
│   ├── soap-transactions-above-amount.png
│   ├── soap-transaction-count.png
│   └── junit-tests.png
│
└── src/
    ├── main/
    │   └── java/com/payinsight/
    │       ├── Main.java
    │       │
    │       ├── data/
    │       │   ├── Transaction.java
    │       │   └── PaymentDataService.java
    │       │
    │       ├── service/
    │       │   ├── IPaymentService.java
    │       │   └── PaymentService.java
    │       │
    │       └── server/
    │           └── PayInsightServer.java
    │
    └── test/
        └── java/com/payinsight/
            └── PayInsightTest.java
```

### Rôle des principaux fichiers

| Fichier                   | Responsabilité                                             |
| ------------------------- | ---------------------------------------------------------- |
| `payments.xml`            | Contient les données du système de paiement                |
| `payments.xsd`            | Définit les règles de structure et de validation du XML    |
| `Main.java`               | Valide le XML (partie 1) et exécute les requêtes XPath          |
| `Transaction.java`        | Représente une transaction sous forme d'objet Java (JAXB)  |
| `PaymentDataService.java` | Valide puis lit `payments.xml` et construit les objets     |
| `IPaymentService.java`    | Définit le contrat et les opérations du service SOAP       |
| `PaymentService.java`     | Implémente les opérations et contrôle les paramètres       |
| `PayInsightServer.java`   | Publie le service SOAP                                     |
| `PayInsightTest.java`     | Contient les tests unitaires JUnit                         |
| `pom.xml`                 | Configure Maven et les dépendances                         |

---

# 5. Partie 1 — Les données (XML, XSD et XPath)

Cette première partie construit la base de tout le projet : des données de paiement bien structurées, vérifiées automatiquement et faciles à interroger.

## 5.1 Modélisation des données

Le fichier `payments.xml` représente les données du système de paiement.

Il possède un élément racine nommé `paymentSystem`, qui contient cinq sections principales.

| Section        | Description                                | Exemple                            |
| -------------- | ------------------------------------------ | ---------------------------------- |
| `customers`    | Informations sur les clients               | C001, Ahmed Ben Ali                |
| `merchants`    | Informations sur les commerçants           | M001, TechStore                    |
| `terminals`    | Informations sur les terminaux de paiement | T001, Sfax                         |
| `transactions` | Informations sur les transactions          | TX001, 250.00 TND                  |
| `errorCodes`   | Codes et descriptions des erreurs          | 05, Transaction declined by issuer |

Les transactions utilisent des identifiants pour référencer les clients, les commerçants et les terminaux concernés.

La devise utilisée dans les transactions est le dinar tunisien (TND).

### Données utilisées

Le projet utilise des données fictives.

**Clients**

| Identifiant | Nom           | E-mail             | Pays    |
| ----------- | ------------- | ------------------ | ------- |
| C001        | Ahmed Ben Ali | ahmed@example.com  | Tunisia |
| C002        | Sarah Mansour | sarah@example.com  | Tunisia |

**Commerçants**

| Identifiant | Nom         | Catégorie   | Pays    |
| ----------- | ----------- | ----------- | ------- |
| M001        | TechStore   | Electronics | Tunisia |
| M002        | FashionShop | Clothing    | Tunisia |

**Terminaux**

| Identifiant | Commerçant | Localisation | Statut   |
| ----------- | ---------- | ------------ | -------- |
| T001        | M001       | Sfax         | active   |
| T002        | M002       | Tunis        | inactive |

**Transactions**

| Identifiant | Client | Commerçant | Terminal | Montant (TND) | Date       | Statut   |
| ----------- | ------ | ---------- | -------- | ------------: | ---------- | -------- |
| TX001       | C001   | M001       | T001     |        250.00 | 2026-10-08 | approved |
| TX002       | C002   | M002       | T002     |        120.00 | 2026-10-09 | declined |
| TX003       | C001   | M001       | T001     |         75.50 | 2026-10-10 | approved |

**Codes d'erreur**

| Code | Description                    | Gravité |
| ---- | ------------------------------ | ------- |
| 05   | Transaction declined by issuer | medium  |
| 51   | Insufficient funds             | medium  |
| 96   | System malfunction             | high    |

### Exemple de structure XML

```xml
<paymentSystem>
    <customers>
        <customer id="C001">
            <name>Ahmed Ben Ali</name>
            <email>ahmed@example.com</email>
            <country>Tunisia</country>
        </customer>
    </customers>

    <merchants>
        ...
    </merchants>

    <terminals>
        ...
    </terminals>

    <transactions>
        ...
    </transactions>

    <errorCodes>
        ...
    </errorCodes>
</paymentSystem>
```

## 5.2 Définition du schéma XSD

Le fichier `payments.xsd` définit les règles que le document XML doit respecter.

Il permet notamment de contrôler :

* La structure et l'ordre des éléments.
* Les types de données.
* Les attributs obligatoires.
* Les identifiants et leurs références.
* Les valeurs autorisées pour les statuts et les niveaux de gravité.

### Principales constructions XSD utilisées

| Construction     | Rôle                                         |
| ---------------- | -------------------------------------------- |
| `xs:element`     | Déclaration d'un élément XML                 |
| `xs:attribute`   | Déclaration d'un attribut                    |
| `xs:complexType` | Définition d'une structure complexe          |
| `xs:simpleType`  | Définition d'un type simple                  |
| `xs:sequence`    | Imposition d'un ordre aux éléments           |
| `xs:ID`          | Déclaration d'un identifiant unique          |
| `xs:IDREF`       | Référence vers un identifiant existant       |
| `xs:restriction` | Restriction des valeurs autorisées           |
| `xs:enumeration` | Définition d'une liste de valeurs autorisées |
| `maxOccurs`      | Définition du nombre maximal d'occurrences   |

### Exemple : structure globale

Dans le fichier XML :

```xml
<paymentSystem>
    <customers>
        <customer id="C001"> ... </customer>
    </customers>
    <merchants> ... </merchants>
    <terminals> ... </terminals>
    <transactions> ... </transactions>
    <errorCodes> ... </errorCodes>
</paymentSystem>
```

Dans le fichier XSD :

```xml
<xs:element name="paymentSystem">
    <xs:complexType>
        <xs:sequence>
            <xs:element name="customers">
                <xs:complexType>
                    <xs:sequence>
                        <xs:element name="customer"
                                    type="CustomerType"
                                    maxOccurs="unbounded"/>
                    </xs:sequence>
                </xs:complexType>
            </xs:element>

            <!-- merchants, terminals, transactions, errorCodes :
                 même principe -->
        </xs:sequence>
    </xs:complexType>
</xs:element>
```

`xs:sequence` impose l'ordre des cinq sections. `maxOccurs="unbounded"` autorise plusieurs éléments dans une section.

### Exemple : définition d'un client

Dans le fichier XML :

```xml
<customer id="C001">
    <name>Ahmed Ben Ali</name>
    <email>ahmed@example.com</email>
    <country>Tunisia</country>
</customer>
```

Dans le fichier XSD :

```xml
<xs:complexType name="CustomerType">
    <xs:sequence>
        <xs:element name="name" type="xs:string"/>
        <xs:element name="email" type="xs:string"/>
        <xs:element name="country" type="xs:string"/>
    </xs:sequence>

    <xs:attribute name="id" type="xs:ID" use="required"/>
</xs:complexType>
```

L'attribut `id` est obligatoire et doit être unique dans le document.

### Exemple : définition d'un commerçant

Dans le fichier XML :

```xml
<merchant id="M001">
    <name>TechStore</name>
    <category>Electronics</category>
    <country>Tunisia</country>
</merchant>
```

Dans le fichier XSD :

```xml
<xs:complexType name="MerchantType">
    <xs:sequence>
        <xs:element name="name" type="xs:string"/>
        <xs:element name="category" type="xs:string"/>
        <xs:element name="country" type="xs:string"/>
    </xs:sequence>

    <xs:attribute name="id" type="xs:ID" use="required"/>
</xs:complexType>
```

L'identifiant du commerçant est déclaré comme un `xs:ID`, ce qui garantit son unicité dans le document XML.

### Exemple : définition d'un terminal

Dans le fichier XML :

```xml
<terminal id="T001">
    <merchantId>M001</merchantId>
    <location>Sfax</location>
    <status>active</status>
</terminal>
```

Dans le fichier XSD :

```xml
<xs:complexType name="TerminalType">
    <xs:sequence>
        <xs:element name="merchantId" type="xs:IDREF"/>
        <xs:element name="location" type="xs:string"/>

        <xs:element name="status">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="active"/>
                    <xs:enumeration value="inactive"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:element>
    </xs:sequence>

    <xs:attribute name="id" type="xs:ID" use="required"/>
</xs:complexType>
```

`merchantId` est de type `xs:IDREF` : sa valeur (`M001`) doit correspondre à un identifiant existant dans le document XML.

Le statut du terminal est limité à deux valeurs : `active` et `inactive`.

### Exemple : définition d'une transaction

Dans le fichier XML :

```xml
<transaction id="TX001">
    <customerId>C001</customerId>
    <merchantId>M001</merchantId>
    <terminalId>T001</terminalId>
    <amount currency="TND">250.00</amount>
    <date>2026-10-08</date>
    <status>approved</status>
</transaction>
```

Dans le fichier XSD :

```xml
<xs:complexType name="TransactionType">
    <xs:sequence>
        <xs:element name="customerId" type="xs:IDREF"/>
        <xs:element name="merchantId" type="xs:IDREF"/>
        <xs:element name="terminalId" type="xs:IDREF"/>

        <xs:element name="amount">
            <xs:complexType>
                <xs:simpleContent>
                    <xs:extension base="xs:decimal">
                        <xs:attribute name="currency"
                                      type="xs:string"
                                      use="required"/>
                    </xs:extension>
                </xs:simpleContent>
            </xs:complexType>
        </xs:element>

        <xs:element name="date" type="xs:date"/>

        <xs:element name="status">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="approved"/>
                    <xs:enumeration value="declined"/>
                    <xs:enumeration value="pending"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:element>
    </xs:sequence>

    <xs:attribute name="id" type="xs:ID" use="required"/>
</xs:complexType>
```

Les références `customerId`, `merchantId` et `terminalId` sont de type `xs:IDREF`.

Le montant utilise `xs:decimal`, tandis que l'attribut `currency` est obligatoire. Le statut est limité à trois valeurs : `approved`, `declined` et `pending`.

**Correspondance XML / XSD pour une transaction**

| Dans le XML                                          | Dans le XSD                                                           |
| ---------------------------------------------------- | --------------------------------------------------------------------- |
| `id="TX001"`                                         | `xs:attribute` de type `xs:ID`, obligatoire                           |
| `<customerId>C001</customerId>` et autres références | `xs:element` de type `xs:IDREF`                                       |
| `250.00`                                             | `xs:decimal`                                                          |
| `currency="TND"`                                     | Attribut obligatoire défini avec `xs:simpleContent` et `xs:extension` |
| `2026-10-08`                                         | `xs:date`                                                             |
| `approved`, `declined`, `pending`                    | Valeurs autorisées avec `xs:restriction` et `xs:enumeration`          |

### Exemple : définition d'un code d'erreur

Dans le fichier XML :

```xml
<error code="05">
    <description>Transaction declined by issuer</description>
    <severity>medium</severity>
</error>
```

Dans le fichier XSD :

```xml
<xs:complexType name="ErrorType">
    <xs:sequence>
        <xs:element name="description" type="xs:string"/>

        <xs:element name="severity">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xs:enumeration value="low"/>
                    <xs:enumeration value="medium"/>
                    <xs:enumeration value="high"/>
                </xs:restriction>
            </xs:simpleType>
        </xs:element>
    </xs:sequence>

    <xs:attribute name="code" type="xs:string" use="required"/>
</xs:complexType>
```

Ici, `code` est un attribut obligatoire de type `xs:string`, et non un `xs:ID`.

Le niveau de gravité est limité à trois valeurs : `low`, `medium` et `high`.

### Récapitulatif XML ↔ XSD

| Dans `payments.xml`                       | Dans `payments.xsd`                                |
| ----------------------------------------- | -------------------------------------------------- |
| Attribut `id="C001"`                      | `xs:attribute` de type `xs:ID`, obligatoire        |
| Référence `<customerId>C001</customerId>` | `xs:element` de type `xs:IDREF`                    |
| Texte (nom, e-mail, lieu, description)    | `xs:string`                                        |
| Montant `250.00`                          | `xs:decimal`, avec attribut `currency` obligatoire |
| Date `2026-10-08`                         | `xs:date`                                          |
| Valeurs limitées (statuts, gravité)       | `xs:restriction` et `xs:enumeration`               |
| Plusieurs éléments dans une section       | `maxOccurs="unbounded"`                            |
| Ordre des éléments                        | `xs:sequence`                                      |

## 5.3 Validation XML/XSD avec Java

La validation est réalisée dans `Main.java`, à l'aide des classes de JAXP :

* `SchemaFactory`
* `Schema`
* `Validator`
* `StreamSource`

Le processus de validation est le suivant :

1. Création d'une fabrique de schémas avec `SchemaFactory`.
2. Chargement du fichier `payments.xsd`.
3. Création d'un objet `Validator`.
4. Validation du fichier `payments.xml`.

La méthode `validateXML()` retourne `true` si le document respecte le schéma XSD.

Les requêtes XPath sont exécutées uniquement après une validation réussie.

### Résultat d'une validation réussie

![Validation XML réussie](images/validation-success.png)

### Test avec un document XML invalide

Pour vérifier le fonctionnement du mécanisme, le statut de la transaction `TX001` a été remplacé temporairement par `unknown`.

Cette valeur n'est pas autorisée par le schéma XSD. La validation échoue donc (erreur `cvc-enumeration-valid`).

Après ce test, la valeur correcte est restaurée.

![Erreur de validation XML](images/validation-error.png)

## 5.4 Interrogation des données avec XPath

XPath permet de sélectionner des éléments et des attributs dans un document XML à partir d'expressions.

Dans `Main.java`, le traitement utilise notamment :

* `DocumentBuilder` pour charger le document XML.
* `XPathFactory` pour créer un moteur XPath.
* `XPathExpression` pour compiler les expressions.
* `NodeList` pour parcourir les résultats.

### Requêtes XPath réalisées

| N° | Objectif                                       | Expression XPath                                                 | Résultat                       |
| -- | ---------------------------------------------- | ---------------------------------------------------------------- | ------------------------------ |
| 1  | Récupérer les noms des commerçants             | `/paymentSystem/merchants/merchant/name`                         | TechStore, FashionShop         |
| 2  | Récupérer les terminaux actifs                 | `/paymentSystem/terminals/terminal[status='active']/@id`         | T001                           |
| 3  | Récupérer les transactions approuvées          | `/paymentSystem/transactions/transaction[status='approved']/@id` | TX001, TX003                   |
| 4  | Trouver les transactions supérieures à 100 TND | `/paymentSystem/transactions/transaction[amount > 100]/@id`      | TX001, TX002                   |
| 5  | Récupérer les transactions refusées            | `/paymentSystem/transactions/transaction[status='declined']/@id` | TX002                          |
| 6  | Récupérer la description de l'erreur 05        | `/paymentSystem/errorCodes/error[@code='05']/description`        | Transaction declined by issuer |

### Exemple d'analyse d'une expression XPath

Expression :

```xpath
/paymentSystem/transactions/transaction[status='approved']/@id
```

| Partie                | Signification                        | Dans `payments.xml`           |
| --------------------- | ------------------------------------ | ----------------------------- |
| `/paymentSystem`      | Sélectionne l'élément racine         | `<paymentSystem>`             |
| `/transactions`       | Accède à la section des transactions | `<transactions>`              |
| `/transaction`        | Sélectionne les transactions         | `<transaction id="TX001">`    |
| `[status='approved']` | Filtre les transactions approuvées   | `<status>approved</status>`   |
| `/@id`                | Récupère leurs identifiants          | `id="TX001"`                  |

Il faut remarquer que `status` est un élément enfant et non un attribut. C'est pourquoi on utilise `[status='approved']` et non `[@status='approved']`.

### Résultats des requêtes XPath

![Résultats des requêtes XPath](images/xpath-results.png)

---

# 6. Partie 2 — Le service web SOAP

Les données de la partie 1 sont désormais structurées, validées et interrogeables en Java. Cette seconde partie les rend accessibles à des applications externes à travers un service web SOAP.

## 6.1 Architecture générale

Le service web s'appuie directement sur les éléments de la partie 1 : le fichier `payments.xml`, le schéma `payments.xsd` et la méthode `Main.validateXML()`.

Il permet à un client externe d'interroger les données sans accéder directement au fichier XML.

Le fonctionnement général est le suivant :

1. Le serveur publie le service web.
2. Le contrat du service est décrit dans un document WSDL.
3. Le client récupère le WSDL pour connaître les opérations disponibles.
4. Le client envoie une requête SOAP via HTTP.
5. Le serveur traite la demande et renvoie une réponse SOAP.

Dans notre projet, le service est directement accessible à partir de son adresse. Aucun annuaire UDDI n'est utilisé.

### Cycle de vie d'un service web

![Cycle de vie d'un service web](images/cycle-de-vie-ws.png)

## 6.2 Organisation des classes Java

Le projet sépare le contrat du service, son implémentation, l'accès aux données et la publication du serveur.

![Diagramme de classes PayInsight](images/diagramme-classes.png)

| Classe               | Responsabilité                                                     |
| -------------------- | ------------------------------------------------------------------ |
| `Main`               | Validation XML/XSD et exécution des requêtes XPath                 |
| `Transaction`        | Représentation d'une transaction sous forme d'objet Java avec JAXB |
| `PaymentDataService` | Validation et lecture des données depuis `payments.xml`            |
| `IPaymentService`    | Définition du contrat et des opérations SOAP                       |
| `PaymentService`     | Implémentation des opérations du service                           |
| `PayInsightServer`   | Publication du service web                                         |

### Rôle des principales annotations

| Annotation         | Utilisation                                                             |
| ------------------ | ----------------------------------------------------------------------- |
| `@WebService`      | Déclare un service web ou son interface                                 |
| `@WebMethod`       | Expose une méthode comme opération du service                           |
| `@WebParam`        | Définit le nom d'un paramètre dans le contrat SOAP                      |
| `@XmlAccessorType` | Définit la stratégie d'accès aux champs JAXB                            |
| `@XmlType`         | Définit la représentation XML d'une classe et l'ordre de ses propriétés |

## 6.3 Correspondance entre Java et WSDL

Le WSDL (*Web Services Description Language*) décrit le contrat du service web.

Il précise les opérations disponibles, leurs paramètres, leurs types de données et leur adresse.

> Les extraits de WSDL présentés dans cette section sont **simplifiés**. Le contrat complet est généré automatiquement par JAX-WS et peut être consulté à l'adresse suivante :

```text
http://localhost:8086/PayInsight/PaymentService?wsdl
```

### Interface Java

L'interface `IPaymentService` définit les opérations proposées par le service :

```java
@WebService
public interface IPaymentService {

    @WebMethod
    Transaction getTransaction(
        @WebParam(name = "transactionId") String transactionId
    );

    @WebMethod
    Transaction[] getApprovedTransactions();

    @WebMethod
    Transaction[] getTransactionsAboveAmount(
        @WebParam(name = "minimumAmount") double minimumAmount
    );

    @WebMethod
    int getTransactionCount();
}
```

### Exemple de représentation dans le WSDL

```xml
<portType name="IPaymentService">
    <operation name="getTransaction">
        <input message="tns:getTransaction"/>
        <output message="tns:getTransactionResponse"/>
    </operation>

    <!-- Autres opérations du service -->
</portType>
```

L'élément `portType` décrit les opérations du service et les messages échangés.

### Correspondance entre les paramètres Java et les types XML

Par exemple, le paramètre Java :

```java
@WebParam(name = "transactionId")
String transactionId
```

est représenté dans le schéma XML du WSDL par :

```xml
<xs:element name="transactionId" type="xs:string"/>
```

De même, le paramètre `minimumAmount` de type `double` est représenté par `xs:double`.

### Classe Transaction et JAXB

La classe `Transaction` représente une transaction sous forme d'objet Java.

JAXB permet de transformer cet objet en une représentation XML utilisable dans les messages SOAP.

Extrait :

```java
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "id", "customerId", "merchantId", "terminalId",
    "amount", "currency", "date", "status"
})
public class Transaction {

    private String id;
    private String customerId;
    private String merchantId;
    private String terminalId;
    private double amount;
    private String currency;
    private String date;
    private String status;
}
```

L'annotation `@XmlType` permet notamment de définir l'ordre des propriétés dans la représentation XML.

### Correspondance entre la classe Transaction et le type XML du WSDL

Le type XML correspondant dans le WSDL est représenté, de manière simplifiée, comme suit :

```xml
<xs:complexType name="transaction">
    <xs:sequence>
        <xs:element name="id" type="xs:string"/>
        <xs:element name="customerId" type="xs:string"/>
        <xs:element name="merchantId" type="xs:string"/>
        <xs:element name="terminalId" type="xs:string"/>
        <xs:element name="amount" type="xs:double"/>
        <xs:element name="currency" type="xs:string"/>
        <xs:element name="date" type="xs:string"/>
        <xs:element name="status" type="xs:string"/>
    </xs:sequence>
</xs:complexType>
```

L'ordre des éléments correspond à celui défini dans `propOrder`.

Le champ `amount`, déclaré avec le type Java `double`, est représenté par `xs:double`. Le champ `date` est ici une chaîne de caractères (`String`) et devient donc `xs:string`.

### Publication du service

La publication est effectuée dans `PayInsightServer.java` avec `Endpoint.publish()`.

```java
String url =
    "http://localhost:8086/PayInsight/PaymentService";

Endpoint.publish(url, new PaymentService());
```

L'adresse du service est :

```text
http://localhost:8086/PayInsight/PaymentService
```

L'adresse du WSDL est :

```text
http://localhost:8086/PayInsight/PaymentService?wsdl
```

### Correspondance entre la publication Java et le WSDL

L'adresse passée à `Endpoint.publish()` et les noms du service et du port apparaissent dans le WSDL généré.

Extrait simplifié :

```xml
<binding name="PaymentServicePortBinding"
         type="tns:IPaymentService">

    <soap:binding style="document"
                  transport="http://schemas.xmlsoap.org/soap/http"/>

    <!-- Définition des opérations -->
</binding>

<service name="PaymentServiceService">
    <port name="PaymentServicePort"
          binding="tns:PaymentServicePortBinding">

        <soap:address
            location="http://localhost:8086/PayInsight/PaymentService"/>

    </port>
</service>
```

Dans la configuration utilisée, les noms du service et du port suivent les conventions de nommage par défaut de JAX-WS à partir de la classe d'implémentation `PaymentService`.

### Structure du WSDL

![Structure du document WSDL](images/structure-wsdl.png)

## 6.4 Structure des messages SOAP

SOAP est un protocole d'échange de messages XML.

Un message SOAP utilise une enveloppe (`Envelope`) qui contient :

* `Header` : les informations complémentaires de l'en-tête, facultatives.
* `Body` : le contenu principal de la requête ou de la réponse.

Dans notre projet, les messages SOAP sont échangés via HTTP.

### Structure générale

![Structure d'un message SOAP](images/message-soap.png)

### Exemple : requête getTransaction

Pour récupérer la transaction `TX001`, le client envoie la requête suivante :

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:ser="http://service.payinsight.com/">

    <soapenv:Header/>

    <soapenv:Body>
        <ser:getTransaction>
            <transactionId>TX001</transactionId>
        </ser:getTransaction>
    </soapenv:Body>

</soapenv:Envelope>
```

### Exemple : réponse SOAP

Le serveur renvoie les informations de la transaction demandée :

```xml
<S:Envelope
    xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">

    <S:Body>
        <ns2:getTransactionResponse
            xmlns:ns2="http://service.payinsight.com/">

            <return>
                <id>TX001</id>
                <customerId>C001</customerId>
                <merchantId>M001</merchantId>
                <terminalId>T001</terminalId>
                <amount>250.0</amount>
                <currency>TND</currency>
                <date>2026-10-08</date>
                <status>approved</status>
            </return>

        </ns2:getTransactionResponse>
    </S:Body>

</S:Envelope>
```

La transaction est donc lue depuis `payments.xml`, convertie en objet Java, puis sérialisée en XML dans la réponse SOAP.

On remarque également que :

* L'attribut XML `id` devient un élément XML `id` dans la réponse.
* L'attribut `currency` de `amount` devient un élément séparé.
* La valeur `250.00` est représentée par `250.0`, car le montant est manipulé avec le type Java `double`.

## 6.5 Accès aux données et traitement des demandes

La classe `PaymentDataService` assure la communication entre le service web et le fichier XML.

Pour chaque opération, le traitement suit les étapes suivantes :

1. **Validation :** appel de `Main.validateXML()` (écrite dans la partie 1) pour vérifier le document avec le schéma XSD.
2. **Lecture sécurisée :** utilisation de `DocumentBuilderFactory` avec la désactivation du DOCTYPE et des entités externes afin de limiter les risques d'attaques XXE.
3. **Parcours :** recherche des transactions correspondant aux critères demandés.
4. **Conversion :** création des objets Java `Transaction`.

La classe `PaymentService` implémente le contrat défini dans `IPaymentService`.

Elle effectue également des contrôles sur les paramètres :

* Un identifiant vide ou inexistant provoque une `IllegalArgumentException`.
* Un montant minimum négatif provoque également une `IllegalArgumentException`.

### Diagramme de séquence : récupération d'une transaction

![Diagramme de séquence getTransaction](images/sequence-gettransaction.png)

## 6.6 Opérations disponibles

Le service expose quatre opérations :

| Opération                    | Paramètre       | Description                                                              |
| ---------------------------- | --------------- | ------------------------------------------------------------------------ |
| `getTransaction`             | `transactionId` | Récupère une transaction à partir de son identifiant                     |
| `getApprovedTransactions`    | Aucun           | Récupère toutes les transactions approuvées                              |
| `getTransactionsAboveAmount` | `minimumAmount` | Récupère les transactions dont le montant est supérieur au seuil indiqué |
| `getTransactionCount`        | Aucun           | Retourne le nombre total de transactions                                 |

## 6.7 Tests avec SoapUI

SoapUI permet de tester les opérations du service web à partir de son WSDL.

Le WSDL est importé depuis :

```text
http://localhost:8086/PayInsight/PaymentService?wsdl
```

### Résultats des tests

| Opération                    | Entrée | Résultat obtenu                               |
| ---------------------------- | ------ | --------------------------------------------- |
| `getTransaction`             | TX001  | TX001 : C001, M001, T001, 250.0 TND, approved |
| `getApprovedTransactions`    | Aucune | TX001 et TX003                                |
| `getTransactionsAboveAmount` | 100    | TX001 et TX002                                |
| `getTransactionCount`        | Aucune | 3 transactions                                |

### Captures d'écran

**1. Récupération d'une transaction**

![Test getTransaction](images/soap-get-transaction.png)

**2. Récupération des transactions approuvées**

![Test getApprovedTransactions](images/soap-approved-transactions.png)

**3. Récupération des transactions supérieures à un montant**

![Test getTransactionsAboveAmount](images/soap-transactions-above-amount.png)

**4. Comptage des transactions**

![Test getTransactionCount](images/soap-transaction-count.png)

## 6.8 Tests unitaires avec JUnit

En complément des tests effectués avec SoapUI, le projet contient sept tests unitaires dans la classe `PayInsightTest`.

| Test                          | Vérification                                                   |
| ----------------------------- | -------------------------------------------------------------- |
| `testXMLValidation`           | Vérifie que le document XML respecte le schéma XSD             |
| `testGetTransaction`          | Vérifie la récupération de TX001                               |
| `testGetApprovedTransactions` | Vérifie que deux transactions approuvées sont retournées       |
| `testTransactionsAboveAmount` | Vérifie la récupération de deux transactions supérieures à 100 |
| `testTransactionCount`        | Vérifie que le nombre total de transactions est égal à 3       |
| `testInvalidTransactionID`    | Vérifie qu'un identifiant inexistant provoque une exception    |
| `testNegativeAmount`          | Vérifie qu'un montant minimum négatif provoque une exception   |

### Résultat des tests

Les sept tests ont été exécutés avec succès :

```text
Tests run: 7
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

![Résultats des tests JUnit](images/junit-tests.png)

---

# 7. Exécution du projet

## 7.1 Prérequis

Avant d'exécuter le projet, il faut disposer de :

* Un JDK compatible avec la configuration Maven.
* IntelliJ IDEA.
* Maven.
* SoapUI pour tester les opérations SOAP.

## 7.2 Récupération du projet

Pour télécharger le projet depuis GitHub :

```bash
git clone https://github.com/bouassidachayma/PayInsight.git
```

Il est également possible de télécharger le projet directement depuis GitHub au format ZIP.

## 7.3 Lancement de la partie 1

1. Ouvrir le projet dans IntelliJ IDEA.
2. Vérifier que Maven a correctement chargé les dépendances.
3. Ouvrir `Main.java`.
4. Exécuter la méthode `main()`.
5. Consulter la console pour vérifier la validation XML/XSD et les résultats XPath.

**Important :** les chemins `data/payments.xml` et `data/payments.xsd` sont relatifs. Il faut donc lancer le programme depuis la racine du projet.

## 7.4 Lancement de la partie 2

1. Ouvrir `PayInsightServer.java`.
2. Exécuter sa méthode `main()`.
3. Vérifier que le serveur est démarré.
4. Ouvrir l'adresse du WSDL :

```text
http://localhost:8086/PayInsight/PaymentService?wsdl
```

Le service est accessible à l'adresse :

```text
http://localhost:8086/PayInsight/PaymentService
```

## 7.5 Test avec SoapUI

1. Ouvrir SoapUI.
2. Créer un nouveau projet SOAP.
3. Importer l'URL du WSDL.
4. Sélectionner une opération.
5. Saisir les paramètres nécessaires.
6. Envoyer la requête et consulter la réponse.

Le serveur doit rester démarré pendant les tests.

## 7.6 Exécution des tests JUnit

Les tests peuvent être lancés depuis IntelliJ IDEA en exécutant la classe `PayInsightTest`.

Il est également possible d'utiliser Maven :

```bash
mvn test
```

---

# 8. Limites et perspectives

Même si le projet fonctionne correctement, plusieurs améliorations peuvent être envisagées.

### Limites actuelles

* **Références XML :** `xs:IDREF` vérifie l'existence d'un identifiant, mais ne garantit pas qu'il appartient à la section attendue. Les contraintes `xs:key` et `xs:keyref` permettraient de préciser les relations.
* **Performance :** le document XML est validé et analysé à chaque opération. Une stratégie de chargement et de réutilisation des données pourrait améliorer les performances.
* **Gestion des montants :** le type `double` peut entraîner des imprécisions d'arrondi. Le type `BigDecimal` serait plus adapté aux calculs financiers.
* **Gestion des erreurs SOAP :** l'utilisation de SOAP Fault permettrait de structurer davantage les erreurs renvoyées aux clients.

### Améliorations possibles

* Développer un client Java à l'aide de `wsimport`.
* Publier le service dans un annuaire UDDI.
* Remplacer le fichier XML par une base de données.
* Ajouter des opérations pour créer, modifier et supprimer des transactions.
* Ajouter une authentification pour sécuriser l'accès au service.
* Ajouter une interface graphique pour faciliter l'utilisation du système.

---

# 9. Conclusion

Le projet **PayInsight** met en pratique plusieurs concepts importants liés à XML, aux services web et au développement Java.

La première partie a permis de modéliser les données de paiement en XML, de définir leur structure avec XSD, de vérifier leur validité et de les interroger avec XPath.

La seconde partie a prolongé ce travail : les mêmes données sont exposées par un service web SOAP, avec un contrat défini avec JAX-WS, un service publié et des opérations testées avec SoapUI et JUnit.

L'enchaînement des deux parties montre comment des données structurées peuvent être validées, exploitées, puis rendues accessibles à travers un service web.

**Les résultats obtenus — quatre opérations SOAP testées et sept tests unitaires réussis — confirment le bon fonctionnement des principales fonctionnalités développées.**

---

# 10. Avertissement

PayInsight est un projet académique utilisant des données de paiement fictives.

Il ne traite pas de véritables transactions financières et ne communique avec aucune infrastructure bancaire réelle.
