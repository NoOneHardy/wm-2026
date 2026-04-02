describe('Navigation', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  describe('without authentication', () => {
    it('should display the navigation bar', () => {
      cy.get('nav').should('be.visible')
    })

    it('should navigate to the ranking page', () => {
      cy.get('nav a').contains('Rangliste').click()
      cy.url().should('equal', 'http://localhost:26000/leaderboard')
      cy.get('h1').contains('Rangliste').should('be.visible')
    })

    it('should navigate to the login page', () => {
      cy.get('a').contains('Anmelden').click()
      cy.url().should('equal', 'http://localhost:26000/login')
      cy.get('h2').contains('Anmelden').should('be.visible')
    })

    it('should navigate to the signup page', () => {
      cy.get('a').contains('Registrieren').click()
      cy.url().should('equal', 'http://localhost:26000/signup')
      cy.get('h2').contains('Konto erstellen').should('be.visible')
    })

    it('should navigate home with the home link', () => {
      cy.visit('/leaderboard')
      cy.get('nav a').contains('Dashboard').should('not.exist')
      cy.get('nav a').contains('Home').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should navigate home with the logo', () => {
      cy.visit('/leaderboard')
      cy.get('[data-cy="wm-header-root"]').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should not allow navigation to the admin page', () => {
      cy.get('nav a').contains('Teilnehmer').should('not.exist')
      cy.get('nav a').contains('Resultate').should('not.exist')
      cy.visit('/admin/users')
      cy.url().should('equal', 'http://localhost:26000/')
      cy.visit('/admin/results')
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should redirect to login when navigating to the bets page', () => {
      cy.get('nav a').contains('Tippschein').should('not.exist')
      cy.visit('/bets')
      cy.url().should('equal', 'http://localhost:26000/login')
    })
  })

  describe('with authentication', () => {
    beforeEach(() => {
      cy.visit('/login')
      cy.get('input[formControlName="username"]').type('NoOneHardy')
      cy.get('input[formControlName="password"]').type('admin123')
      cy.get('button[type="submit"]').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should display the navigation bar', () => {
      cy.get('nav').should('be.visible')
    })

    it('should navigate to the ranking page', () => {
      cy.get('nav a').contains('Rangliste').click()
      cy.url().should('equal', 'http://localhost:26000/leaderboard')
      cy.get('h1').contains('Rangliste').should('be.visible')
    })

    it('should not navigate to the login page', () => {
      cy.get('a').contains('Anmelden').should('not.exist')
      cy.visit('/login')
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should not navigate to the signup page', () => {
      cy.get('a').contains('Registrieren').should('not.exist')
      cy.visit('/signup')
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should navigate to dashboard with the dashboard link', () => {
      cy.visit('/leaderboard')
      cy.get('nav a').contains('Home').should('not.exist')
      cy.get('nav a').contains('Dashboard').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should navigate to dashboard with the logo', () => {
      cy.visit('/leaderboard')
      cy.get('[data-cy="wm-header-root"]').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should not allow navigation to the admin page', () => {
      cy.visit('/admin/users')
      cy.url().should('equal', 'http://localhost:26000/')
      cy.visit('/admin/results')
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should navigate to the bets page', () => {
      cy.get('nav a').contains('Tippschein').click()
      cy.url().should('equal', 'http://localhost:26000/bets')
    })

    it('should display the user profile link', () => {
      cy.get('button').contains('NoOneHardy').should('be.visible')
    })
  })

  describe('with admin authentication', () => {
    beforeEach(() => {
      cy.visit('/login')
      cy.get('input[formControlName="username"]').type('No1Hardy')
      cy.get('input[formControlName="password"]').type('admin123')
      cy.get('button[type="submit"]').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should display the navigation bar', () => {
      cy.get('nav').should('be.visible')
    })

    it('should navigate to the ranking page', () => {
      cy.get('nav a').contains('Rangliste').click()
      cy.url().should('equal', 'http://localhost:26000/leaderboard')
      cy.get('h1').contains('Rangliste').should('be.visible')
    })

    it('should not navigate to the login page', () => {
      cy.get('a').contains('Anmelden').should('not.exist')
      cy.visit('/login')
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should not navigate to the signup page', () => {
      cy.get('a').contains('Registrieren').should('not.exist')
      cy.visit('/signup')
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should navigate to dashboard with the dashboard link', () => {
      cy.visit('/leaderboard')
      cy.get('nav a').contains('Home').should('not.exist')
      cy.get('nav a').contains('Dashboard').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should navigate to dashboard with the logo', () => {
      cy.visit('/leaderboard')
      cy.get('[data-cy="wm-header-root"]').click()
      cy.url().should('equal', 'http://localhost:26000/')
    })

    it('should allow navigation to the user management page', () => {
      cy.visit('/admin/users')
      cy.url().should('equal', 'http://localhost:26000/admin/users')
      cy.visit('/')
      cy.get('nav a').contains('Teilnehmer').click()
      cy.url().should('equal', 'http://localhost:26000/admin/users')
    })

    it('should allow navigation to the result management page', () => {
      cy.visit('/admin/results')
      cy.url().should('equal', 'http://localhost:26000/admin/results')
      cy.visit('/')
      cy.get('nav a').contains('Resultate').click()
      cy.url().should('equal', 'http://localhost:26000/admin/results')
    })

    it('should navigate to the bets page', () => {
      cy.get('nav a').contains('Tippschein').click()
      cy.url().should('equal', 'http://localhost:26000/bets')
    })

    it('should display the user profile link', () => {
      cy.get('button').contains('No1Hardy').should('be.visible')
    })
  })
})
