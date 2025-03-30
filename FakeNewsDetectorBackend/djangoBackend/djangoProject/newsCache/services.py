from datetime import timedelta
from django.utils import timezone
from .models import NewsCache
from .newsApiClient import NewsApiClient


class NewsCacheService:
    CACHE_DURATION = timedelta(hours=48)

    @classmethod
    def get_news(cls, query=None, language=None, country=None, category=None, endpoint="everything"):
        now = timezone.now()
        cache = NewsCache.objects.filter(
            endpoint=endpoint,
            query=query,
            language=language,
            country=country,
            category=category,
            expires_at__gt=now
        ).first()

        if cache:
            return {"source": "cache", "data": cache.data}

        # Fetch fresh data
        if endpoint == "everything":
            data = NewsApiClient.fetch_everything(query, language)
        else:
            data = NewsApiClient.fetch_top_headlines(country, category, language)

        expires_at = now + cls.CACHE_DURATION

        NewsCache.objects.update_or_create(
            endpoint=endpoint,
            query=query,
            language=language,
            country=country,
            category=category,
            defaults={
                'data': data,
                'expires_at': expires_at,
            }
        )

        return {"source": "api", "data": data}
