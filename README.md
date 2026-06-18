# VTaxer Core — Tax Filing Backend

VTaxer Core is a Spring Boot backend service that powers an online tax filing platform. It provides a comprehensive REST API for managing user tax returns, document handling, and administrative workflows. The platform enables individual taxpayers to submit income documents, claim deductions, manage dependents, and track their tax filing status through a secure, multi-tenant architecture with role-based access control.

The system handles the full lifecycle of tax preparation — from user registration and document upload through to tax calculation, return drafting, and communication between tax preparers and clients. Built with MongoDB for flexible document storage and Spring Security for authentication, it supports both customer-facing and admin-facing operations.

## Features & Modules

| Module | Description |
|--------|-------------|
| **User Management** | Registration, authentication, profile management via Spring Security |
| **Tax Document Processing** | Upload and manage W-2s, 1099s, and other tax documents |
| **Income Documents** | Track multiple income sources per filing year |
| **Deduction Documents** | Manage deductible expenses and supporting documentation |
| **Dependent Management** | Add/remove dependents for tax filing purposes |
| **Tax Details & Calculation** | Store computed tax details and return summaries |
| **Revenue Tracking** | Track business revenue for self-employed filers |
| **Disclosure Documents** | Handle required financial disclosure filings |
| **Admin Dashboard** | Administrative operations for tax preparers |
| **Communication System** | Messaging between clients and tax preparers |
| **File Records** | Generic file storage and retrieval system |
| **Health Monitoring** | Health check endpoint for deployment monitoring |

## Tech Stack

- Java 11+ / Spring Boot
- MongoDB (document store)
- Spring Security (auth)
- Docker / Docker Compose (deployment)
- Maven (build)
