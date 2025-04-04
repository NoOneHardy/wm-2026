# Specification

## Table of Contents

| #  | Name          | Description         |
|----|---------------|---------------------|
| 1  | Login         | User login          |
| 2  | Signup        | User signup         |
| 3  | Bet placement | User places a bet   |
| 4  | Bet history   | User bet history    |
| 5  | Leaderboard   | Overall ranking     |
| 6  | Profile       | User profile        |
| 7  | Settings      | User settings       |
| 8  | Results       | Game results        |
| 9  | Notifications | User notifications  |
| 10 | Email         | Email notifications |
| 11 | Dashboard     | User dashboard      |
| 12 | Admin         | Admin panel         |

## 1. Login

| # | Name            | Description                                            |
|---|-----------------|--------------------------------------------------------|
| 1 | Login Form      | A form for users to enter their credentials to log in. |
| 2 | Forgot Password | A link to reset the password if the user forgets it.   |
| 3 | User Menu       | A menu in the header with user options                 |
| 4 | Header          | A header with the app logo and navigation links        |

### 1.1 Login Form

#### Description

Redirects to the dashboard if the user is logged in.

#### Form fields

| # | Name     | Type   | Description         | Validation |
|---|----------|--------|---------------------|------------|
| 1 | username | string | The user's username | required   |
| 2 | password | string | The user's password | required   |

### 1.2 Forgot Password

#### Description

The application sends a mail to the user's email address with a link to reset the password.  
It generates an ID and stores it in the database for 10 minutes together with the user's IP.  
The URL of the link is ```/reset/${id}```.  
If the ID matches with an entry in the database, the user can reset the password using the password reset form.

#### Form fields

| # | Name     | Type   | Description             | Validation                                                    |
|---|----------|--------|-------------------------|---------------------------------------------------------------|
| 1 | password | string | The new password        | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul> |
| 2 | confirm  | string | Repeat the new password | <ul><li>required</li><li>minLength(8)</li><li>match</li></ul> |

<h3 id="user-menu">1.3 User Menu</h3>

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

| # | Name          | Route                                    | Condition     | Important |
|---|---------------|------------------------------------------|---------------|-----------|
| 1 | Home          | ```/```                                  | not logged in |           |
| 2 | Dashboard     | ```/```                                  | logged in     |           |
| 3 | Leaderboard   | ```/leaderboard```                       |               |           |
| 4 | Results       | ```/results```                           |               |           |
| 5 | Bets          | ```/bets```                              | logged in     |           |
| 6 | Notifications | Opens Context menu with notifications    | logged in     | Icon      |
| 7 | Login         | ```/login```                             | not logged in |           |
| 8 | Signup        | ```/signup```                            | not logged in |           |
| 9 | Username      | Opens <a href="#user-menu">User Menu</a> | logged in     |           |

## Signup

| # | Name                 | Description                                             |
|---|----------------------|---------------------------------------------------------|
| 1 | Signup Form          | A form for users to enter their credentials to sign up. |
| 2 | Terms and Conditions | A link to the terms and conditions of the application.  |
| 3 | Redirect to Login    | A link to redirect the user to the login page.          |

### 2.1 Signup Form

#### Description

Automatically logs in user after signup and redirects to the dashboard.

#### Form fields

| # | Name     | Type   | Description         | Validation                                                             |
|---|----------|--------|---------------------|------------------------------------------------------------------------|
| 1 | username | string | The user's username | <ul><li>required</li><li>minLength(5)</li><li>usernameUnique</li></ul> |
| 2 | email    | email  | The user's email    | <ul><li>required</li><li>email<li>emailUnique</li></ul>                |


