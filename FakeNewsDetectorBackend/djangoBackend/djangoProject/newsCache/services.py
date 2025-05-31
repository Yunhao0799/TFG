from datetime import timedelta

from django.db.models import Case, When
from django.utils import timezone
from .models import NewsCache, NewsArticle
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
            # Extract articles from cached data and match them in the DB
            urls = [article['url'] for article in cache.data.get("articles", [])]
            preserved_order = Case(
                *[When(url=url, then=pos) for pos, url in enumerate(urls)]
            )
            return NewsArticle.objects.filter(url__in=urls).order_by(preserved_order)

        # Fetch fresh data from NewsAPI
        if endpoint == "everything":
            data = NewsApiClient.fetch_everything(query, language)
        else:
            data = NewsApiClient.fetch_top_headlines(country, category, language)

        # Save cache
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

        # Save each article into NewsArticle
        articles = data.get("articles", [])
        article_objects = []
        for art in articles:
            if not art.get("url"):  # skip if no unique URL
                continue
            article, _ = NewsArticle.objects.get_or_create(
                url=art["url"],
                defaults={
                    "title": art.get("title", ""),
                    "description": art.get("description", ""),
                    "source_name": art.get("source", {}).get("name", ""),
                    "published_at": art.get("publishedAt"),
                    "urlToImage": art.get("urlToImage") or None,
                    "query": query,
                }
            )
            article_objects.append(article)

        return article_objects
