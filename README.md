# Classified Ads API

This project implements a single-city classified advertisements backend platform that models an online marketplace where users can list products across diverse categories, manage item availability through strict status transitions, and process persistent buyer message inquiries seamlessly. To run the project locally, open your terminal in the root directory and execute the single command: `./mvnw spring-boot:run`. All secure endpoints expect incoming HTTP requests to supply a valid authorization header containing the API key value: `demo-key-2026`.

Once an advertisement transitions to a SOLD or EXPIRED status, its data properties are permanently frozen from any future updates, renewals, or message collections, strictly rejecting subsequent modifications with an HTTP 409 Conflict.

## Example Verification Commands (cURL)

### 1. Create a New Listing (CREATE)
```bash
curl -X 'POST' \
  'http://localhost:8080/ads' \
  -H 'accept: */*' \
  -H 'X-API-Key: demo-key-2026' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "Bike",
  "categoryId": 1,
  "priceEur": 280.0,
  "city": "Skopje",
  "sellerName": "Bob",
  "description": "Electric Bike"
}'
```
### 2. Browse Active Ads Feed (READ)
```bash
curl -X 'GET' 'http://localhost:8080/ads?city=Skopje&size=20' \
  -H 'accept: */*' \
  -H 'X-API-Key: demo-key-2026'
```
### 3. Edit Active Ad Attributes (UPDATE)
```bash

curl -X 'PATCH' \
  'http://localhost:8080/ads/1' \
  -H 'accept: */*' \
  -H 'X-API-Key: demo-key-2026' \
  -H 'Content-Type: application/json' \
  -d '{
  "title": "Bike",
  "categoryId": 1,
  "priceEur": 380.0,
  "city": "Skopje",
  "sellerName": "Bob",
  "description": "Price updated"
}'
```
### 4. Remove a Listing from Public View (DELETE)
```Bash

curl -X 'DELETE' 'http://localhost:8080/ads/1' \
  -H 'accept: */*' \
  -H 'X-API-Key: demo-key-2026'
```
### 5. Review Impending System Deadlines (REPORT)
```Bash

curl -X 'GET' 'http://localhost:8080/reports/expiring-soon' \
  -H 'accept: */*' \
  -H 'X-API-Key: demo-key-2026'
```