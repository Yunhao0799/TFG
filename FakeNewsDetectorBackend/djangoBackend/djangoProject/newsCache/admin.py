from django.contrib import admin
from .models import NewsCache


@admin.register(NewsCache)
class NewsCacheAdmin(admin.ModelAdmin):
    list_display = ("query", "language", "country", "created_at", "expires_at")
    search_fields = ("query",)
