# Telegram Weather Bot

A simple weather bot built with Java and Spring Boot that allows users to get real-time weather information for a specified city or country by interacting with the bot on Telegram. This bot integrates with the OpenWeatherMap API to retrieve and send weather data.

## Features
- Responds to `/start` and `/help` commands with information on usage.
- Provides current weather details when a user sends a city or country name.
- Error handling for non-existent cities or API issues.

## Getting Started

### Prerequisites
- **Java 17** or higher
- **Spring Boot** with **Maven**
- **OpenWeatherMap API Key** (sign up at https://home.openweathermap.org/users/sign_up)
- **Telegram Bot Token** (create a bot with BotFather on Telegram and get the token)

### Dependencies
The project uses the following dependencies:
- `spring-boot-starter`
- `spring-boot-starter-web`
- `spring-boot-starter-logging`
- `spring-boot-starter-validation`
- `spring-boot-configuration-processor`
- `com.fasterxml.jackson.core` for JSON parsing
- `org.telegram` for Telegram bot API integration

### Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/telegram-weather-bot.git
    cd telegram-weather-bot
    ```

2. **Configure API Keys:**
   Open the `src/main/resources/application.properties` file and add your API keys:
   ```properties
   bot.token=YOUR_TELEGRAM_BOT_TOKEN
   bot.username=YOUR_TELEGRAM_BOT_USERNAME
   weather.api.key=YOUR_OPENWEATHERMAP_API_KEY
   weather.api.url=http://api.openweathermap.org/data/2.5/weather
