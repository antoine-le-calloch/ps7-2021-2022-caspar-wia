# PS7 2021-2022 CASPAR-WIA2

![](https://i.imgur.com/ZycSFqr.png)

Welcome to our PS7 project based on the Polyfrontier common subject.

## Table of contents

<!--ts-->
   * [FBCA Basics](#fbca-basics)
   * [FBCA Request (personal)](#fbca-request-personal)
   * [FBCA Request (professional/freight)](#fbca-request-professionalfreight)
   * [FBCA Control (both)](#fbca-control-both)
   * [Resources](#resources)
   * [Glossary](#glossary)
   * [Definition of ready](#definition-of-ready)
   * [Definition of done](#definition-of-done)
<!--te-->

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
A user/technical story is ready when it contains
- a title in French || English
- the details of the user story ("As", "In order to",...)
- the acceptance criteria
- the acceptance tests (if any)
- the labels on:
    - the sizing
    - the t-shirt sizing
    - the moscow
    - the epic
    - TS or US

A spike is ready as soon as the associated issue contains
	- a description of the search to be performed.

## Definition of done
A user/technical story is finished when

- the acceptance tests are passed
- the story is integrated in the main branch
- the new and old unit tests
- the build of the continuous integration passes
