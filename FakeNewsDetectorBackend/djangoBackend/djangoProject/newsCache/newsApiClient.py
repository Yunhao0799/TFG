import requests
from django.conf import settings


class NewsApiClient:

    EVERYTHING_URL = "https://newsapi.org/v2/everything"
    TOP_HEADLINES_URL = "https://newsapi.org/v2/top-headlines"

    @staticmethod
    def fetch_everything(query, language=None):
        params = {
            "q": query,
            "apiKey": settings.NEWS_API_KEY,
            "language": language,
            "pageSize": 30,
        }
        response = requests.get(NewsApiClient.EVERYTHING_URL, params=params)
        response.raise_for_status()
        return response.json()

    @staticmethod
    def fetch_top_headlines(country=None, category=None, language=None):
        params = {
            "apiKey": settings.NEWS_API_KEY,
            "country": country,
            "category": category,
            "language": language,
            "pageSize": 30,
        }
        response = requests.get(NewsApiClient.TOP_HEADLINES_URL, params=params)
        response.raise_for_status()
        return response.json()