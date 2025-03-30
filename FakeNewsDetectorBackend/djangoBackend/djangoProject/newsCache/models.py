from django.db import models
from django.utils import timezone


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
