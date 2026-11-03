# Run Club platform

A microservices-based application that connects running clubs with runners. Clubs can publish running events with route information, runners can register for events, and AI-powered recommendations help runners discover clubs that match their profiles.

Developed as a university project for Development of Scalable Production ICT Systems (Razvoj skalabilnog produkcijskog IKT sustava) at the Faculty of Science, University of Zagreb, in collaboration with Infobip.
Key features
- Running clubs and events: Manage running clubs, publish events, and allow runners to sign up.
- Route information: Integrate with OpenRouteService to calculate route distance and elevation gain.
- AI-powered recommendations: Use generative AI to recommend running clubs based on a runner's profile.
- Route caching: Use Redis to cache route-related results.
- Monitoring: Expose application health and metrics for monitoring with Prometheus and Grafana.
Technology stack
Area	Technologies
Backend	Java 25, Spring Boot 4, REST APIs
AI integration	Spring AI, OpenAI-compatible API
Database	PostgreSQL, Spring Data JPA
Caching	Redis
Route data	OpenRouteService API
Infrastructure	Docker, Docker Compose, HAProxy
Observability	Spring Boot Actuator, Micrometer, Prometheus, Grafana
Build	Maven


Architecture
The application is organized into two Spring Boot services:
- club-service handles clubs, runners, events, registrations, and AI-assisted club recommendations. It uses PostgreSQL for persistent storage.
- route-service integrates with OpenRouteService to calculate route information and uses Redis for caching.
HAProxy provides a shared API entry point and forwards requests beginning with /routes to route-service; other requests go to club-service. Prometheus and Grafana provide monitoring capabilities.
                   Client / API consumer
                            |
                      HAProxy :8080
                       /          \
               club-service     route-service
                    |                |
               PostgreSQL          Redis
                    |                |
             AI API integration   OpenRouteService API

              Metrics -> Prometheus -> Grafana
Getting started
Prerequisites
- Docker and Docker Compose
- An OpenRouteService API key
- An API key and endpoint compatible with the configured AI integration
1. Clone the repository
git clone https://github.com/Marinaa959/pmf-rspis-2026-projekt-runclub.git
cd pmf-rspis-2026-projekt-runclub
2. Configure environment variables
Copy the provided example file:
cp .env.example .env
Edit .env and provide your own values for:
- POSTGRES_PASSWORD: PostgreSQL password
- ORS_API_KEY: OpenRouteService API key
- OPENAI_API_KEY: API key for AI-powered recommendations
- OPENAI_BASE_URL: the endpoint for your OpenAI-compatible API provider
- GRAFANA_ADMIN_PASSWORD: a secure Grafana administrator password
The example AI endpoint is provider-specific. Replace it with an endpoint supported by your credentials. Do not commit .env or real API keys to Git.
3. Build and start the services
docker compose up --build -d
Inspect service status and logs:
docker compose ps
docker compose logs -f
To stop the services:
docker compose down
Note: These commands follow the repository's Docker Compose configuration. A complete clean-machine startup has not been independently verified as part of this README update.

Local service endpoints
As configured in compose.yaml and haproxy.cfg:
Component	Address
API gateway	http://localhost:8080
HAProxy statistics	http://localhost:8404/stats
Prometheus	http://localhost:9090
Grafana	http://localhost:3000


These are local development endpoints, not public deployments. The HAProxy statistics page and monitoring services should be protected before any public deployment.
Repository structure
club-service/       Club, runner, event, registration and AI functionality
route-service/      Route calculation and caching
haproxy/            API routing configuration
prometheus/         Metrics collection configuration
grafana/            Dashboard and provisioning configuration
compose.yaml        Multi-container setup
.env.example        Environment variable template
pom.xml             Parent Maven project
Project context
This is an academic project developed to explore scalable backend systems, microservice communication, external API integration, caching, containerization, and observability in a practical application.
Notes
- The project requires external API credentials for route calculations and AI recommendations.
- The included configurations are intended as a starting point for local development and should be reviewed and hardened before production use.
- No live demo or screenshots are currently included in this repository.
