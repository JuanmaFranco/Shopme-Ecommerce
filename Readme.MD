# Shopme E-Commerce Web Application

![ShopmeLogo](/images/shopme%20logo/ShopmeAdminBig.png)

Welcome to my personal project – a fully-fledged e-commerce web application that harnesses cutting-edge technologies to deliver an exceptional online shopping experience. Built using Java, Spring Boot, Hibernate, Thymeleaf, Bootstrap, jQuery, HTML, and RESTful Web Services, this project showcases the power of modern web development tools.

## Project Overview

This project serves as a comprehensive e-commerce platform that comprises both an Admin application and a Shopping application, catering to the needs of both administrators and customers. 

The **Admin application** encompasses various modules, including Users, Categories, Brands, Products, Customers, Shipping, Orders, Sales Report, Reviews, Questions, and Settings. 

On the other hand, the **Shopping application** offers features such as categories listing, product details, product search, shopping cart management, checkout, order tracking, and customer registration.

## Functional Requirements

### Key Actors

* #### [Backend](images/functional%20requirements/key%20actors/backend.png)

    - Admin
    - Assistant
    - Editor
    - Shipper
    - Salesperson
 
* #### [Frontend](/images/functional%20requirements/key%20actors/frontend.png)

    - Customer
    - Visitor

### Use Cases - Backend


* #### [Admin Use Cases](/images/functional%20requirements/use%20cases/backend/admin.png)

    * Manage Everything
      * Manage users
      * Manage settings

* #### [Assistant Use Cases](/images/functional%20requirements/use%20cases/backend/assistant.png)

    * Manage Questions
    * Manage Reviews

* #### [Editor Use Cases](/images/functional%20requirements/use%20cases/backend/editor.png)

    * Manage brands
    * Manage products
    * Manage categories
    * Manage articles
    * Manage menus

* #### [Salesperson Use Cases](/images/functional%20requirements/use%20cases/backend/salesperson.png)

    * Manage shipping rates
    * Manage sales report
    * Manage customers
    * View products
    * Update product price
    * Manage orders

* #### [Shipper Use Cases](/images/functional%20requirements/use%20cases/backend/shipper.png)
 
    * View orders
    * View products
    * Update order status

### Use Cases - Frontend

* #### [Customer Use Cases](/images/functional%20requirements/use%20cases/frontend/customer.png)

    * Post reviews
    * View products
    * View articles
    * Vote reviews
    * Vote questions
    * Manage addresses
    * Checkout
    * Manage shopping cart
    * View orders
    
* #### [Visitor Use Cases](/images/functional%20requirements/use%20cases/frontend/visitor.png)
 
    * Sign Up
    * View products
    * View articles

## [Technical Requirements](/images/technical%20requirements/technical-req.png)

- **Accesibility:**
  - Apps can be accessible from any devices connected to the internet: PC, Laptop, Tablet, Smartphone.

- **Availability:**
  - Users can access apps anytime, 24/7.

- **Security:**
  - Authentication is required for all users (except visitors).
  - Authorization is required in the admin control panel (editor, assistant, salesperson, shipper, etc.).
  - Passwords are not stored in the database.
  - Payment is secured.

- **Performance:**
  - Fast response time.
  - No request takes longer than 4 seconds.

- **Scalability:**
  - Apps can be scaled on demand and run well under loads.
  - Backend and frontend apps can be scaled separately.


## System Architecture

### Local Development

![Local Development](/images/system%20architecture/local_development.png)

On local development environment, I have everything running on a single computer: the **Admin Application** is running as a standalone JAR file. It is a Spring Boot application with embedded Tomcat server and the same for the **Shopping application** - a standalone JAR file. And they are connecting to a local instance of **MySQL database server**.

And for the static resources like the images of the products - are stored in the **local file system**.

### Production

![Production](/images/system%20architecture/production.png)

So **each application** will be running on its own Dynos which is lightweight container managed by **Heroku**.

For the database, I need to use a Heroku addon for MySQL, and for the product images that are uploaded by the end users, we need to use **Amazon S3 storage service** because Heroku doesn't allow us to store the files uploaded by the end user directly.


## Application Architecture

![Application Architecture](/images/application%20architecture/logical_layers.png)

Typically an application is structured into some logical layers. 

On top is the **view layer** that renders the user interface to the client. 
In the view layer, we can use Thymeleaf mixed with HTML code to render the pages to the client. 
And the actions of the users in the view layer will invoke the controller layer. 

In **controller layer**, we can use Spring MVC Controller, or REST controller or RESTful webservices. 

And the controller layer depends on the **service layer** that contains business classes. And the service layer depends on the **repository layer** that contains entity classes and repository interfaces.

And below is the **Spring Data JPA layer**. Spring Data JPA will use **Hibernate framework** as the implementation of JPA. Hibernate will use **JDBC driver** to communicate with the **underlying database**. 

The request from the client will come to the controller layer, the controller layer is responsible for handling the request from the clients. 

It will call the service layer to perform the business logic of the application, and then it will render the view that is returned to the client.