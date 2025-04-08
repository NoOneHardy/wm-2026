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
| 12 | [Home](#12-home)                  | Home screen         |

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
| 4 | [Email confirmation](#24-email-confirmation)     | The confirmation of the email address.                  |
| 5 | [Permissions](#25-permissions)                   | The role based permission system of the application.    |

### 2.1 Signup Form

#### Description

Automatically logs in user after signup and redirects to the dashboard.

#### Form fields

| # | Name      | Type   | Validation                                                             | Note |
|---|-----------|--------|------------------------------------------------------------------------|------|
| 1 | username  | string | <ul><li>required</li><li>minLength(5)</li><li>usernameUnique</li></ul> |      |
| 2 | email     | email  | <ul><li>required</li><li>email<li>emailUnique</li></ul>                |      |
| 3 | firstname | string | <ul><li>required</li><li>minLength(2)</li></ul>                        |      |
| 4 | lastname  | string | <ul><li>required</li><li>minLength(2)</li></ul>                        |      |
| 5 | password  | string | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul>          |      |
| 6 | confirm   | string | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul>          |      |
| 7 | terms     | bool   | <ul><li>required</li></ul>                                             |      |

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

### 2.5 Permissions

#### Roles

| # | Name        | Condition                   | Note                                                                     |
|---|-------------|-----------------------------|--------------------------------------------------------------------------|
| 1 | Unconfirmed | Initial role                | User has not payed participation fee and has not been confirmed by admin |
| 2 | User        | After confirmation by admin |                                                                          |
| 3 | Admin       | Manually by admin           |                                                                          | 

## 3 Bet placement

| # | Name                                         | Description                                          |
|---|----------------------------------------------|------------------------------------------------------|
| 1 | [Knockout tree](#31-knockout-tree)           | A graph of the teams in knockout phase               |
| 2 | [Overview](#32-overview)                     | Overview of the groups and knockout phases           |
| 3 | [Group view](#33-group-view)                 | Overview of the games in one group                   |
| 4 | [Points](#34-points)                         | The points gained from the game                      |
| 5 | [Bet placement form](#35-bet-placement-form) | A form to place a bet on a game                      |
| 6 | [Knockout view](#36-knockout-view)           | Overview of the games in a knockout phase            |
| 7 | [Game history](#37-game-history)             | Games played by one of the teams in a knockout phase |

### 3.1 Knockout tree

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

#### Description

An overview of the games in one group.  
Each game is displayed as a card and shows the teams, date, time and bet placement form.  
It also shows the percentage of bets placed in the group.
It is located under the following URL: ```/bets/group```.

#### States

| # | Name      | Description                 | Condition                                       | Effect                                     |
|---|-----------|-----------------------------|-------------------------------------------------|--------------------------------------------|
| 1 | Betting   | The game is not played yet. | The game has not started yet.                   | User can place a bet.                      |
| 2 | Locked    | The game is played.         | 5 minutes before the game starts.               | Card is disabled.                          |
| 3 | Closed    | The game is played.         | Results for the game have been uploaded.        | Card is disabled.<br>Points are displayed. |
| 4 | Highlight | A game is highlighted.      | The user has entered view via link to the game. | The game is highlighted for a second.      |

### 3.4 Points

#### Description

The points that the user won with the game.  
It is only displayed in closed state.  
For better understanding the actual result is displayed.  
The user can see the total of points gained from the game and how this total is calculated.

### 3.5 Bet placement form

#### Description

A form to place a bet on a game.  
The user can set a joker and a score for each team.  
It is automatically saved after 10s after changes were made.

#### Form fields

| # | Name  | Type   | Validation                              | Note           |
|---|-------|--------|-----------------------------------------|----------------|
| 1 | joker | number | <ul><li>min(1)</li><li>max(3)</li></ul> | default: **1** |
| 2 | team1 | number | <ul><li>min(0)</li></ul>                |                |
| 3 | team2 | number | <ul><li>min(0)</li></ul>                |                |

### 3.6 Knockout view

#### Description

An overview of the games in a knockout phase.  
Each game is displayed as a large card and shows the teams, date, time, game history and bet placement form.
It also shows the percentage of bets placed in the knockout phase.
It is located under the following URL: ```/bets/knockout```.

#### States

| # | Name      | Description                 | Condition                                       | Effect                                     |
|---|-----------|-----------------------------|-------------------------------------------------|--------------------------------------------|
| 1 | Betting   | The game is not played yet. | The game has not started yet.                   | User can place a bet.                      |
| 2 | Locked    | The game is played.         | 5 minutes before the game starts.               | Card is disabled.                          |
| 3 | Closed    | The game is played.         | Results for the game have been uploaded.        | Card is disabled.<br>Points are displayed. |
| 4 | Highlight | A game is highlighted.      | The user has entered view via link to the game. | The game is highlighted for a second.      |

### 3.7 Game history

#### Description

In the knockout view the user can see the game history of both of the teams.  
This history contains the last 3 games played by the team.  
The score of the team is displayed in bold.

## 4 Bet history

| # | Name               | Description                 |
|---|--------------------|-----------------------------|
| 1 | [Top 3](#41-top-3) | The top 3 games of the user |

### 4.1 Top 3

#### Description

A list of the 3 games with which the user has won the most points.  
Those are displayed in the leaderboard view.  
The contain the teams, date, time, points gained and a link to the game in betting view.

## 5 Leaderboard

| # | Name                               | Description                             |
|---|------------------------------------|-----------------------------------------|
| 1 | [Ranking](#51-ranking)             | Player ranking                          |
| 2 | [User position](#52-user-position) | The user's position in the ranking      |
| 3 | [Movement](#53-movement)           | The movement of the user in the ranking |
| 4 | [Top 3](#54-top-3)                 | The top 3 games of the user             |

### 5.1 Ranking

#### Description

A list of all users and their points.  
The entry of the user is highlighted and always displayed.  
If the players position is higher than the ones displayed the user's entry is displayed at the top.  
If the players position is lower than the ones displayed the user's entry is displayed at the bottom.

#### Fields to display

| # | Name     | Note                                    |
|---|----------|-----------------------------------------|
| 1 | username | The username of the user                |
| 2 | avatar   | The user's avatar                       |
| 3 | points   | The points of the user                  |
| 4 | movement | The movement of the user in the ranking |
| 5 | ranking  | The user's ranking                      |

### 5.2 User position

#### Description

The user's ranking should always be displayed in the leaderboard view.  
They can see their position in the ranking and how many points they have.    
Additionally, the user's avatar is shown.

### 5.3 Movement

#### Description

The movement of the user in the ranking is displayed.  
It is calculated by the difference between the current ranking and the previous ranking.  
It is first displayed when the results of the first game are uploaded.  
There are five types of movement:

| # | Type      | Description                   | Icon                       | Color                                                                                                                 | Rank         |
|---|-----------|-------------------------------|----------------------------|-----------------------------------------------------------------------------------------------------------------------|--------------|
| 1 | far down  | The user has moved down a lot | keyboard_double_arrow_down | --wm-color-red <div style="display: inline-block; height: 10px; width: 10px; background-color: #DF2935;"></div>       | -5 or lower  |
| 2 | down      | The user has moved down       | keyboard_arrow_down        | --wm-color-red-dark <div style="display: inline-block; height: 10px; width: 10px; background-color: #A0001C;"></div>  | -1 until -4  |
| 3 | no change | The user has not moved        | equal                      | --wm-color-orange-600 <div style="display: inline-block; height: 10px; width: 10px; background-color: #FFAC59"></div> | 0            |
| 4 | up        | The user has moved up         | keyboard_arrow_up          | --wm-color-secondary <div style="display: inline-block; height: 10px; width: 10px; background-color: #29873D"></div>  | +1 until +4  |
| 5 | far up    | The user has moved up a lot   | keyboard_double_arrow_up   | --wm-color-primary <div style="display: inline-block; height: 10px; width: 10px; background-color: #57C152"></div>    | +5 or higher |

### 5.4 Top 3

#### Description

The top 3 games from the bet history of the user are displayed in the leaderboard view.

## 6 Settings

| # | Name                                             | Description                                        |
|---|--------------------------------------------------|----------------------------------------------------|
| 1 | [Password](#61-password)                         | User should be able to change their password       |
| 2 | [Avatar](#62-avatar)                             | User should be able to change their avatar         |
| 3 | [Email](#63-email)                               | User should be able to change their email          |
| 4 | [Username](#64-username)                         | User should be able to change their username       |
| 5 | [Delete account](#65-delete-account)             | User should be able to delete their account        |
| 6 | [In-App notifications](#66-in-app-notifications) | User should be able to toggle in-app notifications |
| 7 | [Email notifications](#67-email-notifications)   | User should be able to toggle email notifications  |

### 6.1 Password

#### Description

Users should be able to change their password.  
This can be done without email confirmation when the user is logged in.  
But he has to enter his current password to change it.  
If the password was forgotten the user can [reset it in the login screen](#12-forgot-password).

### Form fields

| # | Name        | Type   | Validation                                                                           | Note |
|---|-------------|--------|--------------------------------------------------------------------------------------|------|
| 1 | oldPassword | string | <ul><li>required</li></ul>                                                           |      |
| 2 | newPassword | string | <ul><li>required</li><li>minLength(8)</li><li>match</li><li>notOldPassword</li></ul> |      |
| 3 | confirm     | string | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul>                        |      |

### 6.2 Avatar

#### Description

In the settings view users can upload and change their avatar.  
If they don't want their avatar anymore they can delete it there too.

#### Form fields

| # | Name   | Type | Validation                               | Note |
|---|--------|------|------------------------------------------|------|
| 1 | avatar | file | <ul><li>required</li><li>image</li></ul> |      |

### 6.3 Email

#### Description

Users should be able to change their email address.
After changing the email address the user has to confirm it.  
The application sends a mail to the new email address with a code to confirm the email address.  
The code is generated by the application and stored in the database for 10 minutes.  
The URL of the link is ```/c-confirm/${id}```.

#### Form fields

| # | Name  | Type  | Validation                                                                       | Note |
|---|-------|-------|----------------------------------------------------------------------------------|------|
| 1 | email | email | <ul><li>required</li><li>email</li><li>emailUnique</li><li>notOldEmail</li></ul> |      |

### 6.4 Username

#### Description

Users should be able to change their username.  
Usernames must be unique and not already taken.

#### Form fields

| # | Name     | Type   | Validation                                                                                       | Note |
|---|----------|--------|--------------------------------------------------------------------------------------------------|------|
| 1 | username | string | <ul><li>required</li><li>minLength(5)</li><li>usernameAvailable</li><li>notOldUsername</li></ul> |      |

### 6.5 Delete account

#### Description

If a user doesn't want to participate anymore they can delete their account.  
This will delete all their data and they won't be able to log in anymore.  
The user has to enter their password to confirm the deletion.  
The application will send a mail to the user to notify the user about the deletion.

### 6.6 In-App notifications

#### Description

A user can toggle in-app notifications.  
There is a slide toggle for every type of notification.  
Additionally there is a slide toggle to turn of all in-app notifications.

### 6.7 Email notifications

A user can toggle email notifications.  
There is a slide toggle for every type of notification.  
Additionally there is a slide toggle to turn of all email notifications.

## 7 Results

| # | Name                               | Description                                          |
|---|------------------------------------|------------------------------------------------------|
| 1 | [Knockout tree](#71-knockout-tree) | A graph of the teams in knockout phase               |
| 2 | [Overview](#72-overview)           | Overview of the groups and knockout phases           |
| 3 | [Group view](#73-group-view)       | Overview of the games in one group                   |
| 4 | [Knockout view](#74-knockout-view) | Overview of the games in a knockout phase            |
| 5 | [Game history](#75-game-history)   | Games played by one of the teams in a knockout phase |

### 7.1 Knockout tree

#### Description

The same [knockout tree](#31-knockout-tree) as in the bet placement view.

### 7.2 Overview

#### Description

The same [overview](#32-overview) as in the bet placement view.  
But the percentages of the groups are showing the amount of games played.

### 7.3 Group view

#### Description

The same [group view](#33-group-view) as in the bet placement view.  
But instead of the bet placement form the results are displayed.

### 7.4 Knockout view

#### Description

The same [knockout view](#36-knockout-view) as in the bet placement view.
But instead of the bet placement form the results are displayed.

### 7.5 Game history

#### Description

In the knockout view the user can see the game history of both of the teams.
It is the same as in [bet placement](#37-game-history).

## 8 Notifications

| # | Name                           | Description                                  |
|---|--------------------------------|----------------------------------------------|
| 1 | [New results](#81-new-results) | When results have been uploaded              |
| 2 | [Open bet](#82-open-bet)       | If the user has any open bets                |
| 3 | [Descending](#83-descending)   | If the user has descended in the leaderboard |
| 4 | [Ascending](#84-ascending)     | If the user has ascended in the leaderboard  |

### 8.1 New results

#### Description

The user gets a notification when results have been uploaded.

#### Fields to display

| # | Name   | Note                          |
|---|--------|-------------------------------|
| 1 | game   | The game that has been played |
| 2 | date   | The date of the game          |
| 3 | team1  | The first team                |
| 4 | team2  | The second team               |
| 5 | result | The result of the game        |
| 6 | points | The points gained             |

### 8.2 Open bet

#### Description

The user gets a notification when they have any open bets that are due in 24 hours.

#### Fields to display

| # | Name  | Note                 |
|---|-------|----------------------|
| 1 | game  | The game that is due |
| 2 | date  | The date of the game |
| 3 | time  | The time of the game |
| 4 | team1 | The first team       |
| 5 | team2 | The second team      |

### 8.3 Descending

#### Description

The user gets a notification when they have descended in the leaderboard.

#### Fields to display

| # | Name        | Note                         |
|---|-------------|------------------------------|
| 1 | position    | The new position of the user |
| 2 | ranks moved | The ranks moved down         |

### 8.4 Ascending

#### Description

The user gets a notification when they have ascended in the leaderboard.

#### Fields to display

| # | Name        | Note                         |
|---|-------------|------------------------------|
| 1 | position    | The new position of the user |
| 2 | ranks moved | The ranks moved up           |

## 9 Email

| # | Name                                         | Description                                                                            |
|---|----------------------------------------------|----------------------------------------------------------------------------------------|
| 1 | [Confirmation email](#91-confirmation-email) | The email sent to the user after registration                                          |
| 2 | [Notifications](#92-notifications)           | If the user has any notifications                                                      |
| 3 | [Thank you](#93-thank-you)                   | Thank you email for playing                                                            |
| 4 | [Account deletion](#94-account-deletion)     | The email after the account has been deleted                                           |
| 5 | [Welcome email](#95-welcome-email)           | After the user payed the participation fee and has been confirmed by one of the admins |

### 9.1 Confirmation email

The application sends a mail to the user's email address with a code to confirm the email address.  
It contains a welcome message and a link to the application.  
The code is a random number 6 digits long.

#### Content

```html
TBD
```

### 9.2 Notifications

The application sends a mail to the user's email address with a notification.  
It contains the same information as the in-app notification.  
The email is sent to the user when the notification is created.  
The user can toggle email notifications in the [settings view](#67-email-notifications).

#### Content

```html
TBD
```

### 9.3 Thank you

The application sends an email to the user's email address after the tournament is finished.  
It contains a thank you message and invites them to play again in the EM 2028.

#### Content

```html
TBD
```

### 9.4 Account deletion

The application sends an email to the user's email address after their account has been deleted.

#### Content

```html
TBD
```

### 9.5 Welcome email

The application sends an email to the user's email address after their account has been confirmed.  
Accounts are confirmed when an admin receives the participation fee and accepts the account.

#### Content

```html
TBD
```

## 10 Dashboard

| # | Name                                            | Description                       |
|---|-------------------------------------------------|-----------------------------------|
| 1 | [Leaderboard preview](#101-leaderboard-preview) | A preview of the user's position  |
| 2 | [Personal statistics](#102-personal-statistics) | Personal statistics of the user   |
| 3 | [Global statistics](#103-global-statistics)     | Global statistics                 |
| 4 | [Open bets](#104-open-bets)                     | Open bets of the user             |
| 5 | [Bet history](#105-bet-history)                 | Bet history of the user           |
| 6 | [Summary](#106-summary)                         | Summary of the user's information |
| 7 | [Greeting](#107-greeting)                       | Greeting message                  |

### 10.1 Leaderboard preview

#### Description

The user's current position in the ranking as an entry like in the [leaderboard](#52-user-position).  
It also shows the positions of the users one rank higher and lower.  
That way the user sees how many points are required to move up or how many points he is away from moving down.

### 10.2 Personal statistics

#### Description

The application collects data of the user and displays them in the dashboard.

#### Types

| # | Name          | Note                                                               |
|---|---------------|--------------------------------------------------------------------|
| 1 | goals bet     | The amount of goals the user bet in total                          |
| 2 | correct games | The amount of games the user guessed correctly                     |
| 3 | jokers wasted | The amount of jokers used on a game that ended up scoring 0 points | 

### 10.3 Global statistics

#### Description

The dashboard shows data of all users together.

| # | Name          | Note                                                               |
|---|---------------|--------------------------------------------------------------------|
| 1 | points        | The points the users scored in total                               |
| 2 | correct games | The amount of games the user guessed correctly                     |
| 3 | jokers wasted | The amount of jokers used on a game that ended up scoring 0 points |

### 10.4 Open bets

#### Description

A list of bets that are still open.  
Bets that are due in less than 24 hours are prioritized and highlighted.  
If no open bets are available it shows the top 3 games.  
The games contain a link to the betting form of the game.  
There is a link below that leads the user to the betting view.

#### Fields to display

| # | Name  | Note                                   |
|---|-------|----------------------------------------|
| 1 | group | The group of the game                  |
| 2 | date  | The date of the game                   |
| 3 | time  | The time of the game                   |
| 4 | team1 | The first team of the game             |
| 5 | team2 | The second team of the game            |
| 6 | link  | A link to the betting form of the game |

### 10.5 Bet history

#### Description

The results of the latest games. A maximum of 4 games are displayed.  
If no history is available this section is hidden.  
The games contain a link to the result view of the game.  
There is a link below that leads the user to the result view.

#### Fields to display

| #  | Name        | Note                                   |
|----|-------------|----------------------------------------|
| 1  | group       | The group of the game                  |
| 2  | date        | The date of the game                   |
| 3  | points      | The points scored with the game        |
| 3  | joker       | The joker set on the game              |
| 4  | team1       | The first team of the game             |
| 5  | team2       | The second team of the game            |
| 6  | bet_team1   | The points the user bet on team 1      |
| 7  | bet_team2   | The points the user bet on team 2      |
| 8  | link        | A link to the betting form of the game |
| 9  | score_team1 | The points team 1 actually scored      |
| 10 | score_team2 | The points team 2 actually scored      |

### 10.6 Summary

#### Description

The summary is a small collection of information about the users bets.  
It is display as a series of information in the top right corner of the dashboard.

#### Fields to display

| # | Name     | Description                           | Note              |
|---|----------|---------------------------------------|-------------------|
| 1 | points   | The points the user scored            |                   |
| 2 | rank     | The users position in the leaderboard |                   |
| 3 | champion | The users world champion              | Only if available |

### 10.7 Greeting

#### Description

A greeting that automatically adjusts to the time of day.

#### Scenarios

| # | Name      | Start time | End time | Greeting                        |
|---|-----------|------------|----------|---------------------------------|
| 1 | Night     | 23:00      | 05:00    | Gute Nacht ***username***       |
| 2 | Morning   | 05:00      | 10:00    | Guten Morgen ***username***     |
| 3 | Day       | 10:00      | 14:00    | Hallo ***username***            |
| 4 | Afternoon | 14:00      | 18:00    | Guten Nachmittag ***username*** |
| 5 | Evening   | 18:00      | 23:00    | Guten Abend ***username***      |

## 11 Admin

This view is only accessible with the admin role.

| # | Name                                        | Description      |
|---|---------------------------------------------|------------------|
| 1 | [User management](#111-user-management)     | Accept new users |
| 2 | [Result management](#112-result-management) | Upload results   |

### 11.1 User management

#### Description

This view contains a list of users that have not been confirmed yet.  
The admin has the option to accept or deny the registration.  
In order to accept the user they have to pay the participation fee and one of the admins must know the user
personally.  
This is necessary because there is no way to legally execute this tournament without further administration and there is
no online payment service available to us at the moment.

#### Fields to display

| # | Name                 | Note                                   |
|---|----------------------|----------------------------------------|
| 1 | username             |                                        |
| 2 | firstname            |                                        |
| 3 | lastname             |                                        |
| 4 | time of registration | date and time                          |
| 5 | email                | as a link to manually contact the user |

### 11.2 Result management

#### Description

Same as [betting view](#3-bet-placement) but for uploading results.

## 12 Home

| # | Name                            | Description           |
|---|---------------------------------|-----------------------|
| 1 | [Greeting](#121-greeting)       | Greeting message      |
| 2 | [Rules](#122-rules)             | Rules of the game     |
| 3 | [How to play](#123-how-to-play) | How to play the game  |
| 4 | [About](#124-about)             | About the application |
| 5 | [Contact](#125-contact)         | Contact information   |

### 12.1 Greeting

#### Content

| # | Name             | Note            |
|---|------------------|-----------------|
| 1 | Heading          |                 |
| 2 | Image            |                 |
| 3 | Link for sign up | Jetzt mitmachen |

### 12.2 Rules

### 12.3 How to play

### 12.4 About

### 12.5 Contact
