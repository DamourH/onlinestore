import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ProductOrder e2e test', () => {
  const productOrderPageUrl = '/product-order';
  const productOrderPageUrlPattern = new RegExp('/product-order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productOrderSample = { placedDate: '2025-08-01T02:33:07.521Z', status: 'PENDING', code: 'um hopelessly' };

  let productOrder;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-orders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productOrder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-orders/${productOrder.id}`,
      }).then(() => {
        productOrder = undefined;
      });
    }
  });

  it('ProductOrders menu should load ProductOrders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductOrder').should('exist');
    cy.url().should('match', productOrderPageUrlPattern);
  });

  describe('ProductOrder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productOrderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductOrder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-order/new$'));
        cy.getEntityCreateUpdateHeading('ProductOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productOrderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-orders',
          body: productOrderSample,
        }).then(({ body }) => {
          productOrder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-orders?page=0&size=20>; rel="last",<http://localhost/api/product-orders?page=0&size=20>; rel="first"',
              },
              body: [productOrder],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productOrderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductOrder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productOrder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productOrderPageUrlPattern);
      });

      it('edit button click should load edit ProductOrder page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productOrderPageUrlPattern);
      });

      it('edit button click should load edit ProductOrder page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductOrder');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productOrderPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductOrder', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productOrder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productOrderPageUrlPattern);

        productOrder = undefined;
      });
    });
  });

  describe('new ProductOrder page', () => {
    beforeEach(() => {
      cy.visit(productOrderPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductOrder');
    });

    it('should create an instance of ProductOrder', () => {
      cy.get(`[data-cy="placedDate"]`).type('2025-07-31T19:51');
      cy.get(`[data-cy="placedDate"]`).blur();
      cy.get(`[data-cy="placedDate"]`).should('have.value', '2025-07-31T19:51');

      cy.get(`[data-cy="status"]`).select('PENDING');

      cy.get(`[data-cy="code"]`).type('though potentially the');
      cy.get(`[data-cy="code"]`).should('have.value', 'though potentially the');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productOrder = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productOrderPageUrlPattern);
    });
  });
});
