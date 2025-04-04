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

| # | Name     | Type   | Description             | Validation                                                                       |
|---|----------|--------|-------------------------|----------------------------------------------------------------------------------|
| 1 | password | string | The new password        | <ul style="color: red"><li>required</li><li>minLength(8)</li><li>match</li></ul> |
| 2 | confirm  | string | Repeat the new password | required<br>minLength(8)<br>match                                                |