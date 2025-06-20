describe('My First Test', () => {
  it('Visits the initial project page', () => {
    cy.visit('/')
    cy.get('[data-cy="wm-header-root"]').should('be.visible')
  })
})
