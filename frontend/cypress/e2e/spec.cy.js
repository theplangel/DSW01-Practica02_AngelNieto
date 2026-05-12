describe('Homepage', () => {
  it('loads the static index page', () => {
    cy.visit('/')
    cy.contains('crud-empleados frontend').should('exist')
  })
})
