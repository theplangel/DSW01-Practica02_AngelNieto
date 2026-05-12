describe('Login flow', () => {
  it('allows a user to login with valid credentials', () => {
    cy.visit('/')
    cy.get('#username').type('user')
    cy.get('#password').type('pass')
    cy.get('#login-button').click()
    cy.contains('Welcome, user').should('exist')
  })
})
