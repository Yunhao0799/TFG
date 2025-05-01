from django.db import models
from django.utils import timezone

from users.models import CustomUser


class NewsCache(models.Model):
    ENDPOINT_CHOICES = [
        ("everything", "Everything"),
        ("top-headlines", "Top Headlines"),
    ]

    endpoint = models.CharField(
        max_length=20,
        choices=ENDPOINT_CHOICES,
        default="everything"
    )
    query = models.CharField(max_length=255, blank=True, null=True)  # Top headlines may not need query
    language = models.CharField(max_length=10, blank=True, null=True)
    country = models.CharField(max_length=10, blank=True, null=True)
    category = models.CharField(max_length=50, blank=True, null=True)
    data = models.JSONField()
    created_at = models.DateTimeField(auto_now_add=True)
    expires_at = models.DateTimeField()

    class Meta:
        unique_together = ("endpoint", "query", "language", "country", "category")

    def is_expired(self):
        return timezone.now() > self.expires_at


class NewsArticle(models.Model):
    title = models.CharField(max_length=512)
    description = models.TextField(blank=True, null=True)
    url = models.URLField(unique=True)  # Use URL as unique identifier
    urlToImage = models.URLField(max_length=1024, blank=True, null=True)
    source_name = models.CharField(max_length=255)
    published_at = models.DateTimeField()
    query = models.CharField(max_length=100, blank=True, null=True)
    fetched_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.title


class FavoriteNews(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    article = models.ForeignKey(NewsArticle, on_delete=models.CASCADE)
    saved_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        unique_together = ('user', 'article')
        verbose_name = 'Favorite News'
        verbose_name_plural = 'Favorite News'

    def __str__(self):
        return f"{self.user.username} liked {self.article.title}"
