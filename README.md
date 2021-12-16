# PS7 2021-2022 CASPAR-WIA2

![](https://i.imgur.com/ZycSFqr.png)

Welcome to our PS7 project based on the Polyfrontier common subject.

## FBCA Basics

- An FBCA (FTBA in French) is the French-Britannic Crossing Authorization.

- Any person crossing the border between France and the United Kingdom needs to fill beforehand a form to get an FBCA. **It's a requirement for a lawful crossing of the border.**

- An FBCA is tightly linked to the **passport** document number, which is also mandatory when crossing the border.

- The validity period of an FBCA is **14 days**. During that period, unlimited crossings of the borders are possible, as long as the holder of the FBCA is a tourist or visitor. In the case of a professional FBCA, the validity of the authorization is revoked after one crossing since the freight is different each time.

- An FBCA can either be personal or professional.

## FBCA Request (personal)

- The form is filled online (Web, App);
- Personal information such as surname, first name, birthdate, residing country... are asked;
- A safety form is included to help fight terrorism;
- A photo of the passport is required and will be verified automatically by a partner service (Persona);
- No account, the user needs to fill the form each time.

## FBCA Request (professional/freight)

- The forms are filled online (Web, App);
- A professional account needs to be created with a SIRET number and personal information such as surname, first name, birthdate, residing country...;
- A safety form is included to help fight terrorism;
- A photo of the passport is required and will be verified automatically by a partner service (Persona);
- Each time a professional needs to cross the border, he needs to submit the FBCA form (prefilled with information from his account) with the new freight information.

## FBCA Outcome (both)

- The outcome of the request is available by e-mail (generally 1h later) to the user;
- It can by either confirmation or a denial;
- No reason for the denial is given;
- The existence of a current and valid FBCA can be verified online with just a passport number (public).

## FBCA Control (both)

- At the border, **random controls** are made to ensure safety of both countries;
- The user needs to provide the physical passport to the police or customs agent in order for them to scan the NFC chip embedded in them;
- Once the passport is scanned, the agent can tell if you are lawfully crossing the border or not (data seen: vital FBCA information);
- Once the passport is scanned, the police can tell if you are lawfully crossing the border or not (data seen: all details of the FBCA, previous FBCA, previous crossing attempts of each FBCA...);
- Finally, the police or customs agent electronically stamps your FBCA (OK, NOT_OK_{REASON}).

## Resources

Many parallels to our system and modus operandi were inspired by the AVE (Electronic Travel Authorization) modus operandi, in effect at the Canadian border crossing for tourists.

## Glossary
- FBCA = French-Britannic Crossing Authorization
- ATFB = Autorisation de Traverse Franco-Britanique


## Definition of ready

Une user/technical story est prête dès lors qu'il contient:
- un titre en français || anglais
- le détails de la user story ("En tant que", "Afin de",...)
- les critères d'acceptation
- les tests d'acceptation (si il y en a)
- les labels sur:
    - le sizing
    - le t-shirt sizing
    - le moscow
    - l'épic
    - ts ou us

Une spike est prête à partir du moment où l'issue associée contient:
	- une description des recherches à effectuer.


## Definition of done

Une user/technical story est finie dès lors:

- les tests d'acceptation passent
- la story est intégré dans la branche principale
- les nouveaux et anciens tests unitaires
- le build de l'intégration continue passe
