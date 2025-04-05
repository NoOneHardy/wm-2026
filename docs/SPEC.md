# Specification

## Table of Contents

| #  | Name                              | Description         |
|----|-----------------------------------|---------------------|
| 1  | [Login](#1-login)                 | User login          |
| 2  | [Signup](#2-signup)               | User signup         |
| 3  | [Bet placement](#3-bet-placement) | User places a bet   |
| 4  | [Bet history](#4-bet-history)     | User bet history    |
| 5  | [Leaderboard](#5-leaderboard)     | Overall ranking     |
| 6  | [Settings](#6-settings)           | User settings       |
| 7  | [Results](#7-results)             | Game results        |
| 8  | [Notifications](#8-notifications) | User notifications  |
| 9  | [Email](#9-email)                 | Email notifications |
| 10 | [Dashboard](#10-dashboard)        | User dashboard      |
| 11 | [Admin](#11-admin)                | Admin panel         |

()

## 1 Login

| # | Name                                   | Description                                            |
|---|----------------------------------------|--------------------------------------------------------|
| 1 | [Login Form](#11-login-form)           | A form for users to enter their credentials to log in. |
| 2 | [Forgot Password](#12-forgot-password) | A link to reset the password if the user forgets it.   |
| 3 | [User Menu](#13-user-menu)             | A menu in the header with user options                 |
| 4 | [Header](#14-header)                   | A header with the app logo and navigation links        |

### 1.1 Login Form

#### Description

Redirects to the dashboard if the user is logged in.

#### Form fields

| # | Name     | Type   | Validation | Note |
|---|----------|--------|------------|------|
| 1 | username | string | required   |      |
| 2 | password | string | required   |      |

### 1.2 Forgot Password

#### Description

The application sends a mail to the user's email address with a link to reset the password.  
It generates an ID and stores it in the database for 10 minutes together with the user's IP.  
The URL of the link is ```/reset/${id}```.  
If the ID matches with an entry in the database, the user can reset the password using the password reset form.

#### Form fields

| # | Name     | Type   | Validation                                                    |
|---|----------|--------|---------------------------------------------------------------|
| 1 | password | string | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul> |
| 2 | confirm  | string | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul> |

### 1.3 User Menu

#### Description

When the user is logged in, a user menu is displayed in the header.  
The menu is a context menu and is triggered when hovering over the username in the header.

#### Menu items

| # | Name     | Description        | Action                              |
|---|----------|--------------------|-------------------------------------|
| 1 | Settings | Open user settings | Navigation to ```/settings```       |
| 2 | Logout   | Logout user        | Logout user and redirect to ```/``` |

### 1.4 Header

#### Navigation Items

| # | Name          | Route                                    | Condition     | Note |
|---|---------------|------------------------------------------|---------------|------|
| 1 | Home          | ```/```                                  | not logged in |      |
| 2 | Dashboard     | ```/```                                  | logged in     |      |
| 3 | Leaderboard   | ```/leaderboard```                       |               |      |
| 4 | Results       | ```/results```                           |               |      |
| 5 | Bets          | ```/bets```                              | logged in     |      |
| 6 | Notifications | Opens Context menu with notifications    | logged in     | Icon |
| 7 | Login         | ```/login```                             | not logged in |      |
| 8 | Signup        | ```/signup```                            | not logged in |      |
| 9 | Username      | Opens <a href="#user-menu">User Menu</a> | logged in     |      |

## 2 Signup

| # | Name                                             | Description                                             |
|---|--------------------------------------------------|---------------------------------------------------------|
| 1 | [Signup Form](#21-signup-form)                   | A form for users to enter their credentials to sign up. |
| 2 | [Terms and Conditions](#22-terms-and-conditions) | A link to the terms and conditions of the application.  |
| 3 | [Redirect to Login](#23-redirect-to-login)       | A link to redirect the user to the login page.          |
| 4 | [Email confirmation](#24-email-confirmation)     | A link to redirect the user to the login page.          |

### 2.1 Signup Form

#### Description

Automatically logs in user after signup and redirects to the dashboard.

#### Form fields

| # | Name      | Type   | Description          | Validation                                                             |
|---|-----------|--------|----------------------|------------------------------------------------------------------------|
| 1 | username  | string | The user's username  | <ul><li>required</li><li>minLength(5)</li><li>usernameUnique</li></ul> |
| 2 | email     | email  | The user's email     | <ul><li>required</li><li>email<li>emailUnique</li></ul>                |
| 3 | firstname | string | The user's firstname | <ul><li>required</li><li>minLength(2)</li></ul>                        |
| 4 | lastname  | string | The user's lastname  | <ul><li>required</li><li>minLength(2)</li></ul>                        |
| 5 | password  | string | The user's password  | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul>          |
| 6 | confirm   | string | Repeat the password  | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul>          |
| 7 | terms     | bool   | Accept terms         | <ul><li>required</li></ul>                                             |

### 2.2 Terms and Conditions

#### Description

A link to the page with the terms and conditions of the application.

| URL        | Display name                               |
|------------|--------------------------------------------|
| ```/agb``` | Ich habe die *AGB* gelesen und akzeptiert. |

### 2.3 Redirect to Login

#### Description

A link to redirect the user to the login page.

| URL          | Display name                  |
|--------------|-------------------------------|
| ```/login``` | Bereits ein Konto? *Anmelden* |

### 2.4 Email confirmation

#### Description

The application sends a mail to the user's email address with a code to confirm the email address.  
The code is generated by the application and stored in the database for 10 minutes.

#### Form fields

| # | Name | Type   | Validation                                   | Note |
|---|------|--------|----------------------------------------------|------|
| 1 | code | string | <ul><li>required</li><li>length(5)</li></ul> |      |

## 3 Bet placement

| # | Name                                         | Description                                          |
|---|----------------------------------------------|------------------------------------------------------|
| 1 | [knockout tree](#31-knockout-tree)           | A graph of the teams in knockout phase               |
| 2 | [Overview](#32-overview)                     | Overview of the groups and knockout phases           |
| 3 | [Group view](#33-group-view)                 | Overview of the games in one group                   |
| 4 | [Result preview](#34-result-preview)         | Preview of the result of a game                      |
| 5 | [Bet placement form](#35-bet-placement-form) | A form to place a bet on a game                      |
| 6 | [Knockout view](#36-knockout-view)           | Overview of the games in a knockout phase            |
| 7 | [Game history](#37-game-history)             | Games played by one of the teams in a knockout phase |

## 3.1 Knockout tree

#### Description

A graph of the teams in knockout phase.  
It is displayed as soon as the teams of a knockout phase are defined.  
The more teams there are the further the tree is displayed.  
It is just a preview of the knockout phase and does not provide any functionality.

### 3.2 Overview

#### Description

An overview of the groups and knockout phases.  
A group is displayed as a card and shows how many percentages of the games have been bet on.  
There is a card for each group and knockout phase. Additionally, there is a card for all groups.  
A card contains a link to the group view or knockout view.  
It is located under the following URL: ```/bets```.

### 3.3 Group view

### 3.4 Result preview

### 3.5 Bet placement form

### 3.6 Knockout view

### 3.7 Game history

## 4 Bet history

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 5 Leaderboard

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 6 Settings

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 7 Results

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 8 Notifications

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 9 Email

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 10 Dashboard

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |

## 11 Admin

| # | Name | Description |
|---|------|-------------|
| 1 |      |             |
